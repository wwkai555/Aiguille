package me.spencerwang.aiguille.dao;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * Created by SpencerWang on 2015/3/11.
 */
public class DataProvider extends ContentProvider {

    private Object lock = new Object();

    private static String SCHEME  = "content://";
    private static String AUTHORITY = "me.spencerwang.aiguille.provider";

    private static  String NEWS_PATH = "news";
    private static String MUSIC_PATH = "music";
    private static String SPORT_PATH = "sport";

    private static final int  NEWS_CODE = 0;
    private static final int MUSIC_CODE = 1;
    private static final int SPORT_CODE = 2;

    public static final Uri NEWS_URI = Uri.parse(SCHEME + AUTHORITY + "/" +NEWS_PATH);
    public static final Uri MUSIC_URI = Uri.parse(SCHEME + AUTHORITY + "/" +MUSIC_PATH);
    public static final Uri SPORT_URI = Uri.parse(SCHEME + AUTHORITY + "/" +SPORT_PATH);

    /*
    * MIME type definitions
    */
    private static final String NEWS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.spencerwang.aiguille.news";
    private static final String MUSIC_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.spencerwang.aiguille.music";
    private static final String SPORT_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.spencerwang.aiguille.sport";

    private static String CONTENT_TYPE = "";


    private static UriMatcher mUriMatcher;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY,NEWS_PATH,NEWS_CODE);
        mUriMatcher.addURI(AUTHORITY,MUSIC_PATH,MUSIC_CODE);
        mUriMatcher.addURI(AUTHORITY,SPORT_PATH,SPORT_CODE);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        synchronized (lock){
            String table = matchTable(uri);
            SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();
            long rowID = 0;
            db.beginTransaction();
            try {
                rowID = db.insert(table,null,values);
                db.setTransactionSuccessful();
            }catch (Exception e){

            } finally {
                db.endTransaction();
            }
            if(rowID > 0){
                Uri reUri = ContentUris.withAppendedId(uri,rowID);
                getContext().getContentResolver().notifyChange(uri,null);
                return  reUri;
            }
            throw new android.database.SQLException("Failed to insert row into " + uri);

        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        synchronized (lock){
            String table = matchTable(uri);
            SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();
            int rowID = 0;
            db.beginTransaction();
            try {
               rowID = db.update(table,values,selection,selectionArgs);
                db.setTransactionSuccessful();
            }catch (Exception e){

            }finally {
                db.endTransaction();
            }
            getContext().getContentResolver().notifyChange(uri,null);
            return  rowID;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        synchronized (lock){
            String table  = matchTable(uri);
            SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();
            int rowID = 0;
            db.beginTransaction();
            try {
                rowID = db.delete(table,selection,selectionArgs);
                db.setTransactionSuccessful();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                db.endTransaction();
            }

            getContext().getContentResolver().notifyChange(uri,null);
        }

        return 0;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        synchronized (lock){
            String table = matchTable(uri);
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();
            builder.setTables(table);
            Cursor cursor = builder.query(db,projection,selection,selectionArgs,null,null,sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(),uri);
            return  cursor;
        }
    }

    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)){
            case NEWS_CODE:
                 CONTENT_TYPE = NEWS_CONTENT_TYPE;
                break;
            case MUSIC_CODE:
                CONTENT_TYPE = MUSIC_CONTENT_TYPE;
                break;
            case SPORT_CODE:
                CONTENT_TYPE = SPORT_CONTENT_TYPE;
                break;
            default:
                throw new RuntimeException("can't find the target uri");
        }
        return CONTENT_TYPE;
    }


    private String matchTable(Uri uri){
        String table = "";
        switch (mUriMatcher.match(uri)){
            case NEWS_CODE:
                table = NEWS_PATH;
                break;
            case MUSIC_CODE:
                table = MUSIC_PATH;
               break;
            case SPORT_CODE:
                table = SPORT_PATH;
                break;
            default:
                throw new RuntimeException("can't find the target sqlite table");
        }
        return table;
    }

}
