package me.spencerwang.aiguille.view.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.spencerwang.aiguille.R;

/**
 * Created by SpencerWang on 2015/4/28.
 */
public class FooterView {
    private View returnView;
    private State mCurrentState = State.Idle;
    @InjectView(R.id.loading)
    TextView loading;

    @InjectView(R.id.the_end)
    TextView theEnd;

   public static enum State{ Idle, TheEnd, Loading}
   private static FooterView footerView;
    private FooterView(Context context){
        returnView = LayoutInflater.from(context).inflate(R.layout.layout_footer_view,null);
        ButterKnife.inject(this,returnView);
    }

    public static FooterView newInstance(Context context){
        if(footerView == null){
            footerView = new FooterView(context);
        }
        return  footerView;
    }

    public View getReturnView(){
        return  returnView;
    }

    public  void setState(State state){
        if(mCurrentState == state){
            return;
        }
        mCurrentState = state;
        switch (state){
            case Idle:
                loading.setVisibility(View.GONE);
                theEnd.setVisibility(View.GONE);
                break;
            case TheEnd:
                loading.setVisibility(View.GONE);
                theEnd.setVisibility(View.VISIBLE);
                break;
            case Loading:
                loading.setVisibility(View.VISIBLE);
                theEnd.setVisibility(View.GONE);
                break;
            default:
                break;
        }

    }

    public State getmCurrentState(){
        return  mCurrentState;
    }


}
