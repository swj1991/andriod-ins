package xunhu.instagram;

import android.graphics.Bitmap;

/**
 * Created by hu on 12/10/2015.
 */
//create a class to for the found device through
public class BluetoothFound  {
    String username ="";
    String event = "send you a photo";
    Bitmap bitmap=null;
    public BluetoothFound(String username,String event,Bitmap bitmap){
        this.username=username;
        this.event=event;
        this.bitmap=bitmap;
    }
    public String getUsername(){return username;}
    public String getEvent(){return event;}
    public Bitmap getBitmap(){return bitmap;}
}
