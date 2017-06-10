package com.digoshop.app.module.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.digoshop.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.youth.banner.loader.ImageLoader;


public class GlideImageLoader extends ImageLoader {
    private DisplayImageOptions options;

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
//         Glide.with(context).load(path).placeholder(R.drawable.no_big_png).into(imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        options = new DisplayImageOptions.Builder()
                // .displayer(new RoundedBitmapDisplayer(45))
                .showStubImage(R.drawable.home_adone)
                .showImageForEmptyUri(R.drawable.home_adone)
                .showImageOnFail(R.drawable.home_adone).cacheInMemory()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc().build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                .displayImage(path.toString(), imageView, options);
//        ImageRequest imageRequest = new ImageRequest(
//                "http://developer.android.com/images/home/aw_dac.png",
//                new Response.Listener<Bitmap>() {
//                    @Override
//                    public void onResponse(Bitmap response) {
//                        imageView.setImageBitmap(response);
//                    }
//                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                imageView.setImageResource(R.drawable.no_big_png);
//            }
//        });

    }

}
