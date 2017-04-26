package xunhu.instagram;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import xunhu.instagram.bluetooth_utility.AcceptThread;
import xunhu.instagram.bluetooth_utility.ConnectThread;

/**
 * Created by hu on 12/10/2015.
 */
public class BluetoothReceivor extends Activity {

   // BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private static final int REQUEST_ENABLE_BT = 12;
    ListView lv;
    List<BluetoothFound> found = new ArrayList<BluetoothFound>();
    ProgressDialog loading;


    private BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

         public void onCreate(Bundle s){
             super.onCreate(s);
             setContentView(R.layout.bluetooth_receiver_layout);
             lv = (ListView)findViewById(R.id.lv_bluetooth);
             loading = new ProgressDialog(BluetoothReceivor.this);
             loading.setTitle("Welcome to searching for user");
             loading.setMessage("Loading data...");
             loading.show();

             TmpVars.br = this;

             Intent discoverableIntent = new
                     Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
             discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
             startActivity(discoverableIntent);

             new AcceptThread(adapter).start();

         }


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

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            List mArrayAdapter = new ArrayList<String>();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());

                new ConnectThread(device).start();

            }

            System.out.println(mArrayAdapter);
        }
    };




    public void test(String tag){

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                "BluetoothPics");

        // Locate the objectId from the class

        query.whereEqualTo("tag", tag);


        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {

                if (e == null) {
                    ParseFile fileObject = (ParseFile) objects.get(0).get("sharedImage");

                    final String userName = (String)objects.get(0).get("userName");
                    System.out.println(userName);
                    System.out.println(fileObject.getUrl());


                    fileObject.getDataInBackground(new GetDataCallback() {


                        @Override
                        public void done(byte[] data, com.parse.ParseException e) {
                            if (e == null) {
                                Log.d("test",
                                        "We've got data in data.");

                                loading.cancel();
                                Bitmap bmp = BitmapFactory
                                        .decodeByteArray(
                                                data, 0,
                                                data.length);
                                BluetoothFound a = new BluetoothFound(userName,"send you a photo",bmp);
                                found.add(a);
                                DeviceFoundAdapter adapter = new DeviceFoundAdapter(BluetoothReceivor.this,R.layout.bluetooth_image_view,found);
                                lv.setAdapter(adapter);
                                lv.invalidateViews();

                            } else {
                                Log.d("test",
                                        "There was a problem downloading the data.");
                            }
                        }

                    });


                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    private class DeviceFoundAdapter extends ArrayAdapter<BluetoothFound> {
        private int resourceId;
        public DeviceFoundAdapter(Context context, int resource, List<BluetoothFound> object) {
            super(context, resource,object);
            resourceId = resource;
        }
        public View getView(int position, View convertView, ViewGroup parent){
            final BluetoothFound device = getItem(position);
            View view = LayoutInflater.from(getContext()).inflate(resourceId, null);


            TextView tv_username = (TextView)view.findViewById(R.id.tv_username);
            tv_username.setText(device.getUsername());



            ImageView iv_upload_photo = (ImageView)view.findViewById(R.id.tv_sent_image);

            System.out.println("777777777777");
            System.out.println((TextView)view.findViewById(R.id.tv_username));
            System.out.println(device.getUsername());
            System.out.println(iv_upload_photo);

            iv_upload_photo.setImageBitmap(device.getBitmap());

            return view;
        }
    }
}
