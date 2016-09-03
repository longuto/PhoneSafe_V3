package com.longuto.phoneSafe.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.longuto.phoneSafe.R;
import com.longuto.phoneSafe.db.AddressManager;
import com.longuto.phoneSafe.util.PhoneSafeConfig;
import com.longuto.phoneSafe.util.ToastUtil;

/**
 * ͨ��״̬�ı���� -- �Զ������
 * @author longuto
 */
public class AddressStateService extends Service {
	// ���Toast����ı�����Դ����
	private final int[] toastResIds = {R.drawable.call_locate_green, R.drawable.call_locate_gray, R.drawable.call_locate_orange, R.drawable.call_locate_blue}; 
	
	private Context context;	// ������
	private PhoneSafeConfig mConfig;	// ��������Ķ���
	
	private TelephonyManager telephonyManager;	// �绰������
	private MyPhoneStateListener myPhoneStateListener;	//�绰����
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		context = getApplicationContext();
		mConfig = new PhoneSafeConfig(context, PhoneSafeConfig.CONFIG);
		
		// ��ȡ�绰�������
		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		myPhoneStateListener= new MyPhoneStateListener();
		telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);			
		return super.onStartCommand(intent, flags, startId);
	}	
	
	@Override
	public IBinder onBind(Intent intent) {	
		return null;
	}

	@Override
	public void onDestroy() {
		telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);	//ȡ���绰״̬����
		super.onDestroy();
	}
	
	/**
	 * �绰������
	 * @author longuto
	 */
	private class MyPhoneStateListener extends PhoneStateListener {	
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:	// ����״̬
				String numLocation = AddressManager.queryLocation(incomingNumber, getApplicationContext());
				int which = mConfig.getIntFromXml(PhoneSafeConfig.TOASTSTYLE);
				// �����û�����,��ʾָ������Toast����
				showDiytoast(numLocation, which);
				break;
			case TelephonyManager.CALL_STATE_IDLE:	// �绰����״̬����
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:	// ����״̬����		
				break;
			default:
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}

		/**
		 * ���ݴ����ֵ��ʾ��ͬ����������������
		 * @param incomingNumber �������
		 * @param which ȷ���Ի�����
		 */
		private void showDiytoast(String numLocation, int which) {
			Toast toast = new Toast(context);	// �Զ�����˾�Ի���
			toast.setGravity(Gravity.TOP|Gravity.CENTER_VERTICAL, 20, 200);	// ������˾λ��
			View view = View.inflate(context, R.layout.diytoast_item, null);	// �Զ�����˾����ͼ
			view.setBackgroundResource(toastResIds[which]);	// ���ñ���
			TextView numLocationTv = (TextView) view.findViewById(R.id.numLocation_tv);
			numLocationTv.setText(numLocation);
			toast.setView(view);
			toast.setDuration(3000);
			toast.show();
		}	
	}
}
