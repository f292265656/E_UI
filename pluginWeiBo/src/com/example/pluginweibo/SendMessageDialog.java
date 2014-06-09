package com.example.pluginweibo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendMessageDialog extends Dialog implements android.view.View.OnClickListener
{
	private Context context,pluginContext;
	private EditText edMsg;
	private Button ok,cancle;
	private WeiBoHelper wh;
	private View rootView;
	public SendMessageDialog(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
		this.context=context;
	
		
		
	}
	
	public SendMessageDialog(Context context, int theme)
	{
	   
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.context=context;
		wh=new WeiBoHelper();
		pluginContext=PluginDes.getPluginContxt();
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LayoutInflater lf=LayoutInflater.from(pluginContext);
		rootView=lf.inflate(R.layout.sendmessage_dialog, null);
		setContentView(rootView);
		init();
	}
	public void init()
	{
		edMsg=(EditText) rootView.findViewById(R.id.edit_msg);
		ok=(Button)rootView. findViewById(R.id.ok);
		cancle=(Button) rootView.findViewById(R.id.cancel);
		bindEvent();
	}
	
	public void bindEvent()
	{
		ok.setOnClickListener(this);
		cancle.setOnClickListener(this);
	}
	
	
	
	
	

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		if(v==ok)
		{
			String msg=edMsg.getText().toString();
			if(!msg.isEmpty())
			{
				wh.sendMessage(msg);
				hide();
			}
			else
			{
				Toast.makeText(context, "输入数据", Toast.LENGTH_SHORT).show();
				hide();
			}
		}
		else if(v==cancle)			
		{
			hide();
		}
		
	}
	

}
