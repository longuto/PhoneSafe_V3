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
	private GridView mFunctionGv;	// 网格布局
	private Context context;
	private PhoneSafeConfig config;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		context = this;
		config = new PhoneSafeConfig(context, PhoneSafeConfig.CONFIG);
		
		// 初始化布局
		init();
	}
	
	/*
	 *  初始化界面布局
	 */
	private void init() {
		mFunctionGv = (GridView) findViewById(R.id.fuction_gv);	// 获取GridView控件
		context = this;
		config = new PhoneSafeConfig(context, PhoneSafeConfig.CONFIG);
		
		mFunctionGv.setAdapter(new HomeAdapter(context));	// 为GridView控件设置适配器
		// 设置适配器监听器
		mFunctionGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:	//手机防盗
					if(config.getStringFromXml(PhoneSafeConfig.SAFE_PASSWORD) == null) {
						showInitPwdDiaLog();						
					}else {
						showTestPwdDialog();
					}
					break;
				case 1:	//通信卫士
					Intent backNumAct = new Intent(context, BackNumActivity.class);
					startActivity(backNumAct);
					break;
				case 2:	//软件管理
					Intent softWareAct = new Intent(context, SoftWareActivity.class);
					startActivity(softWareAct);
					break;
				case 3:	//进程管理
					Intent progressIntent = new Intent(context, ProgressActivity.class);
					startActivity(progressIntent);
					break;
				case 4:	//流量统计
					break;
				case 5:	//手机杀毒
					break;
				case 6:	//缓存清理
					break;
				case 7:	//高级工具
					Intent advanceAct = new Intent(context, AdvanceToolsActivity.class);
					startActivity(advanceAct);
					break;
				case 8:	//设置中心
					Intent settingAct = new Intent(context, SettingActivity.class);
					startActivity(settingAct);
					// 后面不需要结束当前进程, 此Activity设置了SingleTask模式
					break;
				default:
					break;
				}
			}
		});
	}

	/**
	 * 手机防盗 -- 验证密码对话框
	 */
	protected void showTestPwdDialog() {
		View view = View.inflate(context, R.layout.test_pwd_dialog, null);	// 验证密码对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(view);
		final AlertDialog dialog = builder.create();
		
		final EditText testPwdEdt = (EditText) view.findViewById(R.id.test_pwd_edt);	// 验证密码输入控件		
		Button okBtn = (Button) view.findViewById(R.id.ok_btn); 	// 确定按钮
		Button cancleBtn = (Button) view.findViewById(R.id.cancle_btn);		// 取消按钮
		CheckBox changedCkb = (CheckBox) view.findViewById(R.id.changeType_ckb);	// checkBox选择控件
		// 取消按钮点击事件
		cancleBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();	
			}
		});
		// 确定按钮点击事件
		okBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String testPwd = testPwdEdt.getText().toString().trim();	// 获取输入的密码
				if(TextUtils.isEmpty(testPwd)) {
					ToastUtil.show(context, "验证密码输入不能为空!");
					return;
				}
				// 验证密码成功
				if(!MD5.getMD5(testPwd).equals(config.getStringFromXml(PhoneSafeConfig.SAFE_PASSWORD))) {
					ToastUtil.show(context, "密码输入错误!");
					return;
				}
				dialog.dismiss();
				// 进入到LostFindActivity
				Intent intent = new Intent(context, LostFindActivity.class);
				startActivity(intent);
			}
		});
		changedCkb.setChecked(true);	// 初始化,与密码输入文本保持一致
		// CheckBox按钮选择事件
		changedCkb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {	
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				int testPwdLen = testPwdEdt.getText().toString().trim().length();	// 输入字符串的长度
				if(isChecked) {
					testPwdEdt.setInputType(129);	// 密文
					testPwdEdt.setSelection(testPwdLen);	// 设置光标
				}else {
					testPwdEdt.setInputType(EditorInfo.TYPE_CLASS_TEXT);	//明文
					testPwdEdt.setSelection(testPwdLen);
				}
			}
		});
		dialog.show(); 
	}

	/**
	 * 手机防盗 -- 输入初始密码对话框
	 */
	protected void showInitPwdDiaLog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);	// 对话框构造器
		final View view = View.inflate(context, R.layout.init_pwd_dialog, null);	// 对话框视图
		builder.setView(view);	//设置对话框的视图
		final AlertDialog dialog = builder.create();	// 通过构造器构建对话框
		
		final EditText pwdEdt = (EditText) view.findViewById(R.id.pwd_et);	// 密码编辑控件
		// 获取视图的按钮,并设置点击事件
		Button okBtn = (Button) view.findViewById(R.id.ok_btn);	// 设置按钮
		Button cancleBtn = (Button) view.findViewById(R.id.cancle_btn);	// 取消按钮
		// 取消按钮点击事件
		cancleBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		// 确定按钮点击事件
		okBtn.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				EditText confirmPwEdt = (EditText) view.findViewById(R.id.confirm_pwd);	// 确认密码编辑控件
				String pwd = pwdEdt.getText().toString().trim();
				String confirmPwd = confirmPwEdt.getText().toString().trim();
				if(TextUtils.isEmpty(pwd) || TextUtils.isEmpty(confirmPwd)) {
					ToastUtil.show(context, "密码不能为空!");
					return;
				}
				if(!pwd.equals(confirmPwd)) {
					ToastUtil.show(context, "设置密码要一致!");
					return;
				}
				if(pwd.equals(confirmPwd)) {
					config.putStringToXml(PhoneSafeConfig.SAFE_PASSWORD, MD5.getMD5(pwd));
					ToastUtil.show(context, "密码设置成功!");
					dialog.dismiss();
				}
			}
		});
		dialog.show();
	}

	@Override
	protected void onStart() {
		// 看门狗服务的初始化，以及定期检测是否和Xml中的配置一致
		Intent watchDogService = new Intent(context, WatchDogService.class);
		if(config.getBooleanFromXml(PhoneSafeConfig.IS_OPEN_WATCHDOG, false)) {
			startService(watchDogService);
		}else {
			stopService(watchDogService);
		}
		super.onStart();
	}	
}
