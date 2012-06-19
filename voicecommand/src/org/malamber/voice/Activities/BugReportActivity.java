package org.malamber.voice.Activities;

import org.malamber.common.Log.L;
import org.malamber.voice.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BugReportActivity extends Activity {

	/*
	 * Title
	 * Description
	 * Command
	 */
	@Override	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.bugreport);
		L.Tag = "voice";
	}
	
	public void sendBug(View v)
	{
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"smw@malamber.org"});
		i.putExtra(Intent.EXTRA_SUBJECT, "[BUG] - " );
		i.putExtra(Intent.EXTRA_TEXT   , "");
		try {
		    startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
		    L.ex(this, "There are no email clients installed.", ex);
		}
	}
}
