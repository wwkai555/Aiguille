package me.spencerwang.aiguille.util;

import android.os.Environment;

/**
 * Created by SpencerWang on 2015/4/28.
 */
public class LocalPathUtil {
    public static String ROOT = Environment.getExternalStorageDirectory().getAbsolutePath()+"/aiguille/";
    public static String NEWS_CACHE = ROOT+"news/";
    public static String MUSIC_CACHE = ROOT + "music/";
    public static String VIDEO_CACHE = ROOT +"video/";
    public static String SPORT_CACHE = ROOT +"sport/";
    public static String FM_CACHE = ROOT +"fm";
}
