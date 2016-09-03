package com.longuto.phoneSafe.receive;

import com.longuto.phoneSafe.util.PhoneSafeConfig;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
/**
 * �������ļ���ע��ϵͳ������ɹ㲥�¼�
 * @author longuto
 */
public class BootCompleted extends BroadcastReceiver {
	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {
		PhoneSafeConfig config = new PhoneSafeConfig(context, PhoneSafeConfig.CONFIG);
		// ��������˷�������
		if(config.getBooleanFromXml(PhoneSafeConfig.IS_OPEN_SAFELOST, true)) {
			TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			// �����õ�SIM�����к�
			String usedPhoneSimSerialNum = telManager.getSimSerialNumber();
			// �ڴ��д洢��SIM�����к�
			String storePhonesimSerialNum = config.getStringFromXml(PhoneSafeConfig.BIND_SIM_SERIAL, "");
			// �����ͬ,�򷵻�
			if(usedPhoneSimSerialNum.equals(storePhonesimSerialNum)) {
				return;
			}else {
				String safeNum = config.getStringFromXml(PhoneSafeConfig.SAFE_PHONENUM, "110");
				SmsManager smsManager = SmsManager.getDefault();	// ���ŷ���
				// ������
				smsManager.sendTextMessage(safeNum, null, "Boss.I'm lost,help me!", null, null);
			}	
		}else {
			return;
		}
	}
}
