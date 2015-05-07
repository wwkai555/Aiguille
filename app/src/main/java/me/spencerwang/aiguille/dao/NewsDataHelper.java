package me.spencerwang.aiguille.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import me.spencerwang.aiguille.entity.News;
import me.spencerwang.aiguille.util.TableManager;

/**
 * Created by SpencerWang on 2015/3/10.
 */
public class NewsDataHelper extends BaseDataHelper {

    private int mNewsCategory;

    public NewsDataHelper(Context context){
        super(context);
    }

    public  NewsDataHelper(Context context,int newsTag){
        super(context);
        mNewsCategory = newsTag;
    }

    @Override
    protected Uri getContentUri() {
        return DataProvider.NEWS_URI;
    }


    public News query(int id){
        News news = null;
        String where = TableManager.CATEGORY + "=?"+" and "+TableManager.ID +"= ?";
        String[] whereargs = new String[]{String.valueOf(mNewsCategory),String.valueOf(id)};
        Cursor cursor = query(null,where,whereargs,null);
        if(cursor.moveToFirst()){
            news = News.getFromCursor(cursor);
        }
               return news;
    }


    public List<News> query(){
        List<News> allNews = new ArrayList<>();
        String where = TableManager.CATEGORY + "=?";
        String[] whereargs = new String[]{String.valueOf(mNewsCategory)};
        String order = TableManager._ID + " ASC";
       Cursor cursor = query(null,where,whereargs,order);
        while (cursor.moveToFirst()){
            News news = News.getFromCursor(cursor);
            allNews.add(news);
        }
        return allNews;
    }

    public void batchInsert(List<News> newses){
        List<ContentValues> valueses = new ArrayList<>();
        for(News news : newses){
             ContentValues contentValues = getContentValuesFormNewsInstance(news);
            valueses.add(contentValues);
        }
        ContentValues[] valueArray = new ContentValues[valueses.size()];
        batchInsert(valueses.toArray(valueArray));

    }


    public int deleteAll(){
        String where = TableManager.CATEGORY + "=?";
        String[] whereargs = new String[]{String.valueOf(mNewsCategory)};
        return delete(where,whereargs);
    }

    private ContentValues getContentValuesFormNewsInstance(News news){

        ContentValues values = new ContentValues();
        values.put(TableManager.ID,news.getId());
        values.put(TableManager.CATEGORY,news.getCategory().ordinal());
        values.put(TableManager.JSON,news.toJson());
         Log.i("sql",news.toJson());
        return  values;
    }

    public CursorLoader getCursorLoader(Context context) {
        String where = TableManager.CATEGORY + "=?";
        String[] whereargs = new String[]{String.valueOf(mNewsCategory)};
        String order = TableManager._ID + " ASC";
        return super.getCursorLoader(context, getContentUri(), null, where, whereargs, order);
    }


    public void setCategoryChange(int categoryChange){
        mNewsCategory = categoryChange;
    }

}
