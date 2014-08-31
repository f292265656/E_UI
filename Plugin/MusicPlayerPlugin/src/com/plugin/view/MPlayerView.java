package com.plugin.view;



import com.musicplayerplugin.master.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;



public class MPlayerView extends LinearLayout {
	

	private View rootView;
	public MPlayerView(Context context) {
		super(context);
		
		LayoutInflater lif=LayoutInflater.from(context);
		rootView=lif.inflate(R.layout.plugin_main, null);
		
		addView(rootView);
	}


}
