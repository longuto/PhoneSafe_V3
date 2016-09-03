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
	private Context context;	// 上下文
	private PhoneSafeConfig mConfig;	// 系统参数配置
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advance_tools);
		context = this;
		mConfig = new PhoneSafeConfig(context, PhoneSafeConfig.CONFIG);
	}

	// 进入号码归属地查询页面 -- 点击事件
	public void queryNum(View v) {
		Intent addressAct = new Intent(context, AddressActivity.class);
		startActivity(addressAct);
	}
	
	// 弹出来电号码归属地吐司显示风格
	public void chooseDiaStyle(View v) {
		String[] styles = {"苹果绿", "金属灰", "活力橙", "天空蓝"};
		// 获取配置文件中存储的位置,没有则返回0, 默认选择苹果绿
		int which = mConfig.getIntFromXml(PhoneSafeConfig.TOASTSTYLE);
		Dialog dialog = new AlertDialog.Builder(context)
		.setIcon(R.drawable.main_icon_notification)	// 设置图标
		.setTitle("号码归属地风格选择")
		// 设置单选条目事件
		.setSingleChoiceItems(styles, which, new DialogInterface.OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();	// 消失对话框
				// 存储你选择的位置
				mConfig.putIntToXml(PhoneSafeConfig.TOASTSTYLE, which);
			}
		})
		// 设置取消事件
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		})
		.setCancelable(false)
		.show();
	}
	
	// 进入小火箭Activity
	public void enterRockActivity(View v) {
		Intent rocketAct = new Intent(context, RocketActivity.class);
		startActivity(rocketAct);
	}
	
	// 将短信备份至Xml中
	public void backupSms(View v) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(getFilesDir(), "smsDate.xml"));
			SmsEngine.backupSms(context, fos);
			ToastUtil.show(context, "短信备份成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
