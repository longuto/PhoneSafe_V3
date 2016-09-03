package com.longuto.phoneSafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

/**
 * 自定义控件继承TextView
 * @author longuto
 *
 */
public class MyFocusableTV extends TextView {
	// 默认主题和属性
	public MyFocusableTV(Context context) {
		super(context);
	}
	// 自定义
	public MyFocusableTV(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	// 默认主题
	public MyFocusableTV(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * 是否可以获取焦点,默认false,改成true
	 */
	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		return true;
	}
}
