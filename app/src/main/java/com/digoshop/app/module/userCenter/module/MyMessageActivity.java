package com.digoshop.app.module.userCenter.module;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.arrountshops.adp.ArountShop_textAdp;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.userCenter.adp.MyMessageAdp;
import com.digoshop.app.module.userCenter.model.MessageData;
import com.digoshop.app.module.userCenter.model.MessageInfo;
import com.digoshop.app.utils.db.CityBean;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;

/**
 * TODO 我的消息
 *
 * @author lsebyond lsqbeyond@yeah.net
 * @data: 2016-11-4  22:44
 * @version: V1.0
 */
public class MyMessageActivity extends BaseActivity implements OnClickListener {
    private ListView lv_mymessage;
    private PullToRefreshLayout ptrl;
    private int page = 1;
    private int page_length = 10;
    //消息的adp
    private MyMessageAdp myMessageAdp;

    private MessageData messageData;
    private TextView tv_messageweitotal, tv_messagetotal;
    private String noreadmessstr;
    private PopupWindow popupWindowquyu;
    private TextView tv_messagetype;
    private LinearLayout lin_messagetype;
    private PopupWindow popupWindowcate;
    private String typestr = "-1";
    private RelativeLayout re_nolist;
    private String isall = "0";// 清空(0-false, 1-true) empty=1则删除全部notice
    private int deleteint;
    private boolean isshow = false;
    private ArrayList<MessageInfo> messageBeans;//接口List
    private ArrayList<MessageInfo> messageBeansNew = new ArrayList<MessageInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercenter_mymessage);
        TextView    tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("我的消息");

        re_nolist = (RelativeLayout) findViewById(R.id.re_nolist);
        lin_messagetype = (LinearLayout) findViewById(R.id.lin_messagetype);
        lin_messagetype.setOnClickListener(this);
        tv_messagetype = (TextView) findViewById(R.id.tv_messagetype);
        tv_messageweitotal = (TextView) findViewById(R.id.tv_messageweitotal);
        tv_messagetotal = (TextView) findViewById(R.id.tv_messagetotal);
        lv_mymessage = (ListView) findViewById(R.id.lv_mymessage);
        ptrl = ((PullToRefreshLayout) findViewById(R.id.refresh_view));

        FlushMessageList();
        lv_mymessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent = new Intent(MyMessageActivity.this, MyMessageDetailAty.class);
                intent.putExtra("weburl", messageBeansNew.get(position).getTurl());
                intent.putExtra("nid", messageBeansNew.get(position).getNid());
                startActivity(intent);
            }
        });
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (getNetWifi()) {
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                    if (messageBeansNew.size() > 0) {
                        messageBeansNew.clear();
                    }
                    page = 1;
                    getMessageList();
                    myMessageAdp.notifyDataSetChanged();
                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }

                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (getNetWifi()) {
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                    if(messageBeans!=null){
                        if (messageBeans.size() > 0) {
                            messageBeans.clear();
                        }
                        page = page + 1;
                        getMessageList();
                        myMessageAdp.appendData(messageBeans);

                    }
                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }
        });

        myMessageAdp = new MyMessageAdp(MyMessageActivity.this, messageBeansNew);
        lv_mymessage.setAdapter(myMessageAdp);
    }

    @Override
    protected void onResume() {
        super.onResume();
       if(getNetWifi()){
           StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
           if (messageBeansNew.size() > 0) {
               messageBeansNew.clear();
           }
           if(myMessageAdp!=null){
               myMessageAdp.notifyDataSetChanged();
           }
           page = 1;
           getMessageList();
       }


    }

    //通知服务器 把当前用户的所有未读消息数重置为0
    private void FlushMessageList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FlushMessageListApi();
            }
        }).start();
    }

    private void FlushMessageListApi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            boolean isbool = api.FlushMessageList();
            Log.v("ceshi", "重置消息状态+" + isbool);
        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            e.printStackTrace();
        } catch (WSError e) {
            Log.v("ceshi", "WSError");
            e.printStackTrace();
        }
    }

    private void getMessageList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getMessageListApi();
            }
        }).start();
    }

    private void getMessageListApi() {

        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            //  messageData =new MessageData();
            messageData = api.get_message_list(typestr, page + "", page_length + "");
            // messageBeans = new ArrayList<MessageInfo>();

            if (messageData != null) {
                messageBeans = messageData.getMessageinfos();
                if(messageBeans!=null){
                    handler.sendEmptyMessage(1);
                }else{
                    handler.sendEmptyMessage(2);
                }
            } else {
                handler.sendEmptyMessage(2);
            }

        } catch (JSONException e) {
            handler.sendEmptyMessage(5);
            Log.v("ceshi", "JSONException");
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(2);
            Log.v("ceshi", "WSError");
            e.printStackTrace();
        }

    }

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Log.v("ceshi","messageBeansNew+"+messageBeansNew.size());
                    tv_messagetotal
                            .setText("总共" + messageData.getTotalstr() + "个消息");
                    messageBeansNew.addAll(messageBeans);
                    myMessageAdp.notifyDataSetChanged();
                    re_nolist.setVisibility(View.GONE);
                    StyledDialog.dismissLoading();

                    break;
                case 2:
                    if(page==1){
                        tv_messagetotal
                                .setText("总共" + "0" + "个消息");
                    }
                    ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    StyledDialog.dismissLoading();


                    break;
                case 3:
                    App.getInstance().showToast("请求失败");
                    StyledDialog.dismissLoading();

                    break;

                case 5:
                    App.getInstance().showToast("解析失败");
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
            case R.id.lin_messagetype: // 消息类别选择
                getPopupWindowcategory();
                // 这里是位置显示方式,在屏幕的下方
                popupWindowcate.showAsDropDown(lin_messagetype, Gravity.TOP,  21);
                popupWindowcate.showAsDropDown(v);
                break;
            case R.id.tv_alllmessagetype:

                break;
            case R.id.tv_servicemessagetype:

                break;
            case R.id.tv_storemessagetype:

                break;
            case R.id.tv_systemmessagetype:

                break;

            default:
                break;
        }
    }


    /***
     * 获取类别pop
     */
    private void getPopupWindowcategory() {
        if (null != popupWindowcate) {
            popupWindowcate.dismiss();
            return;
        } else {
            initPopuptWindowcate();
        }
    }

    /**
     * 创建商品类别pop
     */
    protected void initPopuptWindowcate() {
        ArrayList<CityBean> shopinfoarea = new ArrayList<>();
        CityBean cb1 = new CityBean();
        cb1.setNn("全部消息");
        shopinfoarea.add(cb1);
        CityBean cb2 = new CityBean();
        cb2.setNn("服务商铺消息");
        shopinfoarea.add(cb2);
        CityBean cb3 = new CityBean();
        cb3.setNn("商品商铺消息");
        shopinfoarea.add(cb3);
        CityBean cb4 = new CityBean();
        cb4.setNn("系统消息");
        shopinfoarea.add(cb4);
        ArountShop_textAdp arountShop_textAdp = new ArountShop_textAdp(MyMessageActivity.this, shopinfoarea);

        View popupWindow_view = MyMessageActivity.this.getLayoutInflater().inflate(
                R.layout.fragment_arountshop_listview, null, false);
        final ListView listView = (ListView) popupWindow_view.findViewById(R.id.lv_arountshop);
        listView.setAdapter(arountShop_textAdp);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int postion, long arg3) {
                if (null != popupWindowcate) {
                    popupWindowcate.dismiss();
                    popupWindowcate = null;
                }
                page = 1;
                if (postion == 0) {
                    if (null != popupWindowcate) {
                        popupWindowcate.dismiss();
                    }
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

                    if (messageBeansNew.size() > 0) {
                        messageBeansNew.clear();
                    }
                    typestr = "-1";
                    tv_messagetype.setText("全部消息");

                    getMessageList();
                    myMessageAdp.notifyDataSetChanged();

                } else if (postion == 1) {
                    if (null != popupWindowcate) {
                        popupWindowcate.dismiss();
                    }
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

                    if (messageBeansNew.size() > 0) {
                        messageBeansNew.clear();
                    }
                    typestr = "2";
                    tv_messagetype.setText("服务商铺消息");

                    getMessageList();
                    myMessageAdp.notifyDataSetChanged();
                }else if(postion==2){
                    if (null != popupWindowcate) {
                        popupWindowcate.dismiss();
                    }
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

                    if (messageBeansNew.size() > 0) {
                        messageBeansNew.clear();
                    }
                    typestr = "1";
                    tv_messagetype.setText("商品商铺消息");
                    getMessageList();
                    myMessageAdp.notifyDataSetChanged();
                }else if(postion==3){

                    if (null != popupWindowcate) {
                        popupWindowcate.dismiss();
                    }
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                    if (messageBeansNew.size() > 0) {
                        messageBeansNew.clear();
                    }
                    typestr = "100";
                    tv_messagetype.setText("系统消息");
                    getMessageList();
                    myMessageAdp.notifyDataSetChanged();
                }

            }
        });
        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        popupWindowcate = new PopupWindow(popupWindow_view,
                LayoutParams.MATCH_PARENT, 580, true);
        popupWindowcate.setFocusable(true);
        popupWindowcate.setBackgroundDrawable(new BitmapDrawable());
        // 设置动画效果
        popupWindowcate.setAnimationStyle(android.R.style.Animation_Dialog);
        // popupWindow.setAnimationStyle(R.style.AnimationFade);
        // 点击其他地方消失
        popupWindow_view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (popupWindowcate != null && popupWindowcate.isShowing()) {
                    popupWindowcate.dismiss();
                    popupWindowcate = null;
                }
                return false;
            }
        });
    }


}
