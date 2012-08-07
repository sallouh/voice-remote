package org.malamber.voice.google;

import org.malamber.logging.L;
import org.malamber.voice.VoiceCommand;
import org.malamber.voice.sevice.VoiceCommandService;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;

public class GoogleRecognizer
implements RecognitionListener
{
	
	SpeechRecognizer speechRecognizer;
	VoiceCommandService service;
	Intent intent;
	Context context;
	boolean on = false;
	boolean started = false;	
	
	public GoogleRecognizer(Context c, Intent i, VoiceCommandService vcs)
	{	
		L.Tag="voice";
		L.d(this, "Recognizer");	
		speechRecognizer = SpeechRecognizer.createSpeechRecognizer(c);
		speechRecognizer.setRecognitionListener(this);
		intent = i;
		context=c;
		service = vcs;
	}
	
	public void startListening()
	{
		L.i(this, "startListening");
		speechRecognizer.startListening(intent);
	}
	
	public void stopListening()
	{
		speechRecognizer.stopListening();
	}
	
	public void onResults(Bundle results) 
	{	
		String cmd = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);		
		
		L.d(this, "onResults: "  + cmd);
		
		if(service != null)
			service.RecognizerResults(results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION));
		else	
		{
			L.toast(context, results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0));
		}
		
		service.RecognizerStateChange(VoiceCommand.STATE_DONE, null);
		//speechRecognizer.startListening(intent);	
	}
	
	public boolean isRecognitionAvailable()
	{
		return SpeechRecognizer.isRecognitionAvailable(context);
	}
	
	public void onBeginningOfSpeech()
	{
		L.d(this, "onBeginningOfSpeech");		
		service.RecognizerStateChange(VoiceCommand.STATE_RECORDING, null);
	}	

	public void onEndOfSpeech() {
		L.d(this, "onEndOfSpeech");		
		service.RecognizerStateChange(VoiceCommand.STATE_WAITINGFORRESULTS, null);
	}

	public void onError(int error) {
		
		
		String msg="";
		boolean tryagain=false;
		switch(error)
		{
		case SpeechRecognizer.ERROR_AUDIO:msg="Audio Error";break;
		case SpeechRecognizer.ERROR_CLIENT:msg="Client Error";break;
		case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:msg="PERMISSIONS";break;
		case SpeechRecognizer.ERROR_NETWORK:msg="Network Error";break;
		case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:msg="Network timeout";break;
		case SpeechRecognizer.ERROR_NO_MATCH:
			//L.toast(context, "No commands given, try again");			
			//
			tryagain=true;
			msg="No match found";
			//c=false;
			break;
		case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:msg="Busy";break;
		case SpeechRecognizer.ERROR_SERVER:msg="Server error";break;
		case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
			msg="Speech timeout";
			
			//speechRecognizer.startListening(intent);
			break;
		}
		
		if(tryagain)
		{
			//speechRecognizer.startListening(intent);
		}
		
		service.RecognizerStateChange(VoiceCommand.STATE_ERROR, msg);
		
		L.d(this, "onError: " + msg);
	}

	public void onReadyForSpeech(Bundle params) {
		
		L.i(this, "Ready");
		
		
		service.RecognizerStateChange(VoiceCommand.STATE_READY, null);
		
	}
	
	public void onEvent(int eventType, Bundle params) {
		L.d(this, "onEvent");
		
	}
	public void onPartialResults(Bundle partialResults) {}
	public void onBufferReceived(byte[] buffer) {}
	public void onRmsChanged(float rmsdB) {}

	
}
