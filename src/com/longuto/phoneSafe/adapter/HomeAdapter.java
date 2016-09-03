package com.longuto.phoneSafe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.longuto.phoneSafe.R;

public class HomeAdapter extends BaseAdapter {
	// Ӧ����������
	private final String names[] = {"�ֻ�����","ͨ����ʿ","�������",
			"���̹���","����ͳ��","�ֻ�ɱ��" ,"��������","�߼�����","��������"		
	};
	// ͼƬ��Դ����
	private final int iconPaths[] = {R.drawable.safe, R.drawable.callmsgsafe,
				R.drawable.app, R.drawable.taskmanager, R.drawable.netmanager,
				R.drawable.trojan, R.drawable.sysoptimize, R.drawable.atools,
				R.drawable.settings		
	};
	
	private Context context;
	public HomeAdapter(Context context) {
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return names.length;
	}

	@Override
	public Object getItem(int position) {
		return names[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// ��ȡÿ��Item��ͼ
		View view = LayoutInflater.from(context).inflate(R.layout.home_grid_item, parent, false);
		ImageView itemIv = (ImageView) view.findViewById(R.id.gridItemIcon_iv);
		TextView itemTv = (TextView) view.findViewById(R.id.gridItemName_tv);
		itemIv.setImageResource(iconPaths[position]);
		itemTv.setText(names[position]);
		return view;
	}

}
