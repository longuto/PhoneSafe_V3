package com.longuto.phoneSafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.longuto.phoneSafe.R;
import com.longuto.phoneSafe.util.LogUtil;
import com.longuto.phoneSafe.util.PhoneSafeConfig;

public class SettingView extends RelativeLayout {
	private static final String TAG = "SettingView";
	private final String NAMESPACE = "http://schemas.android.com/apk/res/com.longuto.phoneSafe";
	
	private String mTitle;	// 标题
	private String mDes_on;	// 自动更新开
	private String mDes_off;	// 自动更新关
	private TextView mTitleTv;	// 标题控件
	private TextView mDesTv;	// 描述控件
	
	// 应用默认布局
	public SettingView(Context context) {
		super(context);
		LogUtil.i(TAG, "context");
		initView();
	}
	
	// 应用自定义属性和样式
	public SettingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LogUtil.i(TAG, "defStyle");
		initView();
	}
	
	// 应用自定义属性
	public SettingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LogUtil.i(TAG, "attrs");
		mTitle = attrs.getAttributeValue(NAMESPACE, "setting_title");
		mDes_on = attrs.getAttributeValue(NAMESPACE, "des_on");
		mDes_off = attrs.getAttributeValue(NAMESPACE, "des_off");
		initView();
	}

	private void initView() {
		View view = View.inflate(getContext(), R.layout.setting_item, this);
		mTitleTv = (TextView) view.findViewById(R.id.title_tv);		// 获取标题文本显示控件	
		mDesTv = (TextView) view.findViewById(R.id.isUpdate_tv);	// 是否升级文本显示控件
		
		mTitleTv.setText(mTitle);
	}
	
	/*
	 * 自定义方法 -- 设置描述控件文本为mDes_on
	 */
	public void setDeson() {
		mDesTv.setText(mDes_on);
	}
	
	/*
	 * 自定义方法 -- 设置描述控件文本为mDes_off
	 */
	public void setDesoff() {
		mDesTv.setText(mDes_off);
	}
}
