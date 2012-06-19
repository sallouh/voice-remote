package org.malamber.voice.aidl;

interface SpeechResultsListener{
	void handSpeechResults(String pattern, String results);
	boolean isAlive();
}