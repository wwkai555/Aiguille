package me.spencerwang.aiguille.data;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SpencerWang on 2015/3/10.
 */
public class SQLiteTable {
    private  List<Cloumn> cloumnList = new ArrayList<>();

    private String mTableName;

    /*
    *  Default Settings the primary key
    */
    public SQLiteTable(String tableName){
        mTableName = tableName;
        cloumnList.add(new Cloumn(BaseColumns._ID, Cloumn.Constraint.PRIMARY_KEY, Cloumn.DataType.INTEGER));
    }

    public  SQLiteTable addCloumn(Cloumn cloumn){
        if(cloumn != null){
            cloumnList.add(cloumn);
        }
        return  this;
    }
    public  SQLiteTable  addCloum(String cloumnName,Cloumn.DataType dataType){
        cloumnList.add(new Cloumn(cloumnName,null,dataType));
        return  this;
    }

    public  SQLiteTable addCloumn(String cloumnName,Cloumn.Constraint constraint,Cloumn.DataType dataType){
        cloumnList.add(new Cloumn(cloumnName,constraint,dataType));
        return  this;
    }

    public void create(SQLiteDatabase db){
        String formater = " %s";
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(mTableName).append("(");

        for(Cloumn cloumn: cloumnList){
            sql.append(cloumn.getCloumnName()).append(String.format(formater, cloumn.getmDataType().name()));
            Cloumn.Constraint constraint = cloumn.getmConstraint();
            if(constraint != null){
                sql.append(String.format(formater,constraint.toString()));
            }
            sql.append(",");
        }
        sql = new StringBuilder(sql.subSequence(0,(sql.length() -1)));
        sql.append(");");
        Log.i("sql",sql.toString());
        db.execSQL(sql.toString());

    }

    public String getmTableName(){
        return mTableName;
    }

    /*
    * Used to delete its own
    */
    public void delete(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS"+mTableName);
    }

}
