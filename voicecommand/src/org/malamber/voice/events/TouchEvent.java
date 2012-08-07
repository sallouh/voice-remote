package org.malamber.voice.SendEvents;

import java.util.ArrayList;
import java.util.List;

import org.malamber.common.shell.ShellExecutor;
import org.malamber.logging.L;

import android.graphics.Point;


public class TouchEvent {
	private static String cmd = "/system/bin/sendevent";
	private static String dev = "/dev/input/event3";

	public static void SendLongTouch(Point p) {
		SendLongTouch(p.x, p.y);
	}
	public static void SendLongTouch(int x, int y) {
		L.d(null, "Touch (" + x + "," + y + ")");

		try {
			sendEvent(dev, 3, 48, 104);
			sendEvent(dev, 3, 53, x);
			sendEvent(dev, 3, 54, y);
			touch(true);
			Thread.sleep(2 * 1000);
			sendEvent(dev, 3, 48, 0);
			sendEvent(dev, 3, 53, x);
			sendEvent(dev, 3, 54, y);
			touch(false);
		} catch (InterruptedException e) {
			L.ex("TouchEvent","SendLongTouch", e);
		}

	}

	public static void SendTouch(Point p) {
		SendTouch(p.x, p.y);
	}
	public static void SendTouch(int x, int y) {
		L.d(null, "Touch (" + x + "," + y + ")");
		sendEvent(dev, 3, 48, 104);
		sendEvent(dev, 3, 53, x);
		sendEvent(dev, 3, 54, y);
		touch(true);
		sendEvent(dev, 3, 48, 0);
		sendEvent(dev, 3, 53, x);
		sendEvent(dev, 3, 54, y);
		touch(false);
	}

	private static void touch(boolean down) {
		L.d(null, "touch " + (down ? "down" : "up"));
		sendEvent(dev, 0, 2, 0);
		sendEvent(dev, 3, 20, down ? 1 : 0);
		sendEvent(dev, 0, 0, 0);
	}

	private static void sendEvent(String device, int type, int code, int value) {
		//L.d(null, "sendEvent (" + device + " " + type + " " + code + " "	+ value);
		List<String> args = new ArrayList<String>();
		args.add(device);
		args.add("" + type);
		args.add("" + code);
		args.add("" + value);
		ShellExecutor.Run(cmd, args, false);
	}
}
/*
 * sub adbSend { my ( $device, $type, $code, $val ) = @_;
 * 
 * system("adb shell sendevent $device $type $code $val"); }
 * 
 * sub Click { my($x, $y) = @_; $device="/dev/input/event3"; adbSend($device,
 * 3,48,104); adbSend($device, 3, 53, $x); adbSend($device, 3, 54, $y);
 * FingerDownUp($device, 1); adbSend($device, 3, 48, 0); adbSend($device, 3, 53,
 * $x); adbSend($device, 3, 54, $y); FingerDownUp($device, 0); }
 * 
 * sub FingerDownUp { # $isDown: 1 - finger down, 0 - finger up my($device,
 * $isDown) = @_; adbSend($device, 0, 2, 0); adbSend($device, 3, 20, $isDown);
 * adbSend($device, 0, 0, 0); }
 */