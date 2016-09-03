package com.longuto.phoneSafe.service;

import java.util.List;

import com.longuto.phoneSafe.util.LogUtil;
import com.longuto.phoneSafe.util.PhoneSafeConfig;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

/**
 * ����ϵͳ��λ�÷���,����ȫ�ֻ����͵���λ��
 * @author longuto
 */
public class LocationService extends Service {
	PhoneSafeConfig mConfig;	// ���õ����ö���
		
	@Override
	public void onCreate() {
		LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		List<String> providers = manager.getAllProviders();	// ��ȡ����λ��
		for (String provider : providers) {
			System.out.println(provider);
		}
		Criteria criteria = new Criteria();	// ��λ����
		criteria.setAltitudeRequired(true);	// ֧�ֺ���
		// ��ȡ��õĶ�λ��ʽ
		String bestProvider = manager.getBestProvider(criteria, true);
		System.out.println("bestprovider: " + bestProvider);
		mConfig = new PhoneSafeConfig(this, PhoneSafeConfig.CONFIG);	// ��ʼ�����ö���
		// ����λ�ø��¼���
		manager.requestLocationUpdates(bestProvider, 2000, 10, new LocationListener() {	
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}		
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}	
			// λ�ñ仯����
			@Override
			public void onLocationChanged(Location location) {
				double latitude = location.getLatitude();	// ��ȡγ��
				double longitude = location.getLongitude();	// ��ȡ����
				System.out.println("���� :" + latitude + ", γ�� :" + longitude);
				mConfig.putStringToXml(PhoneSafeConfig.PHONELATITUDE, latitude + "");
				mConfig.putStringToXml(PhoneSafeConfig.PHONELONGITUDE, longitude + "");
			}
		});
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

}
