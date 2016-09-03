package com.longuto.phoneSafe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.longuto.phoneSafe.util.PhoneSafeConfig;
import com.longuto.phoneSafe.util.ToastUtil;


public class WelcomeActivity extends Activity {
	private ImageView mWelcomeIv;	//欢迎图片控件
	private Context context;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);	// 设置界面没标题
        setContentView(R.layout.activity_welcome);
        
        mWelcomeIv = (ImageView) findViewById(R.id.welcome_iv);
        context = this;
 
        setImageViewAnimation();	//设置欢迎图片的动画效果
    }

    /**
     * 设置欢迎图片的动画效果
     * @param mWelcomeIv 图片控件
     */
	private void setImageViewAnimation() {
		AnimationSet set = new AnimationSet(false);
		// 透明动画
		AlphaAnimation alpha = new AlphaAnimation(0, 1);
		alpha.setDuration(3000);
		alpha.setFillAfter(true);
		set.addAnimation(alpha);
		
		// 缩放动画
		ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, 
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scale.setDuration(3000);
		scale.setFillAfter(true);
		set.addAnimation(scale);
		
		// 旋转动画
		RotateAnimation rotate = new RotateAnimation(0, 360, 
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotate.setDuration(3000);
		rotate.setFillAfter(true);
		set.addAnimation(rotate);
		
		// 设置动画监听
		set.setAnimationListener(new AnimationListener() {	
			@Override
			public void onAnimationStart(Animation animation) {
				ToastUtil.show(context, "动画开始");
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
				ToastUtil.show(context, "动画重复");
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				ToastUtil.show(context, "动画结束");
				
				// 判断Xml配置中的is_first_used是否为true
				PhoneSafeConfig config = new PhoneSafeConfig(context, PhoneSafeConfig.CONFIG);
				if(config.getBooleanFromXml(PhoneSafeConfig.IS_FIRST_USED, true)) {
					Intent intent = new Intent(context, GuideActivity.class);
					startActivity(intent);				
				} else {
					Intent intent = new Intent(context, SplashActivity.class);
					startActivity(intent);
				}
				finish();
			}
		});
		
		// 开始动画
		mWelcomeIv.startAnimation(set);
	}     
}
