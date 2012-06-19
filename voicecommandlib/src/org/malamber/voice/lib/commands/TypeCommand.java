package org.malamber.voice.lib.commands;

import android.content.Context;

public class TypeCommand extends Command {

	String lastTyped;

	public TypeCommand(Context c) {
		super(c);
	}

	@Override
	public void initPatterns() {
		VoicePatternRunnable vpr = new VoicePatternRunnable() {

			@Override
			public void Run(String result) {
				// String s = result.replace("type ", "");
			}
		};
		addPattern("type.*", vpr);
		addPattern("backspace", vpr);
		addPattern("type.*", vpr);
	}
}
