package xunhu.instagram;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import instapi.instagram.InstagramActivity;


public class MainActivity extends InstagramActivity {
     Button register;
     Button sign;
     EditText email;
     Button login;
   // ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    @Override
    protected void onStop(){
        super.onStop();

        finish();
    }



}
