package org.malamber.voice.Commands;

import org.malamber.common.Log.L;
import org.malamber.voice.Service.VoiceCommandService;
import org.malamber.voice.aidl.IMediaPlaybackService;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * Command:  Media
 * Keywords: play, next track, previous track, pause, stop
 * Usage:
 * Description:
 */
public class MediaCommand extends Command 
{    
	
	static final int PLAY=0;	
	static final int PAUSE=1;
	static final int STOP=2;
	static final int NEXT=3;
	static final int PREV=4;
		
	
    IMediaPlaybackService service;
    

    Context context;
    
	public MediaCommand(Context c)
	{
		super(c);
		
		L.d(this, "MediaCommand");
		if(!c.bindService(new Intent(c, VoiceCommandService.class), serviceConnection, Context.BIND_AUTO_CREATE))
		{			
			L.e(null, "bindService failed");
		}
		
			
	}	
	private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder s) {			
			// that's how we get the client side of the IPC connection
			//L.d(this, "onServiceConnected");
			service = IMediaPlaybackService.Stub.asInterface(s);
			//L.i(null, "bound media service");	
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			//L.i(this, "onServiceDisconnected");			
			//serviceIsBound=false;
		}
	};
	
	
	public void media( int m)
	{
		L.d(this, "media");
		if(service == null)
		{
			L.e(this, "null service");
			return;
		}
		try {
			switch(m)
			{
			case NEXT:
				service.next();
				break;
			case PREV:
				service.prev();
				break;
			case STOP:
				service.stop();
				break;
			case PAUSE:
				service.pause();
				break;
			case PLAY:
				service.play();
				break;
			}
		} catch (RemoteException e) {			
			L.ex(this, "Media", e);
		}
		catch (Exception e) {			
			L.ex(this, "Media", e);
		}
	}	
		
	

	@Override
	public void initPatterns() {		
		addPattern("play", new VoicePatternRunnable(){
			@Override
			public void Run(String result) {			
				media(PLAY);				
			}});
		addPattern("next.track", new VoicePatternRunnable(){
			@Override
			public void Run(String result) {
				media(NEXT);		
			}});
		addPattern("previous.track", new VoicePatternRunnable(){
			@Override
			public void Run(String result) {
				media(PREV);		
			}});
		addPattern("stop", new VoicePatternRunnable(){
			@Override
			public void Run(String result) {
				media(STOP);			
			}});
		addPattern("pause", new VoicePatternRunnable(){
			@Override
			public void Run(String result) {
				media(PAUSE);	
			}});
		
	}
}
