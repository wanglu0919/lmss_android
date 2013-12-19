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
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Toast;

import com.challentec.lmss.bean.ResponseData;
import com.challentec.lmss.bean.UpdateInfo;
import com.challentec.lmss.engine.DownLoadTask;
import com.challentec.lmss.listener.AppMessageLinstener;
import com.challentec.lmss.net.SocketClient;
import com.challentec.lmss.net.SynHandler;
import com.challentec.lmss.net.SynTask;
import com.challentec.lmss.recever.AppMessageRecever;
import com.challentec.lmss.util.ClientAPI;
import com.challentec.lmss.util.LogUtil;
import com.challentec.lmss.util.Protocol;
import com.challentec.lmss.util.UIHelper;
import com.challentec.lmss.view.LoadProgressView;

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
	private AppContext appContext;
	private SocketClient socketClient;
	private LoadProgressView pb_load;
	private AppMessageRecever appMessageRecever;
	private static final int SEVER_VEFIY_TIME_OUT = 0x03;// 服务器验证码超时
	private AppManager appManager;
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
			case SEVER_VEFIY_TIME_OUT:
				serverVieryTimeOut();
				break;

			}

		}

	};

	/**
	 * 注册消息监听
	 * 
	 * @author 泰得利通 wanglu
	 */
	public void registerAppMessageRecever() {
		appMessageRecever = appManager.registerAppMessageRecever(this);
		appMessageRecever.setAppMessageLinstener(new MainAppMessageLinstener());// 注册消息监听
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (appMessageRecever != null) {
			unregisterReceiver(appMessageRecever);
		}
		
		
	}

	/**
	 * 服务器返回消息监听类
	 * 
	 * @author 泰得利通 wanglu
	 * 
	 */
	private class MainAppMessageLinstener implements AppMessageLinstener {

		@Override
		public void onRespnseDataReceve(ResponseData responseData) {

			if (responseData.getFunctionCode().equals(Protocol.C_SEVER_VERIFY)) {// 服务器验证返回数据
				socketClient.setVerify(true);// 验证通过
				LogUtil.i(LogUtil.LOG_TAG_I, "服务器验证成功");
				UIHelper.showToask(appContext, "连接服务器成功");
				pb_load.setVisibility(View.GONE);
				appManager.startPolling();// 开始心跳
				checkUpdate();

			}
		}

	}

	/**
	 * 服务器验证超时 wanglu 泰得利通
	 */
	protected void serverVieryTimeOut() {

		if (!socketClient.isVerify()) {// 没有通过服务器验证码
			LogUtil.i(LogUtil.LOG_TAG_I, "验证超时,重新连接");
			connect();// 重新连接服务器
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		pd = new ProgressDialog(this);
		appContext = (AppContext) getApplication();
		pd.setMessage("正在下载...");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setCancelable(false);
		final View view = View.inflate(this, R.layout.start, null);
		pb_load = (LoadProgressView) view.findViewById(R.id.pb_load);
		setContentView(view);
		appManager = AppManager.getManager(appContext);
		registerAppMessageRecever();

		// 渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(2000);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				// checkUpdate();// 检查更新

				connect();// 连接服务器
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
	 * 连接服务器
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void connect() {

		pb_load.setVisibility(View.VISIBLE);
		pb_load.setProgressText(R.string.tip_msg_pb_connecting_server);
		socketClient = SocketClient.getSocketClient();

		new SynTask(new SynHandler() {

			@Override
			public void onConnectSuccess(String code) {// 连接成功

				LogUtil.i(LogUtil.LOG_TAG_CONNECT, "连接服务器成功");
				/**
				 * 验证成功发送验证包
				 */
				sendSeverVifyData();
			}

			@Override
			public void onFianly() {

			}

		}, appContext).connectServer(socketClient);

	}

	/**
	 * 服务器验证 wanglu 泰得利通
	 */
	private void sendSeverVifyData() {

		LogUtil.i(LogUtil.LOG_TAG_I, "发送了验证包");
		String apiData = ClientAPI.getApiStr(Protocol.C_SEVER_VERIFY);

		new SynTask(appContext).writeData(apiData, false);

		new Thread(new Runnable() {// 超时处理

					@Override
					public void run() {

						handler.sendEmptyMessageDelayed(SEVER_VEFIY_TIME_OUT,
								2000);

					}
				}).start();

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
	 * 连接服务器
	 * 
	 * @author 泰得利通 wanglu
	 */

	/**
	 * 监测更新 wanglu 泰得利通
	 */
	private void checkUpdate() {

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					Thread.sleep(1000);

				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}

				updateInfo = new UpdateInfo();
				String url = getString(R.string.updateurl_outer);
				if (socketClient.getIpType() == SocketClient.IP_INNER) {// 内网IP
					url = getString(R.string.updateurl_inner);
				}
				boolean update = AppManager.getManager(getApplicationContext())
						.isUpdate(updateInfo, url);// 检查更新
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
							redirectTo();
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
		builder.setCancelable(false);
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
		if(socketClient!=null){
			socketClient.dispose();//关掉连接
			appManager.stopPolling();//停止心跳
		}
		this.finish();
		
		startActivity(intent);
		
		

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			socketClient.dispose();// 关闭连接

			finish();

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
