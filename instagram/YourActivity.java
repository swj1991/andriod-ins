package xunhu.instagram;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ListView;
import android.widget.TabHost;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import instapi.instagram.InstagramSelfFeedLoader;
import instapi.instagram.InstagramSelfFollowedByLoader;
import instapi.instagram.InstagramSelfFollowsLoader;
import instapi.instagram.InstagramSelfMediaBeLikedLoader;
import instapi.instagram.InstagramSelfMediaLikedLoader;
import instapi.instagram.InstagramUserMediaRecentLoader;
import instapi.instagram.InstagramUsersMediaCommentLoader;
import instapi.instagram.api.model.Comments;
import instapi.instagram.api.model.Feed;
import instapi.instagram.api.model.FeedItem;
import instapi.instagram.api.model.User;
import instapi.instagram.api.model.Users;

/**
 * Created by hu on 10/09/2015.
 */
//create activity that display the Folowing and You event
public class YourActivity extends FragmentActivity   {

    private InstagramSelfFollowsLoader instagramSelfFollowsLoader;
    private InstagramSelfFollowedByLoader instagramSelfFollowedByLoader;

    List<Feed> userFollows = new ArrayList<Feed>();
    List<Feed> userFollowedBy = new ArrayList<Feed>();
    List<FollowingAndYou> person_following;
    Feed selLikedFeed;
    ListView lv;
    ListView lv2;
    private InstagramSelfMediaLikedLoader instagramSelfMediaLikedLoader;
    boolean isFinish = false;
    private InstagramSelfFeedLoader instagramSelfFeedLoader;

    protected void onCreate(Bundle s){
        super.onCreate(s);
        setContentView(R.layout.activity_layout);
        lv= (ListView) findViewById(R.id.list_following);
        lv2 = (ListView)findViewById(R.id.list_You);
        TabHost th = (TabHost)findViewById(R.id.tabHost);
        th.setup();
        final TabHost.TabSpec following = th.newTabSpec("tag1");
        following.setContent(R.id.FOLLOWING);
        following.setIndicator("FOLLOWING");
        th.addTab(following);
        ListView fl = (ListView) findViewById(R.id.list_following);
        final ProgressDialog loading = new ProgressDialog(YourActivity.this);
        loading.setTitle("Welcome to Activity");
        loading.setMessage("Loading data...");
        loading.show();

        TabHost.TabSpec You = th.newTabSpec("tag2");
        You.setContent(R.id.YOU);
        You.setIndicator("You");
        th.addTab(You);
        ActivityCollector.addActivity(this);




        instagramSelfFollowsLoader = new InstagramSelfFollowsLoader(this);
        instagramSelfFollowedByLoader = new InstagramSelfFollowedByLoader(this);
        instagramSelfMediaLikedLoader = new InstagramSelfMediaLikedLoader(this);


        instagramSelfFeedLoader = new InstagramSelfFeedLoader(this);




        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Users selfFollows = instagramSelfFollowsLoader.loadResourceInBackground();

                    for (User u : selfFollows.getData()) {
                        InstagramUserMediaRecentLoader ts = new InstagramUserMediaRecentLoader(YourActivity.this);
                        ts.userId = u.getId();
                        Feed f = null;
                        try {
                            f = ts.loadResourceInBackground();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        userFollows.add(f);

                    }
                    isFinish = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            List<FollowingAndYou> person_list = new ArrayList<FollowingAndYou>();
                            System.out.println(userFollows);
                            for (Feed f:userFollows) {
                                if (f != null)
                                    for (FeedItem ft:f.getData()) {
                                        String profile_url = ft.getUser().getProfilePicture();
                                        String profile_username = ft.getUser().getUsername();
                                        String event = "has post a photo";
                                        String time = "";

                                        long time_long = ft.getCreatedTime();
                                        Date date = new Date(time_long*1000L);
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm");
                                        sdf.setTimeZone(TimeZone.getDefault());
                                        String formattedDate = sdf.format(date);


                                        SimpleDateFormat sdf_2 = new SimpleDateFormat("yyyyMMdd_HHmm");
                                        sdf_2.setTimeZone(TimeZone.getDefault());
                                        String currentDateandTime = sdf_2.format(new Date());
                                        try {
                                            Date date_post = sdf.parse(formattedDate);
                                            Date current_date = sdf_2.parse(currentDateandTime);
                                            long diff = current_date.getTime()-date_post.getTime();


                                            String post_url = ft.getImages().getLowResolution().getUrl();
                                            FollowingAndYou following_event = new FollowingAndYou(profile_url,profile_username,event,time,post_url,diff);
                                            person_list.add(following_event);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                            }

                            Collections.sort(person_list, new Comparator<FollowingAndYou>() {
                                @Override
                                public int compare(FollowingAndYou lhs, FollowingAndYou rhs) {
                                    if (lhs.getTimeLong()>rhs.getTimeLong()){
                                        return 1;
                                    }else {
                                        return -1;
                                    }
                                }
                            });

                            List<FollowingAndYou> list_following = new ArrayList<FollowingAndYou>();
                            for(int i=0;i<11;i++){
                                String user_profile_url = person_list.get(i).getURL();
                                String user_name = person_list.get(i).getUsername();
                                String post_url = person_list.get(i).getPost_url();
                                long time = person_list.get(i).getTimeLong();
                                double time_gap = Double.parseDouble(String.valueOf(time));
                                String time_diff="";
                                if (time_gap/1000.0/60.0<1){
                                    time_diff =String.valueOf(time_gap/1000.0)+"s";
                                }else if (time_gap/1000.0/60.0<60.0 && time_gap/1000.0/60.0>=1.0){
                                    time_diff =String.valueOf(time/1000/60)+"m";
                                }else if (time_gap/1000.0/60.0/60.0>=1 && time_gap/1000.0/60.0/60.0<24){
                                    time_diff = String.valueOf(time/1000/3600)+"h";
                                }

                                FollowingAndYou a = new FollowingAndYou(user_profile_url,user_name,"has post a photo",time_diff,post_url,time);
                                list_following.add(a);
                            }
                            loading.cancel();
                            FollowingAdapter adapter = new FollowingAdapter(YourActivity.this,R.layout.following_layout,list_following);
                            lv.setAdapter(adapter);
                            lv.invalidateViews();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();



        new Thread(new Runnable(){

            @Override
            public void run() {
                try {
                   // final Users selfFollowedBy = instagramSelfFollowedByLoader.loadResourceInBackground();
                    selLikedFeed = instagramSelfMediaLikedLoader.loadResourceInBackground();
                    final Feed userSelfFeed  = instagramSelfFeedLoader.loadResourceInBackground();
                    final List<FollowingAndYou> like_list = new ArrayList<FollowingAndYou>();
                    for(FeedItem fi:userSelfFeed.getData()){
                        String mediaId = fi.getId();
                        InstagramSelfMediaBeLikedLoader instagramSelfMediaBeLikedLoader = new InstagramSelfMediaBeLikedLoader(YourActivity.this);
                        instagramSelfMediaBeLikedLoader.mediaId = mediaId;

                        InstagramUsersMediaCommentLoader instagramUsersMediaCommentLoader = new InstagramUsersMediaCommentLoader(YourActivity.this);
                        instagramUsersMediaCommentLoader.mediaId = mediaId;

                        Users users = null;
                        Comments comments = null;
                        System.out.println("***********");
                        System.out.println(mediaId);

                        try {
                            users = instagramSelfMediaBeLikedLoader.loadResourceInBackground();
                            comments = instagramUsersMediaCommentLoader.loadResourceInBackground();
                            String liked_photo = fi.getImages().getLowResolution().getUrl();

                            for (int i=0;i<users.getData().size();i++){
                                String othername = users.getData().get(i).getUsername();
                                String user_profile_url = users.getData().get(i).getProfilePicture();
                                FollowingAndYou person = new FollowingAndYou(user_profile_url,othername,"liked your photo","",liked_photo,0);
                                like_list.add(person);
                            }
                            for (int i=0;i<comments.getData().size();i++){
                                 String comment_username = comments.getData().get(i).getUser().getUsername();
                                 String url = comments.getData().get(i).getUser().getProfilePicture();
                                 String content = comments.getData().get(i).getText();
                                 FollowingAndYou person = new FollowingAndYou(url,comment_username,"commented your photo: "+content,"",liked_photo,0);
                                 like_list.add(person);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Collections.sort(like_list,new Comparator<FollowingAndYou>() {
                                @Override
                                public int compare(FollowingAndYou lhs, FollowingAndYou rhs) {
                                    if (lhs.getUsername().compareTo(rhs.getUsername())<0){
                                             return 1;
                                    }else {
                                            return -1;
                                    }
                                }
                            });
                            List<FollowingAndYou> top_11_like_list = new ArrayList<FollowingAndYou>();
                            if (like_list.size()<=11){
                                for(int i=0;i<like_list.size();i++){
                                    top_11_like_list.add(like_list.get(i));
                                }
                            }else {
                                for (int i=0;i<11;i++){
                                    top_11_like_list.add(like_list.get(i));
                                }
                            }
                            FollowingAdapter adapter = new FollowingAdapter(YourActivity.this,R.layout.following_layout,like_list);
                            lv2.setAdapter(adapter);
                            lv2.invalidateViews();


                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.finishAll();
    }


}
