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
	private SettingView mBindSimSetV;	// 绑定sim卡的自定义布局
	@ViewInject(R.id.isUpdate_ckb)
	private CheckBox mIsBindSimCkb;	// 自定义布局中的CheckBox控件	

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
		// 如果绑定的sim序号不为空
				if(!TextUtils.isEmpty(mConfig.getStringFromXml(PhoneSafeConfig.BIND_SIM_SERIAL, ""))) {
					mBindSimSetV.setDeson();	//设置描述属性
					mIsBindSimCkb.setChecked(true);
				}else {
					mBindSimSetV.setDesoff();	// 设置描述属性
					mIsBindSimCkb.setChecked(false);
					mConfig.putStringToXml(PhoneSafeConfig.BIND_SIM_SERIAL, "");	// 这步是为了防止后面的检测SIM卡序列号的冲突
				}
				/*
				 * 设置sim绑定按钮CheckBox选择事件
				 */
				mIsBindSimCkb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(isChecked) {
							mBindSimSetV.setDeson();	//设置描述属性
							mIsBindSimCkb.setChecked(true);
							TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
							String serialNum =  manager.getSimSerialNumber();	// 获取sim卡的序列号
							mConfig.putStringToXml(PhoneSafeConfig.BIND_SIM_SERIAL, serialNum);
							ToastUtil.show(context, "SIM卡序列号为:" + serialNum);
						}else {
							mBindSimSetV.setDesoff();	// 设置描述属性
							mIsBindSimCkb.setChecked(false);	
							mConfig.putStringToXml(PhoneSafeConfig.BIND_SIM_SERIAL, "");
						}
					}
				});
	}

}
