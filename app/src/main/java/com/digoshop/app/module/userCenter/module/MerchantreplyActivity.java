package com.digoshop.app.module.userCenter.module;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.userCenter.adp.MerchantreplyAdapter;
import com.digoshop.app.module.userCenter.model.CurlistEntity;
import com.digoshop.app.module.userCenter.model.CustomizedReply;
import com.digoshop.app.module.userCenter.model.Markbean;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * TODO<定制详情>
 *
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016年9月16日 上午1:23:36
 * @version: V1.0
 */
public class MerchantreplyActivity extends BaseActivity implements OnClickListener {
    private ListView lv_Curlist;
    private ArrayList<CurlistEntity> Curlist;
    private TextView tv_name, tv_customservice_time, tv_Replynumber, tv_customservice_item_type, tv_sign;
    String cids;
    private CustomizedReply Customized;
    private String mst;
    private int selposition;
    MerchantreplyAdapter merchantreplyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchantreply);
        TextView    tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("商家回复");
        lv_Curlist = (ListView) findViewById(R.id.lv_Curlist);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_customservice_time = (TextView) findViewById(R.id.tv_customservice_time);
        tv_Replynumber = (TextView) findViewById(R.id.tv_Replynumber);
        tv_customservice_item_type = (TextView) findViewById(R.id.tv_customservice_item_type);
        tv_sign = (TextView) findViewById(R.id.tv_sign);
        Intent intent = getIntent();
        cids = intent.getStringExtra("cid");
        Log.i("TEST", "获取到的ID" + cids);
        getcustservice_content(cids);
    }

    private void getcustservice_content(final String cid) {
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getCustServicecontentList(cid);
            }
        }).start();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if(Curlist!=null){
                        if(Curlist.size()>selposition){
                            mst = Curlist.get(selposition).getMst();
                        }
                    }
                    merchantreplyAdapter = new MerchantreplyAdapter(MerchantreplyActivity.this, Curlist, messageClickListener);
                    lv_Curlist.setAdapter(merchantreplyAdapter);
                    tv_name.setText(Tool.isNullStr(Customized.getTn()));
                    long n = Long.parseLong(Customized.getEt());
                    // 结束时间
                    String Ytime = Tool.getUninxToJavaDayJ(Customized.getEt());
                    tv_customservice_time.setText("截止日期:" + Tool.isNullStr(Ytime));
                    tv_Replynumber.setText(Tool.isNullStr(Customized.getRc()));
                    String type = Customized.getTp();
                    if (type.equals("1")) {
                        tv_customservice_item_type.setText("定制服务");
                    } else if (type.equals("2")) {
                        tv_customservice_item_type.setText("商品采购");
                    }
                    tv_sign.setText(Tool.isNullStr("(" + Customized.getMc() + ")"));
                    StyledDialog.dismissLoading();

                    break;
                case 2:
                    App.getInstance().showToast("返回为空");
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
                    getcustservice_content(cids);
                    break;
                case 7:
                    App.getInstance().showToast("标记失败!");
                    break;

            }
        }
    };

    private void getCustServicecontentList(String strid) {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        Customized = new CustomizedReply();
        Curlist = new ArrayList<CurlistEntity>();
        try {//"1000004"
            Customized = api.get_custservice_content(strid);
            Curlist = Customized.getArrayList();
            if (Customized != null) {
                handler.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(7);
            }
        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            handler.sendEmptyMessage(7);
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(7);
            Log.v("ceshi", "WSError");
            e.printStackTrace();
        }
    }

    @Override
    public boolean isNoTitle() {
        // TODO Auto-generated method stub
        return true;
    }

    /**
     * 实现类，响应按钮点击事件 店铺监听
     */
    private MerchantreplyAdapter.MyMessageClickListener messageClickListener = new MerchantreplyAdapter.MyMessageClickListener() {
        @Override
        public void myOnClick(final int position, View v) {
            selposition = position;
//			deleteint = position;
//			delete_message();
            //1未标记  2已标记
            if ("1".equals(mst)) {
                Csmst(position, 2);
            } else if ("2".equals(mst)) {
                Csmst(position, 1);
            }
        }
    };

    private void Csmst(final int position, final int type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CsmstApi(position, type);
            }
        }).start();
    }

    private Markbean markbean;

    private void CsmstApi(int position, int type) {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            markbean = api.CusttomMst(Curlist.get(position).getId(), type + "");
            if (markbean != null) {
                mst = markbean.getMst();
                handler.sendEmptyMessage(5);
            } else {
                handler.sendEmptyMessage(6);
            }
        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            //   handler.sendEmptyMessage(3);
            e.printStackTrace();
        } catch (WSError e) {
            //  handler.sendEmptyMessage(4);
            Log.v("ceshi", "WSError");
            e.printStackTrace();
        }
    }

    @Override
    public boolean isFullScreen() {
        // TODO Auto-generated method stub
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
