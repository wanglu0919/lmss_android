package com.challentec.lmss.app;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Toast;

import com.challentec.lmss.bean.UpdateInfo;
import com.challentec.lmss.engine.DownLoadTask;

/**
 * 开始
 * 
 * @author wanglu 泰得利通
 * 
 */
public class AppStartActivity extends Activity {
	private UpdateInfo updateInfo;
	private ProgressDialog pd;
	protected static final int UPDATE_APK = 7;

	protected static final int NO_UPDATE_APK = 8;

	public static final int DOWLOAD_ERROR = 9;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case UPDATE_APK:
				showUpdatDialog();
				break;
			case NO_UPDATE_APK:
				redirectTo();
				break;
			case DOWLOAD_ERROR:
				Toast.makeText(AppStartActivity.this, "下载出错", Toast.LENGTH_LONG)
						.show();
				redirectTo();
				break;

			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		pd = new ProgressDialog(this);

		pd.setMessage("正在下载...");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setCancelable(false);
		final View view = View.inflate(this, R.layout.start, null);
		setContentView(view);

		// 渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(2000);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				checkUpdate();// 检查更新
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}

		});

	
	}

	/**
	 * 跳转到...
	 */
	private void redirectTo() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 监测更新 wanglu 泰得利通
	 */
	private void checkUpdate() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				updateInfo = new UpdateInfo();
				boolean update = AppManager.getManager(getApplicationContext())
						.isUpdate(updateInfo);// 检查更新
				if (update) {
					handler.sendEmptyMessage(UPDATE_APK);
				} else {
					handler.sendEmptyMessage(NO_UPDATE_APK);
				}
			}
		}).start();
	}

	private class DownLoadThreadTask implements Runnable {

		private String path;

		private String filePath;

		public DownLoadThreadTask(String path, String filePath) {

			this.path = path;
			this.filePath = filePath;
		}

		@Override
		public void run() {

			try {
				File file = DownLoadTask.dowLoadNewSoft(path, filePath, pd);
				pd.dismiss();
				install(file);// 安装
			} catch (Exception e) {
				e.printStackTrace();
				handler.sendEmptyMessage(DOWLOAD_ERROR);
				pd.dismiss();

			}

		}

	}

	/**
	 * 更新对话框 wanglu 泰得利通
	 */
	private void showUpdatDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setIcon(R.drawable.applog);
		builder.setTitle("更新消息");
		builder.setMessage("版本" + updateInfo.getVersion() + " 更新信息:"
				+ updateInfo.getDescription());
		

		builder.setPositiveButton("确定",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						if (Environment.getExternalStorageState().equals(
								Environment.MEDIA_MOUNTED)) {

							File file = new File(AppConfig.APP_PATH);
							if (!file.exists()) {
								file.mkdirs();
							}
							DownLoadThreadTask dowTask = new DownLoadThreadTask(
									updateInfo.getUrl(), AppConfig.APP_PATH
											+ "LMSS.apk");

							new Thread(dowTask).start();
							pd.show();

						} else {
							Toast.makeText(AppStartActivity.this, "SDK不存在",
									Toast.LENGTH_SHORT).show();

						}

					}
				});

		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						redirectTo();
					}

				});

		builder.create().show();

	}

	/**
	 * 
	 * wanglu 泰得利通 安装APK
	 * 
	 * @param file
	 */
	private void install(File file) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		this.finish();
		startActivity(intent);

	}
}
