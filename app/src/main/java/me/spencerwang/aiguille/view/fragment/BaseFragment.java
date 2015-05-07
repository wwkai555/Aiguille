package me.spencerwang.aiguille.view.fragment;


import android.support.v4.app.Fragment;

import com.android.volley.Request;

import me.spencerwang.aiguille.R;
import me.spencerwang.aiguille.util.RequestManager;

/**
 * Created by SpencerWang on 2015/3/10.
 */
public abstract class BaseFragment extends Fragment{

    public static int[] BGDRAWABLE = {R.drawable.news,R.drawable.music,R.drawable.sport,R.drawable.s,R.drawable.fm};

    protected  void executeRequest(Request request){
        RequestManager.addRequest(request, this);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        RequestManager.cancelRequest(this);
    }

    public abstract void notifyDataCategorySetChange(int contentCategory);
}
