package com.example.pluginweibo;

import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;

public class WeiBoHelper
{
	private static String TAG = "WeiBoData";
	private Context mainContext = null;
	private WeiboAuth mWeiboAuth;
	private Oauth2AccessToken mAccessToken = null;
	private UsersAPI mUsersAPI = null;
	private MRequestListener mListener = null;
	private User mUser = null;
	private Long uid = null;
	private AuthDialogListener mAuthDialogListener;
	private StatusesAPI sApi;
	private String weiboData=null;

    
	public WeiBoHelper()
	{
		mainContext = PluginDes.getMainContext();
		init();
	}

	protected void init()

	{

		mAccessToken = AccessTokenKeeper.readAccessToken(mainContext);
		if(mAccessToken.isSessionValid())
		{
			sApi=new StatusesAPI(mAccessToken);
		}
		mWeiboAuth = new WeiboAuth(mainContext, MConstants.APP_KEY,
				MConstants.REDIRECT_URL, MConstants.SCOPE);
		mListener = new MRequestListener();
		mAuthDialogListener = new AuthDialogListener();
	

	}

	public void Login()
	{

		if (!mAccessToken.isSessionValid())
		{
			mWeiboAuth.anthorize(mAuthDialogListener);
		} else
		{
			Toast.makeText(mainContext, "账号已存在", Toast.LENGTH_SHORT).show();

		}
	}

	// ----------OAuth2.0授权 接口
	protected class AuthDialogListener implements WeiboAuthListener
	{

		@Override
		public void onCancel()
		{
			// TODO Auto-generated method stub
		}

		@Override
		public void onComplete(Bundle arg0)
		{
			// TODO Auto-generated method stub
			mAccessToken = Oauth2AccessToken.parseAccessToken(arg0);
			if (mAccessToken.isSessionValid())
			{
				sApi=new StatusesAPI(mAccessToken);
				AccessTokenKeeper.keepAccessToken(mainContext, mAccessToken);

			} else
			{
				Toast.makeText(mainContext, "授权失败 请联系开发者", Toast.LENGTH_SHORT)
						.show();
				Log.i(TAG, "code is" + arg0.getString("code", ""));
			}

		}

		@Override
		public void onWeiboException(WeiboException arg0)
		{
			// TODO Auto-generated method stub

		}

	}

	// 信息接口
	private class MRequestListener implements RequestListener
	{

		@Override
		public void onComplete(String arg0)
		{
			// TODO Auto-generated method stub
			if (!TextUtils.isEmpty(arg0))
			{
				weiboData=arg0;
				
			}
		}

		@Override
		public void onWeiboException(WeiboException arg0)
		{
			// TODO Auto-generated method stub

		}
	}
	//获取微博信息
	public void requestWeiBoData()
	{
		if(mAccessToken.isSessionValid())
		{	
			sApi.friendsTimeline(0, 0, 50, 1, false, 0, false,mListener);
		}
		else
		{
			Login();
			//Toast.makeText(mainContext, "请登录", Toast.LENGTH_SHORT).show();
			
		}
	}
	public String getWeiBoData()
	{
	    return weiboData;
	}
	//退出
	public void exit()
	{
		AccessTokenKeeper.clear(mainContext);
		mAccessToken=null;
	}
	//发送信息
	public void sendMessage(String msg)
	{
		if(mAccessToken.isSessionValid())
		{
			sApi.update(msg, null, null, mListener);
		}
		else
		{
			Login();
			Toast.makeText(mainContext, "请登录", Toast.LENGTH_SHORT).show();
		}
	}
	public boolean getTokenState()
	{
		return mAccessToken.isSessionValid();
	}
	
}
