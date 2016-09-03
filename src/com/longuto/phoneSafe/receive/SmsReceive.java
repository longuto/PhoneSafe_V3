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
 * ���Ž��ռ���
 * @author longuto
 */
public class SmsReceive extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// ��ȡ�豸����������
		DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		
		// ��ȡ�����ļ��Ĳ���
		PhoneSafeConfig mConfig = new PhoneSafeConfig(context, PhoneSafeConfig.CONFIG);
		// ���ݹ��ʱ�׼padu��ȡ���ŵ�Object����
		Object[] objects = (Object[]) intent.getExtras().get("pdus");
		for (Object object : objects) {
			// ��ÿ��objectת�����ֽ�����,��װ�˶�����Ϣ
			byte[] pdu = (byte[]) object;
			SmsMessage message = SmsMessage.createFromPdu(pdu);
			String phoneNum = message.getOriginatingAddress();	// ��ȡ������Ϣ�ĺ���
			String body = message.getMessageBody();	// ��ȡ��Ϣ����
			// �ж��Ƿ��ǰ�ȫ���뷢�͵���Ϣ
			if(phoneNum.equals(mConfig.getStringFromXml(PhoneSafeConfig.SAFE_PHONENUM, ""))) {
				if("#*location*#".equals(body)) {
					Intent service = new Intent(context, LocationService.class);					
					context.startService(service);
					
					// ���;�γ������ȫ�ֻ�
					String longitude = mConfig.getStringFromXml(PhoneSafeConfig.PHONELONGITUDE, "");
					String latitude = mConfig.getStringFromXml(PhoneSafeConfig.PHONELATITUDE, "");
					SmsManager manager = SmsManager.getDefault();
					manager.sendTextMessage(phoneNum, null, "����:" + longitude + ", γ��:" + latitude, null, null);
					abortBroadcast();	// �ض϶���
				}else if("#*alarm*#".equals(body)) {
					MediaPlayer player = MediaPlayer.create(context, R.raw.guoge);
					player.start();
					abortBroadcast();	// �ض϶���
				}else if("#*wipe*#".equals(body)) {
					// ����һ�����
					ComponentName componentName = new ComponentName(context, MyAdminReceiver.class);
					// ����������
					if(devicePolicyManager.isAdminActive(componentName)) {
						System.out.println("Զ�̲�������");
						devicePolicyManager.wipeData(0);	// ��������
					}
					abortBroadcast();	// �ض϶���
				}else if("#*lockscreen*#".equals(body)) {
					// ����һ�����
					ComponentName componentName = new ComponentName(context, MyAdminReceiver.class);
					// �������������
					if(devicePolicyManager.isAdminActive(componentName)) {
						System.out.println("Զ������");
						devicePolicyManager.lockNow();	// Զ������
					}
					abortBroadcast();	// �ض϶���
				}
			}
		}
	}

}
