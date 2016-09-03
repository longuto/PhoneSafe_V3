package com.longuto.phoneSafe.service;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.longuto.phoneSafe.db.dao.BackNumDb;
import com.longuto.phoneSafe.util.LogUtil;
import com.longuto.phoneSafe.util.ToastUtil;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

public class BackNumService extends Service {
	private static final String TAG = "BackNumService";
	private static TelephonyManager mTelephonyManager;	// �绰������
	private MyBackNumSmsReceiver mySmsReceiver;	// ���Ź㲥������
	private MyPhoneStateListener myPhoneStateListener;	// �绰״̬

	@Override
	public void onCreate() {
		LogUtil.v(TAG, "onCreate");
		// ��̬ע����Ž�����
		mySmsReceiver = new MyBackNumSmsReceiver();
		IntentFilter filter = new IntentFilter();	// ������
		filter.setPriority(1000);
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(mySmsReceiver, filter);	// ע��㲥������
		
		// �绰����
		myPhoneStateListener = new MyPhoneStateListener();
		mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);	// �绰������
		mTelephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);	// �绰����
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LogUtil.v(TAG, "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}
	
	@Override
	public void onDestroy() {
		LogUtil.v(TAG, "onDestroy");
		unregisterReceiver(mySmsReceiver);	// ȡ���㲥
		mTelephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);	//ȡ���绰״̬����
		super.onDestroy();
	}
	
	/**
	 * ���������Ž�����,�ж��Ƿ�Ϊ���Ż���ȫ������ģʽ
	 * @author longuto
	 */
	private class MyBackNumSmsReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			LogUtil.v(TAG, "MyBackNumSms");
			Object[] objs = (Object[]) intent.getExtras().get("pdus");	// ��ȡ����
			for (Object obj : objs) {
				byte[] pdu = (byte[]) obj;	// ��objת�����ֽ�����
				SmsMessage message = SmsMessage.createFromPdu(pdu);	// ��������
				String phoneNum = message.getOriginatingAddress();	// ��ȡ�����ߺ���
				// �������+86��ͷ,��ȥ��+86
				String newPhoneNum = phoneNum;
				if(newPhoneNum.startsWith("+86")) {
					newPhoneNum = phoneNum.substring(3);
				}
				int mode = new BackNumDb(context).queryBackModeByNum(newPhoneNum);	// ��ȡ���������������ģʽ
				if(mode == BackNumDb.ALL_MODE || mode == BackNumDb.SMS_MODE) {
					abortBroadcast();	// �ض϶���
				}
			}
		}
	}
	
	/**
	 * �绰����״̬
	 */
	private class MyPhoneStateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, final String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:	// ����
				String newPhoneNum = incomingNumber;
				if(newPhoneNum.startsWith("+86")) {
					newPhoneNum = newPhoneNum.substring(3);	// ȥ��+86
				}
				int mode = new BackNumDb(BackNumService.this).queryBackModeByNum(newPhoneNum);	// ��ȡ�绰���������ģʽ
				if(BackNumDb.ALL_MODE == mode || BackNumDb.CALL_MODE == mode) {
					endCall();	// �Ҷϵ绰
				}
				
				// ɾ��ͨ����¼
				final String uriStr = "content://call_log/calls";
				final ContentResolver resolver = getContentResolver();	// ��ȡ���ݽ����
				// ע�����ݹ۲���,����ͨ����¼����ʱ,��ɾ����ͨ����¼,�ڶ���������true���������Ϯ
				resolver.registerContentObserver(Uri.parse(uriStr), true, 
						new ContentObserver(new Handler()) {
							@Override
							public void onChange(boolean selfChange) {
								resolver.delete(Uri.parse(uriStr), "number=?", new String[]{incomingNumber});
								super.onChange(selfChange);
							}							
						});
				break;
			case TelephonyManager.CALL_STATE_IDLE:	// ����
				
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:	// ����
				
				break;
			default:
				break;
			}	
		}

		/**
		 * ͨ��Զ�̷����Լ�����ķ����ضϵ绰
		 */
		private void endCall() {
			ToastUtil.show(getApplicationContext(), "�绰�Ѳ���");
			try {
				// ͨ�������ȡbinder
				Class<?> clazz = getClassLoader().loadClass("android.os.ServiceManager");	
				Method method = clazz.getDeclaredMethod("getService", String.class);
				IBinder iBinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
				ITelephony asInterface = ITelephony.Stub.asInterface(iBinder);	// �Ȼ�ȡԶ�̷���Ľӿ�
				asInterface.endCall();	// ���ýӿڷ���,ִ�йҶϵ绰����
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
