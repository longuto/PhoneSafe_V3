package com.longuto.phoneSafe.receive;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.longuto.phoneSafe.R;
import com.longuto.phoneSafe.db.dao.BackNumDb;
import com.longuto.phoneSafe.service.LocationService;
import com.longuto.phoneSafe.util.PhoneSafeConfig;

/**
 * 短信接收监听
 * @author longuto
 */
public class SmsReceive extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// 获取设备方案管理者
		DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		
		// 获取配置文件的参数
		PhoneSafeConfig mConfig = new PhoneSafeConfig(context, PhoneSafeConfig.CONFIG);
		// 根据国际标准padu获取短信的Object数组
		Object[] objects = (Object[]) intent.getExtras().get("pdus");
		for (Object object : objects) {
			// 将每个object转换成字节数组,封装了短信信息
			byte[] pdu = (byte[]) object;
			SmsMessage message = SmsMessage.createFromPdu(pdu);
			String phoneNum = message.getOriginatingAddress();	// 获取发送信息的号码
			String body = message.getMessageBody();	// 获取信息内容
			// 判断是否是安全号码发送的信息
			if(phoneNum.equals(mConfig.getStringFromXml(PhoneSafeConfig.SAFE_PHONENUM, ""))) {
				if("#*location*#".equals(body)) {
					Intent service = new Intent(context, LocationService.class);					
					context.startService(service);
					
					// 发送经纬度至安全手机
					String longitude = mConfig.getStringFromXml(PhoneSafeConfig.PHONELONGITUDE, "");
					String latitude = mConfig.getStringFromXml(PhoneSafeConfig.PHONELATITUDE, "");
					SmsManager manager = SmsManager.getDefault();
					manager.sendTextMessage(phoneNum, null, "经度:" + longitude + ", 纬度:" + latitude, null, null);
					abortBroadcast();	// 截断短信
				}else if("#*alarm*#".equals(body)) {
					MediaPlayer player = MediaPlayer.create(context, R.raw.guoge);
					player.start();
					abortBroadcast();	// 截断短信
				}else if("#*wipe*#".equals(body)) {
					// 声明一个组件
					ComponentName componentName = new ComponentName(context, MyAdminReceiver.class);
					// 如果组件激活
					if(devicePolicyManager.isAdminActive(componentName)) {
						System.out.println("远程擦除数据");
						devicePolicyManager.wipeData(0);	// 擦除数据
					}
					abortBroadcast();	// 截断短信
				}else if("#*lockscreen*#".equals(body)) {
					// 声明一个组件
					ComponentName componentName = new ComponentName(context, MyAdminReceiver.class);
					// 假如组件激活了
					if(devicePolicyManager.isAdminActive(componentName)) {
						System.out.println("远程锁屏");
						devicePolicyManager.lockNow();	// 远程锁屏
					}
					abortBroadcast();	// 截断短信
				}
			}
		}
	}

}
