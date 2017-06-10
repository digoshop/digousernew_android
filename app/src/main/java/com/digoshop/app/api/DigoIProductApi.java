package com.digoshop.app.api;

import com.digoshop.app.api.impl.DigoIProductApiImpl;
import com.digoshop.app.module.arrountshops.model.ShopInfoBean;
import com.digoshop.app.module.home.model.ActivityDetailBean;
import com.digoshop.app.module.looksales.model.ExChangeDetail_Data;
import com.digoshop.app.utils.http.WSError;

import org.json.JSONException;

import java.util.List;

public interface DigoIProductApi {
	/**
	 * 周边商铺
	 * 
	 * @param nationCode
	 *            城市code
	 * @param operateType
	 *            1商品 2 服务 全部空
	 * @param regionId
	 *            区县id 全部是空
	 * @param mid
	 *            业态商圈id
	 * @param lat
	 *            经纬度
	 * @param lgt
	 *            经纬度
	 * @param page 页码
	 * @param page_length  页长
	 * @param  sort排序 0 离我最近  1 好评优先 
	 * @return
	 * @throws WSError
	 * @throws JSONException
	 */
	public List<ShopInfoBean> ArountShops(String moid,String sort,String nationCode, String operateType, String regionId, String mid, String lat,
			String lgt, String page, String page_length) throws WSError, JSONException;

	/**
	 * 获取兑换商品详情
	 * 
	 * @param pid
	 *            商品id
	 * @return
	 * @throws WSError
	 * @throws JSONException
	 */
	public ExChangeDetail_Data get_exchange_detail(String pid,String id) throws WSError, JSONException;

	/**
	 * 确认兑换
	 * 
	 * @param pid
	 *            商品id
	 * @throws WSError
	 * @throws JSONException
	 */
	public String Approve_Exchange(String pid) throws WSError, JSONException;

    public String getCustomNum (String nationCode)throws WSError, JSONException;
}
