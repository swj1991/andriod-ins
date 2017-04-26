package xunhu.instagram;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hu on 11/09/2015.
 */
//Activity is collect activity, that could make user exit at any time
public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<Activity>();
    //add activity to the Arraylist
    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    //remove the activity if press back button
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }
    //remove all of the activity
    public static void finishAll(){
        for (Activity activity : activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
