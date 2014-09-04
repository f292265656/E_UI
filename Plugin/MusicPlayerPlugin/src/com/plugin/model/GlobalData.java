package com.plugin.model;

import android.content.Context;
import android.view.LayoutInflater;

public  class GlobalData {

	private  static Context pluginContext;
	private static Context mainContext;
	private static LayoutInflater lif;
	public static Context getPluginContext() {
		return pluginContext;
	}
	public static void setPluginContext(Context pluginContext) {
		GlobalData.pluginContext = pluginContext;
	}
	public static Context getMainContext() {
		return mainContext;
	}
	public static void setMainContext(Context mainContext) {
		GlobalData.mainContext = mainContext;
	}
	public static LayoutInflater getLif() {
		return lif;
	}
	public static void setLif(LayoutInflater lif) {
		GlobalData.lif = lif;
	}


}
	
	
	
