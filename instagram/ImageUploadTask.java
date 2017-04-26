package xunhu.instagram;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;

/**
 * Created by yes on 11/10/15.
 */
class ImageUploadTask extends AsyncTask<Void, Void, String> {

    private ProgressDialog dialog;
    public Bitmap bitmap;


    @Override
    protected String doInBackground(Void... unsued) {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();

            String urlString = "http://10.9.195.253:8000/image_upload";
            HttpPost httpPost = new HttpPost(urlString);


            httpPost.addHeader("access_token", "1091136837.1e4eebf.3450bb40fa4644a59f472a06ccadde98");

            MultipartEntity entity = new MultipartEntity(
                   HttpMultipartMode.BROWSER_COMPATIBLE);
//
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//            byte[] data = bos.toByteArray();
            entity.addPart("photoId", new StringBody("json"));
//            entity.addPart("returnformat", new StringBody("json"));
//            entity.addPart("uploaded", new ByteArrayBody(data,
//                    "myImage.jpg"));
////            entity.addPart("photoCaption", new StringBody(caption.getText()
////                    .toString()));
            httpPost.setEntity(entity);
            //HttpResponse response = httpClient.execute(httpPost, localContext);

            HttpResponse response = httpClient.execute(httpPost);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            response.getEntity().getContent(), "UTF-8"));

            String sResponse = reader.readLine();
            return sResponse;
        } catch (Exception e) {

            e.printStackTrace();
//            if (dialog.isShowing())
//                dialog.dismiss();
////            Toast.makeText(getApplicationContext(),
////                    getString(R.string.exception_message),
////                    Toast.LENGTH_LONG).show();
//            Log.e(e.getClass().getName(), e.getMessage(), e);
            return null;
        }

        // (null);
    }

    @Override
    protected void onProgressUpdate(Void... unsued) {

    }

    @Override
    protected void onPostExecute(String sResponse) {
//        try {
//            if (dialog.isShowing())
//                dialog.dismiss();
//
//            if (sResponse != null) {
//                JSONObject JResponse = new JSONObject(sResponse);
//                int success = JResponse.getInt("SUCCESS");
//                String message = JResponse.getString("MESSAGE");
//                if (success == 0) {
//                    Toast.makeText(getApplicationContext(), message,
//                            Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getApplicationContext(),
//                            "Photo uploaded successfully",
//                            Toast.LENGTH_SHORT).show();
//                    caption.setText("");
//                }
//            }
//        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(),
//                    getString(R.string.exception_message),
//                    Toast.LENGTH_LONG).show();
//            Log.e(e.getClass().getName(), e.getMessage(), e);
//        }
    }
}

