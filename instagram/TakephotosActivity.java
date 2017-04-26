package xunhu.instagram;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by hu on 29/09/2015.
 */
//create a activity that is used to take the photo
public class TakephotosActivity extends Activity implements SurfaceHolder.Callback, Camera.ShutterCallback, Camera.PictureCallback {
    private ImageView back;
    private ImageView takephoto;
    private Uri imageUri;
    private ImageView big_image;
    public static final int TAKE_PHOTO = 1;
    private File outputImage;
    private Bitmap take_photo;


    Camera mCamera;
    SurfaceView mPreview;
    Boolean grid_or_nor = true;
    ImageView iv;
    Button btn_grid;
    ImageView flashlight;
    ImageView gridview;
    Boolean isFlashOn = true;
    Camera.Parameters params;

    String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/Camera/output.jpg";
    TextView next;
    protected void onCreate(Bundle s){
        super.onCreate(s);
        setContentView(R.layout.photo_taken_layout);

        mPreview = (SurfaceView)findViewById(R.id.preview);
        mPreview.getHolder().addCallback(this);
        mPreview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mCamera = Camera.open(0);
            params= mCamera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
        }

        btn_grid = (Button)findViewById(R.id.btn_gridview);
        gridview = (ImageView)findViewById(R.id.ivCameraPhoto);
        back = (ImageView) findViewById(R.id.ivBackCrossCamera);
        takephoto = (ImageView)findViewById(R.id.iv_photo_capture);
        big_image = (ImageView)findViewById(R.id.ivCameraPhoto);
        next = (TextView) findViewById(R.id.tv_next_capture);
        flashlight = (ImageView)findViewById(R.id.flash_on_off);
        flashlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFlashOn==true){
                    flashlight.setImageResource(R.drawable.flash_off);
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    isFlashOn=false;
                }else {
                    flashlight.setImageResource(R.drawable.flash_on);
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                    isFlashOn=true;
                }
            }
        });



        next.setVisibility(View.INVISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TakephotosActivity.this,FilterActivity.class);
                intent.putExtra("library",path);
                startActivity(intent);
            }
        });



    }


    public void onCaptureClick(View v){

        mCamera.setParameters(params);
        mCamera.takePicture(this, null, null, this);
        next.setVisibility(View.VISIBLE);
    }

    public void onGridViewClick(View v){
        if (grid_or_nor==true){
            gridview.setVisibility(View.INVISIBLE);
            btn_grid.setText("start gridview");
            grid_or_nor=false;
        }else {
            gridview.setVisibility(View.VISIBLE);
            btn_grid.setText("Stop Grid View");
            grid_or_nor=true;
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        mCamera.stopPreview();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mCamera.release();
    }

    public void onBackPressed(){
        super.onBackPressed();
        if (take_photo!=null){
            take_photo.recycle();
            take_photo=null;
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            mCamera.setPreviewDisplay(mPreview.getHolder());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        List<Camera.Size> sizes = params.getSupportedPreviewSizes();
        Camera.Size selected = sizes.get(0);
        params.setPreviewSize(selected.width, selected.height);
        mCamera.setParameters(params);
        mCamera.setDisplayOrientation(90);

        mCamera.startPreview();

    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        try {
            mCamera.stopPreview();
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//            Matrix matrix = new Matrix();
//            matrix.postRotate(90);
            bitmap=RotateBitmap(bitmap,90);
            File file = new File(Environment.getExternalStorageDirectory()+"/DCIM/Camera/");
            file = new File(path);
            FileOutputStream output = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }


    @Override
    public void onShutter() {

    }
}
