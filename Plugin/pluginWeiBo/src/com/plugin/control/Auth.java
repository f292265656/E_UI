package com.plugin.control;
/*
 * 微博授权类
 * 实现微博授权功能
 */
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.plugin.master.PluginDes;
import com.plugin.model.GlobalData;
import com.plugin.model.MConstants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;



public class Auth
{
	private static String TAG = "WeiBoData";
	private Context mainContext = null;
	private WeiboAuth mWeiboAuth;
	private Oauth2AccessToken mAccessToken = null;
	private AuthDialogListener mAuthDialogListener;
	private AuthListener resListener;


    
	public Auth()
	{
		mainContext = GlobalData.getMainContext();
		mAccessToken = GlobalData.readAccessToken(mainContext);
		mWeiboAuth = new WeiboAuth(mainContext, MConstants.APP_KEY,
				MConstants.REDIRECT_URL, MConstants.SCOPE);
		mAuthDialogListener = new AuthDialogListener();
		
	}


	// ----------OAuth2.0授权 接口
	protected class AuthDialogListener implements WeiboAuthListener
	{

		@Override
		public void onComplete(Bundle arg0)
		{
			// TODO Auto-generated method stub
			mAccessToken = Oauth2AccessToken.parseAccessToken(arg0);
			if (mAccessToken.isSessionValid())
			{
				//保存token
				resListener.setResult(true);
				GlobalData.keepAccessToken(mainContext, mAccessToken);

			} else
			{
				resListener.setResult(false);
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

		@Override
		public void onCancel()
		{
			// TODO Auto-generated method stub
		}


	}
	//授权
	public void authRequest(AuthListener listener)
	{
		resListener=listener;
		mWeiboAuth.anthorize(mAuthDialogListener);
	}

    public  interface AuthListener{
    	void setResult(boolean res);
    }

	
}
