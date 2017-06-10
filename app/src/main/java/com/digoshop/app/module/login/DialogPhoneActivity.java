package com.digoshop.app.module.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.digoshop.R;
import com.digoshop.app.base.BaseActivity;

public class DialogPhoneActivity extends BaseActivity{
	public Button reipone_btn,now_login;

	@Override
	public boolean isNoTitle() {
		return true;
	}

	@Override
	public boolean isFullScreen() {
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_dialog);
		reipone_btn = (Button) findViewById(R.id.reipone_btn);
		now_login = (Button) findViewById(R.id.now_login);
		reipone_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		now_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(DialogPhoneActivity.this, Loginaty.class);
				startActivity(intent);
				finish();
			}
		});
	}

}
