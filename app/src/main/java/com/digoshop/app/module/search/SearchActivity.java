package com.digoshop.app.module.search;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.arrountshops.adp.ArroutShopAdp;
import com.digoshop.app.module.arrountshops.model.ShopInfoBean;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.home.adp.HotkeyAdp;
import com.digoshop.app.module.search.view.SearchTipsGroupView;
import com.digoshop.app.module.shopdetailnew.ShopDetailNewSNActivity;
import com.digoshop.app.module.storedetail.StoreDetailActivity;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.DensityUtil;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.http.WSError;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hss01248.dialog.StyledDialog;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity implements OnClickListener {
    private ImageView iv_searchactivity_return, iv_homefragment_search_go;
    private EditText et_homefragment_search;
    // 声明PopupWindow对象的引用
    private PopupWindow popupWindow;
    private TextView tv_homefragment_searchtype;
    private RelativeLayout re_search_type_sp, re_search_type_shop;
    private String searchtext;
    private ShopinfoData shopinfodata;
    private String markstr = "";
    private RelativeLayout re_nolist, lin_search_saomao;
    private String searchkeywordstr;
    private String CityCodestr;
    private SearchTipsGroupView view;
    private GridView gv_searchaty;
    private GoogleApiClient client;
    private ArrayList<String> strlists;
    private String typestr = "1";
    private ArrayList<ShopInfoBean> shopInfoBeans;
    private ArrayList<ShopInfoBean> shopInfoBeansNew = new ArrayList<>();
    private ListView searchshops_lv_shops;
    private ArroutShopAdp arroutShopAdp;
    private PullToRefreshLayout ptrl;
    private int page = 1;
    private int page_length = 10;
    private TextView tv_hottext;
    private boolean isonclick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent = getIntent();
        Log.v("lsq", "sid11111111111111+");
        et_homefragment_search = (EditText) findViewById(R.id.et_homefragment_search);
        searchkeywordstr = intent.getStringExtra("searchkeyword");
        CityCodestr = LocalSave.getValue(SearchActivity.this, AppConfig.basefile, "CityCode", "");
        if (!TextUtils.isEmpty(searchkeywordstr)) {
            et_homefragment_search.setText(searchkeywordstr);
            SearchShops();
        } else {
            SearchHotkey();
        }
        tv_hottext = (TextView) findViewById(R.id.tv_hottext);
        re_nolist = (RelativeLayout) findViewById(R.id.re_nolist);
        lin_search_saomao = (RelativeLayout) findViewById(R.id.lin_search_saomao);
        lin_search_saomao.setOnClickListener(this);
        searchshops_lv_shops = (ListView) findViewById(R.id.searchshops_lv_shops);
        searchshops_lv_shops.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("sid", shopInfoBeansNew.get(position).getSid());
                intent.setClass(SearchActivity.this, ShopDetailNewSNActivity.class);
                startActivity(intent);

            }
        });
        gv_searchaty = (GridView) findViewById(R.id.gv_searchaty);
        gv_searchaty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String hotkeystr = strlists.get(position);
                if (!TextUtils.isEmpty(hotkeystr)) {
                    searchkeywordstr = hotkeystr;
                    et_homefragment_search.setText(searchkeywordstr);
                    tv_hottext.setVisibility(View.GONE);
                    gv_searchaty.setVisibility(View.GONE);
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    gv_searchaty.setVisibility(View.GONE);
                    markstr = "";
                    SearchShops();
                } else {
                    App.getInstance().showToast("请输入内容!");
                }
            }
        });
        tv_homefragment_searchtype = (TextView) findViewById(R.id.tv_homefragment_searchtype);
        tv_homefragment_searchtype.setOnClickListener(this);
        iv_homefragment_search_go = (ImageView) findViewById(R.id.iv_homefragment_search_go);
        iv_homefragment_search_go.setOnClickListener(this);
        iv_searchactivity_return = (ImageView) findViewById(R.id.iv_searchactivity_return);
        iv_searchactivity_return.setOnClickListener(this);
        et_homefragment_search = (EditText) findViewById(R.id.et_homefragment_search);
        et_homefragment_search.clearFocus();
        et_homefragment_search.setOnKeyListener(new View.OnKeyListener() {

            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
// 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    if (!TextUtils.isEmpty(et_homefragment_search.getText().toString())) {
                        searchkeywordstr = et_homefragment_search.getText().toString();
                        searchkeywordstr = searchkeywordstr.replaceAll(" ", "");
                        if (TextUtils.isEmpty(searchkeywordstr)) {
                            App.getInstance().showToast("请输入内容!");
                        } else {
                            if (shopInfoBeansNew.size() > 0) {
                                shopInfoBeansNew.clear();
                            }
                            markstr = "";
                            SearchShops();
                        }
                    } else {
                        App.getInstance().showToast("请输入内容!");
                    }
                    return true;
                }
                return false;
            }
        });
        et_homefragment_search.setHint(Tool.edithint(getString(R.string.search_text), DensityUtil.px2dip(App.getInstance(), (float) getResources().getDimensionPixelSize(R.dimen.base_dimen_32))));


//		view = (SearchTipsGroupView) findViewById(R.id.search_tips);
//		view.initViews(items,this);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        ptrl = ((PullToRefreshLayout) findViewById(R.id.refresh_view));
        arroutShopAdp = new ArroutShopAdp(SearchActivity.this, shopInfoBeansNew);
        searchshops_lv_shops.setAdapter(arroutShopAdp);
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (shopInfoBeansNew.size() > 0) {
                    shopInfoBeansNew.clear();
                }
                markstr = "";
                SearchShops();
                arroutShopAdp.notifyDataSetChanged();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (shopInfoBeans != null) {
                    if (shopInfoBeans.size() > 0) {
                        shopInfoBeans.clear();
                    }
                    SearchShops();
                    arroutShopAdp.appendData(shopInfoBeans);
                }
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);


            }
        });
    }

    private void getPopupWindow() {
        if (null != popupWindow) {
            popupWindow.dismiss();
            return;
        } else {
            initPopuptWindow();
        }
    }

    /**
     * 创建PopupWindow
     */
    protected void initPopuptWindow() {
        // TODO Auto-generated method stub
        // 获取自定义布局文件activity_popupwindow_left.xml的视图
        View popupWindow_view = this.getLayoutInflater().inflate(
                R.layout.activity_search_pulldown, null, false);
        re_search_type_sp = (RelativeLayout) popupWindow_view
                .findViewById(R.id.re_search_type_sp);
        re_search_type_shop = (RelativeLayout) popupWindow_view
                .findViewById(R.id.re_search_type_shop);

        re_search_type_sp.setOnClickListener(this);
        re_search_type_shop.setOnClickListener(this);
        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        popupWindow = new PopupWindow(popupWindow_view,
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);

        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置动画效果
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // popupWindow.setAnimationStyle(R.style.AnimationFade);
        // 点击其他地方消失
        popupWindow_view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                return false;
            }
        });
    }

    private void SearchHotkey() {
        if (TextUtils.isEmpty(CityCodestr)) {
            App.getInstance().showToast("定位获取失败,默认北京");
            CityCodestr = "010";
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                SearchHotkeyApi();
            }
        }).start();
    }

    private void SearchHotkeyApi() {
        {

            DigoIUserApiImpl api = new DigoIUserApiImpl();
            try {

                strlists = api.gethotkey(CityCodestr);
                if ("".equals(strlists) | strlists == null) {
                    handler.sendEmptyMessage(16);
                } else {
                    handler.sendEmptyMessage(5);
                }

            } catch (JSONException e) {
                Log.v("ceshi", "JSONException");
                e.printStackTrace();
            } catch (WSError e) {
                Log.v("ceshi", "WSError");
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isNoTitle() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isFullScreen() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_searchactivity_return:// 返回
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus()
                                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                finish();
                break;
            case R.id.iv_homefragment_search_go:// 搜索
                if (!TextUtils.isEmpty(et_homefragment_search.getText().toString())) {
                    searchkeywordstr = et_homefragment_search.getText().toString();
                    searchkeywordstr = searchkeywordstr.replaceAll(" ", "");
                    if (TextUtils.isEmpty(searchkeywordstr)) {
                        App.getInstance().showToast("请输入内容!");
                        return;
                    }
                    markstr = "";
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    if (shopInfoBeansNew.size() > 0) {
                        shopInfoBeansNew.clear();
                    }
                    SearchShops();
                } else {
                    App.getInstance().showToast("请输入内容!");
                }

                break;
            case R.id.tv_homefragment_searchtype:
                getPopupWindow();
                popupWindow.showAsDropDown(v);
                break;
            case R.id.re_search_type_sp:
                tv_homefragment_searchtype.setText("商品");
                typestr = "2";
                if (null != popupWindow) {
                    popupWindow.dismiss();
                    return;
                }
                Log.v("ceshi", "商品typestr" + typestr);

                break;
            case R.id.re_search_type_shop:
                tv_homefragment_searchtype.setText("店铺");
                typestr = "1";
                if (null != popupWindow) {
                    popupWindow.dismiss();
                    return;
                }
                Log.v("ceshi", "店铺typestr" + typestr);
                break;
            case R.id.lin_search_saomao:
                if (isonclick) {
                    isonclick = false;
                    getTodoPermission();
                }

                break;
            default:
                break;
        }
    }

    private void getTodoPermission() {
        MPermissions.requestPermissions(this, 11, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA});

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @PermissionDenied(11)
    public void requestContactFailed() {

        isonclick = true;
        App.getInstance().showToast("获取权限失败！");
    }


    @PermissionGrant(11)
    public void requestContactSuccess() {
        Intent intent = new Intent();
        intent.setClass(SearchActivity.this, ScanActivity.class);
        startActivityForResult(intent, 0);
        isonclick = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Log.v("ceshi", "data+" + data.getStringExtra("result"));
            showResult(data.getStringExtra("result"));
        } else {
            // App.getInstance().showToast("扫描失败，请重试!");
        }
    }

    public void showResult(String results) {
        Log.v("ceshi", "results+" + results);
        if (TextUtils.isEmpty(results)) {
            App.getInstance().showToast("扫描失败，请重试!");
        } else {
            //
            // {"shop_id":1000048, "type":2} //商铺
            // {"shop_id":1000048, "type":1} //商场
            // {"uid":"103232432432434"} //用户id
            Log.v("ceshi", "results+" + results);
            JSONObject json;
            try {
                json = new JSONObject(results);
                Intent intent = new Intent();
                if (!TextUtils.isEmpty(json.optString("type"))) {
                    if (json.optString("type").equals("2")) {
                        intent.setClass(SearchActivity.this, ShopDetailNewSNActivity.class);
                        intent.putExtra("sid", json.optString("shop_id"));
                        startActivity(intent);
                        finish();
                    } else if (json.optString("type").equals("1")) {
                        intent.setClass(SearchActivity.this, StoreDetailActivity.class);
                        intent.putExtra("storeid", json.optString("shop_id"));
                        startActivity(intent);
                        finish();
                    } else {
                        App.getInstance().showToast("系统无法识别非本系统二维码");
                    }
                } else {
                    App.getInstance().showToast("系统无法识别非本系统二维码");
                }

            }   catch (Exception e) {
                e.printStackTrace();
                App.getInstance().showToast("系统无法识别非本系统二维码");
            }
        }
    }


    private void SearchShops() {
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                SearchShopsApi();
            }
        }).start();
    }

    private void SearchShopsApi() {

        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            Log.v("ceshi", "SerachShopstypestr" + typestr);
            shopinfodata = api.SerachShops(typestr, searchkeywordstr, markstr, CityCodestr);
            if (shopinfodata != null) {
                shopInfoBeans = shopinfodata.getShopInfoBeans();
                markstr = shopinfodata.getMarkstr();
                handler.sendEmptyMessage(1);
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

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 1:
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    shopInfoBeansNew.addAll(shopInfoBeans);
                    arroutShopAdp.notifyDataSetChanged();
                    StyledDialog.dismissLoading();

                    re_nolist.setVisibility(View.GONE);
                    gv_searchaty.setVisibility(View.GONE);
                    tv_hottext.setVisibility(View.GONE);
                    break;
                case 2:
                    App.getInstance().showToast("没有数据了!");
                    re_nolist.setVisibility(View.GONE);
                    gv_searchaty.setVisibility(View.GONE);
                    tv_hottext.setVisibility(View.GONE);
                    StyledDialog.dismissLoading();

                    break;
                case 3:

                    App.getInstance().showToast("解析异常");
                    StyledDialog.dismissLoading();

                    break;
                case 4:

                    re_nolist.setVisibility(View.VISIBLE);
                    gv_searchaty.setVisibility(View.GONE);
                    tv_hottext.setVisibility(View.GONE);
                    StyledDialog.dismissLoading();

                    break;
                case 5:

                    tv_hottext.setVisibility(View.VISIBLE);
                    gv_searchaty.setVisibility(View.VISIBLE);
                    HotkeyAdp hotkeyadp = new HotkeyAdp(SearchActivity.this, strlists);
                    gv_searchaty.setAdapter(hotkeyadp);
                    StyledDialog.dismissLoading();

                    break;
                case 16:
                    tv_hottext.setVisibility(View.GONE);

                    break;
                case 6:
                    App.getInstance().showToast("暂无热门推荐");
                    break;
            }

        }
    };


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Search Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
