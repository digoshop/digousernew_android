package com.digoshop.app.module.userCenter.module;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.base.BaseActivity;

/**
 * TODO<积分说明>
 *
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016年9月15日 下午10:43:03
 * @version: V1.0
 */
public class MyjifenContentActivity extends BaseActivity {
    private WebView wb_jifen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myjifencontent);
        wb_jifen = (WebView) findViewById(R.id.wb_jifen);
        wb_jifen.getSettings().setDefaultTextEncodingName("utf-8");
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("积分说明");

        try {
            // 本地文件处理(如果文件名中有空格需要用+来替代)
            wb_jifen.loadUrl("file:///android_asset/jifenabout.html");
        } catch (Exception ex) {
            ex.printStackTrace();
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
