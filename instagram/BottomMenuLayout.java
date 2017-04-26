package xunhu.instagram;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by hu on 10/09/2015.
 */
public class BottomMenuLayout extends LinearLayout {
        Button instagram;
        Button search;
        Button camera;
        Button activity;
        Button profile;

    public BottomMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.bottom_menu,this);
        //components for the bottom menu
        instagram = (Button) findViewById(R.id.btnHome);
        search = (Button) findViewById(R.id.btnSearch);
        camera = (Button)findViewById(R.id.btnCamera);
        activity = (Button)findViewById(R.id.btnActivity);
        profile = (Button) findViewById(R.id.btnProfile);
        profile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getContext().getClass()!=ProfileActivity.class){
                    //move to profile activity
                    Intent intent = new Intent((Activity)getContext(), ProfileActivity.class);
                    ((Activity)getContext()).startActivity(intent);
                    Toast.makeText(getContext(),"Your ProfileActivity",Toast.LENGTH_SHORT).show();
                }

            }
        });
        activity.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //move to profile Youractivity
               if(getContext().getClass()!=YourActivity.class){
                    Intent intent = new Intent((Activity)getContext(), YourActivity.class);
                    ((Activity)getContext()).startActivity(intent);
                }
            }
        });
        instagram.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //move to profile UserFeedActivity
                if(getContext().getClass()!=UserFeedActivity.class){
                    Intent intent = new Intent((Activity)getContext(), UserFeedActivity.class);
                    ((Activity)getContext()).startActivity(intent);
                }
            }
        });
        search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //move to SearchActivity
                if(getContext().getClass()!=SearchActivity.class){
                    Intent intent = new Intent((Activity)getContext(), SearchActivity.class);
                    ((Activity)getContext()).startActivity(intent);
                }
            }
        });
        camera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //move to UploadActivity
                if(getContext().getClass()!=UploadActivity.class){
                    Intent intent = new Intent((Activity)getContext(), UploadActivity.class);
                    ((Activity)getContext()).startActivity(intent);
                }
            }
        });
    }
}
