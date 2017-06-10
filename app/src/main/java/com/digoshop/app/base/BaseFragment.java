package com.digoshop.app.base;

import android.support.v4.app.Fragment;
import android.view.View;

public abstract class BaseFragment extends Fragment {


    public void OnTitleReturnClick(View view) {
        getActivity().finish();
    }

}
