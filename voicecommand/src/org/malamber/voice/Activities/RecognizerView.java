package org.malamber.voice.Activities;

import org.malamber.voice.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;
import android.view.animation.Animation;

public class RecognizerView extends View {

	public RecognizerView(Context context, int w, int h) {
		super(context);
		height = h;
		width=w;		
	}
	int height = 0;
	int width=0;
	Animation anim;
	Bitmap circle;
	String text;
	boolean animate=false;
	
	
	@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextSize(18);
		p.setTypeface(Typeface.DEFAULT_BOLD);
		p.setColor(Color.YELLOW);
        canvas.drawText("Activity", 100, 100, p);
        
        
	}
	
	public void setRecognitionOk()
	{
		circle = BitmapFactory.decodeResource(getResources(), R.drawable.ball_green);
		this.invalidate();
	}
	public void setError(String error)
	{
		text=error;
		circle = BitmapFactory.decodeResource(getResources(), R.drawable.ball_red);
		this.invalidate();
	}
	public void setRecording()
	{
		animate=true;
		text = "Recording";
		circle = BitmapFactory.decodeResource(getResources(), R.drawable.working_yellow);
		this.invalidate();
	}
	public void setWaitingForResults()
	{
		animate=true;
		circle = BitmapFactory.decodeResource(getResources(), R.drawable.working_green);
		this.invalidate();
	}
	
	public void setImage(int id)
	{
		
	}
	

}
