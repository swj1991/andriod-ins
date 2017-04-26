package xunhu.instagram;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import instapi.ui.Connections;
import xunhu.instagram.bluetooth_utility.ConnectThread;

/**
 * Created by hu on 16/09/2015.
 */
//create a activity to upload photo or swipe the photo
public class ShareImageActivity extends Activity {
    ImageView logo;
    Button share;
    GPSTrackerActivity gps;
    String commentText = "";
    Bitmap bmp;
    String share_path="";
    Button swipe;
    ListView lv;
    boolean visible = true;


    private BluetoothAdapter adapter;
    List<BluetoothDevice> mArrayAdapter = new ArrayList<BluetoothDevice>();



    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private static final int REQUEST_ENABLE_BT = 12;

    private void setBluetoothData() {
        // Getting the Bluetooth adapter
        adapter = BluetoothAdapter.getDefaultAdapter();

        System.out.println("-------------");
        System.out.println("\nAdapter: " + adapter.toString() + "\n\nName: "
                + adapter.getName() + "\nAddress: " + adapter.getAddress());
        // Check for Bluetooth support in the first place
        // Emulator doesn't support Bluetooth and will return null
        if (adapter == null) {
            Toast.makeText(this, "Bluetooth NOT supported. Aborting.",
                    Toast.LENGTH_LONG).show();
        }
        // Starting the device discovery
        System.out.println("\n\nStarting discovery...");
        adapter.startDiscovery();
        System.out.println("\nDone with discovery...\n");
        // Listing paired devices
        System.out.println("\nDevices Pared:");
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        for (BluetoothDevice device : devices) {
            System.out.println("\nFound device: " + device.getName() + " Add: "
                    + device.getAddress());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
//        out.setText("");
        setBluetoothData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_layout);


        share = (Button) findViewById(R.id.btnShare);
        swipe = (Button)findViewById(R.id.swipe);

        swipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UUID d = UUID.randomUUID();

                TmpVars.tag = d.toString();

                // Locate the image in res > drawable-hdpi
                Bitmap bitmap = BitmapFactory.decodeFile(share_path);
                // Convert it to byte
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Compress image to lower quality scale 1 - 100
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] image = stream.toByteArray();

                // Create the ParseFile
                ParseFile file = new ParseFile("SharedImage", image);
                // Upload the image into Parse Cloud
                file.saveInBackground();

                // Create a New Class called "ImageUpload" in Parse
                ParseObject imgupload = new ParseObject("BluetoothPics");

                // Create a column named "ImageName" and set the string
//                imgupload.put("ImageName", "AndroidBegin Logo");

                // Create a column named "ImageFile" and insert the image
                imgupload.put("sharedImage", file);
                imgupload.put("tag", TmpVars.tag);
                imgupload.put("userName", ProfileActivity.current_username);

                        // Create the class and the columns
                        imgupload.saveInBackground();

                // Show a simple toast message
                Toast.makeText(ShareImageActivity.this, "Image Uploaded",
                        Toast.LENGTH_SHORT).show();

                setBluetoothData();
                System.out.println("iiiiiiiiiiiiiii");




                mArrayAdapter.clear();

                logo.setVisibility(View.INVISIBLE);


                lv = (ListView)findViewById(R.id.bluelist);
                DeviceAdapter a = new DeviceAdapter(ShareImageActivity.this,R.layout.device_list,mArrayAdapter);
                lv.setAdapter(a);

                System.out.println(lv);

                if (Connections.blueTooth()) {
                    Intent enableBtIntent = new Intent(
                            BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }

                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);

                registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy


                Intent discoverableIntent = new
                        Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverableIntent);

                adapter = BluetoothAdapter.getDefaultAdapter();
                adapter.startDiscovery();




            }
        });

        final String type = "image/*";
        String filename = "4-seasons.jpg";
        String mediaPath = Environment.getExternalStorageDirectory() + filename;

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                //new FileUpload().execute(share_path);
//
//                new Thread(new Runnable() {
//                    public void run() {
//
//                        try {
////                            new FileUpload().doInBackground(share_path);
//
//
//                            new FileUpload().execute(share_path);
//                            //System.out.println(FileUpload.base64Image(share_path));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
////                        ImageUploadTask task = new ImageUploadTask();
////                        task.bitmap = bmp;
////                        task.doInBackground();
//
//
//
//
//                    }}).start();



                try {
                    createInstagramIntent(type);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //new AcceptThread().start();

//                new Thread(new Runnable() {
//                    public void run() {
//
//                        try {
//                            new FileUpload().doInBackground(share_path);
//
//                            //System.out.println(FileUpload.base64Image(share_path));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
////                        ImageUploadTask task = new ImageUploadTask();
////                        task.bitmap = bmp;
////                        task.doInBackground();
//
//
//
//
//                    }}).start();
//
//
//               //new AcceptThread().start();
//
//                System.out.println("-----------------------");

            }
        });




        logo =(ImageView)findViewById(R.id.ivShareImage);
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);





        logo.setImageBitmap(bmp);
        OutputStream output;
        File filepath = Environment.getExternalStorageDirectory();
        File dir = new File(filepath.getAbsolutePath()+"/DCIM/Camera/");
        dir.mkdir();
        File file = new File(dir,"xunhu_shared_image.jpg");
        share_path = filepath.getAbsolutePath()+"/DCIM/Camera/xunhu_shared_image.jpg";
        try {
            output = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG,100,output);
            output.flush();
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
//            List mArrayAdapter = new ArrayList<String>();


            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                mArrayAdapter.add(device);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DeviceAdapter a = new DeviceAdapter(ShareImageActivity.this,R.layout.device_list,mArrayAdapter);
                        lv.setAdapter(a);

                        lv.invalidateViews();
                        System.out.println("9999999999"); }
                });

                //new ConnectThread(device).start();

            }

            System.out.println(mArrayAdapter.size());
        }
    };



    private void createInstagramIntent(String type) throws IOException {

//        File file = new File(getBaseContext().getCacheDir(), "tmp.JPEG");
//
//        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
//        bmp.compress(Bitmap.CompressFormat.JPEG, 100, os);
//        os.close();
//
//
//

        //create a file to write bitmap data
        File f = new File(getBaseContext().getCacheDir(), "tmp.PNG");
        f.createNewFile();

//Convert bitmap to byte array
        Bitmap bitmap = bmp;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, bos);
        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();




//        System.out.println(file);
        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);

        // Create the URI from the media
        File media = new File(share_path);

        Uri uri = Uri.fromFile(media);

        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);

        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));
    }
    private class DeviceAdapter extends ArrayAdapter<BluetoothDevice> {
        private int resourceId;
        public DeviceAdapter(Context context, int resource, List<BluetoothDevice> object) {
            super(context, resource,object);
            resourceId = resource;
        }
        public View getView(int position, View convertView, ViewGroup parent){
            final BluetoothDevice device = getItem(position);
            View view = LayoutInflater.from(getContext()).inflate(resourceId, null);


            Button connect_button = (Button)view.findViewById(R.id.btnConnet);
            TextView tv_device_name = (TextView)view.findViewById(R.id.tv_devicename);
            TextView tv_mac = (TextView)view.findViewById(R.id.mac);
            tv_mac.setText(device.getName());
            tv_device_name.setText(device.getAddress());
            System.out.println(device.getName());
            connect_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    new ConnectThread(device).start();

                    System.out.println("ABC");

                }
            });

            return view;
        }
    }





}
