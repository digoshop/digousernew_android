package com.digoshop.app.api.impl;

import android.text.TextUtils;
import android.util.Log;

import com.digoshop.app.api.DigoIProductApi;
import com.digoshop.app.module.arrountshops.model.ShopInfoBean;
import com.digoshop.app.module.looksales.model.ExChangeBean;
import com.digoshop.app.module.looksales.model.ExChangeDetail_Data;
import com.digoshop.app.module.looksales.model.Pat;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.http.HttpConnUtil;
import com.digoshop.app.utils.http.ResolveResponse;
import com.digoshop.app.utils.http.WSError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DigoIProductApiImpl implements DigoIProductApi {

    @Override
    public ExChangeDetail_Data get_exchange_detail(String pid, String id) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("pid", pid);
        params.put("pt", "2");//商品类型(2换购)
        try {
            params.put("sign", Tool.getSignature(params));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // params.put("token", AppConfig.usertoken);
        params.put("uid", id);
        String result = HttpConnUtil.doPost(AppConfig.GET_EXCHANGE_DETAIL, params);
        Log.v("ceshi", "result" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);
        String datastr = json.optString("data");
        JSONObject datastrjson = new JSONObject(datastr);
        ExChangeDetail_Data exChangeDetail_Data = new ExChangeDetail_Data();
        // 添加店铺相关信息
        exChangeDetail_Data.setTargetName(datastrjson.optString("targetName"));
        exChangeDetail_Data.setTargetId(datastrjson.optString("targetId"));
        exChangeDetail_Data.setTargetAddress(datastrjson.optString("targetAddress"));
        exChangeDetail_Data.setTargetType(datastrjson.optString("targetType"));
        // 添加兑换商品信息
        String exchangeProductInfostr = datastrjson.optString("exchangeProductInfo");
        JSONObject exchangeProductInfostrjson = new JSONObject(exchangeProductInfostr);
        ExChangeBean exChangeBean = new ExChangeBean();
        //商品属性
        ArrayList<Pat> patlist = new ArrayList<Pat>();
        exChangeBean.setPattr(exchangeProductInfostrjson.optString("pattr"));
        if (!TextUtils.isEmpty(exchangeProductInfostrjson.optString("pat"))) {
            if (exchangeProductInfostrjson.getJSONArray("pat") != null) {
                if (exchangeProductInfostrjson.getJSONArray("pat").length() > 0) {
                    for (int i = 0; i < exchangeProductInfostrjson.getJSONArray("pat").length(); i++) {
                        Pat pat = new Pat();
                        pat.setStr(exchangeProductInfostrjson.getJSONArray("pat").get(i) + "");
                        patlist.add(pat);
                    }
                    exChangeBean.setPatlist(patlist);
                }
            }
        }


        //商品名称
        exChangeBean.setPna(exchangeProductInfostrjson.optString("pna"));
        //兑换价格
        exChangeBean.setEpp(exchangeProductInfostrjson.optString("epp"));
        //兑换类型
        exChangeBean.setPt(exchangeProductInfostrjson.optString("pt"));
        //竞拍结束时间
        exChangeBean.setEped(exchangeProductInfostrjson.optLong("eped"));
        //兑换开始时间：
        exChangeBean.setEpsd(exchangeProductInfostrjson.optLong("epsd"));
        //兑换状态 4兑换中，5已结束，6已兑换
        exChangeBean.setEps(exchangeProductInfostrjson.optString("eps"));
        //商品id
        exChangeBean.setPid(exchangeProductInfostrjson.optString("pid"));
        //商品大图
        ArrayList<String> urllist = new ArrayList<String>();

        for (int i = 0; i < exchangeProductInfostrjson.getJSONArray("pa").length(); i++) {
            String pat = (exchangeProductInfostrjson.getJSONArray("pa").get(i) + "");
            urllist.add(pat);
        }
        exChangeBean.setUrllist(urllist);
        //商品价格
        exChangeBean.setPpr(exchangeProductInfostrjson.optString("ppr"));
        //商品描述
        exChangeBean.setPd(exchangeProductInfostrjson.optString("pd"));
        //剩余数
        exChangeBean.setElnu(exchangeProductInfostrjson.optString("elnu"));
        //2为限量 1不限量
        exChangeBean.setLimit(exchangeProductInfostrjson.optString("limit"));
        //限量的剩余数量
        exChangeBean.setEplnu(exchangeProductInfostrjson.optString("eplnu"));
        //每天可兑换的数量
        exChangeBean.setEpnu(exchangeProductInfostrjson.optString("epnu"));
        //兑换规则
        exChangeBean.setEpd(exchangeProductInfostrjson.optString("epd"));
        //所需币
        exChangeBean.setEpg(exchangeProductInfostrjson.optString("epg"));

        exChangeDetail_Data.setExChangeBean(exChangeBean);
        return exChangeDetail_Data;
    }

    @Override
    public String Approve_Exchange(String pid) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("pid", pid);
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.APPROVE_EXCHANGE, params);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        Log.v("ceshi", "result" + result);
        JSONObject json = new JSONObject(result);
        String datastr = json.optString("data");
        JSONObject datajson = new JSONObject(datastr);
        if (datastr != null) {
            if (datastr.contains("intg")) {

                return datajson.optString("intg");
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public String getCustomNum(String nationCode) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("nationCode", nationCode);
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.CUSTOMNUM, params);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        Log.v("ceshi", "result" + result);
        JSONObject json = new JSONObject(result);
        String datastr = json.optString("data");
        if (TextUtils.isEmpty(datastr)) {
            return null;
        }
        JSONObject datajson = new JSONObject(datastr);
        String count = datajson.optString("count");
        if (TextUtils.isEmpty(count)) {
            return null;
        } else {
            return count;
        }
    }

    @Override
    public ArrayList<ShopInfoBean> ArountShops(String moid, String sort, String nationCode, String operateType, String regionId, String mid,
                                               String lat, String lgt, String page, String page_length) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("moid", moid);
        params.put("sort", sort);
        params.put("nationCode", nationCode);
        // 1商品 2 服务 全部空
        // if(Tool.isNullBool(operateType)){
        params.put("operateType", operateType);
        // }
        params.put("operateType", operateType);
        // 区县id 全部是空
        // if(Tool.isNullBool(regionId)){
        params.put("regionId", regionId);
        // }
        // 业态商圈id
        Log.v("lsq", "mid+=" + mid);
        Log.v("lsq", "isNullBool+=" + Tool.isNullBool(mid));

        if (Tool.isNullBool(mid)) {
            params.put("mid", mid);
        }
        if (Tool.isNullBool(lat)) {
            params.put("lat", lat);
        }
        if (Tool.isNullBool(lgt)) {
            params.put("lgt", lgt);
        }
        params.put("page", page);
        params.put("page_length", page_length);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.AROUNT_SHOPS, params);
        Log.i("zzrmmm", result + "");
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        String msg = ResolveResponse.resolveData(result).toString();
        Log.i("zzrmmm", result);
        Log.i("zzrsss", msg);
        JSONObject json = new JSONObject(result);

        JSONArray datajson = json.getJSONArray("data");
        ArrayList<ShopInfoBean> shopInfoBeans = new ArrayList<ShopInfoBean>();
        for (int i = 0; i < datajson.length(); i++) {
            JSONObject shopInfojson = (JSONObject) datajson.get(i);
            ShopInfoBean shopInfoBean = new ShopInfoBean();
            shopInfoBean.setProduct(shopInfojson.optString("product"));
            shopInfoBean.setCoupon(shopInfojson.optString("coupon"));
            shopInfoBean.setNews(shopInfojson.optString("news"));
            shopInfoBean.setAddress(shopInfojson.optString("address"));
            shopInfoBean.setContact(shopInfojson.optString("contact"));
            shopInfoBean.setCover(shopInfojson.optString("cover"));
            shopInfoBean.setDistance(shopInfojson.optDouble("distance"));
            shopInfoBean.setGoods(shopInfojson.optString("goods"));
            shopInfoBean.setName(shopInfojson.optString("name"));
            shopInfoBean.setStag(shopInfojson.optString("stag"));
            shopInfoBean.setOpen_time(shopInfojson.optString("open_time"));
            shopInfoBean.setOperate(shopInfojson.optString("operate"));
            shopInfoBean.setSid(shopInfojson.optString("sid"));
            shopInfoBeans.add(shopInfoBean);
        }

        if (shopInfoBeans == null) {
            return null;
        } else {
            if (shopInfoBeans.size() > 0) {
                return shopInfoBeans;
            } else {
                return null;
            }
        }


    }


}
