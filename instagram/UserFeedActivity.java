package xunhu.instagram;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import instapi.instagram.InstagramFeedLoader;
import instapi.instagram.InstagramPutCommentLoader;
import instapi.instagram.InstagramUsersMediaLikeLoader;
import instapi.instagram.api.model.Feed;
import instapi.instagram.api.model.FeedItem;
import instapi.instagram.api.model.User;
import instapi.instagram.api.model.Users;

/**
 * Created by hu on 10/09/2015.
 */
//create the Activity that list the user feed
public class UserFeedActivity extends FragmentActivity {
    private List<Comment> comments_feed = new ArrayList<Comment>();
    ListView lv;
    private InstagramFeedLoader instagramFeedLoader;
    private Feed selfFeed;
    private InstagramPutCommentLoader instagramPutCommentLoader;
    ImageView iv_to_receivor;


    private class PostCommentTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {



        }

        @Override
        protected String doInBackground(String... data) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://api.instagram.com/v1/media/"+data[0]+"/comments");

            try {

                httppost.addHeader("text", data[1]);
                httppost.addHeader("access_token", TmpVars.token);
                //execute http post
                HttpResponse response = httpclient.execute(httppost);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String json = reader.readLine();


                JSONObject jsonStr = new JSONObject(json);

                JSONObject meta = (JSONObject) jsonStr.get("meta");


                final String errorInfo = (String) meta.get("error_type");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UserFeedActivity.this.disPlayCommentToast(errorInfo);
                    }
                });

                System.out.println("uiui"+errorInfo);
                System.out.println("uiuiuiuiiuiu");

            } catch (ClientProtocolException e) {

            } catch (IOException e) {

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return  "";
        }
    }

    private class PostLikeTask extends AsyncTask<String, String, String> {


        @Override
        protected void onPostExecute(String s) {



        }

        @Override
        protected String doInBackground(String... data) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://api.instagram.com/v1/media/"+data[0]+"/likes");
            try {

                httppost.addHeader("access_token", TmpVars.token);
                //execute http post
                HttpResponse response = httpclient.execute(httppost);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String json = reader.readLine();


                JSONObject jsonStr = new JSONObject(json);

                JSONObject meta = (JSONObject) jsonStr.get("meta");


                final String errorInfo = (String) meta.get("error_type");

                System.out.println("uiui" + errorInfo);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UserFeedActivity.this.displayToast(errorInfo);
                    }
                });





                System.out.println("uiuiuiuiiuiu");


            } catch (ClientProtocolException e) {

            } catch (IOException e) {

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return  "";
        }
    }

    @Override
    protected void onCreate(Bundle s){
           super.onCreate(s);
           setContentView(R.layout.instagram_layout);
           lv = (ListView)findViewById(R.id.user_feed);
        iv_to_receivor= (ImageView)findViewById(R.id.iv_receive_bluetooth);
        iv_to_receivor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserFeedActivity.this,BluetoothReceivor.class);
                startActivity(intent);
            }
        });
        final ProgressDialog loading = new ProgressDialog(UserFeedActivity.this);
        loading.setTitle("Welcome to user feed");
        loading.setMessage("Loading data...");
        loading.show();
           ActivityCollector.addActivity(this);


        //test();

//        // Enable Local Datastore.
//        Parse.enableLocalDatastore(this);
//
//        Parse.initialize(this, "rD25wtCtrewKQLMECOdA45qlJnD2atztkkUJT78Z", "eCoOryLCtmfSDJfNSDq605Dx0qQQ6ck9aIm590P0");


        instagramPutCommentLoader = new InstagramPutCommentLoader(this);
        instagramPutCommentLoader.mediaId = "1091934601622951851_538113172";
        instagramPutCommentLoader.text = "1234";

           new Thread(new Runnable() {
               public void run() {
                   //code something u want to do
                   try {

//                       System.out.println("----------------");
//                       Meta mv = instagramPutCommentLoader.loadResourceInBackground();
//                       System.out.println(mv);

                       //new PostTask().doInBackground();

                       instagramFeedLoader = new InstagramFeedLoader(UserFeedActivity.this);
                       selfFeed = instagramFeedLoader.loadResourceInBackground();
                       final String current_username = ProfileActivity.current_username;
                       final List<Comment> user_comment = new ArrayList<Comment>();



                       for (FeedItem u : selfFeed.getData()){
                           Boolean isLikeOrNot = false;
                           String profile_url = u.getUser().getProfilePicture();
                           String profile_username = u.getUser().getUsername();
                           long event_time = u.getCreatedTime();
                           int likes = u.getLikes().getCount();
                            String the_id=u.getId();

                           InstagramUsersMediaLikeLoader instagramUsersMediaLikeLoader = new InstagramUsersMediaLikeLoader(UserFeedActivity.this);
                           instagramUsersMediaLikeLoader.mediaId = u.getId();

                           Users usersLikes = instagramUsersMediaLikeLoader.loadResourceInBackground();

                           for (User usr: usersLikes.getData()){
                               if(usr.getUsername().equals(current_username)){
                                   isLikeOrNot=true;
                               }
                           }
                           String all_comments = "";
                           for (int i=0;i<u.getComments().getData().size();i++){
                               String username=u.getComments().getData().get(i).getUser().getUsername();
                               String user_comment2=u.getComments().getData().get(i).getText();
                               String one_line = username+": "+user_comment2;
                               all_comments+=one_line+"\n";
                           }

                           Date date = new Date(event_time*1000L);
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
                               double time_gap = Double.parseDouble(String.valueOf(diff));
                               String time_diff="";
                               if (time_gap/1000.0/60.0<1){
                                   time_diff =String.valueOf(time_gap/1000.0)+"s";
                               }else if (time_gap/1000.0/60.0<60.0 && time_gap/1000.0/60.0>=1.0){
                                   time_diff =String.valueOf(diff/1000/60)+"m";
                               }else if (time_gap/1000.0/60.0/60.0>=1 && time_gap/1000.0/60.0/60.0<24){
                                   time_diff = String.valueOf(diff/1000/3600)+"h";
                               }else if (time_gap/1000.0/60.0/60.0>=24){
                                   time_diff = String.valueOf(diff/1000/3600/24)+"d";
                               }
                               String post_url = u.getImages().getLowResolution().getUrl();

                               Comment like_event = new Comment(profile_url,profile_username,time_diff,post_url,likes,all_comments,isLikeOrNot,0,the_id);
                               isLikeOrNot=false;
                               user_comment.add(like_event);

                           } catch (ParseException e) {
                               e.printStackTrace();
                           }

                       }


                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               UserfeedAdapter adpter = new UserfeedAdapter(UserFeedActivity.this,R.layout.user_feed_layout,user_comment);
                               lv.setAdapter(adpter);
                               loading.cancel();

                           }
                       });
                   }catch (Exception e){
                        e.printStackTrace();
                   }

               }
           }).start();





       }
    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.finishAll();
    }

    public class UserFeedLayout extends RelativeLayout {
        public UserFeedLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
            LayoutInflater.from(context).inflate(R.layout.user_feed_layout,this);

        }
    }
    private class UserfeedAdapter extends ArrayAdapter<Comment>{
            private int resourceId;
        public UserfeedAdapter(Context context, int resource, List<Comment> object) {
            super(context, resource, object);
            resourceId = resource;
        }

        public View getView(int position, View convertView, ViewGroup parent){
              final Boolean isLike = false;
              final Comment comment = getItem(position);
             int numofloves = 0;
             View view = LayoutInflater.from(getContext()).inflate(resourceId,null);

             ImageView iv_profile = (ImageView)view.findViewById(R.id.iv_profile_feed);


            System.out.println("77777777000007777");
            System.out.println("777777"+iv_profile);



            if (comment.profile_photo==null){
                new LoadingImage(iv_profile,comment).execute(comment.getProfile_url());
            }else {
                iv_profile.setImageBitmap(comment.profile_photo);
            }

             TextView tv_profile_username = (TextView)view.findViewById(R.id.user_feed_name);
             tv_profile_username.setText(comment.getProfile_username());

             TextView tv_time = (TextView)view.findViewById(R.id.tv_time);
             tv_time.setText(String.valueOf(comment.getTime()));

             ImageView iv_post_photo = (ImageView)view.findViewById(R.id.user_feed_image);
            if (comment.posts==null){
                new LoadingImage(iv_post_photo,comment).execute(comment.getUploadPhoto_url());
            }else {
                iv_post_photo.setImageBitmap(comment.posts);
            }


             final ImageView like = (ImageView)view.findViewById(R.id.iv_like);

            if (comment.getIsLikeOrNot()==true){
                like.setImageResource(R.drawable.love_icon_red);
            }else {
                like.setImageResource(R.drawable.love_icon_white);
            }

            ImageView iv_comment = (ImageView)view.findViewById(R.id.iv_comment);

              final TextView tv_numoflikes = (TextView)view.findViewById(R.id.tv_numoflikes);
              tv_numoflikes.setText(String.valueOf(comment.getNumoflikes()));

              final TextView user_comments = (TextView)view.findViewById(R.id.tv_comment);
              user_comments.setText(comment.getComment());
              final EditText et_make_comments = (EditText)view.findViewById(R.id.et_make_comments);

              Button btn_comment = (Button)view.findViewById(R.id.btn_comment);


            btn_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = ProfileActivity.current_username;
                    String previous = user_comments.getText().toString();
                    final String the_comment = et_make_comments.getText().toString();
                    System.out.println("username:"+username);
                    System.out.println("previous: "+previous);

                    String latest = previous+"\n"+username+": "+et_make_comments.getText().toString();
                    et_make_comments.setText("");
                    comment.setComment(latest);

                    user_comments.setText(latest);
                    System.out.println("latest:" + latest);
                    System.out.println("edit text:  "+et_make_comments.getText().toString());

                    et_make_comments.requestFocus();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            new PostCommentTask().doInBackground(comment.getId(),the_comment);
                        }
                    }).start();
                }
            });

              iv_comment.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      et_make_comments.requestFocus();
                  }
              });

                   like.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {

                          new Thread(new Runnable() {
                              @Override
                              public void run() {
                                  new PostLikeTask().doInBackground(comment.getId());
                              }
                          }).start();

                        if (comment.getIsLikeOrNot()==true){
                            like.setImageResource(R.drawable.love_icon_white);
                            comment.setIsLikeOrNot(false);
                            int likes = comment.getNumoflikes()-1;
                            comment.setNumoflikes(likes);
                            tv_numoflikes.setText(String.valueOf(comment.getNumoflikes()));


                        }else {
                            like.setImageResource(R.drawable.love_icon_red);
                            comment.setIsLikeOrNot(true);
                            int likes = comment.getNumoflikes()+1;
                            comment.setNumoflikes(likes);
                            tv_numoflikes.setText(String.valueOf(comment.getNumoflikes()));
                        }
                       }
                   });
                        return view;
        }

    }
    protected void displayToast(String errorInfo){

        Toast.makeText(UserFeedActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
    }
    protected void disPlayCommentToast(String errorInfo){
        Toast.makeText(UserFeedActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
    }





}
