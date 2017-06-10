package com.digoshop.app.module.seekservice;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.arrountshops.adp.ArroutShopAdp;
import com.digoshop.app.module.arrountshops.model.ShopInfoBean;
import com.digoshop.app.module.customServices.model.CategoryChooseBean;
import com.digoshop.app.module.home.couponactive.ActivityDetails;
import com.digoshop.app.module.home.model.ActivityDetailBean;
import com.digoshop.app.module.search.ScanActivity;
import com.digoshop.app.module.search.SearchActivity;
import com.digoshop.app.module.seekservice.newview.GridViewAdapter;
import com.digoshop.app.module.seekservice.newview.Model;
import com.digoshop.app.module.seekservice.newview.ViewPagerAdapter;
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
import java.util.List;

import static com.digoshop.app.utils.Tool.getNetWifi;


public class SeekServiceActivitynew extends BaseActivity implements OnClickListener, OnItemClickListener {
    private ImageView iv_zaosp_return;
    private RelativeLayout lin_search_saomao;
    private ListView lv_seekservice;
    private ImageView iv_seekservice_ad, iv_zhaosp_searchlist_go;
    private ArrayList<CategoryChooseBean> categoryChooseBeansfuwu;
    private ScrollView sv_seekservice;
    private String CityCodestr = "";
    private ArrayList<ShopInfoBean> shoplists;
    private ActivityDetailBean hotatybean;
    private DisplayImageOptions options;
    private RelativeLayout re_seekservice_hot;
    private EditText et_homefragment_searchlist;
    private ViewPager mPager;
    private List<View> mPagerList;
    private List<Model> mDatas;
    private LinearLayout mLlDot;
    private LayoutInflater inflater;
    /**
     * 总的页数
     */
    private int pageCount;
    /**
     * 每一页显示的个数
     */
    private int pageSize = 10;
    /**
     * 当前显示的是第几页
     */
    private int curIndex = 0;
    private boolean isonclick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        options = new DisplayImageOptions.Builder()
                // .displayer(new RoundedBitmapDisplayer(45))
                .showStubImage(R.drawable.defaulhomehuan).showImageForEmptyUri(R.drawable.defaulhomehuan)
                .showImageOnFail(R.drawable.defaulhomehuan).cacheInMemory().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc().build();
        setContentView(R.layout.activity_seekservice);
        mPager = (ViewPager) findViewById(R.id.viewpager);
        mLlDot = (LinearLayout) findViewById(R.id.ll_dot);
        lin_search_saomao = (RelativeLayout) findViewById(R.id.lin_search_saomao);
        lin_search_saomao.setOnClickListener(this);
        inflater = LayoutInflater.from(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        CityCodestr = LocalSave.getValue(SeekServiceActivitynew.this, AppConfig.basefile, "CityCode", "");
        initView();
        if (getNetWifi()) {
            getCategory("2");
        } else {
            App.getInstance().showToast("网络不给力，请检查网络设置");
            finish();
        }
    }

    private void getCategory(final String types) {
        if (TextUtils.isEmpty(CityCodestr)) {
            App.getInstance().showToast("定位获取失败,默认北京");
            CityCodestr = "010";
        }
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getCategoryChoose(types);
                getGoodShops();
                getLookSeekAtyApi();
            }
        }).start();
    }

    private void getGoodShops() {

        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            shoplists = api.GoodShops("SERVICE_GOOD_SHOP", CityCodestr);
            if (shoplists != null) {
                handler.sendEmptyMessage(5);
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


    private void getLookSeekAtyApi() {

        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            ArrayList<ActivityDetailBean> hotatybeans;
            hotatybeans = api.getLookSeekAty("", CityCodestr, "", "", "ER_INNER_SERVICE");
            if (hotatybeans != null) {
                if (hotatybeans.size() > 0) {
                    hotatybean = hotatybeans.get(0);
                    if (hotatybean != null) {
                        handler.sendEmptyMessage(6);
                    }
                }

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

    private void getCategoryChoose(String typeid) {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        categoryChooseBeansfuwu = new ArrayList<CategoryChooseBean>();
        try {
            categoryChooseBeansfuwu = api.get_managing_type(typeid, "1", "", "", "", "30", "1");
            if (categoryChooseBeansfuwu != null) {
                handler.sendEmptyMessage(1);
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


    /**
     * 初始化数据源
     */
    private void initCateData() {

        mDatas = new ArrayList<Model>();
        for (int i = 0; i < categoryChooseBeansfuwu.size(); i++) {
            //动态获取资源ID，第一个参数是资源名，第二个参数是资源类型例如drawable，string等，第三个参数包名
            Model model = new Model();
            model.setName(categoryChooseBeansfuwu.get(i).getName());
            model.setIconUrl(categoryChooseBeansfuwu.get(i).getIcon());
            model.setCateid(categoryChooseBeansfuwu.get(i).getMoid());
            mDatas.add(model);
        }
        //总的页数=总数/每页数量，并取整
        pageCount = (int) Math.ceil(mDatas.size() * 1.0 / pageSize);
        mPagerList = new ArrayList<View>();
        for (int i = 0; i < pageCount; i++) {
            //每个页面都是inflate出一个新实例

            GridView gridView = (GridView) inflater.inflate(R.layout.gridviewseekservice, mPager, false);
            gridView.setAdapter(new GridViewAdapter(this, mDatas, i, pageSize));
            mPagerList.add(gridView);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int pos = position + curIndex * pageSize;
                    Log.v("ceshi", "po+" + pos);
                    Log.v("ceshi", "position+" + position);
                    Log.v("ceshi", "curIndex+" + curIndex);
                    Log.v("ceshi", "pageSize+" + pageSize);
                    //   Toast.makeText(SeekServiceActivitynew.this, mDatas.get(pos).getName()+"__"+mDatas.get(pos).getCateid(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    intent.setClass(SeekServiceActivitynew.this, SeekServiceListAty.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("categoryChooseBeans", categoryChooseBeansfuwu);
                    intent.putExtras(bundle);
                    intent.putExtra("operateId", mDatas.get(pos).getCateid());
                    intent.putExtra("operatename", mDatas.get(pos).getName());
                    startActivity(intent);
                }
            });
        }
        //设置适配器
        mPager.setAdapter(new ViewPagerAdapter(mPagerList));
        //设置圆点
        setOvalLayout();
    }

    /**
     * 设置圆点
     */
    public void setOvalLayout() {
        if (pageCount > 1) {
            mLlDot.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < pageCount; i++) {
            mLlDot.addView(inflater.inflate(R.layout.dot, null));
        }
        // 默认显示第一页
        mLlDot.getChildAt(0).findViewById(R.id.v_dot)
                .setBackgroundResource(R.drawable.dot_selected_main);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                // 取消圆点选中
                mLlDot.getChildAt(curIndex)
                        .findViewById(R.id.v_dot)
                        .setBackgroundResource(R.drawable.dot_normalnew);
                // 圆点选中
                mLlDot.getChildAt(position)
                        .findViewById(R.id.v_dot)
                        .setBackgroundResource(R.drawable.dot_selected_main);
                curIndex = position;
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    initCateData();
                    sv_seekservice.smoothScrollTo(0, 20);
                    StyledDialog.dismissLoading();

                    break;
                case 2:
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
                case 5:
                    if (shoplists != null & shoplists.size() > 0) {
                        re_seekservice_hot.setVisibility(View.VISIBLE);
                        lv_seekservice.setAdapter(new ArroutShopAdp(SeekServiceActivitynew.this, shoplists));
                    } else {
                        re_seekservice_hot.setVisibility(View.GONE);
                        // App.getInstance().showToast("暂无上榜好店");
                    }
                    sv_seekservice.smoothScrollTo(0, 20);
                    StyledDialog.dismissLoading();

                    break;
                case 6:
                    DisplayMetrics dm = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                    int screenWidth = dm.widthPixels;
                    int screenHeight = dm.heightPixels;
                    iv_seekservice_ad.setImageResource(R.drawable.defaulhomehuan);
                    ViewGroup.LayoutParams params = iv_seekservice_ad.getLayoutParams();
                    params.height = screenWidth * 3 / 7;
                    params.width = screenWidth;
                    iv_seekservice_ad.setLayoutParams(params);
                    iv_seekservice_ad.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(hotatybean.getMnp(), iv_seekservice_ad, options);
                    break;
            }
        }
    };

    private void initView() {
        iv_zhaosp_searchlist_go = (ImageView) findViewById(R.id.iv_zhaosp_searchlist_go);
        iv_zhaosp_searchlist_go.setOnClickListener(this);
        et_homefragment_searchlist = (EditText) findViewById(R.id.et_homefragment_searchlist);
        et_homefragment_searchlist.setHint(Tool.edithint(getString(R.string.seekservice_youerservice), DensityUtil.px2dip(App.getInstance(), (float) getResources().getDimensionPixelSize(R.dimen.base_dimen_32))));
        et_homefragment_searchlist.setOnKeyListener(new View.OnKeyListener() {

            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
// 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SeekServiceActivitynew.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    if (!TextUtils.isEmpty(et_homefragment_searchlist.getText().toString())) {
                        String searchkeywordstr = et_homefragment_searchlist.getText().toString();
                        searchkeywordstr = searchkeywordstr.replaceAll(" ", "");
                        if (TextUtils.isEmpty(searchkeywordstr)) {
                            App.getInstance().showToast("请输入内容!");
                        } else {

                            Intent intent = new Intent();
                            intent.setClass(SeekServiceActivitynew.this, SearchActivity.class);
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
        re_seekservice_hot = (RelativeLayout) findViewById(R.id.re_seekservice_hot);
        sv_seekservice = (ScrollView) findViewById(R.id.sv_seekservice);
        iv_seekservice_ad = (ImageView) findViewById(R.id.iv_seekservice_ad);
        iv_seekservice_ad.setOnClickListener(this);
        lv_seekservice = (ListView) findViewById(R.id.lv_seekservice);
        lv_seekservice.setOnItemClickListener(this);
        iv_zaosp_return = (ImageView) findViewById(R.id.iv_zaosp_return);
        iv_zaosp_return.setOnClickListener(this);
    }


    @Override
    public boolean isNoTitle() {
        return true;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_zaosp_return:
                finish();
                break;
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
                        intent.setClass(SeekServiceActivitynew.this, SearchActivity.class);
                        intent.putExtra("searchkeyword", et_homefragment_searchlist.getText().toString());
                        startActivity(intent);
                    }
                } else {
                    App.getInstance().showToast("请输入搜索内容");
                }
                break;
            case R.id.iv_seekservice_ad:
                Intent intentaty = new Intent();
                intentaty.setClass(SeekServiceActivitynew.this, ActivityDetails.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("atycontent", hotatybean);
                intentaty.putExtras(bundle);
                startActivity(intentaty);
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
        intent.setClass(SeekServiceActivitynew.this, ScanActivity.class);
        startActivityForResult(intent, 0);
        isonclick = true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
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
                        intent.setClass(SeekServiceActivitynew.this, ShopDetailNewSNActivity.class);
                        intent.putExtra("sid", json.optString("shop_id"));
                        startActivity(intent);
                        finish();
                    } else if (json.optString("type").equals("1")) {
                        intent.setClass(SeekServiceActivitynew.this, StoreDetailActivity.class);
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
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Intent intent = new Intent();
        intent.putExtra("gpsjui", Tool.getGpskmorm(shoplists.get(arg2).getDistance()));
        intent.putExtra("sid", shoplists.get(arg2).getSid());
        intent.setClass(SeekServiceActivitynew.this, ShopDetailNewSNActivity.class);
        startActivity(intent);

    }
}
