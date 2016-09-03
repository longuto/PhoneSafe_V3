package com.longuto.phoneSafe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.longuto.phoneSafe.util.LogUtil;
import com.longuto.phoneSafe.util.PhoneSafeConfig;
import com.longuto.phoneSafe.util.StreamUtil;
import com.longuto.phoneSafe.util.ToastUtil;

public class SplashActivity extends Activity {
	public static final int MSG_SHOW_DIALOG = 1;	// 显示对话框
	public static final int MSG_ENTER_HOMEACT = 2;	// 进入HomeActivity
	public static final int MSG_SERVER_ERROR = -1;	// 服务器连接失败
	protected static final String TAG = "SplashActivity";
	
	private TextView mVersionTv;	// 版本号TextView控件
	private ProgressBar mDownLoadPrb;	// 下载进度ProgressBar控件
	private TextView mProgressTv;	// 下载进度的文本控件
	private Context context;
	
	// 处理子线程传过来的Message
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int flag = msg.what;
			switch (flag) {
			case MSG_SHOW_DIALOG:
				LogUtil.i(TAG, "handler主线程 + 显示升级对话框");
				showUpdateDialog((JSONObject) msg.obj);	//显示升级对话框
				break;
			default:
				LogUtil.i(TAG, "handler主线程 + enterHomeAct");
				enterHomeAct();	// 进入HomeActivity界面
				break;
			}
		}	
	};
	
	/**
	 * 显示升级对话框
	 */
	private void showUpdateDialog(final JSONObject obj) {
		try {
			new AlertDialog.Builder(context)
			.setIcon(R.drawable.ic_launcher)
			.setTitle("phoneSafe:" + obj.getInt("code") + "版")
			.setMessage("是否升级PhoneSafe:" + obj.getString("des"))
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 从网上下载 app
					downloadApk(obj);	
				}
			})
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// 进入HomeActivity界面
					enterHomeAct();
				}
			})
			.setCancelable(false)
			.create()
			.show();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 下载服务器的PhoneSafe软件
	 */
	protected void downloadApk(JSONObject obj) {
		try {
			String apkUrl = (String) obj.get("apkurl");	// 获取服务器上软件下载路径
			/*
			 * 使用Xutils工具类中 -- httpUtils下载apk
			 */
			HttpUtils httpUtils = new HttpUtils();
			httpUtils.download(apkUrl, getExternalCacheDir().getAbsolutePath() + "/phoneSafe.apk", new RequestCallBack<File>() {			
				// 下载成功
				@Override
				public void onSuccess(ResponseInfo<File> responseInfo) {
					LogUtil.i(TAG, "download + onSuccess");
					install();		
				}
				// 下载失败
				@Override
				public void onFailure(HttpException error, String msg) {
					LogUtil.i(TAG, "download + onFailure");
					ToastUtil.show(context, "下载软件失败!");
					enterHomeAct();
				}
				// 下载进度设置
				@Override
				public void onLoading(long total, long current, boolean isUploading) {
					mDownLoadPrb.setVisibility(View.VISIBLE);	// 设置下载框可见
					mDownLoadPrb.setMax((int)total);	// 设置最大值
					mDownLoadPrb.setProgress((int) current);	// 设置当前值
					float currentF = current;	// 转换成单精度
					float currentPercent = (currentF/total) * 100;
					mProgressTv.setText(currentPercent + "%");	// 设置进度文本的内容
					super.onLoading(total, current, isUploading);
				}	
			});	
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 安装新下载软件
	 */
	protected void install() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setDataAndType(Uri.fromFile(new File(getExternalCacheDir(), "phoneSafe.apk")), "application/vnd.android.package-archive");
		startActivityForResult(intent, 6);	// 要求返回结果,并进行处理
	}
	
	/**
	 * 
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtil.i(TAG, "onActivityResult" + resultCode);
		enterHomeAct();
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		// 初始化参数
		initView();
		
		/**
		 * 升级安全卫士
		 */
		upDateApplication();
	}

	/**
	 *  1.检测安全卫士的设置是否为自动升级,否则直接进入HomeActivity
	 *  2.如果自动升级:获取服务器的版本信息
	 * 	3.如果服务器的版本号大于应用的版本号,弹出升级对话框,供用户选择
	 * 	4.如果用户点击确定,下载应用,弹出下载进度条
	 */
	private void upDateApplication() {
		PhoneSafeConfig config = new PhoneSafeConfig(context, PhoneSafeConfig.CONFIG);
		final long startTime = System.currentTimeMillis();	// 开始进入的时间
		// 是否设置为自动升级,没有设置默认为自动升级
		if(config.getBooleanFromXml(PhoneSafeConfig.AUTO_UPDATE, true)) {
			// 开启子线程,获取服务器的版本信息
			new Thread() {
				public void run() {
					LogUtil.i(TAG, "检测服务端是否有新版本");
					Message msg = new Message();
					try {
						URL url = new URL(PhoneSafeConfig.SERVER_INFO_URL);
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setRequestMethod("GET");	// 设置请求方式
						conn.setConnectTimeout(5000);	// 设置请求时间
						// 请求成功
						if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
							LogUtil.i(TAG, "连接服务器成功!");
							InputStream is = conn.getInputStream();
							String jsonString = new String(StreamUtil.parseInputstram(is), "UTF-8");
							JSONObject jonResult = new JSONObject(jsonString);
							int newCode = jonResult.getInt("code");
							if(newCode > getVersionCode()) {
								msg.what = MSG_SHOW_DIALOG;
								msg.obj = jonResult;
							}else {
								msg.what = MSG_ENTER_HOMEACT;
							}
						}else {
							LogUtil.i(TAG, "连接服务器失败!");
							msg.what = MSG_SERVER_ERROR;
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						long durTime = System.currentTimeMillis() - startTime;	// 持续时间
						if(durTime < 2000) {		
							SystemClock.sleep(2000 - durTime);
						}
						handler.sendMessage(msg);
					}
				}
			}.start();
			
		// 没有自动升级,直接进入HomeActivity界面
		}else {
			LogUtil.i(TAG, "没有设置检测更新");
			new Thread() {
				public void run() {		
					SystemClock.sleep(2000);	// 休眠3s
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							enterHomeAct();	// 在主线程中进入HomeActivity
						}
					});			
				}
			}.start();
		}
		
		// 将号码归属地信息文件,导入到手机内存上的数据库文件夹
		copyDb();
	}

	/**
	 * 将手机号码归属地文件从assets文件夹中拷贝到手机内存的数据库中
	 */
	private void copyDb() {
		File file = new File(getFilesDir(), "address.db");	// 需要将归属地文件拷贝至此文件
		// 假如文件不存在,执行拷贝操作
		if(!file.exists()) {
			InputStream is = null;	// 输入流
			FileOutputStream fos = null;	// 输出流
			try {
				is = getAssets().open("address.db");	// 获取address.db文件输入流
				fos = new FileOutputStream(file);	// 输出流
				byte[] arr = new byte[1024];
				int len = 0;
				while ((len = is.read(arr)) != -1) {
					fos.write(arr, 0, len);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();		// 关闭流
					fos.close();	// 关闭流
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 进入到HomeActivity界面
	 */
	protected void enterHomeAct() {
		Intent intent = new Intent(context, HomeActivity.class);
		startActivity(intent);
		finish();
	}
	
	/**
	 * 初始化界面和参数
	 */
	private void initView() {
		mVersionTv = (TextView) findViewById(R.id.version_tv);
		mDownLoadPrb = (ProgressBar) findViewById(R.id.downLoad_pgb);
		context = this;
		mProgressTv = (TextView) findViewById(R.id.progress_tv);
		mVersionTv.setText("版本号:" + getVersionCode());
	}

	/**
	 * 获取安全卫士版本号
	 * @return 版本号
	 */
	private int getVersionCode() {
		try {
			PackageManager packageManager = context.getPackageManager();	// 获取包管理器
			PackageInfo info = packageManager.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
