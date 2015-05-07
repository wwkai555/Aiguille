package me.spencerwang.aiguille.entity;

/**
 * Created by SpencerWang on 2015/3/11.
 */
public class BaseCategory {

    public static enum Category{
        NEWS,MUSIC,SPORT,MOIVE,FM
    }
    public static enum NewsCategory{
        SOCIETY,AMUSEMENT,TECHNOLOGY;
    }

    public static enum MusicCategory{
        CLASSIC_SONG,LIGHT_MUSIC,DISO_DJ;
    }

    public static enum SportCategory{
        BASKETBALL,FOOTBALL,TENNIS;
    }

}
