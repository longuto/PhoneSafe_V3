package com.longuto.phoneSafe.setupPage;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.longuto.phoneSafe.R;
import com.longuto.phoneSafe.fragment.ProtectedFragment;
import com.longuto.phoneSafe.util.PhoneSafeConfig;
import com.longuto.phoneSafe.view.SettingView;

public class Setup4Page extends BasePage {
	@ViewInject(R.id.myset_setv)
	private SettingView mIsOpenSafeLostSetv;	// �Ƿ����������Զ��岼��
	@ViewInject(R.id.isUpdate_ckb)
	private CheckBox mIsOpenSafeLostCkb;		// �Ƿ���������ѡ��ť�ؼ�
	
	@OnClick(R.id.next_btn)
	public void notFirstSetup(View v) {
		// ���������ļ���: �Ƿ�Ϊ��һ��������Ϊfalse
		mConfig.putBooleanToXml(PhoneSafeConfig.IS_FIRST_SETUP, false);
		
		// ��ת֡Ƭ��
		ProtectedFragment protectedFragment = new ProtectedFragment();
		((Activity)context).getFragmentManager().beginTransaction().replace(R.id.content_fly, protectedFragment).commit();
	}

	public Setup4Page(Context context) {
		super(context);
	}

	@Override
	public View initView() {
		rootView = View.inflate(context, R.layout.layout_setup4, null);
		return rootView;	
	}

	@Override
	public void initData() {
		// �����˷�������
		if(mConfig.getBooleanFromXml(PhoneSafeConfig.IS_OPEN_SAFELOST, false)) {
			mIsOpenSafeLostCkb.setChecked(true);
			mIsOpenSafeLostSetv.setDeson();
		}else {
			mIsOpenSafeLostCkb.setChecked(false);
			mIsOpenSafeLostSetv.setDesoff();			
		}
		// ���÷���ѡ��ť��ѡ���¼�
		mIsOpenSafeLostCkb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					mIsOpenSafeLostCkb.setChecked(true);
					mIsOpenSafeLostSetv.setDeson();
					mConfig.putBooleanToXml(PhoneSafeConfig.IS_OPEN_SAFELOST, true);
				}else {
					mIsOpenSafeLostCkb.setChecked(false);
					mIsOpenSafeLostSetv.setDesoff();
					mConfig.putBooleanToXml(PhoneSafeConfig.IS_OPEN_SAFELOST, false);
				}
			}
		});
	}

}
