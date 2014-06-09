package com.example.pluginweibo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class WeiboContent
{
    private static String TAG="WeiboContent";
    public static ArrayList<WeiboContentStruct> parse(String jsonContent) throws JSONException
    {
    	ArrayList<WeiboContentStruct> res=new ArrayList<WeiboContent.WeiboContentStruct>();
    	JSONObject jsonObject=new JSONObject(jsonContent);
    	//第一层
    	JSONArray status=jsonObject.getJSONArray("statuses");
    	//第二层 每条微博数据
    	for(int i=0;i<status.length();i++)
    	{
    		//第三层 详细数据
    		JSONObject data=status.getJSONObject(i);
    		WeiboContentStruct ws=new WeiboContentStruct();
    		ws.text=data.getString("text");
    		ws.created_time=data.getString("created_at");
    		//第四层 用户户信息解析
    		JSONObject usr=data.getJSONObject("user");
    		ws.usrName=usr.getString("name");
    		Log.i(TAG, ws.usrName);
    		ws.usrDescription=usr.getString("description");
    		res.add(ws);
    	}
    	
	
    	return res;
    }
    public static class WeiboContentStruct 
    {
    	public String text=null;
    	public String usrName=null;
    	public String usrDescription=null;
    	public String created_time=null;
    }
}
