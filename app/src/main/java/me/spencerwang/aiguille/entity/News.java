package me.spencerwang.aiguille.entity;

import android.database.Cursor;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SpencerWang on 2015/3/10.
 */
public class News extends BaseEntity {

    private static Map<Integer,News> newsCache = new HashMap<>();

    private int id;
    private String title;
    private String time;
    private String content;
    private NewsImage image;
    private BaseCategory.NewsCategory category;

    public BaseCategory.NewsCategory getCategory() {
        return category;
    }

    public static class NewsImage{
        public String normal;
        public String large;
        public NewsImage(String nor,String lar){
            normal = nor;
            large = lar;
        }
    }

    public News(int id,String title,String content,BaseCategory.NewsCategory category){
        this(id,title,null,content,null,category);
    }

    public News(int id,String title,String time,String content,BaseCategory.NewsCategory category){
        this(id,title,time,content,null,category);
    }

    public News(int id,String title,String content,NewsImage image,BaseCategory.NewsCategory category){
        this(id,title,null,content,image,category);
    }

    public News(int id,String title,String time,String content,NewsImage image,BaseCategory.NewsCategory category){
        this.id = id;
        this.time = time;
        this.title = title;
        this.content = content;
        this.image = image;
        this.category = category;
    }

    public static News getFromCache(int id){
        return newsCache.get(id);
    }

    public static News getFromJosn(String json){
        return  new Gson().fromJson(json,News.class);
    }

    public static News getFromCursor(Cursor cursor){
        int id  =  cursor.getInt(cursor.getColumnIndex("id"));
        Log.i("sql","id:"+id);
        News news = getFromCache(id);
        if(news == null){
            String json = cursor.getString(cursor.getColumnIndex("json"));
            news  = getFromJosn(json);
            addCache(id,news);
        }
        return  news;
    }

    private static void addCache(int id,News news){
        if(news != null){
            newsCache.put(id, news);
        }
    }

    public int getId(){
        return  id;
    }

    public String getTitle(){
        return  title;
    }

    public String getTime(){
        return  time;
    }
    public String getContent(){
        return  content;
    }

    public NewsImage getImage(){
        return image;
    }

    public String getLargeImage(){
        return "";
    }

    public class RequestData{
        String page;
        List<News> newsList = new ArrayList<>();
        public String getPage(){
            return  page;
        }
        public List<News>getNewsList(){
            return  newsList;
        }
    }

}




