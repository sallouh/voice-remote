package org.malamber.voice.Commands.Touch;

import org.malamber.logging.L;
import org.malamber.voice.VoiceCommand;
import org.malamber.voice.commands.Command;
import org.malamber.voice.commands.VoicePatternRunnable;

import android.content.Context;
import android.content.Intent;


/**
 * Command:  Touch
 * Usage:
 * Keywords: touch, press
 * Description:
 */
public class TouchCommand extends Command{

	public TouchCommand(Context c) {
		super(c);
		
		
	}


	public void runGgrid(String s) {
		try {
			Intent intent = new Intent(context, GridOverlay.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			L.d(this, "runGgrid:"+ s);	    	
			if(s.replace("touch", "").length()>0){
					
			String[] ss = s.split(" ");
			int i1=0;
			//int i2=0;
			//int i3=0;
			if(ss != null && ss.length > 0){
				
				if (ss.length > 1) {
					i1 = VoiceCommand.parseNumberString(ss[1]);
				}
				
				if (ss.length > 2) {
					//i2 = VoiceCommand.parseNumberString(ss[2]);					
				}	
				if (ss.length > 3) {
					//i3  =VoiceCommand.parseNumberString(ss[3]);					
				}
				
				intent.putExtra("index", i1);	
				
				
				if (ss.length > 4) {
					int to =VoiceCommand.parseNumberString(ss[4]);
					intent.putExtra("touch", to);
				}	
			}
			else{
				L.e(this, "no args");
			}}				
			
			context.startActivity(intent);
			
		} catch (Exception e) {
			L.ex(this, "Run", e);
		}
	}


	VoicePatternRunnable vpr;
	@Override
	public void initPatterns() {
	
		vpr = new VoicePatternRunnable(){

				@Override
				public void Run(String result) {
					L.i(this,"Run "+result);
					runGgrid(result);			
				}};
		addPattern("^touch", vpr );		
		addPattern("^touch.*", vpr );
		
		
	}
	
}
