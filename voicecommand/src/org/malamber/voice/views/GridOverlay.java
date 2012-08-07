package org.malamber.voice.views;

import java.util.ArrayList;

import org.malamber.logging.L;
import org.malamber.voice.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.View;

public class GridOverlay extends View{

	private Rect 		rect;	
	private int  		columns;
	private ArrayList<GridOverlay>
						grids = new ArrayList< GridOverlay>();	

	private Paint		p = new Paint(Paint.ANTI_ALIAS_FLAG);
	private int 		color = Color.YELLOW;	
	private int 		touchIndex = -1;
	
	public GridOverlay(Context c, Rect r, int col) {
		super(c);
		rect = r;
		columns = col;
		
		p.setColor(color);
		p.setStrokeWidth(1);
		
		init();
	}
	public GridOverlay(Context c, Rect r, int col, int lcolor) {
		super(c);
		rect = r;
		columns = col;
		
		p.setColor(lcolor);
		p.setStrokeWidth(1);	
		
		init();
	}
	 
	void init()
	{
		calculateGrid();
		calculateArea();
	}
	private void calculateGrid()
	{	
		int h = rect.height();
		int w = rect.width();
		
		int deg2 =  (int) Math.toDegrees (Math.atan2(w, h));
		
		this.columns= 3;
		if(deg2 > 50)
			this.columns= 2;
	}
	private void calculateArea()
	{
		int area = rect.width() * rect.height();
		
		L.i(this, "area = " + area);
	}
	
	public void setIndex(int i)
	{
		
		this.invalidate();
	}
	
	public GridOverlay getSubGridView(int i)
	{		
		GridOverlay gv = null;
		
		if(i>=0 && i < grids.size()){
			gv= grids.get(i - 1);
			gv.initGrids();
		}
		return gv;
	}
	
	public Rect getRect()
	{		
		return rect;
	}
	public int getColumns()
	{		
		return columns;
	}
	public Rect getSubRect(int i)
	{
		return grids.get(i - 1).getRect();
	}
	
	public void initGrids()
	{		
		//L.d(this, "initGrids");
		try {
			grids.clear();
			
			int w = rect.width() / columns;
			int h = rect.height() / 3;
			//L.i(this, "w="+w+" h="+h);
			for (int i = 0; i < (columns * 3); i++)
			{			
				
				int col = i % columns;
				int row = i / columns;
				//L.i(this, "col="+col+" row="+row);
				Rect r = new Rect();
				int left = rect.left + col * w;
				int top = rect.top + row * h;
				int right = left + w;
				int bottom = top + h;
				r.set(left,
					  top, 
					  right,
					  bottom);
				
				
				//L.i(this, "GridView("+i+") = left="+r.left+" top="+r.top+ " width="+r.width()+" height=" + r.height());
				GridOverlay gv = new GridOverlay(getContext(), r, 2, Color.YELLOW);
				
				grids.add(gv);
			}
			L.i(this, "initGrids=" + grids.size());
			
		} catch (Exception e) {
			L.ex(this, "initGrids", e);
		}
	}
	
	public void touch(int index)
	{
		touchIndex = index;
		this.invalidate();		
	}
	
	private void drawNumbers(Canvas canvas)
	{		
		try {
			
			p.setTextSize(18);
			p.setTypeface(Typeface.DEFAULT_BOLD);
			for (int i = 0; i < grids.size(); i++)//GridView gv : grids)
			{
				GridOverlay gv = grids.get(i);
				Rect rr = gv.getRect();
				canvas.drawText("" +( i + 1), rr.centerX() - 4, rr.centerY() + 4, p);

			}
		} catch (Exception e) {
			L.ex(this, "drawNumbers", e);
		}
		
	}
	
	private void drawRect(Canvas canvas, int color)
	{			
		
		Rect r = rect;
		
		canvas.drawLine(r.left, r.top, r.right, r.top, p);//top
		canvas.drawLine(r.left, r.bottom, r.right, r.bottom, p);//bottom
		
		canvas.drawLine(r.left, r.top, r.left, r.bottom, p);//left
		canvas.drawLine(r.right, r.top, r.right, r.bottom, p);//right
		
		// vertical lines
		int x = r.left + r.width()/columns;
		canvas.drawLine(x, r.top, x, r.bottom, p);	
		
		if(columns == 3)
		{
			x+=r.width()/columns;
			canvas.drawLine(x, r.top, x, r.bottom, p);
		}
		
		// Horizontal lines
		int y = r.top + r.height() / 3;
		canvas.drawLine(r.left, y, r.right, y, p);		
		y+= r.height() / 3;
		canvas.drawLine(r.left, y, r.right, y, p);
		
	}
	
	private void drawTouch(Canvas c,  int i)
	{
		
		Rect r = this.getSubRect(i);	
		Bitmap picture = BitmapFactory.decodeResource(getResources(), R.drawable.touch);
		int h = picture.getHeight();
		int w = picture.getWidth();
		
		int left = r.centerX() - w/2;
		int top = r.centerY() - h/2;
		c.drawBitmap(picture, left, top, null);
		//c.drawRect(r, p);
	}
	
	boolean drawn=false;
	@Override
	protected void onDraw(Canvas canvas)
	{		
		super.onDraw(canvas);
		
		L.d(this, "onDraw");
		p.setARGB(128, 255,255,0);
		try
		{
			if(!drawn)
			{
				p.setARGB(128, 255,255,0);
				canvas.drawRect(rect, p);
				Thread.sleep(2000);
				this.invalidate();
				drawn=true;
				return;
			}
			
			//p.setARGB(255, 0,0,0);
			//canvas.drawRect(rect, p);
			drawRect(canvas, Color.YELLOW);
			if(touchIndex >= 0)		
				drawTouch(canvas, touchIndex);
			drawNumbers(canvas);
		}
		catch (InterruptedException e)
		{
			
		}
		
		
		
	}
	
}
