package com.longuto.phoneSafe;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

/**
 * �����򵼵Ļ���Activity
 * 1. ���ñ���
 * 2. ������һ����ť����¼�
 * 3. ������һ����ť����¼� 
 * @author longuto
 */
public abstract class SetupBaseActivity extends Activity {
	private TextView mTitleBarTv;	// �������ı�չʾ�ؼ�
	
	/**
	 * ���ñ���
	 */
	public void setTitle(String title){
		mTitleBarTv = (TextView) findViewById(R.id.onlyTitle_bar_tv);
		mTitleBarTv.setText(title);
	}
	
	/**
	 * ��һ�� -- ����¼�
	 */
	public void pre(View v) {
		executePre();
	}
	public abstract void executePre();
	
	/**
	 * ��һ�� -- ����¼� 
	 */
	public void next(View v) {
		executeNext();
	}
	public abstract void executeNext();
	
	/**
	 * ���˼� -- �����¼�
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// ���ؼ�
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			executePre();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
