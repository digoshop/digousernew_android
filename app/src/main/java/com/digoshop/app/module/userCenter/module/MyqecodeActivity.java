package com.digoshop.app.module.userCenter.module;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.Displayer;
import com.digoshop.app.utils.LocalSave;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Hashtable;

/**
 * 用户二维详情页面
 *
 * @author Administrator
 */
public class MyqecodeActivity extends BaseActivity implements OnClickListener {
    private ImageView iv_qrcodecontent;
    private Bitmap logo;
    private static final int IMAGE_HALFWIDTH = 30;// 宽度值，影响中间图片大小
    // {"shop_id":1000048, "type":2} //商铺
    // {"shop_id":1000048, "type":1} //商场
    // {"uid":"103232432432434"} //用户id
    private String mobilestr, usericon;
    private String usernamestrs;
    private TextView tv_qrcode_username, tv_qrcode_userphone;
    private ImageView iv_qrcode_touxiang;
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myqrcodedetailaty);
        //圆形图片
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.grzx_touxiang)
                .showImageForEmptyUri(R.drawable.grzx_touxiang)
                .showImageOnFail(R.drawable.grzx_touxiang)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)   //设置图片的解码类型
                .displayer(new Displayer(0))
                .build();

        iv_qrcode_touxiang = (ImageView) findViewById(R.id.iv_qrcode_touxiang);

        // 获得资源图片，可改成获取本地图片或拍照获取图片

        Intent intent = getIntent();
        mobilestr = intent.getStringExtra("mobilnum");
        usericon = intent.getStringExtra("usericon");

        usernamestrs = intent.getStringExtra("userAccountstr");
        String umImage = LocalSave.getValue(MyqecodeActivity.this, AppConfig.basefile, "login_um_image", "");//友盟登录头像
        Bitmap bitmap;
        if (!TextUtils.isEmpty(umImage)) {
            ImageLoader.getInstance().displayImage(umImage, iv_qrcode_touxiang, options);
              bitmap = ImageLoader.getInstance().loadImageSync(umImage);
        } else {
            bitmap = ImageLoader.getInstance().loadImageSync(usericon);
            ImageLoader.getInstance().displayImage(usericon, iv_qrcode_touxiang, options);
        }


        if(bitmap!=null){
            logo =bitmap;
        }else{
            logo = BitmapFactory.decodeResource(super.getResources(),
                    R.drawable.ic_launcher);
        }
        tv_qrcode_username = (TextView) findViewById(R.id.tv_qrcode_username);
        tv_qrcode_userphone = (TextView) findViewById(R.id.tv_qrcode_userphone);
        tv_qrcode_username.setText(usernamestrs);

        if (!TextUtils.isEmpty(mobilestr) && mobilestr.length() > 6) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mobilestr.length(); i++) {
                char c = mobilestr.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
            tv_qrcode_userphone.setText(sb.toString());
        }

        String contentstr = "{\"uid\":" + "\"" + AppConfig.userid + "\"" + "}";
        Log.v("ceshi", "contentstr+" + contentstr);
        // {"uid":"103232432432434"} //用户id
        if (TextUtils.isEmpty(contentstr)) {
            App.getInstance().showToast("uid为空");
            finish();
        }
        iv_qrcodecontent = (ImageView) findViewById(R.id.iv_qrcodecontent);
        try {
            // 调用方法createCode生成二维码
            Bitmap bm = createCode(contentstr, logo, BarcodeFormat.QR_CODE);
            // 将二维码在界面中显示
            iv_qrcodecontent.setImageBitmap(bm);
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码
     *
     * @param string  二维码中包含的文本信息
     * @param mBitmap logo图片
     * @param format  编码格式
     * @return Bitmap 位图
     * @throws WriterException
     */
    public Bitmap createCode(String string, Bitmap mBitmap, BarcodeFormat format)
            throws WriterException {
        Matrix m = new Matrix();
        float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
        float sy = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getHeight();
        m.setScale(sx, sy);// 设置缩放信息
        // 将logo图片按martix设置的信息缩放
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
                mBitmap.getHeight(), m, false);

        MultiFormatWriter writer = new MultiFormatWriter();//
        Hashtable<EncodeHintType, String> hst = new Hashtable<EncodeHintType, String>();
        hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");// 设置字符编码
        BitMatrix matrix = writer.encode(string, format, 400, 400, hst);// 生成二维码矩阵信息
        int width = matrix.getWidth();// 矩阵高度
        int height = matrix.getHeight();// 矩阵宽度
        int halfW = width / 2;
        int halfH = height / 2;
        int[] pixels = new int[width * height];// 定义数组长度为矩阵高度*矩阵宽度，用于记录矩阵中像素信息
        for (int y = 0; y < height; y++) {// 从行开始迭代矩阵
            for (int x = 0; x < width; x++) {// 迭代列
                if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
                        && y > halfH - IMAGE_HALFWIDTH
                        && y < halfH + IMAGE_HALFWIDTH) {// 次处位置用于存放图片信息
                    pixels[y * width + x] = mBitmap.getPixel(x - halfW
                            + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);// 记录图片每个像素信息
                } else {
                    if (matrix.get(x, y)) {// 如果有黑块点，记录信息
                        pixels[y * width + x] = 0xff000000;// 记录黑块信息
                    }
                }

            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    @Override
    public boolean isNoTitle() {
        return true;
    }

    @Override
    public boolean isFullScreen() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }

}
