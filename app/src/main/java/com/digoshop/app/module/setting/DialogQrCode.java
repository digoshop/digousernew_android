package com.digoshop.app.module.setting;

import android.os.Bundle;

import com.digoshop.R;
import com.digoshop.app.base.BaseActivity;

/**
 * 
 * TODO 用户中心-设置-二维码弹框
 * @author  liushengqiang lsqbeyond@yeah.net
 * @data:  2016-6-6 下午11:28:53 
 * @version:  V1.0
 */
public class DialogQrCode extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.dialogqrcode);
    }

	@Override
	public boolean isNoTitle() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isFullScreen() {
		// TODO Auto-generated method stub
		return false;
	}
}