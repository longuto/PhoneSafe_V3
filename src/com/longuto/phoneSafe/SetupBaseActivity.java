package com.longuto.phoneSafe;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

/**
 * 设置向导的基本Activity
 * 1. 设置标题
 * 2. 设置上一步按钮点击事件
 * 3. 设置下一步按钮点击事件 
 * @author longuto
 */
public abstract class SetupBaseActivity extends Activity {
	private TextView mTitleBarTv;	// 标题栏文本展示控件
	
	/**
	 * 设置标题
	 */
	public void setTitle(String title){
		mTitleBarTv = (TextView) findViewById(R.id.onlyTitle_bar_tv);
		mTitleBarTv.setText(title);
	}
	
	/**
	 * 上一步 -- 点击事件
	 */
	public void pre(View v) {
		executePre();
	}
	public abstract void executePre();
	
	/**
	 * 上一步 -- 点击事件 
	 */
	public void next(View v) {
		executeNext();
	}
	public abstract void executeNext();
	
	/**
	 * 后退键 -- 按键事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 返回键
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			executePre();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
