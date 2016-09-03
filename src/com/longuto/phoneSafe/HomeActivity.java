package com.longuto.phoneSafe;

import com.longuto.phoneSafe.adapter.HomeAdapter;
import com.longuto.phoneSafe.service.WatchDogService;
import com.longuto.phoneSafe.util.MD5;
import com.longuto.phoneSafe.util.PhoneSafeConfig;
import com.longuto.phoneSafe.util.ToastUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;

public class HomeActivity extends Activity {
	private GridView mFunctionGv;	// ���񲼾�
	private Context context;
	private PhoneSafeConfig config;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		context = this;
		config = new PhoneSafeConfig(context, PhoneSafeConfig.CONFIG);
		
		// ��ʼ������
		init();
	}
	
	/*
	 *  ��ʼ�����沼��
	 */
	private void init() {
		mFunctionGv = (GridView) findViewById(R.id.fuction_gv);	// ��ȡGridView�ؼ�
		context = this;
		config = new PhoneSafeConfig(context, PhoneSafeConfig.CONFIG);
		
		mFunctionGv.setAdapter(new HomeAdapter(context));	// ΪGridView�ؼ�����������
		// ����������������
		mFunctionGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:	//�ֻ�����
					if(config.getStringFromXml(PhoneSafeConfig.SAFE_PASSWORD) == null) {
						showInitPwdDiaLog();						
					}else {
						showTestPwdDialog();
					}
					break;
				case 1:	//ͨ����ʿ
					Intent backNumAct = new Intent(context, BackNumActivity.class);
					startActivity(backNumAct);
					break;
				case 2:	//�������
					Intent softWareAct = new Intent(context, SoftWareActivity.class);
					startActivity(softWareAct);
					break;
				case 3:	//���̹���
					Intent progressIntent = new Intent(context, ProgressActivity.class);
					startActivity(progressIntent);
					break;
				case 4:	//����ͳ��
					break;
				case 5:	//�ֻ�ɱ��
					break;
				case 6:	//��������
					break;
				case 7:	//�߼�����
					Intent advanceAct = new Intent(context, AdvanceToolsActivity.class);
					startActivity(advanceAct);
					break;
				case 8:	//��������
					Intent settingAct = new Intent(context, SettingActivity.class);
					startActivity(settingAct);
					// ���治��Ҫ������ǰ����, ��Activity������SingleTaskģʽ
					break;
				default:
					break;
				}
			}
		});
	}

	/**
	 * �ֻ����� -- ��֤����Ի���
	 */
	protected void showTestPwdDialog() {
		View view = View.inflate(context, R.layout.test_pwd_dialog, null);	// ��֤����Ի���
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(view);
		final AlertDialog dialog = builder.create();
		
		final EditText testPwdEdt = (EditText) view.findViewById(R.id.test_pwd_edt);	// ��֤��������ؼ�		
		Button okBtn = (Button) view.findViewById(R.id.ok_btn); 	// ȷ����ť
		Button cancleBtn = (Button) view.findViewById(R.id.cancle_btn);		// ȡ����ť
		CheckBox changedCkb = (CheckBox) view.findViewById(R.id.changeType_ckb);	// checkBoxѡ��ؼ�
		// ȡ����ť����¼�
		cancleBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();	
			}
		});
		// ȷ����ť����¼�
		okBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String testPwd = testPwdEdt.getText().toString().trim();	// ��ȡ���������
				if(TextUtils.isEmpty(testPwd)) {
					ToastUtil.show(context, "��֤�������벻��Ϊ��!");
					return;
				}
				// ��֤����ɹ�
				if(!MD5.getMD5(testPwd).equals(config.getStringFromXml(PhoneSafeConfig.SAFE_PASSWORD))) {
					ToastUtil.show(context, "�����������!");
					return;
				}
				dialog.dismiss();
				// ���뵽LostFindActivity
				Intent intent = new Intent(context, LostFindActivity.class);
				startActivity(intent);
			}
		});
		changedCkb.setChecked(true);	// ��ʼ��,�����������ı�����һ��
		// CheckBox��ťѡ���¼�
		changedCkb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {	
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				int testPwdLen = testPwdEdt.getText().toString().trim().length();	// �����ַ����ĳ���
				if(isChecked) {
					testPwdEdt.setInputType(129);	// ����
					testPwdEdt.setSelection(testPwdLen);	// ���ù��
				}else {
					testPwdEdt.setInputType(EditorInfo.TYPE_CLASS_TEXT);	//����
					testPwdEdt.setSelection(testPwdLen);
				}
			}
		});
		dialog.show(); 
	}

	/**
	 * �ֻ����� -- �����ʼ����Ի���
	 */
	protected void showInitPwdDiaLog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);	// �Ի�������
		final View view = View.inflate(context, R.layout.init_pwd_dialog, null);	// �Ի�����ͼ
		builder.setView(view);	//���öԻ������ͼ
		final AlertDialog dialog = builder.create();	// ͨ�������������Ի���
		
		final EditText pwdEdt = (EditText) view.findViewById(R.id.pwd_et);	// ����༭�ؼ�
		// ��ȡ��ͼ�İ�ť,�����õ���¼�
		Button okBtn = (Button) view.findViewById(R.id.ok_btn);	// ���ð�ť
		Button cancleBtn = (Button) view.findViewById(R.id.cancle_btn);	// ȡ����ť
		// ȡ����ť����¼�
		cancleBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		// ȷ����ť����¼�
		okBtn.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				EditText confirmPwEdt = (EditText) view.findViewById(R.id.confirm_pwd);	// ȷ������༭�ؼ�
				String pwd = pwdEdt.getText().toString().trim();
				String confirmPwd = confirmPwEdt.getText().toString().trim();
				if(TextUtils.isEmpty(pwd) || TextUtils.isEmpty(confirmPwd)) {
					ToastUtil.show(context, "���벻��Ϊ��!");
					return;
				}
				if(!pwd.equals(confirmPwd)) {
					ToastUtil.show(context, "��������Ҫһ��!");
					return;
				}
				if(pwd.equals(confirmPwd)) {
					config.putStringToXml(PhoneSafeConfig.SAFE_PASSWORD, MD5.getMD5(pwd));
					ToastUtil.show(context, "�������óɹ�!");
					dialog.dismiss();
				}
			}
		});
		dialog.show();
	}

	@Override
	protected void onStart() {
		// ���Ź�����ĳ�ʼ�����Լ����ڼ���Ƿ��Xml�е�����һ��
		Intent watchDogService = new Intent(context, WatchDogService.class);
		if(config.getBooleanFromXml(PhoneSafeConfig.IS_OPEN_WATCHDOG, false)) {
			startService(watchDogService);
		}else {
			stopService(watchDogService);
		}
		super.onStart();
	}	
}
