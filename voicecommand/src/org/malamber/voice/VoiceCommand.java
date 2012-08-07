package org.malamber.voice;

import java.util.Hashtable;

import org.malamber.logging.L;
import org.malamber.voice.Service.VoiceCommandService;
import org.malamber.voice.activities.VoiceCommandActivity;
import org.malamber.voice.commands.VoicePatternRunnable;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

public class VoiceCommand 
{
	//public static String VOICECOMMAND = "org.malamber.voicecommand.VOICE_BROADCAST" ;
	
	public static String VOICE_STATECHANGE = "org.malambber.VOICE_STATECHANGE";
	public static String VOICE_BROADCAST = "org.malambber.VOICE_COMMAND";
	public static final int STATE_DONE=0;	
	public static final int STATE_READY=1;
	public static final int STATE_RESULTS=2;
	public static final int STATE_RECORDING=3;
	public static final int STATE_WAITINGFORRESULTS=4;
	public static final int STATE_NOMATCH=5;
	
	public static String NOTIFY_OFF = "Microphone off";
	public static String NOTIFY_ON = "Microphone on";
	public static String NOTIFY_SLEEP = "Microphone sleep";
	
	public static final int STATE_ERROR=10;
	
	public static final String PREF_SHOWNOTIFY = "showNotifyIcon";
	public static final String PREF_AUTOSTART = "autoStart";
	public static final String PREF_COMMANDTIMEOUTSEC ="commandTimeoutSeeconds";
	public static final String PREF_COMMANDSTIMEOUT ="commandsTimeout";
	public static final String PREF_PLAY_ERROR ="playError";
	public static final String PREF_PLAY_NOTICE ="playNotice";
	public static final String PREF_PLAY_RECO ="playRecgnition";
	public static final String PREF_MEDIABUTTON ="useMediaButton";
	public static final String PREF_USEHEADSET ="useMediaButton";
	
	public static void ServiceControl(Context c, boolean start)
	{		
		L.d(c,(start?"Starting":"Stopping")+ " VoiceCommandService");
		
		Intent i = new Intent(c, VoiceCommandService.class);
		if(start)
			c.startService(i);
		else {
			
			if(!c.stopService(i))
			{
				L.e(c, "error stopping service");
			}
			VoiceCommand.hideNotify(c);
		}
	}
	public static int parseNumberString(String s) throws NumberFormatException
	{	
		try
		{
			String ss = s;
			if (ss.equals("one"))
				ss = "1";
			if (ss.equals("two"))
				ss = "2";
			if (ss.equals("three"))
				ss = "3";
			if (ss.equals("four"))
				ss = "4";
			if (ss.equals("five"))
				ss = "5";
			if (ss.equals("six"))
				ss = "6";
			if (ss.equals("seven"))
				ss = "7";
			if (ss.equals("eight"))
				ss = "8";
			if (ss.equals("nine"))
				ss = "9";
			if (ss.equals("ten"))
				ss = "10";
			int i = Integer.parseInt(ss);
			return i;
		}
		catch (Exception e)
		{
			return 0;
		}
	}

	public static void PlayError(Context c)
	{
		play(c, R.raw.error);
	}
	public static void PlayRecognition(Context c)
	{
		play(c, R.raw.recognition);
	}
	public static void PlayNotice(Context c)
	{
		play(c, R.raw.notice);
	}
	private static void play(Context c, int play)
    {    	
    	 try {
			
    		 MediaPlayer player = MediaPlayer.create(c, play);
    		 player.setLooping(false);
    		 player.start();
		} catch (IllegalArgumentException e) {
			
			L.ex(null, "play",e);
		} catch (SecurityException e) {
			L.ex(null, "play",e);
		} catch (IllegalStateException e) {

			L.ex(null, "play",e);
		}
    }
	public static String intToString(int i)
	{
		String s = "";
		switch(i)
		{
			case 0:	s = "zero";
			break;
			case 1:	s = "one";
				break;
			case 2:	s = "two";
			break;
			case 3:	s = "three";
			break;
			case 4:	s = "four";
			break;
			case 5:	s = "five";
			break;
			case 6:	s = "six";
			break;
			case 7:	s = "seven";
			break;
			case 8:	s = "eight";
			break;
			case 9:	s = "nine";
			break;
			case 10: s = "ten";
			break;
		}
		return s;
	}
	
	public static void addNumberPatterns(Hashtable<String,VoicePatternRunnable> patterns, VoicePatternRunnable number)
	{
		for(int i = 1;i<11;i++)
		{
			patterns.put(""+i, number);
			patterns.put(intToString(i), number);
		}
	}
	
	public static void addNumberPatterns(Hashtable<String,VoicePatternRunnable> patterns,
			String fmt, 
			VoicePatternRunnable number)
	{
		for(int i = 1;i<11;i++)
		{
			patterns.put(fmt.replace("%i%", ""+i), number);
			patterns.put(fmt.replace("%i%", intToString(i)), number);
		}
	}
	
	public static void hideNotify(Context c)
	{
		NotificationManager mNotificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(1);
	}
	public static void showNotify(Context c,String s,  int icon)
	{
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) c.getSystemService(ns);
		//int icon = R.drawable.icon;
		CharSequence tickerText = "Voice Command";
		long when = System.currentTimeMillis();

		Notification notification = new Notification(
				//on ? R.drawable.mic_on:R.drawable.mic_off,
				icon, 
				tickerText,
				when);
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		//Context context = getApplicationContext();
		CharSequence contentTitle = s;
		CharSequence contentText = "";
		Intent notificationIntent = new Intent(c, VoiceCommandActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(c, 0,
				notificationIntent, 0);

		notification.setLatestEventInfo(c, contentTitle, contentText,
				contentIntent);

		
		mNotificationManager.notify(1, notification);
		
	}
}
