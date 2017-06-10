package com.digoshop.app.api.impl;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.digoshop.app.App;
import com.digoshop.app.api.DigoIUserApi;
import com.digoshop.app.base.BaseInfo;
import com.digoshop.app.module.arrountshops.model.ArrountitemBean;
import com.digoshop.app.module.arrountshops.model.MyArrayListCityBean;
import com.digoshop.app.module.arrountshops.model.ShopInfoBean;
import com.digoshop.app.module.customServices.model.CategoryChooseBean;
import com.digoshop.app.module.customServices.model.ChildernBean;
import com.digoshop.app.module.customServices.model.ImgeUrl;
import com.digoshop.app.module.home.cityselect.model.HotCityBean;
import com.digoshop.app.module.home.model.ActivityDetailBean;
import com.digoshop.app.module.home.model.Adimg;
import com.digoshop.app.module.home.model.HomeInfo;
import com.digoshop.app.module.home.model.NewsBeean;
import com.digoshop.app.module.home.model.ShopDetailInfo;
import com.digoshop.app.module.looksales.model.AuctionBean;
import com.digoshop.app.module.looksales.model.ExChangeBean;
import com.digoshop.app.module.looksales.model.ExChangeDetail_Data;
import com.digoshop.app.module.looksales.model.FlashSaleDetailBean;
import com.digoshop.app.module.looksales.model.FlashSaleListItemBean;
import com.digoshop.app.module.looksales.model.Pat;
import com.digoshop.app.module.looksales.model.PoitGoodBean;
import com.digoshop.app.module.looksales.model.ProductInfoDto;
import com.digoshop.app.module.looksales.model.SaleHistoryBean;
import com.digoshop.app.module.looksales.model.TiemTitleData;
import com.digoshop.app.module.looksales.model.TimeTitleBean;
import com.digoshop.app.module.looksales.xianshi.bean.AddPriceBean;
import com.digoshop.app.module.search.ShopinfoData;
import com.digoshop.app.module.seekshop.model.BrandDate;
import com.digoshop.app.module.seekshop.model.SeekCateGoryData;
import com.digoshop.app.module.setting.model.AppUpdata;
import com.digoshop.app.module.shopdetail.model.PointBean;
import com.digoshop.app.module.shopdetail.model.ShopDetailDataBean;
import com.digoshop.app.module.shopdetail.model.ShopbrandBean;
import com.digoshop.app.module.shopdetail.model.ShopoperateBean;
import com.digoshop.app.module.shopdetailnew.model.ShopComment;
import com.digoshop.app.module.shopdetailnew.model.ShopDetailNData;
import com.digoshop.app.module.shopdetailnew.model.ShopProduct;
import com.digoshop.app.module.storedetail.model.FloorBean;
import com.digoshop.app.module.storedetail.model.Merchant;
import com.digoshop.app.module.storedetail.model.Shopdatalist;
import com.digoshop.app.module.storedetail.model.StoreDetailInfo;
import com.digoshop.app.module.storedetail.model.StoreDetailMomInfo;
import com.digoshop.app.module.storedetail.model.StoredetailMaInfo;
import com.digoshop.app.module.storedetail.model.StoredetailMllInfo;
import com.digoshop.app.module.storedetail.model.UserInfoData;
import com.digoshop.app.module.userCenter.model.AddShopInviteUserInfo;
import com.digoshop.app.module.userCenter.model.CommentData;
import com.digoshop.app.module.userCenter.model.CommentInfo;
import com.digoshop.app.module.userCenter.model.ConsumerSettlementrecords;
import com.digoshop.app.module.userCenter.model.CostHistorData;
import com.digoshop.app.module.userCenter.model.CouponDetailData;
import com.digoshop.app.module.userCenter.model.CurlistEntity;
import com.digoshop.app.module.userCenter.model.CustomizedReply;
import com.digoshop.app.module.userCenter.model.DiscountcouponData;
import com.digoshop.app.module.userCenter.model.Discountcoupons;
import com.digoshop.app.module.userCenter.model.Markbean;
import com.digoshop.app.module.userCenter.model.MembreEntity;
import com.digoshop.app.module.userCenter.model.MerchanRreplyEntity;
import com.digoshop.app.module.userCenter.model.MessageData;
import com.digoshop.app.module.userCenter.model.MessageInfo;
import com.digoshop.app.module.userCenter.model.MyLikeListData;
import com.digoshop.app.module.userCenter.model.MyLikesEntity;
import com.digoshop.app.module.userCenter.model.MybagEntity;
import com.digoshop.app.module.userCenter.model.MybagEntityData;
import com.digoshop.app.module.userCenter.model.MycardBagEntity;
import com.digoshop.app.module.userCenter.model.MycardData;
import com.digoshop.app.module.userCenter.model.MymenbrescardData;
import com.digoshop.app.module.userCenter.model.NumBean;
import com.digoshop.app.module.userCenter.model.PointsData;
import com.digoshop.app.module.userCenter.model.PointsEntity;
import com.digoshop.app.module.userCenter.model.ShopInviteDetailBean;
import com.digoshop.app.module.userCenter.model.SingData;
import com.digoshop.app.module.userCenter.model.UserDataBean;
import com.digoshop.app.module.userCenter.model.UserStatBean;
import com.digoshop.app.module.welcome.module.DeviceInfo;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.Constants;
import com.digoshop.app.utils.FastJsonUtils;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.db.CityBean;
import com.digoshop.app.utils.http.HttpConnUtil;
import com.digoshop.app.utils.http.ResolveResponse;
import com.digoshop.app.utils.http.WSError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.digoshop.app.utils.Tool.getStringToDate;

public class DigoIUserApiImpl implements DigoIUserApi {
    @Override
    public ArrayList<ShopProduct> get_MyProductNotLogged(String nationCode, String prtag, String page) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("nationCode", nationCode);
        params.put("page", page);
        params.put("page_length", "10");
        params.put("prtag", prtag);
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.HOMESALELIST, params);
        Log.v("ceshi", "result+" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        PoitGoodBean change_Data = null;
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);
        if (!json.isNull("data")) {
            ArrayList<ShopProduct> changeBeans = new ArrayList<ShopProduct>(
                    JSONArray.parseArray(json.optString("data"), ShopProduct.class));
            return changeBeans;
        } else {
            return null;
        }
    }

    @Override
    public boolean PutPoneisAlive(String phone, String devicetoken, String deviceuid) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("passport", phone);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", devicetoken);
        params.put("uid", deviceuid);
        String result = HttpConnUtil.doPost(AppConfig.ISPHONEALIVE, params);
        if (TextUtils.isEmpty(result)) {
            return false;
        }
        if (result.contains("success")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ExChangeDetail_Data getProductDetail(String pid, String pt) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("pid", pid);
        params.put("pt", pt);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.PRODUCTDETAILNEWW, params);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);
        String datastr = json.optString("data");
        if (TextUtils.isEmpty(datastr)) {
            return null;
        }
        JSONObject datastrjson = new JSONObject(datastr);
        ExChangeDetail_Data exChangeDetail_Data = new ExChangeDetail_Data();
        // 添加店铺相关信息
        exChangeDetail_Data.setTargetSm(datastrjson.optString("targetSm"));
        exChangeDetail_Data.setTargetSava(datastrjson.optString("targetSava"));
        exChangeDetail_Data.setProductCount(datastrjson.optString("productCount"));
        exChangeDetail_Data.setCouponCount(datastrjson.optString("couponCount"));
        exChangeDetail_Data.setTargetName(datastrjson.optString("targetName"));
        exChangeDetail_Data.setTargetId(datastrjson.optString("targetId"));
        exChangeDetail_Data.setTargetAddress(datastrjson.optString("targetAddress"));
        exChangeDetail_Data.setTargetType(datastrjson.optString("targetType"));
        //店铺优惠券
        if (!TextUtils.isEmpty(datastrjson.optString("coupons"))) {
            if (datastrjson.optJSONArray("coupons").length() > 0) {
                org.json.JSONArray couponarray = datastrjson.optJSONArray("coupons");
                ArrayList<Discountcoupons> discountcouponses = new ArrayList<>();
                for (int n = 0; n < datastrjson.optJSONArray("coupons").length(); n++) {
                    JSONObject couponTypejson = (JSONObject) couponarray.get(n);
                    Discountcoupons dictBean = new Discountcoupons();
                    dictBean.setCtn(couponTypejson.optString("ctn"));
                    dictBean.setCtid(couponTypejson.optString("ctid"));
                    org.json.JSONArray operatesarray = couponTypejson.optJSONArray("operates");
                    if (operatesarray == null || "".equals(operatesarray) || operatesarray.length() == 0) {

                    } else {
                        StringBuilder oeratesb = new StringBuilder();
                        for (int h = 0; h < operatesarray.length(); h++) {
                            if (h == 0) {
                                oeratesb.append(operatesarray.get(h).toString());

                            } else {
                                oeratesb.append("、" + operatesarray.get(h).toString());

                            }
                        }
                        dictBean.setOperatesstr(oeratesb.toString());
                        //for(int h=0;h<operatesarray.length();h++){
//                        operateslist.add(operatesarray.get(h).toString());
//                    }
//                    dictBean.setOperates(operateslist);
                    }
                    dictBean.setCbca(couponTypejson.optString("cbca"));
                    dictBean.setCbvsd(couponTypejson.optString("cbvsd"));
                    dictBean.setCbr(couponTypejson.optDouble("cbr"));
                    dictBean.setStatus(couponTypejson.optString("status"));
                    dictBean.setCbcf(couponTypejson.optString("cbcf"));
                    dictBean.setCbsnu(couponTypejson.optString("cbsnu"));
                    dictBean.setCbved(couponTypejson.optString("cbved"));
                    dictBean.setCbid(couponTypejson.optString("cbid"));
                    dictBean.setCbi(couponTypejson.optString("cbi"));
                    dictBean.setCbtid(couponTypejson.optString("cbtid"));
                    dictBean.setCbn(couponTypejson.optString("cbn"));
                    dictBean.setTname(couponTypejson.optString("tname"));
                    dictBean.setCbea(couponTypejson.optString("cbea"));
                    dictBean.setCbtt(couponTypejson.optString("cbtt"));
                    dictBean.setTcover(couponTypejson.optString("tcover"));
                    discountcouponses.add(dictBean);
                }
                exChangeDetail_Data.setDiscountcouponses(discountcouponses);
            }
        }

        //普通商品信息
        String productInfoDtostr = datastrjson.optString("productInfoDto");
        if (!TextUtils.isEmpty(productInfoDtostr)) {
            JSONObject productInfoDtostrjson = new JSONObject(productInfoDtostr);
            ProductInfoDto productInfoDto = new ProductInfoDto();
            //商品属性
            ArrayList<Pat> patlist = new ArrayList<Pat>();
            productInfoDto.setPattr(productInfoDtostrjson.optString("pattr"));
            //商品名称
            productInfoDto.setPna(productInfoDtostrjson.optString("pna"));
            //商品售后须知
            productInfoDto.setPasa(productInfoDtostrjson.optString("pasa"));
            //商品编号
            productInfoDto.setPno(productInfoDtostrjson.optString("pno"));
            //商品品牌
            productInfoDto.setBn(productInfoDtostrjson.optString("bn"));
            //商品品类
            productInfoDto.setMon(productInfoDtostrjson.optString("mon"));
            //商品大图
            ArrayList<String> urllist = new ArrayList<String>();
            for (int i = 0; i < productInfoDtostrjson.getJSONArray("pa").length(); i++) {
                String pastr = (productInfoDtostrjson.getJSONArray("pa").get(i) + "");
                urllist.add(pastr);
            }
            productInfoDto.setUrllist(urllist);
            //商品优惠价
            productInfoDto.setPpr(productInfoDtostrjson.optString("ppr"));
            //商品原价
            productInfoDto.setPppr(productInfoDtostrjson.optString("pppr"));
            //商品详情
            productInfoDto.setPd(productInfoDtostrjson.optString("pd"));
            exChangeDetail_Data.setProductInfoDto(productInfoDto);
        }
        // 添加兑换商品信息
        String exchangeProductInfostr = datastrjson.optString("exchangeProductInfo");
        if (!TextUtils.isEmpty(exchangeProductInfostr)) {
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
            //商品编号
            exChangeBean.setPno(exchangeProductInfostrjson.optString("pno"));
            //商品品牌
            exChangeBean.setBn(exchangeProductInfostrjson.optString("bn"));
            //商品品类
            exChangeBean.setMon(exchangeProductInfostrjson.optString("mon"));

            //兑换类型
            exChangeBean.setPt(exchangeProductInfostrjson.optString("pt"));
            //服务器时间
            exChangeBean.setNte(exchangeProductInfostrjson.optLong("nte"));
            //竞拍结束时间
            exChangeBean.setEped(exchangeProductInfostrjson.optLong("eped"));
            //兑换开始时间：
            exChangeBean.setEpsd(exchangeProductInfostrjson.optLong("epsd"));
            //兑换状态 4兑换中，5已结束，6已兑换
            exChangeBean.setEps(exchangeProductInfostrjson.optString("eps"));
            //换购总量
            exChangeBean.setEnu(exchangeProductInfostrjson.optString("enu"));
            //商品id
            exChangeBean.setPid(exchangeProductInfostrjson.optString("pid"));
            //商品售后须知
            exChangeBean.setPasa(exchangeProductInfostrjson.optString("pasa"));
            //商品大图
            ArrayList<String> urllist = new ArrayList<String>();
            for (int i = 0; i < exchangeProductInfostrjson.getJSONArray("pa").length(); i++) {
                String pastr = (exchangeProductInfostrjson.getJSONArray("pa").get(i) + "");
                urllist.add(pastr);
            }
            exChangeBean.setUrllist(urllist);
            //商品优惠价
            exChangeBean.setPpr(exchangeProductInfostrjson.optString("ppr"));
            //商品原价
            exChangeBean.setPppr(exchangeProductInfostrjson.optString("pppr"));
            //商品描述
            exChangeBean.setPd(exchangeProductInfostrjson.optString("pd"));
            //剩余数
            exChangeBean.setElnu(exchangeProductInfostrjson.optString("elnu"));
            //2为限量 1不限量
            exChangeBean.setLimit(exchangeProductInfostrjson.optString("limit"));
            //2为限量 1不限量
            exChangeBean.setEpm(exchangeProductInfostrjson.optString("epm"));
            //限量的剩余数量
            exChangeBean.setEplnu(exchangeProductInfostrjson.optString("eplnu"));
            //每天可兑换的数量
            exChangeBean.setEpnu(exchangeProductInfostrjson.optString("epnu"));
            //兑换规则
            exChangeBean.setEpd(exchangeProductInfostrjson.optString("epd"));
            //所需币
            exChangeBean.setEpg(exchangeProductInfostrjson.optString("epg"));

            exChangeDetail_Data.setExChangeBean(exChangeBean);
        }

        return exChangeDetail_Data;
    }


    @Override
    public ArrayList<ShopProduct> get_exchange_listNew(String moid, String id, String nationCode, String page, String page_length) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("nationCode", nationCode);
        params.put("productType", "2");
        params.put("page", page);
        params.put("moid", moid);
        params.put("uid", id);
        params.put("page_length", page_length);
        // params.put("token", AppConfig.usertoken);

        String result = HttpConnUtil.doPost(AppConfig.EXCHANGE_LIST, params);
        PoitGoodBean change_Data = null;
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);
        change_Data = new PoitGoodBean();

//		if (!json.isNull("intg")) {
//			change_Data.setIntg(json.optString("intg") + "");
//		} else {
//			change_Data.setIntg("获取积分失败!");
//		}
        if (!json.isNull("data")) {
            ArrayList<ShopProduct> changeBeans = new ArrayList<ShopProduct>(
                    JSONArray.parseArray(json.optString("data"), ShopProduct.class));
            return changeBeans;
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<CategoryChooseBean> getShopAllCategory(String sid) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("sid", sid);
        params.put("tag", "1");
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.SHOPCATEGORY, params);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);
        if (TextUtils.isEmpty(json.optString("data"))) {
            return null;
        } else {
            //店铺品类
            if (!TextUtils.isEmpty(json.optString("data"))) {
                ArrayList<CategoryChooseBean> categorychoosbeans = new ArrayList<CategoryChooseBean>();
                if (json.optJSONArray("data").length() > 0) {
                    for (int i = 0; i < json.optJSONArray("data").length(); i++) {
                        JSONObject datajson = (JSONObject) json.optJSONArray("data").get(i);
                        CategoryChooseBean categoryChooseBean = new CategoryChooseBean();
                        categoryChooseBean.setMoid(datajson.optString("moid"));
                        categoryChooseBean.setName(datajson.optString("name"));
                        categoryChooseBean.setSid(sid);
                        if (!TextUtils.isEmpty(datajson.optString("children"))) {
                            org.json.JSONArray childrenarray = datajson.optJSONArray("children");
                            if (childrenarray.length() > 0) {
                                ArrayList<ChildernBean> childderlis = new ArrayList<ChildernBean>();
                                for (int h = 0; h < childrenarray.length(); h++) {
                                    ChildernBean childernBean = new ChildernBean();
                                    JSONObject childrenjson = (JSONObject) childrenarray.get(h);
                                    childernBean.setName(childrenjson.optString("name"));
                                    childernBean.setMoid(childrenjson.optString("moid"));
                                    childernBean.setIcon(childrenjson.optString("icon"));
                                    childernBean.setSid(sid);
                                    childernBean.setPid(childrenjson.optString("pid"));
                                    childderlis.add(childernBean);
                                }
                                categoryChooseBean.setChildderlis(childderlis);
                            }

                        }


                        categorychoosbeans.add(categoryChooseBean);

                    }


                    return categorychoosbeans;
                } else {
                    return null;
                }
            } else {
                return null;
            }


        }
    }

    @Override
    public ArrayList<CategoryChooseBean> getShopCategory(String sid) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("sid", sid);

        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.SHOPCATEGORY, params);
        ArrayList<CategoryChooseBean> categoryChooseBeens = new ArrayList<>();
        if (TextUtils.isEmpty(result)) {
            CategoryChooseBean categoryChooseBean = new CategoryChooseBean();
            categoryChooseBean.setName("全部分类");
            categoryChooseBeens.add(categoryChooseBean);
            return categoryChooseBeens;
        }
        JSONObject json = new JSONObject(result);

        if (TextUtils.isEmpty(json.optString("data"))) {
            CategoryChooseBean categoryChooseBean = new CategoryChooseBean();
            categoryChooseBean.setName("全部分类");
            categoryChooseBeens.add(categoryChooseBean);
            Log.v("ceshi", "***11");
            return categoryChooseBeens;
        } else {
            //店铺品类
            if (!TextUtils.isEmpty(json.optString("data"))) {
                Log.v("ceshi", "***22");
                if (json.optJSONArray("data").length() > 0) {
                    Log.v("ceshi", "***444");
                    categoryChooseBeens = new ArrayList<CategoryChooseBean>(
                            JSONArray.parseArray(json.optString("data"), CategoryChooseBean.class));

                    CategoryChooseBean categoryChooseBean = new CategoryChooseBean();
                    categoryChooseBean.setName("全部分类");
                    categoryChooseBeens.add(categoryChooseBean);
                    return categoryChooseBeens;
                } else {
                    Log.v("ceshi", "***5555");
                    CategoryChooseBean categoryChooseBean = new CategoryChooseBean();
                    categoryChooseBean.setName("全部分类");
                    categoryChooseBeens.add(categoryChooseBean);
                    return categoryChooseBeens;
                }
            } else {
                Log.v("ceshi", "***333");
                CategoryChooseBean categoryChooseBean = new CategoryChooseBean();
                categoryChooseBean.setName("全部分类");
                categoryChooseBeens.add(categoryChooseBean);
                return categoryChooseBeens;
            }
        }
    }

    @Override
    public ShopDetailNData getShopEnv(String sid) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("sid", sid);

        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.GETSHOPENV, params);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);
        if (TextUtils.isEmpty(json.optString("data"))) {
            return null;
        } else {
            ShopDetailNData shopDetailNData = new ShopDetailNData();
            ArrayList<String> shopenvs = new ArrayList<>();
            //店铺环境图片
            if (!TextUtils.isEmpty(json.optString("data"))) {
                if (json.optJSONArray("data").length() > 0) {
                    ArrayList<String> strings = new ArrayList<>();
                    for (int j = 0; j < json.optJSONArray("data").length(); j++) {
                        strings.add(json.getJSONArray("data").get(j) + "");
                    }
                    shopDetailNData.setShopenvs(strings);
                }
            }

            return shopDetailNData;
        }
    }

    @Override
    public ShopDetailNData getShopnProdcuts(String productType, String tid, String nationCode, String priceMode, String page, String moid) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("productType", productType);
        params.put("tid", tid);
        params.put("nationCode", nationCode);
        params.put("priceMode", priceMode);
        params.put("page", page);
        params.put("moid", moid);
        params.put("page_length", "10");

        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.GETSHOPNPRODUCT, params);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);
        if (TextUtils.isEmpty(json.optString("data"))) {
            return null;
        } else {
            ShopDetailNData shopDetailNData = new ShopDetailNData();
            //店铺基本信息
            shopDetailNData.setTotal(json.optString("total"));
            //店铺商品
            if (!TextUtils.isEmpty(json.optString("data"))) {
                if (json.optJSONArray("data").length() > 0) {
                    ArrayList<ShopProduct> shopProducts = new ArrayList<ShopProduct>(
                            JSONArray.parseArray(json.optString("data"), ShopProduct.class));
                    shopDetailNData.setShopProducts(shopProducts);
                } else {
                    return null;
                }
            }


            return shopDetailNData;
        }
    }

    public ShopDetailNData getShopDetailN_aty(String sid) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("shopId", sid);
        params.put("tag", "1");
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.GETSHOPDETAILNEW, params);
        Log.v("niao", "niao+" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);
        if (TextUtils.isEmpty(json.optString("data"))) {
            return null;
        } else {
            ShopDetailNData shopDetailNData = new ShopDetailNData();
            JSONObject datason = new JSONObject(json.optString("data"));
            //店铺基本信息
            shopDetailNData.setSid(datason.optString("sid"));
            shopDetailNData.setName(datason.optString("name"));
            shopDetailNData.setCover(datason.optString("cover"));
            shopDetailNData.setSign(datason.optString("sign"));

            shopDetailNData.setRelation(datason.optString("relation"));
            shopDetailNData.setOpen_time(datason.optString("open_time"));
            shopDetailNData.setContact(datason.optString("contact"));
            shopDetailNData.setAddress(datason.optString("address"));
            shopDetailNData.setDescription(datason.optString("description"));
            shopDetailNData.setContact(datason.optString("contact"));
            shopDetailNData.setMnti(datason.optString("mnti"));
            shopDetailNData.setMnid(datason.optString("mnid"));
            return shopDetailNData;
        }
    }

    @Override
    public ShopDetailNData getShopDetailN(String sid) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("shopId", sid);
        params.put("tag", "1");
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.GETSHOPDETAILNEW, params);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);
        if (TextUtils.isEmpty(json.optString("data"))) {
            return null;
        } else {
            ShopDetailNData shopDetailNData = new ShopDetailNData();
            JSONObject datason = new JSONObject(json.optString("data"));
            //店铺基本信息
            shopDetailNData.setSid(datason.optString("sid"));
            shopDetailNData.setName(datason.optString("name"));
            shopDetailNData.setCover(datason.optString("cover"));
            shopDetailNData.setAddress(datason.optString("address"));
            shopDetailNData.setOpen_time(datason.optString("open_time"));
            shopDetailNData.setContact(datason.optString("contact"));
            shopDetailNData.setDescription(datason.optString("description"));
            shopDetailNData.setContact(datason.optString("contact"));
            //店铺坐标
            PointBean pointBean = new PointBean();
            String posintstr = datason.optString("point");
            JSONObject pointjson = new JSONObject(posintstr);
            pointBean.setLat(pointjson.optDouble("lat"));
            pointBean.setLgt(pointjson.optDouble("lgt"));
            shopDetailNData.setPointBean(pointBean);

            //活动
            if (!TextUtils.isEmpty(datason.optString("news"))) {
                JSONObject newsjson = new JSONObject(datason.optString("news"));
                shopDetailNData.setMnti(newsjson.optString("mnti"));
                shopDetailNData.setMnid(newsjson.optString("mnid"));
            }
            //店铺优惠券
            if (!TextUtils.isEmpty(datason.optString("coupons"))) {
                if (datason.optJSONArray("coupons").length() > 0) {
                    ArrayList<Discountcoupons> discountcouponses = new ArrayList<Discountcoupons>(
                            JSONArray.parseArray(datason.optString("coupons"), Discountcoupons.class));
                    shopDetailNData.setDiscountcouponses(discountcouponses);
                }
            }
            //店铺商品
            if (!TextUtils.isEmpty(datason.optString("products"))) {
                if (datason.optJSONArray("products").length() > 0) {
                    ArrayList<ShopProduct> shopProducts = new ArrayList<ShopProduct>(
                            JSONArray.parseArray(datason.optString("products"), ShopProduct.class));
                    shopDetailNData.setShopProducts(shopProducts);
                }
            }
            //店铺评论
            if (!TextUtils.isEmpty(datason.optString("comment"))) {
                if (datason.optJSONArray("comment").length() > 0) {
                    ArrayList<ShopComment> shopComments = new ArrayList<>();
                    for (int i = 0; i < datason.optJSONArray("comment").length(); i++) {
                        JSONObject commentjson = (JSONObject) datason.getJSONArray("comment").get(i);
                        ShopComment shopComment = new ShopComment();
                        shopComment.setUid(commentjson.optString("uid"));
                        if (commentjson != null) {
                            if (!TextUtils.isEmpty(commentjson.optString("userinfo"))) {
                                JSONObject userinfojson = new JSONObject(commentjson.optString("userinfo"));
                                shopComment.setAvatar(userinfojson.optString("avatar"));
                                shopComment.setNick(userinfojson.optString("nick"));
                            }
                            if (!TextUtils.isEmpty(commentjson.optString("imgs"))) {
                                if (commentjson.optJSONArray("imgs").length() > 0) {
                                    ArrayList<String> strings = new ArrayList<>();

                                    for (int j = 0; j < commentjson.optJSONArray("imgs").length(); j++) {
                                        strings.add(commentjson.getJSONArray("imgs").get(j) + "");
                                    }
                                    shopComment.setStrings(strings);
                                }
                            }

                        }
                        shopComment.setCreate_time(commentjson.optString("create_time"));
                        shopComment.setText(commentjson.optString("text"));
                        shopComment.setType(commentjson.optString("type"));
                        shopComments.add(shopComment);
                    }
                    shopDetailNData.setShopComments(shopComments);
                }
            }
            //商家联盟
            if (!TextUtils.isEmpty(datason.optString("alliance"))) {
                if (datason.optJSONArray("alliance").length() > 0) {
                    ArrayList<ShopInfoBean> shopinfoDatas = new ArrayList<ShopInfoBean>(
                            JSONArray.parseArray(datason.optString("alliance"), ShopInfoBean.class));
                    shopDetailNData.setShopinfoDatas(shopinfoDatas);
                }
            }
            return shopDetailNData;
        }
    }


    SharedPreferences mySharedPreferences;

    @Override
    public BaseInfo register_by_device(String ip, DeviceInfo deviceinfo) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("device_info", JSON.toJSON(deviceinfo).toString());
        try {
            params.put("sign", Tool.getSignature(params));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = HttpConnUtil.doPost(AppConfig.REGISTER_BY_DEVICE, params);
        Log.v("ceshi", "result" + result);
        String msg = ResolveResponse.resolveData(result).toString();
        JSONObject json = new JSONObject(msg);
        BaseInfo baseInfo = null;
        if (!json.isNull("cookie")) {
            baseInfo = JSON.parseObject(json.optString("cookie"), BaseInfo.class);
            return baseInfo;
        } else {
            return null;
        }

    }

    @Override
    public Boolean sendsms(String mobile, int check, String devicetoken, String deviceuid) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("mobile", mobile);
        params.put("check", check + "");
        try {
            params.put("sign", Tool.getSignature(params));
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", devicetoken);
        params.put("uid", deviceuid);
        String result = HttpConnUtil.doPost(AppConfig.SENDSMS, params);
        Log.v("ceshi1", "result" + result);
        String msg = ResolveResponse.resolveData(result).toString();
        Log.v("ceshi", "msg" + msg);
        if (msg.contains("true")) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public Boolean verifysmscode(String mobile, String smscode) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("mobile", mobile);
        params.put("channel", AppConfig.channel);
        params.put("smscode", smscode);
        try {
            params.put("sign", Tool.getSignature(params));
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.VERIFYSMSCODE, params);
        Log.v("ceshi", "result" + result);
        String msg = ResolveResponse.resolveData(result).toString();

        JSONObject jsonObject = new JSONObject(result);
        Boolean data = Boolean.valueOf(jsonObject.optString("data"));
        Log.v("ce", data + "");

        return data;
    }

    @Override
    public UserDataBean register(UserDataBean userInfo) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("mobile", userInfo.getMobile());
        params.put("password", Tool.MD5(userInfo.getPassword()));
        params.put("smscode", userInfo.getSmscode());
        params.put("nationCode", userInfo.getNationCode());
        params.put("login_name", userInfo.getLogin_name());
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", userInfo.getDevicetoken());
        params.put("uid", userInfo.getDeviceuid());
        String result = HttpConnUtil.doPost(AppConfig.REGISTER, params);
//		JSONObject jsonresult = new JSONObject(result);
        Log.v("ceshi", result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        String msg = ResolveResponse.resolveData(result).toString();
        UserDataBean userInfo2 = null;
        if (TextUtils.isEmpty(msg)) {
            return null;
        }
        userInfo2 = JSON.parseObject(msg, UserDataBean.class);
        return userInfo2;
    }

    @Override
    public UserDataBean login(UserDataBean userInfo) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        // params.put("mobile", userInfo.getMobile());
        params.put("login_name", userInfo.getLogin_name());
        params.put("password", Tool.MD5(userInfo.getPassword()));
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", userInfo.getDevicetoken());
        params.put("uid", userInfo.getDeviceuid());
        String result = HttpConnUtil.doPost(AppConfig.LOGIN, params);
        UserDataBean userInfo2 = null;
        Log.v("ceshi", "result" + result);
        String msg = ResolveResponse.resolveData(result).toString();

        //返回整个json
        userInfo2 = JSON.parseObject(msg, UserDataBean.class);
        return userInfo2;
        //	JSONObject json = new JSONObject(msg);
//		if (!json.isNull("cookie")) {
//			userInfo2 = JSON.parseObject(json.optString("cookie"), UserDataBean.class);
//			return userInfo2;
//		} else {
//			return null;
//		}
    }

    @Override
    public Boolean resetpwd(String mobile, String old_pwd, String new_pwd, String smscode, int typecode) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        Log.v("zzrtype", typecode + "");
        params.put("timestamp", Tool.getUninxTime());
        params.put("channel", AppConfig.channel);
        params.put("new_pwd", Tool.MD5(new_pwd));

        if (typecode == 1) {
            params.put("smscode", smscode);
            params.put("mobile", mobile);
            params.put("old_pwd", "");
        } else {
            params.put("old_pwd", Tool.MD5(old_pwd));
            params.put("new_pwd", Tool.MD5(new_pwd));
        }
        params.put("ip", AppConfig.ipstr);
        try {
            params.put("sign", Tool.getSignature(params));
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.RESETPWD, params);
        Log.v("lsq", "result+" + result);
        JSONObject jsonObject = new JSONObject(result);
        String ms = jsonObject.optString("e");
        JSONObject meJson = new JSONObject(ms);
        int code = meJson.getInt("code");
        Log.v("zzr", ms);
        Log.v("zzr1", code + "");
        if (code == 0) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public UserDataBean keepalive(String situation, String password) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("situation", situation);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.KEEPALIVE, params);
        Log.v("ceshi", "result" + result);
        String msg = ResolveResponse.resolveData(result).toString();
        return null;
    }

    @Override
    public UserDataBean checktoken() throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.CHECKTOKEN, params);
        Log.v("ceshi", "result" + result);
        String msg = ResolveResponse.resolveData(result).toString();
        return null;
    }

    @Override
    public UserDataBean login_sms(UserDataBean userInfo) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("mobile", userInfo.getMobile());
        params.put("smscode", userInfo.getSmscode());
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", userInfo.getDevicetoken());
        params.put("uid", userInfo.getDeviceuid());
        String result = HttpConnUtil.doPost(AppConfig.LOGIN_SMS, params);
        String msg = ResolveResponse.resolveData(result).toString();
        UserDataBean userInfo2 = null;
        JSONObject json = new JSONObject(msg);
        if (!json.isNull("cookie")) {
            userInfo2 = JSON.parseObject(json.optString("cookie"), UserDataBean.class);
            AppConfig.userid = userInfo2.getUid();
            AppConfig.usertoken = userInfo2.getToken();

        } else {

        }
        userInfo2 = JSON.parseObject(msg, UserDataBean.class);

        return userInfo2;
    }

    @Override
    public UserDataBean login_third(String access_token, String type, String openid, String app_version, String devicetoken, String deviceuid)
            throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("access_token", access_token);
        params.put("type", type);
        params.put("openid", openid);
        params.put("channel", Constants.CHANNEL);
        params.put("app_version", app_version);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", devicetoken);
        params.put("uid", deviceuid);
        String result = HttpConnUtil.doPost(AppConfig.LOGIN_THIRD, params);
        Log.v("ceshi", "result" + result);
        String msg = ResolveResponse.resolveData(result).toString();
        JSONObject json = new JSONObject(msg);
        UserDataBean userInfo2 = null;
        if (!json.isNull("cookie")) {
            String a = json.optString("cookie");
            userInfo2 = JSON.parseObject(json.optString("cookie"), UserDataBean.class);
            return userInfo2;
        } else {
            return null;
        }
    }

    @Override
    public List<Adimg> queryRecommendMerchant(String nationCode) throws WSError, JSONException {
        // SY_MR_SHOP_COMMON 推荐的店铺标识
        // SY_MR_SHOP_SERVICE 推荐服务的标识
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("merchantRecommendTag", "SY_MR_MERCHANT_BANNER");
        params.put("nationCode", nationCode);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.QUERYRECOMMENDMERCHANT, params);
        Log.v("ceshi", "resultIMG+" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
//        String msg = ResolveResponse.resolveData(result).toString();
        JSONObject json = new JSONObject(result);
        if (TextUtils.isEmpty(json.getJSONArray("data").toString())) {
            return null;
        }
        Log.v("test", "lengtht+" + json.getJSONArray("data").length());
        if (json.getJSONArray("data").length() == 1) {
            ArrayList<Adimg> list = new ArrayList<Adimg>(
                    JSONArray.parseArray(json.getJSONArray("data").getJSONObject(0).optString("SY_MR_MERCHANT_BANNER"), Adimg.class));
            return list;
        } else if (json.getJSONArray("data").length() == 2) {
            Log.v("test", "tiaojianeee222+");
            ArrayList<Adimg> list = new ArrayList<Adimg>(
                    JSONArray.parseArray(json.getJSONArray("data").getJSONObject(1).optString("SY_MR_AD_BANNER"), Adimg.class));
            Log.v("test", "tiaojianeee222+" + list.size());
            return list;
        } else {
            return null;
        }
//        JSONObject proshopjson = json.getJSONArray("data").getJSONObject(1);
//        Log.v("ceshi", "@@@111111111111+" + json.isNull("SY_MR_MERCHANT_BANNER"));
//        Log.v("test", "json.isNull(\"SY_MR_AD_BANNER\")"+json.isNull("SY_MR_AD_BANNER"));
//        if (!json.isNull("SY_MR_MERCHANT_BANNER")) {
//            ArrayList<Adimg> list = new ArrayList<Adimg>(
//                    JSONArray.parseArray(json.optString("SY_MR_MERCHANT_BANNER"), Adimg.class));
//            return list;
//        } else if (!json.isNull("SY_MR_AD_BANNER")) {
//            ArrayList<Adimg> list = new ArrayList<Adimg>(
//                    JSONArray.parseArray(json.optString("SY_MR_AD_BANNER"), Adimg.class));
//            Log.v("ceshi","size+++"+list.size());
//            return list;
//        } else {
//            return null;
//        }
    }

    @Override
    public String actCodeone(String userSign, String uactCode) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("userSign", userSign);
        params.put("uactCode", uactCode);
        params.put("facKn", MD5(userSign + "1_a2_b3"));
        String result = HttpConnUtil.doPost(AppConfig.ACTCODE_ONE, params);
        Log.v("ceshi", "result" + result);
        // String msg = ResolveResponse.resolveData(result).toString();//
        // �������ص����
        return result;
    }

    public static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String actCodetwo(String userSign, String uactCode, String facKn) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("userSign", userSign);
        params.put("uactCode", uactCode);
        params.put("facKn", MD5(userSign + "1_a2_b3"));
        String result = HttpConnUtil.doPost(AppConfig.ACTCODE_TWO, params);
        Log.v("ceshi", "result" + result);
        // String msg = ResolveResponse.resolveData(result).toString();//
        // �������ص����
        return result;
    }

    @Override
    public String verison_ar1(String userSign, String verSign, String verVsion) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("userSign", userSign);
        params.put("verSign", verSign);
        params.put("verVsion", verVsion);

        params.put("facKn", MD5(userSign + "1_a2_b3"));
        String result = HttpConnUtil.doPost(AppConfig.APPVERSION_AR1, params);
        Log.v("ceshi", "result" + result);
        // String msg = ResolveResponse.resolveData(result).toString();//
        // �������ص����
        return result;
    }


    @Override
    public String videodown(String userSign, String couKeyID, String couVsion) throws WSError, JSONException {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("userSign", userSign);
        params.put("couKeyID", couKeyID);
        params.put("couVsion", couVsion);

        params.put("facKn", MD5(userSign + "1_a2_b3"));
        String result = HttpConnUtil.doPost(AppConfig.VIDEON_DOWN, params);
        Log.v("ceshi", "result" + result);
        // String msg = ResolveResponse.resolveData(result).toString();//
        // �������ص����
        return result;

    }

    @Override
    public StoreDetailInfo getMerchantDetail(String MerchantId) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("merchantId", MerchantId);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.GETMERCHANTDETAIL, params);
        Log.v("ceshi", "result" + result);
        String msg = ResolveResponse.resolveDesc(result).toString();
        if (msg.equals("failed")) {
            return null;
        } else {
            StoreDetailInfo storeDetailInfo = new StoreDetailInfo();
            JSONObject json = new JSONObject(msg);
            storeDetailInfo.setTotal(json.optString("total"));
            Merchant merchant = null;
            merchant = JSON.parseObject(json.optString("merchant"), Merchant.class);
            ArrayList<StoreDetailMomInfo> telinfos = new ArrayList<StoreDetailMomInfo>();
            StoredetailMllInfo gpsinfo = new StoredetailMllInfo();
            ArrayList<StoredetailMaInfo> urlinfos = new ArrayList<StoredetailMaInfo>();
            String merchantstr = json.optString("merchant");
            JSONObject merchantjson = new JSONObject(merchantstr);
            for (int i = 0; i < merchantjson.optJSONArray("mom").length(); i++) {
                StoreDetailMomInfo detailMomInfo = new StoreDetailMomInfo();
                detailMomInfo.setTelstr(merchantjson.optJSONArray("mom").get(i) + "");
                telinfos.add(detailMomInfo);

            }
            merchant.setTelinfos(telinfos);

            gpsinfo.setGpsx(merchantjson.getJSONObject("mll").optDouble("x"));
            gpsinfo.setGpsy(merchantjson.getJSONObject("mll").optDouble("y"));
            merchant.setGpsinfos(gpsinfo);
            for (int i = 0; i < merchantjson.getJSONArray("ma").length(); i++) {
                StoredetailMaInfo storedetailMaInfo = new StoredetailMaInfo();
                storedetailMaInfo.setUrl(merchantjson.optJSONArray("ma").get(i) + "");
                urlinfos.add(storedetailMaInfo);

            }
            merchant.setUrlinfos(urlinfos);
            storeDetailInfo.setMerchant(merchant);
            if (!TextUtils.isEmpty(json.optString("new"))) {
                NewsBeean newsbean = JSON.parseObject(json.optString("new"), NewsBeean.class);
                storeDetailInfo.setNewsBeean(newsbean);
            }
            ArrayList<FloorBean> floorBeans = new ArrayList<FloorBean>(
                    JSONArray.parseArray(json.optString("floor"), FloorBean.class));
            storeDetailInfo.setFloorBeans(floorBeans);
            return storeDetailInfo;
            // Log.v("ceshi", "2222222");
            // Log.v("ceshi","@@@111111111111+"+json.isNull("SY_MR_MERCHANT_BANNER"));
            // if (!json.isNull("SY_MR_MERCHANT_BANNER")) {
            // ArrayList<Adimg> list = new
            // ArrayList<Adimg>(JSONArray.parseArray(json.optString("SY_MR_MERCHANT_BANNER"),
            // Adimg.class));
            // return list;
            // } else {
            // return null;
            // }
        }
        // "total": 1, 该商场的所有商铺数量
        // "floor": [
        // {
        // "count": 1, 当前层商铺数量
        // "floorInfo": {
        // "mfln": 1, 层数
        // "mfn": "化妆品", 每层的类型
        // "mfltt": "F", 楼层符号
        // "mflid": 1000009 楼层代码
        // }
        // }
        // ],

        // "merchant": {
        // "mn": "测试商户1", //商场名字
        // "mll": {
        // "y": 39.953586, //商场坐标
        // "x": 116.368003
        // },
        // "md": "测试商户描述", //商场描述
        // "ma": [ //商场滚动大图
        // "http://img.slw01.com/images/aa.png"
        // ],
        // "madd": "1", //商场地址
        // "mid": 1000002, //商场id
        // "mdbt": "测试营业时间",//营业时间
        // "mom": [
        // "11223344", //商场电话
        // "11224455"
        // ]
        // },
        // "activity": [ //商场促销活动
        // {
        // "mai": "测试商户1的活动1详情描述",
        // "maid": 1000003
        // }
        // ]
        // },
    }

    @Override
    public ArrayList<PoitGoodBean> get_exchange_list(String moid, String id, String nationCode, String page, String page_length) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("nationCode", nationCode);
        params.put("productType", "2");
        params.put("page", page);
        params.put("moid", moid);
        params.put("uid", id);
        params.put("page_length", page_length);
        // params.put("token", AppConfig.usertoken);

        String result = HttpConnUtil.doPost(AppConfig.EXCHANGE_LIST, params);
        PoitGoodBean change_Data = null;
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);
        change_Data = new PoitGoodBean();

//		if (!json.isNull("intg")) {
//			change_Data.setIntg(json.optString("intg") + "");
//		} else {
//			change_Data.setIntg("获取积分失败!");
//		}
        if (!json.isNull("data")) {
            ArrayList<PoitGoodBean> changeBeans = new ArrayList<PoitGoodBean>(
                    JSONArray.parseArray(json.optString("data"), PoitGoodBean.class));
            return changeBeans;
        } else {
            return null;
        }

    }

    @Override
    public ActivityDetailBean getNewById(String newId, String lon, String lat) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("newId", newId);
        params.put("lgt", lon);
        params.put("lat", lat);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.ACTIVITY_DETIAL, params);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        ActivityDetailBean activityDetailBean = null;
        JSONObject json = new JSONObject(result);
        activityDetailBean = JSON.parseObject(json.optString("data"), ActivityDetailBean.class);
        PointBean pointBean = new PointBean();
        JSONObject datajson = new JSONObject(json.optString("data"));
        String posintstr = datajson.optString("point");
        JSONObject pointjson = new JSONObject(posintstr);
        pointBean.setLat(pointjson.optDouble("lat"));
        pointBean.setLgt(pointjson.optDouble("lgt"));
        activityDetailBean.setPointBean(pointBean);
        return activityDetailBean;

    }

    @Override
    public HomeInfo queryRecommendShop(String nationCode) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
//        生活乐悠悠	RC_SHOP_HAPPY
//        爱美不用愁	RC_SHOP_BEAUTI
//        折扣超优惠	RC_SHOP_REBATE
//        推荐好店	RC_SHOP_GOODS 分页
        params.put("timestamp", Tool.getUninxTime());
        params.put("channel", AppConfig.channel);
        params.put("ip", AppConfig.ipstr); // SY_MR_SHOP_FAVORABLE
        // params.put("shopRecommendTag", "SY_MR_PROSHOP_ICON,SY_MR_SERSHOP_ICON ");// SY_MR_PROSHOP_ICON,SY_MR_SERSHOP_ICON
        params.put("shopRecommendTag", "RC_SHOP_HAPPY,RC_SHOP_BEAUTI,RC_SHOP_REBATE");// SY_MR_PROSHOP_ICON,SY_MR_SERSHOP_ICON

        params.put("nationCode", nationCode);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        //
        String result = HttpConnUtil.doPost(AppConfig.HOMECOMMDEDLIST, params);

        // String result = HttpConnUtil.doPost(AppConfig.QUERYRECOMMENDSHOP, params);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject alljson = new JSONObject(result);
        if (alljson.optJSONArray("data") == null) {
            return null;
        }
        JSONObject proshopjson = null;
        if (alljson.optJSONArray("data").length() > 0) {
            proshopjson = alljson.optJSONArray("data").getJSONObject(0);
        }
        ArrayList<ShopDetailInfo> shopProInfos = new ArrayList<>();
        if (proshopjson != null) {
            if (!TextUtils.isEmpty(proshopjson.optString("RC_SHOP_HAPPY"))) {
                if (proshopjson.optJSONArray("RC_SHOP_HAPPY").length() > 0) {
                    shopProInfos = new ArrayList<ShopDetailInfo>(
                            JSONArray.parseArray(proshopjson.optString("RC_SHOP_HAPPY"), ShopDetailInfo.class));
                }
            }
        }
        JSONObject sershopjson = null;
        if (alljson.optJSONArray("data").length() > 1) {
            sershopjson = alljson.optJSONArray("data").getJSONObject(1);
        }
        ArrayList<ShopDetailInfo> shopSerInfos = new ArrayList<>();
        if (sershopjson != null) {
            if (!TextUtils.isEmpty(sershopjson.optString("RC_SHOP_BEAUTI"))) {
                if (sershopjson.optJSONArray("RC_SHOP_BEAUTI").length() > 0) {
                    shopSerInfos = new ArrayList<ShopDetailInfo>(
                            JSONArray.parseArray(sershopjson.optString("RC_SHOP_BEAUTI"), ShopDetailInfo.class));

                }
            }
        }
        JSONObject shopRebatejson = null;
        if (alljson.optJSONArray("data").length() > 2) {
            shopRebatejson = alljson.optJSONArray("data").getJSONObject(2);
        }
        ArrayList<ShopDetailInfo> shopRebates = new ArrayList<>();
        if (shopRebatejson != null) {
            if (!TextUtils.isEmpty(shopRebatejson.optString("RC_SHOP_REBATE"))) {
                if (shopRebatejson.optJSONArray("RC_SHOP_REBATE").length() > 0) {
                    shopRebates = new ArrayList<ShopDetailInfo>(
                            JSONArray.parseArray(shopRebatejson.optString("RC_SHOP_REBATE"), ShopDetailInfo.class));
                }
            }
        }

        //JSONObject shopGoosjson = alljson.getJSONArray("data").getJSONObject(3);
        //ArrayList<ShopDetailInfo> shopGoos = new ArrayList<ShopDetailInfo>(
//                JSONArray.parseArray(shopGoosjson.optString("RC_SHOP_GOODS"), ShopDetailInfo.class));
        HomeInfo homeInfo = new HomeInfo();
        homeInfo.setShopProInfos(shopProInfos);
        homeInfo.setShopSerInfos(shopSerInfos);
        homeInfo.setShopRebate(shopRebates);
        //   homeInfo.setShopGoos(shopGoos);
        return homeInfo;
    }

    @Override
    public CostHistorData get_pay_list(String is_comment, String max_id, String min_id, String count)
            throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("count", count);
        params.put("is_comment", is_comment);
        params.put("max_id", max_id + "");
        params.put("min_id", min_id + "");
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.PAD_LIST, params);

        if (TextUtils.isEmpty(result)) {
            return null;
        }
        Log.v("ceshi", "result" + result);
        // String msg = ResolveResponse.resolveData(result).toString();
        JSONObject json = new JSONObject(result);


        JSONObject datajsonnew = json.optJSONObject("data");
        org.json.JSONArray statjsonarray = datajsonnew.optJSONArray("stat");
        ArrayList<NumBean> nums = new ArrayList<>();
        if (statjsonarray != null & !"".equals(statjsonarray)) {
            for (int j = 0; j < statjsonarray.length(); j++) {
                JSONObject statjson = (JSONObject) statjsonarray.get(j);
                NumBean numBean = new NumBean();

                numBean.setType(statjson.optString("ict"));
                numBean.setCount(statjson.optString("count"));
                nums.add(numBean);
            }
        }
        org.json.JSONArray datajson = datajsonnew.optJSONArray("expenses");
        ArrayList<ConsumerSettlementrecords> constlist = new ArrayList<ConsumerSettlementrecords>();
        if (datajson != null) {
            if (datajson.length() > 0) {
                for (int i = 0; i < datajson.length(); i++) {
                    JSONObject csltjson = (JSONObject) datajson.get(i);
                    ConsumerSettlementrecords mreBean = new ConsumerSettlementrecords();
                    mreBean.setAmount(csltjson.optString("amount"));
                    mreBean.setSid(csltjson.optString("sid"));
                    mreBean.setIs_comment(csltjson.optString("is_comment"));
                    mreBean.setCreate_time(csltjson.optString("create_time"));
                    mreBean.setEid(csltjson.optString("eid"));
                    ArrayList<Pat> pats = new ArrayList<>();
                    if (csltjson.optString("categorys") != null & !"".equals(csltjson.optString("categorys"))) {
                        if (csltjson.optJSONArray("categorys").length() > 0) {
                            for (int g = 0; g < csltjson.getJSONArray("categorys").length(); g++) {
                                Pat pat = new Pat();
                                pat.setStr(csltjson.getJSONArray("categorys").get(g) + "");
                                pats.add(pat);
                            }
                            mreBean.setPats(pats);
                        }
                    }

                    if (csltjson.optString("shop_info") != null & !"".equals(csltjson.optString("shop_info"))) {
                        JSONObject shopInfojson = new JSONObject(csltjson.optString("shop_info"));
                        mreBean.setShop_info(shopInfojson.optString("name"));
                        mreBean.setLogo(shopInfojson.optString("logo"));
                    }
                    if (csltjson.optString("product") != null & !"".equals(csltjson.optString("product"))) {
                        JSONObject productjson = new JSONObject(csltjson.optString("product"));
                        mreBean.setS_pna(productjson.optString("pna"));
                        mreBean.setS_ppr(productjson.optString("ppr"));
                        mreBean.setS_epp(productjson.optString("epp"));
                        mreBean.setS_ppi(productjson.optString("ppi"));

                        if (productjson.optString("pat") != null & !"".equals(productjson.optString("pat"))) {
                            if (productjson.optJSONArray("pat").length() > 0) {
                                StringBuffer sb = new StringBuffer();
                                for (int g = 0; g < productjson.getJSONArray("pat").length(); g++) {
                                    sb.append(productjson.getJSONArray("pat").get(g) + "");
                                }
                                mreBean.setS_pat(sb.toString());
                            }
                        }

                    }

                    constlist.add(mreBean);
                }
            }
        }

        CostHistorData commnetdata = new CostHistorData();
        commnetdata.setArrayList(constlist);
        commnetdata.setNumbeans(nums);
        if (!TextUtils.isEmpty(json.optJSONObject("data").toString())) {
            commnetdata.setMax_id(json.optJSONObject("data").optString("max_id"));
            commnetdata.setMin_id(json.optJSONObject("data").optString("min_id"));
        }

        String totalstr = json.optString("total");
        commnetdata.setTotal(totalstr);
        return commnetdata;
    }

    @Override
    public PointsData get_My_points(String max_id, String min_id, int count, String start, String end)
            throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("max_id", max_id + "");
        params.put("min_id", min_id + "");
        params.put("start", start + "");
        params.put("end", end + "");
        params.put("uid", AppConfig.userid);
        params.put("count", count + "");
        String result = HttpConnUtil.doPost(AppConfig.POINTS, params);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);
        org.json.JSONArray datajson = json.getJSONObject("data").getJSONArray("intgs");
        ArrayList<PointsEntity> Pointlist = new ArrayList<PointsEntity>();
        for (int i = 0; i < datajson.length(); i++) {
            JSONObject csltjson = (JSONObject) datajson.get(i);
            PointsEntity mreBean = new PointsEntity();
            mreBean.setUid(csltjson.optString("uid"));
            mreBean.setIntg(csltjson.optString("intg"));
            mreBean.setCreate_time(csltjson.optString("create_time"));
            mreBean.setIntg_id(csltjson.optString("intg_id"));
            mreBean.setType(csltjson.optString("type"));
            Pointlist.add(mreBean);
        }
        PointsData pointsData = new PointsData();
        pointsData.setPointsEntities(Pointlist);
        pointsData.setTotal(json.getJSONObject("data").optString("total"));
        pointsData.setMin_id(json.getJSONObject("data").optString("min_id"));
        pointsData.setMax_id(json.getJSONObject("data").optString("max_id"));
        return pointsData;
    }

    @Override
    public ArrayList<MerchanRreplyEntity> get_custservicelist(String st, int page, int page_length) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("st", st);
        params.put("page", page + "");
        params.put("page_length", page_length + "");
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.GET_CUSTSERVICE_LISTA, params);
        Log.v("ceshi", "result+" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);
        org.json.JSONArray datajson = json.getJSONArray("data");
        if (TextUtils.isEmpty(json.optString("data"))) {
            return null;
        }
        if (!TextUtils.isEmpty(json.optString("data"))) {
            if (json.optJSONArray("data").length() == 0) {
                return null;
            }
        }
        ArrayList<MerchanRreplyEntity> MreBeanlist = new ArrayList<MerchanRreplyEntity>();
        for (int i = 0; i < datajson.length(); i++) {
            JSONObject shopInfojson = (JSONObject) datajson.get(i);
            MerchanRreplyEntity mreBean = new MerchanRreplyEntity();
            mreBean.setUid(shopInfojson.optString("uid"));
            mreBean.setTn(shopInfojson.optString("tn"));
            mreBean.setDesc(shopInfojson.optString("desc"));
            mreBean.setMc(shopInfojson.optString("mc"));
            mreBean.setTp(shopInfojson.optString("tp"));
            org.json.JSONArray iurlsjson = shopInfojson.getJSONArray("iurls");
            ArrayList<ImgeUrl> imgurls = new ArrayList<ImgeUrl>();
            for (int j = 0; j < iurlsjson.length(); j++) {
                ImgeUrl iu = new ImgeUrl();
                iu.setUrl(iurlsjson.optString(j));
                imgurls.add(iu);
            }
            mreBean.setImgurls(imgurls);
            mreBean.setIurls(shopInfojson.optString("iurls"));
            mreBean.setSt(shopInfojson.optString("st"));
            mreBean.setEt(shopInfojson.optString("et"));
            mreBean.setCid(shopInfojson.optString("cid"));
            mreBean.setRc(shopInfojson.optString("rc"));
            mreBean.setCt(shopInfojson.optString("ct"));
            MreBeanlist.add(mreBean);
        }
        return MreBeanlist;
    }

    @Override
    public CustomizedReply get_custservice_content(String custServiceId) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("custServiceId", custServiceId);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.GET_CUSTSERVICE_CONTENT, params);
        Log.v("ceshi", "result" + result);
        JSONObject json = new JSONObject(result);
        CustomizedReply CustomizedBean = new CustomizedReply();
        JSONObject datajson = json.getJSONObject("data");
        String couponBatchstr = datajson.optString("csDto");
        JSONObject couponBatchjson = new JSONObject(couponBatchstr);
        CustomizedBean.setUid(couponBatchjson.optString("uid"));
        CustomizedBean.setTn(couponBatchjson.optString("tn"));
        CustomizedBean.setMc(couponBatchjson.optString("mc"));
        CustomizedBean.setTp(couponBatchjson.optString("tp"));
        org.json.JSONArray iurlsjson = couponBatchjson.getJSONArray("iurls");
        ArrayList<String> usergoodimgs = new ArrayList<>();
        for (int h = 0; h < iurlsjson.length(); h++) {
            String url = iurlsjson.optString(h);
            usergoodimgs.add(url);
        }
        CustomizedBean.setUsergoodimgs(usergoodimgs);
        CustomizedBean.setIurls(couponBatchjson.optString("iurls"));
        CustomizedBean.setSt(couponBatchjson.optString("st"));
        CustomizedBean.setEt(couponBatchjson.optString("et"));
        CustomizedBean.setCid(couponBatchjson.optString("cid"));
        CustomizedBean.setRc(couponBatchjson.optString("rc"));
        CustomizedBean.setCt(couponBatchjson.optString("ct"));
        ArrayList<CurlistEntity> curlist = new ArrayList<CurlistEntity>();
        for (int i = 0; i < datajson.getJSONArray("csrlist").length(); i++) {
            CurlistEntity curBean = new CurlistEntity();
            JSONObject csrjson = (JSONObject) datajson.getJSONArray("csrlist").get(i);
            curBean.setId(csrjson.optString("id"));
            curBean.setSid(csrjson.optString("sid"));
            curBean.setSn(csrjson.optString("sn"));
            org.json.JSONArray urlsjsonarray = csrjson.getJSONArray("urls");
            ArrayList<String> huifugoodimgs = new ArrayList<>();
            for (int n = 0; n < urlsjsonarray.length(); n++) {
                String url = urlsjsonarray.optString(n);
                huifugoodimgs.add(url);
            }
            curBean.setHuifugoodimgs(huifugoodimgs);
            curBean.setUrls(csrjson.optString("urls"));
            curBean.setScov(csrjson.optString("scov"));
            curBean.setRt(csrjson.optString("rt"));
            curBean.setSms(csrjson.optString("sms"));
            curBean.setCid(csrjson.optString("cid"));
            curBean.setMst(csrjson.optString("mst"));
            curBean.setRc(csrjson.optString("rc"));

            curlist.add(curBean);
        }
        CustomizedBean.setArrayList(curlist);
        return CustomizedBean;
    }


    @Override
    public MyLikeListData getMyLikeslist(int page, int page_length) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("page", Integer.toString(page));
        params.put("page_length", Integer.toString(page_length));
        try {
            params.put("sign", Tool.getSignature(params));
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.MYLIKES, params);
        Log.v("ceshi", "result" + result);

        if (TextUtils.isEmpty(result)) {
            return null;
        }

        // String msg = ResolveResponse.resolveData(result).toString();
        JSONObject json = new JSONObject(result);
        String totalnumstr = json.optString("total");

        org.json.JSONArray datajson = json.getJSONArray("data");
        ArrayList<MyLikesEntity> arLikesEntities = new ArrayList<MyLikesEntity>();
        for (int i = 0; i < datajson.length(); i++) {
            MyLikesEntity myLikesEntity = new MyLikesEntity();
            JSONObject csltjson = (JSONObject) datajson.get(i);
            myLikesEntity.setInvite(csltjson.optString("invite"));
            myLikesEntity.setUid(csltjson.optString("uid"));
            myLikesEntity.setRelation_id(csltjson.optString("relation_id"));
            myLikesEntity.setRelation_id(csltjson.optString("relation_id"));
            myLikesEntity.setCreate_time(csltjson.optString("create_time"));

            String couponBatchstr = csltjson.optString("target_info");
            if (!TextUtils.isEmpty(couponBatchstr)) {
                JSONObject couponBatchjson = new JSONObject(couponBatchstr);
                myLikesEntity.setOpen_time(couponBatchjson.optString("open_time"));
                myLikesEntity.setSid(couponBatchjson.optString("sid"));
                myLikesEntity.setStag(couponBatchjson.optString("stag"));
                myLikesEntity.setDistance(couponBatchjson.optDouble("distance"));
                myLikesEntity.setCover(couponBatchjson.optString("cover"));
                myLikesEntity.setAddress(couponBatchjson.optString("address"));
                myLikesEntity.setNews(couponBatchjson.optString("news"));
                myLikesEntity.setProduct(couponBatchjson.optString("product"));
                myLikesEntity.setCoupon(couponBatchjson.optString("coupon"));
                myLikesEntity.setOperate(couponBatchjson.optString("operate"));
                myLikesEntity.setName(couponBatchjson.optString("name"));
                myLikesEntity.setGoods(couponBatchjson.optString("goods"));
                myLikesEntity.setContact(couponBatchjson.optString("contact"));
            }

            arLikesEntities.add(myLikesEntity);
        }
        MyLikeListData likeListData = new MyLikeListData();
        likeListData.setArLikesEntities(arLikesEntities);
        likeListData.setTotal(totalnumstr);
        return likeListData;
    }

    @Override
    public DiscountcouponData getPayDiscouncoupons(String cs, String page, String page_length)
            throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("cs", cs);
        params.put("page", page);
        params.put("page_length", page_length);

        try {
            params.put("sign", Tool.getSignature(params));
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.DISCOUNTCOUPONS, params);
        Log.v("ceshi", "result" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        // String msg = ResolveResponse.resolveData(result).toString();
        JSONObject json = new JSONObject(result);
        org.json.JSONArray datajson = json.optJSONArray("data");
        org.json.JSONArray statjsonarray = json.optJSONArray("stat");
        ArrayList<NumBean> nums = new ArrayList<>();
        Log.v("ceshi", "statjsonarray+" + statjsonarray.length());
        if (statjsonarray != null & !"".equals(statjsonarray)) {
            for (int j = 0; j < statjsonarray.length(); j++) {
                JSONObject statjson = (JSONObject) statjsonarray.get(j);
                NumBean numBean = new NumBean();
                numBean.setType(statjson.optString("cs"));
                numBean.setCount(statjson.optString("count"));
                nums.add(numBean);
            }
        }
        ArrayList<Discountcoupons> dictlist = new ArrayList<Discountcoupons>();
        if (datajson != null & !"".equals(datajson)) {
            for (int i = 0; i < datajson.length(); i++) {
                Discountcoupons DictBean = new Discountcoupons();
                JSONObject csltjson = (JSONObject) datajson.get(i);
                String couponTypestr = csltjson.optString("couponType");
                JSONObject couponTypejson = new JSONObject(couponTypestr);
                DictBean.setCtn(couponTypejson.optString("ctn"));
                DictBean.setCtid(couponTypejson.optString("ctid"));
                String couponBatchstr = csltjson.optString("couponBatch");
                JSONObject couponBatchjson = new JSONObject(couponBatchstr);
                org.json.JSONArray operatesarray = couponBatchjson.optJSONArray("operates");
                if (operatesarray == null || "".equals(operatesarray) || operatesarray.length() == 0) {

                } else {
                    StringBuilder oeratesb = new StringBuilder();
                    for (int h = 0; h < operatesarray.length(); h++) {
                        if (h == 0) {
                            oeratesb.append(operatesarray.get(h).toString());

                        } else {
                            oeratesb.append("、" + operatesarray.get(h).toString());

                        }

                    }
                    DictBean.setOperatesstr(oeratesb.toString());
                }

                DictBean.setCbca(couponBatchjson.optString("cbca"));
                DictBean.setCbvsd(couponBatchjson.optString("cbvsd"));
                DictBean.setCbr(couponBatchjson.optDouble("cbr"));
                if ("4".equals(cs)) {
                    DictBean.setStatus("19");
                } else if ("5".equals(cs)) {
                    DictBean.setStatus("20");
                } else {
                    DictBean.setStatus(couponBatchjson.optString("status"));
                }

                DictBean.setCbcf(couponBatchjson.optString("cbcf"));
                DictBean.setCbsnu(couponBatchjson.optString("cbsnu"));
                DictBean.setCbi(couponBatchjson.optString("cbi"));
                DictBean.setCbved(couponBatchjson.optString("cbved"));
                DictBean.setCbid(couponBatchjson.optString("cbid"));
                DictBean.setCbtid(couponBatchjson.optString("cbtid"));
                DictBean.setCbn(couponBatchjson.optString("cbn"));
                DictBean.setTname(couponBatchjson.optString("tname"));
                DictBean.setCbea(couponBatchjson.optString("cbea"));
                DictBean.setCbtt(couponBatchjson.optString("cbtt"));
                DictBean.setTcover(couponBatchjson.optString("tcover"));
                dictlist.add(DictBean);
            }
        }
        DiscountcouponData discountcouponData = new DiscountcouponData();
        String totalstr = json.optString("total");
        discountcouponData.setNumBeens(nums);
        discountcouponData.setArrayList(dictlist);
        discountcouponData.setTotal(totalstr);
        return discountcouponData;
    }

    @Override
    public String cancel_shop(String tid) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("tid", tid);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.CANCEL_SHOP, params);
        Log.v("ceshi", "result" + result);
        String msg = ResolveResponse.resolveData(result).toString();
        JSONObject json = new JSONObject(result);
        String datastr = json.optString("data");
        return datastr;
    }

    @Override
    public ArrayList<CategoryChooseBean> get_managing_type(String type, String level, String moid, String nationCode,
                                                           String child, String page_length, String page) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("type", type);
        params.put("level", level);
        params.put("moid", moid);
        params.put("nationCode", nationCode);
        params.put("child", child);
        // 候如果child=3则找商铺把二级和连带的三级一块搜索出来
        params.put("page_length", page_length);
        params.put("page", page);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.GET_MANAGING_TYPE_LIST, params);
        Log.v("ceshi", "result" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        // String msg = ResolveResponse.resolveData(result).toString();
        JSONObject json = new JSONObject(result);
        // org.json.JSONArray datajson = json.getJSONArray("data");
        // ArrayList<CategoryChooseBean> Dictlist = new
        // ArrayList<CategoryChooseBean>();
        ArrayList<CategoryChooseBean> dictlist = null;
        String datas = json.optString("data");
        Log.v("ceshi", "datas1" + datas);
        if (datas == null || "".equals(datas) | TextUtils.isEmpty(datas)) {
            return dictlist;
        } else {
            dictlist = new ArrayList<CategoryChooseBean>(
                    JSONArray.parseArray(json.optString("data"), CategoryChooseBean.class));
        }
        // for (int i = 0; i < datajson.length(); i++) {
        // CategoryChooseBean CatcBean = new CategoryChooseBean();
        // JSONObject csltjson = (JSONObject) datajson.get(i);
        // CatcBean.setIcon(csltjson.optString("icon"));
        // CatcBean.setName(csltjson.optString("name"));
        // CatcBean.setPid(csltjson.optString("pid"));
        // CatcBean.setMoid(csltjson.optString("moid"));
        // Dictlist.add(CatcBean);
        // }
        return dictlist;
    }

    @Override
    public boolean create_myservice(String cid, String csName, String desc, String longitude, String latitude,
                                    String etime, String urls, String uName, String uAddress, String uMobile, String tp, String nid)
            throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("cid", cid);
        params.put("channel", AppConfig.channel);
        params.put("csName", csName);
        params.put("desc", desc);
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        params.put("etime", etime);
        params.put("urls", urls);
        params.put("uname", uName);
        params.put("uaddress", uAddress);
        params.put("umobile", uMobile);
        params.put("tp", tp);
        params.put("nid", nid);
        try {
            params.put("sign", Tool.getSignature(params));
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.CREATE_MY_SERVICE, params);
        Log.v("ceshi", "result" + result);
        if (TextUtils.isEmpty(result)) {
            return false;
        } else {
            JSONObject json = new JSONObject(result);
            String estr = json.optString("e");
            if (TextUtils.isEmpty(estr)) {
                return false;
            } else {
                JSONObject ejson = new JSONObject(estr);
                if ("0".equals(ejson.optString("code"))) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    @Override
    public ArrayList<Merchant> getMerchantByNationCode(String nationCode) throws WSError, JSONException {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("nationCode", nationCode);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.GETMERCHANTBYNATIONCODE, params);
        Log.v("ceshi", "result" + result);
        JSONObject json = new JSONObject(result);
        ArrayList<Merchant> merchants = new ArrayList<Merchant>(
                JSONArray.parseArray(json.optString("data"), Merchant.class));
        // 商户不存在
        // {"e":{"desc":"merchant not
        // exist","code":-301},"data":"","cost":0.0020000000949949026}
        return merchants;
    }

    @Override
    public ArrayList<ActivityDetailBean> queryNewsList(String nationCode, String sort, String regionId, String type, String lat,
                                                       String lgt, String page, String page_length) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("nationCode", nationCode);
        params.put("regionId", regionId);
        params.put("sort", sort);

        params.put("type", type);
        params.put("lat", lat);
        params.put("lgt", lgt);
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
        String result = HttpConnUtil.doPost(AppConfig.QUERYNEWSLIST, params);
        Log.v("ceshi", "result" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);
        ArrayList<ActivityDetailBean> activityDetailBeans = new ArrayList<ActivityDetailBean>(
                JSONArray.parseArray(json.optString("data"), ActivityDetailBean.class));
        return activityDetailBeans;
    }

    @Override
    public boolean get_nation_coupon_list(String nationCode, String regionId, String typeId, String otype, String lat,
                                          String lgt, String page, String page_length) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("nationCode", nationCode);
        params.put("regionId", regionId);
        params.put("typeId", typeId);
        params.put("otype", otype);
        params.put("lat", lat);
        params.put("lgt", lgt);
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
        String result = HttpConnUtil.doPost(AppConfig.GET_NATION_COUPON_LIST, params);
        Log.v("ceshi", "result" + result);
        String msg = ResolveResponse.resolveData(result).toString();
        return true;
    }

    @Override
    public UserStatBean get_stat() throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);

        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid_array", "[" + AppConfig.userid + "]");
        String result = HttpConnUtil.doPost(AppConfig.GET_STAT, params);
        Log.v("ceshi", "result" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        String msg = ResolveResponse.resolveData(result).toString();
        JSONObject json = new JSONObject(msg);
        UserStatBean userStatBean = JSON.parseObject(json.optString("stat"), UserStatBean.class);
        return userStatBean;
    }

    @Override
    public MessageData get_message_list(String type, String page, String page_length) throws WSError, JSONException {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("type", type);
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
        String result = HttpConnUtil.doPost(AppConfig.MESSAGE_LIST, params);
        Log.v("ceshi", "result+" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);
        MessageData messageData = new MessageData();
        if (TextUtils.isEmpty(json.optString("total"))) {
            return null;
        }
        messageData.setTotalstr(json.optString("total"));
        if (TextUtils.isEmpty(json.optString("data"))) {
            return null;
        }
        if (!TextUtils.isEmpty(json.optString("data"))) {
            if (json.optJSONArray("data").length() == 0) {
                return null;
            } else {
                ArrayList<MessageInfo> messageinfos = new ArrayList<MessageInfo>(
                        JSONArray.parseArray(json.optString("data"), MessageInfo.class));
                messageData.setMessageinfos(messageinfos);
            }
        }


        return messageData;

    }

    @Override
    public boolean FlushMessageList() throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.FLUSH, params);
        if (TextUtils.isEmpty(result)) {
            return false;
        }
        JSONObject json = new JSONObject(result);
        if (json.optJSONObject("e").optString("desc") == null || "".equals(json.optJSONObject("e").optString("desc") == null)) {
            return false;
        } else {
            if ("success".equals(json.optJSONObject("e").optString("desc"))) {
                return true;
            } else {
                return false;
            }
        }
//{"cost":0.001,"e":{"code":0,"desc":"success"}}

    }

    @Override
    public String NoReadMessag() throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.NO_READMESS, params);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);
        String numstr = json.optString("data");
        if (TextUtils.isEmpty(numstr)) {
            numstr = "0";
        }
        return numstr;
    }

    @Override
    public boolean update_notice(String nid_array) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("nid", nid_array);
        // String nid = "[" + nid_array + "]";
        //  params.put("nid_array", nid);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.UPDATE_SYS, params);
        Log.v("ceshi", "result" + result);
        if (TextUtils.isEmpty(result)) {
            return false;
        }
        JSONObject json = new JSONObject(result);
        String estr = json.optString("e");
        if (!TextUtils.isEmpty(estr)) {
            JSONObject ejson = new JSONObject(estr);
            String descstr = ejson.optString("desc");
            if (!TextUtils.isEmpty(descstr)) {
                if (descstr.equals("success")) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean delete_message(String nid_array, String empty) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        String nid = "[" + nid_array + "]";
        params.put("nid_array", nid);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.DEL_SYS, params);
        Log.v("ceshi", "result" + result);
        JSONObject json = new JSONObject(result);
        String estr = json.optString("e");
        if (!TextUtils.isEmpty(estr)) {
            JSONObject ejson = new JSONObject(estr);
            String descstr = ejson.optString("desc");
            if (!TextUtils.isEmpty(descstr)) {
                if (descstr.equals("success")) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public CommentData get_comment_list(String page, String page_length, String sort) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("page", page);
        params.put("page_length", page_length);
        params.put("sort", sort);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.GET_COMMENT_LIST, params);
        Log.v("ceshi", "result" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        CommentData commentData = new CommentData();
        JSONObject json = new JSONObject(result);
        commentData.setTotal(json.optString("total"));
        if (TextUtils.isEmpty(json.optString("total"))) {
            return null;
        }
        org.json.JSONArray dataarrayjson = json.getJSONArray("data");
        ArrayList<CommentInfo> commentinfos = new ArrayList<CommentInfo>();
        for (int i = 0; i < dataarrayjson.length(); i++) {
            JSONObject dainfojson = (JSONObject) dataarrayjson.get(i);
            CommentInfo commentInfo = new CommentInfo();
            commentInfo.setCreate_time(dainfojson.optString("create_time"));
            commentInfo.setText(dainfojson.optString("text"));
            commentInfo.setType(dainfojson.optString("type"));
            ShopDetailInfo shop_info = new ShopDetailInfo();
            JSONObject shop_infojson = new JSONObject(dainfojson.optString("shop_info"));
            shop_info.setName(shop_infojson.optString("name"));
            shop_info.setSid(shop_infojson.optString("sid"));
            commentInfo.setShopinfo(shop_info);
            if (!TextUtils.isEmpty(dainfojson.optString("reply"))) {
                JSONObject replyjson = new JSONObject(dainfojson.optString("reply"));
                commentInfo.setReply_text(replyjson.optString("text"));
                commentInfo.setReply_time(replyjson.optString("reply_time"));
            } else {
                commentInfo.setReply_time("");
            }
            org.json.JSONArray imgsjson = dainfojson.getJSONArray("imgs");
            ArrayList<String> imgs = new ArrayList<String>();
            if (imgsjson != null & imgsjson.length() > 0) {
                for (int j = 0; j < imgsjson.length(); j++) {
                    Log.v("lsq", "length()++" + dainfojson.getJSONArray("imgs").get(j) + "");
                    String imgurl = dainfojson.getJSONArray("imgs").get(j) + "";
                    imgs.add(imgurl);
                }
            }
            commentInfo.setImgs(imgs);
            commentinfos.add(commentInfo);
        }
        commentData.setCommentinfos(commentinfos);
        return commentData;
    }

    @Override
    public MybagEntityData getMybagWall(String page, String page_length, String st) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("page", page);
        params.put("st", st);

//        public static final int STATUS_ALL   = -1;  // 全部
//        public static final int STATUS_NO     = 0;    // 未结算
//        public static final int STATUS_YES = 1;       // 已结算
//        public static final int STATUS_EXPIRED     = 2;    // 已过期
        params.put("page_length", page_length);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.GET_MY_BAG, params);
        Log.v("ceshi", "我的礼包" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);//userGift
        JSONObject datajson = new JSONObject(json.optString("data"));

        org.json.JSONArray giftjsonarray = datajson.optJSONArray("userGift");
        MybagEntityData mybagEntityData = new MybagEntityData();
        mybagEntityData.setExpired(datajson.optString("expired"));
        mybagEntityData.setNotReceive(datajson.optString("notReceive"));
        mybagEntityData.setReceive(datajson.optString("receive"));
        ArrayList<MybagEntity> mbelist = new ArrayList<MybagEntity>();
        for (int i = 0; i < giftjsonarray.length(); i++) {
            MybagEntity mbeBean = new MybagEntity();
            JSONObject csltjson = (JSONObject) giftjsonarray.get(i);
            mbeBean.setUid(csltjson.optString("uid"));
            mbeBean.setId(csltjson.optString("id"));
            mbeBean.setPna(csltjson.optString("pna"));
            mbeBean.setPat(csltjson.optString("pat"));
            mbeBean.setPpr(csltjson.optString("ppr"));
            mbeBean.setPt(csltjson.optString("pt"));
            mbeBean.setEpp(csltjson.optString("epp"));
            mbeBean.setIntg(csltjson.optString("intg"));
            mbeBean.setPid(csltjson.optString("pid"));
            mbeBean.setCt(csltjson.optString("ct"));
            mbeBean.setEpg(csltjson.optString("epg"));
            mbeBean.setSas(csltjson.optString("sas"));
            mbeBean.setPpi(csltjson.optString("ppi"));
            mbelist.add(mbeBean);
        }
        mybagEntityData.setMybagEntities(mbelist);
        return mybagEntityData;
    }

    @Override
    public MycardData getMycardbagWall(String page, String page_length) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
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
        String result = HttpConnUtil.doPost(AppConfig.GET_MY_CARD_BAG, params);
        Log.v("ceshi", "我的卡包" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        MycardData mycarddata = new MycardData();
        JSONObject json = new JSONObject(result);
        mycarddata.setTotal(json.optString("total"));
        String datajstr = json.optString("data");
        JSONObject datajson = new JSONObject(datajstr);
        ArrayList<MycardBagEntity> mbelist = new ArrayList<MycardBagEntity>();
        mycarddata.setEintg(datajson.optString("eintg"));
        for (int i = 0; i < datajson.getJSONArray("vips").length(); i++) {
            MycardBagEntity mbeBean = new MycardBagEntity();

            JSONObject vipsjson = (JSONObject) datajson.getJSONArray("vips").get(i);
//            if (TextUtils.isEmpty(vipsjson.optString("shop_info"))) {
//                continue;
//            }
            JSONObject shop_infojson = new JSONObject(vipsjson.optString("shop_info"));
            ShopDetailInfo shopinfo = new ShopDetailInfo();
            shopinfo.setName(shop_infojson.optString("name"));
            shopinfo.setSava(shop_infojson.optString("sava"));
            shopinfo.setSid(shop_infojson.optString("sid"));
            shopinfo.setAddress(shop_infojson.optString("address"));
            mbeBean.setShopinfo(shopinfo);

            mbeBean.setCreate_time(vipsjson.optString("create_time"));
            mbeBean.setIntg(vipsjson.optString("intg"));
            mbeBean.setVip_id(vipsjson.optString("vip_id"));
            mbeBean.setVip_level(vipsjson.optString("vip_level"));
            mbeBean.setVip_code(vipsjson.optString("vip_code"));
            mbeBean.setStatus(vipsjson.optString("status"));


            mbelist.add(mbeBean);
        }
        mycarddata.setArrayList(mbelist);
        return mycarddata;
    }

    // 提交评论
    @Override
    public boolean submit_comment(String sid, String eid, String type, String text, String imgs)
            throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("sid", sid);
        params.put("eid", eid);
        params.put("type", type);
        params.put("text", text);
        if (Tool.isNullStrns(imgs)) {
            params.put("imgs", imgs);
        }
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.SUBMIT_COMMENT, params);
        if (TextUtils.isEmpty(result)) {
            return false;
        }
        JSONObject json = new JSONObject(result);
        if (json.optString("data").equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    // 会员卡详情
    @Override
    public MymenbrescardData vip_detail(String vip_id, String page,
                                        String page_length) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("vip_id", vip_id);
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
        String result = HttpConnUtil.doPost(AppConfig.VIP_DETAIL, params);
        Log.v("ceshi", "vip_detail" + result);
        MymenbrescardData mycarddata = new MymenbrescardData();
        JSONObject json = new JSONObject(result);
        String datajstr = json.optString("data");
        JSONObject datajson = new JSONObject(datajstr);
        JSONObject shop_infojson = new JSONObject(datajson.optString("stat"));
        mycarddata.setTotal(shop_infojson.optString("total"));
        mycarddata.setIntg(shop_infojson.optString("intg"));
        ArrayList<MembreEntity> mbelist = new ArrayList<MembreEntity>();
        for (int i = 0; i < datajson.optJSONArray("expenses").length(); i++) {
            MembreEntity mbeBean = new MembreEntity();
            JSONObject expensesjson = (JSONObject) datajson.optJSONArray("expenses").get(i);
            mbeBean.setName(expensesjson.optString("name"));
            mbeBean.setCreate_time(expensesjson.optString("create_time"));
            mbeBean.setAmount(expensesjson.optString("amount"));
            mbeBean.setIntg(expensesjson.optString("intg"));
            mbeBean.setSid(expensesjson.optString("sid"));
            mbelist.add(mbeBean);
        }
        mycarddata.setArrayList(mbelist);
        return mycarddata;
    }

    @Override
    public MycardData un_bind_vip(String vid) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("vid", vid);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.UN_BIND_VIP, params);
        Log.v("ceshi", "UN_BIND_VIP" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        MycardData mycarddata = new MycardData();
        JSONObject json = new JSONObject(result);
        mycarddata.setData(json.optString("data"));
        return mycarddata;
    }

    @Override
    public ArrayList<ShopInfoBean> GoodShops(String typestr, String nationCode) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        // SY_MR_AUCTION_GIF 竞拍
        // SY_MR_SHOP_FAVORABLE 竞拍旁边活动和优惠券
        // SY_MR_PROSHOP_ICON 找商铺
        // SY_MR_SERSHOP_ICON 寻服务
        params.put("timestamp", Tool.getUninxTime());
        params.put("channel", AppConfig.channel);
        params.put("ip", AppConfig.ipstr); // SY_MR_SHOP_FAVORABLE
        params.put("shopRecommendTag", typestr);// SY_MR_PROSHOP_ICON,SY_MR_SERSHOP_ICON
        params.put("nationCode", nationCode);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.QUERYRECOMMENDSHOP, params);
        Log.v("lsq", "***+" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject alljson = new JSONObject(result);
        JSONObject sershopjson = alljson.getJSONArray("data").getJSONObject(0);
        ArrayList<ShopInfoBean> shopSerInfos = new ArrayList<ShopInfoBean>(
                JSONArray.parseArray(sershopjson.optString("SERVICE_GOOD_SHOP"), ShopInfoBean.class));
        return shopSerInfos;
    }

    @Override
    public ArrayList<Discountcoupons> getCouponList(String nationCode, String moid, String typeId, String otype, String page,
                                                    String page_length) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("nationCode", nationCode);
        params.put("moid", moid);
        params.put("typeId", typeId);
        params.put("otype", otype);
        params.put("page", page);
        params.put("page_length", page_length);

        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.GET_NATION_COUPON_LIST, params);
        Log.v("ceshi", "result" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);
        org.json.JSONArray datajson = json.optJSONArray("data");
        ArrayList<Discountcoupons> dictlist = new ArrayList<Discountcoupons>();
        if (datajson != null & !"".equals(datajson)) {
            for (int i = 0; i < datajson.length(); i++) {
                Discountcoupons dictBean = new Discountcoupons();
                JSONObject csltjson = (JSONObject) datajson.get(i);


                String couponTypestr = csltjson.optString("couponType");
                JSONObject couponTypejson = new JSONObject(couponTypestr);
                dictBean.setCtn(couponTypejson.optString("ctn"));
                dictBean.setCtid(couponTypejson.optString("ctid"));

                String couponBatchstr = csltjson.optString("couponBatch");
                JSONObject couponBatchjson = new JSONObject(couponBatchstr);
                //ArrayList<String> operateslist  = new ArrayList<String>();
                org.json.JSONArray operatesarray = couponBatchjson.optJSONArray("operates");
                if (operatesarray == null || "".equals(operatesarray) || operatesarray.length() == 0) {

                } else {
                    StringBuilder oeratesb = new StringBuilder();
                    for (int h = 0; h < operatesarray.length(); h++) {
                        if (h == 0) {
                            oeratesb.append(operatesarray.get(h).toString());

                        } else {
                            oeratesb.append("、" + operatesarray.get(h).toString());

                        }
                    }
                    dictBean.setOperatesstr(oeratesb.toString());
                    //for(int h=0;h<operatesarray.length();h++){
//                        operateslist.add(operatesarray.get(h).toString());
//                    }
//                    dictBean.setOperates(operateslist);
                }
                dictBean.setCbca(couponBatchjson.optString("cbca"));
                dictBean.setCbvsd(couponBatchjson.optString("cbvsd"));
                dictBean.setCbr(couponBatchjson.optDouble("cbr"));
                dictBean.setStatus(couponBatchjson.optString("status"));
                dictBean.setCbcf(couponBatchjson.optString("cbcf"));
                dictBean.setCbsnu(couponBatchjson.optString("cbsnu"));
                dictBean.setCbved(couponBatchjson.optString("cbved"));
                dictBean.setCbid(couponBatchjson.optString("cbid"));
                dictBean.setCbi(couponBatchjson.optString("cbi"));
                dictBean.setCbtid(couponBatchjson.optString("cbtid"));
                dictBean.setCbn(couponBatchjson.optString("cbn"));
                dictBean.setTname(couponBatchjson.optString("tname"));
                dictBean.setCbea(couponBatchjson.optString("cbea"));
                dictBean.setCbtt(couponBatchjson.optString("cbtt"));
                dictBean.setTcover(couponBatchjson.optString("tcover"));
                dictlist.add(dictBean);
            }
        }
        return dictlist;
    }

    @Override
    public ArrayList<Discountcoupons> getCouponListJiXuan(String nationCode, String page) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("nationCode", nationCode);
        params.put("tag", "RC_FINE_COUPON");
        params.put("page", page);

        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.GET_NATION_COUPON_LISTJINGXUAN, params);
        Log.v("ceshi", "result" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);
        org.json.JSONArray datajson = json.optJSONArray("data");
        ArrayList<Discountcoupons> dictlist = new ArrayList<Discountcoupons>();
        if (datajson != null & !"".equals(datajson)) {
            for (int i = 0; i < datajson.length(); i++) {
                Discountcoupons dictBean = new Discountcoupons();
                JSONObject csltjson = (JSONObject) datajson.get(i);
                String couponTypestr = csltjson.optString("couponType");
                JSONObject couponTypejson = new JSONObject(couponTypestr);
                dictBean.setCtn(couponTypejson.optString("ctn"));
                dictBean.setCtid(couponTypejson.optString("ctid"));

                String couponBatchstr = csltjson.optString("couponBatch");
                JSONObject couponBatchjson = new JSONObject(couponBatchstr);
                //ArrayList<String> operateslist  = new ArrayList<String>();
                org.json.JSONArray operatesarray = couponBatchjson.optJSONArray("operates");
                if (operatesarray == null || "".equals(operatesarray) || operatesarray.length() == 0) {

                } else {
                    StringBuilder oeratesb = new StringBuilder();
                    for (int h = 0; h < operatesarray.length(); h++) {
                        if (h == 0) {
                            oeratesb.append(operatesarray.get(h).toString());

                        } else {
                            oeratesb.append("、" + operatesarray.get(h).toString());

                        }
                    }
                    dictBean.setOperatesstr(oeratesb.toString());
                    //for(int h=0;h<operatesarray.length();h++){
//                        operateslist.add(operatesarray.get(h).toString());
//                    }
//                    dictBean.setOperates(operateslist);
                }
                dictBean.setCbca(couponBatchjson.optString("cbca"));
                dictBean.setCbvsd(couponBatchjson.optString("cbvsd"));
                dictBean.setCbr(couponBatchjson.optDouble("cbr"));
                dictBean.setStatus(couponBatchjson.optString("status"));
                dictBean.setCbcf(couponBatchjson.optString("cbcf"));
                dictBean.setCbsnu(couponBatchjson.optString("cbsnu"));
                dictBean.setCbved(couponBatchjson.optString("cbved"));
                dictBean.setCbid(couponBatchjson.optString("cbid"));
                dictBean.setCbi(couponBatchjson.optString("cbi"));
                dictBean.setCbtid(couponBatchjson.optString("cbtid"));
                dictBean.setCbn(couponBatchjson.optString("cbn"));
                dictBean.setTname(couponBatchjson.optString("tname"));
                dictBean.setCbea(couponBatchjson.optString("cbea"));
                dictBean.setCbtt(couponBatchjson.optString("cbtt"));
                dictBean.setTcover(couponBatchjson.optString("tcover"));
                dictlist.add(dictBean);
            }
        }
        return dictlist;
    }

    // 反馈内容
    @Override
    public boolean feedback(String type, String content, String info, String sid) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("content", content);
        params.put("info", info);
        params.put("type", type);
        params.put("sid", sid);
        // params.put("lati", "1.0");

        try {
            params.put("sign", Tool.getSignature(params));
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.FEEDBACK, params);
        if (TextUtils.isEmpty(result)) {
            return false;
        }
        JSONObject json = new JSONObject(result);
        Log.v("ceshi", "result" + result);
        if (json == null) {
            return false;
        }
        String estr = json.optString("e");
        if (!TextUtils.isEmpty(estr)) {
            JSONObject ejson = new JSONObject(estr);
            if (ejson == null) {
                return false;
            }
            String descstr = ejson.optString("desc");
            if (!TextUtils.isEmpty(descstr)) {
                if ("success".equals(descstr)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public SingData sing_in() throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        // params.put("ip", AppConfigDigo.ipstr);不需要ip
        try {
            params.put("sign", Tool.getSignature(params));
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.SIGN_IN, params);
        Log.v("ceshi", "result" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);
        SingData sing = new SingData();
        String datajson = json.optString("data");
        if (!TextUtils.isEmpty(datajson)) {
            JSONObject dason = new JSONObject(datajson);
            sing.setGold(dason.optString("gold"));
            sing.setUid(dason.optString("uid"));
            sing.setCount(dason.optString("count"));
            sing.setSign_time(dason.optString("sign_time"));
        }
        String estr = json.optString("e");
        if (!TextUtils.isEmpty(estr)) {
            JSONObject ejson = new JSONObject(estr);
            sing.setDesc(ejson.optString("desc"));
        }
        return sing;
    }

    @Override
    public UserInfoData get_user(String info, String hide) throws WSError, JSONException {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        // params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("info", info);
        params.put("hide", hide);
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        // params.put("token", AppConfig.usertoken);
        params.put("uid", App.getInstance().getUserid());
        String result = HttpConnUtil.doPost(AppConfig.GET_USER, params);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);
        UserInfoData userinfo = new UserInfoData();
        String datajson = json.optString("data");
        if (!TextUtils.isEmpty(datajson)) {
            JSONObject dason = new JSONObject(datajson);
            userinfo.setArea(dason.optString("area"));
            userinfo.setBirthday(dason.optString("birthday"));
            userinfo.setOccupation(dason.optString("occupation"));
            userinfo.setAddress(dason.optString("address"));
            userinfo.setCreate_time(dason.optString("create_time"));
            userinfo.setMobile(dason.optString("mobile"));
            userinfo.setHobby(dason.optString("hobby"));
            userinfo.setReal_name(dason.optString("real_name"));
            userinfo.setAvatar(dason.optString("avatar"));
            userinfo.setReg_source(dason.optString("reg_source"));
            userinfo.setEmail_status(dason.optString("email_status"));
            userinfo.setNick(dason.optString("nick"));
            userinfo.setUid(dason.optString("uid"));
            userinfo.setGen(dason.optString("gen"));
            userinfo.setReg_time(dason.optString("reg_time"));
            userinfo.setWedding(dason.optString("wedding"));
            userinfo.setVillage(dason.optString("village"));
            userinfo.setEmail(dason.optString("email"));
            userinfo.setStatus(dason.optString("status"));
            userinfo.setProvince(dason.optString("province"));
            userinfo.setCitycode(dason.optString("citycode"));
            userinfo.setCity(dason.optString("city"));
            userinfo.setDistrict(dason.optString("district"));
        }
        return userinfo;
    }

    @Override
    public UserInfoData update_user_info(String realname, String nick, String area, String address, String village,
                                         String birthday, String occipaction, String wedding, String gen, String avatar, String hobby, String province, String citycode, String city, String district)
            throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("hide", "0");
        params.put("nick", nick);
        params.put("real_name", realname);// 真实姓名
        params.put("gen", gen);// 性别 1 男 2 女 0 保密
        params.put("area", area);// 所在区域 ?
        params.put("address", address);// 详细地址
        params.put("village", village);// 所在小区
        params.put("avatar", avatar);// 头像
        params.put("birthday", birthday);// 生日
        params.put("occupation", occipaction);// 职业
        params.put("wedding", wedding);// 结婚纪念日
        params.put("hobby", hobby);// 兴趣
        params.put("province", province);// 省
        params.put("citycode", citycode);// 城市id
        params.put("city", city);// 城市名字
        params.put("district", district);// 具体名字

        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.UPDATE_USER_INFO, params);
        Log.v("ceshi", "result" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);
        UserInfoData userinfodata = new UserInfoData();
        String datajson = json.optString("data");
        Log.v("ceshi", "dataresult" + datajson);
        if (!TextUtils.isEmpty(datajson)) {
            JSONObject dason = new JSONObject(datajson);
            Log.v("ceshi", "uid" + dason.optString("uid"));
            userinfodata.setUid(dason.optString("uid"));
            Log.v("ceshi", "getUid" + userinfodata.getUid());
        }
        return userinfodata;
    }

    @Override
    public ArrayList<ActivityDetailBean> getStoreorShopAtyList(String targetId, String targetType, String page,
                                                               String page_length) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("targetId", targetId);
        params.put("targetType", targetType);
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
        String result = HttpConnUtil.doPost(AppConfig.STOREATYLISTORSHOPATYLIST, params);
        Log.v("ceshi", "result" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);
        if (json.optJSONArray("data") == null || "".equals(json.optJSONArray("data"))
                || json.optJSONArray("data").length() <= 0) {
            return null;
        }
        ArrayList<ActivityDetailBean> activityDetailBeans = new ArrayList<ActivityDetailBean>(
                JSONArray.parseArray(json.optString("data"), ActivityDetailBean.class));
        return activityDetailBeans;
    }

    @Override
    public ArrayList<Discountcoupons> getStoreorShopCouponList(String targetId, String targetType, String page,
                                                               String page_length) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("targetId", targetId);
        params.put("targetType", targetType);
        params.put("page", page);
        params.put("page_length", page_length);
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.STOREATYLISTORSHOPCOUPONLIST, params);
        Log.v("ceshi", "result" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);
        org.json.JSONArray datajson = json.optJSONArray("data");
        ArrayList<Discountcoupons> dictlist = new ArrayList<Discountcoupons>();
        if (datajson != null & !"".equals(datajson)) {
            for (int i = 0; i < datajson.length(); i++) {
                Discountcoupons DictBean = new Discountcoupons();
                JSONObject csltjson = (JSONObject) datajson.get(i);

                String couponTypestr = csltjson.optString("couponType");
                JSONObject couponTypejson = new JSONObject(couponTypestr);
                DictBean.setCtn(couponTypejson.optString("ctn"));
                DictBean.setCtid(couponTypejson.optString("ctid"));

                String couponBatchstr = csltjson.optString("couponBatch");
                JSONObject couponBatchjson = new JSONObject(couponBatchstr);
                org.json.JSONArray operatesarray = couponBatchjson.optJSONArray("operates");
                if (operatesarray == null || "".equals(operatesarray) || operatesarray.length() == 0) {

                } else {
                    StringBuilder oeratesb = new StringBuilder();
                    for (int h = 0; h < operatesarray.length(); h++) {
                        if (h == 0) {
                            oeratesb.append(operatesarray.get(h).toString());

                        } else {
                            oeratesb.append("、" + operatesarray.get(h).toString());

                        }

                    }
                    DictBean.setOperatesstr(oeratesb.toString());
                }

                DictBean.setCbca(couponBatchjson.optString("cbca"));
                DictBean.setCbd(couponBatchjson.optString("cbd"));
                DictBean.setCbvsd(couponBatchjson.optString("cbvsd"));
                Log.v("lsq", "cbr+" + couponBatchjson.optDouble("cbr"));
                DictBean.setCbr(couponBatchjson.optDouble("cbr"));
                Log.v("lsq", "cbr22+" + DictBean.getCbr());
                DictBean.setStatus(couponBatchjson.optString("status"));
                DictBean.setCbcf(couponBatchjson.optString("cbcf"));
                DictBean.setCbsnu(couponBatchjson.optString("cbsnu"));
                DictBean.setCbi(couponBatchjson.optString("cbi"));
                DictBean.setCbved(couponBatchjson.optString("cbved"));
                DictBean.setCbid(couponBatchjson.optString("cbid"));
                DictBean.setCbtid(couponBatchjson.optString("cbtid"));
                DictBean.setCbn(couponBatchjson.optString("cbn"));
                DictBean.setTname(couponBatchjson.optString("tname"));
                DictBean.setCbea(couponBatchjson.optString("cbea"));
                DictBean.setCbtt(couponBatchjson.optString("cbtt"));
                DictBean.setTcover(couponBatchjson.optString("tcover"));
                dictlist.add(DictBean);
            }
        } else {
            dictlist = null;
        }
        return dictlist;
    }

    @Override
    public ShopinfoData SerachShops(String type, String keywords, String mark, String nationCode)
            throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("keywords", keywords);
        // 1 店铺 2是商品
        params.put("type", type);
        params.put("nationCode", nationCode);
        params.put("mark", mark);
        params.put("page_length", "10");
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.KEYWORDSERACH_SHOPS, params);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);
        JSONObject ejson = new JSONObject(json.optString("e"));
        ShopinfoData shopinfoData = new ShopinfoData();
        ArrayList<ShopInfoBean> shopInfoBeans = new ArrayList<ShopInfoBean>();
        if (!TextUtils.isEmpty(ejson.optString("desc"))) {
            return shopinfoData;
        }
        JSONObject datajson = json.getJSONObject("data");
        //分页游标
        String markstr = datajson.optString("mark");
        shopinfoData.setMarkstr(markstr);
        org.json.JSONArray shopsarray = datajson.getJSONArray("shops");
        if (shopsarray != null & shopsarray.length() > 0) {
            for (int i = 0; i < shopsarray.length(); i++) {
                JSONObject shopInfojson = (JSONObject) shopsarray.get(i);
                ShopInfoBean shopInfoBean = new ShopInfoBean();
                shopInfoBean.setProduct(shopInfojson.optString("product"));
                shopInfoBean.setCoupon(shopInfojson.optString("coupon"));
                shopInfoBean.setNews(shopInfojson.optString("news"));
                shopInfoBean.setAddress(shopInfojson.optString("address"));
                shopInfoBean.setContact(shopInfojson.optString("contact"));
                shopInfoBean.setStag(shopInfojson.optString("stag"));
                shopInfoBean.setCover(shopInfojson.optString("cover"));
                shopInfoBean.setDistance(shopInfojson.optDouble("distance"));
                shopInfoBean.setGoods(shopInfojson.optString("goods"));
                shopInfoBean.setName(shopInfojson.optString("name"));
                shopInfoBean.setOpen_time(shopInfojson.optString("open_time"));
                shopInfoBean.setOperate(shopInfojson.optString("operate"));
                shopInfoBean.setSid(shopInfojson.optString("sid"));
                shopInfoBeans.add(shopInfoBean);
            }
            shopinfoData.setShopInfoBeans(shopInfoBeans);
        } else {
            shopInfoBeans = null;
        }

        return shopinfoData;
    }

    // 优惠券详情
    @Override
    public CouponDetailData coupon_detail(String couponBatchId) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("couponBatchId", couponBatchId);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.COUPON_DETAIL, params);
        Log.v("ceshi", "result" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);
        JSONObject datajson = json.optJSONObject("data");
        CouponDetailData Cdetail = new CouponDetailData();
        String estr = json.optString("e");
        if (!TextUtils.isEmpty(estr)) {
            JSONObject ejson = new JSONObject(estr);
            Cdetail.setDesc(ejson.optString("desc"));
        }

        String couponTypestr = datajson.optString("couponType");
        JSONObject couponTypejson = new JSONObject(couponTypestr);
        Cdetail.setCtid(couponTypejson.optString("ctid"));
        Cdetail.setCtn(couponTypejson.optString("ctn"));

        String couponBatchstr = datajson.optString("couponBatch");
        JSONObject couponBatchjson = new JSONObject(couponBatchstr);

        ArrayList<String> operstrs = new ArrayList<String>();
        if (couponBatchjson.optJSONArray("operates") == null || "".equals(couponBatchjson.optJSONArray("operates"))) {

        } else {
            for (int i = 0; i < couponBatchjson.getJSONArray("operates").length(); i++) {
                String opserstr;
                opserstr = couponBatchjson.getJSONArray("operates").get(i) + "";
                operstrs.add(opserstr);
            }
            Cdetail.setOperstrs(operstrs);
        }
        Cdetail.setCbasc(couponBatchjson.optString("cbasc"));
        Cdetail.setCbd(couponBatchjson.optString("cbd"));
        Cdetail.setCbvsd(couponBatchjson.optString("cbvsd"));
        Cdetail.setTname(couponBatchjson.optString("tname"));
        Cdetail.setCbcf(couponBatchjson.optString("cbcf"));
        Cdetail.setCbn(couponBatchjson.optString("cbn"));
        Cdetail.setCbtid(couponBatchjson.optString("cbtid"));
        Cdetail.setCbtt(couponBatchjson.optString("cbtt"));
        Cdetail.setCbr(couponBatchjson.optDouble("cbr"));
        Cdetail.setCbca(couponBatchjson.optString("cbca"));
        Cdetail.setCbid(couponBatchjson.optString("cbid"));
        Cdetail.setCbea(couponBatchjson.optString("cbea"));
        Cdetail.setCbved(couponBatchjson.optString("cbved"));
        Cdetail.setCbsnu(couponBatchjson.optString("cbsnu"));
        Cdetail.setTcover(couponBatchjson.optString("tcover"));
        Cdetail.setStatus(couponBatchjson.optString("status"));
        return Cdetail;
    }

    // 领取优惠劵
    @Override
    public boolean get_coupon(String couponBatchId) throws WSError, JSONException {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("couponBatchId", couponBatchId);

        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.GET_COUPON, params);
        Log.v("ceshi", "result" + result);
        if (TextUtils.isEmpty(result)) {
            return false;
        }
        JSONObject json = new JSONObject(result);
        String estr = json.optString("e");
        JSONObject ejson = new JSONObject(estr);
        String descstr = ejson.optString("desc");
        if ("success".equals(descstr)) {
            return true;
        } else {
            return false;
        }

        // {"desc":"couponBatch get time not
        // exist","provider":"communityunion","code":-229},"cost":0.002}
        // 返回这个couponBatch get time not exist 提示不在活动期内
    }

    @Override
    public boolean getSearchApi(String nationCode) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("nationCode", nationCode);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.QUERYRECOMMENDMERCHANT, params);
        // Log.v("ceshi", "resultIMG+" + result);
        String msg = ResolveResponse.resolveData(result).toString();
        JSONObject json = new JSONObject(msg);
        return false;
    }

    @Override
    public SeekCateGoryData get_managing_twothree(String type, String level, String moid, String nationCode,
                                                  String child, String page_length, String page) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("type", type);
        params.put("level", level);
        params.put("moid", moid);
        params.put("nationCode", nationCode);
        params.put("child", child);
        // 候如果child=3则找商铺把二级和连带的三级一块搜索出来
        params.put("page_length", page_length);
        params.put("page", page);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.GET_MANAGING_TYPE_LIST, params);
        Log.v("ceshi", "result" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        SeekCateGoryData cateGoryData = new SeekCateGoryData();
        JSONObject json = new JSONObject(result);
        String datastr = json.optString("data");
        if (TextUtils.isEmpty(datastr) || "".equals(datastr)) {
            return null;
        }
        JSONObject datajson = new JSONObject(datastr);
        // 活动列表
        ArrayList<ActivityDetailBean> atydetails = new ArrayList<ActivityDetailBean>(
                JSONArray.parseArray(datajson.optString("news"), ActivityDetailBean.class));
        cateGoryData.setAtydetails(atydetails);
        org.json.JSONArray catrgoryarray = datajson.getJSONArray("category");
        ArrayList<CategoryChooseBean> categorychoosbeans = new ArrayList<CategoryChooseBean>();
        for (int j = 0; j < catrgoryarray.length(); j++) {
            CategoryChooseBean categoryChooseBean = new CategoryChooseBean();
            JSONObject cagegoryjson = (JSONObject) catrgoryarray.get(j);
            categoryChooseBean.setName(cagegoryjson.optString("name"));
            categoryChooseBean.setMoid(cagegoryjson.optString("moid"));
            org.json.JSONArray childrenarray = cagegoryjson.getJSONArray("children");
            ArrayList<ChildernBean> childderlis = new ArrayList<ChildernBean>();
            for (int h = 0; h < childrenarray.length(); h++) {
                ChildernBean childernBean = new ChildernBean();
                JSONObject childrenjson = (JSONObject) childrenarray.get(h);
                childernBean.setName(childrenjson.optString("name"));
                childernBean.setMoid(childrenjson.optString("moid"));
                childernBean.setIcon(childrenjson.optString("icon"));
                childernBean.setPid(childrenjson.optString("pid"));
                childderlis.add(childernBean);
            }
            categoryChooseBean.setChildderlis(childderlis);
            categorychoosbeans.add(categoryChooseBean);
        }
        cateGoryData.setCategorychoosbeans(categorychoosbeans);
        return cateGoryData;
    }

    @Override
    public ArrayList<ShopInfoBean> queryShopMulti(String brandId, String lat, String lgt, String mfid, String operateId,
                                                  String mlvl, String regionId, String sort, String type, String nationCode, String page, String page_length) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("brandId", brandId);
        params.put("lat", lat);
        params.put("lgt", lgt);
        params.put("mfid", mfid);
        params.put("operateId", operateId);
        params.put("mlvl", mlvl);
        params.put("regionId", regionId);
        params.put("sort", sort);
        params.put("type", type);
        params.put("page_length", page_length);
        params.put("page", page);
        params.put("nationCode", nationCode);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.QUERYSHOPMYLTI, params);
        Log.v("ceshi", "result" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);
        if (json.optString("data") == null || "".equals(json.optString("data"))) {
            return null;
            // ArrayList<ShopInfoBean> shopInfoBeans = new
            // ArrayList<ShopInfoBean>();
            // shopInfoBeans.get(0).setGoods("success");
        }
        org.json.JSONArray datajson = json.getJSONArray("data");
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
            shopInfoBean.setStag(shopInfojson.optString("stag"));
            shopInfoBean.setGoods(shopInfojson.optString("goods"));
            shopInfoBean.setName(shopInfojson.optString("name"));
            shopInfoBean.setOpen_time(shopInfojson.optString("open_time"));
            shopInfoBean.setOperate(shopInfojson.optString("operate"));
            shopInfoBean.setSid(shopInfojson.optString("sid"));
            shopInfoBeans.add(shopInfoBean);
        }
        return shopInfoBeans;
    }

    @Override
    public ArrayList<BrandDate> getBrandToMoid(String moid, String page, String page_length)
            throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("moids", moid);
        params.put("rec", "1");
        //rec=1
        params.put("page_length", page_length);
        params.put("page", page);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.BRANDTOMOID, params);
        Log.v("ceshi", "result" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);
        String e = json.optString("e");
        JSONObject ejson = new JSONObject(e);
        if (ejson.optString("desc") != null & !"".equals(ejson.optString("desc"))) {
            if (ejson.optString("desc").contains("no")) {
                // ArrayList<BrandDate> arrayList = new ArrayList<BrandDate>();
                // BrandDate brand = new BrandDate();
                // brand.setBrand_id("");
                // brand.setBrand_name("全部");
                // arrayList.add(brand);
                return null;
            }

        }

        ArrayList<BrandDate> arrayList = new ArrayList<BrandDate>(
                JSONArray.parseArray(json.optString("data"), BrandDate.class));
        return arrayList;
    }

    @Override
    public ArrayList<ActivityDetailBean> getLookSeekAty(String page, String nationCode, String latstr, String logstr, String tag) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("nationCode", nationCode);
        params.put("tag", tag);
        params.put("page", page);
        params.put("lat", latstr);
        params.put("lgt", logstr);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.QUERY_LOOK_SEEK_ATY, params);

        ActivityDetailBean activityDetailBean = null;
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);
        if (json.optString("data") == null || "".equals(json)) {
            return null;
        }
        ArrayList<ActivityDetailBean> activityDetailBeans = new ArrayList<ActivityDetailBean>(
                JSONArray.parseArray(json.optString("data"), ActivityDetailBean.class));
        return activityDetailBeans;


    }

    @Override
    public ArrayList<ExChangeBean> getNewProduct(String targetId, String targetType, String page, String page_length)
            throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("targetId", targetId);
        params.put("targetType", targetType);
        params.put("page", page);
        params.put("page_length", page_length);
        params.put("productType", "3");
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.GETPRODUCTNEWSLIST, params);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject alljson = new JSONObject(result);
        if (alljson.opt("data") == null || "".equals(alljson.opt("data"))) {
            return null;
        }
        ArrayList<ExChangeBean> exchangebeans = new ArrayList<ExChangeBean>(JSONArray.parseArray(alljson.optString("data"), ExChangeBean.class));
        return exchangebeans;
    }

    @Override
    public ArrayList<ExChangeBean> getJingpaiDuihuan(String id, String targetId, String targetType, String productType,
                                                     String page, String page_length) throws WSError, JSONException {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("targetId", targetId);
        params.put("targetType", targetType);
        params.put("page", page);
        params.put("page_length", page_length);
        params.put("productType", productType);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        // params.put("token", AppConfig.usertoken);
        params.put("uid", id);
        String result = HttpConnUtil.doPost(AppConfig.GETSTORESHOPPRODUCTLIST, params);
        Log.v("lsq", "***+" + result);
        if (TextUtils.isEmpty(result)) {
            WSError ws = new WSError();
            ws.setMessage("800");
            throw ws;
        }
        if (TextUtils.isEmpty(result)) {
            WSError ws = new WSError();
            ws.setMessage("800");
            throw ws;
        }

        JSONObject alljson = new JSONObject(result);
        if (alljson.opt("data") == null || "".equals(alljson.opt("data"))) {
            return null;
        }
        ArrayList<ExChangeBean> exchangebeans = new ArrayList<ExChangeBean>(
                JSONArray.parseArray(alljson.optString("data"), ExChangeBean.class));
        return exchangebeans;

    }

    @Override
    public ShopDetailDataBean getShopDetail(String shopId) throws WSError, JSONException {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("shopId", shopId);

        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.GETSHOPDETAIL, params);
        Log.v("ceshi", "***+" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject alljson = new JSONObject(result);
        JSONObject datajson = new JSONObject(alljson.optString("data"));
        ShopDetailDataBean shopdetailbean = new ShopDetailDataBean();
        Log.v("lsq", "*name**+" + datajson.optString("sn"));
        ArrayList<ShopoperateBean> ShopoperateBeans = null;
        ArrayList<ShopbrandBean> ShopbrandBeans = null;
        if (datajson.optString("operates") != null & !"".equals(datajson.optString("operates"))) {
            ShopoperateBeans = new ArrayList<ShopoperateBean>(
                    JSONArray.parseArray(datajson.optString("operates"), ShopoperateBean.class));
        }

        if (datajson.optString("brands") != null & !"".equals(datajson.optString("brands"))) {
            ShopbrandBeans = new ArrayList<ShopbrandBean>(
                    JSONArray.parseArray(datajson.optString("brands"), ShopbrandBean.class));
        }
        PointBean pointBean = new PointBean();
        String posintstr = datajson.optString("point");
        JSONObject pointjson = new JSONObject(posintstr);
        pointBean.setLat(pointjson.optDouble("lat"));
        pointBean.setLgt(pointjson.optDouble("lgt"));
        shopdetailbean.setPointBean(pointBean);
        shopdetailbean.setScov(datajson.optString("scov"));
        shopdetailbean.setSid(datajson.optString("sid"));
        shopdetailbean.setSd(datajson.optString("sd"));
        shopdetailbean.setRelation(datajson.optString("relation"));
        shopdetailbean.setSll(datajson.optString("sll"));
        shopdetailbean.setSmn(datajson.optString("smn"));
        shopdetailbean.setSdbt(datajson.optString("sdbt"));
        shopdetailbean.setSbna(datajson.optString("sbna"));
        shopdetailbean.setSad(datajson.optString("sad"));
        shopdetailbean.setSn(datajson.optString("sn"));
        shopdetailbean.setIcas(datajson.optString("icas"));
        ArrayList<String> urls = new ArrayList<String>();

        if (datajson.optJSONArray("sal") == null || "".equals(datajson.optJSONArray("sal"))) {

        } else {
            for (int i = 0; i < datajson.getJSONArray("sal").length(); i++) {
                String url;
                url = datajson.getJSONArray("sal").get(i) + "";
                urls.add(url);
            }
            shopdetailbean.setUrls(urls);
        }


        ArrayList<StoreDetailMomInfo> telinfos = new ArrayList<StoreDetailMomInfo>();

        if (datajson.optJSONArray("som") == null || "".equals(datajson.optJSONArray("som"))) {

        } else {
            for (int i = 0; i < datajson.getJSONArray("som").length(); i++) {
                StoreDetailMomInfo detailMomInfo = new StoreDetailMomInfo();
                detailMomInfo.setTelstr(datajson.getJSONArray("som").get(i) + "");
                telinfos.add(detailMomInfo);

            }
            shopdetailbean.setTelinfos(telinfos);
        }
        shopdetailbean.setShopoperateBeans(ShopoperateBeans);
        shopdetailbean.setShopbrandBeans(ShopbrandBeans);
        return shopdetailbean;
    }

    // 提交头像
    @Override
    public boolean get_UserCenter(String ava) throws WSError, JSONException {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("ava", ava);

        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.GET_USERCENTER, params);
        Log.v("ceshi", "result" + result);
        if (TextUtils.isEmpty(result)) {
            return false;
        }
        JSONObject json = new JSONObject(result);
        String estr = json.optString("e");
        JSONObject ejson = new JSONObject(estr);
        String is = ejson.optString("desc");
        if (is.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean add_shop(String tid) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("tid", tid);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.ADD_SHOP, params);
        String msg = ResolveResponse.resolveData(result).toString();
        return Boolean.parseBoolean(msg);

    }

    @Override
    public CommentData get_shopcomment_list(String sid, String max_id, String min_id, String count, String type)
            throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("sid", sid);
        params.put("max_id", max_id);
        params.put("min_id", min_id);
        params.put("count", count);
        params.put("type", type);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.SHOP_COMMENTLIST, params);
        Log.v("ceshi", "result" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        CommentData commentData = new CommentData();
        JSONObject json = new JSONObject(result);
        if (json.optString("data") == null || "".equals(json.optString("data"))) {
            return null;
        }
        commentData.setTotal(json.optString("total"));

        JSONObject datajson = new JSONObject(json.optString("data"));
        ArrayList<ShopComment> commentinfos = new ArrayList<>();
        commentData.setGood(datajson.optString("good"));
        commentData.setNormal(datajson.optString("normal"));
        commentData.setBad(datajson.optString("bad"));
        commentData.setMax_id(datajson.optString("max_id"));
        commentData.setMin_id(datajson.optString("min_id"));
        org.json.JSONArray commentsarrayjson = datajson.getJSONArray("comments");
        Log.v("TEST", "length+" + commentsarrayjson.length());
        for (int i = 0; i < commentsarrayjson.length(); i++) {
            JSONObject commentjson = (JSONObject) commentsarrayjson.get(i);
            ShopComment commentInfo = new ShopComment();
            org.json.JSONArray imgsarrayjson = commentjson.getJSONArray("imgs");
            ArrayList<String> urls = new ArrayList<>();
            for (int h = 0; h < imgsarrayjson.length(); h++) {
                String strurl = (String) imgsarrayjson.get(h);
                urls.add(strurl);
            }
            if (!TextUtils.isEmpty(commentjson.optString("reply"))) {
                JSONObject replyjson = new JSONObject(commentjson.optString("reply"));
                commentInfo.setReply_text(replyjson.optString("text"));
                commentInfo.setReply_time(replyjson.optString("reply_time"));
            }
            commentInfo.setStrings(urls);
            commentInfo.setCreate_time(commentjson.optString("create_time"));
            commentInfo.setText(commentjson.optString("text"));
            commentInfo.setType(commentjson.optString("type"));
            if (!TextUtils.isEmpty(commentjson.optString("userinfo"))) {
                JSONObject userinfojson = new JSONObject(commentjson.optString("userinfo"));
                commentInfo.setNick(userinfojson.optString("nick"));
                commentInfo.setAvatar(userinfojson.optString("avatar"));
            }

            commentinfos.add(commentInfo);
        }
        commentData.setShopInfoBeans(commentinfos);
        return commentData;
    }


    @Override
    public boolean set_changmobile(String mobile, String smscode, String type) throws WSError, JSONException {
        boolean success;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("mobile", mobile);
        params.put("smscode", smscode);
        params.put("type", type);
        //params.put("ip", AppConfig.ipstr);
        try {
            params.put("sign", Tool.getSignature(params));
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        String result = HttpConnUtil.doPost(AppConfig.SET_CHANGMOBILE, params);
        JSONObject jsonObject = new JSONObject(result);
        String jsonerr = jsonObject.optString("e");
        JSONObject jsonerror = new JSONObject(jsonerr);
        String code = jsonerror.optString("code");
        if (code.equals("0")) {
            success = true;
        } else {
            success = false;
        }
        return success;
    }


    @Override
    public AppUpdata appUpdata(String version_code, String appmd5) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("appkey", "digo");
        params.put("version_code", version_code);
        params.put("md5", appmd5);
        params.put("channel", Constants.CHANNEL);
        params.put("v", "0");

        String result = HttpConnUtil.doPost(AppConfig.SET_AAPPUPDATE, params);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        AppUpdata updata = new AppUpdata();
        JSONObject uodateObject = new JSONObject(result);
        String data = uodateObject.optString("data");
        JSONObject dataJson = new JSONObject(data);
        updata.setTarget_size(dataJson.optString("target_size"));
        updata.setDown_url(dataJson.optString("down_url"));
        updata.setU_log(dataJson.optString("u_log"));
        updata.setUpdate(dataJson.optBoolean("update"));
        updata.setNew_version(dataJson.optString("new_version"));
        updata.setNew_md5(dataJson.optString("new_md5"));

        return updata;
    }

    @Override
    public Shopdatalist get_floor_shops(String mid, String floorId, String page, String page_length)
            throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("mid", mid);
        params.put("floorId", floorId);
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
        String result = HttpConnUtil.doPost(AppConfig.GET_STOREFLOOR_SHOPS, params);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject json = new JSONObject(result);
        Shopdatalist shopdata = new Shopdatalist();
        org.json.JSONArray datajson = json.getJSONArray("data");
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
        shopdata.setShops(shopInfoBeans);
        return shopdata;
    }

    @Override
    public ArrayList<String> gethotkey(String nationCode) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("nationCode", nationCode);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.GET_HOT_KEYS, params);
        Log.v("ceshi", "result+" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject alljson = new JSONObject(result);
        ArrayList<String> strlists = new ArrayList<String>(
                JSONArray.parseArray(alljson.optString("data"), String.class));
        return strlists;
    }


    @Override
    public UserInfoData get_user_address() throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.GET_USER_ADDRESS, params);
        Log.v("lsq", "result+" + result);
        JSONObject alljson = new JSONObject(result);
        UserInfoData userInfoData = new UserInfoData();

        return userInfoData;
    }

    @Override
    public TiemTitleData getPeriodTitle(String starttime, String endtime, String nationCode) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("startTime", starttime);
        params.put("endTime", endtime);
        params.put("nationCode", nationCode);
        String result = HttpConnUtil.doPost(AppConfig.SET_PERIODTITLE, params);
        Log.v("ceshi", "result+" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject jsonObject = new JSONObject(result);
        org.json.JSONArray dataArray = jsonObject.getJSONArray("data");
        TiemTitleData timetitledata = new TiemTitleData();
        ArrayList<TimeTitleBean> timeTitleBeens = new ArrayList<>();
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject datajson = (JSONObject) dataArray.get(i);
            //取该竞拍日期下面的时间段
            org.json.JSONArray valueArray = new org.json.JSONArray(datajson.optString("value"));
            ArrayList<String> values = new ArrayList<>();
            for (int h = 0; h < valueArray.length(); h++) {
                TimeTitleBean timeTitleBean = new TimeTitleBean();
                //服务器时间时间
                timeTitleBean.setNte(datajson.optLong("nte"));
                //取竞拍时期
                timeTitleBean.setTime(datajson.optString("time"));
                String dateandhourstr;
                //当前日+子时间段 string
                dateandhourstr = datajson.optString("time") + " " + (String) valueArray.get(h);
                timeTitleBean.setDateandhourstr(dateandhourstr);
                //当前日+子时间段 long
                long dateandhourl = getStringToDate(dateandhourstr) / 1000;
                //每个前日+子时间段 加两个小时为该时间段可以竞拍的最后时间
                long enddateandhourl = dateandhourl + (2 * 60 * 60);
                //服务器当前long时间
                long ntetime = datajson.optLong("nte");
                timeTitleBean.setValuesstr((String) valueArray.get(h));
                if (ntetime < dateandhourl) {
                    timeTitleBean.setTypestr("未开始");
                } else if (dateandhourl < ntetime && ntetime < enddateandhourl) {
                    timeTitleBean.setTypestr("进行中");
                } else {
                    timeTitleBean.setTypestr("已结束");
                }
                timeTitleBeens.add(timeTitleBean);

            }
            timetitledata.setTimeTitleBeen(timeTitleBeens);
        }
        return timetitledata;
    }

    @Override
    public ArrayList<FlashSaleListItemBean> getPeriodList(String productType, String auctionTime, String nationCode, String page, String page_length) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("productType", productType);
        params.put("auctionTime", auctionTime);
        params.put("nationCode", nationCode);
        params.put("page", page);
        params.put("page_length", page_length);

        String result = HttpConnUtil.doPost(AppConfig.SET_PERIODLIST, params);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject jsonObject = new JSONObject(result);
        org.json.JSONArray datajson = jsonObject.getJSONArray("data");
        ArrayList<FlashSaleListItemBean> limitedAuctionModels = new ArrayList<FlashSaleListItemBean>();
        for (int i = 0; i < datajson.length(); i++) {

            JSONObject dataObj = (JSONObject) datajson.get(i);
            FlashSaleListItemBean limitedAuctionModel = new FlashSaleListItemBean();
            limitedAuctionModel.setApp(dataObj.optString("app"));
            limitedAuctionModel.setNte(dataObj.optLong("nte"));
            limitedAuctionModel.setAped(dataObj.optLong("aped"));
            limitedAuctionModel.setAplg(dataObj.optString("aplg"));
            limitedAuctionModel.setAps(dataObj.optString("aps"));
            limitedAuctionModel.setApsd(dataObj.optLong("apsd"));
            limitedAuctionModel.setPtn(dataObj.optString("ptn"));
            limitedAuctionModel.setMon(dataObj.optString("mon"));
            limitedAuctionModel.setPid(dataObj.optString("pid"));
            limitedAuctionModel.setPna(dataObj.optString("pna"));
            limitedAuctionModel.setPpi(dataObj.optString("ppi"));
            limitedAuctionModel.setApc(dataObj.optString("apc"));
            limitedAuctionModel.setPpr(dataObj.optString("ppr"));
            limitedAuctionModel.setPt(dataObj.optString("pt"));
            limitedAuctionModels.add(limitedAuctionModel);
        }
        return limitedAuctionModels;
    }

    @Override
    public FlashSaleDetailBean getPeriodDetail(String pid, String pt) throws WSError, Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("pid", pid);
        params.put("pt", pt);
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.SET_PERIODDETAIL, params);
        Log.v("ceshi", "result+" + result);
        JSONObject jsonObject = new JSONObject(result);
        String dataJsong = jsonObject.optString("data");
        JSONObject datajson = new JSONObject(dataJsong);
        org.json.JSONArray auctionLogList = datajson.optJSONArray("auctionLogList");

        ArrayList<AuctionBean> auctionlist = new ArrayList<AuctionBean>(
                JSONArray.parseArray(datajson.optString("auctionLogList"), AuctionBean.class));

        FlashSaleDetailBean limitedDetails = FastJsonUtils.getSingleBean(dataJsong, FlashSaleDetailBean.class);
        limitedDetails.setAuctionBeenList(auctionlist);
        return limitedDetails;

    }

    @Override
    public List<AuctionBean> getAuction(String pid, String page, String page_length) throws WSError, Exception {
        List<AuctionBean> auctionList = null;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("pid", pid);
        params.put("page", page);
        params.put("page_length", page_length);
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.GET_PERIODRECORD, params);
        JSONObject jsonObject = new JSONObject(result);
        org.json.JSONArray jsonData = jsonObject.getJSONArray("data");
        auctionList = new ArrayList<>();
        for (int i = 0; i < jsonData.length(); i++) {
            JSONObject jsonObject1 = (JSONObject) jsonData.get(i);
            AuctionBean auctionBean = new AuctionBean();
            auctionBean.setCt(jsonObject1.optString("ct"));
            auctionBean.setNk(jsonObject1.optString("nk"));
            auctionBean.setPpr(jsonObject1.optString("ppr"));
            auctionList.add(auctionBean);
        }

        return auctionList;
    }

    @Override
    public AddPriceBean getAuctionCount(String apr, String pid) throws WSError, Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("pid", pid);
        params.put("apr", apr);
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.GET_AUCTION, params);
        AddPriceBean addPriceBean = new AddPriceBean();
        JSONObject jsonObject = new JSONObject(result);
        String dataJsong = jsonObject.optString("data");
        JSONObject datajson = new JSONObject(dataJsong);
        org.json.JSONArray auctionLogList = datajson.optJSONArray("auctionLogList");

        ArrayList<AuctionBean> auctionlist = new ArrayList<AuctionBean>(
                JSONArray.parseArray(datajson.optString("auctionLogList"), AuctionBean.class));
        addPriceBean.setAuctionBeenList(auctionlist);
        addPriceBean.setApsc(datajson.optString("apsc"));
        return addPriceBean;
    }

    /**
     * 获取商家邀请列表
     *
     * @param page
     * @param page_length
     * @return
     * @throws WSError
     * @throws JSONException
     */
    @Override
    public ArrayList<ShopInfoBean> getShopInviteList(String page, String page_length) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
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
        String result = HttpConnUtil.doPost(AppConfig.SHOPINVITELISTS, params);
        Log.v("ceshi", "***+" + result);
        JSONObject alljson = new JSONObject(result);
        ArrayList<ShopInfoBean> shopSerInfos = new ArrayList<ShopInfoBean>(
                JSONArray.parseArray(alljson.optString("data"), ShopInfoBean.class));
        return shopSerInfos;
    }

    /**
     * 获取邀请页面信息
     *
     * @param sid 邀请店铺id
     * @return
     * @throws WSError
     * @throws JSONException
     */
    @Override
    public ShopInviteDetailBean getShopInvitDetail(String sid) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("sid", sid);
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            Log.v("ceshi", "IOException");
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        Log.v("ceshi", "getShopInviteDetail4");
        String result = HttpConnUtil.doPost(AppConfig.SHOPINVITE_LISTS_DETAIL, params);
        Log.v("ceshi", "***+" + result);
        JSONObject alljson = new JSONObject(result);
        ShopInviteDetailBean shopInviteDetailBean = new ShopInviteDetailBean();
        JSONObject datajson = new JSONObject(alljson.optString("data"));
        shopInviteDetailBean.setMobile(datajson.optString("mobile"));
        shopInviteDetailBean.setIntg(datajson.optString("intg"));
        shopInviteDetailBean.setVip_level_name(datajson.optString("vip_level_name"));
        if (TextUtils.isEmpty(datajson.optString("code"))) {
            return null;
        }
        shopInviteDetailBean.setCode(datajson.optString("code"));
        JSONObject user_infojson = new JSONObject(datajson.optString("user_info"));
        UserInfoData userInfoData = new UserInfoData();
        userInfoData.setReal_name(user_infojson.optString("real_name"));
        userInfoData.setGender(user_infojson.optString("gender"));
        userInfoData.setBirthday(user_infojson.optString("birthday"));
        userInfoData.setOccupation(user_infojson.optString("occupation"));
        userInfoData.setEmail(user_infojson.optString("email"));
        userInfoData.setAddress(user_infojson.optString("address"));
        shopInviteDetailBean.setUserInfoData(userInfoData);
        return shopInviteDetailBean;
    }

    /**
     * 填写邀请接口
     *
     * @param addShopInviteUserInfo
     * @return
     * @throws WSError
     * @throws JSONException
     */
    @Override
    public boolean AddShopInvite(AddShopInviteUserInfo addShopInviteUserInfo) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("sid", addShopInviteUserInfo.getSid());
        params.put("user_name", addShopInviteUserInfo.getUser_name());
        params.put("gen", addShopInviteUserInfo.getGen());
        params.put("birthday", addShopInviteUserInfo.getBirthday());
        params.put("occupation", addShopInviteUserInfo.getOccupation());
        params.put("mobile", addShopInviteUserInfo.getMobile());
        params.put("code", addShopInviteUserInfo.getCode());
        params.put("email", addShopInviteUserInfo.getEmail());
        params.put("address", addShopInviteUserInfo.getAddress());

        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            Log.v("ceshi", "IOException");
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        Log.v("ceshi", "getShopInviteDetail4");
        String result = HttpConnUtil.doPost(AppConfig.SHOPINVITE_COMMIT, params);
        Log.v("ceshi", "***+" + result);
        JSONObject alljson = new JSONObject(result);
        String datastr = alljson.optString("data");
        if (TextUtils.isEmpty(datastr)) {
            return false;
        } else {
            if (datastr.equals("true")) {
                return true;
            } else {
                return false;
            }
        }

    }

    /**
     * 获取该商品的换购历史
     *
     * @param pid 商品id
     * @return
     * @throws WSError
     * @throws JSONException
     */
    @Override
    public ArrayList<SaleHistoryBean> getSaleHistoryList(String pid) throws WSError, JSONException {
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
        String result = HttpConnUtil.doPost(AppConfig.SALE_HISTORY, params);
        Log.v("ceshi", "***+" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject alljson = new JSONObject(result);
        ArrayList<SaleHistoryBean> saleHistoryBeens = new ArrayList<SaleHistoryBean>(
                JSONArray.parseArray(alljson.optString("data"), SaleHistoryBean.class));
        return saleHistoryBeens;
    }

    /**
     * 获取热门推荐城市
     *
     * @param nationCode
     * @return
     * @throws WSError
     * @throws JSONException
     */
    @Override
    public ArrayList<HotCityBean> getHotcitys(String nationCode) throws WSError, JSONException {
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
        String result = HttpConnUtil.doPost(AppConfig.GET_HOT_CITYS, params);
        Log.v("ceshi", "***+" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject alljson = new JSONObject(result);
        ArrayList<HotCityBean> hotCityBeanArrayList = new ArrayList<HotCityBean>(
                JSONArray.parseArray(alljson.optString("data"), HotCityBean.class));
        return hotCityBeanArrayList;
    }

    /**
     * 提交用户行为记录
     *
     * @param type 1 商场 2 店铺
     * @param tid  商场ID 或者店铺ID
     * @param tag  1 详情浏览  2 来电次数
     * @return
     * @throws WSError
     * @throws JSONException
     */
    @Override
    public boolean putUserMessage(String type, String tid, String tag) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("type", type);
        params.put("tid", tid);
        params.put("tag", tag);
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.PUT_USER_MESSAGE, params);
        Log.v("ceshi", "***+" + result);
        return true;
    }

    @Override
    public Markbean CusttomMst(String rid, String mark) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("rid", rid);
        params.put("mark", mark);
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.CS_MST, params);
        Markbean markbean = new Markbean();
        JSONObject resultjson = new JSONObject(result);
//        String data = resultjson.optString("data");
//        JSONObject datajson = new JSONObject(data);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        org.json.JSONArray dataarray = resultjson.optJSONArray("data");
        if (dataarray != null) {
            if (dataarray.length() > 0) {
                JSONObject datazijson = (JSONObject) resultjson.getJSONArray("data").get(0);
                markbean.setMst(datazijson.optString("mst"));
                markbean.setMc(datazijson.optString("mc"));
            } else {
                return null;
            }
        } else {
            return null;
        }

        Log.v("ceshi", "***+" + result);
        return markbean;
    }

    @Override
    public ArrayList<CityBean> upDb(String nation) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("nationCode", nation);
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.UPDB, params);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject alljson = new JSONObject(result);
        ArrayList<CityBean> updbBeanArrayList = new ArrayList<>();
        org.json.JSONArray cityjsonarray = alljson.optJSONArray("data");
        if (cityjsonarray != null & cityjsonarray.length() > 0) {
            CityBean cityBean0 = new CityBean();
            cityBean0.setNn("全部");
            cityBean0.setId("");
            updbBeanArrayList.add(cityBean0);
            for (int i = 0; i < alljson.optJSONArray("data").length(); i++) {
                JSONObject cityjson = (JSONObject) cityjsonarray.get(i);
                CityBean cityBean = new CityBean();
                cityBean.setId(cityjson.optString("nid"));
                cityBean.setNn(cityjson.optString("name"));
                updbBeanArrayList.add(cityBean);
            }
        } else {
            CityBean cityBean0 = new CityBean();
            cityBean0.setNn("全部");
            cityBean0.setId("");
            updbBeanArrayList.add(cityBean0);
        }

        return updbBeanArrayList;
    }

    @Override
    public ArrayList<CityBean> upCityLocation() throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.LOCATION_NATION, params);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject alljson = new JSONObject(result);
        String datastr = alljson.optString("data");
        JSONObject datajson = new JSONObject(datastr);
        String update = datajson.optString("update");
        if (!"true".equals(update)) {
            return null;
        }
        String istype = "open";
        if ("open".equals(datajson.optString("option"))) {
            istype = "open";
        } else {
            istype = "close";
        }
//        String nation = datajson.optString("nation");
//        if(TextUtils.isEmpty(nation)){
//            return null;
//        }
//        JSONObject nationjson = new JSONObject(nation);
        ArrayList<CityBean> updbBeanArrayList = new ArrayList<>();
        org.json.JSONArray cityjsonarray = datajson.optJSONArray("nation");
        if (cityjsonarray != null) {
            Log.v("lsq", "cityjsonarray.length()+" + cityjsonarray.length());
            if (cityjsonarray.length() > 0) {
                for (int i = 0; i < cityjsonarray.length(); i++) {
                    JSONObject cityjson = (JSONObject) cityjsonarray.get(i);
                    CityBean cityBean = new CityBean();
                    cityBean.setCid(cityjson.optString("nid"));
                    cityBean.setIstype(istype);
                    updbBeanArrayList.add(cityBean);
                }
            } else {
                return null;
            }

        } else {
            return null;
        }
        return updbBeanArrayList;
    }

    @Override
    public boolean MessAgeSet(String type, String system) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        //uid=&msg={"mer":1}     商家消息  mer   0 false 1 true     系统  sys   0 false 1 true
        String msgstr = "{\"" + type + "\":" + system + "}";
        params.put("msg", msgstr);
        try {
            params.put("sign", Tool.getSignature(params));
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.SETMESSAGE, params);
        Log.v("ceshi", "result" + result);
        if (TextUtils.isEmpty(result)) {
            return false;
        }
        if (result.contains("true")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ArrayList<StoreDetailInfo> getStoreList(String nationCode, String page, String lgt, String lat) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("nationCode", nationCode);
        params.put("page", page);
        params.put("lgt", lgt);
        params.put("lat", lat);
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.LOOKSTORELIST, params);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        ArrayList<StoreDetailInfo> storeDetailInfos = new ArrayList<>();
        JSONObject alljson = new JSONObject(result);
        org.json.JSONArray dataarray = alljson.optJSONArray("data");
        if (dataarray != null & dataarray.length() > 0) {

            for (int i = 0; i < alljson.optJSONArray("data").length(); i++) {
                JSONObject datajson = (JSONObject) dataarray.get(i);
                StoreDetailInfo storeDetailInfo = new StoreDetailInfo();
                JSONObject infojson = new JSONObject(datajson.optString("info"));
                storeDetailInfo.setS_logo(infojson.optString("logo"));
                storeDetailInfo.setS_mid(infojson.optString("mid"));
                storeDetailInfo.setS_name(infojson.optString("name"));
                if (infojson.optJSONArray("contact") != null) {
                    if (infojson.optJSONArray("contact").length() > 0) {
                        storeDetailInfo.setS_tel(infojson.optJSONArray("contact").get(0).toString() + "");
                    }
                }
                storeDetailInfo.setS_open_time(infojson.optString("open_time"));
                storeDetailInfo.setS_mba(infojson.optString("mba"));
                if (!TextUtils.isEmpty(datajson.optString("news"))) {
                    JSONObject newsjson = new JSONObject(datajson.optString("news"));
                    storeDetailInfo.setS_mnid(newsjson.optString("mnid"));
                    storeDetailInfo.setS_mnti(newsjson.optString("mnti"));
                }
                storeDetailInfos.add(storeDetailInfo);
            }
        }


        return storeDetailInfos;
    }

    @Override
    public ArrayList<PoitGoodBean> getHomeSaleList(String nationCode, String prtag, String page) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("nationCode", nationCode);
        params.put("page", page);
        params.put("prtag", "RC_PRODUCT_GOOD");
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.HOMESALELIST, params);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject alljson = new JSONObject(result);
        ArrayList<PoitGoodBean> poitGoodBeanArrayList = new ArrayList<PoitGoodBean>(
                JSONArray.parseArray(alljson.optString("data"), PoitGoodBean.class));
        return poitGoodBeanArrayList;
    }

    @Override
    public ArrayList<ShopInfoBean> getHomeCommendShop(String nationCode, String page) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("nationCode", nationCode);
        params.put("page", page);
        params.put("shopRecommendTag", "RC_SHOP_GOODS");
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.HOMECOMMDEDLIST, params);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject alljson = new JSONObject(result);
        JSONObject proshopjson = alljson.getJSONArray("data").getJSONObject(0);
        if (alljson == null) {
            return null;
        }
        if (TextUtils.isEmpty(proshopjson.optString("RC_SHOP_GOODS"))) {
            return null;
        }
        ArrayList<ShopInfoBean> poitGoodBeanArrayList = new ArrayList<ShopInfoBean>(
                JSONArray.parseArray(proshopjson.optString("RC_SHOP_GOODS"), ShopInfoBean.class));

        return poitGoodBeanArrayList;
    }

    @Override
    public List<Adimg> getNewHomeBanner(String nationCode) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("adTag", "SY_MR_AD_BANNER");
        params.put("nationCode", nationCode);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.NEWHOMEBANNER, params);
        Log.v("ceshi", "resultIMG+" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
//        String msg = ResolveResponse.resolveData(result).toString();
        JSONObject json = new JSONObject(result);
        if (TextUtils.isEmpty(json.getJSONArray("data").toString())) {
            return null;
        }
        Log.v("test", "lengtht+" + json.getJSONArray("data").length());

        if (json.getJSONArray("data").length() == 0) {
            return null;
        } else {
            ArrayList<Adimg> list = new ArrayList<Adimg>(
                    JSONArray.parseArray(json.optString("data"), Adimg.class));
            return list;
        }
    }

    @Override
    public CommentInfo getCommentDetail(String id) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("order", id);
        String strstr = null;
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.COMMENTDETAIL, params);
        Log.v("ceshi", "resultIMG+" + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
//        String msg = ResolveResponse.resolveData(result).toString();
        JSONObject alljson = new JSONObject(result);
        if (TextUtils.isEmpty(alljson.optString("data").toString())) {
            return null;
        }
        JSONObject dainfojson = new JSONObject(alljson.optString("data"));
        CommentInfo commentInfo = new CommentInfo();
        commentInfo.setCreate_time(dainfojson.optString("create_time"));
        commentInfo.setText(dainfojson.optString("text"));
        commentInfo.setType(dainfojson.optString("type"));
        if (!TextUtils.isEmpty(dainfojson.optString("reply"))) {
            JSONObject replyjson = new JSONObject(dainfojson.optString("reply"));
            commentInfo.setReply_text(replyjson.optString("text"));
            commentInfo.setReply_time(replyjson.optString("reply_time"));
        } else {
            commentInfo.setReply_time("");
        }
        org.json.JSONArray imgsjson = dainfojson.getJSONArray("imgs");
        ArrayList<String> imgs = new ArrayList<String>();
        if (imgsjson != null & imgsjson.length() > 0) {
            for (int j = 0; j < imgsjson.length(); j++) {
                Log.v("lsq", "length()++" + dainfojson.getJSONArray("imgs").get(j) + "");
                String imgurl = dainfojson.getJSONArray("imgs").get(j) + "";
                imgs.add(imgurl);
            }
        }
        commentInfo.setImgs(imgs);
        return commentInfo;

    }

    @Override
    public ArrayList<CityBean> getCouponTypeList(String nationCode) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("nationCode", nationCode);
        params.put("tag", "COUPON");
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.GETTYPELIST, params);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject alljson = new JSONObject(result);
        ArrayList<CityBean> updbBeanArrayList = new ArrayList<>();
        if (TextUtils.isEmpty(alljson.optString("data"))) {
            return null;
        } else {
            org.json.JSONArray cityjsonarray = alljson.optJSONArray("data");
            if (cityjsonarray.length() > 0) {
                for (int i = 0; i < cityjsonarray.length(); i++) {
                    JSONObject cityjson = (JSONObject) cityjsonarray.get(i);
                    CityBean cityBean = new CityBean();
                    cityBean.setMoid(cityjson.optString("moid"));
                    cityBean.setNn(cityjson.optString("name"));
                    updbBeanArrayList.add(cityBean);
                }
            } else {
                return null;
            }
        }


//        else {
//            CityBean cityBean0 = new CityBean();
//            cityBean0.setNn("全部");
//            cityBean0.setId("");
//            updbBeanArrayList.add(cityBean0);
//        }

        return updbBeanArrayList;
    }

    @Override
    public ArrayList<MyArrayListCityBean> getArroutTypeList(String nationCode) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("nationCode", nationCode);
        params.put("tag", "AROUND_SHOP");
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.GETTYPELIST, params);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject alljson = new JSONObject(result);
        if (TextUtils.isEmpty(alljson.optString("data"))) {
            return null;
        }
        org.json.JSONArray dataarray = alljson.optJSONArray("data");
        if (dataarray == null) {
            return null;
        } else {
            if (dataarray.length() == 0) {
                return null;
            }
        }
        ArrayList<MyArrayListCityBean> myArrayListCityBeen = new ArrayList<>();
        if (dataarray != null & dataarray.length() > 0) {
            for (int i = 0; i < dataarray.length(); i++) {
                JSONObject tagnamejson = (JSONObject) dataarray.get(i);
                MyArrayListCityBean myArrayListCityBean = new MyArrayListCityBean();
                myArrayListCityBean.setTagName(tagnamejson.optString("tagName"));
                ArrayList<ArrountitemBean> arrountitemBeens = new ArrayList<>();
                org.json.JSONArray itemsarray = tagnamejson.optJSONArray("items");
                if (itemsarray != null) {
                    if (itemsarray.length() > 0) {
                        for (int j = 0; j < itemsarray.length(); j++) {
                            ArrountitemBean arrountitemBean = new ArrountitemBean();
                            JSONObject itemjson = (JSONObject) itemsarray.get(j);
                            arrountitemBean.setName(itemjson.optString("name"));
                            arrountitemBean.setTagName(itemjson.optString("tagName"));
                            arrountitemBean.setTag(itemjson.optString("tag"));
                            arrountitemBean.setMoid(itemjson.optString("moid"));
                            arrountitemBeens.add(arrountitemBean);
                        }
                    }
                }
                myArrayListCityBean.setArrountitemBeens(arrountitemBeens);
                myArrayListCityBeen.add(myArrayListCityBean);
            }
        }
        if (myArrayListCityBeen == null) {
            return null;
        } else {
            if (myArrayListCityBeen.size() == 0) {
                return null;
            }
        }

        return myArrayListCityBeen;
    }

    @Override
    public ArrayList<MyArrayListCityBean> getSaleTypeList(String nationCode) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("nationCode", nationCode);
        params.put("tag", "EXCHANGE_PRODUCT");
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.GETTYPELIST, params);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject alljson = new JSONObject(result);
        if (TextUtils.isEmpty(alljson.optString("data"))) {
            return null;
        }
        org.json.JSONArray dataarray = alljson.optJSONArray("data");
        if (dataarray == null) {
            return null;
        } else {
            if (dataarray.length() == 0) {
                return null;
            }
        }
        ArrayList<MyArrayListCityBean> myArrayListCityBeen = new ArrayList<>();
        if (dataarray != null & dataarray.length() > 0) {
            for (int i = 0; i < dataarray.length(); i++) {
                JSONObject tagnamejson = (JSONObject) dataarray.get(i);
                MyArrayListCityBean myArrayListCityBean = new MyArrayListCityBean();
                myArrayListCityBean.setTagName(tagnamejson.optString("tagName"));
                myArrayListCityBean.setMoid(tagnamejson.optString("moid"));
                ArrayList<ArrountitemBean> arrountitemBeens = new ArrayList<>();
                if (!TextUtils.isEmpty(tagnamejson.optString("items"))) {
                    if (tagnamejson.optJSONArray("items") != null) {
                        org.json.JSONArray itemsarray = tagnamejson.optJSONArray("items");
                        if (itemsarray != null) {
                            if (itemsarray.length() > 0) {
                                for (int j = 0; j < itemsarray.length(); j++) {
                                    ArrountitemBean arrountitemBean = new ArrountitemBean();
                                    JSONObject itemjson = (JSONObject) itemsarray.get(j);
                                    arrountitemBean.setName(itemjson.optString("name"));
                                    arrountitemBean.setTagName(itemjson.optString("tagName"));
                                    arrountitemBean.setTag(itemjson.optString("tag"));
                                    arrountitemBean.setMoid(itemjson.optString("moid"));
                                    arrountitemBeens.add(arrountitemBean);
                                }
                            }
                        }
                    }
                }

                myArrayListCityBean.setArrountitemBeens(arrountitemBeens);
                myArrayListCityBeen.add(myArrayListCityBean);
            }
        }
        if (myArrayListCityBeen == null) {
            return null;
        } else {
            if (myArrayListCityBeen.size() == 0) {
                return null;
            }
        }

        return myArrayListCityBeen;
    }

    @Override
    public ArrayList<ShopInfoBean> getArountHotdShop(String nationCode, String page, String tag) throws WSError, JSONException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("timestamp", Tool.getUninxTime());
        params.put("ip", AppConfig.ipstr);
        params.put("channel", AppConfig.channel);
        params.put("nationCode", nationCode);
        params.put("page", page);
        params.put("shopRecommendTag", tag);
        try {
            params.put("sign", Tool.getSignature(params));

        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("token", AppConfig.usertoken);
        params.put("uid", AppConfig.userid);
        String result = HttpConnUtil.doPost(AppConfig.HOMECOMMDEDLIST, params);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        JSONObject alljson = new JSONObject(result);
        JSONObject proshopjson = alljson.getJSONArray("data").getJSONObject(0);
        if (alljson == null) {
            return null;
        }
        if (TextUtils.isEmpty(proshopjson.optString("RC_AROUND_SHOP"))) {
            return null;
        }
        ArrayList<ShopInfoBean> poitGoodBeanArrayList = new ArrayList<ShopInfoBean>(
                JSONArray.parseArray(proshopjson.optString("RC_AROUND_SHOP"), ShopInfoBean.class));
        if (poitGoodBeanArrayList == null) {
            return null;
        } else {
            if (poitGoodBeanArrayList.size() > 0) {
                return poitGoodBeanArrayList;
            } else {
                return null;
            }
        }

    }
}
