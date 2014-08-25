package com.plugin.view;

import com.plugin.model.GlobalSetting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;


public class UserExitDialog {
	private final static String TAG="UserExitDialog";
    private  AlertDialog quitDialog;
    private AlertDialog.Builder builder;
    private Context mainContext;
    private ExitClickListener exitClickListener;
    private CancelClickListener cancelClickListener;
  //  private 
    public UserExitDialog(Context context) {
    	this.mainContext=context;
    	this.builder=new AlertDialog.Builder(context);
    	exitClickListener=new ExitClickListener();
    	cancelClickListener=new CancelClickListener();
	}
    public void show()
    {
    	if(quitDialog==null)
    	{
    		initDialog();
    	}
    	Log.i(TAG, "--->before");
    	quitDialog.show();
    	Log.i(TAG, "--->after");
    }
    public void dissmiss()
    {
    	quitDialog.dismiss();
    }
    private void initDialog()
    {
    	builder.setTitle("提示");
    	builder.setMessage("确认退出登陆码?");
    	builder.setPositiveButton("确认", exitClickListener);
    	builder.setNegativeButton("取消", cancelClickListener);
    	quitDialog=builder.create();
    }
    private class ExitClickListener implements OnClickListener
    {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			GlobalSetting.clear(mainContext);
			quitDialog.dismiss();
		}
    }
    private class CancelClickListener implements OnClickListener
    {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			
			quitDialog.dismiss();
		}
    }


}
