package mdstudios.deltahacks;

import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by mickeydang on 2018-01-27.
 */

public class CommunicationClient {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private final String TEXT_MESSAGE = "new sign in detected from: ";

    public CommunicationClient() {

    }

    public void sendToBluetoothShield(BluetoothDevice btd, String name, int requestCode, boolean isSignIn) {

        try {
            btd.createRfcommSocketToServiceRecord(new UUID(0, 100));

        } catch (IOException e) {

        }

    }

    public void textToServerPhone(PendingIntent pi, String name) {
        Log.d(LOG_TAG, "sending text!");
        SmsManager manager = SmsManager.getDefault();
        manager.sendTextMessage(Utils.SERVER_NUMBER, null, TEXT_MESSAGE + name, pi, null);

    }


}
