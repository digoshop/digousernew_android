package com.digoshop.app.module.userCenter.module;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.userCenter.adp.MyreplyAdp;
import com.digoshop.app.module.userCenter.model.CommentData;
import com.digoshop.app.module.userCenter.model.CommentInfo;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;

/**
 * TODO 我的评论列表
 *
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016-6-11 下午10:31:16
 * @version: V1.0
 */
public class MyReplyActivity extends BaseActivity{
    private ListView lv_myreply;
    private ImageView  iv_comment1;
    private CommentData commentdata;
    private String totalnumstr;
    private ArrayList<CommentInfo> commentinfos= new ArrayList<>();
    private ArrayList<CommentInfo> commentinfosNew = new ArrayList<>();
    private TextView tv_replynum;
    private RelativeLayout re_nolist;
    private PullToRefreshLayout ptrl;
    private int page = 1;
    private int page_length = 10;
    private MyreplyAdp myreplyAdp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercenter_myreply);
        TextView    tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("我的评论");
        re_nolist = (RelativeLayout) findViewById(R.id.re_nolist);
        lv_myreply = (ListView) findViewById(R.id.lv_myreply);
        tv_replynum = (TextView) findViewById(R.id.tv_replynum);
        lv_myreply.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                //App.getInstance().showToast((arg2+1)+"个");
            }
        });
        if(getNetWifi()){
            StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
            get_comment_list();
        }else{
            App.getInstance().showToast("网络不给力，请检查网络设置");
        }
        commentRefresh();
        myreplyAdp = new MyreplyAdp(MyReplyActivity.this, commentinfosNew);
        lv_myreply.setAdapter(myreplyAdp);
    }

    public void commentRefresh() {
        ptrl = ((PullToRefreshLayout) findViewById(R.id.refresh_view));
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (commentinfosNew.size() > 0) {
                    commentinfosNew.clear();
                }
                page = 1;
                page_length = 10;
                get_comment_list();
                myreplyAdp.notifyDataSetChanged();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);

            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if(commentinfos!=null){
                    if (commentinfos.size() > 0) {
                        commentinfos.clear();
                    }
                    page = page + 1;
                    page_length = 10;
                    get_comment_list();
                    myreplyAdp.appendData(commentinfos);
                }
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }
        });

    }


    private void get_comment_list() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                get_comment_listapi();
            }
        }).start();
    }

    private void get_comment_listapi() {

        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            commentdata = api.get_comment_list(page + "", page_length + "", "1");
            if (commentdata != null) {
                commentinfos = commentdata.getCommentinfos();
                totalnumstr = commentdata.getTotal();

                handler.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(2);
            }
        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        } catch (WSError e) {
            String msg=e.getMessage();
            if(msg.equals("-99")){
                handler.sendEmptyMessage(4);
            }

            Log.v("ceshi", "WSError");
            e.printStackTrace();
        }

    }


    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    re_nolist.setVisibility(View.GONE);
                    tv_replynum.setText(totalnumstr);
                    commentinfosNew.addAll(commentinfos);
                    myreplyAdp.notifyDataSetChanged();
                    StyledDialog.dismissLoading();

                    break;
                case 2:
                    re_nolist.setVisibility(View.GONE);
                    StyledDialog.dismissLoading();
                    break;
                case 3:
                    App.getInstance().showToast("解析异常");
                    StyledDialog.dismissLoading();

                    break;
                case 4:
                    ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    App.getInstance().showToast("没有数据了!");
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
        return false;
    }

}
