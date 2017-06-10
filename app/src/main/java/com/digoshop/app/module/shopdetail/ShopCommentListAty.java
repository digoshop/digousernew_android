package com.digoshop.app.module.shopdetail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.shopdetailnew.adp.ShopCommentAdp;
import com.digoshop.app.module.shopdetailnew.model.ShopComment;
import com.digoshop.app.module.userCenter.model.CommentData;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;

public class ShopCommentListAty extends BaseActivity implements OnClickListener {
    private LinearLayout lin_shopdetail_allcomment, lin_shopdetail_good, lin_shopdetail_middle_good, lin_shopdetail_poor;
    private String shopid;
    private CommentData commentdata;
    private RelativeLayout re_nolist;
    private ListView lv_shopcommentlist;
    private ArrayList<ShopComment> commentinfos;
    private ArrayList<ShopComment> commentinfosNew = new ArrayList<>();
    private String typestr = "";//(全部空，好0，中1，差2)
    private PullToRefreshLayout ptrl;
    private int page = 1;
    private int page_length = 10;
    private ShopCommentAdp shopcommentadp;
    private String max_id = "";
    private String min_id = "";
    private TextView tv_allpingnum, tv_goodpingnum, tv_zhongpingnum, tv_chapingnum;
    private TextView tv_goodpingnumtext, tv_zhongpingnumtext, tv_chapingnumtext, tv_allpingnumtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopcommentlistaty);
        tv_goodpingnumtext = (TextView) findViewById(R.id.tv_goodpingnumtext);
        tv_zhongpingnumtext = (TextView) findViewById(R.id.tv_zhongpingnumtext);
        tv_chapingnumtext = (TextView) findViewById(R.id.tv_chapingnumtext);
        tv_allpingnumtext = (TextView) findViewById(R.id.tv_allpingnumtext);

        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("店铺评价");
        Intent intent = getIntent();
        shopid = intent.getStringExtra("sid");
        tv_allpingnum = (TextView) findViewById(R.id.tv_allpingnum);
        tv_goodpingnum = (TextView) findViewById(R.id.tv_goodpingnum);
        tv_zhongpingnum = (TextView) findViewById(R.id.tv_zhongpingnum);
        tv_chapingnum = (TextView) findViewById(R.id.tv_chapingnum);
        re_nolist = (RelativeLayout) findViewById(R.id.re_nolist);
        lv_shopcommentlist = (ListView) findViewById(R.id.lv_shopcommentlist);

        lin_shopdetail_good = (LinearLayout) findViewById(R.id.lin_shopdetail_good);
        lin_shopdetail_allcomment = (LinearLayout) findViewById(R.id.lin_shopdetail_allcomment);
        lin_shopdetail_allcomment.setOnClickListener(this);

        lin_shopdetail_middle_good = (LinearLayout) findViewById(R.id.lin_shopdetail_middle_good);
        lin_shopdetail_poor = (LinearLayout) findViewById(R.id.lin_shopdetail_poor);
        lin_shopdetail_good.setOnClickListener(this);
        lin_shopdetail_middle_good.setOnClickListener(this);
        lin_shopdetail_poor.setOnClickListener(this);
        ptrl = ((PullToRefreshLayout) findViewById(R.id.refresh_view));
        if (getNetWifi()) {
            getShopCommentList();
        } else {
            App.getInstance().showToast("网络不给力，请检查网络设置");
        }
        shopcommentadp = new ShopCommentAdp(ShopCommentListAty.this, commentinfosNew);
        lv_shopcommentlist.setAdapter(shopcommentadp);
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (commentinfosNew.size() > 0) {
                    commentinfosNew.clear();
                }
                max_id = "";
                min_id = "";
                page = 1;
                page_length = 10;
                getShopCommentList();
                shopcommentadp.notifyDataSetChanged();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (commentinfos != null) {
                    if (commentinfos.size() > 0) {
                        commentinfos.clear();
                    }

                    page = page + 1;
                    page_length = 10;
                    getShopCommentList();
                    shopcommentadp.appendData(commentinfos);
                }

                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }
        });
    }

    private void getShopCommentList() {
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getShopCommentListApi();
            }
        }).start();
    }

    private void getShopCommentListApi() {

        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {

            commentdata = api.get_shopcomment_list(shopid, max_id, "", "10", typestr);
            if (commentdata != null && !"".equals(commentdata)) {
                max_id = commentdata.getMin_id();
                commentinfos = commentdata.getShopInfoBeans();
                handler.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(2);
            }
        } catch (JSONException e) {
            handler.sendEmptyMessage(3);
            Log.v("ceshi", "JSONException");
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(2);
            Log.v("ceshi", "WSError" + e.getMessage());
            e.printStackTrace();
        }

    }

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    re_nolist.setVisibility(View.GONE);
                    commentinfosNew.addAll(commentinfos);
                    if (TextUtils.isEmpty(typestr)) {
                        if (commentdata != null) {
                            Log.v("ceshi","commentdata.getTotal()"+commentdata.getTotal());
                            if(!TextUtils.isEmpty(commentdata.getTotal())){
                                tv_allpingnum.setText("(" + commentdata.getTotal() + ")");
                            }
                        }
                    }
                    if (commentdata != null) {
                        tv_goodpingnum.setText("(" + commentdata.getGood() + ")");
                        tv_zhongpingnum.setText("(" + commentdata.getNormal() + ")");
                        tv_chapingnum.setText("(" + commentdata.getBad() + ")");
                    } else {
                        tv_goodpingnum.setText("");
                        tv_zhongpingnum.setText("");
                        tv_chapingnum.setText("");
                    }

                    shopcommentadp.notifyDataSetChanged();
                    StyledDialog.dismissLoading();


                    break;
                case 2:
                    App.getInstance().showToast("没有数据了");
                    re_nolist.setVisibility(View.GONE);
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

            }
        }
    };

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
            case R.id.lin_shopdetail_allcomment:// 全部
                tv_allpingnum.setTextColor(getResources().getColor(R.color.custom_tab_orange));
                tv_allpingnumtext.setTextColor(getResources().getColor(R.color.custom_tab_orange));
                tv_goodpingnum.setTextColor(getResources().getColor(R.color.textname));
                tv_goodpingnumtext.setTextColor(getResources().getColor(R.color.textname));
                tv_zhongpingnum.setTextColor(getResources().getColor(R.color.textname));
                tv_zhongpingnumtext.setTextColor(getResources().getColor(R.color.textname));
                tv_chapingnum.setTextColor(getResources().getColor(R.color.textname));
                tv_chapingnumtext.setTextColor(getResources().getColor(R.color.textname));
                typestr = "";
                if (commentinfosNew.size() > 0) {
                    commentinfosNew.clear();
                }
                max_id = "";
                min_id = "";
                page = 1;
                page_length = 10;
                getShopCommentList();
                shopcommentadp.notifyDataSetChanged();
                break;
            case R.id.lin_shopdetail_good:// 好评
                tv_allpingnum.setTextColor(getResources().getColor(R.color.textname));
                tv_allpingnumtext.setTextColor(getResources().getColor(R.color.textname));
                tv_goodpingnum.setTextColor(getResources().getColor(R.color.custom_tab_orange));
                tv_goodpingnumtext.setTextColor(getResources().getColor(R.color.custom_tab_orange));
                tv_zhongpingnum.setTextColor(getResources().getColor(R.color.textname));
                tv_zhongpingnumtext.setTextColor(getResources().getColor(R.color.textname));
                tv_chapingnum.setTextColor(getResources().getColor(R.color.textname));
                tv_chapingnumtext.setTextColor(getResources().getColor(R.color.textname));
                typestr = "0";
                if (commentinfosNew.size() > 0) {
                    commentinfosNew.clear();
                }
                max_id = "";
                min_id = "";
                page = 1;
                page_length = 10;
                getShopCommentList();
                shopcommentadp.notifyDataSetChanged();


                break;
            case R.id.lin_shopdetail_middle_good:// 中评
                tv_allpingnum.setTextColor(getResources().getColor(R.color.textname));
                tv_allpingnumtext.setTextColor(getResources().getColor(R.color.textname));
                tv_goodpingnum.setTextColor(getResources().getColor(R.color.textname));
                tv_goodpingnumtext.setTextColor(getResources().getColor(R.color.textname));
                tv_zhongpingnum.setTextColor(getResources().getColor(R.color.custom_tab_orange));
                tv_zhongpingnumtext.setTextColor(getResources().getColor(R.color.custom_tab_orange));
                tv_chapingnum.setTextColor(getResources().getColor(R.color.textname));
                tv_chapingnumtext.setTextColor(getResources().getColor(R.color.textname));
                typestr = "1";
                if (commentinfosNew.size() > 0) {
                    commentinfosNew.clear();
                }
                max_id = "";
                min_id = "";
                page = 1;
                page_length = 10;
                getShopCommentList();
                shopcommentadp.notifyDataSetChanged();

                break;
            case R.id.lin_shopdetail_poor:// 差评
                tv_allpingnum.setTextColor(getResources().getColor(R.color.textname));
                tv_allpingnumtext.setTextColor(getResources().getColor(R.color.textname));
                tv_goodpingnum.setTextColor(getResources().getColor(R.color.textname));
                tv_goodpingnumtext.setTextColor(getResources().getColor(R.color.textname));
                tv_zhongpingnum.setTextColor(getResources().getColor(R.color.textname));
                tv_zhongpingnumtext.setTextColor(getResources().getColor(R.color.textname));
                tv_chapingnum.setTextColor(getResources().getColor(R.color.custom_tab_orange));
                tv_chapingnumtext.setTextColor(getResources().getColor(R.color.custom_tab_orange));
                typestr = "2";
                if (commentinfosNew.size() > 0) {
                    commentinfosNew.clear();
                }
                max_id = "";
                min_id = "";
                page = 1;
                page_length = 10;
                getShopCommentList();
                shopcommentadp.notifyDataSetChanged();
                break;

            default:
                break;
        }

    }

}
