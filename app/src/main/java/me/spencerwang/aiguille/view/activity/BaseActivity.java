package me.spencerwang.aiguille.view.activity;

import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import me.spencerwang.aiguille.R;
import me.spencerwang.aiguille.util.ImageLoadUtil;

public class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }


    public void loadImageAsync(ImageView imageView,String url){
        ImageLoadUtil.loadImageAsync(imageView,url,null,getResources().getDrawable(R.drawable.media_img_default),50);
    }
    public void loadImageAsync(ImageView imageView,String url,String path){
        ImageLoadUtil.loadImageAsync(imageView,url,path,getResources().getDrawable(R.drawable.media_img_default),50);
    }
    public void loadImageAsync(ImageView imageView,String url,String path,Drawable drawable){
        ImageLoadUtil.loadImageAsync(imageView,url,null,drawable,50);
    }
    public void loadImageAsync(ImageView imageView,String url,String path,Drawable drawable,ImageLoadUtil.OnLoadImageFinishListener listener){
        ImageLoadUtil.loadImageAsync(imageView,url,null,drawable,50,listener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
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
