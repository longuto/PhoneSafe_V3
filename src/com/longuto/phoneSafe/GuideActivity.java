package com.longuto.phoneSafe;

import java.util.ArrayList;
import java.util.List;

import com.longuto.phoneSafe.util.PhoneSafeConfig;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;

public class GuideActivity extends Activity {
	private ViewPager mGuideImageVwp;	// 滑动视图控件
	private Button mEnterBtn;	// 进入按钮
	private Context context;	// 上下文
	private List<ImageView> mImageList;	// 图片集合
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);	//没标题
		setContentView(R.layout.activity_guide);
		// step 1: 初始化参数
		mGuideImageVwp = (ViewPager) findViewById(R.id.guideImage_vwp);
		mEnterBtn = (Button) findViewById(R.id.enter_btn);
		context = this;

		// step 2: 初始化视图
		initView();
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		// 1.初始化参数
		mImageList = new ArrayList<ImageView>();
		ImageView guideIv1 = new ImageView(context);
		guideIv1.setBackgroundResource(R.drawable.guide_1);
		mImageList.add(guideIv1);
		ImageView guideIv2 = new ImageView(context);
		guideIv2.setBackgroundResource(R.drawable.guide_2);
		mImageList.add(guideIv2);
		ImageView guideIv3 = new ImageView(context);
		guideIv3.setBackgroundResource(R.drawable.guide_3);
		mImageList.add(guideIv3);
		
		// 2.装配适配器
		mGuideImageVwp.setAdapter(new PagerAdapter() {	
			@Override
			public boolean isViewFromObject(View view, Object object) {			
				return view==object;
			}
			@Override
			public int getCount() {
				return mImageList == null ? 0 : mImageList.size();
			}
			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				container.removeView(mImageList.get(position));
			}
			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				container.addView(mImageList.get(position));
				return mImageList.get(position);
			}
		});
		
		// 3.设置ViewPage的监听
		mGuideImageVwp.setOnPageChangeListener(new OnPageChangeListener() {		
			// 页面被选择中监听
			@Override
			public void onPageSelected(int position) {
				// 当选择中了最后一个页面,设置进入按钮显示
				if(position == (mImageList.size() - 1)) {
					mEnterBtn.setVisibility(View.VISIBLE);
				}else {
					mEnterBtn.setVisibility(View.GONE);
				}
			}	
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}
	
	/**
	 * 设置进入按钮点击事件
	 * 进入SplashActivity页面
	 */
	public void enterSplash(View v) {
		// 将Xml配置中的is_first_used改为false
		PhoneSafeConfig config = new PhoneSafeConfig(context, PhoneSafeConfig.CONFIG);
		config.putBooleanToXml(PhoneSafeConfig.IS_FIRST_USED, false);
		
		// 进入SplashActivity界面
		Intent intent = new Intent(context, SplashActivity.class);
		startActivity(intent);
		this.finish();
	}
}
