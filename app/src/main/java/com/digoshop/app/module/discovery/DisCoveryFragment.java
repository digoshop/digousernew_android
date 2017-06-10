package com.digoshop.app.module.discovery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digoshop.R;
import com.digoshop.app.base.BaseFragment;
/**
 * 
 * TODO main-发现页面 
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016-5-21 上午10:10:48
 * @version: V1.0
 */
public class DisCoveryFragment extends BaseFragment {

	
	@Override
	public View onCreateView(LayoutInflater inflater,
		 ViewGroup container,  Bundle savedInstanceState) {
		Log.d("mytag", "fragment_discovery");
		return inflater.inflate(R.layout.fragment_discovery, null);
	}


}
