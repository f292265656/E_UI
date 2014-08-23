package com.plugin.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.legacy.WeiboAPI;

/*
 * 评论处理类
 */
public class Comments {
	private final static String TAG="Comments";
	private StatusesAPI weiBoOpera;
	private Oauth2AccessToken accessToken;
	// 评论信息回调(已封装)
	private CommentLisener commentLisener;
	private CommentRequestListener crListener;
	//每页评论条数
	public static final int COMMENT_COUNT=20;
	
	public Comments() {
		accessToken = GlobalData.readAccessToken(GlobalData.getMainContext());
		crListener = new CommentRequestListener();
		weiBoOpera = new StatusesAPI(accessToken);
	
	
	}
	
    
	public void sendComment(String content) {
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
		public String comment = null;
		public String usrName = null;
		public String usrDescription = null;
		public String created_time = null;
	}

	/*
	 * 评论信息回调
	 */
	public interface CommentLisener {
		void setComments(ResStatus status, List<CommentContent> comments);
	}

}
