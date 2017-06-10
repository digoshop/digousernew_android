package com.digoshop.app.module.storedetail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseFragment;
import com.digoshop.app.module.arrountshops.adp.ArroutShopAdp;
import com.digoshop.app.module.arrountshops.model.ShopInfoBean;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.shopdetailnew.ShopDetailNewSNActivity;
import com.digoshop.app.module.storedetail.model.FloorBean;
import com.digoshop.app.module.storedetail.model.FloorInfo;
import com.digoshop.app.module.storedetail.model.Shopdatalist;
import com.digoshop.app.module.storedetail.view.GridViewForScrollView;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.id.list;


/**
 * TODO<商场详情-商业布局>
 *
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016年7月21日 下午11:31:01
 * @version: V1.0
 */
@SuppressLint("ResourceAsColor")
public class Commercial_distributionFragment extends BaseFragment implements OnClickListener, OnItemClickListener {
    private TextView tv_storefloorshopname, tv_storefloorshopnum;
    private ListView lv_listview;
    private ArroutShopAdp arroutShopAdp;
    private LinearLayout ll_Button;
    private GridViewForScrollView gr_gridview;
    // 生成动态数组，并且转入数据
    ArrayList<HashMap<String, String>> listall;
    private boolean isen = true;
    private View rootView;
    private Shopdatalist shopdata;
    private ArrayList<ShopInfoBean> shopInfoBeansNew = new ArrayList<>();
    private ArrayList<ShopInfoBean> shopInfoBeans;
    private RelativeLayout re_nolist;
    private ArrayList<FloorBean> floormBeans = null;
    private String floorcount;//当前楼层店铺数量
    private String storetotal = null;//当前商场的总店铺数
    private String floornum;//显示的当前数量
    private String floorid = "";
    //楼层详情
    private FloorInfo floorInfo;//当前楼层楼层信息
    private PullToRefreshLayout ptrl;
    private int page = 1;
    private int page_length = 8;

    public static Commercial_distributionFragment newInstance(Bundle bundle) {
        Commercial_distributionFragment fragment = new Commercial_distributionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = View.inflate(getActivity(), R.layout.fragment_commercial_distribution, null);
            TextView tv_title_content = (TextView) rootView.findViewById(R.id.tv_title_content);
            tv_title_content.setText("商场布局");
            re_nolist = (RelativeLayout) rootView.findViewById(R.id.re_nolist);
            tv_storefloorshopname = (TextView) rootView.findViewById(R.id.tv_storefloorshopname);
            tv_storefloorshopnum = (TextView) rootView.findViewById(R.id.tv_storefloorshopnum);
            ptrl = ((PullToRefreshLayout) rootView.findViewById(R.id.refresh_view));
            lv_listview = (ListView) rootView.findViewById(R.id.lv_listview);
            lv_listview.setOnItemClickListener(this);
            ll_Button = (LinearLayout) rootView.findViewById(R.id.ll_Button);
            gr_gridview = (GridViewForScrollView) rootView.findViewById(R.id.gr_gridview);
            ll_Button.setOnClickListener(this);
            storetotal = StoreDetailActivity.tatolstr;
            floormBeans = StoreDetailActivity.floorBeans;
            floorid = "";//初始进来请求该商场的全部店铺
            ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                    if (shopInfoBeansNew.size() > 0) {
                        shopInfoBeansNew.clear();
                    }
                    page = 1;
                    page_length = 8;
                    get_floor_shops();
                    arroutShopAdp.notifyDataSetChanged();
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);


                }

                @Override
                public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                    if (shopInfoBeans != null) {
                        if (shopInfoBeans.size() > 0) {
                            shopInfoBeans.clear();
                        }
                        page = page + 1;
                        page_length = 8;
                        get_floor_shops();
                        arroutShopAdp.appendData(shopInfoBeans);
                    }
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

                }
            });
            StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
            get_floor_shops();
        }
        arroutShopAdp = new ArroutShopAdp(getActivity(), shopInfoBeansNew);
        lv_listview.setAdapter(arroutShopAdp);
        return rootView;
    }

    private void get_floor_shops() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                get_floor_shopsapi();
            }
        }).start();
    }

    private void get_floor_shopsapi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            shopdata = api.get_floor_shops(StoreDetailActivity.storeid, floorid, page + "", page_length + "");

            if (shopdata != null & !"".equals(shopdata)) {
                shopInfoBeans = shopdata.getShops();
                if (shopInfoBeans != null) {
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(4);
                }

            } else {
                handler.sendEmptyMessage(2);
            }
        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(4);
            Log.v("ceshi", "WSError");
            e.printStackTrace();
        }
    }


    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //当前楼层入住店铺数量
                    if (floornum != null) {
                        tv_storefloorshopnum.setText("入住商铺" + floornum + "家");
                    } else {
                        tv_storefloorshopnum.setText("入住商铺" + storetotal + "家");

                    }
                    Log.v("lsq", "storetotal入住商铺+===" + floornum + "storetotal+" + storetotal);
                    //楼层名字+品类
                    if (floorInfo != null & !"".equals(floorInfo)) {
                        tv_storefloorshopname.setText(floorInfo.getMfltt() + floorInfo.getMfln() + "    " + floorInfo.getMfn());
                    }
                    re_nolist.setVisibility(View.GONE);
                    shopInfoBeansNew.addAll(shopInfoBeans);
                    arroutShopAdp.notifyDataSetChanged();
                    StyledDialog.dismissLoading();

                    break;
                case 2:
                    tv_storefloorshopnum.setText("入住商铺" + "0" + "家");
                    App.getInstance().showToast("没有数据了!");
                    ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    StyledDialog.dismissLoading();

                    break;
                case 3:
                    tv_storefloorshopnum.setText("入住商铺" + "0" + "家");
                    App.getInstance().showToast("没有数据了!");
                    ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    re_nolist.setVisibility(View.VISIBLE);
                    StyledDialog.dismissLoading();

                    App.getInstance().showToast("解析异常");
                    break;

                case 4:
                    if (shopInfoBeansNew != null) {
                        if (shopInfoBeansNew.size() == 0) {
                            tv_storefloorshopnum.setText("入住商铺" + "0" + "家");
                        }
                    } else {
                        tv_storefloorshopnum.setText("入住商铺" + "0" + "家");

                    }
                    // tv_storefloorshopnum.setText("入住商铺" + "0" + "家");
                    //楼层名字+品类
                    if (floorInfo != null & !"".equals(floorInfo)) {
                        tv_storefloorshopname.setText(floorInfo.getMfltt() + floorInfo.getMfln() + "    " + floorInfo.getMfn());
                    }
                    App.getInstance().showToast("没有数据了!");
                    StyledDialog.dismissLoading();

                    break;
            }

        }
    };

    private void initview() {
        listall = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>>    listone = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>>    listother = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> mapone = new HashMap<String, String>();
        mapone.put("itemtext", "全部");
        mapone.put("itemtype", storetotal);
        listone.add(mapone);
        listall.addAll(listone);


        if (floormBeans.size() > 0) {
            for (int i = 0; i < floormBeans.size(); i++) {
                HashMap<String, String> mapother = new HashMap<String, String>();
                mapother.put("itemtext", floormBeans.get(i).getFloorInfo().getMfltt()
                        + floormBeans.get(i).getFloorInfo().getMfln());
                mapother.put("itemtype", floormBeans.get(i).getCount());
                Log.v("lsq", "@@@@@@list+===" + list);
                listother.add(mapother);
            }
            if(listother!=null){
                if(listother.size()>0){
                    listall.addAll(listother);
                }
            }
        }

        // 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), listall, R.layout.fragment_commercial_distribution_gridview_item,
                new String[]{"itemtext", "itemtype"},
                new int[]{R.id.gridview_textview_name, R.id.gridview_textview_num}
        );
        // 添加并且显示
        gr_gridview.setAdapter(adapter);
        // 添加消息处理
        gr_gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    floorid = "";
                    floornum = listall.get(position).get("itemtype");
                    floorInfo = new FloorInfo();
                    floorInfo.setMfln("全部店铺");
                    floorInfo.setMflid("");
                    floorInfo.setMfltt("");
                    floorInfo.setMfn("");
                } else {
                    floorid = floormBeans.get(position-1).getFloorInfo().getMflid();
                    floornum = floormBeans.get(position-1).getCount();
                    floorInfo = floormBeans.get(position-1).getFloorInfo();
                }


                if (shopInfoBeansNew.size() > 0) {
                    shopInfoBeansNew.clear();
                }
                page = 1;
                get_floor_shops();
                arroutShopAdp.notifyDataSetChanged();
                gr_gridview.setVisibility(View.GONE);
                isen = true;
                ll_Button.setBackgroundColor(getResources().getColor(R.color.white_text));

            }
        });
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_Button:
                if (storetotal != null & !"0".equals(storetotal) & floormBeans != null & !"".equals(floormBeans)) {
                    if (floormBeans.size() == 0) {
                        App.getInstance().showToast("暂无楼层数据");
                        return;
                    }
                    if (isen) {
                        gr_gridview.setVisibility(View.VISIBLE);
                        if (storetotal != null & !"0".equals(storetotal)) {
                            if (floormBeans != null & !"".equals(floormBeans)) {
                                initview();

                            }

                        }
                        ll_Button.setBackgroundColor(getResources().getColor(R.color.qianhui));
                        isen = false;
                    } else {
                        gr_gridview.setVisibility(View.GONE);
                        ll_Button.setBackgroundColor(getResources().getColor(R.color.white_text));
                        isen = true;
                    }

                } else {
                    App.getInstance().showToast("暂无楼层数据");
                }


                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Intent intent = new Intent();
        intent.putExtra("sid", shopInfoBeansNew.get(arg2).getSid());
        intent.setClass(getActivity(), ShopDetailNewSNActivity.class);
        startActivity(intent);
    }

}
