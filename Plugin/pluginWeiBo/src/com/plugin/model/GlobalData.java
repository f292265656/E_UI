package com.plugin.model;

/*
 * 存储全局数据
 * 微博token	Oauth2AccessToken
 * 插件和 主程序 的Context
 * 
 */

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * 该类用于保存Oauth2AccessToken到sharepreference,并提供读取功能
 * 
 * @author wwj
 *
 */
public class GlobalData {
	
	private static final String PREFERENCES_NAME = "com_weibo_sdk_android";
	private static Context mainContext,pluginContext;
	/**
	 * 保存accsssToken到SharedPreferences
	 * @param context	上下文对象
	 * @param token	Oauth2AccessToken
	 */
	public static void keepAccessToken(Context context, Oauth2AccessToken token) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString("token", token.getToken());
		editor.putLong("expiresTime", token.getExpiresTime());
		editor.commit();
	}
	/**
	 * 清空sharedPreferences
	 * @param context
	 */
	public static void clear(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.clear();
		editor.commit();
	}
	/**
	 * 从SharedPreferences读取accessToken
	 * @param context
	 * @return Oauth2AccessToken
	 */
	public static Oauth2AccessToken readAccessToken(Context context) {
		Oauth2AccessToken token = new Oauth2AccessToken();
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		token.setToken(pref.getString("token", ""));
		token.setExpiresTime(pref.getLong("expiresTime", 0));
		return token;
	}
	/*
	 * 主程序Context 
	 */
	public static void setMainContext(Context context)
	{
		mainContext=context;
	}
	public static Context getMainContext()
	{
		return mainContext;
	}
	/*
	 * 插件Context
	 */
	public static void setPluginContext(Context context)
	{
		pluginContext=context;
	}
	public static Context getPluginContext()
	{
		return pluginContext;
	}
}
