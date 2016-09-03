package com.longuto.phoneSafe.setupPage;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.longuto.phoneSafe.R;
import com.longuto.phoneSafe.util.PhoneSafeConfig;
import com.longuto.phoneSafe.util.ToastUtil;
import com.longuto.phoneSafe.view.SettingView;

public class Setup2Page extends BasePage {
	@ViewInject(R.id.myset_setv)
	private SettingView mBindSimSetV;	// ��sim�����Զ��岼��
	@ViewInject(R.id.isUpdate_ckb)
	private CheckBox mIsBindSimCkb;	// �Զ��岼���е�CheckBox�ؼ�	

	public Setup2Page(Context context) {
		super(context);
	}

	@Override
	public View initView() {
		rootView = View.inflate(context, R.layout.layout_setup2, null);
		return rootView;
	}

	@Override
	public void initData() {
		// ����󶨵�sim��Ų�Ϊ��
				if(!TextUtils.isEmpty(mConfig.getStringFromXml(PhoneSafeConfig.BIND_SIM_SERIAL, ""))) {
					mBindSimSetV.setDeson();	//������������
					mIsBindSimCkb.setChecked(true);
				}else {
					mBindSimSetV.setDesoff();	// ������������
					mIsBindSimCkb.setChecked(false);
					mConfig.putStringToXml(PhoneSafeConfig.BIND_SIM_SERIAL, "");	// �ⲽ��Ϊ�˷�ֹ����ļ��SIM�����кŵĳ�ͻ
				}
				/*
				 * ����sim�󶨰�ťCheckBoxѡ���¼�
				 */
				mIsBindSimCkb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(isChecked) {
							mBindSimSetV.setDeson();	//������������
							mIsBindSimCkb.setChecked(true);
							TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
							String serialNum =  manager.getSimSerialNumber();	// ��ȡsim�������к�
							mConfig.putStringToXml(PhoneSafeConfig.BIND_SIM_SERIAL, serialNum);
							ToastUtil.show(context, "SIM�����к�Ϊ:" + serialNum);
						}else {
							mBindSimSetV.setDesoff();	// ������������
							mIsBindSimCkb.setChecked(false);	
							mConfig.putStringToXml(PhoneSafeConfig.BIND_SIM_SERIAL, "");
						}
					}
				});
	}

}
