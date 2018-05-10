package com.healthSystem;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Yuehu.Li on 2017/11/14.
 */

public class MyContentProvider extends ContentProvider {
    public static final String AUTHORITY = "android.liyuehu.com.healthsystem";
    public static final String TABLE = "menu";
    public static final String TABLE_HEALTH = "health";
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    public static final Uri URI_MY_MENU = Uri.parse("content://" + AUTHORITY + "/" + TABLE);
    public static final Uri URI_MY_HEALTH = Uri.parse("content://" + AUTHORITY + "/" + TABLE_HEALTH);
    public static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, TABLE, 1);
        URI_MATCHER.addURI(AUTHORITY, TABLE_HEALTH, 2);
    }

    @Override
    public boolean onCreate() {
        mySQLiteOpenHelper = new MySQLiteOpenHelper(this.getContext(), "health.db", 1);
        //这里必须返回true
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        Cursor cursor;
        cursor = db.query("Menu", projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {


        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id = 0;
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        //这里如果insert的内容在数据库中存在时，会报UNIQUE constraint failed错误
        //把id=db.insert("Menu",null,values)换成replace
        switch (URI_MATCHER.match(uri)) {
            case 1:
                id = db.replace("Menu", null, values);
                break;
            case 2:
                id = db.replace("HealthSystem", null, values);
                Log.v("id==",String.valueOf(id));
                break;
            default:
                break;
        }
        db.close();
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        int result=0;
        switch (URI_MATCHER.match(uri)) {
            case 1:
                result = db.delete("Menu", selection, selectionArgs);
                break;
            case 2:
                result = db.delete("HealthSystem", selection, selectionArgs);
                Log.v("id==",String.valueOf(result));
                break;
            default:
                break;
        }
        db.close();
        return result;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        int count = db.update("Menu", values, selection, selectionArgs);
        db.close();
        return count;
    }
}
