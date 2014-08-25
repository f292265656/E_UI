package com.plugin.control;

/*
 * 微博授权类
 * 实现微博授权功能
 */
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.plugin.model.GlobalSetting;
import com.plugin.model.MConstants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;

public class Oauth {
	private final static String TAG = "Oauth";
	private final int OAUTH_SUCCEED = 1, OAUTH_FAILED = 2;;
	private Context mainContext = null;
	private WeiboAuth mWeiboAuth;
	private Oauth2AccessToken mAccessToken = null;
	private AuthDialogListener mAuthDialogListener;
	private AuthListener resListener;
	private Handler handler;

	public Oauth() {
		mainContext = GlobalSetting.getMainContext();
		mAccessToken = GlobalSetting.readAccessToken(mainContext);
		mWeiboAuth = new WeiboAuth(mainContext, MConstants.APP_KEY,
				MConstants.REDIRECT_URL, MConstants.SCOPE);
		mAuthDialogListener = new AuthDialogListener();
		initHandle();
	}

	// UI线程处理
	public void initHandle() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case OAUTH_SUCCEED:
					resListener.setResult(true);
					break;
				case OAUTH_FAILED:
					resListener.setResult(false);
					Toast.makeText(mainContext, "授权失败 请联系开发者",
							Toast.LENGTH_SHORT).show();
					break;
				default:

					break;
				}
			}
		};
	}

	// ----------OAuth2.0授权 接口
	protected class AuthDialogListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle arg0) {
			//Log.i(TAG, "code is" + arg0.getString("code", ""));
			Message msg=new Message();
			mAccessToken = Oauth2AccessToken.parseAccessToken(arg0);
			if (mAccessToken.isSessionValid()) {
				// 保存token
				GlobalSetting.keepAccessToken(mainContext, mAccessToken);
				msg.what=OAUTH_SUCCEED;

			} else {
                msg.what=OAUTH_FAILED;
			}
			handler.sendMessage(msg);

		}

		@Override
		public void onWeiboException(WeiboException e) {

			Log.i(TAG, "code is" + e.getMessage());
		}

		@Override
		public void onCancel() {
			Log.i(TAG, "onCancel");
		}

	}

	// 授权
	public void authRequest(AuthListener listener) {
		resListener = listener;
		mWeiboAuth.anthorize(mAuthDialogListener);
	}

	public interface AuthListener {
		void setResult(boolean res);
	}

}
