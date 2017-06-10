package com.digoshop.app.module.looksales.xianshi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.digoshop.R;

/**
 * 竞拍详情中的---竞拍记录list
 */
public class ShoptabdetailsActivity extends Activity {

    private TextView Text;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sale_shoptab_details);
        Text = (TextView) findViewById(R.id.tv_shopsinodata);
        Text.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        text = intent.getStringExtra("text");
        if (TextUtils.isEmpty(text)) {
            Text.setText("暂无竞拍记录");
        } else {
            Text.setText(text);
        }

    }


}
