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
 * 调用系统的位置服务,给安全手机发送地理位置
 * @author longuto
 */
public class LocationService extends Service {
	PhoneSafeConfig mConfig;	// 设置的配置对象
		
	@Override
	public void onCreate() {
		LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		List<String> providers = manager.getAllProviders();	// 获取所有位置
		for (String provider : providers) {
			System.out.println(provider);
		}
		Criteria criteria = new Criteria();	// 定位配置
		criteria.setAltitudeRequired(true);	// 支持海拔
		// 获取最好的定位方式
		String bestProvider = manager.getBestProvider(criteria, true);
		System.out.println("bestprovider: " + bestProvider);
		mConfig = new PhoneSafeConfig(this, PhoneSafeConfig.CONFIG);	// 初始化配置对象
		// 设置位置更新监听
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
			// 位置变化监听
			@Override
			public void onLocationChanged(Location location) {
				double latitude = location.getLatitude();	// 获取纬度
				double longitude = location.getLongitude();	// 获取经度
				System.out.println("经度 :" + latitude + ", 纬度 :" + longitude);
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
