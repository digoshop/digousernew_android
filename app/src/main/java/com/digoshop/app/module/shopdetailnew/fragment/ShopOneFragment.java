package com.digoshop.app.module.shopdetailnew.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.module.arrountshops.model.ShopInfoBean;
import com.digoshop.app.module.home.couponactive.ActivityDetails;
import com.digoshop.app.module.home.couponactive.CouponDetailActivity;
import com.digoshop.app.module.home.model.ActivityDetailBean;
import com.digoshop.app.module.product.ProductDetailAty;
import com.digoshop.app.module.product.ProductExchangeAty;
import com.digoshop.app.module.shopdetail.ShopCommentListAty;
import com.digoshop.app.module.shopdetailnew.ShopDetailAllAtyActivity;
import com.digoshop.app.module.shopdetailnew.ShopDetailNewSNActivity;
import com.digoshop.app.module.shopdetailnew.adp.ShopCommentAdp;
import com.digoshop.app.module.shopdetailnew.adp.ShopProductAdp;
import com.digoshop.app.module.shopdetailnew.adp.ShopUnionAdp;
import com.digoshop.app.module.shopdetailnew.model.ShopComment;
import com.digoshop.app.module.shopdetailnew.model.ShopDetailNData;
import com.digoshop.app.module.shopdetailnew.model.ShopProduct;
import com.digoshop.app.module.shopdetailnew.view.CouponsHorizonScrollView;
import com.digoshop.app.module.userCenter.model.Discountcoupons;
import com.digoshop.app.utils.DigoGps;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.http.WSError;

import org.json.JSONException;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;

public class    ShopOneFragment extends Fragment implements View.OnClickListener {
    private String latstr = "";
    private String logstr = "";
    private String operateTypestr = "";
    private String regionIdstr = "";
    private int screenWidth;
    private String sid;
    private ShopDetailNData detailNData;
    private TextView tv_shopdetailn_time, tv_shopdetailn_tel, tv_shopdetailn_address, tv_shopdetailn_content, tv_shopdetailn_aty;
    private LinearLayout lin_shopn_address;
    private CouponsHorizonScrollView ch_shopviewsc;
    private View view_ashop_coupon_line, v_shopn_atylist, view_ashop_prodcut_line, view_ashop_comment_line, view_ashop_shop_line;
    private GridView gv_shopnproduct, gv_shopn_shop;
    private RelativeLayout re_shopnpro_line, re_comment_line, re_shopunion_line;
    private ScrollView sv_shopone;
    private ListView lv_shopn_comment;
    private Double latd, lgtd;
    private String shopnamestr;
    private ArrayList<ShopProduct> shopProducts;
    private RelativeLayout re_shopn_newsarrow, re_shopn_aty;
    private boolean isapitype = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.shopone_fragment, null);
        Bundle args = getArguments();
        v_shopn_atylist = view.findViewById(R.id.v_shopn_atylist);
        re_shopn_aty = (RelativeLayout) view.findViewById(R.id.re_shopn_aty);
        re_shopn_newsarrow = (RelativeLayout) view.findViewById(R.id.re_shopn_newsarrow);
        re_shopn_newsarrow.setOnClickListener(this);
        lv_shopn_comment = (ListView) view.findViewById(R.id.lv_shopn_comment);
        sv_shopone = (ScrollView) view.findViewById(R.id.id_stickynavlayout_innerscrollview);
//        sv_shopone.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                sv_shopone.requestDisallowInterceptTouchEvent(true);
//                return true;
//            }
//        });

        view_ashop_coupon_line = view.findViewById(R.id.view_ashop_coupon_line);
        view_ashop_comment_line = view.findViewById(R.id.view_ashop_comment_line);
        view_ashop_shop_line = view.findViewById(R.id.view_ashop_shop_line);
        view_ashop_prodcut_line = view.findViewById(R.id.view_ashop_prodcut_line);

        tv_shopdetailn_aty = (TextView) view.findViewById(R.id.tv_shopdetailn_aty);
        tv_shopdetailn_aty.setOnClickListener(this);
        gv_shopnproduct = (GridView) view.findViewById(R.id.gv_shopnproduct);
        gv_shopn_shop = (GridView) view.findViewById(R.id.gv_shopn_shop);
        gv_shopn_shop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (detailNData != null) {
                    ArrayList<ShopInfoBean> shopinfoDatas = detailNData.getShopinfoDatas();
                    if (shopinfoDatas != null) {
                        if (shopinfoDatas.size() > 0) {
                            Intent intent = new Intent();
                            intent.putExtra("sid", shopinfoDatas.get(i).getSid());
                            intent.setClass(getActivity(), ShopDetailNewSNActivity.class);
                            startActivity(intent);
                        }
                    }

                }


            }
        });

        gv_shopnproduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                if (!TextUtils.isEmpty(shopProducts.get(i).getType())) {
                    if ("2".equals(shopProducts.get(i).getType())) {
                        intent.setClass(getActivity(), ProductExchangeAty.class);
                    } else if ("3".equals(shopProducts.get(i).getType())) {
                        intent.setClass(getActivity(), ProductDetailAty.class);
                    }
                }
                intent.putExtra("pid", shopProducts.get(i).getPid());
                intent.putExtra("pt", shopProducts.get(i).getType());
                startActivity(intent);

            }
        });
        re_shopnpro_line = (RelativeLayout) view.findViewById(R.id.re_shopnpro_line);
        re_shopnpro_line.setOnClickListener(this);
        re_comment_line = (RelativeLayout) view.findViewById(R.id.re_comment_line);
        re_comment_line.setOnClickListener(this);
        re_shopunion_line = (RelativeLayout) view.findViewById(R.id.re_shopunion_line);


        ch_shopviewsc = (CouponsHorizonScrollView) view.findViewById(R.id.ch_shopviewsc);
        lin_shopn_address = (LinearLayout) view.findViewById(R.id.lin_shopn_address);
        lin_shopn_address.setOnClickListener(this);
        tv_shopdetailn_time = (TextView) view.findViewById(R.id.tv_shopdetailn_time);
        tv_shopdetailn_tel = (TextView) view.findViewById(R.id.tv_shopdetailn_tel);
        tv_shopdetailn_address = (TextView) view.findViewById(R.id.tv_shopdetailn_address);
        tv_shopdetailn_content = (TextView) view.findViewById(R.id.tv_shopdetailn_content);


        sid = args != null ? args.getString("sid") : "";

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isapitype) {
            if (getNetWifi()) {
                getShopDetailN();
                isapitype = false;
            } else {
                App.getInstance().showToast("网络不给力，请检查网络设置");
            }
        }

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
            detailNData = api.getShopDetailN(sid);
            if (detailNData != null) {
                handler.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(4);
            }

        } catch (JSONException e) {
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        } catch (WSError e) {
            if ("A502".equals(e.getMessage())) {
                handler.sendEmptyMessage(502);
                return;
            }
            handler.sendEmptyMessage(4);
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (!TextUtils.isEmpty(detailNData.getOpen_time())) {
                        tv_shopdetailn_time.setText(detailNData.getOpen_time());
                    }
                    if (!TextUtils.isEmpty(detailNData.getContact())) {
                        tv_shopdetailn_tel.setText(detailNData.getContact());
                    }
                    if (!TextUtils.isEmpty(detailNData.getAddress())) {
                        tv_shopdetailn_address.setText(detailNData.getAddress());
                    }
                    if (!TextUtils.isEmpty(detailNData.getDescription())) {

                        tv_shopdetailn_content.setText(detailNData.getDescription());
                    }
                    if (!TextUtils.isEmpty(detailNData.getMnti())) {
                        re_shopn_aty.setVisibility(View.VISIBLE);
                        v_shopn_atylist.setVisibility(View.VISIBLE);
                        tv_shopdetailn_aty.setText(detailNData.getMnti());
                    } else {
                        re_shopn_aty.setVisibility(View.GONE);
                        v_shopn_atylist.setVisibility(View.GONE);
                    }
                    ArrayList<Discountcoupons> discountcouponsArrayList = detailNData.getDiscountcouponses();
                    if (discountcouponsArrayList != null) {
                        if (discountcouponsArrayList.size() > 0) {
                            view_ashop_coupon_line.setVisibility(View.VISIBLE);
                            ch_shopviewsc.setVisibility(View.VISIBLE);
                            ch_shopviewsc.setListData(discountcouponsArrayList, mListener, 0);
                            ch_shopviewsc.smoothScrollBy(0, 0);
                        } else {
                            ch_shopviewsc.setVisibility(View.GONE);
                            view_ashop_coupon_line.setVisibility(View.GONE);
                        }
                    } else {
                        ch_shopviewsc.setVisibility(View.GONE);
                        view_ashop_coupon_line.setVisibility(View.GONE);
                    }
                    shopProducts = detailNData.getShopProducts();
                    if (shopProducts != null) {
                        if (shopProducts.size() > 0) {
                            view_ashop_prodcut_line.setVisibility(View.VISIBLE);
                            gv_shopnproduct.setVisibility(View.VISIBLE);
                            gv_shopnproduct.setAdapter(new ShopProductAdp(getActivity(), shopProducts));
                            re_shopnpro_line.setVisibility(View.VISIBLE);
                        } else {
                            view_ashop_prodcut_line.setVisibility(View.GONE);
                            gv_shopnproduct.setVisibility(View.GONE);
                            re_shopnpro_line.setVisibility(View.GONE);
                        }
                    } else {
                        view_ashop_prodcut_line.setVisibility(View.GONE);
                        gv_shopnproduct.setVisibility(View.GONE);
                        re_shopnpro_line.setVisibility(View.GONE);
                    }
                    ArrayList<ShopComment> shopComments = detailNData.getShopComments();
                    if (shopComments != null) {
                        if (shopComments.size() > 0) {
                            lv_shopn_comment.setVisibility(View.VISIBLE);
                            re_comment_line.setVisibility(View.VISIBLE);
                            lv_shopn_comment.setAdapter(new ShopCommentAdp(getActivity(), shopComments));
                            setListViewHeightBasedOnChildren(lv_shopn_comment);
                            view_ashop_comment_line.setVisibility(View.VISIBLE);
                        } else {
                            lv_shopn_comment.setVisibility(View.GONE);
                            re_comment_line.setVisibility(View.GONE);
                            view_ashop_comment_line.setVisibility(View.GONE);
                        }
                    } else {
                        lv_shopn_comment.setVisibility(View.GONE);
                        re_comment_line.setVisibility(View.GONE);
                        view_ashop_comment_line.setVisibility(View.GONE);
                    }

                    ArrayList<ShopInfoBean> shopinfoDatas = detailNData.getShopinfoDatas();
                    if (shopinfoDatas != null) {
                        if (shopinfoDatas.size() > 0) {

                            gv_shopn_shop.setVisibility(View.VISIBLE);
                            re_shopunion_line.setVisibility(View.VISIBLE);
                            gv_shopn_shop.setAdapter(new ShopUnionAdp(getActivity(), shopinfoDatas));
                            view_ashop_shop_line.setVisibility(View.VISIBLE);
                        } else {
                            gv_shopn_shop.setVisibility(View.GONE);
                            re_shopunion_line.setVisibility(View.GONE);
                            view_ashop_shop_line.setVisibility(View.GONE);
                        }
                    } else {
                        gv_shopn_shop.setVisibility(View.GONE);
                        re_shopunion_line.setVisibility(View.GONE);
                        view_ashop_shop_line.setVisibility(View.GONE);
                    }


                    sv_shopone.smoothScrollTo(0, 0);
                    //StyledDialog.dismissLoading();
                    break;
                case 2:
                    //StyledDialog.dismissLoading();
                    break;
                case 3:
                    App.getInstance().showToast("解析异常");
                    //  StyledDialog.dismissLoading();
                    break;
                case 4:
                    App.getInstance().showToast("请求异常");
                    // StyledDialog.dismissLoading();
                    break;
                case 502:
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                    // StyledDialog.dismissLoading();
                    break;

            }

        }
    };

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.lin_shopn_address:
                //判断经纬度
                if (detailNData != null) {
                    if (detailNData.getPointBean() != null) {
                        if (detailNData.getPointBean() == null) {
                            return;
                        }
                        latd = detailNData.getPointBean().getLat();
                        lgtd = detailNData.getPointBean().getLgt();
                        if (latd != null & lgtd != null) {
                            shopnamestr = detailNData.getName();
                            showPopwindow();
                        }
                    }
                }

                break;
            case R.id.tv_shopdetailn_aty:
                //判断是否有活动
                if (detailNData != null) {
                    if (!TextUtils.isEmpty(detailNData
                            .getMnid())) {
                        ActivityDetailBean homeactivityinfo = new ActivityDetailBean();
                        Intent intentaty = new Intent();
                        homeactivityinfo.setMnid(detailNData
                                .getMnid());
                        intentaty.setClass(getActivity(), ActivityDetails.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("atycontent", homeactivityinfo);
                        intentaty.putExtras(bundle);
                        startActivity(intentaty);
                    }
                }
                break;
            case R.id.re_shopn_newsarrow:
                Log.v("ceshi", "))))))))))))))");
                Intent intentsa = new Intent();
                intentsa.putExtra("sid", sid);
                intentsa.setClass(getActivity(), ShopDetailAllAtyActivity.class);
                startActivity(intentsa);

                break;
            case R.id.re_shopnpro_line:
                //更多热门商品 判断
                break;
            case R.id.re_comment_line:
                //更多评论  判断
                if (!TextUtils.isEmpty(detailNData.getSid())) {
                    intent.putExtra("sid", detailNData.getSid());
                    intent.setClass(getActivity(), ShopCommentListAty.class);
                    startActivity(intent);
                } else {
                    App.getInstance().showToast("请求失败，请稍后再试");
                }

                break;

        }
    }

    private void showPopwindow() {
        //desLatLng.latitude + "," + desLatLng.longitude
        View parent = ((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0);
        View popView = View.inflate(getActivity(), R.layout.camera_pop_menu, null);

        Button btnCamera = (Button) popView.findViewById(R.id.btn_camera_pop_camera);
        Button btnAlbum = (Button) popView.findViewById(R.id.btn_camera_pop_album);
        Button btnCancel = (Button) popView.findViewById(R.id.btn_camera_pop_cancel);
        btnCamera.setText("高德地图");
        btnAlbum.setText("百度地图");
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        final PopupWindow popWindow = new PopupWindow(popView, width, height);
        popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(false);// 设置允许在外点击消失
        View.OnClickListener listener = new View.OnClickListener() {

            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_camera_pop_camera:
                        if (isInstallByread("com.autonavi.minimap")) {
                            openGaoDeMap();
                        } else {
                            App.getInstance().showToast("没有检测到高德地图,请安装高德地图!");
                        }
                        break;
                    case R.id.btn_camera_pop_album:
                        if (isInstallByread("com.baidu.BaiduMap")) {
                            openBaiduMap();
                        } else {
                            App.getInstance().showToast("没有检测到百度地图,请安百度地图!");
                        }

                        break;
                    case R.id.btn_camera_pop_cancel:

                        break;
                }
                popWindow.dismiss();
            }
        };

        btnCamera.setOnClickListener(listener);
        btnAlbum.setOnClickListener(listener);
        btnCancel.setOnClickListener(listener);

        ColorDrawable dw = new ColorDrawable(0x30000000);
        popWindow.setBackgroundDrawable(dw);
        popWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void openBaiduMap() {
        try {

            DigoGps gaodeigps = Tool.gcj02_To_Bd09New(latd, lgtd);
//            double latdlin = 39.912289;
//            double lgtdlin = 116.365868;
//            DigoGps gaodeigpslin = Tool.gcj02_To_Bd09New(latdlin, lgtdlin);
//            Log.v("ceshi","高德开发+bdlat+"+gaodeigpslin.getWgLat());
//            Log.v("ceshi","高德开发+bdlng"+gaodeigpslin.getWgLon() );
            Intent intent = Intent.getIntent("intent://map/marker?coord_type=gcj02&location="
                    + gaodeigps.getWgLat() + "," + gaodeigps.getWgLon() + "&title=" + shopnamestr + "&content="
                    + shopnamestr + "&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            startActivity(intent);
        } catch (URISyntaxException e) {
            App.getInstance().showToast("您尚未安装百度地图app或app版本过低!");
            e.printStackTrace();
            e.printStackTrace();
        } catch (ActivityNotFoundException e) {
            App.getInstance().showToast("您尚未安装百度地图app或app版本过低!");
            e.printStackTrace();
        } catch (Exception e) {
            App.getInstance().showToast("您尚未安装百度地图app或app版本过低!");
            e.printStackTrace();
        }

    }

    private void openGaoDeMap() {
        try {
            Intent intent1 = Intent.getIntent("androidamap://viewMap?sourceApplication=" + "迪购" + "&poiname=" + shopnamestr + "&lat=" + latd + "&lon=" + lgtd + "&dev=0");
            startActivity(intent1);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ActivityNotFoundException e) {
            App.getInstance().showToast("没有检测到高德地图,请安装高德地图!");
            e.printStackTrace();
        } catch (Exception e) {
            App.getInstance().showToast("您尚未安装高德地图app或app版本过低!");
            e.printStackTrace();
        }
    }

    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    /**
     * 实现类，响应按钮点击事件
     */
    private CouponsHorizonScrollView.TextTypeClickListener mListener = new CouponsHorizonScrollView.TextTypeClickListener() {
        @Override
        public void myOnClick(int position, View v) {
//            int offest = (position - 1) * getResources().getDimensionPixelSize(R.dimen.base_dimen_60);
//            aty_textviewsc.smoothScrollTo((position - 1) * getResources().getDimensionPixelSize(R.dimen.base_dimen_100) + offest, 0);
            if (detailNData != null) {
                if (detailNData.getDiscountcouponses() != null) {
                    Intent intent = new Intent();
                    intent.putExtra("cdid", detailNData.getDiscountcouponses().get(position).getCbid());
                    intent.setClass(getActivity(), CouponDetailActivity.class);
                    startActivity(intent);
                    isapitype = true;
                }
            }

        }
    };

    /**
     * 动态设置ListView的高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public String getTitle() {
        return "店铺首页";
    }
}
