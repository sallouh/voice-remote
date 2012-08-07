package org.malamber.voice.Commands.Touch;

import java.util.Timer;
import java.util.TimerTask;

import org.malamber.logging.L;
import org.malamber.voice.R;
import org.malamber.voice.VoiceCommand;
import org.malamber.voice.SendEvents.TouchEvent;
import org.malamber.voice.activities.BaseVoiceActivity;
import org.malamber.voice.commands.VoicePatternRunnable;
import org.malamber.voice.views.GridOverlay;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class GridOverlayActivity extends BaseVoiceActivity 
{
	GridOverlay grid;	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		L.Tag = "voice";
		L.d(this, "onCreate");		
		
		try {
			requestWindowFeature(Window.FEATURE_NO_TITLE);	       
	        
			setContentView(R.layout.grid);
			
			Bundle extras = getIntent().getExtras();
			getArgs(extras);
		
			resetTimer();
			
		} catch (Exception e) {
			L.ex(this, "onCreate", e);
		}
	}
	
	private void getArgs(Bundle extras)
	{		
		try {
			if(extras==null){
				showGrid(-1);
				return;
			}
			
			int index = extras.getInt("index", 0);
			
			int index1 = index;			
			int index2 = 0;//extras.getInt("index2", 0);
			int index3 = 0;//extras.getInt("index3", 0);
			L.i(this, "getArgs:"+index) ;
			if(index > 99)
			{				
				index1 = index / 100;				
				index2 = index % 100 / 10;
				index3 = index  % 100 % 10;
			}
			else if(index > 9)
			{
				L.d(this, "getArgs:"+index1 + " "+index2) ;
				index1 = index / 10;
				index2 = index % 10;
			}
			
			showGrid(index1);
			
			if(index2 > 0)
				showGrid(index2);
			
			if(index3 > 0)
				grid.touch(index3);
						
			
			int touch = extras.getInt("touch", 0)-1;			
			if (touch > 0)
				sendTouch(touch);		
			
			
		} catch (Exception e) {
			L.ex(this, "getArgs ", e);
		}
	}	
	
	private void showGrid(int g1) 
	{	
		
		L.d(this, "showGrid:" + g1);
		
		try 
		{			
			Rect r = new Rect();
			//layout.removeAllViews();
			
			if(grid == null)
			{				
				Display display = getWindowManager().getDefaultDisplay();
				int Width = display.getWidth();				
				int Height = display.getHeight();				
				
				r.set(0, 0,Width,Height);
				grid = new GridOverlay(this, r, 3 );	
				grid.initGrids();
				
				if(g1 >= 0)
				{
					grid = grid.getSubGridView(g1);	
					if(grid == null)return;
								
					grid.initGrids();
					r = grid.getRect();		
				}
			}
			else
			{	
				grid = grid.getSubGridView(g1);				
				if(grid == null) return;
				
				grid.initGrids();
				r = grid.getSubRect(g1);
			}
			
			addView(grid, r, g1);
			
		} catch (Exception e) {
			L.ex(this, "showGrid", e);
		}		
	}	

	
	void addView(GridOverlay grid2, Rect r, int index)
	{	
		L.d(this, "addView "+index);
		try
		{
			RelativeLayout layout = (RelativeLayout) this.findViewById(R.id.rl);
			layout.removeAllViewsInLayout();
			
			Display display = getWindowManager().getDefaultDisplay();
			int Width = display.getWidth();				
			int Height = display.getHeight();
			LayoutParams params = new LayoutParams(Width,Height );
			
			layout.addView(grid2, params);
		}
		catch (Exception e)
		{
			L.ex(this, "", e);
		}
	}
	private void sendTouch(int touch)
	{
		Rect r = grid.getSubRect(touch);		
		Point p = new Point(r.centerX(), r.centerY());		
		TouchEvent.SendTouch(p);
	}	
	
	
	@Override	
	protected void initPatterns() {
		L.d(this, "initPatterns");
		patterns.put("cancel", new VoicePatternRunnable(){
			@Override public void Run(String result) {
				finish();			
			}});
		
		for(int i = 1; i < 10; i++)		
			patterns.put("" + i, new VoicePatternRunnable(){
				@Override public void Run(String pattern) {					
					try 
					{						
						int i = VoiceCommand.parseNumberString(pattern) - 1;						
						showGrid(i);						
						resetTimer();						
						
					} catch (Exception e) {
						L.ex(this, "Number:"+pattern, e);
					}
				}		
			});
	}
	
	private void resetTimer()
	{
		try {
			timer.cancel();
			timer.schedule(timertask, 3 * 1000);
		} catch (Exception e) {
			L.ex(this, "resetTimer", e);
		}
	}

	Timer timer = new Timer();
	TimerTask timertask = new TimerTask(){
		@Override public void run() {
			finish();					
		}
	};

	
}

