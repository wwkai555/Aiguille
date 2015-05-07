package me.spencerwang.aiguille.data;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by SpencerWang on 2015/5/5.
 */
public class GsonRequest<T> extends Request<T> {

    private final Gson mGson = new Gson();
    private final Class<T> mClass;
    private final Response.Listener mListener;
    private Map<String,String>mHeaders;



    public GsonRequest(String url,Class<T>mClass,Response.Listener rlistener, Response.ErrorListener listener) {
      this(Method.GET,url, mClass, null, rlistener, listener);
    }

    public GsonRequest(int method, String url,Class<T>mClass,Map<String, String> headers,Response.Listener rlistener, Response.ErrorListener listener) {
        super(method, url, listener);
        this.mClass = mClass;
         mListener = rlistener;
         mHeaders = headers;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String data = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            return Response.success(mGson.fromJson(data, mClass),
                    HttpHeaderParser.parseCacheHeaders(networkResponse));
        }catch (UnsupportedEncodingException e){
            return Response.error(new ParseError(e));
        }catch (JsonSyntaxException e){
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T t) {

        mListener.onResponse(t);
    }
}
