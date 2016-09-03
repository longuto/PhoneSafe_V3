package com.longuto.phoneSafe;

import java.util.Currency;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.longuto.phoneSafe.db.AddressManager;

public class AddressActivity extends Activity {
	private Context context;	// 上下文
	
	@ViewInject(R.id.numLocation_edt)
	private EditText mNumLocationEdt;	// 号码的文本编辑
	@ViewInject(R.id.numLocation_tv)
	private TextView mNumLocationTv;	// 归属地显示框
	
	@OnClick(R.id.queryNum_btn)
	public void queryNum(View v) {
		String num = mNumLocationEdt.getText().toString().trim();	// 编辑框的文本
		num = num.replace("-", "");	// 替换掉号码中的-
		if(!TextUtils.isEmpty(num)) {
			String Location = AddressManager.queryLocation(num, context);
			if(TextUtils.isEmpty(Location)) {
				mNumLocationTv.setText(Location);							
			}
		}else {
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			mNumLocationEdt.startAnimation(shake);			
		}
		
		Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);	// 震动服务
		vibrator.vibrate(2000);
		vibrator.vibrate(new long[]{500,300,100,200}, -1);
		vibrator.cancel();	// 取消震动	
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		ViewUtils.inject(this);
		context = this;

		// 号码输入框 -- 增加文本改变监听
		mNumLocationEdt.addTextChangedListener(new TextWatcher() {	
			// 当文本改变的时候
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String num = s.toString();	// 编辑框的文本
				num = num.replace("-", "");	// 替换掉号码中的-
				String Location = AddressManager.queryLocation(num, context);
				mNumLocationTv.setText(Location);
			}
			// 在文本改变之前,就是没变时
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			// 在文本改变之后,文本改变后一段时间,再次改变的开始
			@Override
			public void afterTextChanged(Editable s) {	
			
			}
		});
	}
	
}
