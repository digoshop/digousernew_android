package com.digoshop.app;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.widget.Toast;

import com.digoshop.R;
import com.digoshop.app.base.BaseApplication;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.CrashHandler;
import com.digoshop.app.utils.LocalSave;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class App extends BaseApplication {

    static App instance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public int getSeekAd() {
        int ih = getResources().getDimensionPixelOffset(R.dimen.base_dimen_20);

        //  float ih = (float) getResources().getDimensionPixelSize(R.dimen.base_dimen_95);
        return ih;
    }

    public float getShopCateWeight() {
        float ih = (float) getResources().getDimensionPixelOffset(R.dimen.base_dimen_260);

        //  float ih = (float) getResources().getDimensionPixelSize(R.dimen.base_dimen_95);
        return ih;
    }

    public float getSelectHeight() {
        //       float ih = (float) getResources().getDimension(R.dimen.base_dimen_95);
        float ih = (float) getResources().getDimensionPixelOffset(R.dimen.base_dimen_95);

        //  float ih = (float) getResources().getDimensionPixelSize(R.dimen.base_dimen_95);
        return ih;
    }

    public float getCouponHeight() {
        //       float ih = (float) getResources().getDimension(R.dimen.base_dimen_95);
        float ih = (float) getResources().getDimensionPixelOffset(R.dimen.base_dimen_97);

        //  float ih = (float) getResources().getDimensionPixelSize(R.dimen.base_dimen_95);
        return ih;
    }

    @Override
    public void onCreate() {
        super.onCreate();
           /* Bugly SDK初始化
        * 参数1：上下文对象
        * 参数2：APPID，平台注册时得到,注意替换成你的appId
        * 参数3：是否开启调试模式，调试模式下会输出'CrashReport'tag的日志
        */
//        boolean isfabao = false;
//        if (true) {
        CrashReport.initCrashReport(getApplicationContext(), "afca0d8aa8", true);
//        } else {
//            crashLogo();
//        }
        instance = this;
        initImageLoader();
        UMShareAPI.get(this);

//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
////                .addInterceptor(new LoggerInterceptor("TAG"))
//                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
//                .readTimeout(10000L, TimeUnit.MILLISECONDS)
//                //其他配置
//                .build();
//
//        OkHttpUtils.initClient(okHttpClient);
        //高德地图sha
        //   Log.v("ceshi", "sHA1+" + sHA1(getApplicationContext()));
    }

    // 各个平台的配置，建议放在全局Application或者程序入口
    {
        // 微信
        PlatformConfig.setWeixin("wx5613b329728b79f2",
                "38c4e26a8655294b5fbe59bfe273ef45");
    }

    /**
     * 输出错误日志
     */
    private void crashLogo() {
        CrashHandler catchHandler = CrashHandler.getInstance();
        catchHandler.init(getApplicationContext());
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /**
     * imageloader 初始化数据
     */
    private void initImageLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)

                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)

                .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，图片太多就这这个。还有其他设置
                // 如Bitmap.Config.ARGB_8888
                .showImageOnLoading(R.drawable.no_big_png) // 默认图片
                .showImageForEmptyUri(R.drawable.no_big_png) // url爲空會显示该图片，自己放在drawable里面的
                .showImageOnFail(R.drawable.no_big_png)// 加载失败显示的图片
                // .displayer(new RoundedBitmapDisplayer(5)) //圆角，不需要请删除
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(

                this)
                .memoryCacheExtraOptions(480, 800)// 缓存在内存的图片的宽和高度

                .discCacheExtraOptions(480, 800, CompressFormat.PNG, 70, null) // CompressFormat.PNG类型，70质量（0-100）

                .memoryCache(new WeakMemoryCache())

                .memoryCacheSize(2 * 1024 * 1024) // 缓存到内存的最大数据

                .discCacheSize(50 * 1024 * 1024). // 缓存到文件的最大数据

                discCacheFileCount(1000) // 文件数量

                .defaultDisplayImageOptions(options). // 上面的options对象，一些属性配置

                build();
        ImageLoader.getInstance().init(config); // 初始化
    }


    public static App getInstance() {
        return instance;
    }

    public String getUserid() {
        return LocalSave.getValue(App.getInstance(), AppConfig.basefile, "userid", "");
    }


    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
