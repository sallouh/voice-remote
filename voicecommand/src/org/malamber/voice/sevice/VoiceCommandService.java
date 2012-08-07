package org.malamber.voice.sevice;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.malamber.logging.L;
import org.malamber.voice.R;
import org.malamber.voice.VoiceCommand;
import org.malamber.voice.VoiceCommandApplication;
import org.malamber.voice.activities.SpeechResultDialog;
import org.malamber.voice.aidl.IVoiceCommandService;
import org.malamber.voice.aidl.SpeechResultsListener;
import org.malamber.voice.commands.Command;
import org.malamber.voice.commands.VoicePattern;
import org.malamber.voice.commands.VoicePatternRunnable;
import org.malamber.voice.google.GoogleRecognizer;

import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.RemoteControlClient;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.view.KeyEvent;

public class VoiceCommandService extends Service {

	private Intent recognizerIntent = new Intent();
	private List<ListenerPair> listeners = new ArrayList<ListenerPair>();
	private Hashtable<String, PatternPair> patterns = new Hashtable<String, PatternPair>();
	private ArrayList<VoicePattern> servicePatterns = new ArrayList<VoicePattern>();
	private VoiceCommandApplication app;
	private Context context;
	private SharedPreferences prefs;
	private AudioManager audio;
	ComponentName mRemoteControlResponder;

	private GoogleRecognizer recognizer;
	private boolean enabled = true;

	ComponentName receiver;
	EventReceiver mReceiver;

	void init() {
		receiver = new ComponentName(getPackageName(),
				EventReceiver.class.getName());

		AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
		am.registerMediaButtonEventReceiver(receiver);
		am.requestAudioFocus(audioFocus, AudioManager.STREAM_VOICE_CALL,
				AudioManager.AUDIOFOCUS_GAIN);

		am.registerRemoteControlClient(getRemoteControlClient());

		IntentFilter audioStateFilter = new IntentFilter();
		audioStateFilter.addAction(BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED);
		mReceiver = new EventReceiver();
		registerReceiver(mReceiver, audioStateFilter);

		BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
		if (ba.getState() == BluetoothAdapter.STATE_ON) {
			L.w(this, "Bluetooth is off");
			return;
		}
		if (ba.getProfileConnectionState(BluetoothProfile.HEADSET) == BluetoothProfile.STATE_CONNECTED) {
			L.d(this, "Bluetooth STATE_CONNECTEDx");
		}
		boolean bh = ba.getProfileProxy(this, mBluetoothHeadsetServiceListener,
				BluetoothProfile.HEADSET);
		if (bh) {
			L.d(this, "profile success");
		}
	}

	RemoteControlClient getRemoteControlClient() {
		Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
		mediaButtonIntent.setComponent(receiver);
		PendingIntent mediaPendingIntent = PendingIntent.getBroadcast(
				getApplicationContext(), 0, mediaButtonIntent, 0);
		// create and register the remote control client
		RemoteControlClient myRemoteControlClient = new RemoteControlClient(
				mediaPendingIntent);
		return myRemoteControlClient;
	}

	private OnAudioFocusChangeListener audioFocus = new OnAudioFocusChangeListener() {

		@Override
		public void onAudioFocusChange(int focusChange) {
			L.d(this, "onAudioFocusChange");
			switch (focusChange)
			{
				case AudioManager.AUDIOFOCUS_GAIN:
					L.d(this, "AUDIOFOCUS_GAIN");
				break;
				case AudioManager.AUDIOFOCUS_LOSS:
					L.d(this, "AUDIOFOCUS_LOSS");
				break;
				case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
				case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
			}
		}
	};
	
	private BluetoothHeadset.ServiceListener mBluetoothHeadsetServiceListener = new BluetoothHeadset.ServiceListener() {

		@Override
		public void onServiceConnected(int profile, BluetoothProfile proxy) {
			L.d(this, "onServiceConnected");
		}

		@Override
		public void onServiceDisconnected(int profile) {
			L.d(this, "onServiceDisconnected");
		}
	};

	public void onCreate() {
		super.onCreate();
		L.Tag = "voice";

		L.i(this, "Starting voice service");

		try {
			context = getApplicationContext();
			app = (VoiceCommandApplication) this.getApplication();
			app.setContext(context);

			prefs = PreferenceManager.getDefaultSharedPreferences(this);

			recognizerIntent
					.putExtra(
							RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,
							1000 * 4);
			recognizer = new GoogleRecognizer(context, recognizerIntent, this);

			// audio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
			init();

			housekeepingTimer.schedule(housekeepingTask, 15 * 60 * 1000);

			registerMediaButton();

			registerReceiver();

			initServicePatterns();

			VoiceCommand.showNotify(context, VoiceCommand.NOTIFY_OFF,
					R.drawable.mic_off);

		} catch (Exception e) {
			L.ex(this, "onCreate", e);
		}
	}

	private void initServicePatterns() {
		servicePatterns.add(new VoicePattern("microphone.off",
				new VoicePatternRunnable() {

					@Override
					public void Run(String result) {
						VoiceCommand.showNotify(context, "Microphone off",
								R.drawable.mic_off);
						recognizer.stopListening();
					}
				}));
	}

	public void RecognizerStateChange(int state, Object o) {
		switch (state)
		{
			case VoiceCommand.STATE_DONE:
				VoiceCommand.showNotify(context, "Microphone off",
						R.drawable.mic_off);
			break;
			case VoiceCommand.STATE_ERROR:
				VoiceCommand.showNotify(context, "Microphone off",
						R.drawable.mic_off);
				VoiceCommand.PlayError(context);
			break;
		}
		broadcastStateChange(state, o);
	}

	private boolean checkService(String s) {
		for (VoicePattern p : servicePatterns) {
			if (!p.isMatch(s))
				continue;

			L.d(this, "found match in servicePatterns");
			p.Run(s);
			return true;
		}
		return false;
	}

	private boolean checkPatterns(String s) {
		for (PatternPair p : patterns.values()) {
			Matcher m = p.pattern.matcher(s);
			if (m.find()) {
				send(p.pattern.pattern(), s);
				return true;
			}
		}
		return false;
	}

	public void RecognizerResults(ArrayList<String> al) {
		L.i(this, "speechResults: " + al.get(0));

		String msg = "";

		try {
			long stime = System.currentTimeMillis();

			int i = 0;
			for (String ss : al) {
				msg += ss + ":";
				String s = ss;

				L.d(this, "checking servicePatterns");
				if (checkService(s))
					break;

				if (checkPatterns(s))
					break;

				if (openCommand(s, al) || ++i >= 4)
					break;

				if (al.size() > 1)
					openResultsDialog(al);
			}

			long diff = System.currentTimeMillis() - stime;
			Date d = new Date(diff);
			L.d(this,
					"RecognizerResults: time=" + d.getMinutes() + ":"
							+ d.getSeconds());
			broadcastStateChange(VoiceCommand.STATE_RESULTS, msg);

		} catch (Exception e) {
			L.ex(this, "speechResults", e);
		}
	}

	private void openResultsDialog(ArrayList<String> al) {
		Intent i = new Intent(this.context, SpeechResultDialog.class);
		i.putStringArrayListExtra("results", al);
		context.startActivity(i);
	}

	private void send(String p, String s) {

		List<ListenerPair> al = listeners;
		for (ListenerPair l : al) {
			try {

				l.listener.handSpeechResults(p, s);
			} catch (RemoteException e) {
				listeners.remove(l);
			}
		}
	}

	private boolean openCommand(String s, ArrayList<String> al) {
		Command vc = app.lookupCommand(s);
		if (vc != null) {
			L.d(this, "Running command " + vc.getClass().getName());
			VoicePattern vp = vc.getMatchingPattern(s);
			vp.Run(s);
			// vc.Run(vc., al.get(0));

			return true;
		}

		return false;
	}

	private BroadcastReceiver mediaButtonReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context c, Intent i) {
			try {
				L.d(this, "Broadcast received");

				mediaButtonReceived(i);
			} catch (Exception e) {
				L.ex(this, "onReceive", e);
			}
		}
	};

	long keydownStart = 0;
	boolean started;

	private void mediaButtonReceived(Intent intent) {
		int keycode = intent.getIntExtra("key", 0);
		int action = intent.getIntExtra("action", 0);
		long eventtime = intent.getLongExtra("eventtime", 0);

		L.d(this, keycode + " " + action + " " + eventtime);

		if (keycode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
				|| keycode == KeyEvent.KEYCODE_HEADSETHOOK) {
			if (action == KeyEvent.ACTION_DOWN) {
				keydownStart = eventtime;

			}
			if (action == KeyEvent.ACTION_UP) {
				long diff;
				if (keydownStart > 0) {
					diff = eventtime - keydownStart;
					L.d(this, "keydownStart=" + keydownStart + " diff=" + diff);
				} else {
					L.d(this, "KeyEvent.ACTION_UP");

					if (started) {
						L.i(this, "Speech off");
						VoiceCommand.showNotify(context,
								"Speech recognition off", R.drawable.mic_off);
						broadcastStateChange(VoiceCommand.STATE_DONE);
						recognizer.stopListening();
						this.started = false;
					} else {
						if (!enabled) {
							VoiceCommand.showNotify(context,
									VoiceCommand.NOTIFY_SLEEP,
									R.drawable.mic_on);
							enabled = true;

						} else {
							L.i(this, "Speech on");
							VoiceCommand.showNotify(context,
									"Speech recognition on", R.drawable.mic_on);
							startUserIndicator();
							recognizer.startListening();
							this.started = true;
						}
					}
				}

				keydownStart = 0;
			}
		}
	}

	private void broadcastStateChange(int state) {
		Intent intent = new Intent(VoiceCommand.VOICE_STATECHANGE);
		intent.putExtra("state", state);
		context.sendBroadcast(intent);
	}

	private void broadcastStateChange(int state, Object o) {
		Intent intent = new Intent(VoiceCommand.VOICE_STATECHANGE);
		intent.putExtra("state", state);
		if (state == VoiceCommand.STATE_ERROR)
			intent.putExtra("error", (String) o);
		else intent.putExtra("result", (String) o);
		context.sendBroadcast(intent);
	}

	private void startUserIndicator() {
		// Intent i = new Intent(this, RecognizerActivity.class);
		// i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// context.startActivity(i);
	}

	private void registerReceiver() {
		L.d(this, "registerReceiver");

		IntentFilter filter = new IntentFilter("org.malambber.VOICE_COMMAND");
		this.registerReceiver(mediaButtonReceiver, filter);
	}

	private void registerMediaButton() {
		L.d(this, "registerMediaButton");
		mRemoteControlResponder = new ComponentName(getPackageName(),
				EventReceiver.class.getName());
		audio.registerMediaButtonEventReceiver(mRemoteControlResponder);
	}

	@Override
	public void onDestroy() {
		L.d(this, "onDestroy");
		housekeepingTimer.cancel();
		this.unregisterReceiver(mediaButtonReceiver);
		audio.unregisterMediaButtonEventReceiver(mRemoteControlResponder);
		super.onDestroy();
	}

	// int bnum=0;
	@Override
	public IBinder onBind(Intent intent) {
		// L.toast(context, ++bnum + " bindings");
		return api;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// L.toast(context, --bnum + " bindings");
		return super.onUnbind(intent);
	}

	IVoiceCommandService.Stub api = new IVoiceCommandService.Stub() {

		@Override
		public void removePattern(String pattern) throws RemoteException {
			// L.d(this, "removePattern");
			patterns.remove(pattern);
		}

		@Override
		public void removeListener(SpeechResultsListener listener)
				throws RemoteException {
			// L.d(this, "removeListener");
			listeners.remove(listener);
		}

		@Override
		public void addPattern(int id, String pattern) throws RemoteException {
			// L.d(this, "addPattern:"+pattern);
			if (!patterns.containsKey(pattern))
				patterns.put(pattern, new PatternPair(id, pattern));
		}

		@Override
		public void addListener(int id, SpeechResultsListener listener)
				throws RemoteException {

			listeners.add(new ListenerPair(id, listener));
		}

		@Override
		public void callCommand(String cmd) throws RemoteException {
			L.d(this, "callCommand");

			try {
				for (PatternPair p : patterns.values()) {
					// L.d(this, "Checking "+cmd+ " vs "+p.pattern());
					Matcher m = p.pattern.matcher(cmd);
					if (m.find()) {

						send(p.pattern.pattern(), cmd);
						return;
					}
				}

				ArrayList<String> al = new ArrayList<String>();
				String[] ss = cmd.split(" ");
				for (String s : ss)
					al.add(s);
				openCommand(cmd, al);

			} catch (Exception e) {
				L.ex(this, "callCommand", e);
			}
		}

		@Override
		public void removeAllPatterns(int id) throws RemoteException {
			cleanPatterns(id);
		}

	};

	private void cleanPatterns(int id) {
		Hashtable<String, PatternPair> temp = new Hashtable<String, PatternPair>();
		for (String s : patterns.keySet()) {
			PatternPair p = patterns.get(s);
			if (p.id == id) {
				L.d(this, "removing pattern " + s);
				continue;
			}
			temp.put(s, p);
		}
		patterns = temp;
	}

	// private void prepareDragon()
	// {
	// _speechKit =
	// SpeechKit.initialize(getApplication().getApplicationContext(),
	// AppInfo.SpeechKitAppId, AppInfo.SpeechKitServer, AppInfo.SpeechKitPort,
	// AppInfo.SpeechKitApplicationKey);
	// _speechKit.connect();
	// }

	private Timer housekeepingTimer = new Timer();
	private TimerTask housekeepingTask = new TimerTask() {

		@Override
		public void run() {
			for (ListenerPair l : listeners) {
				try {
					if (l.listener.isAlive())
						continue;
				} catch (Exception e) {
					cleanPatterns(l.id);
				}
			}
		}
	};

	public class PatternPair {

		int id;
		Pattern pattern;

		public PatternPair(int i, String s) {
			pattern = Pattern.compile(s);
			id = i;
		}
	}

	public class ListenerPair {

		int id;
		SpeechResultsListener listener;

		public ListenerPair(int i, SpeechResultsListener l) {
			id = i;
			listener = l;
		}
	}

}
