package org.malamber.voice.Commands;

import java.util.regex.Pattern;

import org.malamber.common.Log.L;

public class VoicePattern {
	public VoicePattern(String p, VoicePatternRunnable r)
	{
		pattern=Pattern.compile(p);
		vpr=r;
	}
	Pattern pattern;
	VoicePatternRunnable vpr;
	public String getPattern()
	{
		return pattern.pattern();
	}
	public boolean isMatch(String s)
	{
		return pattern.matcher(s).find();
	}
	public void Run(String s)
	{
		
		try {
			if(vpr==null)
			{
				L.e(this,"vpr==null");
				return;
			}
			vpr.Run(s);
		} catch (Exception e) {
			L.ex(this, "Run", e);
		}
	}

}
