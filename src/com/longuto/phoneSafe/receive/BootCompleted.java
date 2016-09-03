package com.longuto.phoneSafe.receive;

import com.longuto.phoneSafe.util.PhoneSafeConfig;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
/**
 * 在配置文件中注册系统引导完成广播事件
 * @author longuto
 */
public class BootCompleted extends BroadcastReceiver {
	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {
		PhoneSafeConfig config = new PhoneSafeConfig(context, PhoneSafeConfig.CONFIG);
		// 如果开启了防盗功能
		if(config.getBooleanFromXml(PhoneSafeConfig.IS_OPEN_SAFELOST, true)) {
			TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			// 正在用的SIM卡序列号
			String usedPhoneSimSerialNum = telManager.getSimSerialNumber();
			// 内存中存储的SIM卡序列号
			String storePhonesimSerialNum = config.getStringFromXml(PhoneSafeConfig.BIND_SIM_SERIAL, "");
			// 如果相同,则返回
			if(usedPhoneSimSerialNum.equals(storePhonesimSerialNum)) {
				return;
			}else {
				String safeNum = config.getStringFromXml(PhoneSafeConfig.SAFE_PHONENUM, "110");
				SmsManager smsManager = SmsManager.getDefault();	// 短信服务
				// 发短信
				smsManager.sendTextMessage(safeNum, null, "Boss.I'm lost,help me!", null, null);
			}	
		}else {
			return;
		}
	}
}
