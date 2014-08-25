package com.plugin.master;

/*
 * 插件入口类
 */
import com.e_ui.e_ui_support.IDescription;
import com.plugin.model.GlobalSetting;
import com.plugin.view.WeiBoView;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PluginDes implements IDescription
{
	private static String TAG = "PluginDes";
	private WeiBoFragement wp;

	public PluginDes(Context mainContetx,Context pluginContetx)
	{
	    GlobalSetting.setMainContext(mainContetx);
	    GlobalSetting.setPluginContext(pluginContetx);
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
				w =new WeiBoView(GlobalSetting.getPluginContext());
			}
			
			return w;
		}
	}

}
