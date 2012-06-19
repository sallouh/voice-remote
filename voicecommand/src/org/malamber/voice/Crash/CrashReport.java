package org.malamber.voice.Crash;

import android.content.Context;
import android.content.Intent;

public class CrashReport
{
	public static void sendReport(Context c, Exception e)
	{
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ "smw@malamber.org" });
        
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Voice Command Exception");
 
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, e.getStackTrace());

        c.startActivity(Intent.createChooser(emailIntent, "Send mail..."));

	}
}
