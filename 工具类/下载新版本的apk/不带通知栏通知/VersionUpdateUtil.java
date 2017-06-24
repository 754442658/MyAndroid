package com.feirui.ecmobile.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class VersionUpdateUtil {
	public static AlertDialog dialog;
	/**
	 * 获取sd卡的路径
	 */
	private static String getSdPath() {
		String sdPath = "-1";
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (!sdCardExist) {
			sdPath = "-1";
		} else {
			sdPath = Environment.getExternalStorageDirectory().toString();
		}
		return sdPath;
	}

	/**
	 * 下载最新版本的apk
	 * 
	 * @param url
	 *            下载链接
	 * @param con
	 * @param apkHandler
	 *            下载完成以后需要返回一个消息进行安装 2001代表下载完成，msg.obj是下载好的apk文件
	 */
	public static void ShowDownloadApkDialog(final String url, final Context con,
			final Handler apkHandler) {
		final String sdPah = getSdPath();

		dialog = new AlertDialog.Builder(con)
				.setTitle("提示")
				.setMessage("有新版本，是否更新？")
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if (sdPah.equals("-1")) {
							Toast.makeText(con, "未识别到SD卡，下载失败！", 0).show();
							return;
						}
						Toast.makeText(con, "正在下载...", 0).show();
						new Thread(new Runnable() {
							@Override
							public void run() {
								final File apkPath;
								HttpClient client = new DefaultHttpClient();
								HttpGet method = new HttpGet(url);
								InputStream is = null;
								FileOutputStream out = null;
								File packagePath = new File(sdPah + "/Apk");
								if (!packagePath.exists()) {
									packagePath.mkdirs();
								}
								try {
									apkPath = new File(packagePath
											+ "/ishop.apk");
									if (!apkPath.exists()) {
										apkPath.createNewFile();
									} else {
										apkPath.delete();
									}
									HttpResponse res = client.execute(method);
									is = res.getEntity().getContent();
									out = new FileOutputStream(apkPath);
									int byteCount = 0;
									byte[] data = new byte[2048];
									while ((byteCount = is.read(data)) != -1) {
										out.write(data, 0, byteCount);
									}
									Log.e("下载apk", "更新文件下载完成！");
									// 发送消息，apk下载完成，可以安装
									Message msg = new Message();
									msg.what = 0;
									msg.obj = apkPath;
									apkHandler.sendMessage(msg);
								} catch (IOException e) {
									e.printStackTrace();
								} finally {
									try {
										if (out != null) {
											out.close();
											out = null;
										}
										if (is != null) {
											is.close();
											is = null;
										}
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
						}).start();
						arg0.dismiss();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
					}
				}).setCancelable(false).create();
		dialog.show();

	}
}
