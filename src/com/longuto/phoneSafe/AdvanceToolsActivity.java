package com.longuto.phoneSafe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.longuto.phoneSafe.business.SmsEngine;
import com.longuto.phoneSafe.util.PhoneSafeConfig;
import com.longuto.phoneSafe.util.ToastUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdvanceToolsActivity extends Activity {
	private Context context;	// ������
	private PhoneSafeConfig mConfig;	// ϵͳ��������
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advance_tools);
		context = this;
		mConfig = new PhoneSafeConfig(context, PhoneSafeConfig.CONFIG);
	}

	// �����������ز�ѯҳ�� -- ����¼�
	public void queryNum(View v) {
		Intent addressAct = new Intent(context, AddressActivity.class);
		startActivity(addressAct);
	}
	
	// ������������������˾��ʾ���
	public void chooseDiaStyle(View v) {
		String[] styles = {"ƻ����", "������", "������", "�����"};
		// ��ȡ�����ļ��д洢��λ��,û���򷵻�0, Ĭ��ѡ��ƻ����
		int which = mConfig.getIntFromXml(PhoneSafeConfig.TOASTSTYLE);
		Dialog dialog = new AlertDialog.Builder(context)
		.setIcon(R.drawable.main_icon_notification)	// ����ͼ��
		.setTitle("��������ط��ѡ��")
		// ���õ�ѡ��Ŀ�¼�
		.setSingleChoiceItems(styles, which, new DialogInterface.OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();	// ��ʧ�Ի���
				// �洢��ѡ���λ��
				mConfig.putIntToXml(PhoneSafeConfig.TOASTSTYLE, which);
			}
		})
		// ����ȡ���¼�
		.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		})
		.setCancelable(false)
		.show();
	}
	
	// ����С���Activity
	public void enterRockActivity(View v) {
		Intent rocketAct = new Intent(context, RocketActivity.class);
		startActivity(rocketAct);
	}
	
	// �����ű�����Xml��
	public void backupSms(View v) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(getFilesDir(), "smsDate.xml"));
			SmsEngine.backupSms(context, fos);
			ToastUtil.show(context, "���ű��ݳɹ�");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
