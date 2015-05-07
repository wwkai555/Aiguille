package me.spencerwang.aiguille.data;

/**
 * Created by SpencerWang on 2015/3/10.
 */
public class Cloumn {

  public static enum Constraint {
    UNIQUE("UNIQUE"), NOT("NOT"), NULL("NULL"), CHECK("CHECK"), FOREIGN_KEY("FOREIGN KEY"), PRIMARY_KEY(
            "PRIMARY KEY");

    private String value;

    private Constraint(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

    public static enum DataType{
        INTEGER,TEXT,REAL,BLOG,NULL
    }

    private String cloumnName;
    private Constraint mConstraint;
    private DataType mDataType;


    public Cloumn(String name, Constraint constraint, DataType dataType){
        cloumnName = name;
        mConstraint = constraint;
        mDataType = dataType;
    }

    public String getCloumnName(){
        return cloumnName;
    }

    public Constraint getmConstraint(){
        return  mConstraint;
    }

    public DataType getmDataType(){
        return  mDataType;
    }
}
