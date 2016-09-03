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
	public static final int MSG_SHOW_DIALOG = 1;	// ��ʾ�Ի���
	public static final int MSG_ENTER_HOMEACT = 2;	// ����HomeActivity
	public static final int MSG_SERVER_ERROR = -1;	// ����������ʧ��
	protected static final String TAG = "SplashActivity";
	
	private TextView mVersionTv;	// �汾��TextView�ؼ�
	private ProgressBar mDownLoadPrb;	// ���ؽ���ProgressBar�ؼ�
	private TextView mProgressTv;	// ���ؽ��ȵ��ı��ؼ�
	private Context context;
	
	// �������̴߳�������Message
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int flag = msg.what;
			switch (flag) {
			case MSG_SHOW_DIALOG:
				LogUtil.i(TAG, "handler���߳� + ��ʾ�����Ի���");
				showUpdateDialog((JSONObject) msg.obj);	//��ʾ�����Ի���
				break;
			default:
				LogUtil.i(TAG, "handler���߳� + enterHomeAct");
				enterHomeAct();	// ����HomeActivity����
				break;
			}
		}	
	};
	
	/**
	 * ��ʾ�����Ի���
	 */
	private void showUpdateDialog(final JSONObject obj) {
		try {
			new AlertDialog.Builder(context)
			.setIcon(R.drawable.ic_launcher)
			.setTitle("phoneSafe:" + obj.getInt("code") + "��")
			.setMessage("�Ƿ�����PhoneSafe:" + obj.getString("des"))
			.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// ���������� app
					downloadApk(obj);	
				}
			})
			.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// ����HomeActivity����
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
	 * ���ط�������PhoneSafe���
	 */
	protected void downloadApk(JSONObject obj) {
		try {
			String apkUrl = (String) obj.get("apkurl");	// ��ȡ���������������·��
			/*
			 * ʹ��Xutils�������� -- httpUtils����apk
			 */
			HttpUtils httpUtils = new HttpUtils();
			httpUtils.download(apkUrl, getExternalCacheDir().getAbsolutePath() + "/phoneSafe.apk", new RequestCallBack<File>() {			
				// ���سɹ�
				@Override
				public void onSuccess(ResponseInfo<File> responseInfo) {
					LogUtil.i(TAG, "download + onSuccess");
					install();		
				}
				// ����ʧ��
				@Override
				public void onFailure(HttpException error, String msg) {
					LogUtil.i(TAG, "download + onFailure");
					ToastUtil.show(context, "�������ʧ��!");
					enterHomeAct();
				}
				// ���ؽ�������
				@Override
				public void onLoading(long total, long current, boolean isUploading) {
					mDownLoadPrb.setVisibility(View.VISIBLE);	// �������ؿ�ɼ�
					mDownLoadPrb.setMax((int)total);	// �������ֵ
					mDownLoadPrb.setProgress((int) current);	// ���õ�ǰֵ
					float currentF = current;	// ת���ɵ�����
					float currentPercent = (currentF/total) * 100;
					mProgressTv.setText(currentPercent + "%");	// ���ý����ı�������
					super.onLoading(total, current, isUploading);
				}	
			});	
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * ��װ���������
	 */
	protected void install() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setDataAndType(Uri.fromFile(new File(getExternalCacheDir(), "phoneSafe.apk")), "application/vnd.android.package-archive");
		startActivityForResult(intent, 6);	// Ҫ�󷵻ؽ��,�����д���
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
		
		// ��ʼ������
		initView();
		
		/**
		 * ������ȫ��ʿ
		 */
		upDateApplication();
	}

	/**
	 *  1.��ⰲȫ��ʿ�������Ƿ�Ϊ�Զ�����,����ֱ�ӽ���HomeActivity
	 *  2.����Զ�����:��ȡ�������İ汾��Ϣ
	 * 	3.����������İ汾�Ŵ���Ӧ�õİ汾��,���������Ի���,���û�ѡ��
	 * 	4.����û����ȷ��,����Ӧ��,�������ؽ�����
	 */
	private void upDateApplication() {
		PhoneSafeConfig config = new PhoneSafeConfig(context, PhoneSafeConfig.CONFIG);
		final long startTime = System.currentTimeMillis();	// ��ʼ�����ʱ��
		// �Ƿ�����Ϊ�Զ�����,û������Ĭ��Ϊ�Զ�����
		if(config.getBooleanFromXml(PhoneSafeConfig.AUTO_UPDATE, true)) {
			// �������߳�,��ȡ�������İ汾��Ϣ
			new Thread() {
				public void run() {
					LogUtil.i(TAG, "��������Ƿ����°汾");
					Message msg = new Message();
					try {
						URL url = new URL(PhoneSafeConfig.SERVER_INFO_URL);
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setRequestMethod("GET");	// ��������ʽ
						conn.setConnectTimeout(5000);	// ��������ʱ��
						// ����ɹ�
						if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
							LogUtil.i(TAG, "���ӷ������ɹ�!");
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
							LogUtil.i(TAG, "���ӷ�����ʧ��!");
							msg.what = MSG_SERVER_ERROR;
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						long durTime = System.currentTimeMillis() - startTime;	// ����ʱ��
						if(durTime < 2000) {		
							SystemClock.sleep(2000 - durTime);
						}
						handler.sendMessage(msg);
					}
				}
			}.start();
			
		// û���Զ�����,ֱ�ӽ���HomeActivity����
		}else {
			LogUtil.i(TAG, "û�����ü�����");
			new Thread() {
				public void run() {		
					SystemClock.sleep(2000);	// ����3s
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							enterHomeAct();	// �����߳��н���HomeActivity
						}
					});			
				}
			}.start();
		}
		
		// �������������Ϣ�ļ�,���뵽�ֻ��ڴ��ϵ����ݿ��ļ���
		copyDb();
	}

	/**
	 * ���ֻ�����������ļ���assets�ļ����п������ֻ��ڴ�����ݿ���
	 */
	private void copyDb() {
		File file = new File(getFilesDir(), "address.db");	// ��Ҫ���������ļ����������ļ�
		// �����ļ�������,ִ�п�������
		if(!file.exists()) {
			InputStream is = null;	// ������
			FileOutputStream fos = null;	// �����
			try {
				is = getAssets().open("address.db");	// ��ȡaddress.db�ļ�������
				fos = new FileOutputStream(file);	// �����
				byte[] arr = new byte[1024];
				int len = 0;
				while ((len = is.read(arr)) != -1) {
					fos.write(arr, 0, len);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();		// �ر���
					fos.close();	// �ر���
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * ���뵽HomeActivity����
	 */
	protected void enterHomeAct() {
		Intent intent = new Intent(context, HomeActivity.class);
		startActivity(intent);
		finish();
	}
	
	/**
	 * ��ʼ������Ͳ���
	 */
	private void initView() {
		mVersionTv = (TextView) findViewById(R.id.version_tv);
		mDownLoadPrb = (ProgressBar) findViewById(R.id.downLoad_pgb);
		context = this;
		mProgressTv = (TextView) findViewById(R.id.progress_tv);
		mVersionTv.setText("�汾��:" + getVersionCode());
	}

	/**
	 * ��ȡ��ȫ��ʿ�汾��
	 * @return �汾��
	 */
	private int getVersionCode() {
		try {
			PackageManager packageManager = context.getPackageManager();	// ��ȡ��������
			PackageInfo info = packageManager.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
