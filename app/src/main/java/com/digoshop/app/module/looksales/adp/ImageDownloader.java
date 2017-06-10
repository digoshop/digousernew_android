package com.digoshop.app.module.looksales.adp;

 

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
 
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
 
 
/**
 * @author Rowand jj
 *����ͼƬ�Ĺ�����
 *    ͨ��downloadImage��������ͼƬ������ͼƬ���浽�����У�ʹ���̳߳أ��������صõ���ͼƬ����һ���ص��ӿ�OnImageDownloadListener����
 *    ͨ��showCacheImage������ȡ�����е�ͼƬ
 */
public class ImageDownloader
{
    /**
     * ����image���̳߳�
     */
    private ExecutorService mImageThreadPool = null;
 
    /**
     * �ļ�����Ĺ�����
     */
    private FileCacheUtils fileCacheUtils = null;
 
    /**
     * �̳߳����̵߳�����
     */
    private static final int THREAD_NUM = 2;
     
    /**
     * ����ͼ�Ŀ�
     */
    private static final int REQ_WIDTH = 190;
    /**
     * ����ͼ�ĸ�
     */
    private static final int REQ_HEIGHT = 190;
 
    protected static final int DOWNLOAD = 1;
 
    private Context context;
 
    /**
     * ������
     * @param context
     */
    public ImageDownloader(Context context)
    {
        this.context = context;
        fileCacheUtils = new FileCacheUtils(context);
    }
     
    /**
     * ����һ��ͼƬ���ȴ��ڴ滺�����ң����û����ȥ�ļ��������ң����û�оʹ�����������
     * @param url
     * @param listener
     * @return
     */
    public Bitmap downloadImage(final String url,final OnImageDownloadListener listener)
    {
        final String subUrl = url.replaceAll("[^\\w]", "");
        Bitmap bitmap = showCacheBitmap(subUrl);
        if(bitmap!=null)//�������ҵ�
        {
            return bitmap;
        }else//������δ�ҵ��������߳�����
        {
//            new AsyncTask<string, bitmap="">()
//            {
//                @Override
//                protected Bitmap doInBackground(String... params)
//                {
//                    Bitmap bitmap = getImageFromUrl(url);//������������ͼƬ
//                    fileCacheUtils.addBitmapToFile(subUrl,bitmap);//�ӵ��ļ�����
//                    BitmapLruCacheHelper.getInstance().addBitmapToMemCache(subUrl, bitmap);//�ӵ��ڴ滺��
//                    return bitmap;
//                }
//                protected void onPostExecute(Bitmap result) 
//                {
//                    listener.onImageDownload(url, result);
//                }
//            }.execute(url);
             
            final Handler handler = new Handler()
            {
                @Override
                public void handleMessage(Message msg)
                {
                    if(msg.what == DOWNLOAD)
                    {
                        listener.onImageDownload(url,(Bitmap)msg.obj);//�����غ��ͼƬ�Ĳ�������listenerʵ���ദ��
                    }
                }
            };
            getThreadPool().execute(new Runnable()//���̳߳��л�ȡһ���߳�ִ�����ز����������غ��ͼƬ�ӵ��ļ�������ڴ滺��
            {
                @Override
                public void run()
                {
                    Bitmap bitmap = getImageFromUrl(url);//������������ͼƬ
                    Message msg = Message.obtain(handler, DOWNLOAD, bitmap);
                    msg.sendToTarget();//������Ϣ
                     
                    //�ӵ�������
                    fileCacheUtils.addBitmapToFile(subUrl,bitmap);
                    BitmapLruCacheHelper.getInstance().addBitmapToMemCache(subUrl, bitmap);
                }
            });
             
        }
        return null;
    }
     
    /**
     * ��ʾ�����е�ͼƬ
     * @param url
     * @return
     */
    public Bitmap showCacheBitmap(String url)
    {
        Bitmap bitmap = BitmapLruCacheHelper.getInstance().getBitmapFromMemCache(url);
        if(bitmap!=null)//���ȴ��ڴ滺������
        {
            return bitmap;
        }else
        {
            bitmap = fileCacheUtils.getBitmapFromFile(url);
            if(bitmap!=null)//���ļ��������ҵ�
            {
                BitmapLruCacheHelper.getInstance().addBitmapToMemCache(url, bitmap);//�����ڴ滺��
                return bitmap;
            }
        }
        return null;
    }
    /**
     * ��ȡ�̳߳�ʵ��
     */
    public ExecutorService getThreadPool()
    {
        if (mImageThreadPool == null)
        {
            synchronized (ExecutorService.class)
            {
                if (mImageThreadPool == null)
                {
                    mImageThreadPool = Executors.newFixedThreadPool(THREAD_NUM);
                }
            }
        }
        return mImageThreadPool;
    }
 
    /**
     * ��url�л�ȡbitmap
     * @param url
     * @return
     */
    public Bitmap getImageFromUrl(String url)
    {
        HttpURLConnection conn = null;
        try
        {
            URL target = new URL(url);
            conn = (HttpURLConnection) target.openConnection();
            conn.setReadTimeout(3000);
            conn.setConnectTimeout(10 * 1000);
            conn.setDoInput(true);
 
            if (conn.getResponseCode() == 200)
            {
                InputStream is = conn.getInputStream();
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                int len = 0;
                byte[] buf = new byte[1024];
                while((len = is.read(buf))!=-1)
                {
                    bout.write(buf, 0, len);
                }
                is.close();
                byte[] data = bout.toByteArray();
                return BitmapUtils.decodeSampledBitmapFromByteArray(data,REQ_WIDTH, REQ_HEIGHT);//���ص���ѹ���������ͼ
            }
 
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * ȡ��ǰ������
     */
    public synchronized void cancellTask()
    {
        if(mImageThreadPool != null)
        {
            mImageThreadPool.shutdownNow();
            mImageThreadPool = null;
        }
    }
    /**
     *�������غ��ͼƬ�Ļص��ӿ�
     */
    public interface OnImageDownloadListener
    {
        void onImageDownload(String url,Bitmap bitmap);
    }
} 