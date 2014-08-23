package com.plugin.control;

/*
 * 用户 操作类
 */
import android.util.Log;

import com.plugin.control.Oauth.AuthListener;
import com.plugin.model.GlobalData;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;


public class User {
	private Oauth auth;
	
	public User() {
		auth=new Oauth();
	}
	public void loginAsyn(AuthListener listener)
	{
		auth.authRequest(listener);
	}
	public void logout()
	{
		GlobalData.clear(GlobalData.getMainContext());
	}
	public boolean isUserLogin()
	{
		Oauth2AccessToken oAccessToken=GlobalData.readAccessToken(GlobalData.getMainContext());
		if( oAccessToken.isSessionValid())
		{
			Log.i("User", oAccessToken.toString());
			return true;
		}
		return false;
	}

}
