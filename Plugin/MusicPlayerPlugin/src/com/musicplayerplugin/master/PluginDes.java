package com.musicplayerplugin.master;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;

import com.e_ui.e_ui_support.IDescription;
import com.plugin.model.GlobalData;
import com.plugin.view.MPlayerFragment;

public class PluginDes implements IDescription {
	MPlayerFragment ml;
	public PluginDes(Context mainContext,Context pluginContext)
	{
		GlobalData.setMainContext(mainContext);
		GlobalData.setPluginContext(pluginContext);
		GlobalData.setLif(LayoutInflater.from(pluginContext));
        ml=new MPlayerFragment();		
	}

	@Override
	public Fragment getPlugin() {
		// TODO Auto-generated method stub
		return ml;
	}

}
