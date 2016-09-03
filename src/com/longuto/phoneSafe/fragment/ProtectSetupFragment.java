package com.longuto.phoneSafe.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.longuto.phoneSafe.R;
import com.longuto.phoneSafe.setupPage.BasePage;
import com.longuto.phoneSafe.setupPage.Setup1Page;
import com.longuto.phoneSafe.setupPage.Setup2Page;
import com.longuto.phoneSafe.setupPage.Setup3Page;
import com.longuto.phoneSafe.setupPage.Setup4Page;

public class ProtectSetupFragment extends Fragment {
	private View mRootView;	// 根布局
	private Context context;	// 上下文
	private int preDotPosition;	// 上一个点的位置
	private String[] titles = {"1 欢迎使用手机防盗", "2 手机卡绑定", "3 设置安全号码", "4 设置完成"};
	
	@ViewInject(R.id.functionPage_Vp)
	private ViewPager mFunctionPageVp;	// ViewPage布局
	@ViewInject(R.id.onlyTitle_bar_tv)
	private TextView mTitleTv;		// 标题控件
	@ViewInject(R.id.dotState_lly)
	private LinearLayout mDotLly;		// 点布局
	
	private List<BasePage> mPageList;	// 页面集合
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		context = getActivity();
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 初始化Fragment视图
		mRootView = inflater.inflate(R.layout.layout_protectsetup_fragment, container, false);
		ViewUtils.inject(this, mRootView);
		// 初始化ViewPage数据
		initPager();
		mTitleTv.setText(titles[0]);	// 初始化标题
		preDotPosition = 0;	// 初始化视图时,上一个点的位置
		// 设置Viewpager适配器
		mFunctionPageVp.setAdapter(new PagerAdapter() {
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			@Override
			public int getCount() {
				return mPageList.size();
			}
			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				container.addView(mPageList.get(position).getRootView());
				return mPageList.get(position).getRootView();
			}
			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				container.removeView((View)object);
			}
		});
		// ViewPager页面选择事件
		mFunctionPageVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {	
			@Override
			public void onPageSelected(int position) {
				// 设置标题
				mTitleTv.setText(titles[position]);
				// 设置每个片段data事件
				mPageList.get(position).initData();
				// 设置点状态变化属性
				mDotLly.getChildAt(preDotPosition).setEnabled(false);
				mDotLly.getChildAt(position).setEnabled(true);
				preDotPosition = position;
			}	
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
		return mRootView;
	}
	
	/**
	 * 初始化ViewPage数据
	 */
	private void initPager() {
		mPageList = new ArrayList<BasePage>();	
		// 初始化数据
		BasePage page1 = new Setup1Page(context);
		BasePage page2 = new Setup2Page(context);
		BasePage page3 = new Setup3Page(context);
		BasePage page4 = new Setup4Page(context);
		mPageList.add(page1);
		mPageList.add(page2);
		mPageList.add(page3);
		mPageList.add(page4);	
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {	
		super.onActivityCreated(savedInstanceState);
	}
}
