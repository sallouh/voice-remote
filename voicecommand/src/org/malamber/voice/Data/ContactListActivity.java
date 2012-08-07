package org.malamber.voice.Data;

import org.malamber.logging.L;
import org.malamber.voice.R;
import org.malamber.voice.activities.BaseVoiceActivity;
import org.malamber.voice.commands.VoicePatternRunnable;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ContactListActivity extends BaseVoiceActivity
implements OnItemClickListener
{

	SharedPreferences prefs;
	ListView list;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_contacts);
		L.Tag = "voice";

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		ContactList cl = new ContactList();
		ListAdapter la = new ListAdapter(this, cl);
		list.setAdapter(la);
		list.setOnItemClickListener(this);
	}

	@Override
	protected void initPatterns() {
		
		patterns.put("", new VoicePatternRunnable(){

			@Override
			public void Run(String result) {
			
				
			}});
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> pos, View arg1, int arg2, long arg3) {
		
		
	}
	public class ListAdapter extends BaseAdapter
	{
		Context context;
		ContactList data;
		ListAdapter(Context c, ContactList cl)
		{
			data=cl;
			context=c;
		}

		@Override
		public View getView(int pos, View arg1, ViewGroup arg2) {
			
			Contact c = data.get(pos);
			LayoutInflater li = getLayoutInflater();
            View v = li.inflate(R.layout.dialog_contacts_item, null);
            TextView tv = (TextView) v.findViewById(R.id.textView);
            ImageView iv =  (ImageView) v.findViewById(R.id.imageView);
            
            iv.setImageBitmap(c.photo);
            tv.setText(c.name + "("+c.number.get(0)+")");
			return v;
		}
		
		@Override public int getCount() {	return data.size();	}

		@Override public Object getItem(int arg0) { return data.get(arg0); }

		@Override public long getItemId(int arg0) { return 0; }

		
		
	}
	
	

}
