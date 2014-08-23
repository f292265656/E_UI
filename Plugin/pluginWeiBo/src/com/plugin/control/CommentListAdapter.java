package com.plugin.control;

import java.util.List;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.plugin.master.R;
import com.plugin.model.Comments.CommentContent;
import com.plugin.model.GlobalData;


/*
 * listview数据绑定
 */
public class CommentListAdapter extends BaseAdapter {
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
			convertView = GlobalData.getLayoutInflater().inflate(R.layout.listview_item, null);
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
		holder.userName.setText(tmp.usrName);
		holder.userComment.setText(tmp.comment);
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