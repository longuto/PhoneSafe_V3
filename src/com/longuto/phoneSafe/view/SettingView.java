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
	
	private String mTitle;	// ����
	private String mDes_on;	// �Զ����¿�
	private String mDes_off;	// �Զ����¹�
	private TextView mTitleTv;	// ����ؼ�
	private TextView mDesTv;	// �����ؼ�
	
	// Ӧ��Ĭ�ϲ���
	public SettingView(Context context) {
		super(context);
		LogUtil.i(TAG, "context");
		initView();
	}
	
	// Ӧ���Զ������Ժ���ʽ
	public SettingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LogUtil.i(TAG, "defStyle");
		initView();
	}
	
	// Ӧ���Զ�������
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
		mTitleTv = (TextView) view.findViewById(R.id.title_tv);		// ��ȡ�����ı���ʾ�ؼ�	
		mDesTv = (TextView) view.findViewById(R.id.isUpdate_tv);	// �Ƿ������ı���ʾ�ؼ�
		
		mTitleTv.setText(mTitle);
	}
	
	/*
	 * �Զ��巽�� -- ���������ؼ��ı�ΪmDes_on
	 */
	public void setDeson() {
		mDesTv.setText(mDes_on);
	}
	
	/*
	 * �Զ��巽�� -- ���������ؼ��ı�ΪmDes_off
	 */
	public void setDesoff() {
		mDesTv.setText(mDes_off);
	}
}
