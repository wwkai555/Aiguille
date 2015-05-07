package me.spencerwang.aiguille.util;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import me.spencerwang.aiguille.common.App;

/**
 * Created by SpencerWang on 2015/3/10.
 */
public class RequestManager {

    private static RequestQueue requestQueue = Volley.newRequestQueue(App.getContext());

    public static void addRequest(Request<?> request,Object tag){
        if(tag != null){
            request.setTag(tag);
        }
        requestQueue.add(request);

    }

    public static void cancelRequest(Object tag){
        if(requestQueue != null){
        requestQueue.cancelAll(tag);
        }
    }
}
