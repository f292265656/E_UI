package com.plugin.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.musicplayerplugin.master.R;

import com.plugin.model.GlobalData;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MPlayerFragment extends Fragment implements OnClickListener {

	private final static String TAG = "MainActivity";
	private TextView nameTextView;
	private SeekBar seekBar;
	private ListView listView;
	private List<Map<String, String>> data;
	private int current;
	private MediaPlayer player;
	private Handler handler;

	private Button ppButton,preButton,nextButton;

	private boolean isPause;
	private boolean isStartTrackingTouch;

	private View rootView;
	private Context mainContext = GlobalData.getMainContext();
	private Context pluginContext = GlobalData.getPluginContext();
	SimpleAdapter adapter;
	private final int refreshComplete = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rootView = GlobalData.getLif().inflate(R.layout.plugin_main, null);

		initial();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return rootView;
	}

	// 初始化
	public void initial() {
		nameTextView = (TextView) rootView.findViewById(R.id.name);
		seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
		listView = (ListView) rootView.findViewById(R.id.list);

		preButton = (Button) rootView.findViewById(R.id.previous);
		ppButton = (Button) rootView.findViewById(R.id.pp);
		nextButton = (Button) rootView.findViewById(R.id.next);

		preButton.setOnClickListener(this);
		ppButton.setOnClickListener(this);
		nextButton.setOnClickListener(this);

		// 创建一个音乐播放器
		player = new MediaPlayer();

		// 显示音乐播放列表
		// generateListView();
		waitFresh();
		new Thread(new SerachList()).start();
		// 进度条监听器
		seekBar.setOnSeekBarChangeListener(new MySeekBarListener());

		// 播放器监听器
		player.setOnCompletionListener(new MyPlayerListener());

		// 意图过滤器
		IntentFilter filter = new IntentFilter();

		// 播出电话暂停音乐播放
		filter.addAction("Android.intent.action.NEW_OUTGOING_CALL");
		mainContext.registerReceiver(new PhoneListener(), filter);

		// 创建一个电话服务
		TelephonyManager manager = (TelephonyManager) mainContext
				.getSystemService(mainContext.TELEPHONY_SERVICE);

		// 监听电话状态，接电话时停止播放
		manager.listen(new MyPhoneStateListener(),
				PhoneStateListener.LISTEN_CALL_STATE);
	}

	private void waitFresh() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == refreshComplete) {

					if (adapter == null) {
						adapter = new SimpleAdapter(pluginContext, data,
								R.layout.item, new String[] { "name" },
								new int[] { R.id.mName });
						listView.setAdapter(adapter);
					} else {
						adapter.notifyDataSetChanged();
					}

				}
			}
		};
	}

	private final class MyPhoneStateListener extends PhoneStateListener {
		public void onCallStateChanged(int state, String incomingNumber) {
			pause();
		}
	}

	private final class MyPlayerListener implements OnCompletionListener {
		// 歌曲播放完后自动播放下一首歌曲
		public void onCompletion(MediaPlayer mp) {
			next();
		}
	}

	private void previous() {
		current = current - 1 < 0 ? data.size() - 1 : current - 1;
		play();
	}

	private void next() {
		current = (current + 1) % data.size();
		play();
	}

	private final class MySeekBarListener implements OnSeekBarChangeListener {
		// 移动触发
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
		}

		// 起始触发
		public void onStartTrackingTouch(SeekBar seekBar) {
			isStartTrackingTouch = true;
		}

		// 结束触发
		public void onStopTrackingTouch(SeekBar seekBar) {
			player.seekTo(seekBar.getProgress());
			isStartTrackingTouch = false;
		}
	}

	private void generateListView() {
		List<File> list = new ArrayList<File>();

		// 获取sdcard中的所有歌曲
		findAll(Environment.getExternalStorageDirectory(), list);
		for (File f : list) {
			Log.i(TAG, f.getName());
		}

		// 播放列表进行排序，字符顺序
		Collections.sort(list);

		data = new ArrayList<Map<String, String>>();
		for (File file : list) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", file.getName());
			map.put("path", file.getAbsolutePath());
			data.add(map);
		}

		listView.setOnItemClickListener(new MyItemListener());
	}

	private final class MyItemListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			current = position;
			play();
		}
	}

	private void play() {
		try {
			// 重播
			player.reset();

			// 获取歌曲路径
			player.setDataSource(data.get(current).get("path"));
			Log.i(TAG, data.get(current).get("path"));

			// 缓冲
			player.prepare();

			// 开始播放
			player.start();

			// 显示歌名
			nameTextView.setText(data.get(current).get("name"));

			// 设置进度条长度
			seekBar.setMax(player.getDuration());

			// 播放按钮样式
			ppButton.setText("||");

			// 发送一个Runnable, handler收到之后就会执行run()方法
			handler.post(new Runnable() {
				public void run() {
					// 更新进度条状态
					if (!isStartTrackingTouch)
						seekBar.setProgress(player.getCurrentPosition());
					// 1秒之后再次发送
					handler.postDelayed(this, 1000);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void findAll(File file, List<File> list) {
		File[] subFiles = file.listFiles();
		if (subFiles != null)
			for (File subFile : subFiles) {
				// Log.i(TAG, subFile.getName());
				if (subFile.isFile()&& (subFile.getName().endsWith(".mp3") || subFile.getName().endsWith(".acc"))) 
				{
					if (subFile.length() / 1024 > 100) {
						list.add(subFile);
					}

				} else if (subFile.isDirectory())// 如果是目录
					findAll(subFile, list); // 递归
			}
	}

	public void pp() {

		// 默认从第一首歌曲开始播放
		if (!player.isPlaying() && !isPause) {
			play();
			return;
		}

		// Button button = (Button) view;
		// 暂停/播放按钮
		if ("||".equals(ppButton.getText())) {
			pause();
			ppButton.setText("▶");
		} else {
			resume();
			ppButton.setText("||");
		}
	}

	private void resume() {
		if (isPause) {
			player.start();
			isPause = false;
		}
	}

	private void pause() {
		if (player != null && player.isPlaying()) {
			player.pause();
			isPause = true;
		}
	}

	private final class PhoneListener extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			pause();
		}
	}

	public void onResume() {
		super.onResume();
		resume();
	}

	private class SerachList implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			generateListView();
			Message msg = new Message();
			msg.what = refreshComplete;
			handler.sendMessage(msg);

		}

	}

	@Override
	public void onStop() {
		super.onStop();
		player.stop();

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0 == ppButton) {
			pp();
		} else if (arg0 == preButton) {
			previous();
		} else if (arg0 == nextButton) {
			next();
		}

	}

}
