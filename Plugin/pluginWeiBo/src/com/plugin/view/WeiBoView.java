package com.plugin.view;

import java.util.ArrayList;
import org.json.JSONException;


import com.example.pluginweibo.R;
import com.example.pluginweibo.R.id;
import com.example.pluginweibo.R.layout;
import com.plugin.control.Auth;
import com.plugin.master.PluginDes;
import com.plugin.model.CommentJsonUtils;
import com.plugin.model.CommentJsonUtils.WeiboContentStruct;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class WeiBoView extends LinearLayout implements OnClickListener
{
	private static String TAG = "WeiBoView";
	private ListView contentList;     //微博内容列表
	private Button sendMsg, reFresh,usrExit;
	private View rootView;
	private Auth wb;
	private final int REFRESHDATA = 0x10;  //刷新微博数据
	private final int REFRESHUSR = 0x11;   //刷新用户数据
	private final int OVERTIME = 0x12;     //超时
	private final int SENDMESSAGE = 0x20;     //发送微博
	private Handler handler;
    private Context mainContext,pluginContext;
    private ArrayList<WeiboContentStruct> weiboDataList;     //微博内容数据
    private MlistAdapter mlistAdapter;
    private SendMessageDialog dialog;  //发送微博dialog
    private int operation=-1;
    
	public WeiBoView(Context context)
	{
		super(context);
		mainContext=PluginDes.getMainContext();
		pluginContext=PluginDes.getPluginContxt();
		

		// TODO Auto-generated constructor stub
		LayoutInflater lf = LayoutInflater.from(context);
		rootView = lf.inflate(R.layout.plugin_main, null);
		init();
		addView(rootView);

	}

	protected void init()
	{
		wb = new Auth();
		dialog=new SendMessageDialog(mainContext,android.R.style.Theme_Holo_DialogWhenLarge_NoActionBar);
		contentList=(ListView)rootView.findViewById(R.id.contentList);
		sendMsg = (Button) rootView.findViewById(R.id.sendMsg);
		reFresh = (Button) rootView.findViewById(R.id.refresh);
		usrExit=(Button)rootView.findViewById(R.id.UsrExit);
		weiboDataList=new ArrayList<CommentJsonUtils.WeiboContentStruct>();
		mlistAdapter=new MlistAdapter();
		contentList.setAdapter(mlistAdapter);
		bindEvent();
		//
		handler = new Handler(mainContext.getMainLooper())
		{

			@Override
			public void handleMessage(Message msg)
			{
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what)
				{
				case REFRESHDATA:
	
					 Log.i(TAG, weiboDataList.get(0).comment);
					 mlistAdapter.notifyDataSetChanged();
					 
					break;
				case REFRESHUSR:
					// isUsrInfGet=true;
					break;
				case OVERTIME:
					Toast.makeText(mainContext, "超时", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}
		};

	}
	//异步检查数据
	
	public void refreshData()
	{
		
	Thread t=new Thread(new Runnable()
		{
			int t=0;
			Message msg=new Message();
		
			@Override
			public void run()
			{
				
			
			
				// TODO Auto-generated method stub
				while(wb.getWeiBoData()==null && t<5)
				{
					try
					{
						t++;
						Thread.sleep(1000);
					} catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(t<5)
				{
				   msg.what=REFRESHDATA;
				   try
				{
					weiboDataList=CommentJsonUtils.parse(wb.getWeiBoData());
				} catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
				else
				{
					msg.what=OVERTIME;
				}
				handler.sendMessage(msg);
				
			}
		});
		t.start();
	}
	

	protected void bindEvent()
	{
		sendMsg.setOnClickListener(this);
		reFresh.setOnClickListener(this);
		usrExit.setOnClickListener(this);
		

	}

	@Override
	public void onClick(View arg0)
	{
		// TODO Auto-generated method stub
		if (arg0 == sendMsg)
		{
		   if(wb.getTokenState())
		   {
			   dialog.show();
		   }
		   else
		   {
			   wb.Login();
		   }
		  
		} 
		else if (arg0 == reFresh)

		{
			Toast.makeText(mainContext, "刷新中", Toast.LENGTH_SHORT).show();
		 
			wb.requestWeiBoData();
			  if(wb.getTokenState())
			  {
				  refreshData();
			  }
			
		}
		else if(arg0==usrExit)
		{
			
			Toast.makeText(mainContext, "注销成功", Toast.LENGTH_SHORT).show();
			wb.exit();
		}

	}
	//listview处理
	private class MlistAdapter extends BaseAdapter
	{

      
		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return weiboDataList.size();
		}

		@Override
		public Object getItem(int arg0)
		{
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int position)
		{
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			// TODO Auto-generated method stub
			//优化listview性能 
			Holder holder;
			if(convertView==null)
			{
				LayoutInflater mlf=LayoutInflater.from(pluginContext);
				convertView=mlf.inflate(R.layout.listview_item, null);
				//
			    holder=new Holder();
			    holder.name=(TextView) convertView.findViewById(R.id.Uname);
			    holder.content=(TextView) convertView.findViewById(R.id.Ucontent);
				convertView.setTag(holder);
			}
			else
			{
				holder=(Holder) convertView.getTag();
			
			}
			WeiboContentStruct tmp=weiboDataList.get(position);
			holder.name.setText(tmp.usrName);
			holder.content.setText(tmp.text);
			return convertView;
		}
		
	}
	private class Holder
	{
		TextView name=null;
		TextView content=null;
		
	}

}
