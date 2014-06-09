package com.eteam.e_ui.view;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import chen.tools.AppInfo;

import com.eteam.e_ui.R;

public class DrawerView extends GridView implements OnItemClickListener
{
	private static String TAG="DrawerView";
    private Context context;
    private AppInfo appInf;
    private Intent jumpIntent;
    private PackageManager pm;
    private ArrayList<AppInfo.Info> appInfList;
 
    
	public DrawerView(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
		this.context=context;
		init();
	}
    
    
	public DrawerView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context=context;
		init();
	}
	
	protected void bindData()
	{
		appInf=new AppInfo(context);
		appInf.loadInfo();
		this.setAdapter(new DrawerViewAdapter(appInf.getInfList()));
	}
	protected void bindEvent()
	{
		setOnItemClickListener(this);
	}
	protected void init()
	{
		pm=context.getPackageManager();
		bindData();
		bindEvent();
	}
	

	private class DrawerViewAdapter extends BaseAdapter
	{
       
        public DrawerViewAdapter(ArrayList<AppInfo.Info> infList)
        {
        	appInfList=infList;
        }
		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return appInfList.size();
		}

		@Override
		public Object getItem(int arg0)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0)
		{
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2)
		{
			// TODO Auto-generated method stub
			    View v;
				ImageView app_icon;
			    TextView app_name;
				v=LayoutInflater.from(context).inflate(R.layout.drawer_item, null);
				app_icon=(ImageView) v.findViewById(R.id.appIcon);
				app_name=(TextView)v.findViewById(R.id.appName);
				app_icon.setImageDrawable(appInfList.get(arg0).app_icon);
				app_name.setText(appInfList.get(arg0).app_name);
		
				
			return v;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		// TODO Auto-generated method stub
		jumpIntent=pm.getLaunchIntentForPackage(appInfList.get(arg2).packagename);
		context.startActivity(jumpIntent);
		
		
	}

}
