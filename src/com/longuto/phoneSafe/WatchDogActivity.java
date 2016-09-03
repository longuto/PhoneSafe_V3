package com.longuto.phoneSafe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.longuto.phoneSafe.util.MD5;
import com.longuto.phoneSafe.util.PhoneSafeConfig;

public class WatchDogActivity extends Activity {
	private String lockPackage;	// ��ǰ�����İ�
	private int lockTaskid;	// ��ǰ����ʱ��ջid
	private PackageManager pm;	// ��������
	private PhoneSafeConfig config;	// ϵͳ����
	private Context context;	// ������
	
	@ViewInject(R.id.icon_iv)
	private ImageView mIconIv;	// ͼ��ؼ�
	@ViewInject(R.id.appName_tv)
	private TextView mAppNameTv;	// Ӧ�����ƿؼ�
	@ViewInject(R.id.enterUnlockPass_edt)	
	private EditText mEnterUnlockPassEdt;	//����������ؼ�
	
	@OnClick(R.id.unlock_btn)
	public void unlock(View v) {	// ������ť��������¼�
		String unlockPass = mEnterUnlockPassEdt.getText().toString().trim();	// ��������
		String password = config.getStringFromXml(PhoneSafeConfig.SAFE_PASSWORD);
		// ��������������ȷ
		if(password.equals(MD5.getMD5(unlockPass))) {
			Intent intent = new Intent();
			intent.setAction("com.longuto.phoneSafe.unlock");
			intent.putExtra("unlockPackage", lockPackage);	// ���ӵ�ǰ�ѽ����İ�
			intent.putExtra("unlockTask", lockTaskid);	// ���ӵ�ǰ�ѽ�����ջid
			sendBroadcast(intent);	// ���͹㲥
			finish();	//���ٵ�ǰ�Ŀ��Ź�����
		}
	}	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_watch_dog);
		ViewUtils.inject(this);	// ��ʼ���ؼ�
		context = this;
		config = new PhoneSafeConfig(context, PhoneSafeConfig.CONFIG);
		pm = getPackageManager();	// ��ʼ����������
		lockPackage = getIntent().getStringExtra("lockPackageName");	// ��ȡ��ͼ�����İ���
		lockTaskid = getIntent().getIntExtra("lockTaskId", 0);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		lockPackage = intent.getStringExtra("lockPackageName");	// ��ȡ��ͼ�����İ���
		lockTaskid = intent.getIntExtra("lockTaskId", 0);
		super.onNewIntent(intent);
	}
	
	@Override
	protected void onStart() {	
		try {
			PackageInfo packageInfo = pm.getPackageInfo(lockPackage, 0);
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
			Drawable icon = applicationInfo.loadIcon(pm);
			String appName = applicationInfo.loadLabel(pm).toString();
			mAppNameTv.setText(appName);
			mIconIv.setImageDrawable(icon);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		super.onStart();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			// ����������
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
