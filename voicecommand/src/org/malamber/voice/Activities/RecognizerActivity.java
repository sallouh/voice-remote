package org.malamber.voice.Activities;

import java.util.Timer;
import java.util.TimerTask;

import org.malamber.common.Log.L;
import org.malamber.voice.R;
import org.malamber.voice.VoiceCommand;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RecognizerActivity extends Activity {
	
	//RecognizerView view;
	
	Timer timeoutTimer = new Timer();
	TimerTask tt = new TimerTask(){

		@Override
		public void run() {
			finish();			
		}};
	TextView tv;
	ProgressBar pb;
	Handler mHandler;
	Context context;
	int val;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
		context=this;
        setContentView(R.layout.progress);
        //timeoutTimer.schedule(tt, 10 * 1000);
        pb = (ProgressBar) this.findViewById(R.id.progress);
        /*new Thread(new Runnable() {
        	              public void run() {
        	                  while (val < 100) {
        	                     // val = doWork();
        	 
        	                      // Update the progress bar
        	                      mHandler.post(new Runnable() {
        	                          public void run() {
        	                              pb.setProgress(val);
        	                              //sleep(1);
        	                          }
        	                      });
        	                  }
        	              }
        	          }).start();*/
        
        tv = (TextView) this.findViewById(R.id.textProgress);
        tv.setVisibility(View.GONE);
        
        registerStateChangeBroadcast();
    }
	
	private BroadcastReceiver stateReceiver = new BroadcastReceiver() 
	{
		@Override
		public void onReceive(Context c, Intent i) 
		{
			L.d(this, "RecognizerActivity:StateChanged");
			L.toast(context, "RecognizerActivity:StateChanged");
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
	public void onPause()
	{
		super.onPause();
		
		finish();
	}
	private void handleStateChange(int state, Intent i)
	{
		switch(state)
		{
		case VoiceCommand.STATE_DONE:
			finish();
			pb.setVisibility(View.GONE);
			
			tv.setText("STATE_DONE");
			tv.setVisibility(View.VISIBLE);
			break;
		case VoiceCommand.STATE_ERROR:
			pb.setVisibility(View.GONE);
			tv.setText(i.getStringExtra("error"));		
			tv.setVisibility(View.VISIBLE);
			break;
		case VoiceCommand.STATE_READY:
			break;
		case VoiceCommand.STATE_RECORDING:	
			pb.setVisibility(View.VISIBLE);
			tv.setVisibility(View.GONE);
			break;
		case VoiceCommand.STATE_RESULTS:
			//finish();
			pb.setVisibility(View.GONE);
			
			tv.setText("STATE_RESULTS");
			tv.setVisibility(View.VISIBLE);
			break;
		case VoiceCommand.STATE_WAITINGFORRESULTS:
			pb.setVisibility(View.VISIBLE);
			tv.setVisibility(View.GONE);
			break;
		case VoiceCommand.STATE_NOMATCH:
			pb.setVisibility(View.GONE);
			
			tv.setText("No match found");
			tv.setVisibility(View.VISIBLE);
			break;
		}
	}
	

}
