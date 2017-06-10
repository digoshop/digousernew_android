package com.digoshop.app.module.customServices;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.CannedAccessControlList;
import com.alibaba.sdk.android.oss.model.CreateBucketRequest;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.model.IPickerViewData;
import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.module.arrountshops.newbean.PickerViewData;
import com.digoshop.app.module.arrountshops.newbean.ProvinceBean;
import com.digoshop.app.module.customServices.view.SimpleGrid;
import com.digoshop.app.module.login.Loginaty;
import com.digoshop.app.module.storedetail.model.UserInfoData;
import com.digoshop.app.module.userCenter.module.MyCustiomGSListActivity;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.DensityUtil;
import com.digoshop.app.utils.KeyboardUtils;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.lljjcoder.citypickerview.widget.CityPicker;

import org.json.JSONException;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.digoshop.app.utils.AppConfig.accessKeyId;
import static com.digoshop.app.utils.AppConfig.accessKeySecret;
import static com.digoshop.app.utils.AppConfig.endpoint;
import static com.digoshop.app.utils.AppConfig.pj;
import static com.digoshop.app.utils.AppConfig.testBucket;
import static com.digoshop.app.utils.Tool.getNetWifi;

public class CustomServiceActivity extends TakePhotoActivity
        implements View.OnClickListener, SimpleGrid.Callback {
    private Button bt_customservice_submit;
    private EditText et_custom_service_thing, et_custom_service_thing_add;
    private TextView et_custom_service_type, typetext;
    private EditText et_custom_service_username, et_custom_service_userphone;
    private TextView et_custom_service_useraddress;
    private TextView tv_custom_service_agreement, tv_goumaititle;
    private TextView tv_title_right, tv_custom_service_date;
    private static int istype = 2;// 2商品采购，1是服务
    private String istypesubmit = "2";
    public static final int REQUSET = 1;
    public static int year, month, day;
    private String citycodestr;// 城市id
    private String latstr;// 首页的维度
    private String lonstr;// 首页的经度
    private String categorytype_name = null; // 需求名称
    private String categorytype_id = null;// 需求类别id
    private String showtext;// toast显示文字
    private ImageView iv_csiv, iv_custom_service_date_icon;
    // 获取一个日历对象
    Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
    private RelativeLayout re_selectadd;
    private Button mBtnSubmit;
    private TextView tv_length;


    // 需求截至日期
    private String service_datastr;
    // 需求服务类别
    private String service_typestr;
    // 预约服务事项标题
    private String service_thingstr;
    // 补充预约服务事项
    private String service_thing_addstr;
    // 联系人姓名
    private String Namestr;
    // 请输入联系人方式
    private String phonestr;
    // 联系人地址
    private String addressstr;
    //选择地址
    private String selectadd;
    //详细地址
    private String contentadd;
    // 当点击DatePickerDialog控件的设置按钮时，调用该方法
    DatePickerDialog.OnDateSetListener selectdate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // 修改日历控件的年，月，日
            // 这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            // 将页面TextView的显示更新为最新时间
            upDateDate(year, monthOfYear, dayOfMonth);
        }
    };
    private OSS oss;
    private TextView tv_zuihou, tv_isgoutong;
    // uploadObject 这个前面不能加/，且里面的"2016-09-05/beyond001.jpg"是咱们自己定义的可以随便写啥
    // 但是为了同意规定，规则是日期/图片名/加后缀
    private String andoridimgpath = "android_user/custom/";
    private static String uploadObject;// sampleObject
    private static final String downloadObject = "2016-09-05/beyond.jpg";
    List<String> list1 = new ArrayList<String>();
    ArrayList<TImage> images;
    private SimpleGrid mSimpleGrid;
    private List<Bitmap> mImageList;
    private List<String> mFileList;
    private static final int MAX_SEL_PHOTOS = 3;
    private EditText et_custom_service_contentadd;
    TimePickerView pvTime;
    OptionsPickerView pvOptions;
    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<IPickerViewData>>> options3Items = new ArrayList<>();

    //https://github.com/crazyandcoder/citypicker
    CityPicker cityPicker;

    private String CityCodestr = "";
    private UserInfoData userinfo;
    private String istypestr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_service);
        Intent intent = getIntent();
        istypestr = intent.getStringExtra("cstype");
        initView();
        CityCodestr = LocalSave.getValue(CustomServiceActivity.this, AppConfig.basefile, "CityCode", "");

    }

    private void initView() {
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        if ("2".equals(istypestr)) {
            tv_title_content.setText("定制商品");
        } else {
            tv_title_content.setText("服务预约");
        }
        cityPicker = new CityPicker.Builder(this).textSize(20) //滚轮文字的大小

                //以下属性设置为新增方法
                .onlyShowProvinceAndCity(false) //显示省市两级联动boolean 类型，默认false，三级联动
                .confirTextColor("#000000") //设置确认按钮文字颜色
                .cancelTextColor("#000000") //设置取消按钮文字颜色
                .province("北京市") //设置默认省
                .city("北京市") //设置默认城市
                .district("朝阳区") //设置默认地区（县）
                .visibleItemsCount(5)//（滚轮显示的item个数，int 类型，默认为5个）
                //...相关属性同v0.4.0版本
                .cityCyclic(false)//（市的滚轮是否循环滚动）
                .districtCyclic(false)//（区的滚轮是否循环滚动）
                .build();
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                String province = citySelected[0];
                //城市
                String city = citySelected[1];
                //区县（如果设定了两级联动，那么该项返回空）
                String district = citySelected[2];
                //邮编
                String code = citySelected[3];
                if ("北京市".equals(city) | "天津市".equals(city) | "上海市".equals(city) | "重庆市".equals(city)) {
                    city = "";
                }
                et_custom_service_useraddress.setText(province + city + district);
            }
        });
        ImageView iv_title_return = (ImageView) findViewById(R.id.iv_title_return);
        iv_title_return.setOnClickListener(this);
        // iv_title_return.setVisibility(View.GONE);
        tv_zuihou = (TextView) findViewById(R.id.tv_zuihou);
        tv_isgoutong = (TextView) findViewById(R.id.tv_isgoutong);
        re_selectadd = (RelativeLayout) findViewById(R.id.re_selectadd);
        re_selectadd.setOnClickListener(this);
        //时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        pvTime.setTitle("选择截止日期");
        pvTime.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 1);//要在setTime 之前才有效果哦
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                //现在时间
                long startdata = Tool.getUninxTimeL();
                String tttt = getTime(date);
                //用户选择时间
                long returnlongtimet = Tool.getjavatoUninxTimel(tttt);
                Log.v("lsq", "returnlongtimet" + returnlongtimet);
                Log.v("lsq", "startdata" + startdata);
                if (returnlongtimet > startdata) {
                    tv_custom_service_date.setText(getTime(date));
                } else {
                    App.getInstance().showToast("请选择今天以后的截止日期!");
                }

            }
        });


        tv_goumaititle = (TextView) findViewById(R.id.tv_goumaititle);
        iv_csiv = (ImageView) findViewById(R.id.iv_csiv);
        iv_csiv.setOnClickListener(this);
        //选项选择器
        pvOptions = new OptionsPickerView(this);
        //选项1
        options1Items.add(new ProvinceBean(0, "北京市", "北京市", "北京市"));
        //选项2
        ArrayList<String> options2Items_02 = new ArrayList<>();
        options2Items_02.add("北京市");
        options2Items.add(options2Items_02);
        //选项3
        ArrayList<ArrayList<IPickerViewData>> options3Items_02 = new ArrayList<>();

        ArrayList<IPickerViewData> options3Items_02_01 = new ArrayList<>();

        options3Items_02_01.add(new PickerViewData("西城区"));
        options3Items_02_01.add(new PickerViewData("海淀区"));
        options3Items_02_01.add(new PickerViewData("丰台区"));
        options3Items_02_01.add(new PickerViewData("东城区"));
        options3Items_02_01.add(new PickerViewData("朝阳区"));
        options3Items_02.add(options3Items_02_01);
        options3Items.add(options3Items_02);
        //三级联动效果
        pvOptions.setPicker(options1Items, options2Items, options3Items, true);
        //设置选择的三级单位
//        pwOptions.setLabels("省", "市", "区");
        pvOptions.setTitle("选择城市");
        pvOptions.setCyclic(false, false, false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        pvOptions.setSelectOptions(0, 0, 0);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText()
                        + options2Items.get(options1).get(option2)
                        + options3Items.get(options1).get(option2).get(options3).getPickerViewText();
                et_custom_service_useraddress.setText(tx);
            }
        });
        citycodestr = LocalSave.getValue(this, AppConfig.basefile, "CityCode", "");
        latstr = LocalSave.getValue(this, AppConfig.basefile, "lat", "");
        lonstr = LocalSave.getValue(this, AppConfig.basefile, "lon", "");
        tv_custom_service_date = (TextView) findViewById(R.id.tv_custom_service_date);
        tv_custom_service_date.setOnClickListener(this);
        iv_custom_service_date_icon = (ImageView) findViewById(R.id.iv_custom_service_date_icon);
        iv_custom_service_date_icon.setOnClickListener(this);
        bt_customservice_submit = (Button) findViewById(R.id.bt_customservice_submit);
        bt_customservice_submit.setOnClickListener(this);
        tv_title_right = (TextView) findViewById(R.id.tv_title_right);
        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setOnClickListener(this);
        tv_title_right.setText("我的定制");
        tv_length = (TextView) findViewById(R.id.tv_length);
        et_custom_service_type = (TextView) findViewById(R.id.et_custom_service_type);
        et_custom_service_type.setOnClickListener(this);
        et_custom_service_thing = (EditText) findViewById(R.id.et_custom_service_thing);
        et_custom_service_thing_add = (EditText) findViewById(R.id.et_custom_service_thing_add);
        et_custom_service_username = (EditText) findViewById(R.id.et_custom_service_username);
        et_custom_service_userphone = (EditText) findViewById(R.id.et_custom_service_userphone);
        et_custom_service_useraddress = (TextView) findViewById(R.id.et_custom_service_useraddress);
        et_custom_service_useraddress.setOnClickListener(this);
        et_custom_service_userphone.setHint(Tool.edithint(getString(R.string.custom_service_phone), DensityUtil.px2dip(App.getInstance(), (float) getResources().getDimensionPixelSize(R.dimen.base_dimen_24))));
        et_custom_service_useraddress.setHint(Tool.edithint("选择地址", DensityUtil.px2dip(App.getInstance(), (float) getResources().getDimensionPixelSize(R.dimen.base_dimen_24))));


        et_custom_service_contentadd = (EditText) findViewById(R.id.et_custom_service_contentadd);
        et_custom_service_contentadd.setHint(Tool.edithint("请填写详细地址", DensityUtil.px2dip(App.getInstance(), (float) getResources().getDimensionPixelSize(R.dimen.base_dimen_24))));
        et_custom_service_username.setHint(Tool.edithint(getString(R.string.custom_service_name), DensityUtil.px2dip(App.getInstance(), (float) getResources().getDimensionPixelSize(R.dimen.base_dimen_24))));
        et_custom_service_type.setHint("请选择商品所属品类");
        iv_custom_service_date_icon.setClickable(true);
        iv_custom_service_date_icon.setFocusable(true);

        et_custom_service_thing_add.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = editable.length();
                tv_length.setText(length + "/200");
            }
        });


        typetext = (TextView) findViewById(R.id.tv_typetext);
        tv_goumaititle.setText("购买商品");
        typetext.setText("所需商品");
        et_custom_service_thing.setHint(Tool.edithint("例：我想定制一条加肥牛仔裤       限制20字以内", DensityUtil.px2dip(App.getInstance(), (float) getResources().getDimensionPixelSize(R.dimen.base_dimen_24))));
        et_custom_service_thing_add.setHint(Tool.edithint("请补充商品的规格要求 ...", DensityUtil.px2dip(App.getInstance(), (float) getResources().getDimensionPixelSize(R.dimen.base_dimen_24))));


        /**
         * 获取时间
         */
        // Calendar c = Calendar.getInstance();
        // year = c.get(Calendar.YEAR);
        // month = c.get(Calendar.MONTH);
        // day = c.get(Calendar.DAY_OF_MONTH);
        // upDateDate(year, month, day);
        /**
         * 获取照片拍照
         */
        mImageList = new ArrayList<Bitmap>();
        mFileList = new ArrayList<String>();
        mSimpleGrid = (SimpleGrid) findViewById(R.id.simpleGrid);

        mSimpleGrid.setMaxItemPerRow(3);
        mSimpleGrid.setItemMarginHor((float) getResources().getDimensionPixelSize(R.dimen.base_dimen_12));
        mSimpleGrid.setItemMarginVer((float) getResources().getDimensionPixelSize(R.dimen.base_dimen_12));
        mSimpleGrid.setCallback(this);

        // 要放在setCallBack(this)后面

        updateImgGrid();
        initData(istypestr);
    }

    private void initData(String str) {
        if ("1".equals(str)) {
            showempty();
            tv_zuihou.setText("签约服务");
            tv_isgoutong.setText("沟通预约");
            istype = 1;
            typetext.setText("需求类别");
            tv_goumaititle.setText("预约事项");
            et_custom_service_type.setHint("请选择需求所属类别");
            et_custom_service_thing.setText("");
            et_custom_service_thing_add.setText("");
            et_custom_service_thing.setHint(Tool.edithint("例：我想请一名月嫂       限制20字以内", DensityUtil.px2dip(App.getInstance(), (float) getResources().getDimensionPixelSize(R.dimen.base_dimen_24))));
            et_custom_service_thing_add.setHint(Tool.edithint("请补充服务需求 ...", DensityUtil.px2dip(App.getInstance(), (float) getResources().getDimensionPixelSize(R.dimen.base_dimen_24))));

        } else {
            showempty();
            istype = 2;
            tv_isgoutong.setText("沟通预定");
            tv_zuihou.setText("到店购买");
            typetext.setText("所需商品");
            tv_goumaititle.setText("购买商品");
            et_custom_service_type.setHint("请选择商品所属品类");
            et_custom_service_thing.setText("");
            et_custom_service_thing_add.setText("");
            et_custom_service_thing.setHint(Tool.edithint("例：我想定制一条加肥牛仔裤       限制20字以内", DensityUtil.px2dip(App.getInstance(), (float) getResources().getDimensionPixelSize(R.dimen.base_dimen_24))));
            et_custom_service_thing_add.setHint(Tool.edithint("请补充商品的规格要求 ...", DensityUtil.px2dip(App.getInstance(), (float) getResources().getDimensionPixelSize(R.dimen.base_dimen_24))));

        }

    }

    @Override
    public View onCreateView(ViewGroup viewGroup, int position) {
        // 如果未满,第一个位置显示照相机
        Log.v("asdf", "+position+" + position);
        Log.v("asdf", "+mImageList.size()+" + mImageList.size());
        if (position == 0 && mImageList.size() < MAX_SEL_PHOTOS) {
            final View view = LayoutInflater.from(this).inflate(R.layout.item_photo_preview_with_upload,
                    viewGroup, false);
            final ImageView ivAdd = (ImageView) view.findViewById(R.id.iv_add);
            ivAdd.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO点击拍照
                    Log.v("lsq", "todo=====");
                    KeyboardUtils.hideSoftInput(CustomServiceActivity.this);
                    showPopwindow();

                }
            });
            return view;
        }

        // 获取实际的数据索引，未满的时候需要减去1，因为第一个是照相机
        final int pos = (mImageList.size() < MAX_SEL_PHOTOS) ? (position - 1) : (position);
        final View itemV = LayoutInflater.from(this).inflate(R.layout.item_photo_preview_with_delete,
                viewGroup, false);
        final ImageView img = (ImageView) itemV.findViewById(R.id.img);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        final Bitmap bitmap = mImageList.get(pos);
        final String fileName = mFileList.get(pos);
        img.setImageBitmap(bitmap);
        itemV.findViewById(R.id.del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareDelete(bitmap, fileName);
            }
        });
        return itemV;
    }

    private void get_ModiUserinfo() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                get_UserInfo();
            }
        }).start();
    }

    private void get_UserInfo() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
//			*            0 false 1true 是否查询用户详细信息
//					* @param hide
//					*            0 false 1true 是否隐藏用户手机号和邮箱号
            userinfo = api.get_user("1", "0");
            if (userinfo != null) {
                handler.sendEmptyMessage(5);
            }
        } catch (JSONException e) {
            android.util.Log.v("ceshi", "JSONException");
            e.printStackTrace();
        } catch (WSError e) {
            android.util.Log.v("ceshi", "WSError");
            e.printStackTrace();
        }
    }

    private void updateImgGrid() {
        final int curImgCount = mImageList.size();
        if (curImgCount < MAX_SEL_PHOTOS) {
            mSimpleGrid.createViews(curImgCount + 1); // 未满的时候，要加上1， 照相机的位置
        } else {
            mSimpleGrid.createViews(curImgCount);
        }
    }

    private void prepareDelete(Bitmap bitmap, String fileName) {
        if (bitmap != null) {
            mImageList.remove(bitmap);
            mFileList.remove(fileName);
            updateImgGrid();
            bitmap.recycle();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (pvOptions != null) {
            if (pvOptions.isShowing() || pvTime.isShowing()) {
                pvOptions.dismiss();
                pvTime.dismiss();
            }
            if (pvTime.isShowing()) {
                pvTime.dismiss();
            }
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.re_selectadd:
                //  pvOptions.show();
                cityPicker.show();

                break;
            case R.id.et_custom_service_useraddress://地区选择
//                pvOptions.show();
                cityPicker.show();

                break;
            case R.id.iv_custom_service_date_icon:// 日历选择
                pvTime.show();
                break;
            case R.id.tv_custom_service_date:// 日历选择
                pvTime.show();
                break;
            case R.id.tv_title_right:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                Intent intentnew = new Intent();
                if ("true".equals(AppConfig.isusertype)) {
                    intentnew.setClass(CustomServiceActivity.this, MyCustiomGSListActivity.class);
                    startActivity(intentnew);
                } else {
                    intentnew.putExtra("tag", "custom");
                    intentnew.setClass(CustomServiceActivity.this, Loginaty.class);
                    startActivity(intentnew);
                }

                break;
            case R.id.bt_customservice_submit:
                if (getNetWifi()) {
                    if (istype == 2) {// 商品采购
                        istypesubmit = "2";
                        makeSure();
                    } else {// 服务
                        istypesubmit = "1";
                        makeSure();
                    }
                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }
                break;
            case R.id.et_custom_service_type:
                Log.v("ceshi", "istype+" + istype);
                Intent intentaty = new Intent();
                if (istype == 1) {// 商品采购
                    intentaty.setClass(this, CategoryChooseActivity.class);
                    intentaty.putExtra("istype", "2");
                } else {// 服务
                    intentaty.setClass(this, CategoryChooseActivity.class);
                    intentaty.putExtra("istype", "1");
                }
                // 发送意图标示为REQUSET=1
                startActivityForResult(intentaty, REQUSET);
                break;
            case R.id.iv_csiv:
                Log.v("ceshi", "istype+" + istype);
                Intent intentatya = new Intent();
                if (istype == 1) {// 商品采购
                    intentatya.setClass(this, CategoryChooseActivity.class);
                    intentatya.putExtra("istype", "2");
                } else {// 服务
                    intentatya.setClass(this, CategoryChooseActivity.class);
                    intentatya.putExtra("istype", "1");
                }
                // 发送意图标示为REQUSET=1
                startActivityForResult(intentatya, REQUSET);
                break;
            case R.id.iv_title_return:
                KeyboardUtils.hideSoftInput(CustomServiceActivity.this);
                finish();
                break;
            default:
                break;
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    bt_customservice_submit.setEnabled(true);

                    Intent intent2 = new Intent(CustomServiceActivity.this, MyCustiomGSListActivity.class);
                    startActivity(intent2);
                    StyledDialog.dismissLoading();

                    App.getInstance().showToast("提交成功!");
                    showempty();
                    break;
                case 2:
                    bt_customservice_submit.setEnabled(true);
                    App.getInstance().showToast("提交失败!");
                    showempty();
                    StyledDialog.dismissLoading();

                    break;
                case 3:
                    submitCustom();
                    break;
                case 4:
                    App.getInstance().showToast("图片上传失败!");
                    StyledDialog.dismissLoading();

                    break;
                case 5:
                    Log.v("ceshi", "userinfo" + userinfo);
                    if (TextUtils.isEmpty(et_custom_service_username.getText().toString())) {
                        et_custom_service_username.setText(userinfo.getReal_name());
                    }
                    if (TextUtils.isEmpty(et_custom_service_userphone.getText().toString())) {
                        et_custom_service_userphone.setText(userinfo.getMobile());
                    }
                    if (TextUtils.isEmpty(et_custom_service_contentadd.getText().toString())) {
                        et_custom_service_contentadd.setText(userinfo.getAddress());
                    }

                    // et_custom_service_useraddress.setText(userinfo.getAddress());

                    StyledDialog.dismissLoading();

                    break;
                case 6:
                    App.getInstance().showToast("积分不足!");
                    StyledDialog.dismissLoading();
                    bt_customservice_submit.setEnabled(true);

                    break;
                case 7:
                    App.getInstance().showToast("提交失败!");
                    StyledDialog.dismissLoading();
                    bt_customservice_submit.setEnabled(true);

                    break;
            }
        }


    };

    private void showempty() {
        tv_custom_service_date.setText("请选择回复截至日期");// 时间
        if (istype == 2) {
            et_custom_service_type.setText("请选择商品所属品类");
        } else if (istype == 1) {
            et_custom_service_type.setText("请选择需求所属类别");
        }

        et_custom_service_thing.setText("");// 服务事项
        et_custom_service_thing_add.setText("");/// 更多的服务事项
        et_custom_service_username.setText("");// 联系人姓名
        et_custom_service_userphone.setText("");// 联系人手机
        et_custom_service_useraddress.setText("");// 联系人地址
        if (mImageList != null) {
            for (int i = 0; i < mImageList.size(); i++) {
                if (i >= 0) {
                    mSimpleGrid.removeView(i + 1);
                    mImageList.clear();
                    mFileList.clear();
                    updateImgGrid();
                }

            }
        }

    }

    private void submitCustom() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                submitCustomapi();
            }
        }).start();
    }

    private void submitCustomapi() {
        String timel = Tool.getjavatoUninxTime(service_datastr);
        int size = list1.size();
        String[] array = new String[size];
        StringBuffer sbstr = new StringBuffer();
        for (int j = 0; j < list1.size(); j++) {
            array[j] = (String) list1.get(j);
            if (j == 0) {
                sbstr.append(array[j]);
            } else {
                sbstr.append("," + array[j]);
            }
        }
        try {
            DigoIUserApiImpl api = new DigoIUserApiImpl();
            boolean issubmit = api.create_myservice(categorytype_id, service_thingstr, service_thing_addstr, lonstr,
                    latstr, timel + "", sbstr.toString(), Namestr, addressstr, phonestr, istypesubmit, CityCodestr);
            if (issubmit == true) {
                handler.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(2);
            }
        } catch (JSONException e) {
            handler.sendEmptyMessage(7);
            e.printStackTrace();
        } catch (WSError e) {
            if ("-1513".equals(e.getMessage())) {
                handler.sendEmptyMessage(6);
            } else {
                handler.sendEmptyMessage(7);
            }
            e.printStackTrace();
        }
    }

    // 提交
    private void makeSure() {
        //用户登录状态，登录成功未true ,默认未false
        AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
        if (!"true".equals(AppConfig.isusertype)) {
            Intent intent = new Intent();
            intent.putExtra("tag", "custom");
            intent.setClass(CustomServiceActivity.this, Loginaty.class);
            startActivity(intent);
            return;
        }
        // 需求截至日期
        service_datastr = tv_custom_service_date.getText().toString();// 时间
        // 需求服务类别
        service_typestr = et_custom_service_type.getText().toString();// 需求类别
        service_thingstr = et_custom_service_thing.getText().toString();// 服务事项
        service_thing_addstr = et_custom_service_thing_add.getText().toString();/// 更多的服务事项
        Namestr = et_custom_service_username.getText().toString();// 联系人姓名
        phonestr = et_custom_service_userphone.getText().toString();// 联系人手机

        selectadd = et_custom_service_useraddress.getText().toString();
        contentadd = et_custom_service_contentadd.getText().toString();

        addressstr = selectadd + contentadd;
        // 需求服务类别
        Log.v("lsq", "service_datastr+" + service_datastr);
        if (!TextUtils.isEmpty(phonestr)) {
            if (phonestr.length() != 11) {
                Toast.makeText(this, "请输入正确联系人方式!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (TextUtils.isEmpty(service_typestr) | "请选择需求所属类别".equals(service_typestr) | "请选择商品所属品类".equals(service_typestr)) {
            Toast.makeText(this, "请选择需求类型!", Toast.LENGTH_SHORT).show();
            return;
        } else if (service_datastr.equals("") | "请选择回复截至日期".equals(service_datastr)) {
            Toast.makeText(this, "请选择回复截至日期!", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(service_thingstr)) {
            Toast.makeText(this, "请输入需要预约的服务事项!", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(service_thing_addstr)) {
            Toast.makeText(this, "请输入补充预约服务事项!", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(Namestr)) {
            Toast.makeText(this, "请输入联系人姓名!", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(phonestr)) {
            Toast.makeText(this, "请输入联系人方式!", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(selectadd)) {
            Toast.makeText(this, "请选择地址!", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(contentadd)) {
            Toast.makeText(this, "请输入详细地址!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (mFileList != null) {
                if (mFileList.size() > 0) {
                    bt_customservice_submit.setEnabled(false);
                    submintCustomApi(categorytype_id, istypesubmit);
                } else {
                    bt_customservice_submit.setEnabled(false);
                    submitCustom();
                }
            } else {
                bt_customservice_submit.setEnabled(false);
                submitCustom();
            }

        }
    }

    private void showOOS() {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();
        oss = new OSSClient(this.getApplicationContext(), endpoint, credentialProvider, conf);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mFileList.size(); i++) {
                    String str1 = mFileList.get(i);
                    uploadObject = andoridimgpath + Tool.getUninxToJavaDaya() + "/" + Tool.getUninxTimea() + i + ".jpg";
                    PutObjectRequest put = new PutObjectRequest(testBucket, uploadObject, str1);
                    list1.add(pj + uploadObject);
                    // 异步上传时可以设置进度回调
                    put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
                        @Override
                        public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                            Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
                        }
                    });
                    CreateBucketRequest createBucketRequest = new CreateBucketRequest(testBucket);
                    createBucketRequest.setBucketACL(CannedAccessControlList.PublicRead);
                    // 指定Bucket的ACL权限
                    createBucketRequest.setLocationConstraint("oss-cn-hangzhou");
                    // 指定Bucket所在的数据中心
                    OSSAsyncTask task = oss.asyncPutObject(put,
                            new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                                @Override
                                public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                                    int requ = result.getStatusCode();
                                    if (requ == 200) {

                                        Log.i("lsq", "url1+" + pj + uploadObject);

                                    } else {
                                        handler.sendEmptyMessage(4);
                                    }
                                }

                                @Override
                                public void onFailure(PutObjectRequest request, ClientException clientExcepion,
                                                      ServiceException serviceException) {
                                    // 请求异常
                                    if (clientExcepion != null) {
                                        // 本地异常如网络异常等
                                        clientExcepion.printStackTrace();
                                    }
                                    if (serviceException != null) {
                                        // 服务异常
                                        App.getInstance().showToast("图片上传失败，请重新上传");
                                    }
                                }
                            });
                }
                handler.sendEmptyMessage(3);
            }
        }).start();

    }

    private void submintCustomApi(final String type_id, final String ypesubmit) {
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                showOOS();
            }
        }).start();

    }


    TakePhoto takePhoto;
    Uri imageUri;

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        showImg(result.getImages());
    }

    private void showImg(ArrayList<TImage> images) {
        this.images = images;
        for (int i = 0; i < images.size(); i++) {
            // Bitmap bitmap = BitmapFactory.decodeFile(images.get(i).getPath());
            Bitmap bitmap = ModifyBitmap(images.get(i).getPath());
            showImageView(bitmap, images.get(i).getPath());
            //  Log.v("ceshi","path+"+images.get(i).getPath());
        }

    }

    private Bitmap ModifyBitmap(String absolutePath) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        // 这个isjustdecodebounds很重要
        opt.inJustDecodeBounds = true;
        Bitmap bm = BitmapFactory.decodeFile(absolutePath, opt);

        // 获取到这个图片的原始宽度和高度
        int picWidth = opt.outWidth;
        int picHeight = opt.outHeight;

        // 获取屏的宽度和高度
        WindowManager windowManager = this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();

        // isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
        opt.inSampleSize = 2;
        // 根据屏的大小和图片大小计算出缩放比例
        if (picWidth > picHeight) {
            if (picWidth > screenWidth)
                opt.inSampleSize = picWidth / screenWidth;
        } else {
            if (picHeight > screenHeight)

                opt.inSampleSize = picHeight / screenHeight;
        }

        // 这次再真正地生成一个有像素的，经过缩放了的bitmap
        opt.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(absolutePath, opt);
        return bm;
    }

    public void showImageView(Bitmap bitmap, String fileName) {
        if (mImageList.size() < MAX_SEL_PHOTOS) {
            mImageList.add(bitmap);
            mFileList.add(fileName);
            updateImgGrid();
        }
    }

    private void showPopwindow() {
        View parent = ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        View popView = View.inflate(this, R.layout.camera_pop_menu, null);

        Button btnCamera = (Button) popView.findViewById(R.id.btn_camera_pop_camera);
        Button btnAlbum = (Button) popView.findViewById(R.id.btn_camera_pop_album);
        Button btnCancel = (Button) popView.findViewById(R.id.btn_camera_pop_cancel);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        final PopupWindow popWindow = new PopupWindow(popView, width, height);
        popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(false);// 设置允许在外点击消失
        // 图片存贮路径：
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        imageUri = Uri.fromFile(file);
        takePhoto = getTakePhoto();
        View.OnClickListener listener = new View.OnClickListener() {

            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_camera_pop_camera:
                        if (takePhoto != null) {
                            CompressConfig config = new CompressConfig.Builder()
                                    .setMaxSize(51200)
                                    .setMaxPixel(500)
                                    .create();
                            takePhoto.onEnableCompress(config, true);
                        }
                        if (mImageList != null) {
                            if (mImageList.size() == 3) {
                                App.getInstance().showToast("最多选择三张照片!");
                            } else {
                                //打开摄像头
                                takePhoto.onPickFromCapture(imageUri);
                            }
                        } else {
                            //打开摄像头
                            takePhoto.onPickFromCapture(imageUri);
                        }

                        break;
                    case R.id.btn_camera_pop_album:
//                        CompressConfig config = new CompressConfig.Builder()
//                                .setMaxSize(102400)
//                                .setMaxPixel(600 >= 600 ? 600 : 600)
//                                .create();
//                        takePhoto.onEnableCompress(config, true);
                        CompressConfig config = new CompressConfig.Builder()
                                .setMaxSize(51200)
                                .setMaxPixel(500)
                                .create();
                        takePhoto.onEnableCompress(config, true);
                        //  是否使用自带相册
                        takePhoto.setTakePhotoOptions(new TakePhotoOptions.Builder().setWithOwnGallery(true).create());

//                //从相册
//                takePhoto.onPickFromGallery();
                        if (mImageList != null) {
                            // 选择最多选择几张
                            takePhoto.onPickMultiple(3 - mImageList.size());
                        } else {

                            // 选择最多选择几张
                            takePhoto.onPickMultiple(3);
                        }

                        break;
                    case R.id.btn_camera_pop_cancel:

                        break;
                }
                popWindow.dismiss();
            }
        };

        btnCamera.setOnClickListener(listener);
        btnAlbum.setOnClickListener(listener);
        btnCancel.setOnClickListener(listener);

        ColorDrawable dw = new ColorDrawable(0x30000000);
        popWindow.setBackgroundDrawable(dw);
        popWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        String isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
        if ("true".equals(isusertype)) {
            if (getNetWifi()) {
                StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                get_ModiUserinfo();
            }
        }

//        else {
//            App.getInstance().showToast("网络不给力，请检查网络设置");
//        }
    }

    @Override
    public void onRemoveView(int position, View v) {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUSET & data != null) {
            categorytype_name = data.getStringExtra(CategoryChooseActivity.KEY_TYPE_NAME);
            categorytype_id = data.getStringExtra(CategoryChooseActivity.KEY_TYPE_ID);
            StyledDialog.dismissLoading();

            et_custom_service_type.setText(data.getStringExtra(CategoryChooseActivity.KEY_TYPE_NAME));
        }
    }

    private void upDateDate(int year, int monthOfYear, int dayOfMonth) {
        //现在时间
        long startdata = Tool.getUninxTimeL();
        String tttt = year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日";
        //用户选择时间
        long returnlongtimet = Tool.getjavatoUninxTimel(tttt);
        Log.v("lsq", "returnlongtimet" + returnlongtimet);
        Log.v("lsq", "startdata" + startdata);
        if (returnlongtimet > startdata) {
            tv_custom_service_date.setText(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
        } else {
            App.getInstance().showToast("请选择今天以后的截止日期!");
        }
    }

    private static SimpleDateFormat sf = null;

    /* 将字符串转为时间戳 */
    public long getStringToDatea(String time) {
        sf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = sf.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    //获取当前设备年月日
    public static String getCurrentDate() {
        Date d = new Date();
        sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(d);
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(date);
    }
}
