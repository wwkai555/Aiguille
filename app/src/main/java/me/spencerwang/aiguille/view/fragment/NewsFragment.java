package me.spencerwang.aiguille.view.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.spencerwang.aiguille.R;
import me.spencerwang.aiguille.common.App;
import me.spencerwang.aiguille.dao.NewsDataHelper;
import me.spencerwang.aiguille.data.GsonRequest;
import me.spencerwang.aiguille.data.TestData;
import me.spencerwang.aiguille.entity.BaseCategory;
import me.spencerwang.aiguille.entity.News;
import me.spencerwang.aiguille.util.TaskUtil;
import me.spencerwang.aiguille.util.ToastUtils;
import me.spencerwang.aiguille.view.activity.NewsItemActivity;
import me.spencerwang.aiguille.view.adapter.NewsAdapter;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 */
public class NewsFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> ,SwipeRefreshLayout.OnRefreshListener{

    private int currentNewsCategoty = 0;

    private NewsDataHelper mNewsHelper;

    /**
     * The fragment's RecyclerView.
     */
    @InjectView(R.id.newsList)
    RecyclerView mRecyclerview;

    @InjectView(R.id.refreashLayout)
    SwipeRefreshLayout refreshLayout;

    RecyclerView.ItemAnimator itemAnimator ;

    /**
     * The Adapter which will be used to populate the RecyclerView with
     * Views.
     */
    private NewsAdapter mAdapter;

    /**
     * The Listener which will be used to populate the RecyclerView with Views click event.
     */
    private NewsAdapter.OnItemClickListener onItemClickListener;

    private String page = "0";

    // TODO: Rename and change types of parameters
    public static NewsFragment newInstance(int position) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    public static NewsFragment newInstance(){
        NewsFragment fragment = new NewsFragment();
        return  fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentNewsCategoty = getArguments().getInt("position");
        }
        mNewsHelper = new NewsDataHelper(getActivity());

        //loading test data if you need
        new TestData(mNewsHelper).loadTestData();

        initListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.inject(this,view);
        view.setBackgroundResource(BGDRAWABLE[0]);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setRefreshing(false);
        refreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        // TODO: Change Adapter to display your content
        mAdapter = new NewsAdapter(getActivity(),mRecyclerview);
        mAdapter.setOnItemClickListener(onItemClickListener);

        mRecyclerview.setAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);
        return view;
    }

    private void initListener(){
        onItemClickListener = new NewsAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View v, Bundle bundle) {
                Intent intent = new Intent(getActivity(), NewsItemActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            @Override
            public void onItemLongClick(View view, int position) {
                ToastUtils.showShort("long click");
            }
        };
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(mNewsHelper == null){
            mNewsHelper = new NewsDataHelper(getActivity());
        }
        return mNewsHelper.getCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.i("sql","load finish");
        setRefreshing(false);
        mAdapter.changeCursor(cursor);
        if(cursor != null && cursor.getCount() <= 0){
        loadFirst();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
       mAdapter.changeCursor(null);
    }

    @Override
    public void onRefresh() {
        mAdapter.setIsRefreash(true);
        loadFirst();
    }

    @Override
    public void notifyDataCategorySetChange(int tag) {
        if(currentNewsCategoty == tag){
            return;
        }
        currentNewsCategoty =  tag;
        mNewsHelper.setCategoryChange(currentNewsCategoty);
        getLoaderManager().restartLoader(0,null,this);
    }

    private void loadFirst(){
        page = "0";
        loadData(page);
    }
    private void loadData(String page){
        if(refreshLayout.isRefreshing() && page.equals("0")){
        setRefreshing(true);
        }
      executeRequest(new GsonRequest(String.format(App.SERVER,BaseCategory.Category.NEWS,page),News.RequestData.class,getResponseListener(),getResponseErrorListener()));
    }

    private Response.Listener<News.RequestData> getResponseListener(){
        final boolean isRefreshFromTop = ("0".equals(page));
        return new Response.Listener<News.RequestData>() {
            @Override
            public void onResponse(final News.RequestData requestData) {
                TaskUtil.executeAsyncTask(new AsyncTask<Object, Object, Object>() {
                    @Override
                    protected Object doInBackground(Object... params) {
                        if(isRefreshFromTop){
                            mNewsHelper.deleteAll();
                        }
                        page = requestData.getPage();
                        List<News> newsList = requestData.getNewsList();
                        if(newsList != null){
                            mNewsHelper.batchInsert(newsList);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                            setRefreshing(false);
                    }
                });

            }
        };
    }
    private void setRefreshing(boolean b) {
        refreshLayout.setRefreshing(b);
    }

    private Response.ErrorListener getResponseErrorListener(){

        return  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showShort(volleyError.getMessage());
                setRefreshing(false);
            }
        };
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mNewsHelper != null){
        new TestData(mNewsHelper).deleteTestData();
        }
    }
}
