package com.longuto.phoneSafe.setupPage;

import android.content.Context;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.longuto.phoneSafe.util.PhoneSafeConfig;

/**
 * ViewPage��Ƭ�λ���
 * @author longuto
 */
public abstract class BasePage {
	public Context context;
	public View rootView;		// ����ͼ
	public PhoneSafeConfig mConfig;	// ����������� 
	
	public BasePage(Context context) {
		this.context = context;
		mConfig = new PhoneSafeConfig(context, PhoneSafeConfig.CONFIG);
		rootView = initView();
		ViewUtils.inject(this, rootView);
	}
	
	public View getRootView() {
		return rootView;
	}
	
	public abstract View initView();	// ��ʼ����ͼ
	public abstract void initData();	// ��ʼ������

}
