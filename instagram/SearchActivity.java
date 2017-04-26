package xunhu.instagram;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.List;

import instapi.instagram.InstagramUserFollowsLoader;
import instapi.instagram.InstagramUserSearchLoader;
import instapi.instagram.api.model.User;
import instapi.instagram.api.model.Users;

/**
 * Created by hu on 10/09/2015.
 */
//create activity that is to suggest users and search user
public class SearchActivity extends FragmentActivity {
       private List<FollowingSuggestion> suggestion_following= new ArrayList<FollowingSuggestion>();
       ListView lv;
       private List<FollowingSuggestion> search_users = new ArrayList<FollowingSuggestion>();

    public List<User> sugFrds = new ArrayList<User>();
    public  int sugNum = 20;

    private InstagramUserFollowsLoader instagramUserFollowsLoader;
    private InstagramUserFollowsLoader instagramSelfFollowsLoader;
    private InstagramUserSearchLoader instagramUserSearchLoader;
    SuggestionAdapter adapter;
    String queryText="xunsavior";



    private class FollowingTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {


        }

        @Override
        protected String doInBackground(String... data) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://api.instagram.com/v1/users/"+data[0]+"/relationship?access_token="+TmpVars.token);

            try {
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
                        SearchActivity.this.displayToast(errorInfo);
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


    protected void onCreate(Bundle s){
        super.onCreate(s);
        setContentView(R.layout.search_layout);
        final EditText et_search = (EditText)findViewById(R.id.et_search);
        Button btn_search = (Button)findViewById(R.id.btnSearch);
        final ProgressDialog loading = new ProgressDialog(SearchActivity.this);
        loading.setTitle("Welcome to searching for user");
        loading.setMessage("Loading data...");
        loading.show();
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                queryText = et_search.getText().toString();
                instagramUserSearchLoader = new InstagramUserSearchLoader(SearchActivity.this);
                loading.setTitle("Searching...");
                loading.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            search_users.clear();
                            instagramUserSearchLoader.q = queryText;
                            Users urs = instagramUserSearchLoader.loadResourceInBackground();
                            for (int i = 0; i < urs.getData().size(); i++) {
                                String username = urs.getData().get(i).getUsername();
                                String url = urs.getData().get(i).getProfilePicture();
                                FollowingSuggestion person = new FollowingSuggestion(url, username);
                                person.userId = urs.getData().get(i).getId();
                                search_users.add(person);

                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SuggestionAdapter adapter = new SuggestionAdapter(SearchActivity.this, R.layout.discovery_layout, search_users);
                                    lv.setAdapter(adapter);
                                    loading.cancel();
                                    lv.invalidateViews();


                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        });




        lv = (ListView)findViewById(R.id.lv_suggestion);
        ActivityCollector.addActivity(this);



        instagramSelfFollowsLoader = new InstagramUserFollowsLoader(this);
        instagramSelfFollowsLoader.userId = "self";
        instagramSelfFollowsLoader.count = 62;
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Users selfFollows = instagramSelfFollowsLoader.loadResourceInBackground();
                    List<FollowingSuggestion> following_suggestion = new ArrayList<FollowingSuggestion>();

                    List<Users> usersFrdsList = new ArrayList<>();



                    System.out.println("================");
                    System.out.println(selfFollows.getData().size());
                    for (User ur: selfFollows.getData()){
                        instagramUserFollowsLoader = new InstagramUserFollowsLoader(SearchActivity.this);
                        instagramUserFollowsLoader.userId = ur.getId();

                        System.out.println("----------------");

                        System.out.println(ur.getId());

                        Users frdFollows = instagramUserFollowsLoader.loadResourceInBackground();

                        usersFrdsList.add(frdFollows);
                    }

                    for (Users frdsL:usersFrdsList){
                        for(User frdU:frdsL.getData()){
                            for(User myFrd:selfFollows.getData()){

                                if(myFrd.getId().equals(frdU.getId())) {
                                    break;
                                }

                                if(!myFrd.getId().equals(frdU.getId())) {
                                    if(selfFollows.getData().get(selfFollows.getData().size()-1) == myFrd){

                                        if (sugFrds.size() == 0){

                                            sugFrds.add(frdU);
                                        }else {

                                            for (User sg : sugFrds) {

                                                if (sg.getId().equals(frdU.getId())) {

                                                    break;
                                                }
                                                if (sugFrds.lastIndexOf(sg) == sugFrds.size() - 1) {

                                                    sugFrds.add(frdU);
                                                    System.out.println(frdU);
                                                    if (sugFrds.size() > sugNum) {
                                                        System.out.println(70000099);
                                                        System.out.println(sugFrds);
                                                        for (int i=0;i<sugFrds.size();i++){
                                                            String profile_url = sugFrds.get(i).getProfilePicture();
                                                            String username = sugFrds.get(i).getUsername();
                                                            FollowingSuggestion fs = new FollowingSuggestion(profile_url,username);
                                                            fs.userId = sugFrds.get(i).getId();
                                                            suggestion_following.add(fs);
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    loading.cancel();
                                                                    adapter = new SuggestionAdapter(SearchActivity.this,R.layout.discovery_layout,suggestion_following);
                                                                    lv.setAdapter(adapter);
                                                                    lv.invalidateViews();

                                                                }
                                                            });
                                                        }
                                                        return;

                                                    }
                                                }

                                            }
                                        }


                                    }
                                }

                            }


                        }


                    }

            } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();





    }



    public class SuggestionAdapter extends ArrayAdapter<FollowingSuggestion> {
            private int resourceId;

        public SuggestionAdapter(Context context, int resource,List<FollowingSuggestion> objects) {
            super(context, resource,objects);
            resourceId = resource;
        }

        public View getView(int position, View convertView, ViewGroup parent){
            final FollowingSuggestion person = getItem(position);
            View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            ImageView profile_image = (ImageView)view.findViewById(R.id.discover_profile_photo);
            Button btn_follow = (Button)view.findViewById(R.id.btn_follow);
            btn_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            new FollowingTask().doInBackground(person.userId);
                        }
                    }).start();


                }
            });
            if (person.bitmap==null){
                new LoadingImage(profile_image,person).execute(person.getProfile_image_url());
            }else {
                profile_image.setImageBitmap(person.bitmap);
            }
            TextView tv_username = (TextView)view.findViewById(R.id.discover_profile_username);
            tv_username.setText(person.getProfile_username());
            return view;
        }
    }
    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.finishAll();
    }


    protected void displayToast(String errorInfo){

        Toast.makeText(SearchActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
    }

}
