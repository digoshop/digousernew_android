package com.digoshop.app.module.looksales.poitsale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.digoshop.R;

/**
 * Created by lsqbeyond on 2016/10/29.
 */

public class SaletabDetailActivity extends Activity {

    private TextView Text;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saletabdetailactivity);


        Text=(TextView)findViewById(R.id.textView1);

        Intent intent=getIntent();
        text=intent.getStringExtra("text");
        Text.setText(text);
    }


}
