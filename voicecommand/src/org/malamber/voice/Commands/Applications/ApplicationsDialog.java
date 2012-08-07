package org.malamber.voice.commands.Applications;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.malamber.logging.L;
import org.malamber.voice.R;
import org.malamber.voice.VoiceCommand;
import org.malamber.voice.activities.BaseVoiceActivity;
import org.malamber.voice.commands.VoicePatternRunnable;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ApplicationsDialog extends BaseVoiceActivity
implements  OnItemClickListener
{
	final public static int APPS_PER_PAGE = 10;
	ListView gridView;	
	
	List<ResolveInfo> list;	
	int currentPage=0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		L.d(this, "onCreate");
		try {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			
			this.setContentView(R.layout.dialog_apps);
			
			
			initAppList();
		}
		catch(Exception e)
		{
			
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		
		L.d(this, "onItemClick "+pos);
		run(pos);
	}
	private void run(int index)
	{		
		L.d(this, "Run application :"+index);
		final PackageManager pm = getPackageManager();
		ResolveInfo r = (ResolveInfo) gridView.getAdapter().getItem(index);
		try {
			ApplicationInfo ai = pm.getApplicationInfo(r.activityInfo.packageName, 0);
			runApp(pm, ai);
			finish();
		} catch (NameNotFoundException e) {
			
			e.printStackTrace();
		}
	}
	private void runApp(PackageManager pm, ApplicationInfo ai)
	{
		Intent i= pm.getLaunchIntentForPackage(ai.packageName);
		this.startActivity(i);
	}
		
	
	
	PackageManager pm;
	int numPages=0;
	private void initAppList()
	{	
		pm  = getPackageManager();
		
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		  intent.addCategory(Intent.CATEGORY_LAUNCHER);
		  
        //List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        list = pm.queryIntentActivities(intent, PackageManager.PERMISSION_GRANTED);
        
        for(ResolveInfo r : list)
        {
        	try {
				
				String pname=r.activityInfo.packageName;
				if(pname.contains("music")
					|| pname.contains("email")
					|| pname.contains("messaging")
					|| pname.contains("music")
						)
				{
					L.i(this, r.activityInfo.packageName);
				}
						
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
        }
        numPages=list.size()/APPS_PER_PAGE + ((list.size() % APPS_PER_PAGE)>0?1:0);
        Collections.sort(list, new SortResolveInfo(pm));
		
        gridView = (ListView) this.findViewById(R.id.gridView);
		
        showListItems(pm, 0);
		gridView.setOnItemClickListener(this);
		   
	}
	private void showListItems(PackageManager pm, int page)
	{
		try {
			L.d(this, "remainder=:"+(list.size() % APPS_PER_PAGE)+ "::numPages-1="+ (numPages-1) );
			
			if((list.size() % APPS_PER_PAGE)>0 && page  > (numPages-1))
			{				
				page = currentPage = 0;
			}
			else if(page  >= numPages )				
				page = currentPage = 0;
			else if(page < 0)
				page  = currentPage = numPages + ((list.size() % APPS_PER_PAGE)>0 ? -1 : 0);
			L.d(this, "showListItems:"+page);
			
			int p = page +1;
			TextView tv = (TextView) this.findViewById(R.id.textPageNum);
			tv.setText("Page " + p + " of " + numPages);
			List<ResolveInfo> l = new ArrayList<ResolveInfo>();	
			
			
			L.d(this, "list size ="+list.size());
			for(int i = page *APPS_PER_PAGE; i < page *APPS_PER_PAGE+APPS_PER_PAGE; i++)
			{
				if(i>=list.size())break;
				
				l.add(list.get(i));
			}
			
			gridView.setAdapter(new AppAdapter(this,pm, l));
			
		} catch (Exception e) {
			L.ex(this, "", e);
		}
	}
	
	public void onHistory(View v)
	{
		
	}
	void showAll()
	{
		
	}
	void showFavorites()
	{
		
	}
	public void onPrevious(View v)
	{
		showListItems(pm, --currentPage);
	}	
	
	public void onNext(View v)
	{
		showListItems(pm, ++currentPage);
	}
	
	public class AppAdapter extends BaseAdapter
	{
		List<ResolveInfo> packages;
		PackageManager pm;
		public AppAdapter(Context c,PackageManager pp, List<ResolveInfo> p)
		{
			packages = p;
			pm =pp;
		}	

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{			
			ResolveInfo ai = packages.get(position);
			LayoutInflater li = getLayoutInflater();
            View v = li.inflate(R.layout.dialog_apps_item, null);
            TextView tv = (TextView) v.findViewById(R.id.textView);
            ImageView iv =  (ImageView) v.findViewById(R.id.imageView);
            ((TextView) v.findViewById(R.id.textNum)).setText("("+(position +1) + ")");
            iv.setImageDrawable(ai.loadIcon(pm));
            tv.setText(ai.loadLabel(pm));
			return v;
		}
		
		@Override
		public int getCount() {	return packages.size();	}
		@Override
		public Object getItem(int arg0) { return packages.get(arg0); }

		@Override
		public long getItemId(int arg0) { return 0;	}
		
	}
	
	public class SortResolveInfo implements Comparator<ResolveInfo>{
		
		PackageManager pm;
		public SortResolveInfo(PackageManager p)
		{
			pm=p;
		}
		
	    public int compare(ResolveInfo o1, ResolveInfo o2)
	    {
	    	String s1=new String((String) o1.loadLabel(pm));
	    	String s2=new String((String) o2.loadLabel(pm));
	        return s1.toLowerCase().compareTo(s2.toLowerCase());
	    }
	}

	@Override
	protected void initPatterns() {
		L.d(this, "initPatterns");
		
		try {
			patterns.put("favorites", new VoicePatternRunnable(){
				@Override public void Run(String cmd) {
					showFavorites();					
				}});
			patterns.put("all", new VoicePatternRunnable(){
				@Override public void Run(String cmd) {
					showAll();					
				}});
			patterns.put("next", new VoicePatternRunnable(){
				@Override public void Run(String cmd) {
					onNext(null);					
				}});
			patterns.put("previous", new VoicePatternRunnable(){
				@Override public void Run(String cmd) {
					onPrevious(null);					
				}});
			
			VoiceCommand.addNumberPatterns(patterns, number);			
			patterns.put("select.[0-9]*", number);
			patterns.put("select.number.[0-9]*", number);
			
		} catch (Exception e) {
			L.ex(this, "initPatterns", e);
		}
		
	}

	VoicePatternRunnable number = new VoicePatternRunnable()
	{
		@Override
		public void Run(String cmd) {			
			try {
				int i = VoiceCommand.parseNumberString(cmd);
				if (i >= 0 && i < APPS_PER_PAGE) {
					int index = currentPage * APPS_PER_PAGE + i;
					run(index);
				}
			} catch (Exception e) {
				L.ex(this, "Number Run:"+cmd, e);
			}			
		}		
	};
	

}
