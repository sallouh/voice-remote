package org.malamber.voice;

import java.util.ArrayList;

import org.malamber.logging.L;
import org.malamber.voice.commands.Command;
import org.malamber.voice.commands.CommandCollection;
import org.malamber.voice.commands.VoicePattern;
import org.malamber.voice.commands.VoicePatternRunnable;

import android.app.Application;
import android.content.Context;

public class VoiceCommandApplication extends Application 
{	
	Context context;
	CommandCollection cc;
	private ArrayList<VoicePattern> servicePatterns = new ArrayList<VoicePattern>();    
	
	//static {
    //    System.loadLibrary("jniLib");
    //}
	//public native String  stringFromJNI();
	
	public VoiceCommandApplication()
	{
		initServicePatterns();
	}
	
	public void setContext(Context c)
	{
		context=c;
	}
	@Override
	public void onCreate() 
	{
		super.onCreate();
		L.Tag="voice";
		L.i(this, "Starting VoiceCommandApplication");
		
		try
		{
			cc = CommandCollection.createBaseCommandSet(getApplicationContext()); //new CommandCollection(this.getApplicationContext());
		}
		catch (Exception e)
		{
			L.ex(this, "VoiceCommandApplication",e);
		}
    }
	
	public Command lookupCommand(String s)
    {
		return cc.lookupCommand(s);
    }
	public void initServicePatterns()
	{		
		servicePatterns.add(new VoicePattern("microphone.off",new VoicePatternRunnable(){
			@Override
			public void Run(String result) {
				VoiceCommand.showNotify(context, "Microphone off", R.drawable.mic_off);	
				//recognizer.stopListening();
			}} ));
	}
	
	public boolean checkServicePattern(String s)
	{
		for(VoicePattern p:servicePatterns)
		{
			if(!p.isMatch(s)) continue;
			
			L.d(this, "found match in servicePatterns");
			p.Run(s);
			return true;
		}
		
		return false;
	}
}
