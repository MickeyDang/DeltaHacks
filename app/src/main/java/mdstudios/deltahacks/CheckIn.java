package mdstudios.deltahacks;

import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

public class CheckIn extends AppCompatActivity {

    private static final String TAG = "bluetooth";
    private final String LOG_TAG = this.getClass().getSimpleName();

    Button mCheckIn;
    private String username;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;

    // SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // MAC-address of Bluetooth module (you must edit this line)
    private static String address = "60:64:05:D1:4E:C4";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        SharedPreferences sharedPreferences = this.getSharedPreferences("package mdstudios.deltahacks", Context.MODE_PRIVATE);
        username = sharedPreferences.getString(Utils.NAME_KEY, null);


        mCheckIn = findViewById(R.id.checkIn);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();

        final CommunicationClient cc = new CommunicationClient();


        mCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                sendData(username);
//                sendData("true");
                cc.textToServerPhone(makeBundle());
            }
        });

    }

    private Bundle makeBundle() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Bundle bundle = new Bundle();
        bundle.putString(Utils.NAME_KEY, prefs.getString("name", "Mickey"));
        bundle.putString(Utils.STATUS_KEY, "true");
        bundle.putString(Utils.LOC_KEY, "E5 Room 6008");
        return bundle;
    }

    /*
    public void connectToTanjot() {

        //SEND TANJOT ARDUINO SOMETHING VIA BLUETOOTH;
        String username = sharedPreferences.getString("name", null);
        String password = sharedPreferences.getString("password", null);

        Toast.makeText(this, username + ": " + password, Toast.LENGTH_SHORT).show();
        sendToTanjot();
    }

    public void sendToTanjot() {


    }

    */

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if(Build.VERSION.SDK_INT >= 21){
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e(TAG, "Could not create Insecure RFComm Connection",e);
            }
        }
        return  device.createRfcommSocketToServiceRecord(MY_UUID);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        Log.d(TAG, "...onResume - try connect...");
//
//        // Set up a pointer to the remote node using it's address.
//        BluetoothDevice device = btAdapter.getRemoteDevice(address);
//
//        // Two things are needed to make a connection:
//        //   A MAC address, which we got above.
//        //   A Service ID or UUID.  In this case we are using the
//        //     UUID for SPP.
//
//        try {
//            btSocket = createBluetoothSocket(device);
//        } catch (IOException e1) {
//            errorExit("Fatal Error", "In onResume() and socket create failed: " + e1.getMessage() + ".");
//        }
//
//        // Discovery is resource intensive.  Make sure it isn't going on
//        // when you attempt to connect and pass your message.
//        btAdapter.cancelDiscovery();
//
//        // Establish the connection.  This will block until it connects.
//        Log.d(TAG, "...Connecting...");
//        try {
//            btSocket.connect();
//            Log.d(TAG, "...Connection ok...");
//        } catch (IOException e) {
//            try {
//                btSocket.close();
//            } catch (IOException e2) {
//                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
//            }
//        }
//
//        // Create a data stream so we can talk to server.
//        Log.d(TAG, "...Create Socket...");
//
//        try {
//            outStream = btSocket.getOutputStream();
//        } catch (IOException e) {
//            errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//
//        Log.d(TAG, "...In onPause()...");
//
//        if (outStream != null) {
//            try {
//                outStream.flush();
//            } catch (IOException e) {
//                errorExit("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
//            }
//        }
//
//        try     {
//            btSocket.close();
//        } catch (IOException e2) {
//            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
//        }
//    }

    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if(btAdapter==null) {
            errorExit("Fatal Error", "Bluetooth not support");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth ON...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    private void errorExit(String title, String message){
        Log.d(LOG_TAG, title + " - " + message);
        finish();
    }

    private void sendData(String message) {
        byte[] msgBuffer = message.getBytes();

        Log.d(TAG, "...Send data: " + message + "...");

        try {
            outStream.write(msgBuffer);
        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 35 in the java code";
            msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            errorExit("Fatal Error", msg);
        }
    }
}

