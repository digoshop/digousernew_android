package com.digoshop.app.module.home.couponactive;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.home.model.ActivityDetailBean;
import com.digoshop.app.module.shopdetailnew.ShopDetailNewSNActivity;
import com.digoshop.app.module.storedetail.StoreDetailActivity;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.DigoGps;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONException;

import java.io.File;
import java.net.URISyntaxException;

import static com.digoshop.app.utils.Tool.getNetWifi;

/**
 * 
 * TODO 活动详情页面
 * 
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016年6月22日 下午8:57:02
 * @version: V1.0
 */
public class ActivityDetails extends BaseActivity implements OnClickListener {
	private ActivityDetailBean homeactivityinfo;
	private DisplayImageOptions options;
	private ImageView iv_atydetail_icon;
	private TextView tv_atydetail_name, tv_atydetail_storename, tv_activitydetail_distance, tv_atydetail_time,
			tv_atydetail_location, tv_atydetail_content;
	private String lat;
	private String lon;
	private String mnid;
	private ImageView iv_atydetial_icon;
	private LinearLayout lin_atydetailgps;
	private RelativeLayout re_atydetail_shop;
	private String shopnamestr;
	private Double latd, lgtd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activitydetails_main);
		init();
		lat = LocalSave.getValue(ActivityDetails.this, AppConfig.basefile, "lat", "");
		lon = LocalSave.getValue(ActivityDetails.this, AppConfig.basefile, "lon", "");
		Intent intent = getIntent();
		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.kcx_001)
				.showImageForEmptyUri(R.drawable.kcx_001).showImageOnFail(R.drawable.kcx_001).cacheInMemory()
				.imageScaleType(ImageScaleType.NONE).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
				.build();
		homeactivityinfo = (ActivityDetailBean) intent.getSerializableExtra("atycontent");

		if (homeactivityinfo == null) {
			mnid = intent.getStringExtra("mnid");
		} else {
			mnid = homeactivityinfo.getMnid();
		}
		if (getNetWifi()) {
			atyinfo();
		} else {
			App.getInstance().showToast("网络不给力，请检查网络设置");
		}


		iv_atydetial_icon = (ImageView) findViewById(R.id.iv_atydetial_icon);

	}

	private void init() {
		TextView    tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		tv_title_content.setText("活动详情");
		lin_atydetailgps = (LinearLayout) findViewById(R.id.lin_atydetailgps);
		re_atydetail_shop = (RelativeLayout) findViewById(R.id.re_atydetail_shop);
		lin_atydetailgps.setOnClickListener(this);
		re_atydetail_shop.setOnClickListener(this);
		iv_atydetial_icon = (ImageView) findViewById(R.id.iv_atydetial_icon);
		iv_atydetail_icon = (ImageView) findViewById(R.id.iv_atydetail_icon);
		tv_atydetail_name = (TextView) findViewById(R.id.tv_atydetail_name);
		tv_atydetail_storename = (TextView) findViewById(R.id.tv_atydetail_storename);
		tv_activitydetail_distance = (TextView) findViewById(R.id.tv_activitydetail_distance);
		tv_atydetail_time = (TextView) findViewById(R.id.tv_atydetail_time);
		tv_atydetail_location = (TextView) findViewById(R.id.tv_atydetail_location);
		tv_atydetail_content = (TextView) findViewById(R.id.tv_atydetail_content);
	}

	private void atyinfo() {
		StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				Atydetail();
			}
		}).start();

	}

	private void Atydetail() {

		DigoIUserApiImpl api = new DigoIUserApiImpl();
		try {
			homeactivityinfo = api.getNewById(mnid, lon, lat);
			if (homeactivityinfo != null) {
				handler.sendEmptyMessage(1);
			} else {
				handler.sendEmptyMessage(2);
			}

		} catch (JSONException e) {
			handler.sendEmptyMessage(2);
			Log.v("ceshi", "JSONException");
			e.printStackTrace();
		} catch (WSError e) {
			//if("-2".equals(e.getMessage())){
			handler.sendEmptyMessage(3);
			finish();
			//}

			Log.v("ceshi", "WSError");
			e.printStackTrace();
		}

	}

	Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 1:
					if (homeactivityinfo.getMnp() != null) {
						shopnamestr = homeactivityinfo.getTname();
						DisplayMetrics dm = new DisplayMetrics();
						getWindowManager().getDefaultDisplay().getMetrics(dm);
						int screenWidth = dm.widthPixels;
						int screenHeight = dm.heightPixels;
						iv_atydetail_icon.setImageResource(R.drawable.defaulhomehuan);
						ViewGroup.LayoutParams params = iv_atydetail_icon.getLayoutParams();
						params.height = screenWidth * 3 / 7;
						params.width = screenWidth;
						iv_atydetail_icon.setLayoutParams(params);
						iv_atydetail_icon.setVisibility(View.VISIBLE);
						ImageLoader.getInstance().displayImage(homeactivityinfo.getMnp(), iv_atydetail_icon, options);
					}
					if (homeactivityinfo.getPointBean() != null) {
						latd = homeactivityinfo.getPointBean().getLat();
						lgtd = homeactivityinfo.getPointBean().getLgt();
					}
					tv_atydetail_name.setText(Tool.isNullStr(homeactivityinfo.getMnti()));
					tv_atydetail_storename.setText(Tool.isNullStr(homeactivityinfo.getTname()));
					if (homeactivityinfo.getDistance()<1) {
						iv_atydetial_icon.setVisibility(View.INVISIBLE);
					} else {
						iv_atydetial_icon.setVisibility(View.INVISIBLE);
						tv_activitydetail_distance.setText(" | " + Tool.getGpskmorm(homeactivityinfo.getDistance()));
					}
					if (homeactivityinfo.getMnvsd() != null & homeactivityinfo.getMnved() != null) {
						long startl = Long.parseLong(homeactivityinfo.getMnvsd());
						long endl = Long.parseLong(homeactivityinfo.getMnved());
						tv_atydetail_time
								.setText(Tool.isNullStr(Tool.getUninxToJavaDay(startl) + "-" + Tool.getUninxToJavaDay(endl)));
					}
					tv_atydetail_location.setText(Tool.isNullStr(homeactivityinfo.getMnad()));
					tv_atydetail_content.setText(Tool.isNullStr(homeactivityinfo.getMnc()));
					StyledDialog.dismissLoading();

					break;
				case 2:
					App.getInstance().showToast("解析失败");
					StyledDialog.dismissLoading();

					break;
				case 3:
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
	public void onClick(View v) {
		switch (v.getId()) {

			case R.id.lin_atydetailgps:
				showPopwindow();
				break;
			case R.id.re_atydetail_shop:
				if (homeactivityinfo != null) {

					Intent intent = new Intent();
					if ("1".equals(homeactivityinfo.getMntt())) {
						intent.putExtra("storeid", homeactivityinfo.getMntid());
						intent.putExtra("gpsjui", homeactivityinfo.getDistance());
						intent.setClass(ActivityDetails.this, StoreDetailActivity.class);
						startActivity(intent);
					} else if ("2".equals(homeactivityinfo.getMntt())) {
						intent.putExtra("sid", homeactivityinfo.getMntid());
						intent.putExtra("gpsjui", homeactivityinfo.getDistance());
						intent.setClass(ActivityDetails.this, ShopDetailNewSNActivity.class);
						startActivity(intent);
					}
//                shopid = intent.getStringExtra("sid");
//                gpsjui = intent.getStringExtra("gpsjui");
					// mntt ;// 1 商户活动  2店铺活动


				}
				break;

			default:
				break;
		}

	}

	private void showPopwindow() {
		View parent = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
		View popView = View.inflate(this, R.layout.camera_pop_menu, null);

		Button btnCamera = (Button) popView.findViewById(R.id.btn_camera_pop_camera);
		Button btnAlbum = (Button) popView.findViewById(R.id.btn_camera_pop_album);
		Button btnCancel = (Button) popView.findViewById(R.id.btn_camera_pop_cancel);
		btnCamera.setText("高德地图");
		btnAlbum.setText("百度地图");
		int width = getResources().getDisplayMetrics().widthPixels;
		int height = getResources().getDisplayMetrics().heightPixels;

		final PopupWindow popWindow = new PopupWindow(popView, width, height);
		popWindow.setAnimationStyle(R.style.AnimBottom);
		popWindow.setFocusable(true);
		popWindow.setOutsideTouchable(false);// 设置允许在外点击消失
		View.OnClickListener listener = new View.OnClickListener() {

			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.btn_camera_pop_camera:
						if (isInstallByread("com.autonavi.minimap")) {
							openGaoDeMap();
						} else {
							App.getInstance().showToast("没有检测到高德地图,请安装高德地图!");
						}
						break;
					case R.id.btn_camera_pop_album:
						if (isInstallByread("com.baidu.BaiduMap")) {
							openBaiduMap();
						} else {
							App.getInstance().showToast("没有检测到百度地图,请安百度地图!");
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

	private boolean isInstallByread(String packageName) {
		return new File("/data/data/" + packageName).exists();
	}

	private void openBaiduMap() {
		try {
			DigoGps gaodeigps = Tool.gcj02_To_Bd09New(latd, lgtd);
			Intent intent = Intent.getIntent("intent://map/marker?coord_type=gcj02&location="
					+ gaodeigps.getWgLat() + "," + gaodeigps.getWgLon() + "&title=" + shopnamestr + "&content="
					+ shopnamestr + "&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
			startActivity(intent);
		} catch (URISyntaxException e) {
			App.getInstance().showToast("您尚未安装百度地图app或app版本过低!");
			e.printStackTrace();
		} catch (ActivityNotFoundException e) {
			App.getInstance().showToast("您尚未安装百度地图app或app版本过低!");
			e.printStackTrace();
		} catch (Exception e) {
			App.getInstance().showToast("您尚未安装百度地图app或app版本过低!");
			e.printStackTrace();
		}

	}

	private void openGaoDeMap() {
		try {      //   latlng = new LatLng(latd, lgtd);
			Intent intent1 = Intent.getIntent("androidamap://viewMap?sourceApplication=" + "迪购" + "&poiname=" + shopnamestr + "&lat=" + latd + "&lon=" + lgtd + "&dev=0");
			startActivity(intent1);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ActivityNotFoundException e) {
			App.getInstance().showToast("没有检测到高德地图,请安装高德地图!");
			e.printStackTrace();
		} catch (Exception e) {
			App.getInstance().showToast("您尚未安装高德地图app或app版本过低!");
			e.printStackTrace();
		}
	}
}
