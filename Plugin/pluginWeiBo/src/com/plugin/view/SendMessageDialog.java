package com.plugin.view;

/*
 * 发送评论Dialog
 */
import com.plugin.master.R;
import com.plugin.model.Comments;
import com.plugin.model.GlobalSetting;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SendMessageDialog extends Dialog implements
		android.view.View.OnClickListener {
	private final static String TAG="SendMessageDialog";
	
	private Context context, pluginContext;
	private EditText edMsg;
	private ImageView ok, cancle;
	private View rootView;
	private Comments comments;

	public SendMessageDialog(Context context) {
		super(context);
		this.context = context;
		init();
	}
	public SendMessageDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		init();
	}

	public void init() {
		rootView = GlobalSetting.getLayoutInflater().inflate(
				R.layout.sendmessage_dialog, null);
		setContentView(rootView);
		setTitle(GlobalSetting.getPluginContext().getResources().getString(R.string.sendMsg));
		//初始化
		pluginContext = GlobalSetting.getPluginContext();
		comments = new Comments();
		edMsg = (EditText) rootView.findViewById(R.id.edit_msg);
		ok = (ImageView) rootView.findViewById(R.id.ok);
		cancle = (ImageView) rootView.findViewById(R.id.cancel);
		bindEvent();
	}

	public void bindEvent() {
		ok.setOnClickListener(this);
		cancle.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == ok) {
			String content = edMsg.getText().toString();
			Log.i(TAG, "OK"+content);
			if (content != null && !content.isEmpty()) {
				comments.sendComment(content);
				hide();
			} else {
				Toast.makeText(context, "输入数据", Toast.LENGTH_SHORT).show();
				hide();
			}
		} else if (v == cancle) {
			Log.i(TAG, "cancle");
			hide();
		}

	}

}
