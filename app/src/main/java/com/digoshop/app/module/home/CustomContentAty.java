package com.digoshop.app.module.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIProductApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.customServices.CustomServiceActivity;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.http.WSError;

import org.json.JSONException;

public class CustomContentAty extends BaseActivity {

    @Override
    public boolean isNoTitle() {
        return true;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }
    private int screenWidth;
    private ImageView iv_c_bg;
    private LinearLayout lin_jieshao;
    private TextView tv_c_caozuo;
    private View v_fuwu,v_caowu;
    private TextView tv_customnum;
    private String CityCodestr = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_content_aty);
        CityCodestr = LocalSave.getValue(CustomContentAty.this, AppConfig.basefile, "CityCode", "");

        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("定制服务");
        tv_customnum = (TextView) findViewById(R.id.tv_customnum);
        v_fuwu =findViewById(R.id.v_fuwu);
        v_caowu = findViewById(R.id.v_caozuo);
        lin_jieshao  = (LinearLayout) findViewById(R.id.lin_jieshao);
        tv_c_caozuo = (TextView) findViewById(R.id.tv_c_caozuo);
        iv_c_bg = (ImageView) findViewById(R.id.iv_c_bg);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        ViewGroup.LayoutParams params = iv_c_bg.getLayoutParams();
        params.height = screenWidth / 2 ;
        params.width = screenWidth;
        iv_c_bg.setLayoutParams(params);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getShops();
    }

    private void getShops() {
        if (TextUtils.isEmpty(CityCodestr)) {
            App.getInstance().showToast("定位获取失败,默认北京");
            CityCodestr = "010";
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                getshopapi();
            }
        }).start();

    }
    private String count;
    private void getshopapi() {
        DigoIProductApiImpl api = new DigoIProductApiImpl();
        try {
            count = api.getCustomNum(CityCodestr);

            if (count != null) {

                handler.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(2);
            }

        } catch (JSONException e) {
            handler.sendEmptyMessage(2);
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(2);
            e.printStackTrace();
        }
    }
    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    tv_customnum.setText(count);
                    break;
                case 2:
                    tv_customnum.setText("获取失败!");
                    break;

            }

        }
    };
    public void refuwujieshao(View v){
        lin_jieshao.setVisibility(View.VISIBLE);
        tv_c_caozuo.setVisibility(View.GONE);
        v_fuwu.setVisibility(View.VISIBLE);
         v_caowu.setVisibility(View.INVISIBLE);

    }
    public void recaozuoliucheng(View v){
        v_fuwu.setVisibility(View.INVISIBLE);
        v_caowu.setVisibility(View.VISIBLE);
        lin_jieshao.setVisibility(View.GONE);
        tv_c_caozuo.setVisibility(View.VISIBLE);
    }

    public  void btncschanpin (View  v){

            Intent intent  = new Intent();
            intent.putExtra("cstype","2");
            intent.setClass(CustomContentAty.this, CustomServiceActivity.class);
            startActivity(intent);



    }
    public  void btncsfuwu (View  v){

            Intent intent  = new Intent();
            intent.putExtra("cstype","1");
            intent.setClass(CustomContentAty.this, CustomServiceActivity.class);
            startActivity(intent);


    }
}
