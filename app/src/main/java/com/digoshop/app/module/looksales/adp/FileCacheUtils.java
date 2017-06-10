package com.digoshop.app.module.looksales.adp;
 

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
 
import java.util.Arrays;
import java.util.Comparator;
 

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
 
/**
 * @author Rowand jj
 *
 *�ļ�����
 */
public class FileCacheUtils
{
    /**
     *ͼƬ��������·�� 
     */
    private static final String IMG_CACH_DIR = "/imgCache";
     
    /**
     * �ֻ��Ŀ¼
     */
    private static String DATA_ROOT_PATH = null;
    /**
     * sd����Ŀ¼
     */
    private static String SD_ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
     
    /**
     *�������չ�� 
     */
    private static final String CACHE_TAIL = ".cach";
     
    /**
     * ��󻺴�ռ�,��λ��mb
     */
    private static final int CACHE_SIZE = 4;
     
    /**
     * sd���ڴ���ڴ�ֵʱ�������?��,��λ��mb
     */
    private static final int NEED_TO_CLEAN = 10;
 
    /**
     * ������
     */
    private Context context;
     
    private static final String TAG = "BitmapFileCacheUtils";
     
     
    public FileCacheUtils(Context context)
    {
        this.context = context;
        DATA_ROOT_PATH = context.getCacheDir().getAbsolutePath();
    }
    /**
     * �ӻ����л�ȡһ��ͼƬ
     */
    public Bitmap getBitmapFromFile(String key)
    {
        if(key==null)
        {
            return null;
        }
        String filename = getCacheDirectory()+File.separator+convertKeyToFilename(key);
        File file = new File(filename);
        if(file.exists())
        {
            Bitmap bitmap = BitmapFactory.decodeFile(filename);
            if(bitmap == null)
            {
                file.delete();
            }
            else
            {
                updateFileModifiedTime(filename);
                Log.i(TAG,"get file from sdcard cache success...");
                return bitmap;
            }
        }
        return null;
    }
    /**
     * ��ͼƬ�����ļ�����
     */
    public void addBitmapToFile(String key,Bitmap bm)
    {
        if(bm == null || key == null)
        {
            return;
        }
        //��������ֻ���
        removeCache(getCacheDirectory());
         
        String filename = convertKeyToFilename(key);
        File dir = new File(getCacheDirectory());
        if(!dir.exists())
        {
            dir.mkdirs();
        }
        File file = new File(dir, filename);
        try
        {
            OutputStream out = new FileOutputStream(file);//������Ҫע�⣬���ָ��Ŀ¼�����ڣ�Ӧ���ȵ���mkdirs���Ŀ¼��������ܴ����ļ�ʧ��
            bm.compress(CompressFormat.JPEG,100, out);
            out.close();
            Log.i(TAG,"add file to sdcard cache success...");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    /**
     * ��ȡ�ļ�����·��
     * @return
     */
    private String getCacheDirectory()
    {
        String cachePath = null;
        if(isSdcardAvailable())
        {
            cachePath = SD_ROOT_PATH+IMG_CACH_DIR;
        }else
        {
            cachePath = DATA_ROOT_PATH+IMG_CACH_DIR;
        }
        return cachePath;
    }
    /**
     * 
     * ���40%�Ļ��棬��Щ���汻ɾ������ȼ���ݽ���ʹ��ʱ������,Խ��û��ʹ�ã�Խ���ױ�ɾ��
     */
    private void removeCache(String dirPath)
    {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if(files == null)
        {
            return;
        }
        double total_size = 0;
        for(File file : files)
        {
            total_size+=file.length();
        }
        total_size = total_size/1024/1024;
        if(total_size > CACHE_SIZE || getSdCardFreeSpace() <= NEED_TO_CLEAN)
        {
            Log.i(TAG,"remove cache from sdcard cache...");
            int removeFactor = (int) (files.length*0.4);
            
            Arrays.sort(files, new FileLastModifiedComparator());
            for(int i = 0; i < removeFactor; i++)
            {
                files[i].delete();
            }
        }
    }
     
    /**
     *��ȡsd�����ÿռ�
     */
    private int getSdCardFreeSpace()
    {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double freespace = stat.getAvailableBlocks()*stat.getBlockSize();
        return (int) (freespace/1024/1024);
    }
    /**
     *�ж�sd���Ƿ����
     * @return
     */
    private boolean isSdcardAvailable()
    {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }
    /**
     * ���ؼ���ת��Ϊ�ļ���
     */
    private String convertKeyToFilename(String key)
    {
        if(key == null)
        {
            return "";
        }
        return key.hashCode()+CACHE_TAIL;
    }
    /**
     * �����ļ�����޸�ʱ��
     */
    private void updateFileModifiedTime(String path)
    {
        File file = new File(path);
        file.setLastModified(System.currentTimeMillis());
    }
 
    private class FileLastModifiedComparator implements Comparator<File>
    {
        @Override
        public int compare(File lhs, File rhs)
        {
            if(lhs.lastModified() > rhs.lastModified())
            {
                return 1;
            }else if(lhs.lastModified() == rhs.lastModified())
            {
                return 0;
            }else
            {
                return -1;
            }
        }

	 
    }
}