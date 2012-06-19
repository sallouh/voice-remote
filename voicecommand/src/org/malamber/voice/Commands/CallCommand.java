package org.malamber.voice.Commands;

import org.malamber.common.Log.L;
import org.malamber.voice.Service.VoiceCommandService;
import org.malamber.voice.aidl.ITelephony;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.TelephonyManager;

/**
 * Command:  Call
 * Keywords: call
 * Usage:
 * Description: used to call
 */
public class CallCommand extends Command
{
	
	public CallCommand(Context c)
	{
		super(c);
		
		try {
			if(!c.bindService(new Intent(c, VoiceCommandService.class), serviceConnection, Context.BIND_AUTO_CREATE))
				L.e(this,"telephonyService failed");
			/*TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			Class<?> cl = Class.forName(tm.getClass().getName());
			Method m = cl.getDeclaredMethod("getITelephony");
			m.setAccessible(true);
			telephonyService = (ITelephony) m.invoke(tm);
			if(telephonyService == null)
			{
				L.e(this,"telephonyService failed");
			}*/
		} catch (Exception e) {
			L.ex(this, "CallCommand", e);
		}
	}
	
	private void answerCall()
	{
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm.getCallState() != TelephonyManager.CALL_STATE_RINGING) {
                return;
        }

        // Answer the phone
        try {
        	answerPhoneAidl(context);
        }
        catch (Exception e) {
                e.printStackTrace();
               
        }

	}
	ITelephony telephonyService;
	private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder s) {			
			// that's how we get the client side of the IPC connection
			//L.d(this, "onServiceConnected");
			telephonyService = ITelephony.Stub.asInterface(s);
						
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			//L.i(this, "Service connection closed");			
		
		}
	};
    private void answerPhoneAidl(Context context) throws Exception {     
        telephonyService.silenceRinger();
        telephonyService.answerRingingCall();
    }

	private void endCall()
	{
		try {
			telephonyService.endCall();
		} catch (RemoteException e) {
			
			e.printStackTrace();
		}
	}
	
	@Override
	public void initPatterns() {
		VoicePatternRunnable vpr = new VoicePatternRunnable(){
			@Override
			public void Run(String result) {
		
			}};
		addPattern("call",vpr );
		addPattern("call.*",vpr );
		
		addPattern("answer.*",new VoicePatternRunnable(){
			@Override
			public void Run(String result) {				
				answerCall();
			}} );
		addPattern("end.*",new VoicePatternRunnable(){
			@Override
			public void Run(String result) {				
				endCall();
			}} );
		addPattern("ignore.*",new VoicePatternRunnable(){
			@Override
			public void Run(String result) {				
				endCall();
			}} );
		
	}
	

}
