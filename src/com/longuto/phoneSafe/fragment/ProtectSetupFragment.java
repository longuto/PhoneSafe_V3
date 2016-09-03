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
	private View mRootView;	// ������
	private Context context;	// ������
	private int preDotPosition;	// ��һ�����λ��
	private String[] titles = {"1 ��ӭʹ���ֻ�����", "2 �ֻ�����", "3 ���ð�ȫ����", "4 �������"};
	
	@ViewInject(R.id.functionPage_Vp)
	private ViewPager mFunctionPageVp;	// ViewPage����
	@ViewInject(R.id.onlyTitle_bar_tv)
	private TextView mTitleTv;		// ����ؼ�
	@ViewInject(R.id.dotState_lly)
	private LinearLayout mDotLly;		// �㲼��
	
	private List<BasePage> mPageList;	// ҳ�漯��
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		context = getActivity();
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// ��ʼ��Fragment��ͼ
		mRootView = inflater.inflate(R.layout.layout_protectsetup_fragment, container, false);
		ViewUtils.inject(this, mRootView);
		// ��ʼ��ViewPage����
		initPager();
		mTitleTv.setText(titles[0]);	// ��ʼ������
		preDotPosition = 0;	// ��ʼ����ͼʱ,��һ�����λ��
		// ����Viewpager������
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
		// ViewPagerҳ��ѡ���¼�
		mFunctionPageVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {	
			@Override
			public void onPageSelected(int position) {
				// ���ñ���
				mTitleTv.setText(titles[position]);
				// ����ÿ��Ƭ��data�¼�
				mPageList.get(position).initData();
				// ���õ�״̬�仯����
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
	 * ��ʼ��ViewPage����
	 */
	private void initPager() {
		mPageList = new ArrayList<BasePage>();	
		// ��ʼ������
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
