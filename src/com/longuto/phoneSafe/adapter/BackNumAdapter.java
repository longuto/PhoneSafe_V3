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
	
	// ���һ�鼯������
	public void addListData(List<BackNumInfo> data) {
		mData.addAll(data);
	}
	
	// ����һ������
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
			holder.backDelIv = (ImageView) convertView.findViewById(R.id.backnumDel_iv);	// ���
			holder.backNumTv = (TextView) convertView.findViewById(R.id.phoneNum_tv);	// ����
			holder.backModeTv = (TextView) convertView.findViewById(R.id.mode_tv);		// ģʽ
			convertView.setTag(holder);
		}else {
			holder = (Holder) convertView.getTag();			
		}
		holder.backNumTv.setText(temp.getNum());	// �����ֻ���
		switch (temp.getMode()) {					// ��������ģʽ
		case BackNumDb.ALL_MODE:
			holder.backModeTv.setText("ȫ������");
			break;
		case BackNumDb.CALL_MODE:
			holder.backModeTv.setText("�绰����");
			break;
		case BackNumDb.SMS_MODE:
			holder.backModeTv.setText("��������");			
			break;
		default:
			break;
		}
		// ����ɾ��ͼƬ�����¼�
		holder.backDelIv.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				new BackNumDb(context).deleteBackNumMode(temp.getId());
				mData.remove(temp);	// ɾ�������е�����
				notifyDataSetChanged();
			}
		});
		// ����ģʽ�������
		holder.backModeTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showChangeModeDia(temp, position);
			}
		});
		return convertView;
	}
	
	/**
	 * ������ĺ�����������ģʽ
	 * @param temp ��Ҫչʾ��������Ϣ
	 */
	private void showChangeModeDia(final BackNumInfo temp, final int position) {
		View view = View.inflate(context, R.layout.backnum_change_dialog, null);	// ��ȡ�ı�ģʽ�ĶԻ���
		final Dialog dialog = new AlertDialog.Builder(context)	// ����ģʽ�ĶԻ���
		.setView(view)
		.create();
		TextView backNumTv = (TextView) view.findViewById(R.id.backNum_tv);	// ��������ʾTextView
		final RadioGroup group = (RadioGroup) view.findViewById(R.id.backNumMode_rgp);	// ����������ģʽ��ѡ���
		backNumTv.setText(temp.getNum());	// ����չʾ�ĺ���������
		int mode = temp.getMode();	// ��ȡ����������ģʽ
		// ���ú�����ģʽ�ڵ�ѡ����ϵ���ʾ
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
		Button sureBtn = (Button) view.findViewById(R.id.sure_btn);	// ȷ����ť
		Button cancelBtn = (Button) view.findViewById(R.id.cancel_btn);	// ȡ����ť
		// ȷ����ť�����¼�
		sureBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int id = group.getCheckedRadioButtonId();	// ���ȷ��ʱ,��ȡ��ѡ��ѡ�е��ӿؼ�id
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
			 * ����ָ��ģʽ,���ĺ����������ݿ��mData���ݵ�ģʽ
			 * @param mode
			 */
			private void changeDataMode(int mode) {
				new BackNumDb(context).updataBackNumMode(temp.getId(), mode);	// ִ�����ݿ�ĸ���
				temp.setMode(mode);	// ����ָ��������������Ϣ
				mData.set(position, temp);	// ����ָ��λ�õļ����еĺ�������Ϣ(��������Ӱ�쵱ǰ�����������ڼ����е�˳��)		
				notifyDataSetChanged();
			}
		});
		// ȡ����ť�����¼�
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();	//��ʧ�Ի���	
			}
		});
		dialog.show();
	}

	/**
	 * ������չ��İ�����
	 */
	public class Holder {
		public ImageView backDelIv;		// չʾid
		public TextView backNumTv;		// չʾ����
		public TextView backModeTv;		// չʾģʽ
	}
}
