package com.digoshop.app.module.userCenter.module;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.shopdetail.bigimg.aty.ImagePagerActivity;
import com.digoshop.app.module.userCenter.model.CommentInfo;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.Displayer;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONException;

import static com.digoshop.app.module.shopdetail.bigimg.aty.ImagePagerActivity.EXTRA_IMAGE_URLS;
import static com.digoshop.app.utils.Tool.getNetWifi;

public class MyCommentDetailAty extends BaseActivity implements View.OnClickListener {
    private String sid, eid, name, logo;
    private CommentInfo commentInfo;
    private TextView tv_user_comname, tv_user_comtime, tv_user_comtype, tv_user_comtext, tv_shop_comname, tv_shop_comtime, tv_shop_comtext;
    private ImageView iv_user_com_icon, iv_myreply_type, iv_comment1, iv_comment2, iv_comment3, iv_shop_com_icon;
    private DisplayImageOptions options, optionstwo;
    private LinearLayout lin_shopre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comment_detail_aty);
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("评价详情");
        Intent intent = getIntent();
        lin_shopre = (LinearLayout) findViewById(R.id.lin_shopre);
        tv_user_comname = (TextView) findViewById(R.id.tv_user_comname);
        tv_user_comtime = (TextView) findViewById(R.id.tv_user_comtime);
        tv_user_comtype = (TextView) findViewById(R.id.tv_user_comtype);
        tv_user_comtext = (TextView) findViewById(R.id.tv_user_comtext);
        tv_shop_comname = (TextView) findViewById(R.id.tv_shop_comname);
        tv_shop_comtime = (TextView) findViewById(R.id.tv_shop_comtime);
        tv_shop_comtext = (TextView) findViewById(R.id.tv_shop_comtext);
        iv_user_com_icon = (ImageView) findViewById(R.id.iv_user_com_icon);
        iv_comment1 = (ImageView) findViewById(R.id.iv_comment1);
        iv_comment2 = (ImageView) findViewById(R.id.iv_comment2);
        iv_comment3 = (ImageView) findViewById(R.id.iv_comment3);
        iv_comment1.setOnClickListener(this);
        iv_comment2.setOnClickListener(this);
        iv_comment3.setOnClickListener(this);
        iv_shop_com_icon = (ImageView) findViewById(R.id.iv_shop_com_icon);
        iv_myreply_type = (ImageView) findViewById(R.id.iv_myreply_type);
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.defaultyuan)
                .showImageForEmptyUri(R.drawable.defaultyuan)
                .showImageOnFail(R.drawable.defaultyuan)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)   //设置图片的解码类型
                .displayer(new Displayer(0))
                .build();
        optionstwo = new DisplayImageOptions.Builder().showStubImage(R.drawable.kcx_001)
                .showImageForEmptyUri(R.drawable.kcx_001).showImageOnFail(R.drawable.kcx_001).cacheInMemory()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
                .build();
        sid = intent.getStringExtra("sid");
        eid = intent.getStringExtra("eid");
        logo = intent.getStringExtra("logo");
        name = intent.getStringExtra("name");
        if (getNetWifi()) {
            StyledDialog.buildLoading(MyCommentDetailAty.this, App.getInstance().getString(R.string.onloading), true, false).show();
            getComment();
        } else {
            App.getInstance().showToast("网络不给力，请检查网络设置");
        }
    }

    private void getComment() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getCommentapi();
            }
        }).start();
    }

    private void getCommentapi() {

        try {
            DigoIUserApiImpl api = new DigoIUserApiImpl();
            commentInfo = api.getCommentDetail(eid);
            if (commentInfo != null) {
                handler.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(2);
            }
        } catch (JSONException e) {
            handler.sendEmptyMessage(2);
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(2);
            String msg = e.getMessage();
            if (msg.equals("-1302")) {
                handler.sendEmptyMessage(1302);
            }
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {
        Intent intent = new Intent();

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //用户名
                    String userendname = LocalSave.getValue(App.getInstance(), AppConfig.basefile, "userendname", "");
                    tv_user_comname.setText(userendname);
                    if (!TextUtils.isEmpty(commentInfo.getCreate_time())) {
                        tv_user_comtime.setText(Tool.TimeStamp2Date(commentInfo.getCreate_time(),""));
                    }
                    if (!TextUtils.isEmpty(commentInfo.getType())) {
                        if (commentInfo.getType().equals("0")) {// 0好评 1中评 2 差评
                            tv_user_comtype.setText("好评");
                            iv_myreply_type.setBackgroundResource(R.drawable.dppj_hao);
                        } else if (commentInfo.getType().equals("1")) {
                            tv_user_comtype.setText("中评");
                            iv_myreply_type.setBackgroundResource(R.drawable.dppj_zhong);
                        } else if (commentInfo.getType().equals("2")) {
                            tv_user_comtype.setText("差评");
                            iv_myreply_type.setBackgroundResource(R.drawable.dppj_cha);
                        }
                    }
                    tv_user_comtext.setText(commentInfo.getText());

                    if (!TextUtils.isEmpty(commentInfo.getReply_time())) {
                        tv_shop_comtime.setText(Tool.TimeStamp2Date(commentInfo.getReply_time(),""));
                    }
                    if (commentInfo.getImgs().size() > 0) {


                        if (commentInfo.getImgs().size() == 1) {
                            Log.v("lsq", "__" + commentInfo.getImgs().get(0));
                            iv_comment1.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(commentInfo.getImgs().get(0), iv_comment1, optionstwo);
                        } else if (commentInfo.getImgs().size() == 2) {
                            iv_comment1.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(commentInfo.getImgs().get(0),
                                    iv_comment1, optionstwo);
                            iv_comment2.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(commentInfo.getImgs().get(1),
                                    iv_comment2, optionstwo);
                        } else if (commentInfo.getImgs().size() == 3) {
                            iv_comment1.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(commentInfo.getImgs().get(0),
                                    iv_comment1, optionstwo);
                            iv_comment2.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(commentInfo.getImgs().get(1),
                                    iv_comment2, optionstwo);
                            iv_comment3.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(commentInfo.getImgs().get(2),
                                    iv_comment3, optionstwo);
                        } else if (commentInfo.getImgs().size() == 0) {
                            iv_comment1.setVisibility(View.GONE);
                            iv_comment2.setVisibility(View.GONE);
                            iv_comment2.setVisibility(View.GONE);
                        }
                    }
                    //用户头像
                    String userimg = LocalSave.getValue(App.getInstance(), AppConfig.basefile, "userimg", "");
                    ImageLoader.getInstance().displayImage(userimg, iv_user_com_icon,
                            options);
                    //商户头像
                    ImageLoader.getInstance().displayImage(logo, iv_shop_com_icon,
                            options);

                    if (!TextUtils.isEmpty(commentInfo.getReply_text())) {
                        lin_shopre.setVisibility(View.VISIBLE);
                        tv_shop_comtext.setText(commentInfo.getReply_text());
                        tv_shop_comname.setText(name);

                    } else {
                        lin_shopre.setVisibility(View.GONE);

                    }
                    StyledDialog.dismissLoading();
                    break;
                case 2:
                    App.getInstance().showToast("请求失败");
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


    @Override
    public void onClick(View view) {
        Intent intenta = new Intent(MyCommentDetailAty.this, ImagePagerActivity.class);
        switch (view.getId()) {
            case R.id.iv_comment1:
                // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                intenta.putExtra(EXTRA_IMAGE_URLS, commentInfo.getImgs());
                intenta.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
                startActivity(intenta);
                break;
            case R.id.iv_comment2:
                // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                intenta.putExtra(EXTRA_IMAGE_URLS, commentInfo.getImgs());
                intenta.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
                startActivity(intenta);
                break;
            case R.id.iv_comment3:

                // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                intenta.putExtra(EXTRA_IMAGE_URLS, commentInfo.getImgs());
                intenta.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
                startActivity(intenta);
                break;
        }
    }
}
