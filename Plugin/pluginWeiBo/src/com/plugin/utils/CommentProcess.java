package com.plugin.utils;

/*
 * 评论信息处理类
 * 对评论相关数据加工修饰
 * （变色 截取数据等等）
 */
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.plugin.model.Comments.CommentContent;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

public class CommentProcess {
	private final static String TAG = "commentProcess";
	private SimpleDateFormat sf;
	private Calendar createTime;
	private Date buffDate;
	private Pattern isUri, isCallSb, fromDevice;

	private final static String REG_CALL_SB = "@[\u4e00-\u9fa5\\w]+";
	private final static String REG_ISURL = "http://\\S+";
	private final static String REG_FROM_DEVICE = ">.*<";

	public CommentProcess() {
		sf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
		createTime = Calendar.getInstance();
		isUri = Pattern.compile(REG_ISURL);
		isCallSb = Pattern.compile(REG_CALL_SB);
		fromDevice = Pattern.compile(REG_FROM_DEVICE);

	}

	public void process(Calendar current, CommentContent content) {
		if (content.isPrecceed) {
			return;
		}
		content.created_time = processTime(current, content.created_time);
		content.commentBuiler = processComment(content.comment);
		content.fromDevice = processFromeDevice(content.fromDevice);
		content.isPrecceed = true;
	}

	/*
	 * 截取发送设备信息数据
	 */
	private String processFromeDevice(String from) {
		Matcher fromMatcher = fromDevice.matcher(from);

		if (fromMatcher.find()) {
			// 去除 > <
			StringBuilder stringBuilder = new StringBuilder(fromMatcher.group());
			stringBuilder.deleteCharAt(0);
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);

			return stringBuilder.toString();
		}
		return "未知设备";
	}

	/*
	 * 对评论处理 连接变色 @某人 变色等
	 */
	private SpannableStringBuilder processComment(String comment) {
		comment.trim();
		SpannableStringBuilder style = new SpannableStringBuilder(comment);
		Matcher callMatcher = isCallSb.matcher(comment);
		Matcher urlMacher = isUri.matcher(comment);
		// @处理
		changeColor(style, callMatcher);
		// url处理
		changeColor(style, urlMacher);
		// style.insert(, tb, start, end)
		return style;
	}

	private void changeColor(SpannableStringBuilder style, Matcher m) {
		while (m.find()) {
			style.setSpan(new ForegroundColorSpan(Color.BLUE), m.start(),
					m.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

		}
	}

	/*
	 * 对时间差结果提示进行处理
	 */
	public String processTime(Calendar current, String time) {
		String timeRes = "未知";
		createTime.setTime(new Date(time));
		DiffTime diffTime = getDiffTime(createTime, current);
		// 根据时间差 给出适合的时间差提示
		if (diffTime.year > 0) {
			buffDate = createTime.getTime();
			timeRes = sf.format(buffDate);
		} else {
			if (diffTime.day_of_year <= 2) {
				switch (diffTime.day_of_year) {
				case 0:
					if (diffTime.hour > 0) {
						timeRes = getTime(createTime, false,false);
					} else {
						timeRes = diffTime.min + "分钟前";
					}
					break;
				case 1:
					timeRes = "昨天 " + getTime(createTime, false,false);
					break;
				case 2:
					timeRes = "前天 " + getTime(createTime, false,false);
					break;

				default:
					break;
				}
			} else {
				// xx月xx日 xx时xx分
				timeRes = getTime(createTime, true,true);
			}
		}

		return timeRes;
	}

	public DiffTime getDiffTime(Calendar before, Calendar after) {
		DiffTime dTime = new DiffTime();
		dTime.year = after.get(Calendar.YEAR) - before.get(Calendar.YEAR);
		dTime.month = after.get(Calendar.MONTH) - before.get(Calendar.MONTH);
		dTime.day_of_year = after.get(Calendar.DAY_OF_YEAR)
				- before.get(Calendar.DAY_OF_YEAR);
		dTime.hour = after.get(Calendar.HOUR_OF_DAY)
				- before.get(Calendar.HOUR_OF_DAY);
		dTime.min = after.get(Calendar.MINUTE) - before.get(Calendar.MINUTE);
		return dTime;
	}

	private class DiffTime {
		int year = 0, month = 0, day_of_year = 0, hour = 0, min = 0;

		@Override
		public String toString() {
			String res = "year:" + year + " month:" + month + " day_of_year:"
					+ day_of_year + " hour:" + hour + " min:" + min;
			return res;
		}
	}

	/*
	 * 格式化 期望效果: 12月12号 02:24 或 24号 22:01 或者 08:21
	 */
	private String getTime(Calendar calendar, boolean needMonth,
			boolean needDayOfMonth) {
		String res = "";
		String baseTime = formateTime(calendar.get(Calendar.HOUR_OF_DAY)) + ":"
				+ formateTime(calendar.get(Calendar.MINUTE));
		if (needMonth) {
			res += formateTime(calendar.get(Calendar.MONTH)) + "月" + baseTime;
		}
		if (needDayOfMonth) {
			res += formateTime(calendar.get(Calendar.DAY_OF_MONTH)) + "号 ";

		}
		res += baseTime;

		return res;
	}

	/*
     * 
     * 
     */
	private String formateTime(int time) {
		String res = null;
		if (time < 10) {
			res = "0" + time;
		} else {

			res = time + "";
		}
		return res;
	}
}
