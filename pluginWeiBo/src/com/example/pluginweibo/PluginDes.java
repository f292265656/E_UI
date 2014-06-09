package com.example.pluginweibo;


import com.e_ui.e_ui_support.IDescription;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PluginDes implements IDescription
{
	private static String TAG = "PluginDes";
	private static Context mainContext,pluginContetx;

	

	WeiBoFragement wp;

	public PluginDes(Context mainContetx,Context pluginContetx)
	{
	
		this.pluginContetx = pluginContetx;
		this.mainContext=mainContetx;
		wp=new WeiBoFragement();


		Log.i(TAG, "PluginDes");
	}

	@Override
	public Fragment getPlugin()
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "getPlugin");
		return wp;	
	}
	public static Context getMainContext()
	{
		return mainContext;
	}
	public static Context getPluginContxt()
	{
		return pluginContetx;
	}
	public Boolean needActivityResult()
	{
		return true;
	}
	
	
	public static class WeiBoFragement extends Fragment
	{
		
		private TextView tv;
		WeiBoView w=null;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			if(w==null)
			{
				w =new WeiBoView(PluginDes.getPluginContxt());
			}
			
			return w;
		}
	}

	

}
