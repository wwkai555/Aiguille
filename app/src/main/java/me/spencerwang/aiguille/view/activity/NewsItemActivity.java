package me.spencerwang.aiguille.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.spencerwang.aiguille.R;
import me.spencerwang.aiguille.entity.News;

public class NewsItemActivity extends BaseActivity {

    @InjectView(R.id.img_back)
    LinearLayout mBack;

    @InjectView(R.id.tv_title)
    TextView mTitleBar;

    @InjectView(R.id.title)
    TextView mTitle;

    @InjectView(R.id.content)
    TextView mContent;

    @InjectView(R.id.icon)
    ImageView mIcon;

    private News mNews;
    private int mColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_item);
        ButterKnife.inject(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
        mNews = News.getFromJosn(bundle.getString("value"));
        }

        initControl();
        initData();
    }


    private void initControl(){
        mTitleBar.setText(R.string.detail_news);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    private void initData(){
        if(mNews.getTitle() != null){
            mTitle.setText(mNews.getTitle());
        }

        if(mNews.getContent() != null){
            mContent.setText(mNews.getContent());
        }

        if(mNews.getImage() != null){
            News.NewsImage images = mNews.getImage();
            loadImageAsync(mIcon,images.large);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
