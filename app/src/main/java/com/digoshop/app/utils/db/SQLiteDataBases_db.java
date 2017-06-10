package com.digoshop.app.utils.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.digoshop.app.module.home.cityselect.model.City;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SQLiteDataBases_db {
    /**
     * 方法：检查表中某列是否存在
     *
     * @param db
     * @param columnName 列名
     * @return
     */
    public boolean checkColumnExists(SQLiteDatabase db, String columnName) {
        boolean result = false;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from sqlite_master where name = ? and sql like ?"
                    , new String[]{"CityTable", "%" + columnName + "%"});
            result = null != cursor && cursor.moveToFirst();
        } catch (Exception e) {
            Log.e("text", "checkColumnExists..." + e.getMessage());
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    //    public boolean InsertColumn(SQLiteDatabase db) {
//        db.execSQL("ALTER TABLE CityTable ADD  type varchar(20);");
//        return true;
//    }
    public boolean upDaCityTypeNew(SQLiteDatabase db, ArrayList<CityBean> updbBeanArrayList) {
        if (updbBeanArrayList != null) {
            if (db == null || !db.isOpen())
                return false;
            for (int i = 0; i < updbBeanArrayList.size(); i++) {
                ContentValues cv = new ContentValues();
                cv.put("type", updbBeanArrayList.get(i).getIstype());
                String[] args = {String.valueOf(updbBeanArrayList.get(i).getCid())};
                db.update("CityTable", cv, "cid=?", args);
            }
        }
        return true;
    }





    public List<City> getAllOpenCityLists(SQLiteDatabase db) {
        if (db == null || !db.isOpen())
            return null;
        Cursor cursor = db.query("CityTable", null, null, null, null, null, null, null);
        List<City> resultopen = new ArrayList<City>();
        City city = null;
        int j = 0;
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                  j = j+1;
                String name = cursor.getString(cursor.getColumnIndex("nn"));
                String pinyin = cursor.getString(cursor.getColumnIndex("pinyin"));
                String lvl = cursor.getString(cursor.getColumnIndex("lvl"));
                String cid = cursor.getString(cursor.getColumnIndex("cid"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                if("open".equals(type)){
                    String citycode = null;
                    if (lvl.equals("3")) {
                        if ("1".equals(cursor.getString(cursor.getColumnIndex("tag")))) {
                            citycode = cursor.getString(cursor.getColumnIndex("nc"));
                        } else {
                            citycode = cursor.getString(cursor.getColumnIndex("citycode"));
                        }
                        city = new City(name, pinyin, citycode,cid);
                        resultopen.add(city);
                    } else if (lvl.equals("1")) {
                        //如果是1说明是省级什么也不加
                    } else {
                        citycode = cursor.getString(cursor.getColumnIndex("nc"));
                        city = new City(name, pinyin, citycode,cid);
                        resultopen.add(city);
                    }
                }
                cursor.moveToNext();
            }
            cursor.close();
        }




        Log.v("lsq", "DDDDDDDDDD444+" + resultopen.size());
        City citycidnipd = null;
        List<City> resultcidnpid = new ArrayList<City>();
        for(int h=0;h<resultopen.size();h++){
            //初始id
            String cid=resultopen.get(h).cid;
            //查询id为cid的城市
            String cidstr;
            cidstr = "cid=" + "'" + cid + "'";
            Cursor ccid = db.query("CityTable", null, cidstr, null, null, null, null, null);
            if (ccid != null && ccid.moveToFirst()) {
                while (!ccid.isAfterLast()) {
                    iscursor(ccid,citycidnipd,resultcidnpid);
                    ccid.moveToNext();
                }
                ccid.close();
            }
//            String npidstr;
//            npidstr = "npid=" + "'" + cid + "'";
//            Cursor cnpid = db.query("CityTable", null, npidstr, null, null, null, null, null);
//            if (cnpid != null && cnpid.moveToFirst()) {
//                while (!cnpid.isAfterLast()) {
//                    iscursor(cnpid,citycidnipd,resultcidnpid);
//                    cnpid.moveToNext();
//                }
//                cnpid.close();
//            }
        }

        Collections.sort(resultcidnpid, new CityComparator());
        return resultcidnpid;
    }

    /**
     * 读取所有城市
     *
     * @return
     */
    public List<City> getAllCities(SQLiteDatabase db) {
        if (db == null || !db.isOpen())
            return null;
        Cursor cursor = db.query("CityTable", null, null, null, null, null, null, null);
        List<City> result = new ArrayList<City>();
        City city=null;
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String type = cursor.getString(cursor.getColumnIndex("type"));
                if(!TextUtils.isEmpty(type)){
                    Log.v("niao","type+"+type);
                }
                Log.v("niao","type+"+type);
                iscursor(cursor,city,result);
                cursor.moveToNext();
            }
            cursor.close();
        }
        Collections.sort(result, new CityComparator());
        return result;
    }
    //只有当高德提供的citycod等于数据库中的citycode且district=nn且tag=1的时候才把数据库中的nc返回当citycode
    public  String getDigoCityCode(SQLiteDatabase db, String citycodestr,String districtstr){
        if (db == null || !db.isOpen())
            return null;
        Log.v("ceshi", "citycodestr+" + citycodestr);
        Log.v("ceshi", "districtstr+" + districtstr);
        String wherestr = "citycode=" + "'" + citycodestr + "'";
        Cursor c = db.query("CityTable", null, wherestr, null, null, null, null, null);
        String returncitycode=null;
        if (c != null && c.moveToFirst()) {
            while (!c.isAfterLast()) {
                if(c.getString(c.getColumnIndex("nn")).equals(districtstr)&&
                        c.getString(c.getColumnIndex("tag")).equals("1")	){
                    returncitycode  =  c.getString(c.getColumnIndex("nc"));
                    //Log.v("ceshi", "returncitycode1+" + returncitycode);
                }else{
                    returncitycode = citycodestr;
                }

                c.moveToNext();
            }
            c.close();
        }

        return returncitycode;
    }
    private void iscursortype(Cursor cursor, City city,List<City> result){
        String name = cursor.getString(cursor.getColumnIndex("nn"));
        String pinyin = cursor.getString(cursor.getColumnIndex("pinyin"));
        String lvl = cursor.getString(cursor.getColumnIndex("lvl"));
        String cid = cursor.getString(cursor.getColumnIndex("cid"));
        String type = cursor.getString(cursor.getColumnIndex("type"));
        if("open".equals(type)){
            String citycode = null;
            Log.v("lsq", "DDDDDDDDDDopen" +type);
            if (lvl.equals("3")) {
                if ("1".equals(cursor.getString(cursor.getColumnIndex("tag")))) {
                    citycode = cursor.getString(cursor.getColumnIndex("nc"));
                } else {
                    citycode = cursor.getString(cursor.getColumnIndex("citycode"));
                }
                city = new City(name, pinyin, citycode,cid);
                result.add(city);
            } else if (lvl.equals("1")) {
                //如果是1说明是省级什么也不加
            } else {
                citycode = cursor.getString(cursor.getColumnIndex("nc"));
                city = new City(name, pinyin, citycode,cid);
                result.add(city);
            }
            Log.v("lsq", "DDDDresult" +result.size());
        }
    }
    private void iscursor(Cursor cursor, City city,List<City> result){
        String name = cursor.getString(cursor.getColumnIndex("nn"));
        String pinyin = cursor.getString(cursor.getColumnIndex("pinyin"));
        String lvl = cursor.getString(cursor.getColumnIndex("lvl"));
        String citycode = null;
        if (lvl.equals("3")) {

            if ("1".equals(cursor.getString(cursor.getColumnIndex("tag")))) {
                citycode = cursor.getString(cursor.getColumnIndex("nc"));
            } else {
                citycode = cursor.getString(cursor.getColumnIndex("citycode"));
            }
            city = new City(name, pinyin, citycode);
            result.add(city);
        } else if (lvl.equals("1")) {
            //如果是1说明是省级什么也不加
        } else {
            citycode = cursor.getString(cursor.getColumnIndex("nc"));
            city = new City(name, pinyin, citycode);
            result.add(city);
        }
    }
    public List<City> searchCityType(SQLiteDatabase db, final String keyword) {
        if (db == null || !db.isOpen())
            return null;
        Log.v("ceshi", "__keyword___" + keyword);
        Cursor cursor = db.rawQuery("select * from " + "CityTable" + " where nn like \"%" + keyword
                + "%\" or pinyin like \"%" + keyword + "%\"", null);
        List<City> result = new ArrayList<City>();
        City city=null;
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                iscursortype(cursor,city,result);
                cursor.moveToNext();
            }
            cursor.close();
        }
        Collections.sort(result, new CityComparator());
        return result;
    }
    /**
     * 通过名字或者拼音搜索
     *
     * @param keyword
     * @return
     */
    public List<City> searchCity(SQLiteDatabase db, final String keyword) {
        if (db == null || !db.isOpen())
            return null;
        Log.v("ceshi", "__keyword___" + keyword);
        Cursor cursor = db.rawQuery("select * from " + "CityTable" + " where nn like \"%" + keyword
                + "%\" or pinyin like \"%" + keyword + "%\"", null);
        List<City> result = new ArrayList<City>();
        City city=null;
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                iscursor(cursor,city,result);
                cursor.moveToNext();
            }
            cursor.close();
        }
        Collections.sort(result, new CityComparator());
        return result;
    }

    /**
     * a-z排序
     */
    private class CityComparator implements Comparator<City> {
        @Override
        public int compare(City lhs, City rhs) {
            String a = lhs.getPinyin().substring(0, 1);
            String b = rhs.getPinyin().substring(0, 1);
            return a.compareTo(b);
        }
    }
}
