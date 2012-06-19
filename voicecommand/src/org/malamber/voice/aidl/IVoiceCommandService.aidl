package org.malamber.voice.aidl;

//import org.malamber.voicecommand.Commands.PatternCollection;
import org.malamber.voice.aidl.SpeechResultsListener;


interface IVoiceCommandService{	

	void addListener(int id, SpeechResultsListener listener);

	void removeListener(SpeechResultsListener listener);
	
	void addPattern(int id, String pattern);
	
	void removePattern(String pattern);
		
	void removeAllPatterns(int id);	
	
	void callCommand(String cmd);
	
}