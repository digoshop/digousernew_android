package com.digoshop.app.module.userCenter.adp;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.userCenter.model.MessageInfo;
import com.digoshop.app.utils.Tool;

import java.util.ArrayList;

public class MyMessageAdp extends BaseAdapter {
    private ArrayList<MessageInfo> messageBeans;
    private Context context;
    private MessageInfo messageBean;
    public MyMessageAdp(Context mContext, ArrayList<MessageInfo> messageBeans){
        this.context = mContext;
        this.messageBeans = messageBeans;
    }
    @Override
    public int getCount() {
        return messageBeans.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return messageBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        messageBean = messageBeans.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.activity_mymessage_item, null);
            holder.tv_mymessage_itme_sender = (TextView) convertView
                    .findViewById(R.id.tv_mymessage_itme_sender);
            holder.tv_mymessage_item_time = (TextView) convertView
                    .findViewById(R.id.tv_mymessage_item_time);
            holder.tv_mymessage_item_title = (TextView) convertView
                    .findViewById(R.id.tv_mymessage_item_title);
            holder.tv_mymessage_itme_type = (TextView) convertView
                    .findViewById(R.id.tv_mymessage_itme_type);

            holder.iv_messageicon = (ImageView) convertView
                    .findViewById(R.id.iv_messageicon);
            holder.iv_messagetype = (ImageView) convertView
                    .findViewById(R.id.iv_messagetype);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_mymessage_itme_sender.setText(messageBean.getSender());
        holder.tv_mymessage_item_title.setText(messageBean.getTitle());

        holder.tv_mymessage_item_time.setText(Tool
                .TimeStamp2Date(messageBean.getCreate_time(),""));

        if (messageBean.getType().equals("100")) {
            holder.tv_mymessage_itme_type.setText("  |  "+"系统消息");
            holder.iv_messageicon.setBackgroundResource(R.drawable.xitongmess);
        } else if (messageBean.getType().equals("2")) {
            holder.tv_mymessage_itme_type.setText("  |  "+"服务商铺消息");
            holder.iv_messageicon.setBackgroundResource(R.drawable.fuwumess);
        } else if (messageBean.getType().equals("1")) {
            holder.tv_mymessage_itme_type.setText("  |  "+"商品商铺消息");
            holder.iv_messageicon.setBackgroundResource(R.drawable.shangjiamess);
        }
        if (!TextUtils.isEmpty(messageBean.getStatus())) {
            // 0是未读1是已读
            if (messageBean.getStatus().equals("0")) {
                holder.iv_messagetype.setVisibility(View.VISIBLE);
            } else if (messageBean.getStatus().equals("1")) {
                holder.iv_messagetype.setVisibility(View.INVISIBLE);
            }
        }

        return convertView;
    }

    class ViewHolder {
        private TextView tv_mymessage_itme_sender,tv_mymessage_itme_type, tv_mymessage_item_time,
                tv_mymessage_item_title;
        private ImageView iv_messageicon, iv_messagetype;
    }
    public void appendData(ArrayList<MessageInfo> list) {
        this.messageBeans.addAll(list);
        notifyDataSetChanged();
    }
}
