package com.digoshop.app.module.home.jifenhg;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.module.arrountshops.adp.ArountShop_textgridTypeAdp;
import com.digoshop.app.module.arrountshops.model.ArrountitemBean;
import com.digoshop.app.module.arrountshops.model.MyArrayListCityBean;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.product.ProductExchangeAty;
import com.digoshop.app.module.shopdetailnew.adp.ShopAllProductAdp;
import com.digoshop.app.module.shopdetailnew.model.ShopDetailNData;
import com.digoshop.app.module.shopdetailnew.model.ShopProduct;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;

public class SaleListFragment extends Fragment {
    private ShopAllProductAdp flashSaleslistAdp;
    private GridView lv_citycouponlist;
    private ArrayList<ShopProduct> couponInfos = new ArrayList<>();
    private ArrayList<ShopProduct> couponInfosNew = new ArrayList<>();

    private PullToRefreshLayout ptrl;
    private int page = 1;
    private int page_length = 10;
    private RelativeLayout re_nolist;
    private String CityCodestr = "";
    private String latstr = "";
    private String logstr = "";
    private String operateTypestr = "";
    private String regionIdstr = "";
    private int screenWidth;
    private ArrayList<MyArrayListCityBean> myArrayListCityBeens;
    private GridView gv_sale_type;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.w("test", "www");
            if (getNetWifi()) {

                if (couponInfosNew != null) {
                    couponInfosNew.clear();
                }
                if (couponInfos != null) {
                    couponInfos.clear();
                }
                getexchangeList();

            }

        } else {

        }
    }

    int heightiv;
    ArrayList<ArrountitemBean> arrountitemBeensNEW;
    ArountShop_textgridTypeAdp arountShop_textgridTypeAdp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.sales_fragment, null);
        Bundle args = getArguments();
        arrountitemBeensNEW = (ArrayList<ArrountitemBean>) args.getSerializable("list");

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
      //  int heightiv = screenWidth / 2 * 2 / 3;
        Log.v("ceshi","^^^"+screenWidth);
        int heightiv = screenWidth / 2 -getResources().getDimensionPixelOffset(R.dimen.base_dimen_12) ;
        int screenHeight = dm.heightPixels;
        re_nolist = (RelativeLayout) view.findViewById(R.id.re_nocouponlist);
        CityCodestr = LocalSave.getValue(getActivity(), AppConfig.basefile, "CityCode", "");
        ptrl = ((PullToRefreshLayout) view.findViewById(R.id.refresh_store_view));
        lv_citycouponlist = (GridView) view.findViewById(R.id.gv_saleactivity);

        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
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
        if (couponInfosNew != null) {
            couponInfosNew.clear();
        }
        if (couponInfos != null) {
            couponInfos.clear();
        }
        flashSaleslistAdp = new ShopAllProductAdp(getActivity(), couponInfosNew);
        lv_citycouponlist.setAdapter(flashSaleslistAdp);
        gv_sale_type = (GridView) view.findViewById(R.id.gv_sale_type);
        gv_sale_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (getNetWifi()) {
                    if (couponInfosNew != null) {
                        couponInfosNew.clear();
                    }

//                    if (couponInfos != null) {
//                        couponInfos.clear();
//                    }
                    page = 1;
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                    moidstr = arrountitemBeensNEW.get(i).getMoid();
                    arountShop_textgridTypeAdp.setChoicePos(i);
                    arountShop_textgridTypeAdp.notifyDataSetChanged();
                      Log.v("ceshi", "gv+" + moidstr);
                    getexchangeList();
                    flashSaleslistAdp.notifyDataSetChanged();
                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }
            }
        });



        if (arrountitemBeensNEW != null) {
            if (arrountitemBeensNEW.size() > 0) {
                if(!TextUtils.isEmpty(arrountitemBeensNEW.get(0).getName())){
                    gv_sale_type.setVisibility(View.VISIBLE);
                    arountShop_textgridTypeAdp = new ArountShop_textgridTypeAdp(getActivity(), arrountitemBeensNEW);
                    gv_sale_type.setAdapter(arountShop_textgridTypeAdp);
                    arountShop_textgridTypeAdp.setChoicePos(0);
                    arountShop_textgridTypeAdp.notifyDataSetChanged();
                }else{
                    gv_sale_type.setVisibility(View.GONE);
                }

            }
        }
        if (arrountitemBeensNEW != null) {
            if (arrountitemBeensNEW.size() > 0) {
                moidstr = arrountitemBeensNEW.get(0).getMoid();
            } else {
                moidstr = "";
            }
        } else {
            moidstr = "";
        }
        getexchangeList();

        return view;
    }

    private String moidstr = "";

    private void getexchangeList() {
        if (TextUtils.isEmpty(CityCodestr)) {
            App.getInstance().showToast("定位获取失败,默认北京");
            CityCodestr = "010";
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                getexchangeListApi();
            }
        }).start();
    }
    private ShopDetailNData detailNData;
    private void getexchangeListApi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            detailNData = api.getShopnProdcuts("2", "", CityCodestr, "", page + "", moidstr);

           // couponInfos = api.get_exchange_listNew(moidstr, isuserid, CityCodestr, page + "", page_length + "");
            if (detailNData != null) {
                couponInfos = detailNData.getShopProducts();
                if (couponInfos != null) {
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
                    if (couponInfosNew != null & couponInfos != null) {
                       if(couponInfosNew.size()>0&couponInfos.size() == 0){
                           App.getInstance().showToast("已是最后一页");
                       }
                        if (couponInfosNew.size() == 0 & couponInfos.size() == 0) {
                            //第一次进来的时候没一个
                            re_nolist.setVisibility(View.VISIBLE);
                            ptrl.setVisibility(View.GONE);
                        } else {
                            ptrl.setVisibility(View.VISIBLE);
                            re_nolist.setVisibility(View.GONE);
                        }
                        if(couponInfosNew.size() == 0){
                            ptrl.setVisibility(View.GONE);
                            re_nolist.setVisibility(View.VISIBLE);
                        }
                    }
                    //  App.getInstance().showToast("暂无数据!");
                    if(ptrl!=null){
                        ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
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
