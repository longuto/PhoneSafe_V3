package com.longuto.phoneSafe.setupPage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.longuto.phoneSafe.ListContactActivity;
import com.longuto.phoneSafe.R;
import com.longuto.phoneSafe.util.PhoneSafeConfig;
import com.longuto.phoneSafe.util.ToastUtil;

public class Setup3Page extends BasePage {
	@ViewInject(R.id.safePhoneNum_edt)
	private EditText mSafePhoneNumEdt;	// ��ȫ����༭��
	
	@OnClick(R.id.saveNum_btn)
	public void saveNum(View v) {
		String safePhoneNum = mSafePhoneNumEdt.getText().toString().trim();
		safePhoneNum = safePhoneNum.replace("-", "");	// �������е�"-"ȥ��
		if(TextUtils.isEmpty(safePhoneNum)) {
			ToastUtil.show(context, "��δ���ð�ȫ����!");
			return;
		}
		mConfig.putStringToXml(PhoneSafeConfig.SAFE_PHONENUM, safePhoneNum);
		ToastUtil.show(context, "�����ѱ��棬������һ��������");
	}
	
	@OnClick(R.id.choosePeople_btn)
	public void choosePeople(View v) {	// ѡ����ϵ�˰�ť����¼�
		Intent intent = new Intent(context, ListContactActivity.class);
		((Activity)context).startActivityForResult(intent, 6);
	}
	
	public Setup3Page(Context context) {
		super(context);
	}

	@Override
	public View initView() {
		rootView = View.inflate(context, R.layout.layout_setup3, null);
		return rootView;
	}

	@Override
	public void initData() {
		// ��ȡ��ȫ����, û������Ĭ��Ϊ110
		String safePhoneNum = mConfig.getStringFromXml(PhoneSafeConfig.SAFE_PHONENUM, "110");
		mSafePhoneNumEdt.setText(safePhoneNum);
		mSafePhoneNumEdt.setSelection(safePhoneNum.length());
	}
	
}
