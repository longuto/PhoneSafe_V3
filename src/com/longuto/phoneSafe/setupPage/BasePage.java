package com.longuto.phoneSafe.setupPage;

import android.content.Context;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.longuto.phoneSafe.util.PhoneSafeConfig;

/**
 * ViewPage的片段基类
 * @author longuto
 */
public abstract class BasePage {
	public Context context;
	public View rootView;		// 根视图
	public PhoneSafeConfig mConfig;	// 配置软件对象 
	
	public BasePage(Context context) {
		this.context = context;
		mConfig = new PhoneSafeConfig(context, PhoneSafeConfig.CONFIG);
		rootView = initView();
		ViewUtils.inject(this, rootView);
	}
	
	public View getRootView() {
		return rootView;
	}
	
	public abstract View initView();	// 初始化视图
	public abstract void initData();	// 初始化数据

}
