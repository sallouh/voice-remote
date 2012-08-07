package org.malamber.voice.commands.Applications;

import org.malamber.logging.L;
import org.malamber.voice.commands.Command;
import org.malamber.voice.commands.VoicePatternRunnable;

import android.content.Context;
import android.content.Intent;

/**
 * Command:  Open
 * Usage:
 * Keywords: open, open music, open voicemail...
 * Description:
 */
public class OpenCommand extends Command {
	
	static String IntentContacts = "";
	
	public OpenCommand(Context c){
		super(c);		
	}
		
	private void openApplicationList()
	{
		L.d(this, "Starting ApplicationsDialog" );
		try {
			Intent intent = new Intent(context, ApplicationsDialog.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} catch (Exception e) {
			
			L.ex(this, "openApplicationList", e);
		}    	
	}

	@Override
	public void initPatterns() {
		VoicePatternRunnable vprApps = new VoicePatternRunnable()
		{
			@Override
			public void Run(String result) {
				openApplicationList();			
			}			
		};
		
		addPattern("open", vprApps );
		addPattern("open.music", new VoicePatternRunnable(){
			@Override
			public void Run(String result) {				
				
			}});
		addPattern("open.books", new VoicePatternRunnable(){
			@Override
			public void Run(String result) {				
				
			}});
		addPattern("open.email", new VoicePatternRunnable(){
			@Override
			public void Run(String result) {				
				
			}});
		addPattern("open.text.messages", new VoicePatternRunnable(){
			@Override
			public void Run(String result) {				
				
			}});		
		addPattern("open.voicemail", new VoicePatternRunnable(){
			@Override
			public void Run(String result) {				
				
			}});
		addPattern("open.contacts", new VoicePatternRunnable(){
			@Override
			public void Run(String result) {				
				
			}});
		
	}
	
}
