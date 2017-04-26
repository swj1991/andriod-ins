package xunhu.instagram;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by hu on 6/10/2015.
 */
public class LoadingImage extends AsyncTask<String,Void,Bitmap> {
    ImageView image;
    FollowingAndYou pr;
    Comment com;
    FollowingSuggestion followingSuggestion;

    public LoadingImage(ImageView bmImage,FollowingAndYou pr){
        this.image = bmImage;
        this.pr = pr;
    }


    public LoadingImage(ImageView bmImage){
        this.image = bmImage;

    }
    public LoadingImage(ImageView bitImage,Comment com){
        this.image=bitImage;
        this.com=com;
    }

    public LoadingImage(ImageView bitImage, FollowingSuggestion followingSuggestion){
            this.image=bitImage;
            this.followingSuggestion=followingSuggestion;
    }
    protected void onPreExecute(){

    }
    @Override
    protected Bitmap doInBackground(String... link) {
        try {
            URL url = new URL(link[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    protected void onPostExecute(Bitmap result){
        image.setImageBitmap(result);
        if(this.pr != null){
            if (image.getId()==R.id.following_profile){
                this.pr.bt1=result;
            }else {
                this.pr.bt2=result;
            }

        }
        if (this.com!=null){
            if (image.getId()==R.id.iv_profile_feed){
                this.com.profile_photo = result;

            }
            else{
                this.com.posts = result;

            }

        }
        if(this.followingSuggestion!=null){
            followingSuggestion.bitmap = result;
        }



    }
}
