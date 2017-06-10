package com.digoshop.app.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by zzr on 16/10/26.
 */
public class MyImageLoder  {

    private static MyImageLoder INSTANCE;

    private MyImageLoder(){}

    public static MyImageLoder getInstance(){
        if (INSTANCE==null){
            synchronized (MyImageLoder.class){
                if (INSTANCE==null){
                    INSTANCE =new MyImageLoder();
                }
            }
        }
        return INSTANCE;
    }

    public void disImage(Context context, String url, int imageId, ImageView imageView){
        Glide.with(context).load(url).placeholder(imageId).centerCrop().into(imageView);
    }
}
