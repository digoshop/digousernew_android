package com.digoshop.app.module.arrountshops;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIProductApiImpl;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.module.arrountshops.adp.ArountShop_textgridTypeAdp;
import com.digoshop.app.module.arrountshops.adp.ArroutShopAdp;
import com.digoshop.app.module.arrountshops.model.ArrountitemBean;
import com.digoshop.app.module.arrountshops.model.MyArrayListCityBean;
import com.digoshop.app.module.arrountshops.model.ShopInfoBean;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.shopdetailnew.ShopDetailNewSNActivity;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;

public class ArountListFragment extends Fragment {
    private ArroutShopAdp flashSaleslistAdp;
    private ListView lv_citycouponlist;
    private ArrayList<ShopInfoBean> couponInfos = new ArrayList<>();
    private ArrayList<ShopInfoBean> couponInfosNew = new ArrayList<>();

    private PullToRefreshLayout ptrl;
    private int page = 1;
    private int page_length = 10;
    private RelativeLayout re_nolist;
    private String CityCodestr = "";
    private String latstr = "";
    private String logstr = "";
    private ArrayList<MyArrayListCityBean> myArrayListCityBeens;
    private GridView gv_sale_type;
    private String tagname = "";

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.v("ceshi", "www");
            if (getNetWifi()) {

                if (couponInfosNew != null) {
                    couponInfosNew.clear();
                }
                if (couponInfos != null) {
                    couponInfos.clear();
                }
                if (typeapi == 0) {
                    getShops();
                } else {
                    getHotshop();
                }
            }

        } else {
//            String    linCityCodestr = LocalSave.getValue(getActivity(), AppConfig.basefile, "CityCode", "");
//            if(!TextUtils.isEmpty(linCityCodestr)){
//                if(!linCityCodestr.equals(CityCodestr)){
//                    if (getNetWifi()) {
//                        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
//                        CityCodestr = linCityCodestr;
//                        if (getNetWifi()) {
//
//                            if (couponInfosNew != null) {
//                                couponInfosNew.clear();
//                            }
//                            if (couponInfos != null) {
//                                couponInfos.clear();
//                            }
//                            if(typeapi==0){
//                                getShops();
//                            }else{
//                                getHotshop();
//                            }
//                        }
//
//                    } else {
//                        App.getInstance().showToast("网络不给力，请检查网络设置");
//                    }
//                }
//            }
            Log.v("ceshi", "www222");
        }
    }

    int heightiv;
    ArrayList<ArrountitemBean> arrountitemBeensNEW;
    ArountShop_textgridTypeAdp arountShop_textgridTypeAdp;
    private int typeapi = 0;//0位getshop接口，1为hot接口；

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.arounts_fragment, null);
        Bundle args = getArguments();
        arrountitemBeensNEW = (ArrayList<ArrountitemBean>) args.getSerializable("list");
        re_nolist = (RelativeLayout) view.findViewById(R.id.re_nolist);
        CityCodestr = LocalSave.getValue(getActivity(), AppConfig.basefile, "CityCode", "");
        latstr = LocalSave.getValue(getActivity(), AppConfig.basefile, "lat", "");
        logstr = LocalSave.getValue(getActivity(), AppConfig.basefile, "lon", "");

        ptrl = ((PullToRefreshLayout) view.findViewById(R.id.refresh_view));
        lv_citycouponlist = (ListView) view.findViewById(R.id.arountshops_lv_shops);

        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (couponInfosNew.size() > 0) {
                    couponInfosNew.clear();
                }
                CityCodestr = LocalSave.getValue(getActivity(), AppConfig.basefile, "CityCode", "");
                page = 1;
                page_length = 10;
                if (typeapi == 0) {
                    getShops();
                } else {
                    getHotshop();
                }
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
                    if (typeapi == 0) {
                        getShops();
                    } else {
                        getHotshop();
                    }
                    flashSaleslistAdp.appendData(couponInfos);
                }
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }
        });
        lv_citycouponlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("sid", couponInfosNew.get(position).getSid());
                intent.putExtra("gpsjui", Tool.getGpskmorm(couponInfosNew.get(position).getDistance()));
                intent.setClass(getActivity(), ShopDetailNewSNActivity.class);

                startActivity(intent);


            }
        });
        if (couponInfosNew != null) {
            couponInfosNew.clear();
        }
        if (couponInfos != null) {
            couponInfos.clear();
        }

        flashSaleslistAdp = new ArroutShopAdp(getActivity(), couponInfosNew);
        lv_citycouponlist.setAdapter(flashSaleslistAdp);
        gv_sale_type = (GridView) view.findViewById(R.id.gv_arountshop_type);
        gv_sale_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (getNetWifi()) {

                    if (couponInfosNew != null) {
                        couponInfosNew.clear();
                    }
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                    page = 1;
                    arountShop_textgridTypeAdp.setChoicePos(i);
                    arountShop_textgridTypeAdp.notifyDataSetChanged();
                    if (arrountitemBeensNEW != null) {
                        if (arrountitemBeensNEW.size() > 0) {
                            if ("0".equals(arrountitemBeensNEW.get(i).getTag())) {
                                moidstr = arrountitemBeensNEW.get(i).getMoid();
                                typeapi = 0;
                                getShops();
                            } else {
                                typeapi = 1;
                                tagname = arrountitemBeensNEW.get(i).getTagName();
                                Log.v("ceshi", "###" + tagname);
                                getHotshop();
                            }
                        } else {
                            typeapi = 0;
                            moidstr = "";
                            getShops();
                        }
                    } else {
                        typeapi = 0;
                        moidstr = "";
                        getShops();
                    }
                    flashSaleslistAdp.notifyDataSetChanged();
                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }
            }
        });

        if (arrountitemBeensNEW != null) {
            if (arrountitemBeensNEW.size() > 0) {
                if ("0".equals(arrountitemBeensNEW.get(0).getTag())) {
                    moidstr = arrountitemBeensNEW.get(0).getMoid();
                    typeapi = 0;
                    getShops();
                } else {
                    typeapi = 1;
                    tagname = arrountitemBeensNEW.get(0).getTagName();
                    Log.v("ceshi", "###" + tagname);
                    getHotshop();
                }
            } else {
                typeapi = 0;
                moidstr = "";
                getShops();
            }
        } else {
            typeapi = 0;
            moidstr = "";
            getShops();
        }
//        if (typeapi == 1) {
            if (arrountitemBeensNEW != null) {
                if (arrountitemBeensNEW.size() > 0) {
                    arountShop_textgridTypeAdp = new ArountShop_textgridTypeAdp(getActivity(), arrountitemBeensNEW);
                    gv_sale_type.setAdapter(arountShop_textgridTypeAdp);
                    arountShop_textgridTypeAdp.setChoicePos(0);
                    arountShop_textgridTypeAdp.notifyDataSetChanged();
                }
            }
//        }


        return view;
    }

    private String moidstr = "";

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

    private void getHotshop() {
        if (TextUtils.isEmpty(CityCodestr)) {
            App.getInstance().showToast("定位获取失败,默认北京");
            CityCodestr = "010";
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                getHotshopapi();
            }
        }).start();

    }

    private void getshopapi() {
        DigoIProductApiImpl api = new DigoIProductApiImpl();
        try {
            couponInfos = api.ArountShops(moidstr, "0", CityCodestr, "", "", "", latstr, logstr, page + "", page_length + "");

            if (couponInfos != null) {
                if (couponInfos.size() > 0) {
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(2);
                }
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

    private void getHotshopapi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            couponInfos = api.getArountHotdShop(CityCodestr, page + "", tagname);
            if (couponInfos != null) {

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

    boolean isview = false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    ptrl.setVisibility(View.VISIBLE);
                    if (couponInfosNew != null & couponInfos != null) {
                        if (couponInfosNew.size() == 0 & couponInfos.size() == 0) {
                            re_nolist.setVisibility(View.VISIBLE);
                        } else {
                            re_nolist.setVisibility(View.GONE);
                        }
                    }
                    couponInfosNew.addAll(couponInfos);
                    flashSaleslistAdp.notifyDataSetChanged();
                    StyledDialog.dismissLoading();

                    break;
                case 2:
                    Log.v("ceshi", "2222222222");
                    Log.v("ceshi", "couponInfosNew.size()+" + couponInfosNew);
                    Log.v("ceshi", "couponInfos.size()" + couponInfos);
                    if (couponInfosNew != null & couponInfos != null) {
                        if (couponInfosNew.size() == 0 & couponInfos.size() == 0) {
                            //第一次进来的时候没一个
                            re_nolist.setVisibility(View.VISIBLE);
                            ptrl.setVisibility(View.GONE);
                        } else {
                            ptrl.setVisibility(View.VISIBLE);
                            re_nolist.setVisibility(View.GONE);
                        }
                        if (couponInfosNew.size() == 0) {
                            ptrl.setVisibility(View.GONE);
                            re_nolist.setVisibility(View.VISIBLE);
                        }
                    }
                    if (couponInfosNew != null) {
                        if (couponInfosNew.size() == 0 & couponInfos == null) {
                            ptrl.setVisibility(View.GONE);
                            re_nolist.setVisibility(View.VISIBLE);
                        }
                        if (couponInfosNew.size() > 0) {
                            if (couponInfos == null) {
                                App.getInstance().showToast("已是最后一页");
                            }
                        }
                    }
                    StyledDialog.dismissLoading();
                    break;
                case 3:
                    App.getInstance().showToast("解析异常!");
                    StyledDialog.dismissLoading();
                    break;
            }
        }
    };
}
