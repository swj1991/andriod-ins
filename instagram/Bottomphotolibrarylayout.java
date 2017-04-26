package xunhu.instagram;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by hu on 29/09/2015.
 */

//Create Bottom menu layout of camera which include photo, library and photo gallery
public class Bottomphotolibrarylayout extends RelativeLayout {
       TextView tvPhoto;
       TextView tvLibrary;

    public Bottomphotolibrarylayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.bottom_photo_menu,this);

        tvLibrary = (TextView) findViewById(R.id.tvLibrary);
        tvPhoto = (TextView) findViewById(R.id.tPhoto);
        if (getContext().getClass()==UploadActivity.class){
            tvLibrary.setTextColor(Color.parseColor("#4090db"));
            tvPhoto.setTextColor(Color.WHITE);
        }else{
            tvLibrary.setTextColor(Color.WHITE);
            tvPhoto.setTextColor(Color.parseColor("#4090db"));
        }
        tvLibrary.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getContext().getClass()!=UploadActivity.class){
                   ((Activity) getContext()).onBackPressed();
                }
            }
        });

        tvPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getContext().getClass()!=TakephotosActivity.class){
                    Intent intent = new Intent((Activity)getContext(), TakephotosActivity.class);

                    ((Activity)getContext()).startActivity(intent);
                }
            }
        });
    }
}
