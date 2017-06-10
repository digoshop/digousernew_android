package com.digoshop.app.module.userCenter.module;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseFragment;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.login.Loginaty;
import com.digoshop.app.module.product.ProductExchangeAty;
import com.digoshop.app.module.shopdetailnew.adp.ShopAllProductAdp;
import com.digoshop.app.module.shopdetailnew.model.ShopProduct;
import com.digoshop.app.module.userCenter.adp.MyGiftAdp;
import com.digoshop.app.module.userCenter.model.MybagEntity;
import com.digoshop.app.module.userCenter.model.MybagEntityData;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;


public class MyShopListFragment extends BaseFragment implements View.OnClickListener {
    private ListView lv_mygift;
    private ArrayList<MybagEntity> MbeList;
    private ArrayList<MybagEntity> MbeListNew = new ArrayList<>();
    private MyGiftAdp myGiftAdp;

    private PullToRefreshLayout ptrl, ptrlgrid;
    private int page = 1;
    private int page_length = 10;
    private String st;
    private RelativeLayout re_nomycouponlist;
    private TextView tv_shoplistlogin;
    private RadioButton text_homeaty_titlea, text_homeaty_titleb, text_homeaty_titlec;
    private View v_mypro_line;
    private LinearLayout lin_myshop_title;
    private RelativeLayout re_mypro_islogin, re_myprodecut_tuijian;
    private ShopAllProductAdp flashSaleslistAdp;
    private GridView lv_citycouponlist;
    private ArrayList<ShopProduct> couponInfos = new ArrayList<>();
    private ArrayList<ShopProduct> couponInfosNew = new ArrayList<>();
    private String CityCodestr = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_shop_list, null);
        TextView tv_title_content = (TextView) v.findViewById(R.id.tv_title_content);
        tv_title_content.setText("我的商品");
        v_mypro_line = v.findViewById(R.id.v_mypro_line);
        re_mypro_islogin = (RelativeLayout) v.findViewById(R.id.re_mypro_islogin);
        re_myprodecut_tuijian = (RelativeLayout) v.findViewById(R.id.re_myprodecut_tuijian);


        lin_myshop_title = (LinearLayout) v.findViewById(R.id.lin_myshop_title);
        text_homeaty_titlea = (RadioButton) v.findViewById(R.id.text_homeaty_titlea);
        text_homeaty_titleb = (RadioButton) v.findViewById(R.id.text_homeaty_titleb);
        text_homeaty_titlec = (RadioButton) v.findViewById(R.id.text_homeaty_titlec);
        text_homeaty_titlea.setOnClickListener(this);
        text_homeaty_titlea.setChecked(true);
        text_homeaty_titleb.setOnClickListener(this);
        text_homeaty_titlec.setOnClickListener(this);
        CityCodestr = LocalSave.getValue(getActivity(), AppConfig.basefile, "CityCode", "");
        re_nomycouponlist = (RelativeLayout) v.findViewById(R.id.re_nomycouponlist);
        ImageView iv_title_return = (ImageView) v.findViewById(R.id.iv_title_return);
        iv_title_return.setVisibility(View.GONE);
        ImageView iv_title_right = (ImageView) v.findViewById(R.id.iv_title_right);
        iv_title_right.setBackgroundResource(R.drawable.grzx_quan);
        iv_title_right.setBackgroundResource(R.drawable.l_juan);
        iv_title_right.setVisibility(View.VISIBLE);

        iv_title_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    intent.setClass(getActivity(), MyCouponActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(getActivity(), Loginaty.class);
                    startActivity(intent);
                }
            }
        });
        ptrlgrid = ((PullToRefreshLayout) v.findViewById(R.id.refresh_store_view));
        lv_citycouponlist = (GridView) v.findViewById(R.id.gv_saleactivity);
        flashSaleslistAdp = new ShopAllProductAdp(getActivity(), couponInfosNew, 1);
        lv_citycouponlist.setAdapter(flashSaleslistAdp);
        ptrlgrid.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (couponInfosNew.size() > 0) {
                    couponInfosNew.clear();
                }
                CityCodestr = LocalSave.getValue(getActivity(), AppConfig.basefile, "CityCode", "");
                page = 1;
                page_length = 10;
                getexchangeList();
                flashSaleslistAdp.notifyDataSetChanged();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

                if (couponInfos != null) {
                    if (couponInfos.size() > 0) {
                        couponInfos.clear();
                    }
                    page = page + 1;
                    getexchangeList();
                    flashSaleslistAdp.appendData(couponInfos);
                }
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }
        });
        lv_citycouponlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(),
                        ProductExchangeAty.class);
                intent.putExtra("pid", couponInfosNew.get(position)
                        .getPid());
                intent.putExtra("pt", "2");
                startActivity(intent);


            }
        });


        lv_mygift = (ListView) v.findViewById(R.id.lv_mygift);
        ptrl = ((PullToRefreshLayout) v.findViewById(R.id.refresh_view));
        tv_shoplistlogin = (TextView) v.findViewById(R.id.tv_shoplistlogin);
        tv_shoplistlogin.setOnClickListener(this);


        myGiftAdp = new MyGiftAdp(getActivity(), MbeListNew);
        lv_mygift.setAdapter(myGiftAdp);
        lv_mygift.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("pid", MbeListNew.get(i).getPid()
                );
                intent.putExtra("pt", "2");
                intent.setClass(getActivity(), ProductExchangeAty.class);
                startActivity(intent);
            }
        });
        refresh();
        return v;
    }

    private void getexchangeList() {
        if (TextUtils.isEmpty(CityCodestr)) {
            CityCodestr = "010";
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                getexchangeListApi();
            }
        }).start();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.v("ceshi", "user");
        } else {
            if (getNetWifi()) {
                String isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                Log.v("ceshi", "onResume99");
                CityCodestr = LocalSave.getValue(getActivity(), AppConfig.basefile, "CityCode", "");
                if (!"true".equals(isusertype)) {
                    ptrl.setVisibility(View.GONE);
                    v_mypro_line.setVisibility(View.GONE);
                    lin_myshop_title.setVisibility(View.GONE);
                    re_mypro_islogin.setVisibility(View.VISIBLE);
                    re_myprodecut_tuijian.setVisibility(View.VISIBLE);
                    ptrlgrid.setVisibility(View.VISIBLE);
                    if (flashSaleslistAdp != null) {
                        flashSaleslistAdp.notifyDataSetChanged();
                    }
//                    if (getNetWifi()) {
//                        if (couponInfos != null) {
//                            couponInfos.clear();
//                        }
//                        if(flashSaleslistAdp!=null){
//                            flashSaleslistAdp.notifyDataSetChanged();
//                        }
//                        getexchangeList();
//                    }
                    if (MbeListNew.size() > 0) {
                        MbeListNew.clear();
                    }
                    page = 1;
                    if (myGiftAdp != null) {
                        myGiftAdp.notifyDataSetChanged();
                    }
                } else {
                    ptrl.setVisibility(View.VISIBLE);
                    v_mypro_line.setVisibility(View.VISIBLE);
                    lin_myshop_title.setVisibility(View.VISIBLE);
                    re_mypro_islogin.setVisibility(View.GONE);
                    re_myprodecut_tuijian.setVisibility(View.GONE);
                    ptrlgrid.setVisibility(View.GONE);
                    if (getNetWifi()) {
                        st = "0";
                        text_homeaty_titlea.setChecked(true);
                        text_homeaty_titleb.setChecked(false);
                        text_homeaty_titlec.setChecked(false);
                        if (MbeListNew.size() > 0) {
                            MbeListNew.clear();
                        }
                        page = 1;
                        page_length = 10;
                        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                        get_Mybag();
                    } else {
                        App.getInstance().showToast("网络不给力，请检查网络设置");
                    }
                }
            } else {
                App.getInstance().showToast("网络不给力，请检查网络设置");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
        Log.v("ceshi", "onResume88");
        if (!"true".equals(isusertype)) {
            v_mypro_line.setVisibility(View.GONE);
            lin_myshop_title.setVisibility(View.GONE);
            ptrl.setVisibility(View.GONE);
            re_mypro_islogin.setVisibility(View.VISIBLE);
            re_myprodecut_tuijian.setVisibility(View.VISIBLE);
            ptrlgrid.setVisibility(View.VISIBLE);
            if (getNetWifi()) {
                if (couponInfos != null) {
                    couponInfos.clear();
                }
                if (couponInfosNew != null) {
                    couponInfosNew.clear();
                }
                if (flashSaleslistAdp != null) {
                    flashSaleslistAdp.notifyDataSetChanged();
                }
                getexchangeList();
            }

            if (MbeListNew.size() > 0) {
                MbeListNew.clear();
            }
            page = 1;
            if (myGiftAdp != null) {
                myGiftAdp.notifyDataSetChanged();
            }

        } else {
            ptrl.setVisibility(View.VISIBLE);
            v_mypro_line.setVisibility(View.VISIBLE);
            lin_myshop_title.setVisibility(View.VISIBLE);
            re_mypro_islogin.setVisibility(View.GONE);
            re_myprodecut_tuijian.setVisibility(View.GONE);
            ptrlgrid.setVisibility(View.GONE);
            if (getNetWifi()) {
                st = "0";
                text_homeaty_titlea.setChecked(true);
                text_homeaty_titleb.setChecked(false);
                text_homeaty_titlec.setChecked(false);
                if (MbeListNew.size() > 0) {
                    MbeListNew.clear();
                }
                page = 1;
                page_length = 10;
                get_Mybag();
            } else {
                App.getInstance().showToast("网络不给力，请检查网络设置");
            }
        }
    }

    private void get_Mybag() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                get_MybagWall();
            }
        }).start();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    MbeListNew.addAll(MbeList);
                    myGiftAdp.notifyDataSetChanged();
                    StyledDialog.dismissLoading();
                    if (re_nomycouponlist != null) {
                        re_nomycouponlist.setVisibility(View.GONE);
                    }
                    if (MbeList != null) {
                        if (MbeListNew.size() == 0 & MbeList.size() == 0) {
                            re_nomycouponlist.setVisibility(View.VISIBLE);
                        }
                        if (MbeList.size() == 0 & MbeListNew.size() != 0) {
                            App.getInstance().showToast("没有数据了!");
                        }
                    }

                    text_homeaty_titlea.setText("未结算(" + notReceivenum + ")");
                    text_homeaty_titleb.setText("已结算(" + receivenum + ")");
                    text_homeaty_titlec.setText("已过期(" + expirednum + ")");
                    break;
                case 2:
                    if (MbeList != null) {
                        if (MbeListNew != null) {
                            if (MbeListNew.size() > 0) {
                                App.getInstance().showToast("没有数据了!");
                            } else {
                                re_nomycouponlist.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        if (MbeListNew.size() > 0) {
                            App.getInstance().showToast("没有数据了!");
                        } else {
                            re_nomycouponlist.setVisibility(View.VISIBLE);
                        }

                    }
                    StyledDialog.dismissLoading();
                    text_homeaty_titlea.setText("未结算(" + notReceivenum + ")");
                    text_homeaty_titleb.setText("已结算(" + receivenum + ")");
                    text_homeaty_titlec.setText("已过期(" + expirednum + ")");
                    Log.v("ceshi", "notReceivenum+" + notReceivenum);

                    break;
                case 3:
                    App.getInstance().showToast("解析异常");
                    StyledDialog.dismissLoading();

                    break;
                case 4:
                    App.getInstance().showToast("请求异常");
                    StyledDialog.dismissLoading();

                    break;
                case 21:
                    ptrlgrid.setVisibility(View.VISIBLE);
                    if (couponInfosNew != null & couponInfos != null) {
                        if (couponInfosNew.size() == 0 & couponInfos.size() == 0) {
                            ptrlgrid.setVisibility(View.GONE);
                        } else {
                            ptrlgrid.setVisibility(View.VISIBLE);
                        }
                    }
                    couponInfosNew.addAll(couponInfos);
                    flashSaleslistAdp.notifyDataSetChanged();
                    StyledDialog.dismissLoading();
                    break;

                case 22:
                    if (couponInfosNew != null & couponInfos != null) {
                        if (couponInfosNew.size() > 0 & couponInfos.size() == 0) {
                            App.getInstance().showToast("已是最后一页");
                        }
                        if (couponInfosNew.size() == 0 & couponInfos.size() == 0) {
                            //第一次进来的时候没一个
                            ptrlgrid.setVisibility(View.GONE);
                        } else {
                            ptrlgrid.setVisibility(View.VISIBLE);
                        }
                        if (couponInfosNew.size() == 0) {
                            ptrlgrid.setVisibility(View.GONE);
                        }
                    }
                    //  App.getInstance().showToast("暂无数据!");
                    try {
                        if (ptrlgrid != null) {
                            ptrlgrid.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    StyledDialog.dismissLoading();
                    break;

                case 23:
                    App.getInstance().showToast("解析异常!");
                    StyledDialog.dismissLoading();
                    break;
            }
        }
    };
    private MybagEntityData mybagEntityData;
    //        public static final int STATUS_ALL   = -1;  // 全部
//        public static final int STATUS_NO     = 0;    // 未结算
//        public static final int STATUS_YES = 1;       // 已结算
//        public static final int STATUS_EXPIRED     = 2;    // 已过期
    private String receivenum = "0";
    private String expirednum = "0";
    private String notReceivenum = "0";

    private void getexchangeListApi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            couponInfos = api.get_MyProductNotLogged(CityCodestr, "RC_MYPRODUCT_NOTLOGGED", page + "");
            if (couponInfos != null) {
                handler.sendEmptyMessage(21);
            } else {
                handler.sendEmptyMessage(22);
            }

        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            handler.sendEmptyMessage(23);
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(22);
            Log.v("ceshi", "WSError");
            e.printStackTrace();
        }
    }

    private void get_MybagWall() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        MbeList = new ArrayList<MybagEntity>();
        try {
            mybagEntityData = api.getMybagWall(page + "", page_length + "", st);
            if (mybagEntityData != null) {
                Log.v("ceshi", "mybagEntityData.getNotReceive()" + mybagEntityData.getNotReceive());

                if (TextUtils.isEmpty(mybagEntityData.getReceive())) {
                    receivenum = "0";
                } else {
                    receivenum = mybagEntityData.getReceive();
                }
                if (TextUtils.isEmpty(mybagEntityData.getExpired())) {
                    expirednum = "0";
                } else {
                    expirednum = mybagEntityData.getExpired();
                }
                if (TextUtils.isEmpty(mybagEntityData.getNotReceive())) {
                    notReceivenum = "0";
                } else {
                    notReceivenum = mybagEntityData.getNotReceive();
                }
                MbeList = mybagEntityData.getMybagEntities();
                if (MbeList.size() > 0) {
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(2);
                }

            } else {
                handler.sendEmptyMessage(2);
            }
        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(2);
            Log.v("ceshi", "WSError");
            e.printStackTrace();
        }
    }

    public void refresh() {
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (getNetWifi()) {
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                    if (MbeListNew.size() > 0) {
                        MbeListNew.clear();
                    }
                    page = 1;
                    page_length = 10;
                    get_Mybag();
                    myGiftAdp.notifyDataSetChanged();
                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }


                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (getNetWifi()) {
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                    if (MbeList != null) {
                        if (MbeList.size() > 0) {
                            MbeList.clear();
                        }
                        page = page + 1;
                        page_length = 10;
                        get_Mybag();
                        myGiftAdp.appendData(MbeList);
                    }
                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }


                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }
        });

    }


    @Override
    public void onClick(View view) {
        //用户登录状态，登录成功未true ,默认未false
        AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
        if ("true".equals(AppConfig.isusertype)) {
        } else {
            Intent intent = new Intent();
            intent.setClass(getActivity(), Loginaty.class);
            startActivity(intent);
            return;
        }
        switch (view.getId()) {
            //= 0未结算    1已结算   2已过期
            case R.id.text_homeaty_titlea:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    text_homeaty_titlea.setChecked(true);
                    text_homeaty_titleb.setChecked(false);
                    text_homeaty_titlec.setChecked(false);
                    st = "0";
                    page = 1;
                    if (MbeListNew.size() > 0) {
                        MbeListNew.clear();
                    }
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

                    get_Mybag();
                    myGiftAdp.notifyDataSetChanged();
                } else {

                }

                break;
            case R.id.text_homeaty_titleb:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    st = "1";
                    text_homeaty_titlea.setChecked(false);
                    text_homeaty_titleb.setChecked(true);
                    text_homeaty_titlec.setChecked(false);
                    page = 1;
                    if (MbeListNew.size() > 0) {
                        MbeListNew.clear();
                    }
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

                    get_Mybag();
                    myGiftAdp.notifyDataSetChanged();
                } else {

                }


                break;
            case R.id.text_homeaty_titlec:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    text_homeaty_titlea.setChecked(false);
                    text_homeaty_titleb.setChecked(false);
                    text_homeaty_titlec.setChecked(true);
                    st = "2";
                    page = 1;
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

                    if (MbeListNew.size() > 0) {
                        MbeListNew.clear();
                    }
                    get_Mybag();
                    myGiftAdp.notifyDataSetChanged();
                } else {

                }


                break;
            case R.id.tv_shoplistlogin:
                Intent intent = new Intent();
                intent.setClass(getActivity(), Loginaty.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

}
