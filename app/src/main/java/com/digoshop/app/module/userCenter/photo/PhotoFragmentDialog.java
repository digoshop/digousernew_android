package com.digoshop.app.module.userCenter.photo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;

import com.digoshop.R;
import com.digoshop.app.App;

public class PhotoFragmentDialog extends DialogFragment implements
		OnClickListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.fragment_photo, null);
		builder.setView(view);
		initView(view);

		AlertDialog dialog = builder.create();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		return dialog;
	}

	LinearLayout ll_picture, ll_photo;

	void initView(View view) {
		ll_picture = (LinearLayout) view.findViewById(R.id.ll_picture);
		ll_photo = (LinearLayout) view.findViewById(R.id.ll_photo);

		ll_picture.setOnClickListener(this);
		ll_photo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_picture:
			App.getInstance().showToast("拍照");
			dismiss();
			break;
		case R.id.ll_photo:
			App.getInstance().showToast("相册");
			dismiss();
			break;
		}

	}

 
}
