package com.digoshop.app.module.seekservice;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.customServices.adp.CategoryChooseAdp;
import com.digoshop.app.module.customServices.adp.CategoryChooseTwoAdp;
import com.digoshop.app.module.customServices.model.CategoryChooseBean;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;


public class CategoryChooseServiceActivity extends BaseActivity   {
    private TextView tv_dataTwo;
    private ArrayList<CategoryChooseBean> categoryChooseBeansfuwu;
    private ArrayList<CategoryChooseBean> categoryChooseBeanssahngpin;
    private ListView lv_Classtwo, lv_Classone;
    public static final String KEY_TYPE_STR = "KEY_TYPE_STR";
    public static final String KEY_TYPE_NAME = "KEY_TYPE_NAME";
    public static final String KEY_TYPE_ID = "KEY_TYPE_ID";
    public static final String KEY_TYPE_ONENAME = "KEY_TYPE_ONENAME";
    private String type = "1";
    private CategoryChooseAdp adapter;
    private CategoryChooseTwoAdp Twoadapter;
    private String moidOne, moidTwe, moidname ;
    private TextView tv_title_content;
   private String nameOne="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(true);
        setContentView(R.layout.categorychooseaty);

        tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        lv_Classone = (ListView) findViewById(R.id.lv_Classone);
        lv_Classtwo = (ListView) findViewById(R.id.lv_Classtwo);
        tv_dataTwo = (TextView) findViewById(R.id.tv_dataTwo);
        tv_title_content.setText("请选择服务类别");
        type = "2";
        getCategory(type);

        lv_Classone.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int location, long arg3) {
                adapter.setChoicePos(location);
                adapter.notifyDataSetChanged();
                moidOne = categoryChooseBeansfuwu.get(location).getMoid();
                nameOne = categoryChooseBeansfuwu.get(location).getName();
                tv_title_content.setText("请选择服务类别");
                type = "2";
                getCategoryTwo(type, moidOne);

            }
        });
        lv_Classtwo.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int location, long arg3) {
                Twoadapter.setChoicePos(location);
                Twoadapter.notifyDataSetChanged();

                moidname = categoryChooseBeanssahngpin.get(location).getName();
                Log.v("lsq", "moidname+" + moidname);
                if (moidname.equals("全部")) {
                    moidTwe = moidOne;
                } else {
                    moidTwe = categoryChooseBeanssahngpin.get(location).getMoid();
                }
                handler.sendMessageDelayed(handler.obtainMessage(10), 300);
            }
        });

    }


    private void getCategory(final String types) {
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getCategoryChoose(types);
            }
        }).start();
    }

    private void getCategoryTwo(final String type2, final String moid2) {
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getCategoryChooseTwo(type2, moid2);
            }
        }).start();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    adapter = new CategoryChooseAdp(CategoryChooseServiceActivity.this, categoryChooseBeansfuwu);
                    adapter.setChoicePos(0);
                    lv_Classone.setAdapter(adapter);
                    handler.sendEmptyMessage(9);
                    StyledDialog.dismissLoading();

                    break;
                case 2:
                    App.getInstance().showToast("暂无数据！");
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

                    Twoadapter = new CategoryChooseTwoAdp(CategoryChooseServiceActivity.this, categoryChooseBeanssahngpin);
                    lv_Classtwo.setAdapter(Twoadapter);
                    tv_dataTwo.setVisibility(View.GONE);
                    lv_Classtwo.setVisibility(View.VISIBLE);
                    StyledDialog.dismissLoading();

                    break;
                case 6:
                    tv_dataTwo.setVisibility(View.VISIBLE);
                    lv_Classtwo.setVisibility(View.GONE);
                    StyledDialog.dismissLoading();

                    break;
                case 7:
                    App.getInstance().showToast("解析异常");
                    StyledDialog.dismissLoading();

                    break;
                case 8:
                    App.getInstance().showToast("请求异常");
                    StyledDialog.dismissLoading();

                    break;
                case 9:
                    if (categoryChooseBeansfuwu == null | "".equals(categoryChooseBeansfuwu) || categoryChooseBeansfuwu.size() == 0) {

                    } else {
                        moidOne = categoryChooseBeansfuwu.get(0).getMoid();
                        getCategoryTwo(type, categoryChooseBeansfuwu.get(0).getMoid());
                    }

                    break;
                case 10:
                    if (TextUtils.isEmpty(moidTwe)) {
                        App.getInstance().showToast("请选择具体二级类别");
                    } else {
                        Log.v("lsq", "moidname2+" + moidname);
                        if (TextUtils.isEmpty(nameOne)) {
                            if (categoryChooseBeansfuwu != null) {
                                if (categoryChooseBeansfuwu.size() > 0) {
                                    nameOne = categoryChooseBeansfuwu.get(0).getName();

                                }
                            }
                        }
                        Intent intent = new Intent();
                        intent.putExtra(KEY_TYPE_NAME, moidname);
                        intent.putExtra(KEY_TYPE_ID, moidTwe);
                        intent.putExtra(KEY_TYPE_ONENAME, nameOne);

                        setResult(1, intent);
                        finish();
                    }
                    break;
            }
        }
    };

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

    private void getCategoryChooseTwo(String type2, String moidtow) {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        categoryChooseBeanssahngpin = new ArrayList<CategoryChooseBean>();
        try {
            categoryChooseBeanssahngpin = api.get_managing_type(type2, "2", moidtow, "", "", "30", "1");
            if (categoryChooseBeanssahngpin == null || "".equals(categoryChooseBeanssahngpin)) {
                handler.sendEmptyMessage(6);
            } else {
                Log.v("lsq", "categoryChooseBeanssahngpin1+" + categoryChooseBeanssahngpin.size());
                CategoryChooseBean categoryChooseBean = new CategoryChooseBean();
                categoryChooseBean.setName("全部");
                categoryChooseBeanssahngpin.add(0, categoryChooseBean);
                handler.sendEmptyMessage(5);
            }
        } catch (JSONException e) {
            handler.sendEmptyMessage(7);
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(6);
            e.printStackTrace();
        } catch (Exception e) {
            handler.sendEmptyMessage(6);
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
}
