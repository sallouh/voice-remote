package org.malamber.voice.lib.commands;

import org.malamber.common.Log.L;
import org.malamber.voice.lib.sendevents.KeyCode;

import android.content.Context;

/**
 * Command:  Move
 * Keywords: 
 * Usage:
 * Description:
 */
public class MoveCommand extends Command {

	public static String[] strings = new String[] 
			{
				"",
			};
	
	
	static final int UP=0;	
	static final int DOWN=1;
	static final int LEFT=2;
	static final int RIGHT=3;
	//AndroidNativeDriver _native;
	public MoveCommand(Context c) {
		super(c);
		try
		{
			//_native = new AndroidNativeDriver();//..withDefaultServer().build();
			
			
		}
		catch (Exception e)
		{
			L.ex(this, "", e);
		}
	}

	//public void sendKey(Keys k)
	{
		//_native.getKeyboard().pressKey(k);
		//_native.getKeyboard().releaseKey(k);
	}
	
	public void move(int dir, int num)
	{		
		L.d(this,"move "+dir+":"+num);
		int key = KeyCode.DPAD_DOWN;
		switch(dir)
		{
		case UP:
			key = KeyCode.DPAD_UP;
			break;		
		case LEFT:
			key = KeyCode.DPAD_LEFT;
			break;
		case RIGHT:
			key = KeyCode.DPAD_RIGHT;
			break;
		}
		for(int i = 0; i < num; i++)
			KeyCode.SendKeycode(key);		
	
	}
	
	@Override
	public void initPatterns() {
		VoicePatternRunnable vpr = new VoicePatternRunnable(){
			@Override
			public void Run(String result) {
				String r = result.split(" ")[1];
				int n = 1;
				if(result.split(" ").length > 2){
					String sn = result.split(" ")[2];
					n = Integer.parseInt(sn);
				}
				
				if(r.equals("up"))
					move(UP, n);
				if(r.equals("down"))
					move(DOWN, n);
				if(r.equals("left"))
					move(LEFT, n);
				if(r.equals("right"))
					move(RIGHT, n);
				
			}};
			
		addPattern("move.up", vpr);
		addPattern("move.up.%i%", vpr);

		addPattern("move.down.*", vpr);
		addPattern("move.left.*", vpr);
		addPattern("move.down.*", vpr);
	}
}
