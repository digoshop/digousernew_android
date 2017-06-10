package com.digoshop.app.module.userCenter.module;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.shopdetailnew.ShopDetailNewSNActivity;
import com.digoshop.app.module.userCenter.adp.MyLikeAdp;
import com.digoshop.app.module.userCenter.model.MyLikeListData;
import com.digoshop.app.module.userCenter.model.MyLikesEntity;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;

/**
 * TODO 我的关注
 *
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016-6-11 下午10:31:16
 * @version: V1.0
 */
public class MyLikeActivity extends BaseActivity implements OnClickListener {
    private ListView lv_mylike;
    private MyLikeAdp myLikeAdp;
    private ArrayList<MyLikesEntity> MleList;
    private ArrayList<MyLikesEntity> MleListNew = new ArrayList<>();
    private int page = 1;
    private int page_length = 10;
    private PullToRefreshLayout refresh_store_view;
    private MyLikeListData mylikelikedata;
    private TextView tv_follow;
    private String totalnumstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercenter_mylike);
        TextView    tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("我的关注");
        lv_mylike = (ListView) findViewById(R.id.lv_mylike);
        tv_follow = (TextView) findViewById(R.id.tv_follow);
        refresh_store_view = ((PullToRefreshLayout)findViewById(R.id.refresh_store_view));
        refresh_store_view.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (MleListNew.size() > 0) {
                    MleListNew.clear();
                }
                page = 1;
                get_MyLikes();
                myLikeAdp.notifyDataSetChanged();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if(MleList!=null){
                    if (MleList.size() > 0) {
                        MleList.clear();
                    }
                    page = page + 1;
                    get_MyLikes();
                    myLikeAdp.appendData(MleList);
                }

                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }
        });
        myLikeAdp = new MyLikeAdp(MyLikeActivity.this, MleListNew);
        lv_mylike.setAdapter(myLikeAdp);



        lv_mylike.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                Intent intent = new Intent();
                intent.putExtra("gpsjui", Tool.getGpskmorm(MleListNew.get(position).getDistance()));
                intent.putExtra("sid", MleListNew.get(position).getSid());
                intent.setClass(MyLikeActivity.this, ShopDetailNewSNActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getNetWifi()) {
            StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
             if(MleListNew!=null){
                 if (MleListNew.size() > 0) {
                     MleListNew.clear();
                 }
             }
             if(myLikeAdp!=null){
                 myLikeAdp.notifyDataSetChanged();
             }
            get_MyLikes();
        } else {
            App.getInstance().showToast("网络不给力，请检查网络设置");
        }

    }

    private void get_MyLikes() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                get_My_LikesArray();
            }
        }).start();
    }


    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    tv_follow.setText("您已关注" + totalnumstr + "家商户");
                    MleListNew.addAll(MleList);
                    myLikeAdp.notifyDataSetChanged();
                    StyledDialog.dismissLoading();

                    break;
                case 2:
                    App.getInstance().showToast("暂无数据！");
                    refresh_store_view.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    StyledDialog.dismissLoading();

                    break;
                case 3:
                    App.getInstance().showToast("请求失败，请稍后再试");
                    StyledDialog.dismissLoading();

                    break;
                case 4:
                    App.getInstance().showToast("请求失败，请稍后再试");
                    StyledDialog.dismissLoading();

                    break;

            }
        }
    };


    // 获取我的关注列表
    private void get_My_LikesArray() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        MleList = new ArrayList<MyLikesEntity>();
        try {
            mylikelikedata = api.getMyLikeslist(page, page_length);
            Log.i("TEST", "获取到" + MleList);
            if (mylikelikedata != null) {
                MleList = mylikelikedata.getArLikesEntities();
                totalnumstr = mylikelikedata.getTotal();
                if(MleList!=null){
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(2);
                }
                Log.v("TEST", "333" + MleList.size());
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
        switch (v.getId()) {


            default:
                break;
        }
    }

}
