package com.digoshop.app.module.userCenter.module;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.userCenter.adp.MyCardAdp;
import com.digoshop.app.module.userCenter.model.MycardBagEntity;
import com.digoshop.app.module.userCenter.model.MycardData;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;

/**
 * 
 * TODO 我的卡包
 * 
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016-6-11 下午10:31:16
 * @version: V1.0
 */
public class MyCardActivity extends BaseActivity implements OnClickListener {
	private ListView lv_mycard;
	private TextView tv_title_right;
	private MycardData mycardData;
	private ArrayList<MycardBagEntity> McbearrayList;
	private ArrayList<MycardBagEntity> McbearrayListNew = new ArrayList<>();
	private TextView tv_count, tv_eintg;
    private RelativeLayout rl_nolist;
	private PullToRefreshLayout ptrl;
	private int page = 1;
	private int page_length = 10;
	private MyCardAdp mycardadp;
	private TextView tv_gettime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usercenter_mycard);
		TextView    tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		tv_title_content.setText("我的卡包");
		tv_gettime = (TextView) findViewById(R.id.tv_gettime);
		tv_gettime.setText("截止: "+Tool.getCurrentDate());
		lv_mycard = (ListView) findViewById(R.id.lv_mycard);

		tv_title_right = (TextView) findViewById(R.id.tv_title_right);
		tv_title_right.setVisibility(View.VISIBLE);
		tv_title_right.setOnClickListener(this);
		tv_title_right.setText("商家邀请");
		tv_count = (TextView) findViewById(R.id.tv_count);
		tv_eintg = (TextView) findViewById(R.id.tv_eintg);
		rl_nolist = (RelativeLayout) findViewById(R.id.rl_nolist);
		lv_mycard.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent myIntent = new Intent();
				myIntent.putExtra("intg", McbearrayListNew.get(arg2).getIntg());

				myIntent.putExtra("vip_level",McbearrayListNew.get(arg2).getVip_level());

				myIntent.putExtra("name", McbearrayListNew.get(arg2).getShopinfo().getName());
				myIntent.putExtra("address", McbearrayListNew.get(arg2).getShopinfo().getAddress());
				myIntent.putExtra("sava", McbearrayListNew.get(arg2).getShopinfo().getSava());
				myIntent.putExtra("vipcode", McbearrayListNew.get(arg2).getVip_code());
				myIntent.putExtra("createtime", McbearrayListNew.get(arg2).getCreate_time());
				myIntent.putExtra("vipid", McbearrayListNew.get(arg2).getVip_id());
				myIntent.setClass(MyCardActivity.this, MyMembershipCard.class);
				startActivity(myIntent);
			}
		});


		mycardadp=new MyCardAdp(MyCardActivity.this,McbearrayListNew);
		lv_mycard.setAdapter(mycardadp);
		ptrl = ((PullToRefreshLayout) findViewById(R.id.refresh_view));
		ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				if (getNetWifi()) {
					if (McbearrayListNew.size() > 0) {
						McbearrayListNew.clear();
					}
					page = 1;
					page_length = 10;
					get_MycardBag();
					mycardadp.notifyDataSetChanged();
				}

				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}

			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				if (getNetWifi()) {
					if(McbearrayList!=null){
						if (McbearrayList.size() > 0) {
							McbearrayList.clear();
						}
						page = page + 1;
						page_length = 10;
						get_MycardBag();
						mycardadp.appendData(McbearrayList);
					}
				}

				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (getNetWifi()) {
			if (McbearrayListNew.size() > 0) {
				McbearrayListNew.clear();
			}
			StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
			if(mycardadp!=null){
				mycardadp.notifyDataSetChanged();
			}
			get_MycardBag();
		} else {
			App.getInstance().showToast("网络不给力，请检查网络设置");
		}
	}

	private void get_MycardBag() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				get_MycardBagwall();
			}
		}).start();
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				rl_nolist.setVisibility(View.GONE);
				McbearrayListNew.addAll(McbearrayList);
				mycardadp.notifyDataSetChanged();
				tv_count.setText(mycardData.getTotal());
				tv_eintg.setText(mycardData.getEintg());
				StyledDialog.dismissLoading();

				break;
			case 2:
				rl_nolist.setVisibility(View.GONE);
			 App.getInstance().showToast("没有数据了!");
				StyledDialog.dismissLoading();

				break;
			case 3:
				rl_nolist.setVisibility(View.VISIBLE);
				App.getInstance().showToast("解析异常");
				StyledDialog.dismissLoading();

				break;
			case 4:
				rl_nolist.setVisibility(View.VISIBLE);
				App.getInstance().showToast("请求异常");
				StyledDialog.dismissLoading();

				break;
			}
		}
	};

	private void get_MycardBagwall() {
		DigoIUserApiImpl api = new DigoIUserApiImpl();
		mycardData = new MycardData();
		McbearrayList = new ArrayList<MycardBagEntity>();
		try {
			mycardData = api.getMycardbagWall(page+"", page_length+"");
			if(mycardData!=null){
				McbearrayList = mycardData.getArrayList();
				if (McbearrayList != null & McbearrayList.size() > 0) {
					Log.v("TEST", "---MbeList---" + McbearrayList.size());
					handler.sendEmptyMessage(1);
				} else {
					handler.sendEmptyMessage(2);
				}
			}else{
				handler.sendEmptyMessage(2);
			}

		} catch (JSONException e) {
			Log.v("ceshi", "JSONException");
			handler.sendEmptyMessage(3);
			e.printStackTrace();
		} catch (WSError e) {
			handler.sendEmptyMessage(2);
			Log.v("ceshi", "WSError");
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.tv_title_right:
			Intent intent = new Intent();
			intent.setClass(MyCardActivity.this,ShopInviteListActivity.class);
			startActivity(intent);
			break;
		default:
			break;
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
}
