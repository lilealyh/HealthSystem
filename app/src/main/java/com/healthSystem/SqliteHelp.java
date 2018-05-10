package com.healthSystem;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Yuehu.Li on 2017/11/16.
 */

public class SqliteHelp {
    private Context mContext;
    private static SqliteHelp mSqliteHelp;
    private SharedPreferences mSharedPreference;
    public static boolean needClear=false;
    public SqliteHelp() {
    }

    public SqliteHelp(Context context){
        this.mContext=context;
    }
    public static SqliteHelp getSqliteHelp(){
        if(mSqliteHelp==null){
            mSqliteHelp=new SqliteHelp();
        }
        return mSqliteHelp;
    }
    private static String[] content = new String[]{"番茄炒蛋", "土豆丝", "酸菜鱼", "烤鱼", "清汤羊肉"};
    private static String[] updateContent = new String[]{"苹果", "香蕉", "草莓", "香草", "榴莲"};
    public void query(){
        //projection表示要查询返回的列
        String[] projection = new String[]{"content", "price", "remain"};
        String[] selectionArg = new String[]{"红烧肉"};
        String selection = "content" + " not null";
        Cursor cursor = mContext.getContentResolver().query(MyContentProvider.URI_MY_MENU, projection, selection, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // int price = cursor.getColumnIndexOrThrow(cursor.getColumnName(1))
                int price=cursor.getInt(1);
                String content = cursor.getString(0);
                //int remain = cursor.getColumnIndexOrThrow(cursor.getColumnName(2))
                int remain = cursor.getInt(2);
                Log.v("lilealyh", " content==" + content + " price===" + price + " remain==" + remain);
            } while (cursor.moveToNext());
        }
    }
    public void insert(){
        int price = 30;
        int remain = 10;
        for (int i = 0; i < content.length; i++) {
            //这里如果不同步，插入的值会不对
            synchronized (this) {
                ContentValues values = new ContentValues();
                values.put("content", content[i]);
                values.put("price", ++price);
                values.put("remain", remain--);
                Uri insertResult = mContext.getContentResolver().insert(MyContentProvider.URI_MY_MENU, values);
                Log.v("lilealyh", "insertResult===" + insertResult);
            }
        }
    }
    public void update(){
        int fruitPrice = 5;
        int fruitRemain = 10;
        for (int i = 0; i <updateContent.length ; i++) {
            synchronized (this){
                ContentValues updateValues = new ContentValues();
                updateValues.put("content", updateContent[i]);
                updateValues.put("price", fruitPrice++);
                updateValues.put("remain", fruitRemain--);
                String where="content" + "=?";
                String[] arg=new String[]{content[i]};
                int updateValue = mContext.getContentResolver().update(MyContentProvider.URI_MY_MENU, updateValues, where, arg);
                Log.v("lilealyh", "updateValue===" + updateValue);
            }
        }
    }
    public void delete(){
        int deleteResult = mContext.getContentResolver().delete(MyContentProvider.URI_MY_HEALTH, "dage"+" <= "+"10" , null);
        if(deleteResult==0){
            deleteResult = mContext.getContentResolver().delete(MyContentProvider.URI_MY_MENU, "content" +" LIKE "+"'%香%'" , null);
        }
        Log.v("lilealyh", "deleteResult===" + deleteResult);
    }


    public boolean insertForHealth(SharedPreferences mSharedPreference,Context context){
        String breakfast=mSharedPreference.getString("breakfast","");
        String lunch=mSharedPreference.getString("lunch","");
        String dinner=mSharedPreference.getString("dinner","");
        String date=mSharedPreference.getString("date","");
        int dapi=mSharedPreference.getInt("dapi",0);
        int morningDage=mSharedPreference.getInt("dage_morning", 0);
        int afternoonDage=mSharedPreference.getInt("dage_afternoon", 0);
        int eveningDage=mSharedPreference.getInt("dage_evening", 0);
        synchronized (this) {
            ContentValues values = new ContentValues();
            values.put("breakfast",breakfast);
            values.put("lunch", lunch);
            values.put("dinner",dinner);
            values.put("dapi", dapi);
            values.put("morningDage", morningDage);
            values.put("afternoonDage",afternoonDage );
            values.put("eveningDage", eveningDage);
            values.put("dageTotal", morningDage+afternoonDage+eveningDage);
            values.put("date", date);
            if(breakfast.equals("")||lunch.equals("")||dinner.equals("")){
                return false;
            }
            Uri insertResult = context.getContentResolver().insert(MyContentProvider.URI_MY_HEALTH, values);
            Log.v("lilealyh", "insertResult===" + insertResult+" date==="+date);
        }
        return true;
    }
}
