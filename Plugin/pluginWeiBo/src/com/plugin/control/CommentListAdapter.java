package com.plugin.control;

import java.util.Calendar;
import java.util.List;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.plugin.master.R;
import com.plugin.model.Comments.CommentContent;
import com.plugin.model.GlobalSetting;
import com.plugin.utils.CommentProcess;


/*
 * listview数据绑定
 */
public class CommentListAdapter extends BaseAdapter {
	private final static String TAG="CommentListAdapter";
	private List<CommentContent> commentListData;

	public void setDataList(List<CommentContent> commentList)
	{
		this.commentListData=commentList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (commentListData == null) {
			return 0;
		}
		return commentListData.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// 优化listview性能
		Holder holder;
		if (convertView == null) {
			convertView = GlobalSetting.getLayoutInflater().inflate(R.layout.listview_item, null);
			//
			holder = new Holder();
			holder.userIcon=(ImageView)convertView.findViewById(R.id.Image_userIcon);
			holder.userName = (TextView) convertView.findViewById(R.id.Uname);
			holder.fromDevice=(TextView)convertView.findViewById(R.id.text_fremDevice);
			holder.time=(TextView)convertView.findViewById(R.id.text_time);
			holder.userComment = (TextView) convertView
					.findViewById(R.id.Ucontent);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();

		}
		CommentContent tmp = commentListData.get(position);
		ImageLoadHelper.load(tmp.IconUrl, holder.userIcon);
		holder.userName.setText(" "+tmp.usrName);
		holder.fromDevice.setText(" "+tmp.fromDevice);
		
		holder.time.setText(tmp.created_time);
		holder.userComment.setText(tmp.commentBuiler);
		
		return convertView;
	}

	private class Holder {
		TextView userName = null;
		TextView userComment = null;
		TextView fromDevice=null;
		TextView time=null;
		ImageView userIcon=null;

	}

}