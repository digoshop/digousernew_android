package com.digoshop.app.module.arrountshops;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.bigkoo.pickerview.TimePickerView;
import com.digoshop.R;
import com.digoshop.app.base.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lsqbeyond on 2016/11/28.
 */

public class TestAty extends BaseActivity {
    TimePickerView pvTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atestaty);
        //时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
       Calendar calendar = Calendar.getInstance();
        pvTime.setTitle("选择截止日期");
       pvTime.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR )+1);//要在setTime 之前才有效果哦
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                Log.v("lsq","getTime(date)+"+getTime(date));
            }
        });

    }
    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(date);
    }
    public void btn(View v) {
        pvTime.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(pvTime.isShowing() ){
                pvTime.dismiss();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
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
