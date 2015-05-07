package me.spencerwang.aiguille.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.spencerwang.aiguille.R;
import me.spencerwang.aiguille.entity.News;
import me.spencerwang.aiguille.util.ImageLoadUtil;

/**
 * Created by SpencerWang on 2015/3/11.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private Context mContext;
    private  boolean isRefreash = true;
    private static int mPrePosition;
    private OnItemClickListener mOnItemClickListener;
    private RecyclerView mView;
    private List<News> newsList = new ArrayList<>();


    public NewsAdapter(Context context,RecyclerView view){
        mContext = context;
        mView = view;
    }

    public void addData(News news){
        if(news  != null){
            newsList.add(news);
        }
    }

    public void changeCursor(Cursor cursor){
        newsList.removeAll(newsList);
        if(cursor != null){
            Log.i("sql","news size:"+cursor.getCount()+"  position:"+ cursor.getPosition());

            if(cursor != null && cursor.getCount() >= 0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    News news = News.getFromCursor(cursor);
                    addData(news);
                }
                if(!isRefreash){
                    mView.scrollToPosition(mPrePosition);
                }else{
                    mView.scrollToPosition(0);
                }
                notifyDataSetChanged();
                mPrePosition = newsList.size();
            }
        }
    }

    private static final String TAG ="NewsAdapter";

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_news_item,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view,mOnItemClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(newsList != null && newsList.size() > 0){
        News news = newsList.get(position);
        holder.title.setText(news.getTitle());
        holder.introdution.setText(news.getContent());
        holder.bundle.putString("value",news.toJson());

        if(news.getImage().normal != null && !news.getImage().equals("")){
            ImageLoadUtil.loadImageAsync(holder.img,news.getImage().large, null,mContext.getResources().getDrawable(R.drawable.default_news_img),50);
        }else{
      }
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }


    public void setIsRefreash(boolean b) {
        isRefreash = b;

    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        private OnItemClickListener onClickListener;
       @InjectView(R.id.news_img)
        ImageView img;

       @InjectView(R.id.news_title)
        TextView title;

       @InjectView(R.id.introduction)
        TextView introdution;

        Bundle bundle = new Bundle();
        public MyViewHolder(View view,OnItemClickListener clickListener){
            super(view);
            ButterKnife.inject(this, view);
            onClickListener = clickListener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onItemClick(v,bundle);
        }

        @Override
        public boolean onLongClick(View v) {
            onClickListener.onItemLongClick(v,getPosition());
            return true;
        }
    }
    public interface OnItemClickListener{
        void onItemClick(View v ,Bundle bundle);
        void onItemLongClick(View view,int position);
    }
}
