package com.digoshop.app.module.shopdetailnew.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.module.shopdetail.bigimg.aty.ImagePagerActivity;
import com.digoshop.app.module.shopdetailnew.adp.ShopEnvAdp;
import com.digoshop.app.module.shopdetailnew.model.ShopDetailNData;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.app.module.shopdetail.bigimg.aty.ImagePagerActivity.EXTRA_IMAGE_URLS;
import static com.digoshop.app.utils.Tool.getNetWifi;

public class ShopMesstFragment extends Fragment {

    private ShopDetailNData detailNData;
    private String sid;
    ArrayList<String> shopenvs;
    private ListView lv_shop_env;
    int heightiv;
    private RelativeLayout re_nocouponlist;
    private ScrollView id_stickynavlayout_innerscrollview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.shopmess_fragment, null);
        Bundle args = getArguments();
        sid = args != null ? args.getString("sid") : "";
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        heightiv = screenWidth * 2 / 3;
        id_stickynavlayout_innerscrollview = (ScrollView) view.findViewById(R.id.id_stickynavlayout_innerscrollview);
        re_nocouponlist = (RelativeLayout) view.findViewById(R.id.re_nocouponlist);
        lv_shop_env = (ListView) view.findViewById(R.id.lv_shopmessage_lv);
        lv_shop_env.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (shopenvs != null) {
                    if (shopenvs.size() > 0) {
                        Intent intenta = new Intent(getActivity(), ImagePagerActivity.class);
                        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                        intenta.putExtra(EXTRA_IMAGE_URLS, shopenvs);
                        intenta.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, i);
                        getActivity().startActivity(intenta);
                    }
                }

            }
        });
        if (getNetWifi()) {
            getShopDetailN();
        } else {
            App.getInstance().showToast("网络不给力，请检查网络设置");
        }
        return view;
    }

    private void getShopDetailN() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getShopDetailNApi();
            }
        }).start();
    }

    private void getShopDetailNApi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            detailNData = api.getShopEnv(sid);


            if (detailNData != null) {
                shopenvs = detailNData.getShopenvs();
                if (shopenvs != null) {
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(2);
                }
            } else {
                handler.sendEmptyMessage(2);
            }

        } catch (JSONException e) {
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        } catch (WSError e) {
            if ("A502".equals(e.getMessage())) {
                handler.sendEmptyMessage(502);
                return;
            }
            handler.sendEmptyMessage(2);
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if(shopenvs==null){
                        id_stickynavlayout_innerscrollview.setVisibility(View.GONE);
                    }
                    if(shopenvs!=null){
                        if(shopenvs.size()==0){
                            id_stickynavlayout_innerscrollview.setVisibility(View.GONE);
                        }else{
                            lv_shop_env.setAdapter(new ShopEnvAdp(getActivity(), shopenvs, heightiv));
                        }
                    }
                    re_nocouponlist.setVisibility(View.GONE);
                    StyledDialog.dismissLoading();
                    break;
                case 2:
                    if(shopenvs==null){
                        id_stickynavlayout_innerscrollview.setVisibility(View.GONE);
                    }
                    if(shopenvs!=null){
                        if(shopenvs.size()==0){
                            id_stickynavlayout_innerscrollview.setVisibility(View.GONE);
                        }
                    }
                    re_nocouponlist.setVisibility(View.VISIBLE);
                    StyledDialog.dismissLoading();
                    break;
                case 3:
                    App.getInstance().showToast("解析异常");
                    StyledDialog.dismissLoading();
                    break;
                case 4:
                    App.getInstance().showToast("请求异常");
                    StyledDialog.dismissLoading();
                    break;
                case 502:
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                    StyledDialog.dismissLoading();
                    break;

            }

        }
    };
    public String getTitle() {
        return "商家环境";
    }
}
