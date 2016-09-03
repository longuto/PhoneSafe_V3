package com.longuto.phoneSafe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.longuto.phoneSafe.service.AddressStateService;
import com.longuto.phoneSafe.service.BackNumService;
import com.longuto.phoneSafe.service.WatchDogService;
import com.longuto.phoneSafe.util.PhoneSafeConfig;
import com.longuto.phoneSafe.util.ServiceStateUtil;
import com.longuto.phoneSafe.view.SettingView;

public class SettingActivity extends Activity {
	private SettingView mAutoUpdateSetv;	// 自定义布局控件
	private CheckBox mAutoUpdateCkb;	// 自动更新的复选按钮
	
	private SettingView mIsOpenLocationSetv;	// 手机号码归属地开启自定义布局
	private CheckBox mIsOpenLocationCkb;	// 手机号码归属地提醒复选按钮
	
	private SettingView mIsOpenBackNumSetv;	// 黑名单拦击模式自定义布局
	private CheckBox mIsOpenBackNumCkb;	// 黑名单拦击开启复选按钮
	
	private SettingView mIsOpenWatchDogSetv;	// 看门狗功能开启自定义布局
	private CheckBox mIsOpenWatchDogCkb;	// 看门狗功能开启复选按钮
	
	private Context context;	// 上下文
	private PhoneSafeConfig config;	// 配置文件设置
	private Intent mWatchDogService;	// 看门狗服务
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		context = this;
		config = new PhoneSafeConfig(context, PhoneSafeConfig.CONFIG);
		
		initView();
	}

	private void initView() {
		/**
		 * 1.自动更新软件SettingView初始化设置 
		 */
		mAutoUpdateSetv = (SettingView) findViewById(R.id.updateSet_SetV);	// 自定义布局控件
		mAutoUpdateCkb = (CheckBox) mAutoUpdateSetv.findViewById(R.id.isUpdate_ckb);
		
		if(config.getBooleanFromXml(PhoneSafeConfig.AUTO_UPDATE, true)) {
			mAutoUpdateCkb.setChecked(true);
			mAutoUpdateSetv.setDeson();
		}else {
			mAutoUpdateCkb.setChecked(false);
			mAutoUpdateSetv.setDesoff();
		}
		// 设置复选按钮选择事件 -- 更改数据配置
		mAutoUpdateCkb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					config.putBooleanToXml(PhoneSafeConfig.AUTO_UPDATE, true);
					mAutoUpdateSetv.setDeson();
				}else {
					config.putBooleanToXml(PhoneSafeConfig.AUTO_UPDATE, false);
					mAutoUpdateSetv.setDesoff();
				}
			}
		});
		
		/**
		 * 2.是否开启来电号码归属地提醒SettingView
		 */
		mIsOpenLocationSetv = (SettingView) findViewById(R.id.numLocation_setv);
		mIsOpenLocationCkb = (CheckBox) mIsOpenLocationSetv.findViewById(R.id.isUpdate_ckb);
		// 设置CheckBox状态改变监听
		final Intent adressService = new Intent(context, AddressStateService.class);
		mIsOpenLocationCkb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					mIsOpenLocationSetv.setDeson();
					startService(adressService);
				}else {
					mIsOpenLocationSetv.setDesoff();
					stopService(adressService);
				}
			}
		});
		
		/**
		 * 3.是否开启黑名单拦击模式
		 */
		mIsOpenBackNumSetv = (SettingView) findViewById(R.id.isOPenBackNum_setv);	// 是否开启黑名单拦击模式自定义布局
		mIsOpenBackNumCkb = (CheckBox) mIsOpenBackNumSetv.findViewById(R.id.isUpdate_ckb);	// 黑名单模式开启复选按钮
		final Intent backNumService = new Intent(context, BackNumService.class);	// 黑名单服务
		// 设置复选按钮监听事件
		mIsOpenBackNumCkb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					startService(backNumService);	// 开启服务
					mIsOpenBackNumSetv.setDeson();
				}else {
					stopService(backNumService);	// 关闭服务
					mIsOpenBackNumSetv.setDesoff();
				}
			}
		});
		
		/**
		 * 4. 看门狗功能模式开启
		 */
		mIsOpenWatchDogSetv = (SettingView) findViewById(R.id.isOpenWatchDog_setv);	// 是否开启看门狗功能自定义布局
		mIsOpenWatchDogCkb = (CheckBox) mIsOpenWatchDogSetv.findViewById(R.id.isUpdate_ckb);	// 是否开启看门狗功能复选按钮
		mWatchDogService = new Intent(context, WatchDogService.class);	// 看门狗服务意图
		// 设置复选按钮选择事件
		mIsOpenWatchDogCkb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {	
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					config.putBooleanToXml(PhoneSafeConfig.IS_OPEN_WATCHDOG, true);	// 写入XML
					startService(mWatchDogService);	// 开启服务
					mIsOpenWatchDogSetv.setDeson();
				}else {		
					config.putBooleanToXml(PhoneSafeConfig.IS_OPEN_WATCHDOG, false);	// 写入XML
					stopService(mWatchDogService);	// 关闭服务
					mIsOpenWatchDogSetv.setDesoff();	// 更改控件状态
				}
			}
		});
	}
	
	@Override
	protected void onStart() {
		// 判断来电归属服务是否开启
		boolean adressRunning = ServiceStateUtil.isServiceRunning(context, "com.longuto.phoneSafe.service.AddressStateService");
		if(adressRunning) {
			mIsOpenLocationSetv.setDeson();
			mIsOpenLocationCkb.setChecked(true);
		}else {
			mIsOpenLocationSetv.setDesoff();
			mIsOpenLocationCkb.setChecked(false);
		}
		
		// 判断黑名单服务是否开启
		boolean backNumRunning = ServiceStateUtil.isServiceRunning(context, "com.longuto.phoneSafe.service.BackNumService");
		if(backNumRunning) {
			mIsOpenBackNumSetv.setDeson();	
			mIsOpenBackNumCkb.setChecked(true);
		}else {
			mIsOpenBackNumSetv.setDesoff();	
			mIsOpenBackNumCkb.setChecked(false);	
		}
		
		// 判断看门狗服务是否开启
		if(config.getBooleanFromXml(PhoneSafeConfig.IS_OPEN_WATCHDOG, false)) {
			startService(mWatchDogService);
			mIsOpenWatchDogCkb.setChecked(true);
			mIsOpenBackNumSetv.setDeson();
		}else {
			stopService(mWatchDogService);
			mIsOpenWatchDogCkb.setChecked(false);
			mIsOpenWatchDogSetv.setDesoff();
		}
		super.onStart();		
	}
}
