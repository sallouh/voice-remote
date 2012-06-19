package org.malamber.voice.Commands;

import java.util.Hashtable;

import org.malamber.common.Log.L;
import org.malamber.voice.VoiceCommand;

import android.content.Context;

public abstract class Command 
{	
	public Context 			context;
	public Hashtable<String, VoicePattern> 	patterns = new Hashtable<String, VoicePattern>();	
	
	public Command(Context c)
	{		
		//L.d(this,"Constructor");
		context=c;
		initPatterns();		
	}
	
	public String[] getRegex()
	{
		return (String[]) patterns.keySet().toArray();
	}
	public void addNumberPatterns(
			String fmt, 
			VoicePatternRunnable r)
	{
		for(int i = 1;i<11;i++)
		{
			String s = fmt.replace("%i%", ""+i);
			VoicePattern vp = new VoicePattern(s, r);
			patterns.put(s, vp);
			patterns.put(fmt.replace("%i%",VoiceCommand.intToString(i)), vp);
		}
	}
	
	public void addPattern(String p,VoicePatternRunnable r )
	{
		if(r==null)
		{
			L.e(this, "no valid runnable for "+p);
			return;
		}
		if(p.contains("%i%"))
			addNumberPatterns(p, r);
		else patterns.put(p, new VoicePattern(p, r));
	}	
		public VoicePattern getMatchingPattern(String s)
		{
			for(VoicePattern p : patterns.values())
			{
				if(p.isMatch(s))
				{
					//L.d(this,"Found match " + p.getPattern());
					return p;				
				}
			}
			return null;
		}
	public boolean isMatch(String s)
	{
		//L.d(this,"isMatch");
		for(VoicePattern p : patterns.values())
		{
			if(p.isMatch(s))
			{
				L.d(this,"Found match " + p.getPattern());
				return true;				
			}
		}
		return false;
	}
	
	public void Run(String p, String cmd)
	{
		try 
		{
			if(p==null || cmd==null || patterns==null)
			{
				L.e(this, "params null");
				return;
			}
			
			if (patterns.containsKey(p))
			{
				VoicePattern vp = patterns.get(p);
				if (vp != null)
					vp.Run(cmd);
				
			} else
				L.d(this, p + " not found in patterns");
		} catch (Exception e) {
			L.ex(this, "Command Run", e);
		}
	}
	
	public abstract void initPatterns();
	
	
}
