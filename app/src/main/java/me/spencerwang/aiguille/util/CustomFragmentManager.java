package me.spencerwang.aiguille.util;

import java.util.HashMap;
import java.util.Map;

import me.spencerwang.aiguille.view.fragment.BaseFragment;
import me.spencerwang.aiguille.view.fragment.MusicFragment;
import me.spencerwang.aiguille.view.fragment.NewsFragment;

/**
 * Created by SpencerWang on 2015/3/10.
 */
public class CustomFragmentManager {

    private static int mTag = 0;
    private static  Map<Integer,BaseFragment> fragmentList = new HashMap<>();
    private static  BaseFragment mFragment = null;

    public static BaseFragment createFragmentByTag(int tag){
        mTag = tag;
        mFragment = getItemFragmentFromCach(tag);
        if(mFragment == null){
            mFragment = createFragmentByTag();
        }
        if(mFragment != null){
            fragmentList.put(tag,mFragment);
        }
        return mFragment;
    }

    private CustomFragmentManager(){}

    private static BaseFragment createFragmentByTag(){
        switch (mTag){
            case 0:
                mFragment = NewsFragment.newInstance();
                break;
            case 1:
                mFragment = MusicFragment.newInstance();
                break;
            case 2:
                mFragment = MusicFragment.newInstance();
                break;
            case 3:
                mFragment = MusicFragment.newInstance();
                break;
            case 4:
                mFragment = MusicFragment.newInstance();
                break;
            default:
                throw new RuntimeException(" can not find the positon fragment");
        }
        return  mFragment;
    }

    private static BaseFragment getItemFragmentFromCach(int position){
       return  fragmentList.get(position);
    }

    public static BaseFragment getTargetFragmentByCategory(int id){
        if(getItemFragmentFromCach(id) != null){
            return getItemFragmentFromCach(id);
        }else {
            throw new RuntimeException("target fragment is null");
        }
    }
    public static int getItemFragmentResourse(int tag){
        return BaseFragment.BGDRAWABLE[tag];
    }
}
