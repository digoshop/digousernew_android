package com.digoshop.app.module.looksales.xianshi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.looksales.model.FlashSaleListItemBean;
import com.digoshop.app.module.looksales.xianshi.PaiDetailsActivity;
import com.digoshop.app.module.looksales.xianshi.adp.FlashSaleslistAdp;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.http.WSError;

import org.json.JSONException;

import java.util.ArrayList;

import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.countdownview.DynamicConfig;

import static com.digoshop.app.utils.Tool.getNetWifi;

public class NewsFragment extends Fragment {
    String dateandhourstr;
    private String CityCodestr = "";
    private FlashSaleslistAdp flashSaleslistAdp;
    private ListView lv_falshlist;
    private ArrayList<FlashSaleListItemBean> periodList = new ArrayList<>();
    private ArrayList<FlashSaleListItemBean> periodListNew = new ArrayList<>();
    private PullToRefreshLayout ptrl;
    private int page = 1;
    private int page_length = 10;
    private RelativeLayout re_nolist;
    private TextView tv_coutdownstype;
    private CountdownView mCvCountdownViewTest4;
    private RelativeLayout re_newfragmettime;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Bundle args = getArguments();
        dateandhourstr = args != null ? args.getString("text") : "";
        Log.v("lsq", "onCreate1+" + dateandhourstr);
        Log.v("lsq", "onCreate3+" + periodListNew.size());
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.w("test", "www");
            if (getNetWifi()) {

                if (periodListNew != null) {
                    periodListNew.clear();
                }
                if (periodList != null) {
                    periodList.clear();
                }
                getFlashSalesList();
            }

        } else {

        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.news_fragment, null);
        re_nolist = (RelativeLayout) view.findViewById(R.id.re_nolist);
        tv_coutdownstype = (TextView) view.findViewById(R.id.tv_coutdownstype);
        re_newfragmettime= (RelativeLayout) view.findViewById(R.id.re_newfragmettime);
        CityCodestr = LocalSave.getValue(getActivity(), AppConfig.basefile, "CityCode", "");
        ptrl = ((PullToRefreshLayout) view.findViewById(R.id.refresh_view));
        lv_falshlist = (ListView) view.findViewById(R.id.lv_falshlist);
        mCvCountdownViewTest4 = (CountdownView) view.findViewById(R.id.cv_countdownViewTest4);
        mCvCountdownViewTest4.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                getFlashSalesList();
            }
        });
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (periodListNew.size() > 0) {
                    periodListNew.clear();
                }
                CityCodestr = LocalSave.getValue(getActivity(), AppConfig.basefile, "CityCode", "");
                page = 1;
                page_length = 10;
                getFlashSalesList();
                flashSaleslistAdp.notifyDataSetChanged();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

                if (periodList != null) {
                    if (periodList.size() > 0) {
                        periodList.clear();
                    }
                    page = page + 1;
                    getFlashSalesList();
                    flashSaleslistAdp.appendData(periodList);
                }
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }
        });
        lv_falshlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("pid", periodListNew.get(position).getPid());
                intent.putExtra("pt", periodListNew.get(position).getPt());
                intent.putExtra("aps", periodListNew.get(position).getAps());
                intent.setClass(getActivity(), PaiDetailsActivity.class);
                startActivity(intent);
            }
        });
        if (periodListNew != null) {
            periodListNew.clear();
        }
        if (periodList != null) {
            periodList.clear();
        }
        flashSaleslistAdp = new FlashSaleslistAdp(getActivity(), periodListNew);
        lv_falshlist.setAdapter(flashSaleslistAdp);
        getFlashSalesList();
        Log.v("test", "onCreateView+" + periodListNew.size());
        return view;
    }


    private void getFlashSalesList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getFlashSalesListApi();
            }
        }).start();
    }

    //竞拍商品列表
    public void getFlashSalesListApi() {

        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            periodList = api.getPeriodList("1", dateandhourstr, CityCodestr, page + "", "" + page_length);
            if (periodList != null) {
                handler.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(2);
            }
        } catch (WSError wsError) {
            handler.sendEmptyMessage(2);
            wsError.printStackTrace();
        } catch (JSONException e) {
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        }


    }
    boolean isview = false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if(periodListNew!=null&periodList!=null){
                        if(periodListNew.size()==0&periodList.size()==0){
                            re_nolist.setVisibility(View.VISIBLE);
                            re_newfragmettime.setVisibility(View.GONE);
                        }else{
                            re_newfragmettime.setVisibility(View.VISIBLE);
                            re_nolist.setVisibility(View.GONE);
                        }
                    }
                    periodListNew.addAll(periodList);
                    if(periodListNew!=null){
                        Log.v("text",":+111+++"+periodListNew.size());
                        if(periodListNew.size()>0){
                            long nte = periodListNew.get(0).getNte();
                            long apsd = periodListNew.get(0).getApsd();
                            long aped = periodListNew.get(0).getAped();
                            long daojishitime = 0;
                            if (nte < apsd) {
                                tv_coutdownstype.setText("距离开始: ");
                                daojishitime = (apsd - nte)  ;
                            } else if (nte > apsd & nte < aped) {
                                tv_coutdownstype.setText("距离结束: ");
                                daojishitime = (aped - nte)  ;
                            } else if (nte > aped) {
                                tv_coutdownstype.setText("已结束 ");
                                daojishitime = 0;
                            }
                            boolean daytrue = false;
                            if(daojishitime>24*60*60){
                                daytrue = true;
                            }
                            boolean hourtrue = false;
                            if(daojishitime>60*60){
                                hourtrue = true;
                            }
                            DynamicConfig.Builder dynamicConfigBuilder = new DynamicConfig.Builder();
                            dynamicConfigBuilder.setShowDay(daytrue)
                                    .setShowHour(hourtrue)
                                    .setShowMinute(true)
                                    .setShowSecond(true)
                                    .setShowMillisecond(false);
                            if(daojishitime!=0){
                                re_newfragmettime.setVisibility(View.VISIBLE);
                                mCvCountdownViewTest4.setVisibility(View.VISIBLE);
                                mCvCountdownViewTest4.dynamicShow(dynamicConfigBuilder.build());
                                mCvCountdownViewTest4.start(daojishitime*1000);
                            }else{
                                mCvCountdownViewTest4.setVisibility(View.GONE);
                            }
                        }
                    }
                    flashSaleslistAdp.notifyDataSetChanged();
                    break;
                case 2:
                    if(periodListNew!=null&periodList!=null){
                        if(periodListNew.size()==0&periodList.size()==0){
                            re_nolist.setVisibility(View.VISIBLE);
                        }else{
                            re_nolist.setVisibility(View.GONE);
                        }
                    }
                    //  App.getInstance().showToast("暂无数据!");
                    ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    break;
                case 3:
                    App.getInstance().showToast("解析异常!");
                    break;
            }
        }
    };
}
