package me.spencerwang.aiguille.view.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.spencerwang.aiguille.R;
import me.spencerwang.aiguille.entity.BaseCategory;
import me.spencerwang.aiguille.util.CustomFragmentManager;
import me.spencerwang.aiguille.view.adapter.ViewPagerAdapter;
import me.spencerwang.aiguille.view.component.IndicatorTabView;


public class MainActivity extends ActionBarActivity{

    @InjectView(R.id.bar)
    Toolbar mToolBar;

    @InjectView(R.id.indicator)
    IndicatorTabView mIndicator;

    @InjectView(R.id.viewPager)
    ViewPager mViewPager;

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle  mToggle;
    private Bitmap mBitmap;

    private ViewPager.OnPageChangeListener mPageChangeListener;

    private ViewPagerAdapter mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    private enum ColorCommand{DEEPED,DODGE}
    private Spinner mSpinner;

    private ArrayAdapter<String> categoryAdapter = null;
    private int mCurrentFragmentCategory = 0;

    private static final String TAG ="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initListener();
        initComponent();
        updateThemeStyle(0);
    }

    private void  initComponent(){
        /**
         * init toolbar : title set should before setSupportActionBar() operation
         */
        mToolBar.setTitle(R.string.toolBarTitle);
        mToolBar.setLogo(R.drawable.logo);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToggle = new ActionBarDrawerToggle(MainActivity.this,mDrawerLayout,mToolBar,R.string.drawerOpen,R.string.drawerClose);
        mToggle.syncState();
        mDrawerLayout.setDrawerListener(mToggle);

        mViewPager.setAdapter(mPagerAdapter);
        mIndicator.setViewPager(mViewPager);
        mIndicator.setOnPagerChangeListener(mPageChangeListener);

        categoryAdapter = new ArrayAdapter<String>(this,R.layout.spninner_category,R.id.category, getCategoryFilterData(BaseCategory.Category.NEWS));
    }

    private void initListener(){
        mPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }
            @Override
            public void onPageSelected(int i) {
                mCurrentFragmentCategory = i;
                updateCategory(i);
                updateThemeStyle(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        };
    }

    private List<String> getCategoryFilterData(BaseCategory.Category category){
        List<String> categoryList = new ArrayList<>();
        int resid = R.array.news_category;
        switch (category){
            case NEWS:
                resid = R.array.news_category;
                break;
            case MUSIC:
                resid = R.array.music_category;
                break;
            case SPORT:
                resid = R.array.sport_category;
                break;
            case MOIVE:
                resid = R.array.moive_category;
                break;
            case FM:
                resid = R.array.fm_category;
                break;
            default:
                break;
        }
        Collections.addAll(categoryList,getResources().getStringArray(resid));
        return categoryList;
    }

    private void updateCategory(int tag){
        BaseCategory.Category category = null;
        for(BaseCategory.Category c : BaseCategory.Category.values()){
            if(c.ordinal() == tag){
                category = c;
            }
        }
        categoryAdapter.clear();
        if(category != null){
           categoryAdapter.addAll(getCategoryFilterData(category));
        }else{
            Log.e(TAG,"can not found the target categoryFilter data");
        }
        categoryAdapter.notifyDataSetChanged();
    }

    private void updateThemeStyle(int tag){
        mBitmap = BitmapFactory.decodeResource(getResources(), CustomFragmentManager.getItemFragmentResourse(tag));
        Palette.generateAsync(mBitmap,new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch = palette.getVibrantSwatch();
                mIndicator.setIndicatorColor(colorManager(swatch.getRgb(), ColorCommand.DEEPED, 0.2f));
                mIndicator.setDividerColor(swatch.getRgb());
                mIndicator.setTextColor(swatch.getTitleTextColor());
                mIndicator.setBackgroundColor(swatch.getRgb());
                mIndicator.setUnderLineColor(colorManager(swatch.getRgb(),ColorCommand.DEEPED,0.2f));

                mToolBar.setBackgroundColor(swatch.getRgb());
                if(Build.VERSION.SDK_INT > 21){
                    Window window = getWindow();
                    window.setNavigationBarColor(swatch.getRgb());
                    window.setStatusBarColor(swatch.getRgb());
                }
            }
        });

    }


    private int colorManager(int rgbValues,ColorCommand order,float deg){
        float degree = 1;
        if(deg < 0 || deg > 1){
            throw new RuntimeException("the attr degree for deal with color is error");
        }
        if(order == ColorCommand.DEEPED){
            degree -= deg;
        }else if(order == ColorCommand.DODGE){
            degree += deg;
        }else {
            degree = 1;
        }

        int alpha = rgbValues >> 24;
        int red = rgbValues >> 16 & 0xFF;
        int green = rgbValues >> 8 & 0xFF;
        int blue = rgbValues & 0xFF;
        red = (int) Math.floor(red *  degree);
        green = (int) Math.floor(green * degree);
        blue = (int) Math.floor(blue * degree);
        return Color.rgb(red, green, blue);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mSpinner = (Spinner) MenuItemCompat.getActionView(menu
                .findItem(R.id.action_setting));
        mSpinner.setBackgroundColor(getResources().getColor(R.color.menu));
        mSpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
        if(categoryAdapter != null){
        mSpinner.setAdapter(categoryAdapter);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mPagerAdapter.notifyPagerDataSetChange(mCurrentFragmentCategory,position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
