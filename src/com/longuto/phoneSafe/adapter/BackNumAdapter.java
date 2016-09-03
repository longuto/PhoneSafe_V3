package com.longuto.phoneSafe.adapter;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.longuto.phoneSafe.R;
import com.longuto.phoneSafe.db.dao.BackNumDb;
import com.longuto.phoneSafe.entity.BackNumInfo;

public class BackNumAdapter extends BaseAdapter {
	private Context context;
	private List<BackNumInfo> mData;

	public BackNumAdapter(Context context, List<BackNumInfo> data) {
		this.context = context;
		this.mData = data;
	}
	
	// 添加一组集合数据
	public void addListData(List<BackNumInfo> data) {
		mData.addAll(data);
	}
	
	// 增加一条数据
	public void addBackNum(BackNumInfo temp) {
		mData.add(temp);
	}
	
	@Override
	public int getCount() {
		return mData == null ? 0 : mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData == null ? null : mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final BackNumInfo temp = mData.get(position);
		Holder holder = null;
		if(convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.backnum_item, parent, false);
			holder = new Holder();
			holder.backDelIv = (ImageView) convertView.findViewById(R.id.backnumDel_iv);	// 编号
			holder.backNumTv = (TextView) convertView.findViewById(R.id.phoneNum_tv);	// 号码
			holder.backModeTv = (TextView) convertView.findViewById(R.id.mode_tv);		// 模式
			convertView.setTag(holder);
		}else {
			holder = (Holder) convertView.getTag();			
		}
		holder.backNumTv.setText(temp.getNum());	// 设置手机号
		switch (temp.getMode()) {					// 设置拦截模式
		case BackNumDb.ALL_MODE:
			holder.backModeTv.setText("全部拦截");
			break;
		case BackNumDb.CALL_MODE:
			holder.backModeTv.setText("电话拦截");
			break;
		case BackNumDb.SMS_MODE:
			holder.backModeTv.setText("短信拦截");			
			break;
		default:
			break;
		}
		// 设置删除图片监听事件
		holder.backDelIv.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				new BackNumDb(context).deleteBackNumMode(temp.getId());
				mData.remove(temp);	// 删除集合中的内容
				notifyDataSetChanged();
			}
		});
		// 设置模式点击更改
		holder.backModeTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showChangeModeDia(temp, position);
			}
		});
		return convertView;
	}
	
	/**
	 * 点击更改黑名单的拦击模式
	 * @param temp 需要展示的数据信息
	 */
	private void showChangeModeDia(final BackNumInfo temp, final int position) {
		View view = View.inflate(context, R.layout.backnum_change_dialog, null);	// 获取改变模式的对话框
		final Dialog dialog = new AlertDialog.Builder(context)	// 更改模式的对话框
		.setView(view)
		.create();
		TextView backNumTv = (TextView) view.findViewById(R.id.backNum_tv);	// 黑名单显示TextView
		final RadioGroup group = (RadioGroup) view.findViewById(R.id.backNumMode_rgp);	// 黑名单拦截模式单选组件
		backNumTv.setText(temp.getNum());	// 设置展示的黑名单号码
		int mode = temp.getMode();	// 获取黑名单拦击模式
		// 设置黑名单模式在单选组件上的显示
		switch (mode) {
		case BackNumDb.ALL_MODE:
			RadioButton allRb = (RadioButton) group.findViewById(R.id.all_rb);
			allRb.setChecked(true);
			break;
		case BackNumDb.SMS_MODE:
			RadioButton smsRb = (RadioButton) group.findViewById(R.id.sms_rb);
			smsRb.setChecked(true);
			break;
		case BackNumDb.CALL_MODE:
			RadioButton callRb = (RadioButton) group.findViewById(R.id.call_rb);
			callRb.setChecked(true);
			break;
		default:
			break;
		}
		Button sureBtn = (Button) view.findViewById(R.id.sure_btn);	// 确定按钮
		Button cancelBtn = (Button) view.findViewById(R.id.cancel_btn);	// 取消按钮
		// 确定按钮监听事件
		sureBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int id = group.getCheckedRadioButtonId();	// 点击确定时,获取单选框选中的子控件id
				switch (id) {
				case R.id.all_rb:
					changeDataMode(BackNumDb.ALL_MODE);
					break;
				case R.id.sms_rb:
					changeDataMode(BackNumDb.SMS_MODE);
					break;
				case R.id.call_rb:
					changeDataMode(BackNumDb.CALL_MODE);
					break;
				default:
					break;
				}
				dialog.dismiss();
			};
			/**
			 * 根据指定模式,更改黑名单的数据库和mData数据的模式
			 * @param mode
			 */
			private void changeDataMode(int mode) {
				new BackNumDb(context).updataBackNumMode(temp.getId(), mode);	// 执行数据库的更改
				temp.setMode(mode);	// 更改指定黑名单数据信息
				mData.set(position, temp);	// 更改指定位置的集合中的黑名单信息(这样不会影响当前黑名单出现在集合中的顺序)		
				notifyDataSetChanged();
			}
		});
		// 取消按钮监听事件
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();	//消失对话框	
			}
		});
		dialog.show();
	}

	/**
	 * 适配器展板的帮助类
	 */
	public class Holder {
		public ImageView backDelIv;		// 展示id
		public TextView backNumTv;		// 展示号码
		public TextView backModeTv;		// 展示模式
	}
}
