package com.digoshop.app.module.userCenter.module;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.userCenter.model.AddShopInviteUserInfo;
import com.digoshop.app.module.userCenter.model.ShopInviteDetailBean;
import com.digoshop.app.utils.Displayer;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lsqbeyond on 2016/10/27.
 */

public class ShopInviteDetailActivity extends BaseActivity implements View.OnClickListener {
    private String name, sid, cover;
    private ImageView iv_sava ;
    private DisplayImageOptions options;
    private ShopInviteDetailBean shopInviteDetailBean;
    private TextView tv_name,tv_shopinvite_sex,tv_shopinvite_birthday, tv_vip_code, tv_intg, tv_vip_level, tv_title_right;
    private EditText edit_shopinvite_mobile, edit_shopinvite_name,    edit_shopinvite_pro, edit_shopinvite_email, edit_shopinvite_address;
    private AddShopInviteUserInfo addShopInviteUserInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopinvitedetailaty);
        TextView    tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("填写会员信息");
        iv_sava = (ImageView) findViewById(R.id.iv_sava);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_vip_code = (TextView) findViewById(R.id.tv_vip_code);
        tv_intg = (TextView) findViewById(R.id.tv_intg);
        tv_vip_level = (TextView) findViewById(R.id.tv_vip_level);
        tv_title_right = (TextView) findViewById(R.id.tv_title_right);
        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setOnClickListener(this);
        tv_title_right.setText("保存");
        edit_shopinvite_mobile = (EditText) findViewById(R.id.edit_shopinvite_mobile);
        edit_shopinvite_name = (EditText) findViewById(R.id.edit_shopinvite_name);
        tv_shopinvite_sex = (TextView) findViewById(R.id.tv_shopinvite_sex);
        tv_shopinvite_sex.setOnClickListener(this);
        tv_shopinvite_birthday = (TextView) findViewById(R.id.tv_shopinvite_birthday);
        tv_shopinvite_birthday.setOnClickListener(this);
        edit_shopinvite_pro = (EditText) findViewById(R.id.edit_shopinvite_pro);
        edit_shopinvite_email = (EditText) findViewById(R.id.edit_shopinvite_email);
        edit_shopinvite_address = (EditText) findViewById(R.id.edit_shopinvite_address);


        //圆形图片
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.defaultyuan)
                .showImageForEmptyUri(R.drawable.defaultyuan)
                .showImageOnFail(R.drawable.defaultyuan)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)   //设置图片的解码类型
                .displayer(new Displayer(0))
                .build();
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        sid = intent.getStringExtra("sid");
        cover = intent.getStringExtra("cover");
        Log.v("ceshi", "sid" + sid);
        if (TextUtils.isEmpty(sid)) {
            App.getInstance().showToast("商家id为空");
            finish();
        }else{
            getShopInviteDetail();
        }
    }

    private void getShopInviteDetail() {
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.v("ceshi", "getShopInviteDetail1");
                getShopInviteDetailapi();
            }
        }).start();
    }

    private void getShopInviteDetailapi() {
        {
            Log.v("ceshi", "getShopInviteDetail2");
            DigoIUserApiImpl api = new DigoIUserApiImpl();
            try {
                Log.v("ceshi", "getShopInviteDetail3");
                shopInviteDetailBean = api.getShopInvitDetail(sid);
                if (shopInviteDetailBean != null & !"".equals(shopInviteDetailBean)) {
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(2);
                }

            } catch (JSONException e) {
                Log.v("ceshi", "JSONException");
                handler.sendEmptyMessage(3);
                e.printStackTrace();
            } catch (WSError e) {
                String code = e.getMessage();
                // if (code.equals("-4")||code.equals("0")) {
                handler.sendEmptyMessage(4);
                //  }
                Log.v("ceshi", "WSError");
                Log.i("zzreeee", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    ImageLoader.getInstance().displayImage(cover, iv_sava, options);
                    tv_name.setText(name);//商铺名字
                    tv_vip_code.setText("卡号:" + shopInviteDetailBean.getCode());//会卡号
                    tv_intg.setText( shopInviteDetailBean.getIntg());//总计分数
                    tv_vip_level.setText("等级:" + shopInviteDetailBean.getVip_level_name());//会等等级成名
                    edit_shopinvite_mobile.setText(shopInviteDetailBean.getMobile());//手机号
                    edit_shopinvite_name.setText(shopInviteDetailBean.getUserInfoData().getReal_name());
                    if (shopInviteDetailBean.getUserInfoData() != null) {
                        if ("1".equals(shopInviteDetailBean.getUserInfoData().getGender())) {
                            tv_shopinvite_sex.setText("男");
                        } else if ("2".equals(shopInviteDetailBean.getUserInfoData().getGender())) {
                            tv_shopinvite_sex.setText("女");
                        }
                        if(!TextUtils.isEmpty(shopInviteDetailBean.getUserInfoData().getBirthday())){
                            tv_shopinvite_birthday.setText(shopInviteDetailBean.getUserInfoData().getBirthday());

                        }
                        edit_shopinvite_pro.setText(shopInviteDetailBean.getUserInfoData().getOccupation());
                        edit_shopinvite_email.setText(shopInviteDetailBean.getUserInfoData().getEmail());
                        edit_shopinvite_address.setText(shopInviteDetailBean.getUserInfoData().getAddress());
                    }
                    StyledDialog.dismissLoading();

                    break;
                case 2:

                    Toast.makeText(ShopInviteDetailActivity.this, "获取会员卡号失败", Toast.LENGTH_SHORT).show();
                    StyledDialog.dismissLoading();

                    finish();
                    break;
                case 3:
                    StyledDialog.dismissLoading();
                    Toast.makeText(ShopInviteDetailActivity.this, "解析异常", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    StyledDialog.dismissLoading();
                    Toast.makeText(ShopInviteDetailActivity.this, "返回异常", Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    StyledDialog.dismissLoading();
                    Toast.makeText(ShopInviteDetailActivity.this, "保存成功!", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 8:
                    StyledDialog.dismissLoading();
                    Toast.makeText(ShopInviteDetailActivity.this, "保存失败!", Toast.LENGTH_SHORT).show();
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

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        DatePickerDialog dateDlg;
        switch (v.getId()) {
            case R.id.tv_title_right://保存
                if(TextUtils.isEmpty(edit_shopinvite_mobile.getText().toString())){
                    App.getInstance().showToast("请填写手机号");
                    return;
                }else if(TextUtils.isEmpty(edit_shopinvite_name.getText().toString())){
                    App.getInstance().showToast("请填写用户名");
                    return;
                }else if(tv_shopinvite_sex.getText().toString().equals("请选择")){
                    App.getInstance().showToast("请选择性别");
                    return;
                }else if(tv_shopinvite_birthday.getText().toString().equals("请选择日期")){
                    App.getInstance().showToast("请选择日期");
                    return;
                }else if(TextUtils.isEmpty(edit_shopinvite_pro.getText().toString())){
                    App.getInstance().showToast("请填写职业");
                    return;
                }else if(TextUtils.isEmpty(edit_shopinvite_email.getText().toString())){
                    App.getInstance().showToast("请填写邮箱");
                    return;
                }else if(TextUtils.isEmpty(edit_shopinvite_address.getText().toString())){
                    App.getInstance().showToast("请填写地址");
                    return;
                }else if(!checkEmaile(edit_shopinvite_email.getText().toString())){
                    App.getInstance().showToast("请填写正确邮箱格式!");
                    return;
                }
                Log.v("ceshi","sex"+tv_shopinvite_sex.getText().toString());
                Log.v("ceshi","tv_shopinvite_birthday"+tv_shopinvite_birthday.getText().toString());
                addShopInviteUserInfo = new AddShopInviteUserInfo();
                addShopInviteUserInfo.setSid(sid);
                addShopInviteUserInfo.setCode(shopInviteDetailBean.getCode());
                addShopInviteUserInfo.setAddress(edit_shopinvite_address.getText().toString());
                addShopInviteUserInfo.setBirthday(tv_shopinvite_birthday.getText().toString());
                addShopInviteUserInfo.setEmail(edit_shopinvite_email.getText().toString());
                if ("男".equals(tv_shopinvite_sex.getText().toString())) {
                    addShopInviteUserInfo.setGen("1");
                } else if ("女".equals(tv_shopinvite_sex.getText().toString())) {
                    addShopInviteUserInfo.setGen("2");
                }
                addShopInviteUserInfo.setMobile(edit_shopinvite_mobile.getText().toString());
                addShopInviteUserInfo.setOccupation(edit_shopinvite_pro.getText().toString());
                addShopInviteUserInfo.setUser_name(edit_shopinvite_name.getText().toString());
                addShopInviteDetail();
                break;
            case R.id.tv_shopinvite_sex://性别选择
                SexDialog();
                break;
            case R.id.tv_shopinvite_birthday:
                dateDlg = new DatePickerDialog(this, selectdate, dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH), dateAndTime.get(Calendar.DAY_OF_MONTH));
                dateDlg.show();
                break;

        }

    }
    /**
     * 正则表达式校验邮箱
     * @param emaile 待匹配的邮箱
     * @return 匹配成功返回true 否则返回false;
     */
    private static boolean checkEmaile(String emaile){
        String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        //正则表达式的模式
        Pattern p = Pattern.compile(RULE_EMAIL);
        //正则表达式的匹配器
        Matcher m = p.matcher(emaile);
        //进行正则匹配
        return m.matches();
    }
    // 当点击DatePickerDialog控件的设置按钮时，调用该方法
    DatePickerDialog.OnDateSetListener selectdate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // 修改日历控件的年，月，日
            // 这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            tv_shopinvite_birthday.setText(df.format(dateAndTime.getTime()));
        }
    };
    DatePickerDialog.OnDateSetListener selectdates = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // 修改日历控件的年，月，日
            // 这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            tv_shopinvite_birthday.setText(df.format(dateAndTime.getTime()));
        }
    };
    // 获取一个日历对象
    Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
    private void SexDialog() {
        final String items[] = { "男", "女"};
        boolean isselect = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(this); // 先得到构造器
        builder.setTitle("修改性别"); // 设置标题
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tv_shopinvite_sex.setText(items[which] + "");
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v("ceshi","which+"+which);
//                if("请选择".equals(tv_shopinvite_sex.getText().toString())){
//                    tv_shopinvite_sex.setText("男");
//                }
               // tv_shopinvite_sex.setText(items[which] + "");
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    private void addShopInviteDetail() {
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.v("ceshi", "getShopInviteDetail1");
                addShopInviteDetailapi();
            }
        }).start();
    }
    private void addShopInviteDetailapi() {
        {
            DigoIUserApiImpl api = new DigoIUserApiImpl();
            try {
                boolean  isreturn = api.AddShopInvite(addShopInviteUserInfo);
                if (isreturn) {
                    handler.sendEmptyMessage(7);
                } else {
                    handler.sendEmptyMessage(8);
                }

            } catch (JSONException e) {
                Log.v("ceshi", "JSONException");
                handler.sendEmptyMessage(3);
                e.printStackTrace();
            } catch (WSError e) {
                String code = e.getMessage();
                // if (code.equals("-4")||code.equals("0")) {
                handler.sendEmptyMessage(4);
                //  }
                Log.v("ceshi", "WSError");
                Log.i("zzreeee", e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
