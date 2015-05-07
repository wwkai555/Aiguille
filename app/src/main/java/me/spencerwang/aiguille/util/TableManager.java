package me.spencerwang.aiguille.util;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import me.spencerwang.aiguille.data.Cloumn;
import me.spencerwang.aiguille.data.SQLiteTable;

/**
 * Created by SpencerWang on 2015/3/10.
 *
 * TODO: Create and delete table.
 */
public class TableManager implements BaseColumns{

    /**
     * Table tag: According to it to create a different table
     */
    public static final int MUSIC_TABLE = 1;
    public static final int SPORT_TABLE = 2;
    public static final int NEWS_TABLE = 3;
    public static final int ALL_TABLE = 0;

   /**
    * Public fields :
    */
    public static final String ID = "id";
    public static final String CATEGORY = "category";
    public static final String JSON = "json";
    public static final String CONTENT_CATEGORY = "contentCategory";

   /**
    * Table name:
    */
    private static final String NEWS_TABLE_NAME = "news";
    private static final String MUSIC_TABLE_NAME = "music";
    private static final String SPORT_TABLE_NAME = "sport";

    private static SQLiteDatabase mDB = null;

    public static void createTable(int tableTag,SQLiteDatabase db){
        if(db == null){
            Log.i("sql","db is null");
            return;
        }
        mDB = db;
        switch (tableTag){
            case NEWS_TABLE:
                createNewsTable();
                break;
            case MUSIC_TABLE:
                createMusicTable();
                break;
            case SPORT_TABLE:
                createSportTable();
                break;
            default:
        }

    }

    private static void createNewsTable(){

        new SQLiteTable(NEWS_TABLE_NAME)
                .addCloum(ID, Cloumn.DataType.INTEGER)
                .addCloum(JSON, Cloumn.DataType.TEXT)
                .addCloum(CATEGORY, Cloumn.DataType.INTEGER)
                .create(mDB);
    }
    private static void createMusicTable(){
        new SQLiteTable(MUSIC_TABLE_NAME)
                .addCloum(ID, Cloumn.DataType.INTEGER)
                .addCloum(JSON, Cloumn.DataType.TEXT)
                .addCloum(CATEGORY, Cloumn.DataType.INTEGER)
                .create(mDB);

    }
    private static void createSportTable(){

        new SQLiteTable(SPORT_TABLE_NAME)
                .addCloum(ID, Cloumn.DataType.INTEGER)
                .addCloum(JSON, Cloumn.DataType.TEXT)
                .addCloum(CATEGORY, Cloumn.DataType.INTEGER)
                .create(mDB);
    }


    public static void deleteTable(String tableName){
        mDB.execSQL("DROP TABLE IF EXISTS"+tableName);
    }

}
