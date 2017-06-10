package com.digoshop.app.module.userCenter.adp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.shopdetail.bigimg.aty.ImagePagerActivity;
import com.digoshop.app.module.userCenter.model.CommentInfo;
import com.digoshop.app.utils.Displayer;
import com.digoshop.app.utils.Tool;
import com.github.library.bubbleview.BubbleTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

import static com.digoshop.R.id.iv_comment1;
import static com.digoshop.R.id.iv_comment3;
import static com.digoshop.R.id.tv_myreply_item_content;
import static com.digoshop.R.id.tv_myreply_item_replytype_icon;
import static com.digoshop.R.id.tv_myreply_item_title;
import static com.digoshop.app.module.shopdetail.bigimg.aty.ImagePagerActivity.EXTRA_IMAGE_URLS;

public class MyreplyAdp extends BaseAdapter {
    private Context mContext;
    private LayoutInflater minflater;
    private ArrayList<CommentInfo> commentinfos;
    private DisplayImageOptions options;
    private DisplayImageOptions optionsyuan;

    public MyreplyAdp(Context context, ArrayList<CommentInfo> commentinfos) {
        this.mContext = context;
        minflater = LayoutInflater.from(context);
        this.commentinfos = commentinfos;
        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.kcx_001)
                .showImageForEmptyUri(R.drawable.kcx_001).showImageOnFail(R.drawable.kcx_001).cacheInMemory()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
                .build();
        //圆形图片
        optionsyuan = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.defaultyuan)
                .showImageForEmptyUri(R.drawable.defaultyuan)
                .showImageOnFail(R.drawable.defaultyuan)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)   //设置图片的解码类型
                .displayer(new Displayer(0))
                .build();


    }

    public void appendData(List<CommentInfo> list) {
        this.commentinfos.addAll(list);
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return this.commentinfos != null ? this.commentinfos.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return commentinfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final viewholder holder;
        if (convertView == null) {
            holder = new viewholder();
            convertView = minflater.inflate(R.layout.activity_myreply_list_itemnew, null);
            // holder.iv_myreply_item_icon = (ImageView) convertView.findViewById(R.id.iv_myreply_item_icon);
            holder.iv_myreply_type = (ImageView) convertView.findViewById(R.id.iv_myreply_type);
            holder.iv_user_com_icon = (ImageView) convertView.findViewById(R.id.iv_user_com_icon);
            holder.iv_comment1 = (ImageView) convertView.findViewById(iv_comment1);
            holder.iv_comment2 = (ImageView) convertView.findViewById(R.id.iv_comment2);
            holder.iv_comment3 = (ImageView) convertView.findViewById(iv_comment3);
            holder.tv_myreply_item_time = (TextView) convertView.findViewById(R.id.tv_myreply_item_time);
            holder.tv_myreply_item_title = (TextView) convertView.findViewById(tv_myreply_item_title);
            holder.tv_reply_text = (BubbleTextView) convertView.findViewById(R.id.tv_reply_text);


            holder.tv_myreply_item_replytype_icon = (TextView) convertView
                    .findViewById(tv_myreply_item_replytype_icon);
            holder.tv_myreply_item_content = (TextView) convertView.findViewById(tv_myreply_item_content);
            convertView.setTag(holder);
        } else {
            holder = (viewholder) convertView.getTag();
        }

        final CommentInfo commentInfo = commentinfos.get(position);
        //用户头像
        ImageLoader.getInstance().displayImage(commentInfo.getAvatar(), holder.iv_user_com_icon, optionsyuan);
       //用户名字
        holder.tv_myreply_item_title.setText(commentInfo.getNick());
        //用户评论时间
        holder.tv_myreply_item_time.setText(Tool.getUninxToJavaDayJ(commentInfo.getCreate_time()));


        if (commentInfo.getType().equals("0")) {// 0好评 1中评 2 差评
            holder.tv_myreply_item_replytype_icon.setText("好评");
            holder.iv_myreply_type.setBackgroundResource(R.drawable.reply_a);
        } else if (commentInfo.getType().equals("1")) {
            holder.tv_myreply_item_replytype_icon.setText("中评");
            holder.iv_myreply_type.setBackgroundResource(R.drawable.reply_b);
        } else if (commentInfo.getType().equals("2")) {
            holder.tv_myreply_item_replytype_icon.setText("差评");
            holder.iv_myreply_type.setBackgroundResource(R.drawable.reply_c);
        }
        holder.tv_myreply_item_content.setText(commentInfo.getText());

        if (commentinfos != null) {
            if (commentInfo.getImgs().size() == 1) {
                holder.iv_comment1.setVisibility(View.VISIBLE);
                holder.iv_comment2.setVisibility(View.INVISIBLE);
                holder.iv_comment3.setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(commentInfo.getImgs().get(0), holder.iv_comment1, options);
            } else if (commentInfo.getImgs().size() == 2) {
                holder.iv_comment1.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(commentInfo.getImgs().get(0),
                        holder.iv_comment1, options);
                holder.iv_comment2.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(commentInfo.getImgs().get(1),
                        holder.iv_comment2, options);
                holder.iv_comment3.setVisibility(View.INVISIBLE);
            } else if (commentInfo.getImgs().size() == 3) {
                holder.iv_comment1.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(commentInfo.getImgs().get(0),
                        holder.iv_comment1, options);
                holder.iv_comment2.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(commentInfo.getImgs().get(1),
                        holder.iv_comment2, options);
                holder.iv_comment3.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(commentInfo.getImgs().get(2),
                        holder.iv_comment3, options);
            } else if (commentInfo.getImgs().size() == 0) {
                holder.iv_comment1.setVisibility(View.GONE);
                holder.iv_comment2.setVisibility(View.GONE);
                holder.iv_comment2.setVisibility(View.GONE);
            }
        } else {
            holder.iv_comment1.setVisibility(View.GONE);
            holder.iv_comment2.setVisibility(View.GONE);
            holder.iv_comment2.setVisibility(View.GONE);
        }
        if ("".equals(commentinfos.get(position).getReply_time()) || commentinfos.get(position).getReply_time() == null) {
            holder.tv_reply_text.setVisibility(View.GONE);
        } else {
            holder.tv_reply_text.setVisibility(View.VISIBLE);
            holder.tv_reply_text.setText("商家回复:" + commentinfos.get(position).getReply_text());

        }
        holder.iv_comment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (commentInfo.getImgs() != null) {
                    if (commentInfo.getImgs().size() > 0) {
                        Intent intenta = new Intent(mContext, ImagePagerActivity.class);
                        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                        intenta.putExtra(EXTRA_IMAGE_URLS, commentInfo.getImgs());
                        intenta.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
                        mContext.startActivity(intenta);
                    }
                }


            }
        });
        holder.iv_comment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commentInfo.getImgs() != null) {
                    if (commentInfo.getImgs().size() > 0) {
                        Intent intenta = new Intent(mContext, ImagePagerActivity.class);
                        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                        intenta.putExtra(EXTRA_IMAGE_URLS, commentInfo.getImgs());
                        intenta.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 1);
                        mContext.startActivity(intenta);
                    }
                }


            }
        });
        holder.iv_comment3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commentInfo.getImgs() != null) {
                    if (commentInfo.getImgs().size() > 0) {
                        Intent intenta = new Intent(mContext, ImagePagerActivity.class);
                        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                        intenta.putExtra(EXTRA_IMAGE_URLS, commentInfo.getImgs());
                        intenta.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 2);
                        mContext.startActivity(intenta);
                    }
                }

            }
        });

        // ImageLoader.getInstance().displayImage(shopInfoBeans.get(position).getCover(),
        // holder.iv_comment1, options);
        // holder.iv_comment1 = (ImageView)
        // convertView.findViewById(R.id.iv_comment1);
        // holder.iv_comment2 = (ImageView)
        // convertView.findViewById(R.id.iv_comment2);
        // holder.iv_comment3
        return convertView;
    }

    public class viewholder {
        BubbleTextView tv_reply_text;
        TextView tv_myreply_item_time , tv_myreply_item_title, tv_myreply_item_replytype_icon, tv_myreply_item_content;
        ImageView iv_myreply_type, iv_user_com_icon, iv_comment1, iv_comment2, iv_comment3;
    }


}
