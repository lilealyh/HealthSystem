package android.liyuehu.com.sqlitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import static android.liyuehu.com.sqlitedemo.MyContentProvider.URI_MY_MENU;

/**
 * Created by Yuehu.Li on 2017/11/16.
 */

public class SqliteHelp {
    private Context mContext;
    public SqliteHelp(Context context){
        this.mContext=context;
    }
    private static String[] content = new String[]{"番茄炒蛋", "土豆丝", "酸菜鱼", "烤鱼", "清汤羊肉"};
    private static String[] updateContent = new String[]{"苹果", "香蕉", "草莓", "香草", "榴莲"};
    public void query(){
        //projection表示要查询返回的列
        String[] projection = new String[]{"content", "price", "remain"};
        String[] selectionArg = new String[]{"红烧肉"};
        String selection = "content" + " not null";
        Cursor cursor = mContext.getContentResolver().query(URI_MY_MENU, projection, selection, null, null);
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
    void insert(){
        int price = 30;
        int remain = 10;
        for (int i = 0; i < content.length; i++) {
            //这里如果不同步，插入的值会不对
            synchronized (this) {
                ContentValues values = new ContentValues();
                values.put("content", content[i]);
                values.put("price", ++price);
                values.put("remain", remain--);
                Uri insertResult = mContext.getContentResolver().insert(URI_MY_MENU, values);
                Log.v("lilealyh", "insertResult===" + insertResult);
            }
        }
    }
    void update(){
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
                int updateValue = mContext.getContentResolver().update(URI_MY_MENU, updateValues, where, arg);
                Log.v("lilealyh", "updateValue===" + updateValue);
            }
        }
    }
    void delete(){
        int deleteResult = mContext.getContentResolver().delete(URI_MY_MENU, "content" +" LIKE "+"'%鱼%'" , null);
        if(deleteResult==0){
            deleteResult = mContext.getContentResolver().delete(URI_MY_MENU, "content" +" LIKE "+"'%香%'" , null);
        }
        Log.v("lilealyh", "deleteResult===" + deleteResult);
    }
}
