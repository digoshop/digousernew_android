package com.digoshop.app.module.userCenter.module;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.module.customServices.view.IMainPresenter;
import com.digoshop.app.module.customServices.view.IMainView;
import com.digoshop.app.module.customServices.view.MainPresenter;
import com.digoshop.app.module.customServices.view.SimpleGrid;
import com.digoshop.app.module.shopdetailnew.ShopDetailNewSNActivity;
import com.digoshop.app.utils.DensityUtil;
import com.digoshop.app.utils.Displayer;
import com.digoshop.app.utils.KeyboardUtils;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.digoshop.app.utils.AppConfig.accessKeyId;
import static com.digoshop.app.utils.AppConfig.accessKeySecret;
import static com.digoshop.app.utils.AppConfig.endpoint;
import static com.digoshop.app.utils.AppConfig.pj;
import static com.digoshop.app.utils.AppConfig.testBucket;
import static com.digoshop.app.utils.Tool.getNetWifi;

/**
 * 提交评论页面
 */
public class MyCommentActivity extends TakePhotoActivity
        implements OnClickListener, SimpleGrid.Callback, IMainView {

    private RelativeLayout re_mycomment_shopdetail, re_mycommtent_submit;
    private EditText et_mycomment;
    private List<Bitmap> mImageList;
    private List<String> mFileList;
    private SimpleGrid mSimpleGrid;
    private Button mBtnSubmit;
    private LinearLayout ll_Praise, In_comments, ll_negative;
    private TextView tv_negative, tv_comments, tv_Praise;
    private String negative, comments, praise, sid, eid, typeid;

    private IMainPresenter mMainPresenter;
    private static final int MAX_SEL_PHOTOS = 3;
    private OSS oss;
    // uploadObject 这个前面不能加/，且里面的"2016-09-05/beyond001.jpg"是咱们自己定义的可以随便写啥
    // 但是为了同意规定，规则是日期/图片名/加后缀
    private String andoridimgpath = "android_user/comment/";
    private static String uploadObject;// sampleObject
    private static final String downloadObject = "2016-09-05/beyond.jpg";
    List<String> list1 = new ArrayList<String>();
    private TextView tv_commentshopname;
    private ImageView iv_reply_a, iv_reply_b, iv_reply_c;
   private String logo;
    private ImageView iv_mycomment_shopicon;
    private DisplayImageOptions options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去掉窗口标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mycomment);
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("添加评论");
        et_mycomment = (EditText) findViewById(R.id.et_mycomment);
        et_mycomment.setHint(Tool.edithint(getString(R.string.mycommtent_str), DensityUtil.px2dip(App.getInstance(), (float) getResources().getDimensionPixelSize(R.dimen.base_dimen_25))));
        re_mycomment_shopdetail = (RelativeLayout) findViewById(R.id.re_mycomment_shopdetail);
        re_mycomment_shopdetail.setOnClickListener(this);
        iv_reply_a = (ImageView) findViewById(R.id.iv_reply_a);
        iv_reply_b = (ImageView) findViewById(R.id.iv_reply_b);
        iv_reply_c = (ImageView) findViewById(R.id.iv_reply_c);
        re_mycommtent_submit = (RelativeLayout) findViewById(R.id.re_mycommtent_submit);
        re_mycommtent_submit.setOnClickListener(this);

        ll_Praise = (LinearLayout) findViewById(R.id.ll_Praise);
        In_comments = (LinearLayout) findViewById(R.id.In_comments);
        ll_negative = (LinearLayout) findViewById(R.id.ll_negative);
        tv_negative = (TextView) findViewById(R.id.tv_negative);
        tv_comments = (TextView) findViewById(R.id.tv_comments);
        tv_Praise = (TextView) findViewById(R.id.tv_Praise);
        iv_mycomment_shopicon = (ImageView) findViewById(R.id.iv_mycomment_shopicon);
        tv_commentshopname = (TextView) findViewById(R.id.tv_commentshopname);
        ll_Praise.setOnClickListener(this);
        In_comments.setOnClickListener(this);
        ll_negative.setOnClickListener(this);

        mImageList = new ArrayList<Bitmap>();
        mFileList = new ArrayList<String>();
        mMainPresenter = new MainPresenter(this, this);
        Intent intent = getIntent();
        sid = intent.getStringExtra("sid");
        eid = intent.getStringExtra("eid");
        logo= intent.getStringExtra("logo");
        tv_commentshopname.setText(intent.getStringExtra("name"));
        Log.i("zzrdata", "lgo"+logo);
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.defaultyuan)
                .showImageForEmptyUri(R.drawable.defaultyuan)
                .showImageOnFail(R.drawable.defaultyuan)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)   //设置图片的解码类型
                .displayer(new Displayer(0))
                .build();
        intView();
        handler.sendEmptyMessage(5);

    }

    public void OnTitleReturnClick(View view) {
        KeyboardUtils.hideSoftInput( MyCommentActivity.this);
        finish();
    }

    private void intView() {
        mSimpleGrid = (SimpleGrid) findViewById(R.id.simpleGrid);

        mSimpleGrid.setMaxItemPerRow(3);
        mSimpleGrid.setItemMarginHor(6f);
        mSimpleGrid.setItemMarginVer(6f);

        mSimpleGrid.setCallback(this);

        // 要放在setCallBack(this)后面
        updateImgGrid();
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
    public void showImageView(Bitmap bitmap, String fileName) {
        if (mImageList.size() < MAX_SEL_PHOTOS) {
            mImageList.add(bitmap);
            mFileList.add(fileName);
            updateImgGrid();
        }
    }

    @Override
    public View onCreateView(ViewGroup viewGroup, int position) {
        // 如果未满,第一个位置显示照相机
        if (position == 0 && mImageList.size() < MAX_SEL_PHOTOS) {
            final View view = LayoutInflater.from(this).inflate(R.layout.item_photo_preview_with_upload, viewGroup,
                    false);
            final ImageView ivAdd = (ImageView) view.findViewById(R.id.iv_add);
            ivAdd.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    KeyboardUtils.hideSoftInput( MyCommentActivity.this);
                    // TODO点击拍照
                    showPopwindow();
                }
            });
            return view;
        }

        // 获取实际的数据索引，未满的时候需要减去1，因为第一个是照相机
        final int pos = (mImageList.size() < MAX_SEL_PHOTOS) ? (position - 1) : (position);
        final View itemV = LayoutInflater.from(this).inflate(R.layout.item_photo_preview_with_delete, viewGroup, false);
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
        itemV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO跳到大图浏览
                // ImagePagerActivity.startImagePagerActivity(RefundActivity.this,
                // pos, mImageList);
            }
        });
        return itemV;
    }

    @Override
    public void onRemoveView(int position, View v) {
        // TODO Auto-generated method stub

    }

    TakePhoto takePhoto;
    Uri imageUri;
    ArrayList<TImage> images;

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        showImg(result.getImages());
    }

    private void showImg(ArrayList<TImage> images) {
        this.images = images;
        for (int i = 0; i < images.size(); i++) {
            //Bitmap bitmap = BitmapFactory.decodeFile(images.get(i).getPath());
            Bitmap bitmap = ModifyBitmap(images.get(i).getPath());
            showImageView(bitmap, images.get(i).getPath());
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
        WindowManager windowManager = getWindowManager();
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

    private void showPopwindow() {
        View parent = ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        View popView = View.inflate(MyCommentActivity.this, R.layout.camera_pop_menu, null);

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
//						CompressConfig config = new CompressConfig.Builder()
//								.setMaxSize(102400)
//								.setMaxPixel(600 >= 600 ? 600 : 600)
//								.create();
//						takePhoto.onEnableCompress(config, true);
                        if (takePhoto != null) {
                            CompressConfig config = new CompressConfig.Builder()
                                    .setMaxSize(51200)
                                    .setMaxPixel(500)
                                    .create();
                            takePhoto.onEnableCompress(config, true);
                        }
                        //  是否使用自带相册
                        takePhoto.setTakePhotoOptions(new TakePhotoOptions.Builder().setWithOwnGallery(true).create());

//                //从相册
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
    public void onClick(View v) {
        mMainPresenter.uploadImage(mFileList);
        Intent intent = new Intent();
        switch (v.getId()) {

            case R.id.re_mycomment_shopdetail:// 进入店铺详情
                intent.putExtra("sid", sid);
                intent.setClass(MyCommentActivity.this, ShopDetailNewSNActivity.class);
                startActivity(intent);
                break;
            case R.id.re_mycommtent_submit:// 提交评论
                if (getNetWifi()) {
                    KeyboardUtils.hideSoftInput( MyCommentActivity.this);
                    showComment();
                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }
                break;
            // 好评
            case R.id.ll_Praise:
                typeid = "0";
                inittypeimg(typeid);
			tv_Praise.setTextColor(MyCommentActivity.this.getResources().getColor(R.color.yellow_a));
			tv_comments.setTextColor(MyCommentActivity.this.getResources().getColor(R.color.home_text_item_type));
			tv_negative.setTextColor(MyCommentActivity.this.getResources().getColor(R.color.home_text_item_type));

                break;

            // 中评
            case R.id.In_comments:
                typeid = "1";
                inittypeimg(typeid);
                tv_negative.setTextColor(MyCommentActivity.this.getResources().getColor(R.color.home_text_item_type));
                	tv_comments.setTextColor(MyCommentActivity.this.getResources().getColor(R.color.yellow_a));
                	tv_Praise.setTextColor(MyCommentActivity.this.getResources().getColor(R.color.home_text_item_type));
                break;
            // 差评
            case R.id.ll_negative:
                typeid = "2";
                inittypeimg(typeid);
                tv_Praise.setTextColor(MyCommentActivity.this.getResources().getColor(R.color.home_text_item_type));
                tv_comments.setTextColor(MyCommentActivity.this.getResources().getColor(R.color.home_text_item_type));
                tv_negative.setTextColor(MyCommentActivity.this.getResources().getColor(R.color.yellow_a));
                break;
            default:
                break;
        }
    }

    private void inittypeimg(String str) {
        int inttype = Integer.valueOf(str).intValue();
        switch (inttype) {
            case 0:
                iv_reply_a.setBackgroundResource(R.drawable.dppj_hao);
                iv_reply_b.setBackgroundResource(R.drawable.dppj_zhong01);
                iv_reply_c.setBackgroundResource(R.drawable.dppj_cha01);
                break;
            case 1:
                iv_reply_a.setBackgroundResource(R.drawable.dppj_hao01);
                iv_reply_b.setBackgroundResource(R.drawable.dppj_zhong);
                iv_reply_c.setBackgroundResource(R.drawable.dppj_cha01);
                break;

            case 2:
                iv_reply_a.setBackgroundResource(R.drawable.dppj_hao01);
                iv_reply_b.setBackgroundResource(R.drawable.dppj_zhong01);
                iv_reply_c.setBackgroundResource(R.drawable.dppj_cha);
                break;

            default:
                break;
        }
    }

    Handler handler = new Handler() {
        Intent intent = new Intent();

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    StyledDialog.dismissLoading();
                    App.getInstance().showToast("提交成功!");
//                    intent.setClass(MyCommentActivity.this, MyReplyActivity.class);
//                    startActivity(intent);
                    finish();
                    break;
                case 2:
                    App.getInstance().showToast("评论失败!");
                    StyledDialog.dismissLoading();

                    break;
                case 3:
//					App.getInstance().showToast("测试哦");
//					StyledDialog.dismissLoading();
                    submitCS();
                    break;
                case 4:
                    App.getInstance().showToast("图片上传失败!");
                    StyledDialog.dismissLoading();
                    break;
                case 1302:
                    StyledDialog.dismissLoading();

                    Toast.makeText(MyCommentActivity.this, "已评论", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    ImageLoader.getInstance().displayImage(logo,iv_mycomment_shopicon,
                            options);

                    break;
            }
        }
    };

    private void showComment() {
        String mycomment = et_mycomment.getText().toString();
        // 需求服务类别
        if (TextUtils.isEmpty(typeid)) {
            Toast.makeText(MyCommentActivity.this, "请选择评论！", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(mycomment)) {
            Toast.makeText(MyCommentActivity.this, "请输入评论内容！", Toast.LENGTH_LONG).show();
        } else if (mFileList.size() < 0) {
            Toast.makeText(MyCommentActivity.this, "请添加图片", Toast.LENGTH_LONG).show();
        } else {
            StyledDialog.buildLoading(MyCommentActivity.this, App.getInstance().getString(R.string.onloading), true, false).show();
            if (mFileList != null) {
                if (mFileList.size() > 0) {
                    submitComment();
                } else {
                    submitCS();
                }
            } else {
                submitCS();
            }
        }
    }

    private void submitComment() {
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                showOOS();
            }
        }).start();
    }

    private void submitCS() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                submitCSapi();
            }
        }).start();
    }

    private void submitCSapi() {
        String mycomment = et_mycomment.getText().toString();
        int size = list1.size();
        Log.v("lsq", "list1+" + list1.size());
        String[] array = new String[size];
        StringBuffer sbstr = new StringBuffer();
        for (int j = 0; j < list1.size(); j++) {
            array[j] = (String) list1.get(j);
            Log.i("TEST", "--array[j]--" + array[j]);
            if (j == 0) {
                if (list1.size() == 1) {
                    sbstr.append("[\"" + array[j] + "\"]");
                } else {
                    sbstr.append("[\"" + array[j] + "\"");
                }

            } else if (j == list1.size() - 1) {
                sbstr.append(",\"" + array[j] + "\"]");
            } else {
                sbstr.append(",\"" + array[j] + "\"");
            }
        }
        Log.i("TEST", "--sbstr--" + sbstr);
        try {
            DigoIUserApiImpl api = new DigoIUserApiImpl();
            boolean issubmit = api.submit_comment(sid, eid, typeid, mycomment, sbstr.toString());
            if (issubmit == true) {
                handler.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(2);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (WSError e) {
            String msg = e.getMessage();
            if (msg.equals("-1302")) {
                handler.sendEmptyMessage(1302);
            }
            e.printStackTrace();
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
        oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider, conf);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mFileList.size(); i++) {
                    String str1 = mFileList.get(i);
                    uploadObject = andoridimgpath + Tool.getUninxToJavaDaya() + "/" + Tool.getUninxTimea() + i + ".jpg";
                    Log.i("lsq", "---uploadObject----" + uploadObject);
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

}
