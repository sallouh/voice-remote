package org.malamber.voice.lib.commands.help;

import org.malamber.voice.lib.commands.Command;
import org.malamber.voice.lib.commands.VoicePatternRunnable;

import android.content.Context;

/**
 * Command:  Help
 * Usage:
 * Keywords: help
 * Description:
 */
public class HelpCommand extends Command
{

	public HelpCommand(Context c)
	{
		super(c);
		
		
	}
	

	@Override
	public void initPatterns() {
		addPattern("help", new VoicePatternRunnable(){
			@Override
			public void Run(String result) {
				
			}});
		
	}

}
