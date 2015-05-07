package me.spencerwang.aiguille.entity;

import com.google.gson.Gson;

/**
 * Created by SpencerWang on 2015/3/10.
 */
public class BaseEntity {

    public String toJson(){
      return new Gson().toJson(this);
    }

}
