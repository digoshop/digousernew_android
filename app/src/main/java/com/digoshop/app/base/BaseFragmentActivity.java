package com.digoshop.app.base;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.digoshop.R;
import com.digoshop.app.utils.SystemBarTintManager;

public abstract class BaseFragmentActivity extends FragmentActivity {

	protected SystemBarTintManager tintManager;
	public void OnTitleReturnClick(View view) {
		 finish();
	}
	public abstract boolean isNoTitle();
	public abstract boolean isFullScreen();
	private static final String EXITACTION = "action.exitfragment";
	private ExitReceiver exitReceiver = new ExitReceiver();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		if (isNoTitle()){
		//	requestWindowFeature(Window.FEATURE_NO_TITLE);
		//	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			// 去掉窗口标题
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		}
		if (isFullScreen()){
			// 全屏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		//顶部颜色
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.title_bar_bg);//通知栏所需颜色
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(EXITACTION);
		registerReceiver(exitReceiver, filter);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(exitReceiver);
	}

	//顶部颜色
	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	class ExitReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			BaseFragmentActivity.this.finish();
		}

	}
}
