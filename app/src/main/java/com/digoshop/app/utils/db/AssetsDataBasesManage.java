package com.digoshop.app.utils.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class AssetsDataBasesManage {
    private static String tag = "AssetsDatabase"; // 打印的tag
    private static String databasepath = "/data/data/%s/database"; // %s 是包名
    public static String coursesDBName = "NationDB.db";

    // 从assets数据库文件复制到目标数据库
    private Map<String, SQLiteDatabase> databases = new HashMap<String, SQLiteDatabase>();

    // 上下文
    private Context context = null;
    // 使用单利模式，使管理对象不重复创建
    private static AssetsDataBasesManage mInstance = null;

    /**
     * 初始化manage对象
     */
    public static void initManager(Context context) {
        if (mInstance == null) {
            mInstance = new AssetsDataBasesManage(context);
        }
    }

    /**
     * 获取一个000 AssetsDatabaseManager对象
     */
    public static AssetsDataBasesManage getManager() {
        return mInstance;
    }

    private AssetsDataBasesManage(Context context) {
        this.context = context;
    }

    public boolean DeleteOldDb(String dbfile) {
        String sfile = getDatabaseFile(dbfile);
        System.out.println("sfile::" + sfile);
        File file = new File(sfile);
        if (file.exists()) {
            file.delete();
        }
        //删除后重新拷贝
        Log.v("lsq", "file.exists()+" + file.exists());
        if (!file.exists()) {
//            String spath = getDatabaseFilepath();
//            File spathfile = new File(spath);\
            boolean copybool = copyAssetsToFilesystem(dbfile, sfile);
            Log.v("lsq", "copybool+" + copybool);
            if (copybool) {
                return true;
            }else{
                return false;
            }
        }
        return true;
    }

    /**
     * 获取数据对象，并打开数据库
     */
    public SQLiteDatabase getDatabase(String dbfile) {
//        if (databases.get(dbfile) != null) {
//            return (SQLiteDatabase) databases.get(dbfile);
//        }
        Log.v("lsq", "flag222");
        if (context == null)
            return null;

        String spath = getDatabaseFilepath();
        String sfile = getDatabaseFile(dbfile);
        File file = new File(sfile);
        //文件data/data目录下没有这个文件时候
        //重新拷贝
        if (!file.exists()) {
            file = new File(spath);
            //
            if (!file.exists() && !file.mkdirs()) {
                return null;
            }
            if (!copyAssetsToFilesystem(dbfile, sfile)) {
                return null;
            }
        }

        SQLiteDatabase db = SQLiteDatabase.openDatabase(sfile, null,SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        if (db != null) {
            databases.put(dbfile, db);
        }
        return db;
    }


    /**
     * 安全的拷贝courses数据库到内存
     */
    public void safeCopyCourseDB() {
        String assetsSrc = coursesDBName;
        String sfile = getDatabaseFile(coursesDBName);
        File file = new File(sfile);
//		数据库文件存在则删除
        if (file.exists()) {
            file.delete();
        }
        copyAssetsToFilesystem(assetsSrc, sfile);
    }

    /**
     * @author Administrator 回去数据库路径
     */

    public String getDatabaseFilepath() {
        return String.format(databasepath,
                context.getApplicationInfo().packageName);
    }

    /**
     * @author Administrator 得到数据库文件
     */

    private String getDatabaseFile(String dbfile) {
        return getDatabaseFilepath() + "/" + dbfile;
    }

    /**
     * @author Administrator 复制assets文件到系统文件
     */
    public boolean copyAssetsToFilesystem(String assetsSrc, String des) {
        // Log.i(tag, "Copy "+assetsSrc+" to "+des);
        InputStream istream = null;
        OutputStream ostream = null;
        try {
            AssetManager am = context.getAssets();
            istream = am.open(assetsSrc);
            ostream = new FileOutputStream(des);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = istream.read(buffer)) > 0) {
                ostream.write(buffer, 0, length);
            }
            istream.close();
            ostream.close();
            Log.v("lsq", "ostream.close()+");
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (istream != null)
                    istream.close();
                if (ostream != null)
                    ostream.close();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            return false;
        }
        return true;
    }

    /**
     * 关闭数据库
     */
    public boolean closeDatabase(String dbfile) {
        if (databases.get(dbfile) != null) {
            SQLiteDatabase db = (SQLiteDatabase) databases.get(dbfile);
            db.close();
            databases.remove(dbfile);
            return true;
        }
        return false;
    }

    /**
     * 关闭所有数据哭
     */
    static public void closeAllDatabase() {
        if (mInstance != null) {
            for (int i = 0; i < mInstance.databases.size(); ++i) {
                if (mInstance.databases.get(i) != null) {
                    mInstance.databases.get(i).close();
                }
            }
            mInstance.databases.clear();
        }
    }
}
