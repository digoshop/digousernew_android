package com.digoshop.app.utils.http;

import android.text.TextUtils;
import android.util.Log;

import com.digoshop.BuildConfig;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.Tool;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.digoshop.app.utils.Tool.getNetWifi;

/**
 * �������ӹ���
 *
 * @author ̷��ǿ
 * @date 2012-7-25
 * @company �����ֺ������Ϣ�������޹�˾
 */
public class HttpConnUtil {

    private static final String tag = HttpConnUtil.class.getSimpleName();
    /**
     * Cache for most recent request
     */
    private static RequestCache requestCache = null;
    static long time = 0;

    /**
     * ��ӡPOST�����ַ
     *
     * @param url
     * @param params
     */
    private static void printPosRequestUrl(String url, HashMap<String, String> params) {
        if (AppConfig.IS_PRINT_LOG) {
            for (Iterator<String> iterator = params.keySet().iterator(); iterator.hasNext(); ) {
                String key = iterator.next();
                if (TextUtils.isEmpty(params.get(key))) {
                    if (key != null)
                        url += key + "=" + "" + "&";
                } else {
                    if (key != null)
                        url += key + "=" + params.get(key).toString() + "&";
                }

            }
            Log.v("ceshi", "接口get+" + url.substring(0, url.length() - 1));
            //System.out.println("请求get+" + url.substring(0, url.length() - 1));
        }
    }

    /**
     * ִ��GET���� (��ȡ��������)
     *
     * @param url ����URL
     * @return ���ؽ���ַ�����ʽ
     * @throws WSError
     */
    public static String doGet(String url) throws WSError {

        if (BuildConfig.DEBUG)
            System.out.println("doGet:" + url);
        String data = null;
        if (requestCache != null) {
            data = (String) requestCache.get(url);
            if (data != null) {
                return data;
            }
        }

        // initialize HTTP GET request objects
        HttpParams httpParameters = new BasicHttpParams();// Set the timeout in
        // milliseconds
        // until a
        // connection is
        // established.
        HttpConnectionParams.setConnectionTimeout(httpParameters, AppConfig.HTTP_TIMEOUT_CONNECTION);// Set
        // the
        // default
        // socket
        // timeout
        // (SO_TIMEOUT)
        // //
        // in
        // milliseconds
        // which
        // is
        // the
        // timeout
        // for
        // waiting
        // for
        // data.
        HttpConnectionParams.setSoTimeout(httpParameters, AppConfig.HTTP_SOTIMOUT);
        HttpClient httpClient = new DefaultHttpClient(httpParameters);
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse;
        int code = 0;
        try {
            // execute request
            try {
                httpResponse = httpClient.execute(httpGet);
            } catch (UnknownHostException e) {
                WSError wsError = new WSError();
                wsError.setMessage(e.getLocalizedMessage());
                e.printStackTrace();
                throw wsError;
            } catch (SocketException e) {
                WSError wsError = new WSError();
                wsError.setMessage(e.getLocalizedMessage());
                e.printStackTrace();
                throw wsError;
            }

            // request data��������
            HttpEntity httpEntity = httpResponse.getEntity();

            if (httpEntity != null) {
                InputStream inputStream = httpEntity.getContent();
                data = convertStreamToString(inputStream);


                JSONObject jsonObject = new JSONObject(data);
                Log.i("zzrdata", jsonObject + "");
                String jsonerr = jsonObject.getString("e");
                Log.i("zzrdata", jsonerr + "");
                JSONObject jsonerror = new JSONObject(jsonerr);

                code = jsonerror.getInt("code");


                JSONArray jsonData = jsonObject.getJSONArray("data");
                if (jsonData.length() == 0) {
                    code = -99;
                }

                // cache the result
                if (requestCache != null) {
                    requestCache.put(url, data);
                }
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (code == 0) {
            return data;
        }
        WSError wsError = new WSError();
        wsError.setMessage(code + "");
        Log.i("zzrcode", code + "");
        throw wsError;

    }

    /**
     * ִ��GET������ȡ���ݣ�����������
     *
     * @param url
     * @return ���� InputStream
     * @throws WSError
     */
    public static InputStream doGetStream(String url) throws WSError {
        InputStream data = null;
        // initialize HTTP GET request objects
        HttpParams httpParameters = new BasicHttpParams();// Set the timeout in
        // milliseconds
        // until a
        // connection is
        // established.
        HttpConnectionParams.setConnectionTimeout(httpParameters, AppConfig.HTTP_TIMEOUT_CONNECTION);// Set
        // the
        // default
        // socket
        // timeout
        // (SO_TIMEOUT)
        // //
        // in
        // milliseconds
        // which
        // is
        // the
        // timeout
        // for
        // waiting
        // for
        // data.
        HttpConnectionParams.setSoTimeout(httpParameters, AppConfig.HTTP_SOTIMOUT);
        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse;

        try {
            // execute request
            try {
                httpResponse = httpClient.execute(httpGet);
            } catch (UnknownHostException e) {
                WSError wsError = new WSError();
                wsError.setMessage(e.getLocalizedMessage());
                throw wsError;
            } catch (SocketException e) {
                WSError wsError = new WSError();
                wsError.setMessage(e.getLocalizedMessage());
                throw wsError;
            }

            // request data
            HttpEntity httpEntity = httpResponse.getEntity();

            if (httpEntity != null) {
                data = httpEntity.getContent();
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    /**
     * �����緵�ص�������ת��Ϊ�ַ���
     *
     * @param is
     * @return
     */
    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    public static void setRequestCache(RequestCache requestCache) {
        HttpConnUtil.requestCache = requestCache;
    }

    /**
     * ִ��POST���� ���ύJson ����
     *
     * @param url     url
     * @param content josn ��ʽ�� �ַ�������
     * @return
     */
    public static String doPostJson(String url, String content) {

        String data = null;
        HttpParams httpParameters = new BasicHttpParams();// Set the timeout in
        // milliseconds
        // until a
        // connection is
        // established.
        HttpConnectionParams.setConnectionTimeout(httpParameters, AppConfig.HTTP_TIMEOUT_CONNECTION);// Set
        // the
        // default
        // socket
        // timeout
        // (SO_TIMEOUT)
        // //
        // in
        // milliseconds
        // which
        // is
        // the
        // timeout
        // for
        // waiting
        // for
        // data.
        HttpConnectionParams.setSoTimeout(httpParameters, AppConfig.HTTP_SOTIMOUT);
        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        HttpPost httpPost = new HttpPost(url);
        // ���httpͷ��Ϣ
        httpPost.addHeader("Content-Type", "application/json");
        try {
            httpPost.setEntity(new StringEntity(content, "UTF-8"));
            HttpResponse httpResponse = httpClient.execute(httpPost);

            // request data
            HttpEntity httpEntity = httpResponse.getEntity();

            if (httpEntity != null) {
                InputStream inputStream = httpEntity.getContent();
                data = convertStreamToString(inputStream);

            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return data;
    }

    /**
     * ִ��POST���� �ύ���ݸ�������
     *
     * @param url
     * @param params ���ݲ���
     * @return
     * @throws WSError
     */
    public static String doPost(String url, HashMap<String, String> params) throws WSError {
        //if(AppConfig.GETSHOPDETAILNEW.equals(url)){
            printPosRequestUrl(url, params);
       // }
        if (!getNetWifi()) {
            WSError ws = new WSError();
            ws.setMessage("A502");
            throw ws;
        }
        String data = null;
        String jsondata = null;
        String code = "0";
        String desc = "";
        /**
         * Post ���д��ݲ��������� NameValuePair[] ����洢
         */
        List<NameValuePair> _params = new ArrayList<NameValuePair>();

        Iterator<?> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
            Object key = entry.getKey();
            Object value = Tool.getisEmpty(entry.getValue()+"");
            _params.add(new BasicNameValuePair("" + key, "" + value));
        }
        HttpParams httpParameters = new BasicHttpParams();// Set the timeout in
        HttpConnectionParams.setConnectionTimeout(httpParameters, AppConfig.HTTP_TIMEOUT_CONNECTION);// Set
        HttpConnectionParams.setSoTimeout(httpParameters, AppConfig.HTTP_SOTIMOUT);
        HttpClient httpClient = new DefaultHttpClient(httpParameters);
        HttpPost httppost = new HttpPost(url);
        try {
            // ��Ӳ���
            httppost.setEntity(new UrlEncodedFormEntity(_params, HTTP.UTF_8));
            // ִ��
            HttpResponse httpResponse = httpClient.execute(httppost);
            // ��������

            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                // ȡ����Ӧ�ַ���
                data = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
                JSONObject jsonObject = new JSONObject(data);
                String jsonerr = jsonObject.optString("e");
                JSONObject jsonerror = new JSONObject(jsonerr);
                code = jsonerror.optString("code");
                if ("-101".equals(code)) {
                    WSError ws = new WSError();
                    ws.setMessage(code + "");
                    throw ws;
                } else if ("-110".equals(code)) {
                    WSError ws = new WSError();
                    ws.setMessage(code + "");
                    throw ws;
                } else if ("-112".equals(code)) {
                    WSError ws = new WSError();
                    ws.setMessage(code + "");
                    throw ws;
                } else if ("-114".equals(code)) {
                    WSError ws = new WSError();
                    ws.setMessage(code + "");
                    throw ws;
                } else if ("-97".equals(code)) {
                    WSError ws = new WSError();
                    ws.setMessage(code + "");
                    throw ws;
                } else if ("-96".equals(code)) {
                    WSError ws = new WSError();
                    ws.setMessage(code + "");
                    throw ws;
                } else if ("-113".equals(code)) {
                    WSError ws = new WSError();
                    ws.setMessage(code + "");
                    throw ws;
                } else if ("-109".equals(code)) {
                    WSError ws = new WSError();
                    ws.setMessage(code + "");
                    throw ws;
                } else if ("-105".equals(code)) {
                    WSError ws = new WSError();
                    ws.setMessage(code + "");
                    throw ws;
                } else if ("-133".equals(code)) {
                    WSError ws = new WSError();
                    ws.setMessage(code + "");
                    throw ws;
                } else if ("-1512".equals(code)) {
                    WSError ws = new WSError();
                    ws.setMessage(code + "");
                    throw ws;//-1513
                } else if ("-1513".equals(code)) {
                    WSError ws = new WSError();
                    ws.setMessage(code + "");
                    throw ws;//-1513
                } else if ("-112".equals(code)) {
                    WSError ws = new WSError();
                    ws.setMessage(code + "");
                    throw ws;//-1513
                } else if ("-111".equals(code)) {
                    WSError ws = new WSError();
                    ws.setMessage(code + "");
                    throw ws;//-1513
                } else if ("-1302".equals(code)) {
                    WSError ws = new WSError();
                    ws.setMessage(code + "");
                    throw ws;//-1513
                }
                desc = jsonerror.optString("desc");
                String message = jsonerror.optString("message");
                Log.i("zzrdata", "code+" + code);
                boolean ddd = code.equals("0") && desc.equals("success");
                Log.v("ceshi", "dddboolean" + ddd);
                if (code.equals("0") && desc.equals("success")) {
                    code = "0";
                } else {
                    Log.v("ceshi", "111111111111+" + code);
                    //解决一种情况 提交定制返回积分不足
//					if(code.contains("1513")){
//						WSError ws=new WSError();
//						ws.setMessage("1513");
//						throw ws;
//					}
                    String datastr = jsonObject.optString("data");
                    if (datastr == null || "".equals(datastr) | TextUtils.isEmpty(datastr)) {
                        code = "-99";
                    } else {
                        String jsonDatastr = jsonObject.optString("data");
                        if (jsonDatastr == null || "".equals(jsonDatastr)) {
                            code = "-99";

                        }
                    }
                }


            }

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("niao", code + "");
        if ("0".equals(code)) {
            return data;
        }
        WSError ws = new WSError();
        ws.setMessage(code + "");
        throw ws;

    }

    /**
     * ִ��Delete����
     *
     * @param url ����URL
     * @return ���ؽ���ַ�����ʽ
     * @throws WSError
     */
    public static String doDelete(String url) throws WSError {
        String data = null;

        // initialize HTTP GET request objects
        HttpParams httpParameters = new BasicHttpParams();// Set the timeout in
        // milliseconds
        // until a
        // connection is
        // established.
        HttpConnectionParams.setConnectionTimeout(httpParameters, AppConfig.HTTP_TIMEOUT_CONNECTION);// Set
        // the
        // default
        // socket
        // timeout
        // (SO_TIMEOUT)
        // //
        // in
        // milliseconds
        // which
        // is
        // the
        // timeout
        // for
        // waiting
        // for
        // data.
        HttpConnectionParams.setSoTimeout(httpParameters, AppConfig.HTTP_SOTIMOUT);
        HttpClient httpClient = new DefaultHttpClient(httpParameters);
        HttpDelete httpDelete = new HttpDelete(url);
        HttpResponse httpResponse;

        try {
            // execute request
            try {
                httpResponse = httpClient.execute(httpDelete);
            } catch (UnknownHostException e) {
                WSError wsError = new WSError();
                wsError.setMessage(e.getLocalizedMessage());
                throw wsError;
            } catch (SocketException e) {
                WSError wsError = new WSError();
                wsError.setMessage(e.getLocalizedMessage());
                throw wsError;
            }
            // request data��������
            HttpEntity httpEntity = httpResponse.getEntity();

            if (httpEntity != null) {
                InputStream inputStream = httpEntity.getContent();
                data = convertStreamToString(inputStream);
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            WSError wsError = new WSError();
            wsError.setMessage(e.getLocalizedMessage());
            throw wsError;
        }

        return data;
    }

    /**
     * �ϴ��ļ�
     *
     * @param url
     * @param name   �ϴ����ļ���������
     * @param files  �ļ��б���ʽ<�ļ���,�ļ�>��
     * @param params �ı����Ͳ���
     * @return
     * @throws WSError
     */
    public static String uploadFile(String url, String name, HashMap<String, File> files,
                                    HashMap<String, String> params) throws WSError {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        // String CHARSET = "UTF-8";
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

            /**
             * ������ChunkedStreamingMode�󣬲��ٵȴ�OutputStream�رպ�����������http
             * requestһ�ι����ͣ������ȷ���http
             * requestͷ����������������·���ķ�ʽʵʱ���͵���������ʵ�����ǲ����߷�����http���ĵĳ���
             * ������ģʽ����������������ͽϴ�Ļ����ǲ����� ��ȡ���ȵ�����
             */
            con.setChunkedStreamingMode(51200);
            /* ����Input��Output����ʹ��Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            /* ���ô��͵�method=POST */
            con.setRequestMethod("POST");
            /* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            // ������ƴ�ı����͵Ĳ���
            StringBuilder sb = new StringBuilder();
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    sb.append(twoHyphens);
                    sb.append(boundary);
                    sb.append(end);
                    sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + end);
                    sb.append(end);
                    sb.append(entry.getValue());
                    sb.append(end);
                }
            }

            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            ds.write(sb.toString().getBytes());
			/* ����DataOutputStream */

            // �����ļ�����
            if (files != null) {
                for (Map.Entry<String, File> file : files.entrySet()) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(twoHyphens);
                    sb2.append(boundary);
                    sb2.append(end);
                    sb2.append("Content-Disposition: form-data; " + "name=\"" + name + "\";filename=\"" + file.getKey()
                            + "\"" + end);
                    sb2.append("Content-Type: application/octet-stream" + end);
                    sb2.append(end);

                    ds.write(sb2.toString().getBytes());

					/* ȡ���ļ���FileInputStream */
                    FileInputStream fStream = new FileInputStream(file.getValue());

					/* ����ÿ��д��1024bytes */
                    int bufferSize = 1024;
                    byte[] buffer = new byte[bufferSize];

                    int length = -1;
					/* ���ļ���ȡ������������ */
                    while ((length = fStream.read(buffer)) != -1) {
						/* ������д��DataOutputStream�� */
                        ds.write(buffer, 0, length);
                    }
					/* close streams */
                    fStream.close();
                    ds.write(end.getBytes());
                }

            }

            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            ds.flush();

			/* ȡ��Response���� */
            InputStream is = con.getInputStream();

            String result = convertStreamToString(is);
			/*
			 * int ch; StringBuffer b = new StringBuffer(); while ((ch =
			 * is.read()) != -1) { b.append((char) ch); }
			 * 
			 * Logger.i(tag, "response :" + b.toString().trim());
			 */

			/* �ر�DataOutputStream */
            ds.close();
            con.disconnect();

            // return b.toString().trim();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * ��⵱ǰURL�Ƿ�����ӻ��Ƿ���Ч�������������2��
     *
     * @param url
     * @return
     */
    public static boolean isConnect(final String url) {
        if (url == null || "".equals(url)) {
            return false;
        }
        int counts = 0;
        while (counts < 2) {
            try {
                URL urlStr = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlStr.openConnection();
                connection.setConnectTimeout(AppConfig.HTTP_TIMEOUT_CONNECTION);
                int state = connection.getResponseCode();
                if (state == 200 || state == 400) {
                    return true;
                }
                break;
            } catch (Exception ex) {
                counts++;
                continue;
            }
        }
        return false;
    }

}
