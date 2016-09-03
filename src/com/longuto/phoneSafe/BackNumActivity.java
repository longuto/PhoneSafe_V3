package com.longuto.phoneSafe;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.longuto.phoneSafe.adapter.BackNumAdapter;
import com.longuto.phoneSafe.db.dao.BackNumDb;
import com.longuto.phoneSafe.entity.BackNumInfo;
import com.longuto.phoneSafe.util.ToastUtil;

public class BackNumActivity extends Activity {
	private static final String TAG = "BackNumActivity";

	private Context context;	// 上下文
	private List<BackNumInfo> mData;		// 获取所有黑名单数据集合
	private BackNumAdapter mAdapter;	// 适配器
	private int mLimit = 20;	// 限制大小
	private int mOffset = 0;	// 偏移量
	
	@ViewInject(R.id.backNum_lstV)
	private ListView mBackNumLstv;	// 黑名单ListView
	
	// 更新集合数据
	public void setmData(List<BackNumInfo> data) {
		mData = data;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_back_num);
		ViewUtils.inject(this);
		context = this;
		
		// 初始化时，装配ListView
		mData = limitData(mLimit, mOffset);	// 初始装配数据
		if(mData == null) {
			mData = new ArrayList<BackNumInfo>();
		}
		mAdapter = new BackNumAdapter(context, mData);
		mBackNumLstv.setAdapter(mAdapter);
		
		/**
		 * 设置ListView滑动监听
		 * SCROLL_STATE_FLING: 快速滑动，离开手指的缓冲
		 * SCROLL_STATE_IDLE： 停止滑动
		 * SCROLL_STATE_TOUCH_SCROLL： 触摸滑动
		 */
		mBackNumLstv.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// 假如滑动停止
				if(OnScrollListener.SCROLL_STATE_IDLE == scrollState) {
					int lastLocation = mBackNumLstv.getLastVisiblePosition();	// 获取最后一位可见的item选项
					int size = mData.size();
					if(lastLocation == size-1) {
						mOffset += mLimit;
						List<BackNumInfo> data = limitData(mLimit, mOffset);	// 再次查询数据
						if(data != null) {
							mAdapter.addListData(data);
							mAdapter.notifyDataSetChanged();	// 通知适配器，数据已经改变													
						}
					}
				}	
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				
			}
		});
		
	}
	
	/**
	 * 装配数据
	 */
	private List<BackNumInfo> limitData(int limit, int offset) {
		return new BackNumDb(context).getDataByLimit(limit, offset);			
	}

	/**
	 * 创建菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.back_num, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * 菜单选择监听
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
		case R.id.backNum_add:
			final View view = View.inflate(context, R.layout.backnum_add_dialog, null);	// 添加弹窗视图
			final Dialog dialog = new AlertDialog.Builder(context)
			.setView(view)
			.create();	// 弹出添加对话框
			// 确定按钮点击事件
			view.findViewById(R.id.backNumAdd_btn).setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					EditText backNumEdt = (EditText) view.findViewById(R.id.backNum_edt);	// 黑名单号码编辑框
					String backNum = backNumEdt.getText().toString().trim();	// 黑名单号码
					if(!TextUtils.isEmpty(backNum)) {
						BackNumInfo temp = new BackNumInfo();	// 定义一个黑名单实体类
						temp.setNum(backNum);	// 设置实体类的号码
						RadioGroup group = (RadioGroup) view.findViewById(R.id.backNumMode_rgp);	// 选择组
						switch (group.getCheckedRadioButtonId()) {
						case R.id.all_rb:	// 全部
							temp.setMode(BackNumDb.ALL_MODE);
							break;
						case R.id.sms_rb:	// 短信
							temp.setMode(BackNumDb.SMS_MODE);
							break;
						case R.id.call_rb:	// 电话
							temp.setMode(BackNumDb.CALL_MODE);
							break;
						default:
							break;
						}
						new BackNumDb(context).addBackNum(temp.getNum(), temp.getMode());	// 数据库执行增加操作
						mAdapter.addBackNum(temp);	// 向适配器数据里增加一条数据
						mAdapter.notifyDataSetChanged();	// 通知数据库信息已经改变
						dialog.dismiss();	// 消失对话框
					}else {
						ToastUtil.show(context, "黑名单号码不能为空");
					}
				} 
			});
			// 取消按钮点击事件
			view.findViewById(R.id.backNumCancel_btn).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();	// 消失对话框
				}
			});
			dialog.show();	// 显示对话框
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
