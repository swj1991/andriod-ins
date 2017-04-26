package xunhu.instagram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.twotoasters.android.support.v7.widget.RecyclerView;

import java.util.List;

import static com.twotoasters.android.support.v7.widget.RecyclerView.*;

/**
 * Created by hu on 6/10/2015.
 */
public class FollowingAdapter  extends ArrayAdapter<FollowingAndYou> {
    private int resourceId;
    public FollowingAdapter(Context context, int textViewResource, List<FollowingAndYou> objects){
        super(context,textViewResource,objects);
        resourceId=textViewResource;
    }

    public View getView(int position, View convertView, ViewGroup parent){
           System.out.println("adapter found");
           FollowingAndYou person = getItem(position);

           View view = LayoutInflater.from(getContext()).inflate(resourceId,null);

           TextView tv_event = (TextView)view.findViewById(R.id.event);
           TextView tv_username=(TextView)view.findViewById(R.id.username);
           ImageView iv_url = (ImageView) view.findViewById(R.id.following_profile);
           TextView tv_time=(TextView)view.findViewById(R.id.time_following);
          tv_username.setText(person.getUsername());
          tv_event.setText(person.getEvent());
          tv_time.setText(person.getTime());

            if (person.bt1 == null) {
                new LoadingImage(iv_url,person).execute(person.getURL());


            }else {

                iv_url.setImageBitmap(person.bt1);

            }
          ImageView iv_posts = (ImageView)view.findViewById(R.id.iv_posts);
          if (person.bt2==null){
              new LoadingImage(iv_posts,person).execute(person.getPost_url());
          }else {
              iv_posts.setImageBitmap(person.bt2);
          }

             return view;

    }

}
