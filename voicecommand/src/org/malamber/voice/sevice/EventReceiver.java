package org.malamber.voice.sevice;

import org.malamber.logging.L;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

public class EventReceiver extends BroadcastReceiver {
	long keydownStart;
    @Override
    public void onReceive(Context context, Intent intent) {
    	L.Tag="voice";
    	
    	try {
			L.d(this, "onReceive:EventReceiver - ");

			/*if (!PreferenceManager.getDefaultSharedPreferences(context)
					.getBoolean(VoiceCommand.PREF_MEDIABUTTON, true)
					&& Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
				return;
			}*/
			Intent filter = new Intent("org.malambber.VOICE_COMMAND");
			KeyEvent event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
			if(event==null)
			{
				filter.putExtra("key",intent.getIntExtra("key", 0));
				filter.putExtra("action",intent.getIntExtra("action", 0));
				filter.putExtra("eventtime", intent.getIntExtra("eventtime", 0) );
			}
			else
			{
				filter.putExtra("key",event.getKeyCode());
				filter.putExtra("action",event.getAction());
				filter.putExtra("eventtime", event.getEventTime());
			}
	            
	          
	          //filter.putExtra(Intent.EXTRA_KEY_EVENT, event);
			//context.sendOrderedBroadcast(filter,"");
	          //context.sendBroadcast(filter);
        
			
			
		} catch (Exception e) {
			L.ex(this, "onReceive", e);
		}
    }

}
