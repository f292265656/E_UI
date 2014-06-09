package com.eteam.e_ui;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import chen.tools.PluginInf;
import chen.tools.Plugins;

import com.eteam.e_ui.view.PluginDrawerLayout;
import com.eteam.e_ui.view.SlidingView;
import com.eteam.e_ui.view.ViewPagerL_R;
import com.eteam.e_ui.view.WorkSpace;

public class MainActivity extends Activity implements View.OnClickListener
{
	private static String TAG="MainActivity";
	private Context context;
	public static SlidingView slidingViewGroup;
	//viewPager部分
	private ViewPagerL_R viewPager;
	private ArrayList<View> viewPager_viewList;
	//drawer部分
	private TextView top;
	private LinearLayout l;
	//workspace
	private WorkSpace workSpace;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		context=this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Init();
		bindEvent();

	}



	private void Init()
	{
	
		slidingViewGroup = (SlidingView) findViewById(R.id.SlidingViewGroup);
		viewPager=(ViewPagerL_R)findViewById(R.id.viewpager);
		viewPager_viewList=new ArrayList<View>();
		
		workSpace=new WorkSpace(context);
		
		l=(LinearLayout) findViewById(R.id.bottomLinearLayout);
		initViewPager();

				
	}
	private void initViewPager()
	{
		viewPager_viewList.add(new PluginDrawerLayout(context));
		viewPager_viewList.add(workSpace);
		viewPager_viewList.add(new TextView(context));
		viewPager.setViewDate(viewPager_viewList);
	}


	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		 if(v==l)
		{
			slidingViewGroup.shrink();
		}
	}
	protected void bindEvent()
	{
		//top.setOnClickListener(this);
		l.setOnClickListener(this);
	
		
	}



/*	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Boolean need=false;
		if(Plugins.pluginClass!=null)
		{
			
			try
			{
				Method m=Plugins.pluginClass.getDeclaredMethod("needActivityResult");
				if(m!=null)
				{
					need=(Boolean)m.invoke(Plugins.pluginObject);
				}
				if(need==true)
				{
					Class[] argType=new Class[]{int.class,int.class,Intent.class};
					Object[] argParam=new Object[]{requestCode,resultCode,data};
					Method exMethod=Plugins.pluginClass.getDeclaredMethod("onActivityResult", argType);
					exMethod.invoke(Plugins.pluginObject, argParam);
					Log.i(TAG, "requestCode is "+requestCode);
				}
				
			} catch (NoSuchMethodException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}*/
}
