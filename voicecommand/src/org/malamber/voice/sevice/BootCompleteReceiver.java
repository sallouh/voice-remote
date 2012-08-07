package org.malamber.voice.sevice;

import org.malamber.logging.L;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class BootCompleteReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		L.Tag = "voice";
		
		try {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

			if (prefs.getBoolean("AutoStart", true))
				context.startService(new Intent(context, VoiceCommandService.class));

		} catch (Exception e) {

			L.ex(this,"", e );

			//MessageBox.AlertBox(context, e.getMessage());
		}

	}

}
