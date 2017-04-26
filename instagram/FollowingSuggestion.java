package xunhu.instagram;

import android.graphics.Bitmap;

/**
 * Created by hu on 7/10/2015.
 */
//create a class for suggestion
public class FollowingSuggestion {
    String userId;
    String profile_image_url;
    String profile_username;
    Bitmap bitmap;
    public FollowingSuggestion(String profile_image_url,String profile_username){
        this.profile_image_url=profile_image_url;
        this.profile_username=profile_username;
    }
    public String getProfile_image_url(){
        return profile_image_url;
    }
    public String getProfile_username(){
        return profile_username;
    }
}
