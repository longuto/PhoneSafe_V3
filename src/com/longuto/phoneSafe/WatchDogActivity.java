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
	private String lockPackage;	// 当前锁定的包
	private int lockTaskid;	// 当前锁定时的栈id
	private PackageManager pm;	// 包管理者
	private PhoneSafeConfig config;	// 系统配置
	private Context context;	// 上下文
	
	@ViewInject(R.id.icon_iv)
	private ImageView mIconIv;	// 图标控件
	@ViewInject(R.id.appName_tv)
	private TextView mAppNameTv;	// 应用名称控件
	@ViewInject(R.id.enterUnlockPass_edt)	
	private EditText mEnterUnlockPassEdt;	//　密码输入控件
	
	@OnClick(R.id.unlock_btn)
	public void unlock(View v) {	// 解锁按钮点击监听事件
		String unlockPass = mEnterUnlockPassEdt.getText().toString().trim();	// 解锁密码
		String password = config.getStringFromXml(PhoneSafeConfig.SAFE_PASSWORD);
		// 如果输入的密码正确
		if(password.equals(MD5.getMD5(unlockPass))) {
			Intent intent = new Intent();
			intent.setAction("com.longuto.phoneSafe.unlock");
			intent.putExtra("unlockPackage", lockPackage);	// 附加当前已解锁的包
			intent.putExtra("unlockTask", lockTaskid);	// 附加当前已解锁的栈id
			sendBroadcast(intent);	// 发送广播
			finish();	//销毁当前的看门狗界面
		}
	}	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_watch_dog);
		ViewUtils.inject(this);	// 初始化控件
		context = this;
		config = new PhoneSafeConfig(context, PhoneSafeConfig.CONFIG);
		pm = getPackageManager();	// 初始化包管理器
		lockPackage = getIntent().getStringExtra("lockPackageName");	// 获取意图所带的包名
		lockTaskid = getIntent().getIntExtra("lockTaskId", 0);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		lockPackage = intent.getStringExtra("lockPackageName");	// 获取意图所带的包名
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
			// 进入主界面
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
