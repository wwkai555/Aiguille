package me.spencerwang.aiguille.view.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.spencerwang.aiguille.R;

/**
 * Created by SpencerWang on 2015/3/4.
 */
public class IndicatorTabView extends HorizontalScrollView{

    private Context mContext;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private ViewPager.OnPageChangeListener mPageChangeListener;
    private CustomPageChangeListener mCustomPageChangeListener = new CustomPageChangeListener();

    private LinearLayout tabContainer;
    private ViewGroup.LayoutParams tabItemLayoutParam ;

    /* custom attrs init */
    private int scrollOffset = 52;
    private int indicatorColor = 0xFF666666;
    private int underLineColor = 0x1A000000;
    private int dividerColor = 0x1A000000;

    private int indicatorHeight = 8;
    private int underLineHeight = 2;
    private int dividerPadding = 12;
    private int tabPadding = 24;
    private int dividerWidth = 1;

    private int tabTextSize = 14;
    private int tabTextColor = 0xFF666666;
    private int selectedTabTextColor = 0xFF668822;

    /* tab indicator scroll position */
    private int currentPosition = 0;
    private int lastPosition = 0;
    private int selectedPosition = 0;

    private float currentPositionOffset = 0f;

    private int lastScrollX = 0;

    private Paint rectPaint;
    private Paint dividerPaint;

    private Typeface tabTypeface = null;
    private int tabTypefaceStyle = Typeface.NORMAL;

    public IndicatorTabView(Context context){
        this(context, null);
    }

    public IndicatorTabView(Context context, AttributeSet attributeSet){
        this(context,attributeSet,0);
    }

    public IndicatorTabView(Context context, AttributeSet attributes, int defStyle){
        super(context,attributes,defStyle);
        mContext = context;

        setFillViewport(true);
        setWillNotDraw(false);

        tabContainer = new LinearLayout(context);
        tabContainer.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        tabContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabItemLayoutParam = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        addView(tabContainer);

        init_attrs(context);
        init_attrt_unitConversion(context);

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);

    }

    private void init_attrt_unitConversion(Context context){

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        indicatorHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,indicatorHeight,displayMetrics);
        underLineHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,underLineHeight,displayMetrics);
        dividerWidth = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dividerWidth,displayMetrics);
        dividerPadding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dividerPadding,displayMetrics);
        tabPadding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,tabPadding,displayMetrics);

        tabTextSize = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,tabTextSize,displayMetrics);

    }

    private void init_attrs(Context context){
        TypedArray typedArray = context.obtainStyledAttributes(R.styleable.IndicatorTabAttrs);

        indicatorColor = typedArray.getColor(R.styleable.IndicatorTabAttrs_pstsIndicatorColor,indicatorColor);
        underLineColor = typedArray.getColor(R.styleable.IndicatorTabAttrs_pstsUnderlineColor,underLineColor);
        dividerColor = typedArray.getColor(R.styleable.IndicatorTabAttrs_pstsDividerColor,dividerColor);

        scrollOffset = typedArray.getDimensionPixelSize(R.styleable.IndicatorTabAttrs_pstsScrollOffset,scrollOffset);
        indicatorHeight = typedArray.getDimensionPixelSize(R.styleable.IndicatorTabAttrs_pstsIndicatorHeight,indicatorHeight);
        underLineHeight = typedArray.getDimensionPixelSize(R.styleable.IndicatorTabAttrs_pstsUnderlineHeight,underLineHeight);
        dividerPadding= typedArray.getDimensionPixelSize(R.styleable.IndicatorTabAttrs_pstsDividerPadding,dividerPadding);
        tabPadding = typedArray.getDimensionPixelSize(R.styleable.IndicatorTabAttrs_pstsTabPaddingLeftRight,tabPadding);

        typedArray.recycle();
    }

    public void setOnPagerChangeListener(ViewPager.OnPageChangeListener listener){

        if(listener == null){
            throw  new RuntimeException("");
        }
        mPageChangeListener = listener;
    }

    public void setViewPager(ViewPager pager){
        if(pager == null){
            return;
        }
        mViewPager = pager;
        mPagerAdapter = mViewPager.getAdapter();
        mViewPager.setOnPageChangeListener(mCustomPageChangeListener);
        notifycationDataSetChange();
    }

    private int allTabCount;

    private  void notifycationDataSetChange(){
        tabContainer.removeAllViews();
        allTabCount = mPagerAdapter.getCount();

        for(int i = 0 ; i < allTabCount; i++){
            addTabItemView(i);
        }
        updateTabStyle();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                currentPosition = mViewPager.getCurrentItem();
                scrollToPositionTab(currentPosition, 0);
            }
        });

    }

    private  void addTabItemView(final int position){
        String tabTitle = mPagerAdapter.getPageTitle(position).toString();
        final TextView tabView = new TextView(mContext);
        tabView.setText(tabTitle);
        tabView.setGravity(Gravity.CENTER);
        tabView.setSingleLine(true);
        tabView.setTextSize(tabTextSize);

        tabView.setTextColor(tabTextColor);
        tabView.setFocusable(true);
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
             tabView.setTextColor(selectedTabTextColor);
                mViewPager.setCurrentItem(position);
                //scrollToPositionTab(position,0);
            }
        });
        tabView.setPadding(tabPadding,0,tabPadding,0);
        tabContainer.addView(tabView, position,tabItemLayoutParam);
    }

    private void updateTabStyle(){

        for(int i = 0;i< allTabCount;i++){
            View view = tabContainer.getChildAt(i);

            if(view instanceof TextView){
                TextView textView = (TextView)view;

                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
                textView.setTypeface(tabTypeface, tabTypefaceStyle);
                textView.setTextColor(tabTextColor);

                if(i == selectedPosition){
                    textView.setTextColor(selectedTabTextColor);
                }
            }

        }
    }

    private  void scrollToPositionTab(int position,int offset){
        if (allTabCount == 0) {
            return;
        }

        int newScrollX = tabContainer.getChildAt(position).getLeft() + offset;

      /*  if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }*/

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isInEditMode() || allTabCount == 0){
            return;
        }
        int height = getHeight();
        rectPaint.setColor(underLineColor);
        canvas.drawRect(0,height-underLineHeight,tabContainer.getWidth(),height,rectPaint);


        View view = tabContainer.getChildAt(currentPosition);
        float left = view.getLeft();
        float right = view.getRight();

        if(currentPositionOffset > 0f && currentPosition < allTabCount - 1){
            View nextTab = tabContainer.getChildAt(currentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();

            left = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * left);
            right = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * right);
        }
        rectPaint.setColor(indicatorColor);
        canvas.drawRect(left, height - indicatorHeight, right, height, rectPaint);

        dividerPaint.setColor(dividerColor);
        for (int i = 0; i < allTabCount - 1; i++) {
            View tab = tabContainer.getChildAt(i);
            canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(), height - dividerPadding,
                    dividerPaint);
        }
    }

    public void setTextColor(int rgb){
        selectedTabTextColor = rgb;
        updateTabStyle();
    }

    public void setIndicatorColor(int rgb){
        indicatorColor = rgb;
        invalidate();
    }

    public void setDividerColor(int rgb){
        dividerColor = rgb;
        invalidate();
    }

    public void setUnderLineColor(int rgb){
        underLineColor = rgb;
        invalidate();
    }
    class  CustomPageChangeListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                       currentPosition = position;
                       currentPositionOffset = positionOffset;
                       scrollToPositionTab(position,(int) (positionOffset * tabContainer.getChildAt(position).getWidth()));
                       invalidate();

            if(mPageChangeListener != null){
             mPageChangeListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToPositionTab(mViewPager.getCurrentItem(), 0);
            }

            if (mPageChangeListener != null) {
                mPageChangeListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            selectedPosition = position;
            updateTabStyle();
            if (mPageChangeListener != null) {
                mPageChangeListener.onPageSelected(position);
            }
        }
    }
}
