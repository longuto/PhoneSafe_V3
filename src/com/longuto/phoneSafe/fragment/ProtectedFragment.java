package com.longuto.phoneSafe.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.longuto.phoneSafe.R;
import com.longuto.phoneSafe.util.PhoneSafeConfig;

public class ProtectedFragment extends Fragment {
	private PhoneSafeConfig mConfig;	// 配置文件
	private Context context;	// 上下文
	private View rootView;	// 根视图
	
	@ViewInject(R.id.safeNum_tv)
	private TextView mSafeNumTv;	// 安全号码文本显示控件
	@ViewInject(R.id.isOpenlost_iv)
	private ImageView mIsOpenLostIv;	// 是否打开防盗设置图片控件
	
	@OnClick(R.id.enterSetupAgain_btn)
	public void enterSetupAgain(View v) {
		ProtectSetupFragment protectSetupFragment = new ProtectSetupFragment();
		getFragmentManager().beginTransaction().replace(R.id.content_fly, protectSetupFragment).commit();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		context = getActivity();
		mConfig = new PhoneSafeConfig(context, PhoneSafeConfig.CONFIG);
		super.onCreate(savedInstanceState);
	}
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.layout_protected_fragment, container, false);
		ViewUtils.inject(this, rootView);
		// 设置安全号码控件
		String phoneSafeNum = mConfig.getStringFromXml(PhoneSafeConfig.SAFE_PHONENUM, "");
		if(!TextUtils.isEmpty(phoneSafeNum)) {
			mSafeNumTv.setText(phoneSafeNum);
		}else {
			mSafeNumTv.setText("未设置");
		}
		// 设置是否开启防盗功能图标
		if(mConfig.getBooleanFromXml(PhoneSafeConfig.IS_OPEN_SAFELOST, false)) {
			mIsOpenLostIv.setImageResource(R.drawable.lock);
		}else {
			mIsOpenLostIv.setImageResource(R.drawable.unlock);
		}
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
}
