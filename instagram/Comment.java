package xunhu.instagram;

import android.graphics.Bitmap;

/**
 * Created by hu on 6/10/2015.
 */
public class Comment {
    String profile_url;
    String profile_username="";
    String time = "15m";
    String uploadPhoto_url;
    int numoflikes = 0;
    String mediaId = "";
    String comment="";
    Boolean isLikeOrNot = false;
    long time_long;
    public Bitmap profile_photo;
    public Bitmap posts;


    public Comment(String profile_url,String profile_username,String time, String uploadPhoto_url,int numoflikes,String comment,Boolean isLikeOrNot, long time_long,String mediaId){
               this.profile_url=profile_url;
               this.profile_username=profile_username;
               this.time = time;
               this.uploadPhoto_url=uploadPhoto_url;
               this.numoflikes=numoflikes;
               this.comment=comment;
               this.isLikeOrNot = isLikeOrNot;
               this.time_long=time_long;
               this.mediaId = mediaId;
    }
    public String getProfile_url(){
          return profile_url;
    }
    public String getProfile_username(){
          return profile_username;
    }
    public String getTime(){
          return time;
    }
    public String getUploadPhoto_url(){
          return  uploadPhoto_url;
    }
    public int getNumoflikes(){
          return numoflikes;
    }
    public String getComment(){
          return comment;
    }
    public Boolean getIsLikeOrNot(){return isLikeOrNot;}
    public void setNumoflikes(int num){
         this.numoflikes=num;
    }
    public void setIsLikeOrNot(Boolean a){
        this.isLikeOrNot = a;
    }
    public void setComment(String comment){
        this.comment = comment;
    }
    public void setTime(String t){
         this.time=t;
    }
    public long getTime_long(){
        return time_long;
    }
    public String getId(){return  mediaId;};
}
