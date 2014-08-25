package com.plugin.view;

/*
 *主显示类
 *显示用户以及用户关注用户的评论 
 */

import java.util.Calendar;
import java.util.List;

import android.content.Context;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.plugin.control.CommentListAdapter;
import com.plugin.control.Oauth.AuthListener;
import com.plugin.control.User;
import com.plugin.master.R;
import com.plugin.model.Comments;
import com.plugin.model.Comments.CommentContent;
import com.plugin.model.Comments.CommentLisener;
import com.plugin.model.Comments.ResStatus;
import com.plugin.model.GlobalSetting;
import com.plugin.utils.CommentProcess;

public class WeiBoView extends LinearLayout implements OnClickListener {
	private static String TAG = "WeiBoView";
	private Context mainContext, pluginContext;
	// 控件
	private PullToRefreshListView commentListView;
	private ImageView sendMsg, usrExit;
	private View rootView;
	private SendMessageDialog dialog;
	private UserExitDialog userExitDialog;
	// 数据
	private CommentListAdapter listAdapter;
	private Comments commentHelper;
	private User user;
	//事件监听
	private UpCommentListener upCommentListener;
	private UserLogListener userLogListener;
	private ListRefreashListener refreashListener;
	// 请求微博数据页数
	private Handler handler;
	private CommentProcess commentProcess;
	//控制 状态
	AddDataStatus addDataStatus=AddDataStatus.InFoot;

	public WeiBoView(Context context) {
		super(context);
		mainContext = GlobalSetting.getMainContext();
		pluginContext = GlobalSetting.getPluginContext();
		rootView = GlobalSetting.getLayoutInflater().inflate(
				R.layout.plugin_main, null);
		addView(rootView);
		init();

	}

	protected void init() {
		// 初始化
		commentListView = (PullToRefreshListView) rootView
				.findViewById(R.id.commentList);
		sendMsg = (ImageView) rootView.findViewById(R.id.sendMsg);

		usrExit = (ImageView) rootView.findViewById(R.id.UsrExit);
		commentListView.setMode(Mode.BOTH);
  
		// 数据/事件
		refreashListener=new ListRefreashListener();
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

		usrExit.setOnClickListener(this);
		commentListView.setOnRefreshListener(refreashListener);
	}

	// 接收子线程消息 更新界面
	protected void waitRefreshUI() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if ((ResStatus) msg.obj == ResStatus.SUCCEED) {
					listAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(mainContext, "获取信息失败", Toast.LENGTH_SHORT)
							.show();
				}
				commentListView.onRefreshComplete();
			}
		};
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view == sendMsg) {
			if (user.isUserLogin()) {
				if(dialog==null)
				{
					dialog = new SendMessageDialog(mainContext,
							android.R.style.Theme_Holo_Light_Dialog);
				}
				dialog.show();
			} else {
				Toast.makeText(mainContext, "请先登录", Toast.LENGTH_SHORT).show();
			}
		}  else if (view == usrExit) {
			if(userExitDialog==null)
			{
			
				//延迟加载
				userExitDialog=new UserExitDialog(mainContext);
			}
			if(user.isUserLogin())
			{
				Log.i(TAG, "已登录");
				userExitDialog.show();
			}
			else
			{
				Log.i(TAG, "未登录");
				Toast.makeText(mainContext, "登录中", Toast.LENGTH_SHORT).show();
				user.loginAsyn(userLogListener);
			}
		}

	}
    
	//下拉刷新
	public void pullDownRefresh() {
		addDataStatus=AddDataStatus.InHead;
		if (user.isUserLogin()) {
			commentHelper.getFriendsComments(1, upCommentListener);
		} else {
			Toast.makeText(mainContext, "请先登录", Toast.LENGTH_SHORT).show();
			user.loginAsyn(userLogListener);
		}
	}
    //上拉刷新
	public void pullUpRefresh() {
		addDataStatus=AddDataStatus.InFoot;
		if (user.isUserLogin()) {
			commentHelper.getFriendsComments(commentHelper.getComments().size()
					/ Comments.COMMENT_COUNT + 1, upCommentListener);
		} else {
			Toast.makeText(mainContext, "请先登录", Toast.LENGTH_SHORT).show();
			user.loginAsyn(userLogListener);
		}
	}

	/*
	 * 登陆结果接口
	 */
	private class UserLogListener implements AuthListener {

		@Override
		public void setResult(boolean res) {
			Log.i(TAG, "LogIn res is "+res);
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
			Message msg = new Message();
			msg.obj = status;
			if (status != ResStatus.SUCCEED) {

				handler.sendMessage(msg);
				return;
			}
			// 延迟加载
			if (commentProcess == null) {
				commentProcess = new CommentProcess();
			}
			if(addDataStatus==AddDataStatus.InFoot)
			{
				commentHelper.addInFoot(comments);
			}
			else if(addDataStatus==AddDataStatus.InHead)
			{
				commentHelper.addInHead(comments);
			}
			
			// 数据格式处理
			for (CommentContent c : commentHelper.getComments()) {
				commentProcess.process(Calendar.getInstance(), c);
			}
			listAdapter.setDataList(commentHelper.getComments());
			handler.sendMessage(msg);
		}
	}
	private enum AddDataStatus
	{
		InHead,InFoot;
	}
	private class ListRefreashListener implements OnRefreshListener2<ListView>
	{

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			// TODO Auto-generated method stub
			pullDownRefresh();
			
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			// TODO Auto-generated method stub
			pullUpRefresh();
		}
		
	}

}
