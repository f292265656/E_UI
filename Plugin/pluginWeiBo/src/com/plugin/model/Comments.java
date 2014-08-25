package com.plugin.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.util.Log;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.legacy.WeiboAPI;

/*
 * 评论数据管理类
 */
public class Comments {
	private final static String TAG="Comments";
	private StatusesAPI weiBoOpera;
	private Oauth2AccessToken accessToken;
	// 评论信息回调(已封装)
	private CommentLisener commentLisener;
	private CommentRequestListener crListener;
	//每页评论条数
	public static final int COMMENT_COUNT=10;
	//微博列表
	private  List<CommentContent> contentList;
	
	public Comments() {
		accessToken = GlobalSetting.readAccessToken(GlobalSetting.getMainContext());
		crListener = new CommentRequestListener();
		weiBoOpera = new StatusesAPI(accessToken);
	 //   contentList =new ArrayList<Comments.CommentContent>(); 
	
	}
	public List<CommentContent> getComments()
	{
		if(contentList==null)
		{
			contentList=new ArrayList<Comments.CommentContent>();
		}
		return contentList;
	}
	public List<CommentContent> addInHead(List<CommentContent> dataList)
	{
		if(isEmpty())
		{
			contentList=dataList;
		}
		else
		{
			long id=contentList.get(0).id;
			int delIndex=contentList.size()-1;
			while(delIndex<dataList.size()&&dataList.get(delIndex).id!=id)
			{
				delIndex--;
			}
		
			if(delIndex<dataList.size()&&dataList.get(delIndex).id==id)
			{
				for(int i=delIndex;i<dataList.size();i++)
				{
					//去除多余数据
					dataList.remove(delIndex);
				}
				contentList.addAll(dataList);
			
			}
			else
			{
				//数据全新
				contentList.addAll(dataList);
				
			}
			
		}
		return contentList;
	}
	public List<CommentContent> addInFoot(List<CommentContent> dataList)
	{
		if(isEmpty())
		{
			contentList=dataList;
		}
		else
		{
			int delIndex=0;
			long id=contentList.get(contentList.size()-1).id;
			while(delIndex<dataList.size() &&dataList.get(delIndex).id!=id)
			{
				delIndex++;
			}
			if(delIndex<dataList.size() && dataList.get(delIndex).id==id)
			{
				//有重复
				for(int i=delIndex;i>=0;i--)
				{
					dataList.remove(0);
				}
				contentList.addAll(dataList);
				
			}
			else
			{
				contentList.addAll(dataList);
			}
			
		}
		return contentList;
	}
	
	
	public boolean isEmpty()
	{
		if(contentList==null || contentList.size()<=0)
		{
			return true;
		}
		return false;
	}

	
    
	public void sendComment(String content) {
		Log.i(TAG, content);
		weiBoOpera.update(content,"0.0","0.0", null);  
	}

	public void getFriendsComments(int page, CommentLisener listener) {
		commentLisener = listener;
		WeiboAPI.FEATURE feature=WeiboAPI.FEATURE.ALL;
		weiBoOpera
				.friendsTimeline(0, 0, COMMENT_COUNT, page, false, feature, false, crListener);

	}

	/*
	 * 异步请求评论信息 结果回调
	 */
	protected class CommentRequestListener implements RequestListener {

		@Override
		public void onComplete(String response) {
			// TODO Auto-generated method stub
			Log.i(TAG, response);
			commentLisener.setComments(ResStatus.SUCCEED,
					CommentJsonUtils.toCommentContents(response));
		}

		@Override
		public void onComplete4binary(ByteArrayOutputStream responseOS) {
			// TODO Auto-generated method stub
			commentLisener.setComments(ResStatus.Complete4binary, null);
		}

		@Override
		public void onIOException(IOException e) {
			// TODO Auto-generated method stub
			commentLisener.setComments(ResStatus.IOException, null);
		}

		@Override
		public void onError(WeiboException e) {
			// TODO Auto-generated method stub
			commentLisener.setComments(ResStatus.ERROR, null);
		}

	}

	/*
	 * 请求结果状态码
	 */
	public static enum ResStatus {
		ERROR, IOException, SUCCEED, Complete4binary;
	}

	/*
	 * 评论信息类
	 */
	public static class CommentContent {
		public long id=-1L;
		public String comment = null;
		public String usrName = null;
		//public String usrDescription = null;
		public String created_time = null;
		public String IconUrl=null;
		public String fromDevice=null;
		//处理(特殊文字变色等)过的评论
		public SpannableStringBuilder commentBuiler=null;
		//是否被加工过
		public boolean isPrecceed=false;
	}

	/*
	 * 评论信息回调
	 */
	public interface CommentLisener {
		void setComments(ResStatus status, List<CommentContent> comments);
	}

}
