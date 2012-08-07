package org.malamber.voice.data;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;


public class Contact
{
	public String name;
	public ArrayList<String> number = new ArrayList<String>();
	public String email;	
	public Bitmap photo;
	
	Context context;
	
	public Contact(Context c)
	{
		context=c;
	}
	
	public void call(String num)
	{
		Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + num ));
		context.startActivity(i);
	}
	
	public void text(String num)
	{
		
	}
	public void email(String num)
	{
		
	}
}
