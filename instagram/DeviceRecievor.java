package xunhu.instagram;

import android.graphics.Bitmap;

/**
 * Created by hu on 12/10/2015.
 */
//create a class for found device through bluetooth
public class DeviceRecievor {
    String user_url = "";
    String username = "";
    String event = "send you a photo";
    Bitmap bitmap=null;

    public  DeviceRecievor(String user_url, String username,String event,Bitmap bitmap){
          this.bitmap = bitmap;
          this.user_url = user_url;
          this.event=event;
          this.username=username;
    }
    public String getUser_url(){ return user_url;}
    public String getUsername(){ return  username;}
    public String getEvent(){ return event;}
    public Bitmap getBitmap(){return bitmap;}
}
