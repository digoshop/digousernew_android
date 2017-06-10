package com.digoshop.app.module.setting;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.base.BaseActivity;

/**
 * Created by lsqbeyond on 2016/11/3.
 */

public class AppIntroduceActivity extends BaseActivity implements View.OnClickListener{
    private WebView wb_about;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appintroduceaty);
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("功能介绍");
        wb_about = (WebView) findViewById(R.id.wb_about);
        wb_about.getSettings().setDefaultTextEncodingName("utf-8");
        try {
            // 本地文件处理(如果文件名中有空格需要用+来替代)
            wb_about.loadUrl("file:///android_asset/appabout.html");
        } catch (Exception ex) {
            ex.printStackTrace();
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
    public void onClick(View view) {
        switch (view.getId()){

        }
    }
}
