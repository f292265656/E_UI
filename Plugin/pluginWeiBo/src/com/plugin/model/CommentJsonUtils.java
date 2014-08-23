package com.plugin.model;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class CommentJsonUtils {
	private static String TAG = "WeiboContent";

	public  ArrayList<WeiboContent> toWeiBoContents(String jsonContent) {
		ArrayList<WeiboContent> res = new ArrayList<CommentJsonUtils.WeiboContent>();
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonContent);
			// 第一层
			JSONArray status = jsonObject.getJSONArray("statuses");
			// 第二层 每条微博数据
			for (int i = 0; i < status.length(); i++) {
				// 第三层 详细数据
				JSONObject data = status.getJSONObject(i);
				WeiboContent ws = new WeiboContent();
				ws.comment = data.getString("text");
				ws.created_time = data.getString("created_at");
				// 第四层 用户户信息解析
				JSONObject usr = data.getJSONObject("user");
				ws.usrName = usr.getString("name");
				Log.i(TAG, ws.usrName);
				ws.usrDescription = usr.getString("description");
				res.add(ws);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;
	}

	public static class WeiboContent {
		public String comment = null;
		public String usrName = null;
		public String usrDescription = null;
		public String created_time = null;
	}
}
