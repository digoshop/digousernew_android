package com.digoshop.app.module.looksales.adp;

  
import android.graphics.Bitmap;
 
import android.support.v4.util.LruCache;
import android.util.Log;
 
 
 
/**
 * @author Rowand jj
 *
 *ʹ��lrucache����ͼƬ���ڴ棬�����˵���ģʽ
 */
public class BitmapLruCacheHelper
{
    private static final String TAG = null;
    private static BitmapLruCacheHelper instance = new BitmapLruCacheHelper();
    private LruCache<String,Bitmap> cache = null;
    private BitmapLruCacheHelper()
    {
        int maxSize = (int) (Runtime.getRuntime().maxMemory()/8);
        cache = new LruCache<String, Bitmap>(maxSize)
        {
            @Override
            protected int sizeOf(String key, Bitmap value)
            {
                return value.getRowBytes()*value.getHeight();
            }
        };
    }
     
    /**
     *���뻺�� 
     * @param key
     * @param value
     */
    public void addBitmapToMemCache(String key,Bitmap value)
    {
        if(key == null || value == null)
        {
            return;
        }
        if(cache!=null && getBitmapFromMemCache(key)==null)
        {
            cache.put(key, value);
            Log.i(TAG,"put to lrucache success");
        }
    }
     
    /**
     * �ӻ����л�ȡͼƬ
     * @param key
     * @return
     */
    public Bitmap getBitmapFromMemCache(String key)
    {
        if(key == null)
        {
            return null;
        }
        Bitmap bitmap = cache.get(key);
        Log.i(TAG,"from lrucache,bitmap="+bitmap);
        return bitmap;
    }
     
    /**
     * ��ȡʵ��
     * @return
     */
    public static BitmapLruCacheHelper getInstance()
    {
        return instance;
    }
} 