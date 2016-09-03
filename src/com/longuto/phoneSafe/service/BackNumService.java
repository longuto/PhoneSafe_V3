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
	private static TelephonyManager mTelephonyManager;	// 电话管理器
	private MyBackNumSmsReceiver mySmsReceiver;	// 短信广播接收器
	private MyPhoneStateListener myPhoneStateListener;	// 电话状态

	@Override
	public void onCreate() {
		LogUtil.v(TAG, "onCreate");
		// 动态注册短信接收器
		mySmsReceiver = new MyBackNumSmsReceiver();
		IntentFilter filter = new IntentFilter();	// 过滤器
		filter.setPriority(1000);
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(mySmsReceiver, filter);	// 注册广播接收器
		
		// 电话监听
		myPhoneStateListener = new MyPhoneStateListener();
		mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);	// 电话管理者
		mTelephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);	// 电话监听
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
		unregisterReceiver(mySmsReceiver);	// 取消广播
		mTelephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);	//取消电话状态监听
		super.onDestroy();
	}
	
	/**
	 * 黑名单短信接收器,判断是否为短信或者全部拦截模式
	 * @author longuto
	 */
	private class MyBackNumSmsReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			LogUtil.v(TAG, "MyBackNumSms");
			Object[] objs = (Object[]) intent.getExtras().get("pdus");	// 获取短信
			for (Object obj : objs) {
				byte[] pdu = (byte[]) obj;	// 将obj转换成字节数组
				SmsMessage message = SmsMessage.createFromPdu(pdu);	// 创建短信
				String phoneNum = message.getOriginatingAddress();	// 获取发送者号码
				// 如果是以+86开头,则去掉+86
				String newPhoneNum = phoneNum;
				if(newPhoneNum.startsWith("+86")) {
					newPhoneNum = phoneNum.substring(3);
				}
				int mode = new BackNumDb(context).queryBackModeByNum(newPhoneNum);	// 获取黑名单号码的拦截模式
				if(mode == BackNumDb.ALL_MODE || mode == BackNumDb.SMS_MODE) {
					abortBroadcast();	// 截断短信
				}
			}
		}
	}
	
	/**
	 * 电话响铃状态
	 */
	private class MyPhoneStateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, final String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:	// 响铃
				String newPhoneNum = incomingNumber;
				if(newPhoneNum.startsWith("+86")) {
					newPhoneNum = newPhoneNum.substring(3);	// 去掉+86
				}
				int mode = new BackNumDb(BackNumService.this).queryBackModeByNum(newPhoneNum);	// 获取电话号码的拦截模式
				if(BackNumDb.ALL_MODE == mode || BackNumDb.CALL_MODE == mode) {
					endCall();	// 挂断电话
				}
				
				// 删除通话记录
				final String uriStr = "content://call_log/calls";
				final ContentResolver resolver = getContentResolver();	// 获取内容解决者
				// 注册内容观察者,当有通话记录产生时,才删除该通话记录,第二个参数：true代表可以沿袭
				resolver.registerContentObserver(Uri.parse(uriStr), true, 
						new ContentObserver(new Handler()) {
							@Override
							public void onChange(boolean selfChange) {
								resolver.delete(Uri.parse(uriStr), "number=?", new String[]{incomingNumber});
								super.onChange(selfChange);
							}							
						});
				break;
			case TelephonyManager.CALL_STATE_IDLE:	// 空闲
				
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:	// 挂起
				
				break;
			default:
				break;
			}	
		}

		/**
		 * 通过远程服务以及反射的方法截断电话
		 */
		private void endCall() {
			ToastUtil.show(getApplicationContext(), "电话已拨打");
			try {
				// 通过反射获取binder
				Class<?> clazz = getClassLoader().loadClass("android.os.ServiceManager");	
				Method method = clazz.getDeclaredMethod("getService", String.class);
				IBinder iBinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
				ITelephony asInterface = ITelephony.Stub.asInterface(iBinder);	// 先获取远程服务的接口
				asInterface.endCall();	// 调用接口方法,执行挂断电话操作
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
