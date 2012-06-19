package org.malamber.voice.Commands.Help;

import org.malamber.common.Log.L;
import org.malamber.voice.R;
import org.malamber.voice.Activities.BaseVoiceActivity;
import org.malamber.voice.Commands.VoicePatternRunnable;

import android.os.Bundle;
import android.view.Window;

public class HelpDialog extends BaseVoiceActivity

{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		L.d(this, "onCreate");
		try {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			
			this.setContentView(R.layout.activity_help);
		
		}
		catch(Exception e)
		{
			
		}
	}
	
	
	@Override
	protected void initPatterns() {
		L.d(this, "initPatterns");
		
		try {
			//scroll up, down
			
			/*patterns.put("favorites", new VoicePatternRunnable(){
				@Override public void Run(String cmd) {
					showFavorites();					
				}});
			patterns.put("all", new VoicePatternRunnable(){
				@Override public void Run(String cmd) {
					showAll();					
				}});
			patterns.put("next", new VoicePatternRunnable(){
				@Override public void Run(String cmd) {
					onNext(null);					
				}});
			patterns.put("previous", new VoicePatternRunnable(){
				@Override public void Run(String cmd) {
					onPrevious(null);					
				}});*/
			
			patterns.put("[0-9].*", number);
			patterns.put("select.[0-9]*", number);
			patterns.put("select.number.[0-9]*", number);
			
		} catch (Exception e) {
			L.ex(this, "initPatterns", e);
		}
		
	}

	VoicePatternRunnable number = new VoicePatternRunnable()
	{

		@Override
		public void Run(String cmd) {
			
			try {
				//int i = Integer.parseInt(cmd);
				
			} catch (Exception e) {
				L.ex(this, "Number Run:"+cmd, e);
			}
			
		}
		
	};
	

}
