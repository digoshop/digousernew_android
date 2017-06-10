package com.digoshop.app.module.home.cityselect;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.home.cityselect.adapter.CityListAdapter;
import com.digoshop.app.module.home.cityselect.adapter.ResultListAdapter;
import com.digoshop.app.module.home.cityselect.model.City;
import com.digoshop.app.module.home.cityselect.model.HotCityBean;
import com.digoshop.app.module.home.cityselect.model.LocateState;
import com.digoshop.app.module.home.cityselect.view.SideLetterBar;
import com.digoshop.app.module.home.cityselect.view.SideLetterBarNoHot;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.db.AssetsDataBasesManage;
import com.digoshop.app.utils.db.CityBean;
import com.digoshop.app.utils.db.SQLiteDataBases_db;
import com.digoshop.app.utils.http.WSError;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * author zaaach on 2016/1/26.
 * 城市选择aty
 */
public class CityPickerActivity extends BaseActivity implements View.OnClickListener {
    public static final int REQUEST_CODE_PICK_CITY = 2333;
    public static final String KEY_PICKED_CITY = "picked_city";
    private ListView mListView;
    private ListView mResultListView;
    private SideLetterBar mLetterBar;
    private   SideLetterBarNoHot mLetterBarno;
    private EditText searchBox;
    private ImageView clearBtn;
    private ViewGroup emptyView;

    private CityListAdapter mCityAdapter;
    private ResultListAdapter mResultAdapter;
    private List<City> mAllCities;

    private AssetsDataBasesManage manage;
    private SQLiteDatabase db;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public boolean isNoTitle() {
        return true;
    }

    public ArrayList<City> mCities;

    @Override
    public boolean isFullScreen() {
        return false;
    }

    private ArrayList<HotCityBean> hotCityBeens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citypickaty);
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("城市选择");
        Intent intent = getIntent();
        hotCityBeens = (ArrayList<HotCityBean>) intent.getSerializableExtra("hotCityBeens");
        AssetsDataBasesManage.initManager(CityPickerActivity.this);
        manage = AssetsDataBasesManage.getManager();
        db = manage.getDatabase(AssetsDataBasesManage.coursesDBName);
        mResultAdapter = new ResultListAdapter(this, null);
        initView();
        String cityopentime = null;
        cityopentime = LocalSave.getValue(CityPickerActivity.this, AppConfig.basefile, "cityopentime", "");
        Log.v("lsq", "cityopentime+" + cityopentime);
        Log.v("lsq", "cityopentime122+" + Tool.getCurrentDate());
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        //  if (!Tool.getCurrentDate().equals(cityopentime)) {//说明今天没有更新过
        getOpenCloseCity();
//        } else {
//           getAllCityData();
//          }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void getOpenCloseCity() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getOpenCloseCityapi();
            }
        }).start();

    }

    ArrayList<CityBean> quyulistsNew;

    private void getOpenCloseCityapi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            quyulistsNew = api.upCityLocation();
            if (quyulistsNew != null) {
                handler.sendEmptyMessage(17);
            } else {
                handler.sendEmptyMessage(18);
            }
        } catch (JSONException e) {
            handler.sendEmptyMessage(18);
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(18);
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mCityAdapter = new CityListAdapter(CityPickerActivity.this, mAllCities, mCities);
                    mListView.setAdapter(mCityAdapter);
                    mCityAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
                        @Override
                        public void onCityClick(String name, String citycode) {
                            back(name, citycode);
                        }

                        @Override
                        public void onLocateClick() {
                            Log.e("onLocateClick", "重新定位...");
                            mCityAdapter.updateLocateState(LocateState.LOCATING, null, "");
                        }
                    });
                    StyledDialog.dismissLoading();

                    break;
                case 17:
                    boolean update = new SQLiteDataBases_db().upDaCityTypeNew(db, quyulistsNew);
                    Log.v("lsq", "DDDDDDDDDD17" + update);
                    if (update) {
                        LocalSave.putValue(CityPickerActivity.this, AppConfig.basefile, "cityopentime", Tool.getCurrentDate() + "");
                    }
                    getAllCityData();
                    break;
                case 18:
                    getAllCityData();
                    break;


            }

        }
    };

    private void getAllCityData() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                setdata();

            }
        }).start();
    }

    private void setdata() {
        boolean type = new SQLiteDataBases_db().checkColumnExists(db, "type");
        Log.v("lsq", "DDDDDDDDDD" + type);
        if (type) {
            Log.v("lsq", "DDDDDDDDDD333333333333");
            if (mAllCities != null) {
                mAllCities.clear();
            }
            mAllCities = new SQLiteDataBases_db().getAllOpenCityLists(db);
        } else {
            Log.v("lsq", "DDDDDDDDDD44444444444");
            mAllCities = new SQLiteDataBases_db().getAllCities(db);
        }

        //暂无热门城市
        mCities = new ArrayList<>();
        if (hotCityBeens != null && !hotCityBeens.isEmpty()) {

            for (int i = 0; i < hotCityBeens.size(); i++) {
                City city = new City();
                city.setName(hotCityBeens.get(i).getCity());
                Log.v("lsq", "hotCityBeens()+" + city.getName());
                city.setCitycode(hotCityBeens.get(i).getCityCode());
                mCities.add(city);
            }
        }
//        else {
////            City city = new City();
////            city.setName("暂无");
////            Log.v("lsq", "hotCityBeens()+" + city.getName());
////            city.setCitycode("");
//            City city = new City();
//            city.setName("");
//            Log.v("lsq", "hotCityBeens()+" + city.getName());
//            city.setCitycode("");
//            mCities.add(city);
//        }
        handler.sendEmptyMessage(1);

    }


    private void initView() {
        mListView = (ListView) findViewById(R.id.listview_all_city);


        TextView overlay = (TextView) findViewById(R.id.tv_letter_overlay);
        mLetterBar = (SideLetterBar) findViewById(R.id.side_letter_bar);
        mLetterBarno = (SideLetterBarNoHot) findViewById(R.id.side_letter_bar_nohot);

        if (hotCityBeens != null && !hotCityBeens.isEmpty()) {
            mLetterBar.setVisibility(View.VISIBLE);
            mLetterBar.setOverlay(overlay);
            try {
                mLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
                    @Override
                    public void onLetterChanged(String letter) {

                        int position = mCityAdapter.getLetterPosition(letter);
                        mListView.setSelection(position);
                    }
                });
            } catch (Exception e) {
            }
        }else{
            mLetterBarno.setVisibility(View.VISIBLE);
            mLetterBarno.setOverlay(overlay);
            try {
                mLetterBarno.setOnLetterChangedListener(new SideLetterBarNoHot.OnLetterChangedListener() {
                    @Override
                    public void onLetterChanged(String letter) {

                        int position = mCityAdapter.getLetterPosition(letter);
                        mListView.setSelection(position);
                    }
                });
            } catch (Exception e) {
            }
        }





        searchBox = (EditText) findViewById(R.id.et_search);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                if (TextUtils.isEmpty(keyword)) {
                    clearBtn.setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);
                    mResultListView.setVisibility(View.GONE);
                } else {
                    clearBtn.setVisibility(View.VISIBLE);
                    mResultListView.setVisibility(View.VISIBLE);
                    List<City> result = null;
                    boolean type = new SQLiteDataBases_db().checkColumnExists(db, "type");
                    if (type) {
                        result = new SQLiteDataBases_db().searchCityType(db, keyword);
                    } else {
                        result = new SQLiteDataBases_db().searchCity(db, keyword);
                    }
                    if (result == null || result.size() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                        mResultAdapter.changeData(result);
                    }
                }
            }
        });

        emptyView = (ViewGroup) findViewById(R.id.empty_view);
        mResultListView = (ListView) findViewById(R.id.listview_search_result);
        mResultListView.setAdapter(mResultAdapter);
        mResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                back(mResultAdapter.getItem(position).getName(), mResultAdapter.getItem(position).getCitycode());
            }
        });

        clearBtn = (ImageView) findViewById(R.id.iv_search_clear);
        clearBtn.setOnClickListener(this);
    }

    private void back(String city, String citycode) {
        Toast.makeText(this, "点击的城市：" + city, Toast.LENGTH_SHORT).show();
        Intent data = new Intent();

        data.putExtra("cityname", city);
        data.putExtra("citycode", citycode);
        Log.v("test", "1citycode" + citycode);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search_clear:
                searchBox.setText("");
                clearBtn.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
                mResultListView.setVisibility(View.GONE);
                break;

        }
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("CityPicker Page") // TODO: Define a title for the content shown.
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
