package org.malamber.voice.Activities;

import java.util.ArrayList;

import org.malamber.common.Log.L;
import org.malamber.voice.R;
import org.malamber.voice.VoiceCommand;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.CheckBox;

public class OptionsActivity extends Activity {

	SharedPreferences prefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);
		L.Tag = "voice";

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
	}

	public void onOptionChanged()
	{
		CheckBox cb = (CheckBox) findViewById(R.id.cbNotify);
		CheckBox auto = (CheckBox) findViewById(R.id.cbNotify);
		CheckBox mb = (CheckBox) findViewById(R.id.cbUseMediaButton);
		
		Editor e = prefs.edit();
		e.putBoolean(VoiceCommand.PREF_SHOWNOTIFY, cb.isChecked());
		e.putBoolean(VoiceCommand.PREF_AUTOSTART, auto.isChecked());
		e.putBoolean(VoiceCommand.PREF_MEDIABUTTON, mb.isChecked());
		e.commit();
	}
	


	public void Apply(View v) {
		finish();
	}

	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

	@SuppressWarnings("unused")
	private void startVoiceRecognitionActivity() {
		try {
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
					RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			// intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
			// "Speech recognition");
			intent.putExtra(
					RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,
					3 * 1000);

			startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
		} catch (Exception e) {
			L.ex(this, e.getMessage());
		}
	}

	private void handleSpeechResults(int resultCode, Intent i) {
		L.d(this, "");
		if (resultCode == RecognizerIntent.RESULT_NO_MATCH) {
			// no results found
		} else if (resultCode == RESULT_OK) {

			@SuppressWarnings("unused")
			ArrayList<String> matches = i.getStringArrayListExtra("matches");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent i) {
		L.d(this, "onActivityResult");
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE) {
			handleSpeechResults(resultCode, i);
		}
	}

	
}
