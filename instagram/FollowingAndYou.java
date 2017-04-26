package xunhu.instagram;

import android.graphics.Bitmap;

/**
 * Created by hu on 5/10/2015.
 */
//create class which stands for Activity event
public class FollowingAndYou {
      private String url;
      private String event;
      private String username;
      private String time;
      private String post_url;
      private long time_long;
      public Bitmap bt1;
      public Bitmap bt2;

    public FollowingAndYou(String url, String username, String event, String time,String post_url,long time_long){
        this.url=url;
        this.event = event;
        this.username=username;
        this.time=time;
        this.post_url = post_url;
        this.time_long=time_long;
    }

    public String getURL(){
        return url;
    }
    public String getEvent(){
        return event;
    }
    public String getUsername(){
        return username;
    }
    public String getTime(){return time;}
    public String getPost_url(){return post_url; }
    public long getTimeLong(){return time_long;}


}
