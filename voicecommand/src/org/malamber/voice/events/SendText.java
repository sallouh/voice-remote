package org.malamber.voice.events;

import java.util.ArrayList;
import java.util.List;

import org.malamber.common.shell.ShellExecutor;
import org.malamber.logging.L;


public class SendText {

	private static String cmd = "/system/bin/input";

	

	public static void SendString(String s) {
		L.d(null, "SendString (" + s + ")");
		List<String> args = new ArrayList<String>();
		args.add("text");
		args.add(s);
		ShellExecutor.Run(cmd, args, true);
	}
}
