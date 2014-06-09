package com.eteam.e_ui.view;

import java.util.ArrayList;
import java.util.HashMap;




import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import chen.tools.PluginInf;
import chen.tools.Plugins;

import com.eteam.e_ui.R;

//Nevigation Drawer实现

public class PluginDrawerLayout extends LinearLayout implements
		View.OnClickListener
{

	private static String TAG = "PluginDrawerLayout";
	public final static int DrawerClosed = 0, DrawerSlide = 1,
			DrawerOpened = 2;
	private Context context = null;
	protected ListView pluginList_panel;
	// drawerlaout
	protected DrawerLayout drawerLayout;
	protected static int state = 0;
	protected Button refreash;
	protected LinearLayout leftPanel;
	// 数据
	protected ArrayList<PluginInf> pluginInfList;
	protected Plugins psInf;
	protected ArrayList<HashMap<String, Object>> data;
	protected SimpleAdapter panleAdapter;

	public PluginDrawerLayout(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public PluginDrawerLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	protected void init()
	{
		inflate(context, R.layout.plugins, this);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		pluginList_panel = (ListView) findViewById(R.id.pluginList);
		refreash = (Button) findViewById(R.id.refreash);
		leftPanel = (LinearLayout) findViewById(R.id.leftPanel);
		psInf = new Plugins(context);
		bindDate();
		panleAdapter = new SimpleAdapter(getContext(), data,
				R.layout.plugins_item, new String[] { "plugin_name" },
				new int[] { R.id.plugin_name });

		bindEvent();

	}

	protected void bindDate()
	{
		// 插件信息
		psInf.searchPlugins();
		pluginInfList = psInf.getPluginInflist();
		if (data == null)
		{
			data = new ArrayList<HashMap<String, Object>>();
		} else
		{
			data.clear();
		}

		for (PluginInf p : pluginInfList)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("plugin_name", p.lable);
			data.add(map);
		}

	}

	protected void refrashPlugins()
	{
		bindDate();
		panleAdapter.notifyDataSetChanged();
	}

	protected void bindEvent()
	{
		pluginList_panel.setAdapter(panleAdapter);
		drawerLayout.setDrawerListener(new PluDrawerListener());
		pluginList_panel.setOnItemClickListener(new DrawerItemClickListener());
		refreash.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0)
	{
		// TODO Auto-generated method stub
		if (arg0 == refreash)
		{
			refrashPlugins();
		}

	}

	public static int getDrawerState()
	{
		return state;
	}

	protected class PluDrawerListener implements DrawerListener
	{

		@Override
		public void onDrawerClosed(View arg0)
		{
			// TODO Auto-generated method stub
			state = DrawerClosed;
		}

		@Override
		public void onDrawerOpened(View arg0)
		{
			// TODO Auto-generated method stub
			state = DrawerOpened;

		}

		@Override
		public void onDrawerSlide(View arg0, float arg1)
		{
			// TODO Auto-generated method stub
			state = DrawerSlide;

		}

		@Override
		public void onDrawerStateChanged(int arg0)
		{
			// TODO Auto-generated method stub

		}

	}

	protected class DrawerItemClickListener implements
			ListView.OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3)
		{
			// 切换fragment
			// TODO Auto-generated method stub
			FragmentManager fm = ((Activity) context).getFragmentManager();
		
			Fragment f = pluginInfList.get(arg2).fragement;
			if (f != null)
			{
				fm.beginTransaction().replace(R.id.pluginContainer, f).commit();
				Log.i(TAG, f.toString());
			} else
			{
				Log.i(TAG, "f is null！");
			}
			pluginList_panel.setItemChecked(arg2, true);
			
			
			drawerLayout.closeDrawer(leftPanel);
			

		}

	}

	public static class PlanetFragment extends Fragment
	{
		public PlanetFragment()
		{
			// Empty constructor required for fragment subclasses
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			return inflater.inflate(R.layout.test, container, false);
		}
	}

}
