package xunhu.instagram;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by hu on 13/09/2015.
 */

public class FilterActivity extends Activity {
    ImageView ivPhoto;
    ImageView back;
    ImageView next;
    ImageView bright;
    ImageView contrast;
    ImageView orgin;
    ImageView moon;
    Bitmap gray;
    Bitmap original;
    Bitmap finalone;
    ImageView final_version;
    String img;
    Bitmap selected_image=null;
    Bitmap filtered_image=null;
    SeekBar seekbarbrightness;
    SeekBar seekbarcontrast;
    Bitmap crop_image=null;
    ImageView crop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filters_layout);
        ivPhoto = (ImageView) findViewById(R.id.ivPhotoFilter);
        back = (ImageView) findViewById(R.id.ivBackCross_filter);
        bright = (ImageView) findViewById(R.id.ivBrightness);
        orgin = (ImageView) findViewById(R.id.iv_origin);
        orgin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekbarcontrast.setProgress(1);
                seekbarbrightness.setProgress(255);
                selected_image=original;
                ivPhoto.setImageBitmap(original);
                filtered_image=original;
            }
        });


        moon = (ImageView)findViewById(R.id.iv_moon);
        moon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekbarcontrast.setProgress(1);
                seekbarbrightness.setProgress(255);
                selected_image=gray;
                ivPhoto.setImageBitmap(gray);
                filtered_image=gray;
            }
        });

        final_version = (ImageView) findViewById(R.id.iv_final);
        final_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekbarcontrast.setProgress(1);
                seekbarbrightness.setProgress(255);
                selected_image=finalone;
                ivPhoto.setImageBitmap(finalone);
                filtered_image=finalone;
            }
        });


        contrast = (ImageView)findViewById(R.id.ivContrast);
        contrast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekbarbrightness.setVisibility(View.INVISIBLE);
                seekbarcontrast.setVisibility(View.VISIBLE);
            }
        });
        bright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                seekbarcontrast.setVisibility(View.INVISIBLE);
                seekbarbrightness.setVisibility(View.VISIBLE);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekbarcontrast.setProgress(1);
                seekbarbrightness.setProgress(255);
                onBackPressed();
            }
        });
        crop = (ImageView) findViewById(R.id.iv_UploadCrop);
        //crop the photo by clicking the crop
        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekbarcontrast.setProgress(1);
                seekbarbrightness.setProgress(255);
                Intent viewMediaIntent = new Intent("com.android.camera.action.CROP");
                File file = new File(img);
                viewMediaIntent.setDataAndType(Uri.fromFile(file), "image/*");
                viewMediaIntent.putExtra("crop", "true");
                viewMediaIntent.putExtra("aspectX", 0);
                viewMediaIntent.putExtra("aspectY", 0);
                viewMediaIntent.putExtra("outputX", 200);
                viewMediaIntent.putExtra("outputY", 200);
                viewMediaIntent.putExtra("return-data",true);

                startActivityForResult(viewMediaIntent, 1);


            }
        });
         Intent intent = getIntent();
         img= intent.getStringExtra("library");

        selected_image =decodeFile(img);
        filtered_image = selected_image;
        original = selected_image;

        ivPhoto.setImageBitmap(selected_image);

        orgin.setImageBitmap(original);

        gray = toGrayscale(original,0);
        moon.setImageBitmap(gray);

        finalone = toGrayscale(original,5);
        final_version.setImageBitmap(finalone);

        next= (ImageView)findViewById(R.id.ivNext_filter);
        seekbarbrightness = (SeekBar) findViewById(R.id.seekBar_Brightness);
        seekbarbrightness.setMax(510);
        seekbarbrightness.setProgress((255));
        seekbarcontrast = (SeekBar)findViewById(R.id.seekBar_Contrast);
        seekbarcontrast.setMax(10);
        seekbarcontrast.setProgress((1));
        //adjust contrast
        seekbarcontrast.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                filtered_image = changeBitmapContrastBrightness(selected_image, (float)(progress-1),(float)(seekbarbrightness.getProgress()-255));
                ivPhoto.setImageBitmap(filtered_image);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
     //adjust the brightness
        seekbarbrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                filtered_image = changeBitmapContrastBrightness(selected_image, (float)(seekbarcontrast.getProgress()-1), (float)(progress-255));
                ivPhoto.setImageBitmap(filtered_image);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        //move to the next activity by clicking it
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                filtered_image.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                byte[] byteArray = stream.toByteArray();
                Intent intent = new Intent(FilterActivity.this,ShareImageActivity.class);
                intent.putExtra("image",byteArray);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data){
        super.onActivityResult(requestcode, resultcode, data);
        if (requestcode==1){
            if (resultcode==RESULT_OK){
                Bundle extras=data.getExtras();
                crop_image = extras.getParcelable("data");
                filtered_image = crop_image;
                original=crop_image;
                selected_image=filtered_image;
                ivPhoto.setImageBitmap(filtered_image);
                orgin.setImageBitmap(filtered_image);

                gray = toGrayscale(original,0);
                moon.setImageBitmap(gray);

                finalone = toGrayscale(original,5);
                final_version.setImageBitmap(finalone);
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public static Bitmap decodeFile(String pathName) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        for (options.inSampleSize = 1; options.inSampleSize <= 32; options.inSampleSize++) {
            try {
                bitmap = BitmapFactory.decodeFile(pathName, options);
                System.out.println(bitmap);
                if (bitmap.getByteCount()/1024/1024>10){
                    options.inSampleSize=bitmap.getByteCount()/1024/1024/10;
                    bitmap = BitmapFactory.decodeFile(pathName, options);
                }
                Log.i("filter_enough", "Decoded successfully for sampleSize " + options.inSampleSize);
                break;
            } catch (OutOfMemoryError outOfMemoryError) {
               // If an OutOfMemoryError occurred, we continue with for loop and next inSampleSize value
                Log.e("filter_OOM", "outOfMemoryError while reading file for sampleSize " + options.inSampleSize
                        + " retrying with higher value");
            }
        }
        return bitmap;
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if (selected_image!=null){
            selected_image.recycle();
            selected_image=null;
        }
        if (crop_image!=null){
            crop_image.recycle();
            crop_image=null;
        }
        if (filtered_image!=null){
            filtered_image.recycle();
            filtered_image=null;
        }
        if (original!=null){
            original.recycle();
            original=null;
        }if (gray!=null){
            gray.recycle();
            gray=null;
        }if(finalone!=null){
            finalone.recycle();
            finalone=null;
        }

    }
    //adjust the color of bitmap
    public static Bitmap changeBitmapContrastBrightness(Bitmap bmp, float contrast, float brightness)
    {
        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, brightness,
                        0, contrast, 0, 0, brightness,
                        0, 0, contrast, 0, brightness,
                        0, 0, 0, 1, 0
                });

        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        Canvas canvas = new Canvas(ret);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);

        return ret;
    }
    //create a new bit map if the color is changed
    public Bitmap toGrayscale(Bitmap bmpOriginal, int Saturation)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(Saturation);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }
    //reset the seek bar
    protected void onStop(){
        super.onStop();
        seekbarbrightness.setProgress(255);
        seekbarcontrast.setProgress(1);
    }



}
