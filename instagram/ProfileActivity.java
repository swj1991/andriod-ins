package xunhu.instagram;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import instapi.instagram.InstagramProfileLoader;
import instapi.instagram.InstagramSelfFeedLoader;
import instapi.instagram.api.model.Feed;
import instapi.instagram.api.model.Profile;
/**
 * Created by hu on 10/09/2015.
 */
//this activity is to display the profile photos and the number of following, follow and posts
public class ProfileActivity extends FragmentActivity {
    ImageView logout;
    ImageView profile_photo;
    TextView posts;
    TextView followers;
    TextView following;
    LinearLayout rows;
    InteractiveScrollView scroll_photos;
    int width;
    String profile_image;
    private TextView tvUsername;
    String[] array;
    List<String> links = new ArrayList<>();
    InstagramProfileLoader instagramProfileLoader;
    InstagramSelfFeedLoader instagramSelfFeedLoader;
    int num_of_links=0;
    public static String current_username="";
    public static int numberoffollowing=0;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.profile);

        logout = (ImageView)findViewById(R.id.iv_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                    new Thread(new Runnable() {
                        public void run() {

                            try {
                                instagramProfileLoader.deleteToken();
                                Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }}).start();

                        try {
                            instagramProfileLoader.deleteToken();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }}).start();




                }
        });

        LinearLayout lin = (LinearLayout)findViewById(R.id.line);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        rows = (LinearLayout)findViewById(R.id.line);
        scroll_photos = (InteractiveScrollView)findViewById(R.id.sent_photo_scroll);

        scroll_photos.setOnBottomReachedListener(new InteractiveScrollView.OnBottomReachedListener() {
            @Override
            public void onBottomReached() {



            }
        });
        followers = (TextView)findViewById(R.id.tv_followers);
        following = (TextView)findViewById(R.id.tv_following);
        tvUsername = (TextView)findViewById(R.id.tvUsername);
        posts = (TextView)findViewById(R.id.tv_posts);
        profile_photo = (ImageView)findViewById(R.id.iv_profile);
        // Bundle extract_data = this.getIntent().getExtras();



        instagramProfileLoader = new  InstagramProfileLoader(this);
        instagramSelfFeedLoader = new InstagramSelfFeedLoader(this);
        new Thread(new Runnable() {
            public void run() {
                //code something u want to do
                try {
                    final Profile userProfile = instagramProfileLoader.loadResourceInBackground();
                    final Feed userSelfFeed  = instagramSelfFeedLoader.loadResourceInBackground();

                    System.out.println(userProfile);
                    System.out.println(userSelfFeed);
                    //userSelfFeed.getData().get(0).getImages().getLowResolution().getUrl();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            posts.setText(userProfile.getData().getCounts().getMedia().toString());
                            tvUsername.setText(userProfile.getData().getUsername());
                            current_username = userProfile.getData().getUsername();
                            numberoffollowing = userProfile.getData().getCounts().getFollows();
                            followers.setText(userProfile.getData().getCounts().getFollowed_by().toString());
                            following.setText(userProfile.getData().getCounts().getFollows().toString());
                            new LoadingImage(profile_photo).execute(userProfile.getData().getProfilePicture());
                            num_of_links=userSelfFeed.getData().size();
                            if (num_of_links<=3){
                                LinearLayout linear = new LinearLayout(ProfileActivity.this);
                                for (int j=0;j<num_of_links;j++){
                                    ImageView iv = new ImageView(ProfileActivity.this);
                                    iv.setLayoutParams(new android.view.ViewGroup.LayoutParams(width/3,width/3));
                                    iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                    iv.setImageResource(R.drawable.loading_icon);
                                    new LoadingImage(iv).execute(userSelfFeed.getData().get(j).getImages().getLowResolution().getUrl());
                                    linear.addView(iv);
                                }
                                rows.addView(linear);
                            }else{
                                int numofrows = 0;
                                if((double)num_of_links/3.0==num_of_links/3){
                                    numofrows = num_of_links/3;
                                }else {
                                    numofrows = num_of_links/3+1;
                                }
                                for(int i=0;i<numofrows;i++){
                                    LinearLayout linear = new LinearLayout(ProfileActivity.this);
                                    if (i<numofrows-1){
                                        for (int j=3*i;j<3*i+3;j++){
                                            ImageView iv = new ImageView(ProfileActivity.this);
                                            iv.setLayoutParams(new android.view.ViewGroup.LayoutParams(width/3,width/3));
                                            iv.setScaleType(ImageView.ScaleType.FIT_XY);
                                            iv.setImageResource(R.drawable.loading_icon);
                                            linear.addView(iv);
                                            new LoadingImage(iv).execute(userSelfFeed.getData().get(j).getImages().getLowResolution().getUrl());
                                        }
                                    }else {
                                        for (int j=3*(i);j<num_of_links; j++){
                                            ImageView iv = new ImageView(ProfileActivity.this);
                                            iv.setLayoutParams(new android.view.ViewGroup.LayoutParams(width/3,width/3));
                                            iv.setScaleType(ImageView.ScaleType.FIT_XY);
                                            iv.setImageResource(R.drawable.loading_icon);
                                            linear.addView(iv);
                                            new LoadingImage(iv).execute(userSelfFeed.getData().get(j).getImages().getLowResolution().getUrl());
                                        }
                                    }
                                    rows.addView(linear);

                                }
                            }


                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        ).start();



    }
    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.finishAll();
    }




}
