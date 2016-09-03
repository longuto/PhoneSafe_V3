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
	private EditText mSafePhoneNumEdt;	// 安全号码编辑框
	
	@OnClick(R.id.saveNum_btn)
	public void saveNum(View v) {
		String safePhoneNum = mSafePhoneNumEdt.getText().toString().trim();
		safePhoneNum = safePhoneNum.replace("-", "");	// 将号码中的"-"去掉
		if(TextUtils.isEmpty(safePhoneNum)) {
			ToastUtil.show(context, "您未设置安全号码!");
			return;
		}
		mConfig.putStringToXml(PhoneSafeConfig.SAFE_PHONENUM, safePhoneNum);
		ToastUtil.show(context, "号码已保存，继续下一步操作！");
	}
	
	@OnClick(R.id.choosePeople_btn)
	public void choosePeople(View v) {	// 选择联系人按钮点击事件
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
		// 获取安全号码, 没有设置默认为110
		String safePhoneNum = mConfig.getStringFromXml(PhoneSafeConfig.SAFE_PHONENUM, "110");
		mSafePhoneNumEdt.setText(safePhoneNum);
		mSafePhoneNumEdt.setSelection(safePhoneNum.length());
	}
	
}
