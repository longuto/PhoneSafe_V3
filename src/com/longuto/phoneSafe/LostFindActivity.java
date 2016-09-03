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
		
		initView();	// ��ʼ����ͼ
	}
	
	/**
	 * ��ʼ����ͼ
	 */
	private void initView() {
		FragmentManager manager = getFragmentManager();	// ��ȡFragment������
		// �Ƿ��ǵ�һ��������, Ĭ���ǵ�
		if(!mConfig.getBooleanFromXml(PhoneSafeConfig.IS_FIRST_SETUP, true)) {
			// ����ProtectedFragment֡����
			ProtectedFragment protectedFragment = new ProtectedFragment();
			manager.beginTransaction().replace(R.id.content_fly, protectedFragment, "protectedFragment").commit();
		}else {
			// ����ProtectSetupFragment֡����
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
