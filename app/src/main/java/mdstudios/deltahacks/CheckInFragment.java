package mdstudios.deltahacks;
import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import mdstudios.deltahacks.BluetoothConnectionService;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;


public class CheckInFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    BluetoothAdapter mBluetoothAdapter;

    BluetoothConnectionService mBluetoothConnection;

    Button mScanButton;
    ProgressBar mProgressBar;

    private final UUID MY_UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final String MAC_ADDRESS = "98:D3:31:FD:67:87";


    BluetoothDevice mBTDevice;

    private OnFragmentInteractionListener mListener;

    public CheckInFragment() {
        // Required empty public constructor
    }

    /**
     * Broadcast Receiver for listing devices that are not yet paired
     * -Executed by btnDiscover() method.
     */
    //runs without button click
    //finds tanjot's arduino and assigns mBTDevice to it. (Y)
    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
                Log.d("TANJOT", device.getAddress());
                if (device.getAddress().equals(MAC_ADDRESS)) {
                    mBTDevice = device;
                    enableScan();
                }

                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());

            }
        }
    };

    /**
     * Broadcast Receiver that detects bond state changes (Pairing status changes)
     */
    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "receving msg");
            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases:
                //case1: bonded already
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDED.");
                    //inside BroadcastReceiver4
                    mBTDevice = mDevice;
                    if (mBTDevice.getAddress().equals(MAC_ADDRESS)) {
                        mBTDevice.createBond();
                        Toast.makeText(getContext(), "MAKING BONDS!", Toast.LENGTH_SHORT).show();
                    }


                }
                //case2: creating a bond
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDING.");
                }
                //case3: breaking a bond
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.d(TAG, "BroadcastReceiver: BOND_NONE.");
                }
            }
        }
    };


    public static CheckInFragment newInstance() {
        CheckInFragment fragment = new CheckInFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mScanButton = view.findViewById(R.id.scanButton);
        final CommunicationClient cc = new CommunicationClient();
        mProgressBar = view.findViewById(R.id.loadingIcon);

        disableScan();

        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "button pressed");
                cc.textToServerPhone(makeBundle());

                Log.d(TAG, "setting up");


////
//                while (mBTDevice == null) {
////                 Log.d(TAG, "waiting...");
//                }
//                Log.d(TAG, "done setting up");
                startConnection();
            }
        });

    }

    private Bundle makeBundle() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Bundle bundle = new Bundle();
        bundle.putString(Utils.NAME_KEY, prefs.getString("name", "Mickey"));
        bundle.putString(Utils.STATUS_KEY, "true");
        bundle.putString(Utils.LOC_KEY, "E5 Room 6008");
        return bundle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkBTPermissions();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
//            Log.d(TAG, "btnDiscover: Canceling discovery.");
            //check BT permissions in manifest
//            checkBTPermissions();
            mBluetoothAdapter.startDiscovery();

        }
        if(!mBluetoothAdapter.isDiscovering()){

            //check BT permissions in manifest
//            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
        }
        mBluetoothConnection = new BluetoothConnectionService(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        getActivity().registerReceiver(mBroadcastReceiver4, filter);
        IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);

    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mBroadcastReceiver3);
        getActivity().unregisterReceiver(mBroadcastReceiver4);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_in, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public void enableScan() {
        mScanButton.setClickable(true);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    public void disableScan() {
        mScanButton.setClickable(false);
        mProgressBar.setVisibility(View.VISIBLE);

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void startConnection(){
        startBTConnection(mBTDevice,MY_UUID_INSECURE);
    }

    /**
     * starting chat service method
     */
    public void startBTConnection(BluetoothDevice device, UUID uuid){
        Log.d(TAG, "startBTConnection: Initializing RFCOM Bluetooth Connection.");

        mBluetoothConnection.startClient(device,uuid);

        Log.d(TAG, "FLAG THREE");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        signalArduino(prefs.getString("name", "Mickey"));

    }

    public void signalArduino(String name) {
        String text = "A";
        byte[] bytes = text.getBytes(Charset.defaultCharset());
        Log.d(TAG, text);

        if (mBluetoothConnection != null) {
            mBluetoothConnection.write(bytes);
        } else{
            Log.d(TAG, "NOPEEE");
        }

    }



    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = getActivity().checkPermission("Manifest.permission.ACCESS_FINE_LOCATION", 0, 0);
//            permissionCheck += getActivity().checkPermission("Manifest.permission.ACCESS_COARSE_LOCATION", 1, 1);
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

}
