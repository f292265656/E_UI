package com.plugin.view;

/*
 *主显示类
 *显示用户以及用户关注用户的评论 
 */
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.plugin.control.CommentListAdapter;
import com.plugin.control.Oauth;
import com.plugin.control.Oauth.AuthListener;
import com.plugin.control.User;
import com.plugin.master.PluginDes;
import com.plugin.master.R;
import com.plugin.model.CommentJsonUtils;
import com.plugin.model.Comments;
import com.plugin.model.GlobalData;
import com.plugin.model.Comments.CommentContent;
import com.plugin.model.Comments.CommentLisener;
import com.plugin.model.Comments.ResStatus;

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

public class WeiBoView extends LinearLayout implements OnClickListener {
	private static String TAG = "WeiBoView";
	private final int REFRESH_UI = 1;
	private Context mainContext, pluginContext;
	// 控件
	private ListView commentListView;
	private Button sendMsg, reFresh, usrExit;
	private View rootView;
	private SendMessageDialog dialog;
	// 数据
	private List<CommentContent> commentListData;
	private CommentListAdapter listAdapter;
	private Comments commentHelper;
	private UpCommentListener upCommentListener;
	private User user;
	private UserLogListener userLogListener;
	// 请求微博数据页数
	private int pageSize = 1;
	private Handler handler;

	public WeiBoView(Context context) {
		super(context);
		mainContext = GlobalData.getMainContext();
		pluginContext = GlobalData.getPluginContext();
		rootView = GlobalData.getLayoutInflater().inflate(R.layout.plugin_main,
				null);
		addView(rootView);
		init();

	}

	protected void init() {
		// 初始化
		dialog = new SendMessageDialog(mainContext,
				android.R.style.Theme_Holo_DialogWhenLarge_NoActionBar);
		commentListView = (ListView) rootView.findViewById(R.id.commentList);
		sendMsg = (Button) rootView.findViewById(R.id.sendMsg);
		reFresh = (Button) rootView.findViewById(R.id.refresh);
		usrExit = (Button) rootView.findViewById(R.id.UsrExit);

		// 数据/事件
		userLogListener = new UserLogListener();
		user = new User();
		listAdapter = new CommentListAdapter();
		upCommentListener = new UpCommentListener();
		commentHelper = new Comments();
		commentListView.setAdapter(listAdapter);
		bindEvent();
		waitRefreshUI();
	}

	protected void bindEvent() {
		sendMsg.setOnClickListener(this);
		reFresh.setOnClickListener(this);
		usrExit.setOnClickListener(this);
	}

	// 接收子线程消息 更新界面
	protected void waitRefreshUI() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == REFRESH_UI) {
					listAdapter.notifyDataSetChanged();
				}
			}
		};
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view == sendMsg) {
			if (user.isUserLogin()) {
				dialog.show();
			} else {
				Toast.makeText(mainContext, "请先登录", Toast.LENGTH_SHORT).show();
				user.loginAsyn(userLogListener);
			}
		} else if (view == reFresh) {
			if (user.isUserLogin()) {
				commentHelper.getFriendsComments(pageSize, upCommentListener);
			} else {
				Toast.makeText(mainContext, "请先登录", Toast.LENGTH_SHORT).show();
				user.loginAsyn(userLogListener);
			}
		} else if (view == usrExit) {
			if (user.isUserLogin()) {
				user.logout();
				Toast.makeText(mainContext, "退出成功", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(mainContext, "无用户", Toast.LENGTH_SHORT).show();
			}
		}

	}

	/*
	 * 登陆结果接口
	 */
	private class UserLogListener implements AuthListener {

		@Override
		public void setResult(boolean res) {
			// TODO Auto-generated method stub
			if (res) {
				Toast.makeText(mainContext, "登陆成功", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(mainContext, "登陆失败", Toast.LENGTH_SHORT).show();
			}
		}

	}

	/*
	 * 评论数据接口 子线程中
	 */
	private class UpCommentListener implements CommentLisener {

		@Override
		public void setComments(ResStatus status, List<CommentContent> comments) {
			// TODO Auto-generated method stub
			if (status != ResStatus.SUCCEED) {
				Toast.makeText(mainContext, "获取信息失败", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			addCommentData(comments);
			listAdapter.setDataList(commentListData);
			Message msg=new Message();
			msg.what=REFRESH_UI;
			handler.sendMessage(msg);
		}
	}

	/*
	 * 更新数据源
	 */

	private void addCommentData(List<CommentContent> newComments) {
		if (commentListData == null) {
			commentListData = new ArrayList<Comments.CommentContent>();
		}
		int pageBase = commentListData.size() / Comments.COMMENT_COUNT;
		int remainder = commentListData.size() % Comments.COMMENT_COUNT;
		// 前面数据填满一页倍数
		if (remainder == 0) {
			commentListData.addAll(newComments);
		}
		// 未填满
		else {
			int startIndex = pageBase * Comments.COMMENT_COUNT;
			while (startIndex <= commentListData.size()) {
				commentListData.remove(startIndex);
			}
			commentListData.addAll(newComments);
		}
		// 判断下次请求页数
		if (commentListData.size() / Comments.COMMENT_COUNT == 0) {
			pageSize++;
		}

	}



}
