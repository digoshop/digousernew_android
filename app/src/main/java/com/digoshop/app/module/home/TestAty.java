package com.digoshop.app.module.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.digoshop.R;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.home.model.ShowInfo;
import com.digoshop.app.utils.QRCodeUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

/**
 * Created by lsqbeyond on 2017/4/14.
 */

public class TestAty extends BaseActivity {
    private ImageView qrShowImg;
    int width, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.testaty);
        Intent intent =getIntent();
      String   url = getIntent().getStringExtra("url");

       qrShowImg = (ImageView) findViewById(R.id.qrCodeShowImg);
        // createLogQRImg();
       // createShareShopImg();
        Log.v("ceshi","displayImage+"+url);
        ImageLoader.getInstance().displayImage("file://" + url, qrShowImg);

    }

    private void createShareShopImg() {
        // TODO Auto-generated method stub
        final String filePath = getFileRoot(TestAty.this) + File.separator
                + "qr_" + System.currentTimeMillis() + ".jpg";
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        // 二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
        new Thread(new Runnable() {
            @Override
            public void run() {

                ShowInfo showInfo = new ShowInfo();
                showInfo.setWidth(width);
                showInfo.setHeight(height);
                showInfo.setDigoslogan("逛街用迪购购物更轻松");
                showInfo.setShopname("刚毅体育");
                showInfo.setShoptype("签约");
                showInfo.setShopintroduce("七匹狼品牌创立于1990年，是中国男装行业开创性品牌。2000年推出格子茄克，成为红遍大江南北的“茄克之王”，并在此后连续16年中国茄克市场占有率第一。");
                showInfo.setShoptime("周一-周四 9:00-21:00,周五-周日 9:00-22:00");
                showInfo.setShoptel("010-8584821");
                showInfo.setShopaddress("(双井店)位于北京市朝阳区东三环中路双井桥东几百米路北");
                showInfo.setQrcodecontent("http://m.digoshop.com/shop/getShopById?shopId=1000458");
                boolean success = QRCodeUtil.createShareShopImg(TestAty.this, showInfo, filePath);
                if (success) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            qrShowImg.setImageBitmap(BitmapFactory
                                    .decodeFile(filePath));
                        }
                    });
                }
            }
        }).start();

    }

    private void createLogQRImg() {
        // TODO Auto-generated method stub
        final String filePath = getFileRoot(TestAty.this) + File.separator
                + "qr_" + System.currentTimeMillis() + ".jpg";
        Log.v("ceshi", "filePath+" + filePath);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        // 二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
        new Thread(new Runnable() {
            @Override
            public void run() {

                ShowInfo showInfo = new ShowInfo();
                showInfo.setWidth(width);
                showInfo.setHeight(height);
                showInfo.setLogoName("果果");
                showInfo.setStoreName("苹果旗舰店");
                showInfo.setUserName("用户名");
                showInfo.setWeChatHint("分享了一个宝贝给你");
                showInfo.setProdName("美女一枚，价格优惠");
                showInfo.setProdActivityPrice("￥100-120");
                showInfo.setProdPrice("￥1800-2000");
                showInfo.setProdUrl("http://www.baidu.com");
                showInfo.setQrCodeHint("二维码会有惊喜");
                boolean success = QRCodeUtil.createShareWechatImg(TestAty.this, showInfo, filePath);
                if (success) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            qrShowImg.setImageBitmap(BitmapFactory
                                    .decodeFile(filePath));
                        }
                    });
                }
            }
        }).start();

    }

    // 文件存储根目录
    private String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }

        return context.getFilesDir().getAbsolutePath();
    }

    @Override
    public boolean isNoTitle() {
        return true;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }
}
