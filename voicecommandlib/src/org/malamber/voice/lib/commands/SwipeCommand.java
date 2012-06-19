package org.malamber.voice.lib.commands;

import android.content.Context;

/**
 * Command:  Swipe
 * Keywords: play, next track, previous track, pause, stop
 * Usage:
 * Description:
 */
public class SwipeCommand extends Command{

	static final int UP=0;	
	static final int DOWN=1;
	static final int LEFT=2;
	static final int RIGHT=3;
	
	public SwipeCommand(Context c) {
		super(c);
		
	}
	public void swipe(int dir)
	{
		switch(dir)
		{
		case UP:
			break;
		case DOWN:
			break;
		case LEFT:
			break;
		case RIGHT:
			break;
		}
	}
	
	@Override
	public void initPatterns() {
		
		VoicePatternRunnable sr = new VoicePatternRunnable(){
			@Override
			public void Run(String result) {				
				String r = result.replace("swipe ", "");
				if(r.equals("up"))
					swipe(UP);
				if(r.equals("down"))
					swipe(DOWN);
				if(r.equals("left"))
					swipe(LEFT);
				if(r.equals("right"))
					swipe(RIGHT);
			}};
		
		addPattern("swipe.left",sr );
		addPattern("swipe.right",sr );
		addPattern("swipe.up",sr );
		addPattern("swipe.down",sr );
		
	}

}
