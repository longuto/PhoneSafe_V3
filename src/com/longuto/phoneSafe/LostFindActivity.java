package com.longuto.phoneSafe;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.longuto.phoneSafe.fragment.ProtectSetupFragment;
import com.longuto.phoneSafe.fragment.ProtectedFragment;
import com.longuto.phoneSafe.util.PhoneSafeConfig;

public class LostFindActivity extends Activity {
	private static Context context;
	private PhoneSafeConfig mConfig;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lost_find);
		
		context = this;
		mConfig = new PhoneSafeConfig(context, PhoneSafeConfig.CONFIG);
		
		initView();	// 初始化视图
	}
	
	/**
	 * 初始化视图
	 */
	private void initView() {
		FragmentManager manager = getFragmentManager();	// 获取Fragment管理器
		// 是否是第一次设置向导, 默认是的
		if(!mConfig.getBooleanFromXml(PhoneSafeConfig.IS_FIRST_SETUP, true)) {
			// 进入ProtectedFragment帧界面
			ProtectedFragment protectedFragment = new ProtectedFragment();
			manager.beginTransaction().replace(R.id.content_fly, protectedFragment, "protectedFragment").commit();
		}else {
			// 进入ProtectSetupFragment帧界面
			ProtectSetupFragment protectSetupFragment = new ProtectSetupFragment();
			manager.beginTransaction().replace(R.id.content_fly, protectSetupFragment, "protectSetupFragment").commit();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 6) {
			if(RESULT_OK == resultCode) {
				EditText safePhoneNumEdt = (EditText) findViewById(R.id.safePhoneNum_edt);
				String safePhoneNum = data.getStringExtra("PHONENUM");
				safePhoneNumEdt.setText(safePhoneNum);
				safePhoneNumEdt.setSelection(safePhoneNum.length());
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
