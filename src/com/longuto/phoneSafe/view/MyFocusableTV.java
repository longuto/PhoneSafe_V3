package com.longuto.phoneSafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

/**
 * �Զ���ؼ��̳�TextView
 * @author longuto
 *
 */
public class MyFocusableTV extends TextView {
	// Ĭ�����������
	public MyFocusableTV(Context context) {
		super(context);
	}
	// �Զ���
	public MyFocusableTV(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	// Ĭ������
	public MyFocusableTV(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * �Ƿ���Ի�ȡ����,Ĭ��false,�ĳ�true
	 */
	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		return true;
	}
}
