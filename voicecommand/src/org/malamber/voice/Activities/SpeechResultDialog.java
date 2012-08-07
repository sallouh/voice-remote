package org.malamber.voice.activities;

import java.util.ArrayList;

import org.malamber.logging.L;
import org.malamber.voice.R;
import org.malamber.voice.VoiceCommand;
import org.malamber.voice.commands.VoicePatternRunnable;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SpeechResultDialog extends BaseVoiceActivity 
implements  OnItemClickListener{

	ListView list;
	ArrayList<String> results;
	
	
	public void setResults(ArrayList<String> r)
	{
		results = r;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_results);
	
		Bundle extras = getIntent().getExtras();
		
		ArrayList<String> al = extras.getStringArrayList("results");
		ListAdapter la = new ListAdapter(this, al);
		list = (ListView) this.findViewById(R.id.listResultChooser);
		list.setAdapter(la);
        
	}	

	VoicePatternRunnable number = new VoicePatternRunnable()
	{
		@Override public void Run(String cmd) {			
			int index  = VoiceCommand.parseNumberString(cmd);
			try
			{
				service.callCommand(results.get(index));
			}
			catch (RemoteException e)
			{
				
			}
		}
		
	};
	@Override
	protected void initPatterns() {
		VoiceCommand.addNumberPatterns(patterns, number);
		
	}

	private void selectItem(int index)
	{
		String msg = (String) list.getAdapter().getItem(index);
		L.toast(getApplicationContext(), msg);
		
	}
	

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		selectItem(pos);		
	}
	public class ListAdapter extends BaseAdapter
	{
		ArrayList<String> results;
		Context context;
		
		public ListAdapter(Context c, ArrayList<String> r)
		{
			results = r;
			context = c;
		}
		@Override
		public int getCount() {
			return results.size();
		}

		@Override
		public Object getItem(int arg0) {
			return results.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return results.get(position).hashCode();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			RelativeLayout rowLayout=null;
	    		
			try {
				rowLayout = (RelativeLayout)LayoutInflater.from(context).inflate(R.layout.row, parent, false);
				TextView name = (TextView)rowLayout.findViewById(R.id.textOption);
				name.append(position + " - " + results.get(position));
				
			} catch (Exception e) {
				
			}
			return rowLayout;
		}
		
	}


}
