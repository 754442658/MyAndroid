package com.shixiutv.utils;

import android.app.Activity;
import android.graphics.Color;
import android.widget.TextView;

import com.shixiutv.R;

import java.util.Timer;
import java.util.TimerTask;

public class ClickSmsBtn {
	private static Timer timer;
	private static TimerTask timerTask;
	private static int time = 60;

	/**
	 * 点击获取验证码是textview要设置显示倒计时
	 *
	 * @param tv
	 * @param activity
	 *            当前的activity
	 */

	public static void clickGetSms(final Activity activity, final TextView tv) {
		// 清除点击的控件的onclick事件
		tv.setClickable(false);
		tv.setBackgroundColor(Color.rgb(0xd5, 0xd5, 0xd5));
		timer = new Timer();
		timerTask = new TimerTask() {

			@Override
			public void run() {
				time--;
				if (time > 0) {
					activity.runOnUiThread(new Runnable() {
						public void run() {
							tv.setText(time + "秒后可重新发送");
						}
					});
				} else {
					tv.setClickable(true);
					activity.runOnUiThread(new Runnable() {
						public void run() {
							tv.setText("获取验证码");
							tv.setBackgroundResource(R.drawable.btn_org);
						}
					});
					//停止计时器
					stopTimer();
				}
			}
		};
		// 启动计时器
		// 执行计时器
		if (timer != null && timerTask != null) {
			timer.schedule(timerTask, 0, 1000);
		}
	}

	/**
	 * 退出当前页面时停止timer
	 */
	public static void stopTimer(){
		time = 60;
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (timerTask != null) {
			timerTask.cancel();
			timerTask = null;
		}
	}
}
