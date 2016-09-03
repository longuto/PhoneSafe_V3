package com.longuto.phoneSafe.setupPage;

import android.content.Context;
import android.view.View;

import com.longuto.phoneSafe.R;

public class Setup1Page extends BasePage {
	
	public Setup1Page(Context context) {
		super(context);
	}

	@Override
	public View initView() {
		rootView = View.inflate(context, R.layout.layout_setup1, null);
		return rootView;	
	}

	@Override
	public void initData() {
		
	}

}
