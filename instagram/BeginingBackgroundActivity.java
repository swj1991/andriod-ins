package xunhu.instagram;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by hu on 9/09/2015.
 */
public class BeginingBackgroundActivity extends Activity {
               @Override
               public void onCreate(Bundle savedInstanceState){
                   super.onCreate(savedInstanceState);
                   setContentView(R.layout.starting);


                   // Enable Local Datastore.
                   Parse.enableLocalDatastore(this);

                   Parse.initialize(this, "rD25wtCtrewKQLMECOdA45qlJnD2atztkkUJT78Z", "eCoOryLCtmfSDJfNSDq605Dx0qQQ6ck9aIm590P0");


                   ParseObject testObject = new ParseObject("TestObject");
                   testObject.put("foo", "bar");
                   testObject.saveInBackground();

                   Thread t = new Thread(new Runnable() {
                       @Override
                       public void run() {
                           //create a intent
                           Intent intent;
                           //define intent to transit to activity
                           intent = new Intent(BeginingBackgroundActivity.this,MainActivity.class);
                           try {
                               //Sleep 4 seconds to go to the next activity
                               Thread.sleep(4000);
                           } catch (InterruptedException e) {
                               e.printStackTrace();
                           }
                           // start the intent
                           startActivity(intent);
                       }
                   });
                      t.start();

               }

                 @Override
              public void onStop(){
                  super.onStop();
                  finish();
              }
}
