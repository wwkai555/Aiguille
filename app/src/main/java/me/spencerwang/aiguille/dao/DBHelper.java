package me.spencerwang.aiguille.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import me.spencerwang.aiguille.common.App;
import me.spencerwang.aiguille.util.TableManager;

/**
 * Created by SpencerWang on 2015/3/10.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper mDBHeper = new DBHelper(App.getContext());

    private static String DBNAME = "aiguille.db";

    public static DBHelper getInstance(){
        if(mDBHeper == null){
            mDBHeper = new DBHelper(App.getContext());
        }
        return  mDBHeper;
    }

    public DBHelper(Context context){
        super(context,DBNAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TableManager.createTable(TableManager.NEWS_TABLE,db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
