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
	private SettingView mAutoUpdateSetv;	// �Զ��岼�ֿؼ�
	private CheckBox mAutoUpdateCkb;	// �Զ����µĸ�ѡ��ť
	
	private SettingView mIsOpenLocationSetv;	// �ֻ���������ؿ����Զ��岼��
	private CheckBox mIsOpenLocationCkb;	// �ֻ�������������Ѹ�ѡ��ť
	
	private SettingView mIsOpenBackNumSetv;	// ����������ģʽ�Զ��岼��
	private CheckBox mIsOpenBackNumCkb;	// ����������������ѡ��ť
	
	private SettingView mIsOpenWatchDogSetv;	// ���Ź����ܿ����Զ��岼��
	private CheckBox mIsOpenWatchDogCkb;	// ���Ź����ܿ�����ѡ��ť
	
	private Context context;	// ������
	private PhoneSafeConfig config;	// �����ļ�����
	private Intent mWatchDogService;	// ���Ź�����
	
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
		 * 1.�Զ��������SettingView��ʼ������ 
		 */
		mAutoUpdateSetv = (SettingView) findViewById(R.id.updateSet_SetV);	// �Զ��岼�ֿؼ�
		mAutoUpdateCkb = (CheckBox) mAutoUpdateSetv.findViewById(R.id.isUpdate_ckb);
		
		if(config.getBooleanFromXml(PhoneSafeConfig.AUTO_UPDATE, true)) {
			mAutoUpdateCkb.setChecked(true);
			mAutoUpdateSetv.setDeson();
		}else {
			mAutoUpdateCkb.setChecked(false);
			mAutoUpdateSetv.setDesoff();
		}
		// ���ø�ѡ��ťѡ���¼� -- ������������
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
		 * 2.�Ƿ�������������������SettingView
		 */
		mIsOpenLocationSetv = (SettingView) findViewById(R.id.numLocation_setv);
		mIsOpenLocationCkb = (CheckBox) mIsOpenLocationSetv.findViewById(R.id.isUpdate_ckb);
		// ����CheckBox״̬�ı����
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
		 * 3.�Ƿ�������������ģʽ
		 */
		mIsOpenBackNumSetv = (SettingView) findViewById(R.id.isOPenBackNum_setv);	// �Ƿ�������������ģʽ�Զ��岼��
		mIsOpenBackNumCkb = (CheckBox) mIsOpenBackNumSetv.findViewById(R.id.isUpdate_ckb);	// ������ģʽ������ѡ��ť
		final Intent backNumService = new Intent(context, BackNumService.class);	// ����������
		// ���ø�ѡ��ť�����¼�
		mIsOpenBackNumCkb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					startService(backNumService);	// ��������
					mIsOpenBackNumSetv.setDeson();
				}else {
					stopService(backNumService);	// �رշ���
					mIsOpenBackNumSetv.setDesoff();
				}
			}
		});
		
		/**
		 * 4. ���Ź�����ģʽ����
		 */
		mIsOpenWatchDogSetv = (SettingView) findViewById(R.id.isOpenWatchDog_setv);	// �Ƿ������Ź������Զ��岼��
		mIsOpenWatchDogCkb = (CheckBox) mIsOpenWatchDogSetv.findViewById(R.id.isUpdate_ckb);	// �Ƿ������Ź����ܸ�ѡ��ť
		mWatchDogService = new Intent(context, WatchDogService.class);	// ���Ź�������ͼ
		// ���ø�ѡ��ťѡ���¼�
		mIsOpenWatchDogCkb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {	
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					config.putBooleanToXml(PhoneSafeConfig.IS_OPEN_WATCHDOG, true);	// д��XML
					startService(mWatchDogService);	// ��������
					mIsOpenWatchDogSetv.setDeson();
				}else {		
					config.putBooleanToXml(PhoneSafeConfig.IS_OPEN_WATCHDOG, false);	// д��XML
					stopService(mWatchDogService);	// �رշ���
					mIsOpenWatchDogSetv.setDesoff();	// ���Ŀؼ�״̬
				}
			}
		});
	}
	
	@Override
	protected void onStart() {
		// �ж�������������Ƿ���
		boolean adressRunning = ServiceStateUtil.isServiceRunning(context, "com.longuto.phoneSafe.service.AddressStateService");
		if(adressRunning) {
			mIsOpenLocationSetv.setDeson();
			mIsOpenLocationCkb.setChecked(true);
		}else {
			mIsOpenLocationSetv.setDesoff();
			mIsOpenLocationCkb.setChecked(false);
		}
		
		// �жϺ����������Ƿ���
		boolean backNumRunning = ServiceStateUtil.isServiceRunning(context, "com.longuto.phoneSafe.service.BackNumService");
		if(backNumRunning) {
			mIsOpenBackNumSetv.setDeson();	
			mIsOpenBackNumCkb.setChecked(true);
		}else {
			mIsOpenBackNumSetv.setDesoff();	
			mIsOpenBackNumCkb.setChecked(false);	
		}
		
		// �жϿ��Ź������Ƿ���
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
