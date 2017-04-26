package instapi.ui;

import android.bluetooth.BluetoothAdapter;

/**
 * Created by yes on 10/10/15.
 */
public class Connections {
    private static boolean state = false;
    public static boolean blueTooth() {
        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
        if (!bluetooth.isEnabled()) {
            System.out.println("Bluetooth is Disable...");
            state = true;
        } else if (bluetooth.isEnabled()) {
            String address = bluetooth.getAddress();
            String name = bluetooth.getName();
            System.out.println(name + " : " + address);
            state = false;
        }
        return state;
    }
}
