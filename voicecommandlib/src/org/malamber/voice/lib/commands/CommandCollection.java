package org.malamber.voice.lib.commands;

import java.util.ArrayList;
import java.util.regex.Pattern;

import org.malamber.common.Log.L;
import org.malamber.voice.lib.commands.applications.OpenCommand;
import org.malamber.voice.lib.commands.touch.TouchCommand;

import android.content.Context;

public class CommandCollection extends ArrayList<Command> {

	private static final long serialVersionUID = -1830493455753160768L;
	Context context;
	public ArrayList<Pattern> pp = new ArrayList<Pattern>();

	public CommandCollection(Context c) {
		context = c;
	}

	private static void addCommand(CommandCollection cc, Command c) {
		cc.add(c);
		/*
		 * for(String s : c.getRegex()) { Pattern p = Pattern.compile(s);
		 * if(!cc.pp.contains(p)){ cc.pp.add(p); } else { L.e(null, "pattern " +
		 * p.pattern() + "already exists"); } }
		 */
	}

	public static CommandCollection createBaseCommandSet(Context c) {
		CommandCollection cc = new CommandCollection(c);
		try {
			long start = System.currentTimeMillis();
			addCommand(cc, new OpenCommand(c));
			addCommand(cc, new MoveCommand(c));
			addCommand(cc, new MediaCommand(c));
			addCommand(cc, new TouchCommand(c));
			addCommand(cc, new CallCommand(c));
			addCommand(cc, new SwipeCommand(c));
			addCommand(cc, new ButtonCommand(c));
			long startTime = System.currentTimeMillis() - start;
			L.i(null, "createBaseCommandSet:startuptime = " + startTime);
		} catch (Exception e) {
			L.ex(null, "createBaseCommandSet", e);
		}
		return cc;
	}

	public Command lookupCommand(String s) {
		L.d(this, "lookupCommand: '" + s + "'");
		for (Command vc : this)
			if (vc.isMatch(s))
				return vc;
		return null;
	}
}
