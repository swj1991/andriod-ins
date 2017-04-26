package xunhu.instagram;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by hu on 11/09/2015.
 */
//create a activity that is for upload the photo
public class UploadActivity extends Activity {
    // upload
    ImageView next;
    ImageView backcross;
    TextView library;
    TextView photo;
    ImageView image;
    final File photoPath = Environment.getExternalStorageDirectory();
    final String cameraPath = photoPath.getAbsolutePath()+"/DCIM/Camera/";
    ArrayList<File> imageFiles = new ArrayList<File>();
    LinearLayout lPhotos;
    ArrayList<ImageView> ivPhotos = new ArrayList<ImageView>();
    int small_image_id=0;
    Bitmap big_image;
    String selected_path="";
    Bitmap crop_image=null;
    Bitmap grid_image =null;
    int big_width=0;
    ImageView gallery;
    int times =0 ;
    InteractiveScrollView sc;
    private static int RESULT_LOAD_IMAGE = 3;
    protected void onCreate(Bundle s){
        super.onCreate(s);
        setContentView(R.layout.camera_layout);

        next = (ImageView) findViewById(R.id.ivNext);
        gallery = (ImageView)findViewById(R.id.iv_gallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        backcross = (ImageView) findViewById(R.id.ivBackCross);
        library = (TextView) findViewById(R.id.tvLibrary);
        photo = (TextView) findViewById(R.id.tPhoto);
        ActivityCollector.addActivity(this);
        image=(ImageView) findViewById(R.id.ivPhoto);
        lPhotos = (LinearLayout) findViewById(R.id.lPhoto);
        Display display = getWindowManager().getDefaultDisplay();
        big_width = display.getWidth();
        big_width = image.getWidth();
        sc = (InteractiveScrollView)findViewById(R.id.svPhoto);
        sc.setOnBottomReachedListener(new InteractiveScrollView.OnBottomReachedListener() {
            @Override
            public void onBottomReached() {
                if(imageFiles.size()>8){
                    if (times==0){
                        Toast.makeText(getApplicationContext(),"Clicking the gallery icon at " +
                                "right bottom corner for more photos",Toast.LENGTH_SHORT).show();
                        times++;
                    }
                }
            }
        });


        backcross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                if(crop_image!=null){
                    big_image=crop_image;
                }

                Intent intent = new Intent(UploadActivity.this, FilterActivity.class);

                intent.putExtra("library",selected_path);
                startActivity(intent);
            }
        });

        getLastPhoto();

    }

    protected void getLastPhoto(){
        File f = new File(cameraPath);
        f.mkdirs();
        File[] files = f.listFiles();
        for (int i=0;i<files.length;i++){
            imageFiles.add(files[i]);
            Collections.reverse(imageFiles);
        }
        if(imageFiles.size()==0){
            image.setImageResource(R.drawable.noimage_icon);
        }else{

            big_image =decodeFile(cameraPath + imageFiles.get(0).getName().toString());
            selected_path = cameraPath + imageFiles.get(0).getName().toString();
            image.setImageBitmap(big_image);

            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            if(imageFiles.size()<=4){
                LinearLayout row = new LinearLayout(this);
                for (int i=0;i<imageFiles.size();i++){
                    ImageView smallImage = new ImageView(this);
                    smallImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            crop_image = null;
                            selected_path = cameraPath + imageFiles.get(v.getId()).getName().toString();
                            big_image = decodeFile((cameraPath + imageFiles.get(v.getId()).getName().toString()));
                            image.setImageBitmap(big_image);
                            small_image_id = v.getId();
                        }
                    });
                    smallImage.setLayoutParams(new android.view.ViewGroup.LayoutParams(width/4,width/4));
                    smallImage.setMaxHeight(200);
                    smallImage.setMaxWidth(200);
                    smallImage.setScaleType(ImageView.ScaleType.FIT_XY);
                    grid_image = decodeFile(cameraPath + imageFiles.get(i).getName().toString());

                    smallImage.setImageBitmap(grid_image);
                    smallImage.setId(i);
                    row.addView(smallImage);
                    smallImage.setId(i);

                }
                lPhotos.addView(row);


            }else if (imageFiles.size()<=8 && imageFiles.size()>4){
                for (int i=0;i<imageFiles.size();i++){
                    ImageView smallImage = new ImageView(this);
                    smallImage.setLayoutParams(new android.view.ViewGroup.LayoutParams(width/4,width/4));
                    smallImage.setMaxHeight(200);
                    smallImage.setMaxWidth(200);
                    smallImage.setScaleType(ImageView.ScaleType.FIT_XY);
                    grid_image = decodeFile(cameraPath + imageFiles.get(i).getName().toString());

                    smallImage.setImageBitmap(grid_image);
                    smallImage.setId(i);
                    ivPhotos.add(smallImage);
                    smallImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            crop_image = null;
                            selected_path = cameraPath + imageFiles.get(v.getId()).getName().toString();
                            big_image = decodeFile((cameraPath + imageFiles.get(v.getId()).getName().toString()));
                            image.setImageBitmap(big_image);
                            small_image_id = v.getId();
                        }
                    });
                }
                int rows = 2;
                for (int i=0;i<rows;i++){
                    LinearLayout row = new LinearLayout(this);
                    if(i<rows-1){
                        for (int j=4*i;j<4*i+4;j++){
                            row.addView(ivPhotos.get(j));
                        }
                    }else{

                        for (int j=4*(i);j<imageFiles.size(); j++){
                            row.addView(ivPhotos.get(j));
                        }
                    }
                    lPhotos.addView(row);
                }
            }else {
                for (int i=0;i<8;i++){
                    ImageView smallImage = new ImageView(this);
                    smallImage.setLayoutParams(new android.view.ViewGroup.LayoutParams(width / 4, width / 4));
                    smallImage.setMaxHeight(200);
                    smallImage.setMaxWidth(200);
                    smallImage.setScaleType(ImageView.ScaleType.FIT_XY);
                    grid_image = decodeFile(cameraPath + imageFiles.get(i).getName().toString());

                    smallImage.setImageBitmap(grid_image);
                    smallImage.setId(i);
                    ivPhotos.add(smallImage);
                    smallImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            crop_image = null;
                            selected_path = cameraPath + imageFiles.get(v.getId()).getName().toString();
                            big_image = decodeFile((cameraPath + imageFiles.get(v.getId()).getName().toString()));

                            image.setImageBitmap(big_image);

                            small_image_id = v.getId();
                        }
                    });
                }
                int rows = 2;
                for (int i=0;i<rows;i++){
                    LinearLayout row = new LinearLayout(this);
                    if(i<rows-1){
                        for (int j=4*i;j<4*i+4;j++){
                            row.addView(ivPhotos.get(j));
                        }
                    }else{

                        for (int j=4*(i);j<8; j++){
                            row.addView(ivPhotos.get(j));
                        }
                    }
                    lPhotos.addView(row);
                }

            }

        }
    }




    public static Bitmap decodeFile(String pathName) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        for (options.inSampleSize = 1; options.inSampleSize <= 32; options.inSampleSize++) {
            try {
                bitmap = BitmapFactory.decodeFile(pathName, options);

                    bitmap = BitmapFactory.decodeFile(pathName, options);

                Log.i("filter_enough", "Decoded successfully for sampleSize " + options.inSampleSize);
                break;
            } catch (OutOfMemoryError outOfMemoryError) {

                Log.e("filter_OOM", "outOfMemoryError while reading file for sampleSize " + options.inSampleSize
                        + " retrying with higher value");
            }
        }
        return bitmap;
    }
    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data){
        super.onActivityResult(requestcode, resultcode, data);
        if (requestcode==1){
            if (resultcode==RESULT_OK){
                Bundle extras=data.getExtras();
                crop_image = extras.getParcelable("data");
                image.setImageBitmap(crop_image);
            }
        }else if (requestcode==3){
            try {

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                selected_path = picturePath;
                image.setImageBitmap(decodeFile(picturePath));
            }catch (Exception e){
                e.printStackTrace();

            }



        }

    }


}
