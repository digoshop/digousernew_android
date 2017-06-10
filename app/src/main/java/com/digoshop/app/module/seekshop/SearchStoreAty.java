package com.digoshop.app.module.seekshop;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.customServices.model.CategoryChooseBean;
import com.digoshop.app.module.home.couponactive.ActivityDetails;
import com.digoshop.app.module.home.model.ActivityDetailBean;
import com.digoshop.app.module.search.ScanActivity;
import com.digoshop.app.module.search.SearchActivity;
import com.digoshop.app.module.seekshop.adp.SeekShopOneCateAdp;
import com.digoshop.app.module.seekshop.adp.SeekShopTwoAdp;
import com.digoshop.app.module.seekshop.model.SeekCateGoryData;
import com.digoshop.app.module.shopdetailnew.ShopDetailNewSNActivity;
import com.digoshop.app.module.storedetail.StoreDetailActivity;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.DensityUtil;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;

public class SearchStoreAty extends BaseActivity implements OnClickListener, OnItemClickListener {
    private ListView lv_seekshop_category;
    private ArrayList<CategoryChooseBean> categorychoosbeansone;
    private ArrayList<CategoryChooseBean> categorychoosbeansthree;//二级类别的list数据里面包含三级的数据
    private ArrayList<CategoryChooseBean> categorychoosbeansthreeNew = new ArrayList<CategoryChooseBean>();

    private ListView lv_seekshopcategory;
    private ImageView iv_zhaosp_searchlist_go;
    private EditText et_homefragment_searchlist;
    private ListView lv_search_aty;
    private String CityCodestr = "";
    private String latstr = "";
    private String logstr = "";
    private SeekShopOneCateAdp seekshopadp;
    private RelativeLayout re_nolist;
    private DisplayImageOptions options;

    private int page = 1;
    private int page_length = 10;
    private SeekShopTwoAdp seekShopTwoAdp;
    private String moidtwo;
    private ImageView iv_seekservice_ad;
    private PullToRefreshLayout refresh_store_view;
    private ScrollView pullsv;
    private RelativeLayout lin_search_saomao;
    private boolean isonclick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchstoreaty);
        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.defaulhomehuan)
                .showImageForEmptyUri(R.drawable.defaulhomehuan).showImageOnFail(R.drawable.defaulhomehuan).cacheInMemory()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
                .build();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        re_nolist = (RelativeLayout) findViewById(R.id.re_nolist);
        pullsv = (ScrollView) findViewById(R.id.pullsv);
        lin_search_saomao = (RelativeLayout) findViewById(R.id.lin_search_saomao);
        lin_search_saomao.setOnClickListener(this);
        iv_seekservice_ad = (ImageView) findViewById(R.id.iv_seekservice_ad);
        iv_seekservice_ad.setOnClickListener(this);
        et_homefragment_searchlist = (EditText) findViewById(R.id.et_homefragment_searchlist);
        et_homefragment_searchlist.setHint(Tool.edithint("寻找您喜欢的店铺", DensityUtil.px2dip(App.getInstance(), (float) getResources().getDimensionPixelSize(R.dimen.base_dimen_32))));
        lv_seekshopcategory = (ListView) findViewById(R.id.lv_seekshopcategory);
        lv_seekshopcategory.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // 三级类别
                Intent intent = new Intent();
                intent.putExtra("operateId",
                        categorychoosbeansthree.get(arg2).getMoid());
                intent.putExtra("operatename",
                        categorychoosbeansthree.get(arg2).getName());
                intent.setClass(SearchStoreAty.this, SeekShopListAty.class);
                startActivity(intent);
            }
        });
        et_homefragment_searchlist.setOnKeyListener(new View.OnKeyListener() {

            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
// 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchStoreAty.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    if (!TextUtils.isEmpty(et_homefragment_searchlist.getText().toString())) {
                        String searchkeywordstr = et_homefragment_searchlist.getText().toString();
                        searchkeywordstr = searchkeywordstr.replaceAll(" ", "");
                        if (TextUtils.isEmpty(searchkeywordstr)) {
                            App.getInstance().showToast("请输入内容!");
                        } else {

                            Intent intent = new Intent();
                            intent.setClass(SearchStoreAty.this, SearchActivity.class);
                            intent.putExtra("searchkeyword", et_homefragment_searchlist.getText().toString());
                            startActivity(intent);
                        }
                    } else {
                        App.getInstance().showToast("请输入搜索内容");
                    }
                }
                return false;
            }
        });
        iv_zhaosp_searchlist_go = (ImageView) findViewById(R.id.iv_zhaosp_searchlist_go);
        iv_zhaosp_searchlist_go.setOnClickListener(this);
        lv_seekshop_category = (ListView) findViewById(R.id.lv_seekshop_category);
        lv_seekshop_category.setOnItemClickListener(this);

        CityCodestr = LocalSave.getValue(SearchStoreAty.this, AppConfig.basefile, "CityCode", "");
        latstr = LocalSave.getValue(SearchStoreAty.this, AppConfig.basefile, "lat", "");
        logstr = LocalSave.getValue(SearchStoreAty.this, AppConfig.basefile, "lon", "");
        if (getNetWifi()) {
            StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
            getSeekShopOneCategory();
        } else {
            App.getInstance().showToast("网络不给力，请检查网络设置");
            finish();
        }
        refresh_store_view = ((PullToRefreshLayout) findViewById(R.id.refresh_store_view));
        refresh_store_view.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (categorychoosbeansthreeNew.size() > 0) {
                    categorychoosbeansthreeNew.clear();
                }
                page = 1;
                getSeekShopTwoCategory(moidtwo);
                ;
                seekShopTwoAdp.notifyDataSetChanged();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (categorychoosbeansthree != null) {
                    if (categorychoosbeansthree.size() > 0) {
                        categorychoosbeansthree.clear();
                    }
                    page = page + 1;
                    getSeekShopTwoCategory(moidtwo);
                    ;
                    seekShopTwoAdp.appendData(categorychoosbeansthree);
                }
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }
        });
        seekShopTwoAdp = new SeekShopTwoAdp(SearchStoreAty.this, categorychoosbeansthreeNew);
        lv_seekshopcategory.setAdapter(seekShopTwoAdp);


    }

    private void getSeekShopOneCategory() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getSeekShopOneCategoryApi();
            }
        }).start();
    }

    private void getSeekShopOneCategoryApi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        categorychoosbeansone = new ArrayList<CategoryChooseBean>();
        try {
            categorychoosbeansone = api.get_managing_type("1", "1", "", "", "", "30", "1");
            if (categorychoosbeansone != null) {
                if (categorychoosbeansone.size() > 0) {
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
            handler.sendEmptyMessage(4);
            Log.v("ceshi", "WSError");
            e.printStackTrace();
        }
    }

    private void getSeekShopTwoCategory(final String onecate) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getSeekShopTwoCategoryApi(onecate);
            }
        }).start();
    }

    private ArrayList<ActivityDetailBean> atydetails;
    private SeekCateGoryData seekcatedata;

    private void getSeekShopTwoCategoryApi(String moid) {
        Log.v("lsq", "moid+" + moid);
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {// child当请求二级的时候如果child=3则找商铺把二级和连带的三级一块搜索出来
            // categorychoosbeans = api.get_managing_type("1", "2", onecate,
            // CityCodestr, "3","10", "1");
            seekcatedata = api.get_managing_twothree("1", "2", moid, CityCodestr, "3", page_length + "", page + "");

            if (seekcatedata != null) {
                categorychoosbeansthree = seekcatedata.getCategorychoosbeans();
                atydetails = seekcatedata.getAtydetails();
                if (seekcatedata == null || "".equals(seekcatedata)) {
                    handler.sendEmptyMessage(6);
                } else {
                    handler.sendEmptyMessage(5);
                }
            } else {
                handler.sendEmptyMessage(6);
            }

        } catch (JSONException e) {
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(6);
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    seekshopadp = new SeekShopOneCateAdp(SearchStoreAty.this, categorychoosbeansone);
                    if (categorychoosbeansone.size() >= 0) {
                        // 因为现在只有精品钟表有数据所以默认加载她
                        seekshopadp.setSelectItem(0);
                        if (categorychoosbeansone.size() > 0) {
                            moidtwo = categorychoosbeansone.get(0).getMoid();
                            getSeekShopTwoCategory(moidtwo);
                        }
                    }
                    lv_seekshop_category.setAdapter(seekshopadp);

                    lv_seekshop_category.setItemsCanFocus(true);// 让ListView的item获得焦点
                    lv_seekshop_category.setChoiceMode(ListView.CHOICE_MODE_SINGLE);// 单选模式
                    // 默认第一个item被选中
                    lv_seekshop_category.setItemChecked(0, true);
                    break;
                case 2:
                    App.getInstance().showToast("暂无一级类别");
                    StyledDialog.dismissLoading();

                    break;
                case 5:

                    pullsv.smoothScrollTo(0, 20);
                    categorychoosbeansthreeNew.addAll(categorychoosbeansthree);
                    seekShopTwoAdp.notifyDataSetChanged();
                    if (seekcatedata.getAtydetails() != null &
                            seekcatedata.getAtydetails().size() > 0) {


                        DisplayMetrics dm = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(dm);
                        int screenWidth = dm.widthPixels;
                        int screenHeight = dm.heightPixels;
                        ViewGroup.LayoutParams params = iv_seekservice_ad.getLayoutParams();
                        params.height = ((screenWidth * 75 / 100) - App.getInstance().getSeekAd()) * 3 / 7;
                        params.width = (screenWidth * 75 / 100) - App.getInstance().getSeekAd();
                        iv_seekservice_ad.setLayoutParams(params);
                        Log.v("ceshi", "");
                        iv_seekservice_ad.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(seekcatedata.getAtydetails().get(0).getMnp(),
                                iv_seekservice_ad,
                                options);
                    } else {
                        iv_seekservice_ad.setVisibility(View.GONE);
                    }
                    re_nolist.setVisibility(View.GONE);
                    StyledDialog.dismissLoading();

                    break;
                case 6:
                    App.getInstance().showToast("没有数据了");
                    StyledDialog.dismissLoading();

                    refresh_store_view.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    break;
                case 3:
                    App.getInstance().showToast("解析异常");
                    StyledDialog.dismissLoading();

                    break;
                case 4:
                    App.getInstance().showToast("请求异常");
                    StyledDialog.dismissLoading();

                    break;
            }
        }
    };

    @Override
    public boolean isNoTitle() {
        return true;
    }

    @Override
    public boolean isFullScreen() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.lin_search_saomao:
                if (isonclick) {
                    isonclick = false;
                    getTodoPermission();
                }

                break;
            case R.id.iv_zhaosp_searchlist_go:
                if (!TextUtils.isEmpty(et_homefragment_searchlist.getText().toString())) {
                    String searchkeywordstr = et_homefragment_searchlist.getText().toString();
                    searchkeywordstr = searchkeywordstr.replaceAll(" ", "");
                    if (TextUtils.isEmpty(searchkeywordstr)) {
                        App.getInstance().showToast("请输入内容!");
                    } else {
                        intent.setClass(SearchStoreAty.this, SearchActivity.class);
                        intent.putExtra("searchkeyword", et_homefragment_searchlist.getText().toString());
                        startActivity(intent);
                    }
                } else {
                    App.getInstance().showToast("请输入搜索内容");
                }
                break;
            case R.id.iv_seekservice_ad:
                if (seekcatedata.getAtydetails() != null &
                        seekcatedata.getAtydetails().size() > 0) {
                    intent.setClass(SearchStoreAty.this, ActivityDetails.class);
                    Bundle bundle = new Bundle();
                    ActivityDetailBean homeActivityInfo = new ActivityDetailBean();
                    homeActivityInfo.setMnid(atydetails.get(0).getMnid());
                    bundle.putSerializable("atycontent", homeActivityInfo);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

                //               break;
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
        isonclick = true;
        Intent intent = new Intent();
        intent.setClass(SearchStoreAty.this, ScanActivity.class);
        startActivityForResult(intent, 0);
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
        if (TextUtils.isEmpty(results)) {
            App.getInstance().showToast("扫描失败，请重试!");
        } else {
            //
            // {"shop_id":1000048, "type":2} //商铺
            // {"shop_id":1000048, "type":1} //商场
            // {"uid":"103232432432434"} //用户id
            JSONObject json;
            try {
                json = new JSONObject(results);
                Intent intent = new Intent();
                if (!TextUtils.isEmpty(json.optString("type"))) {
                    if (json.optString("type").equals("2")) {
                        intent.setClass(SearchStoreAty.this, ShopDetailNewSNActivity.class);
                        intent.putExtra("sid", json.optString("shop_id"));
                        startActivity(intent);
                        finish();
                    } else if (json.optString("type").equals("1")) {
                        intent.setClass(SearchStoreAty.this, StoreDetailActivity.class);
                        intent.putExtra("storeid", json.optString("shop_id"));
                        startActivity(intent);
                        finish();
                    } else {
                        App.getInstance().showToast("系统无法识别非本系统二维码");

                    }
                } else {
                    App.getInstance().showToast("系统无法识别非本系统二维码");

                }


            } catch (Exception e) {
                App.getInstance().showToast("系统无法识别非本系统二维码");
                finish();
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
        // 一级类别
        if (categorychoosbeansone.size() > 0) {
            StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
            seekshopadp.setSelectItem(arg2);
            if (categorychoosbeansthreeNew != null) {
                categorychoosbeansthreeNew.clear();
            }
            page = 1;
            moidtwo = categorychoosbeansone.get(arg2).getMoid();
            getSeekShopTwoCategory(moidtwo);
            seekshopadp.notifyDataSetInvalidated();
            Log.i("TEST", "获取到的getMoid" + categorychoosbeansone.get(arg2).getMoid());
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
