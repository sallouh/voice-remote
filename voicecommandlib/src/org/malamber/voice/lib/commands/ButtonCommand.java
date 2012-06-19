package org.malamber.voice.lib.commands;

import org.malamber.voice.lib.sendevents.KeyCode;

import android.content.Context;

/**
 * Command:  Buttons
 * Keywords: play, next track, previous track, pause, stop
 * Usage:
 * Description:
 */
public class ButtonCommand extends Command
{

	public ButtonCommand(Context c)
	{
		super(c);
		
	}

	private void handleRes(String s)
	{
		int key = getKey(s);
		if(key > 0)
		KeyCode.SendKeycode(key);
		
	}
	private int getKey(String s)
	{
		if(s.contains("home")) return KeyCode.HOME;
		if(s.contains("back")) return KeyCode.BACK;
		if(s.contains("menu")) return KeyCode.MENU;
		if(s.contains("volume")){
			if(s.contains("down")) return KeyCode.VOLUME_DOWN;
			if(s.contains("up")) return KeyCode.VOLUME_UP;
		}
		
		
		
		return 0;
	}
	@Override
	public void initPatterns()
	{
		VoicePatternRunnable vpr = new VoicePatternRunnable(){

			@Override
			public void Run(String result)
			{
				handleRes(result);
				
			}};
		addPattern("go.home", vpr);
		addPattern("go.back", vpr);
		addPattern("menu", vpr);
		
	}

}
