package org.malamber.voice.activities;

import java.util.Enumeration;
import java.util.Hashtable;

import org.malamber.logging.L;
import org.malamber.voice.VoiceCommand;
import org.malamber.voice.VoiceCommandApplication;
import org.malamber.voice.aidl.IVoiceCommandService;
import org.malamber.voice.aidl.SpeechResultsListener;
import org.malamber.voice.commands.VoicePatternRunnable;
import org.malamber.voice.sevice.VoiceCommandService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;

public abstract class BaseVoiceActivity extends Activity{
	
	public Hashtable<String, 
					VoicePatternRunnable> 	patterns = new Hashtable<String, VoicePatternRunnable>();	
	public IVoiceCommandService 			service;
	public VoiceCommandApplication			application;
	public SharedPreferences 				prefs;		
	public boolean 							serviceIsBound = true;	
	private Handler							updateHandler = new Handler();
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			
			application=(VoiceCommandApplication) this.getApplication();
			prefs = PreferenceManager.getDefaultSharedPreferences(this);
			
			VoiceCommand.ServiceControl(this, true);			
			bind();
			
			initPatterns();
			
		} catch (Exception e) {
			L.ex(this, "onCreate", e);
		}
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		L.d(this, "onPause");
		finish();
	}
	
	@Override
	protected void onDestroy()
	{
		L.d(this, "onDestroy");			
		try {
			service.removeListener(resultListener);
			for(String s : patterns.keySet())
				service.removePattern(s);
			
					this.unbindService(serviceConnection);
		} catch (Exception e) {
			
		}
		
		super.onDestroy();
	}	
	
	
	protected abstract void initPatterns();
	
    private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder s) {			
			// that's how we get the client side of the IPC connection
			//L.d(this, "onServiceConnected");
			service = IVoiceCommandService.Stub.asInterface(s);
			try {
				service.addListener(this.hashCode(), resultListener);
				Enumeration<String> p =patterns.keys();
				while(p.hasMoreElements())
				{
					String ss = (String) p.nextElement();
					service.addPattern(this.hashCode(), ss);
				}
				
			} catch (RemoteException e) {
				L.ex(this, "Failed to add listener", e);
			}			
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {		
			serviceIsBound=false;
			service=null;
			
		}
	};
	
	public void unbind()
	{
		L.d(this, "unbind Service");
		try
		{
			service.removeAllPatterns(this.hashCode());
			this.unbindService(serviceConnection);
		}
		catch (Exception e)
		{
			L.ex(this, "unbind", e);
		}
		
	}
	public void bind()
	{
		if(!bindService(new Intent(this, VoiceCommandService.class), serviceConnection, BIND_AUTO_CREATE))
		{
			serviceIsBound=false;
			L.e(this, "bindService failed");
		}
	}
	
	private void postResults(String pattern, String results)
	{
		L.d(this, "postResults:"+results);
		L.toast(this, results);
		try {
			patterns.get(pattern).Run(results);
			
		} catch (Exception e) {
			L.ex(this, "run", e);
		}
	}
	
	private SpeechResultsListener.Stub resultListener = new SpeechResultsListener.Stub() 
	{
		@Override 
		public void handSpeechResults(final String pattern,final String results) throws RemoteException 
		{
			updateHandler.post(new Runnable(){
				@Override public void run() {	postResults(pattern, results); }
				});			
		}

		@Override
		public boolean isAlive() throws RemoteException {			
			return true;
		}		
	};
}
