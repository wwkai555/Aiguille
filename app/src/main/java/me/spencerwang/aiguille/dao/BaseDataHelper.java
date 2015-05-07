package me.spencerwang.aiguille.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

/**
 * Created by SpencerWang on 2015/3/10.
 */
public abstract class BaseDataHelper {
    // CRUD
    private Context mContext;


    public BaseDataHelper(Context context){
        mContext = context;
    }

    protected abstract Uri getContentUri();

    protected  Uri  insert(ContentValues values){
      return mContext.getContentResolver().insert(getContentUri(),values);
    }

    protected int batchInsert(ContentValues[] valueses){
        return mContext.getContentResolver().bulkInsert(getContentUri(),valueses);
    }

    protected int update(ContentValues values,String where,String[] stringArgs){
        return mContext.getContentResolver().update(getContentUri(),values,where,stringArgs);
    }

    protected int  delete(String where,String[] stringArgs){
      return mContext.getContentResolver().delete(getContentUri(),where,stringArgs);
    }

    protected Cursor query(String[] projection,String where,String[] whereargs,String order){
        return mContext.getContentResolver().query(getContentUri(),projection,where,whereargs,order);
    }

    public void notifycationDataSetChange(){
        mContext.getContentResolver().notifyChange(getContentUri(),null);
    }

    public CursorLoader getCursorLoader(Context context){
        return getCursorLoader(context,null,null,null,null,null);
    }

    protected CursorLoader getCursorLoader(Context context,Uri uri,String[] projection,String where,String[] whereargs,String order){
        return new CursorLoader(context,uri,projection,where,whereargs,order);
    }

}
