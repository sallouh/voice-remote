package org.malamber.voice.activities;

import org.malamber.logging.L;
import org.malamber.voice.R;
import org.malamber.voice.VoiceCommand;
import org.malamber.voice.commands.VoicePatternRunnable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

public class VoiceCommandActivity extends BaseVoiceActivity
{
	Context context;
	ProgressBar pb;
	TextView tv;
	MediaPlayer mp;
	@Override	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		L.Tag = "voice";
		
		context=this;
		mp=new MediaPlayer();
		
		
		try {
			pb = (ProgressBar) this.findViewById(R.id.pbResults);
			tv = (TextView) this.findViewById(R.id.tvResults);
			pb.setVisibility(View.GONE);
			tv.setVisibility(View.GONE);
			registerStateChangeBroadcast();
			
		} catch (Exception e) {
			L.ex(this, "onCreate", e);
		}        
	}
	
	
	private BroadcastReceiver stateReceiver = new BroadcastReceiver() 
	{
		@Override
		public void onReceive(Context c, Intent i) 
		{
			L.d(this, "RecognizerActivity:StateChanged");
			
			int state = i.getIntExtra("state", 0);
			
			handleStateChange(state, i);
		}
	};
	
	private void registerStateChangeBroadcast()
	{
		IntentFilter filter = new IntentFilter(VoiceCommand.VOICE_STATECHANGE);			
		this.registerReceiver(stateReceiver, filter);		
	}
	
	@Override    
    public boolean onPrepareOptionsMenu(Menu menu) {

    	menu.clear();
    	menu.add(0, 1, 0, "Options");
    	menu.add(0, 2, 0, "Bug Report");
		return true;
	}
	
	
	
	@Override    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
        case 1://options
        	Intent i = new Intent(this, OptionsActivity.class);
        	this.startActivity(i);
        	break;
        case 2://options
        	Intent bi = new Intent(this, BugReportActivity.class);
        	this.startActivity(bi);
        	break;
        }
        return true;
	}
	
	public void onService(View v)
	{
		try {
			L.d(this, "onService ");
			//VoiceCommand.ServiceControl(this, false);
			//Intent i = new Intent(this, VoiceCommandService.class);
			this.unbind();
			
			VoiceCommand.ServiceControl(this, false);
			
			finish();
			/*Button b = (Button) this.findViewById(R.id.bnService);
			if (service == null) {
				VoiceCommand.ServiceControl(this, true);
				bind();
			} else {
				//unbind();
				this.stopService(new Intent(this, VoiceCommandService.class));
				service.stopService();
				unbind();
			}
			b.setText(service == null ? "Start" : "Stop");*/
		} catch (Exception e) {
			L.ex(this, "onService", e);
			service=null;
		}		
	}
	
	private void handleStateChange(int s, Intent i)
	{
		switch(s)
		{
		case VoiceCommand.STATE_DONE:
			//finish();
			pb.setVisibility(View.GONE);	
			tv.append("Done\n");
			
			tv.setVisibility(View.VISIBLE);
			break;
		case VoiceCommand.STATE_ERROR:
			pb.setVisibility(View.GONE);
			tv.append(i.getStringExtra("error")+"\n");		
			tv.setVisibility(View.VISIBLE);
			break;
		case VoiceCommand.STATE_READY:
			tv.append("Ready\n");
			break;
		case VoiceCommand.STATE_RECORDING:	
			tv.append("Recording\n");
			pb.setVisibility(View.VISIBLE);
			tv.setVisibility(View.VISIBLE);
			break;
		case VoiceCommand.STATE_RESULTS:
			//finish();
			pb.setVisibility(View.GONE);			
			tv.append(i.getStringExtra("result"));
			tv.setVisibility(View.VISIBLE);
			break;
		case VoiceCommand.STATE_WAITINGFORRESULTS:
			tv.append("Getting Results\n");
			pb.setVisibility(View.VISIBLE);
			tv.setVisibility(View.VISIBLE);
			break;
		case VoiceCommand.STATE_NOMATCH:
			pb.setVisibility(View.GONE);			
			tv.append("No match found\n");
			tv.setVisibility(View.VISIBLE);
			break;
		}
	}

	public void onSpeak(View v)
	{
		tv.setText("");
		Intent i = new Intent(VoiceCommand.VOICE_BROADCAST);
		i.putExtra("key", KeyEvent.KEYCODE_HEADSETHOOK);
		i.putExtra("action", KeyEvent.ACTION_UP);
		i.putExtra("eventtime", System.currentTimeMillis());
		
		this.sendBroadcast(i);
				
		pb.setVisibility(View.VISIBLE);			    
	}
	public void onTest(View v)
	{		
		try 
		{
			service.callCommand("touch 4");	
			
		} catch (Exception e) {
			L.ex(this, "openApps", e);
		}	    
	}
	
	@Override
	protected void onDestroy()
	{
		this.unregisterReceiver(stateReceiver);		
		super.onDestroy();
	}
	
	@Override
	protected void initPatterns() {
		try {
			VoicePatternRunnable v = new VoicePatternRunnable(){

					@Override
					public void Run(String cmd) {
						onService(null);
						
					}};
			patterns.put("start.*",v);
			patterns.put("stop.*",v);
			
			
		} catch (Exception e) {
			L.ex(this, "initPatterns", e);
		}
		
	}
}