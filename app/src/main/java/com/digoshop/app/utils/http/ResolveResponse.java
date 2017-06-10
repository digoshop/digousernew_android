package com.digoshop.app.utils.http;

import android.util.Log;

import com.digoshop.app.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * �������ص�JSON����
 * 
 * @company �����ֺ������Ϣ�������޹�˾
 * @author �ڹ���
 * @date 2012-9-3
 */

public class ResolveResponse {


	public static Object resolveData(String resStr) throws WSError, JSONException {
		if (resStr != null && !resStr.equals("")) {
			JSONObject json = new JSONObject(resStr);
			if (json != null) {
				if (!json.isNull("data")) {
					String data = json.getString("data");
					final char[] strChar = data.substring(0, 1).toCharArray();
					final char firstChar = strChar[0];
					if (firstChar == '[') {
						data = data.substring(1, data.length() - 1);
					}
					return data;
				} else {
					Logger.e("", "数据为空111");
					Logger.e("", "数据为空");
					//错误信息
//					String data = json.getString("e");
//					JSONObject jsonObject=new JSONObject(data);
//					int code=jsonObject.getInt("code");
//					WSError ws=new WSError();
//					ws.setMessage(code+"");
//					throw ws;
				}
			}
		} else
			Logger.i("ceshi", "json为空");
		throw new WSError();

	}

	public static Object resolveDesc(String resStr) throws WSError{
		if (resStr != null && !resStr.equals("")) {
			JSONObject json;
			try {
				json = new JSONObject(resStr);
			
			if (json != null) {
				if (!json.isNull("e")) {
					String datae = json.getString("e");
					if (datae.equals("failed")) {
						Log.v("ceshi","db failed");;
						return "db failed";
					} else {
						//Log.v("ceshi","db failed"+json.getString("data"));;
						if (!json.isNull("data")) {
							String data = json.getString("data");
							return data;
						} else {
							Logger.e("", "数据为空");
						}

					}

				} else {
					Logger.e("", "数据为空");
				}

			}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
			Logger.i("ceshi", "json为空");
		throw new WSError();
	}


}
