package com.longuto.phoneSafe.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.longuto.phoneSafe.R;
import com.longuto.phoneSafe.entity.Contact;

/**
 * չʾ��ϵ�˵�adapter
 * @author longuto
 */
public class ContactAdapter extends BaseAdapter {
	private Context context;	// ������
	private List<Contact> mContacts;	// ����

	public ContactAdapter(Context context, List<Contact> contacts) {
		this.context = context;
		this.mContacts = contacts;
	}

	@Override
	public int getCount() {
		return mContacts == null ? 0 : mContacts.size();
	}

	@Override
	public Object getItem(int position) {
		return mContacts == null ? 0 : mContacts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			Holder holder = new Holder();
			// ��ϵ���б�
			convertView = LayoutInflater.from(context).inflate(R.layout.listcontact_item, parent, false);
			holder.nameTv = (TextView) convertView.findViewById(R.id.contactName_tv);
			holder.phoneNumTv = (TextView) convertView.findViewById(R.id.contactNum_tv);
			convertView.setTag(holder);
		}
		Holder holder = (Holder) convertView.getTag();	// ��ȡչ��tag
		Contact item = (Contact) getItem(position);
		holder.nameTv.setText(item.getName());	// ��������
		holder.phoneNumTv.setText(item.getPhoneNum());	// ���ú���
		return convertView;
	}

	/**
	 * չ���ϵĿؼ�
	 * @author longuto
	 */
	class Holder {
		TextView nameTv;	// ����TextView
		TextView phoneNumTv;	// ����TextView
	}
}
