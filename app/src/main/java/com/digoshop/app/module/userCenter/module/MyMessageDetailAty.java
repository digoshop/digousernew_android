package com.digoshop.app.module.userCenter.module;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

/**
 * Created by lsqbeyond on 2016/10/24.
 */

public class MyMessageDetailAty extends BaseActivity {
    private WebView wv_messagedetil;
    private String nidstr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

        setContentView(R.layout.mymessagedetialaty);
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);

        Intent intent = getIntent();
        String titlestr = intent.getStringExtra("title");
        if(TextUtils.isEmpty(titlestr)){
            tv_title_content.setText("消息详情");
        }else{
            tv_title_content.setText(titlestr);
        }
        String weburl = intent.getStringExtra("weburl");
        nidstr =   intent.getStringExtra("nid");
        if(!TextUtils.isEmpty(nidstr)){
            update_notice(nidstr);
        }
        wv_messagedetil = (WebView) findViewById(R.id.wv_messagedetil);
        //启用支持javascript
        WebSettings settings = wv_messagedetil.getSettings();
        settings.setJavaScriptEnabled(true);
        if(!TextUtils.isEmpty(weburl)){
            wv_messagedetil.loadUrl(weburl);
        }
        wv_messagedetil.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                StyledDialog.dismissLoading();
            }
        });
    }
    private void update_notice(final String id) {
        Log.v("ceshi", "id++" + id);
        new Thread(new Runnable() {
            @Override
            public void run() {
                update_noticeapi(id);
            }
        }).start();
    }

    private void update_noticeapi(String id) {

        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {

            boolean messageData = api.update_notice(id);

        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            e.printStackTrace();
        } catch (WSError e) {
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
}
