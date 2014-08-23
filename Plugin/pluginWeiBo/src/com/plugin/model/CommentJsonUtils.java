package com.plugin.model;

/*
 * 微博评论 json解析封装类
 */
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.plugin.model.Comments.CommentContent;

import android.util.Log;

public class CommentJsonUtils {
	private static String TAG = "WeiboContent";

	// 解析组装
	public static List<CommentContent> toCommentContents(String jsonContent) {
		List<CommentContent> res = new ArrayList<CommentContent>();
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonContent);
			// 第一层
			JSONArray status = jsonObject.getJSONArray("statuses");
			// 第二层 每条微博数据
			for (int i = 0; i < status.length(); i++) {
				// 第三层 详细数据
				JSONObject data = status.getJSONObject(i);
				CommentContent ws = new CommentContent();
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

	public static String toJsonString(CommentContent c) {
		// TODO Auto-generated method stub
		return null;
	}

}
