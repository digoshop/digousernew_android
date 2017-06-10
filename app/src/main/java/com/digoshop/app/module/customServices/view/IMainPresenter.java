package com.digoshop.app.module.customServices.view;

import android.net.Uri;

import java.util.List;

/**
 * Created by Arlen on 2016/6/30 10:48.
 */
public interface IMainPresenter {

    void compressImage(Uri uri);

    void uploadImage(List<String> files);

}
