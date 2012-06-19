package org.malamber.voice.lib.views;

import org.malamber.common.Log.L;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GridFragment extends Fragment {

	GridView grid;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle b) {
		L.d(this, "onCreateView ");
		if (container == null) { return null; }
		Activity a = this.getActivity();
		Display display = a.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		Rect r = new Rect(0, 0, width, height);
		GridView gv = new GridView(a.getApplicationContext(), a, r);
		return gv;
	}
	@Override
	public void onViewCreated(View v, Bundle savedInstanceState) {
		L.d(this, "onViewCreated ");
		super.onViewCreated(v, savedInstanceState);
	}
}
