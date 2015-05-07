package me.spencerwang.aiguille.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import me.spencerwang.aiguille.R;
import me.spencerwang.aiguille.common.App;
import me.spencerwang.aiguille.util.CustomFragmentManager;

/**
 * Created by SpencerWang on 2015/3/9.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private String[] titles;

    public ViewPagerAdapter(FragmentManager manager){
        super(manager);
        titles = App.getContext().getResources().getStringArray(R.array.titles);
    }
    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Fragment getItem(int tag) {
        return CustomFragmentManager.createFragmentByTag(tag);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return  titles[position];
       // return App.getContext().getResources().getString(titles[position]);
    }

    /**
     * note: When current fragment category changed and it will be notify current fragment to change content
     * @param fragmentCategory
     * @param fragmentContentcategory
     */
     public void notifyPagerDataSetChange(int fragmentCategory,int fragmentContentcategory){
        CustomFragmentManager.getTargetFragmentByCategory(fragmentCategory).notifyDataCategorySetChange(fragmentContentcategory);
    }
}

