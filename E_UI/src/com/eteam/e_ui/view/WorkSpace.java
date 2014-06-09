package com.eteam.e_ui.view;

import chen.tools.Plugins;

import com.eteam.e_ui.MainActivity;
import com.eteam.e_ui.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;;

public class WorkSpace extends LinearLayout implements OnTouchListener
{
	private static String TAG="WorkSpace"; 
	private Context context;
	private TextView bottomPanel;
	public WorkSpace(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
		this.context=context;
		init();
		bindEvent();
	}
	
	public WorkSpace(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context=context;
		init();
		bindEvent();
	}
	
	protected void init()
	{
		inflate(context, R.layout.worksapce, this);
		bottomPanel=(TextView)findViewById(R.id.bottom_panel);
	}
	protected void bindEvent()
	{
		bottomPanel.setOnTouchListener(this);
	}


	@Override
	public boolean onTouch(View arg0, MotionEvent arg1)
	{
		// TODO Auto-generated method stub
		
		if(arg1.getAction()==MotionEvent.ACTION_DOWN)
		{
			if(arg0==bottomPanel)
			{
				MainActivity.slidingViewGroup.expand();
				return true;
			}
		}
		return true;
	}

}
