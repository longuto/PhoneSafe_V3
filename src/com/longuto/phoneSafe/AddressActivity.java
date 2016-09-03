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
	private Context context;	// ������
	
	@ViewInject(R.id.numLocation_edt)
	private EditText mNumLocationEdt;	// ������ı��༭
	@ViewInject(R.id.numLocation_tv)
	private TextView mNumLocationTv;	// ��������ʾ��
	
	@OnClick(R.id.queryNum_btn)
	public void queryNum(View v) {
		String num = mNumLocationEdt.getText().toString().trim();	// �༭����ı�
		num = num.replace("-", "");	// �滻�������е�-
		if(!TextUtils.isEmpty(num)) {
			String Location = AddressManager.queryLocation(num, context);
			if(TextUtils.isEmpty(Location)) {
				mNumLocationTv.setText(Location);							
			}
		}else {
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			mNumLocationEdt.startAnimation(shake);			
		}
		
		Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);	// �𶯷���
		vibrator.vibrate(2000);
		vibrator.vibrate(new long[]{500,300,100,200}, -1);
		vibrator.cancel();	// ȡ����	
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		ViewUtils.inject(this);
		context = this;

		// ��������� -- �����ı��ı����
		mNumLocationEdt.addTextChangedListener(new TextWatcher() {	
			// ���ı��ı��ʱ��
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String num = s.toString();	// �༭����ı�
				num = num.replace("-", "");	// �滻�������е�-
				String Location = AddressManager.queryLocation(num, context);
				mNumLocationTv.setText(Location);
			}
			// ���ı��ı�֮ǰ,����û��ʱ
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			// ���ı��ı�֮��,�ı��ı��һ��ʱ��,�ٴθı�Ŀ�ʼ
			@Override
			public void afterTextChanged(Editable s) {	
			
			}
		});
	}
	
}
