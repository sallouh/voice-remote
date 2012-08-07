/**
 * Copyright 2012, Malamber
 * http://www.malamber.org/android
 */

package org.malamber.voice.sevice;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.malamber.logging.L;
import org.malamber.voice.aidl.VoiceServiceBinder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class VoiceService extends Service {

	PlaybackService ps = new PlaybackService();

	@Override
	public void onCreate() {
		L.d(this, "onCreate");
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		L.d(this, "onDestroy");
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {

		return binder;
	}

	class Comm {

		public Pattern p;
		public Method m;
	}

	List<Comm> patterns = new ArrayList<Comm>();

	VoiceServiceBinder.Stub binder = new VoiceServiceBinder.Stub() {

		@Override
		public int lookupResult(String res) throws RemoteException {
			for (Comm c : patterns) {
				if (c.p.matcher(res).matches()) {
					try {
						c.m.invoke(null, (Object[]) null);
					} catch (IllegalArgumentException e) {

						L.ex(this, "", e);
					} catch (IllegalAccessException e) {

						L.ex(this, "", e);
					} catch (InvocationTargetException e) {

						L.ex(this, "", e);
					}
					return 1;
				}
			}
			return 0;
		}

		@Override
		public void restrictPatterns(String[] spatterns, String cls,
				String method) throws RemoteException {
			patterns.clear();
			Class<?> clas;
			Method m = null;
			try {
				clas = Class.forName(cls);
				m = clas.getMethod(method, (Class<?>) null);
			} catch (ClassNotFoundException e) {

				L.ex(this, "", e);
			} catch (NoSuchMethodException e) {

				L.ex(this, "", e);
				return;
			}

			for (String s : spatterns) {
				Pattern p = Pattern.compile(s);
				Comm c = new Comm();
				c.p = p;
				c.m = m;
				patterns.add(c);
			}

		}
	};
}
