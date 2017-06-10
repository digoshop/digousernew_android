package com.digoshop.app.module.shopdetailnew.adp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.shopdetail.bigimg.aty.ImagePagerActivity;
import com.digoshop.app.module.shopdetailnew.model.ShopComment;
import com.digoshop.app.utils.Displayer;
import com.digoshop.app.utils.Tool;
import com.github.library.bubbleview.BubbleTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

import static com.digoshop.R.id.tv_user_comtext;
import static com.digoshop.app.module.shopdetail.bigimg.aty.ImagePagerActivity.EXTRA_IMAGE_URLS;

/**
 * Created by lsqbeyond on 2017/3/10.
 */

public class ShopCommentAdp extends BaseAdapter {
    private Context mContext;
    private int typeadp=0;
    private LayoutInflater minflater;
    private ArrayList<ShopComment> shopInfoBeans = null;
    private DisplayImageOptions options;
    private DisplayImageOptions optionsyuan;
    public ShopCommentAdp(Context context, ArrayList<ShopComment> shopInfoBeans) {
        this.mContext = context;
        minflater = LayoutInflater.from(context);
        this.shopInfoBeans = shopInfoBeans;
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
        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.kcx_001)
                .showImageForEmptyUri(R.drawable.kcx_001).showImageOnFail(R.drawable.kcx_001).cacheInMemory()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
                .build();
    }
    public ShopCommentAdp(Context context, ArrayList<ShopComment> shopInfoBeans,int istype) {
        this.mContext = context;
        minflater = LayoutInflater.from(context);
        this.shopInfoBeans = shopInfoBeans;
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
        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.kcx_001)
                .showImageForEmptyUri(R.drawable.kcx_001).showImageOnFail(R.drawable.kcx_001).cacheInMemory()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
                .build();
    }

    @Override
    public int getCount() {

        return shopInfoBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return shopInfoBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("zzzppp", position + "");
        Viewholder holder = null;
        if (convertView == null) {
            convertView = minflater.inflate(R.layout.a_shop_comment_item, null);
            holder = new Viewholder();
            holder.iv_user_com_icon = (ImageView) convertView.findViewById(R.id.iv_user_com_icon);
            holder.iv_user_com_type = (ImageView) convertView.findViewById(R.id.iv_user_com_type);
            holder.iv_comment1 = (ImageView) convertView.findViewById(R.id.iv_comment1);
            holder.iv_comment2 = (ImageView) convertView.findViewById(R.id.iv_comment2);
            holder.iv_comment3 = (ImageView) convertView.findViewById(R.id.iv_comment3);
            holder.tv_reply_text = (BubbleTextView) convertView.findViewById(R.id.tv_reply_text);



            holder.tv_user_comname = (TextView) convertView.findViewById(R.id.tv_user_comname);
            holder.tv_user_comtime = (TextView) convertView.findViewById(R.id.tv_user_comtime);
            holder.tv_user_comtype = (TextView) convertView.findViewById(R.id.tv_user_comtype);
            holder.tv_user_comtext = (TextView) convertView.findViewById(tv_user_comtext);
           holder.lin_comment_all = (LinearLayout) convertView.findViewById(R.id.lin_comment_all);
            convertView.setTag(holder);
        } else {

            holder = (Viewholder) convertView.getTag();
        }
        final ShopComment commentInfo = shopInfoBeans.get(position);
        //用户头像
        ImageLoader.getInstance().displayImage(shopInfoBeans.get(position).getAvatar(), holder.iv_user_com_icon, optionsyuan);
        //用户名字
        holder.tv_user_comname.setText(shopInfoBeans.get(position).getNick());
        //用户评论时间
        if(!TextUtils.isEmpty(shopInfoBeans.get(position).getCreate_time())){
            holder.tv_user_comtime.setText(Tool.getUninxToJavaDayJ(shopInfoBeans.get(position).getCreate_time()));
        }
        if (shopInfoBeans.get(position).getType().equals("0")) {// 0好评 1中评 2 差评
            holder.tv_user_comtype.setText("评价:  "+"好评");
            holder.iv_user_com_type.setBackgroundResource(R.drawable.a_shopcoment_good);
        } else if (shopInfoBeans.get(position).getType().equals("1")) {
            holder.tv_user_comtype.setText("评价:  "+"中评");
            holder.iv_user_com_type.setBackgroundResource(R.drawable.a_shopcoment_midd);
        } else if (shopInfoBeans.get(position).getType().equals("2")) {
            holder.tv_user_comtype.setText("评价:  "+"差评");
            holder.iv_user_com_type.setBackgroundResource(R.drawable.a_shopcoment_bad);
        }
        holder.tv_user_comtext.setText( shopInfoBeans.get(position).getText());
        if (shopInfoBeans != null) {
            if(commentInfo.getStrings()!=null){
                if (commentInfo.getStrings().size() == 1) {
                    holder.lin_comment_all.setVisibility(View.VISIBLE);
                    holder.iv_comment1.setVisibility(View.VISIBLE);
                    holder.iv_comment2.setVisibility(View.INVISIBLE);
                    holder.iv_comment3.setVisibility(View.INVISIBLE);
                    ImageLoader.getInstance().displayImage(commentInfo.getStrings().get(0), holder.iv_comment1, options);
                } else if (commentInfo.getStrings().size() == 2) {
                    holder.lin_comment_all.setVisibility(View.VISIBLE);
                    holder.iv_comment1.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(commentInfo.getStrings().get(0),
                            holder.iv_comment1, options);
                    holder.iv_comment2.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(commentInfo.getStrings().get(1),
                            holder.iv_comment2, options);
                    holder.iv_comment3.setVisibility(View.INVISIBLE);
                } else if (commentInfo.getStrings().size() == 3) {
                    holder.lin_comment_all.setVisibility(View.VISIBLE);
                    holder.iv_comment1.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(commentInfo.getStrings().get(0),
                            holder.iv_comment1, options);
                    holder.iv_comment2.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(commentInfo.getStrings().get(1),
                            holder.iv_comment2, options);
                    holder.iv_comment3.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(commentInfo.getStrings().get(2),
                            holder.iv_comment3, options);
                } else if (commentInfo.getStrings().size() == 0) {
                    holder.lin_comment_all.setVisibility(View.GONE);
                    holder.iv_comment1.setVisibility(View.GONE);
                    holder.iv_comment2.setVisibility(View.GONE);
                    holder.iv_comment2.setVisibility(View.GONE);
                }
            }else{
                holder.lin_comment_all.setVisibility(View.GONE);
                holder.iv_comment1.setVisibility(View.GONE);
                holder.iv_comment2.setVisibility(View.GONE);
                holder.iv_comment2.setVisibility(View.GONE);
            }

        } else {
            holder.lin_comment_all.setVisibility(View.GONE);
            holder.iv_comment1.setVisibility(View.GONE);
            holder.iv_comment2.setVisibility(View.GONE);
            holder.iv_comment2.setVisibility(View.GONE);
        }

        holder.iv_comment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (commentInfo.getStrings() != null) {
                    if (commentInfo.getStrings().size() > 0) {
                        Intent intenta = new Intent(mContext, ImagePagerActivity.class);
                        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                        intenta.putExtra(EXTRA_IMAGE_URLS, commentInfo.getStrings());
                        intenta.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
                        mContext.startActivity(intenta);
                    }
                }


            }
        });
        holder.iv_comment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commentInfo.getStrings() != null) {
                    if (commentInfo.getStrings().size() > 0) {
                        Intent intenta = new Intent(mContext, ImagePagerActivity.class);
                        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                        intenta.putExtra(EXTRA_IMAGE_URLS, commentInfo.getStrings());
                        intenta.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 1);
                        mContext.startActivity(intenta);
                    }
                }


            }
        });
        holder.iv_comment3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commentInfo.getStrings() != null) {
                    if (commentInfo.getStrings().size() > 0) {
                        Intent intenta = new Intent(mContext, ImagePagerActivity.class);
                        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                        intenta.putExtra(EXTRA_IMAGE_URLS, commentInfo.getStrings());
                        intenta.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 2);
                        mContext.startActivity(intenta);
                    }
                }

            }
        });
        if ("".equals(commentInfo.getReply_text()) || commentInfo.getReply_text() == null) {
            holder.tv_reply_text.setVisibility(View.GONE);
        } else {
            holder.tv_reply_text.setVisibility(View.VISIBLE);
            holder.tv_reply_text.setText("商家回复:" +commentInfo.getReply_text());

        }
        return convertView;
    }

    public void appendData(List<ShopComment> list) {
        this.shopInfoBeans.addAll(list);
        notifyDataSetChanged();
    }

    private class Viewholder {
        BubbleTextView tv_reply_text;
        private LinearLayout lin_comment_all;
        ImageView iv_user_com_icon,iv_user_com_type,iv_comment1,iv_comment2,iv_comment3;
        TextView tv_user_comname,tv_user_comtime,tv_user_comtype,tv_user_comtext;
    }
}
