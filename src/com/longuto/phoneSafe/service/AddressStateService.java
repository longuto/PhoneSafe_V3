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
 * 通话状态改变监听 -- 自定义服务
 * @author longuto
 */
public class AddressStateService extends Service {
	// 存放Toast界面的背景资源数组
	private final int[] toastResIds = {R.drawable.call_locate_green, R.drawable.call_locate_gray, R.drawable.call_locate_orange, R.drawable.call_locate_blue}; 
	
	private Context context;	// 上下文
	private PhoneSafeConfig mConfig;	// 配置软件的对象
	
	private TelephonyManager telephonyManager;	// 电话管理者
	private MyPhoneStateListener myPhoneStateListener;	//电话监听
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		context = getApplicationContext();
		mConfig = new PhoneSafeConfig(context, PhoneSafeConfig.CONFIG);
		
		// 获取电话管理服务
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
		telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);	//取消电话状态监听
		super.onDestroy();
	}
	
	/**
	 * 电话监听类
	 * @author longuto
	 */
	private class MyPhoneStateListener extends PhoneStateListener {	
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:	// 响铃状态
				String numLocation = AddressManager.queryLocation(incomingNumber, getApplicationContext());
				int which = mConfig.getIntFromXml(PhoneSafeConfig.TOASTSTYLE);
				// 根据用户设置,显示指定风格的Toast界面
				showDiytoast(numLocation, which);
				break;
			case TelephonyManager.CALL_STATE_IDLE:	// 电话空闲状态监听
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:	// 挂起状态监听		
				break;
			default:
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}

		/**
		 * 根据传入的值显示不同风格的来电号码归属地
		 * @param incomingNumber 来电号码
		 * @param which 确定对话框风格
		 */
		private void showDiytoast(String numLocation, int which) {
			Toast toast = new Toast(context);	// 自定义吐司对话框
			toast.setGravity(Gravity.TOP|Gravity.CENTER_VERTICAL, 20, 200);	// 设置吐司位置
			View view = View.inflate(context, R.layout.diytoast_item, null);	// 自定义吐司的视图
			view.setBackgroundResource(toastResIds[which]);	// 设置背景
			TextView numLocationTv = (TextView) view.findViewById(R.id.numLocation_tv);
			numLocationTv.setText(numLocation);
			toast.setView(view);
			toast.setDuration(3000);
			toast.show();
		}	
	}
}
