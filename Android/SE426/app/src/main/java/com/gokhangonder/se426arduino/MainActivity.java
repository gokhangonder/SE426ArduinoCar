package com.gokhangonder.se426arduino;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.Provider;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity implements LifecycleObserver, View.OnClickListener {

    private final String TAG = "SE426_CAR";

    // Layout related
    private ImageButton forwardButton;
    private ImageButton backwardButton;
    private ImageButton stopButton;
    private ImageButton leftButton;
    private ImageButton rightButton;
    private Button connectButton;
    private Button button_track;

    private SwitchCompat lineTrackingSwitch;
    private SwitchCompat obstacleDetectionSwitch;
    private SeekBar detectionRangeSeekBar;
    private TextView detectionRangeTextView;
    private TextView carBluetoothStatusTextView;

    private TextView smartContractCommandTextView;
    private TextView smartContractGasTextView;
    private TextView smartContractBlockTextView;
    private TextView smartContractDateTextView;
    private TextView smartContractAddressTextView;

    // Others
    private static boolean isRecursionEnable = true;

    private String cmdText = null;
    private String lastCommand = "Stop";

    // Rinkeby infura endpoint urls
    // https://rinkeby.infura.io/v3/260ef79ce44d44d4893ab1345390f5ae
    // wss://rinkeby.infura.io/ws/v3/260ef79ce44d44d4893ab1345390f5ae

    // Smart Contract Related
    private final String INFURA_ENDOPOINT_URL = "https://rinkeby.infura.io/v3/260ef79ce44d44d4893ab1345390f5ae";
    private final String INFURA_ENDPOINT_WSS = "wss://rinkeby.infura.io/ws/v3/260ef79ce44d44d4893ab1345390f5ae";
    private final String contractAddress = "0x84E118709d8bFbCA9555E48065451f94d36EE30d";

    private final BigInteger gasLimit = BigInteger.valueOf(20_000_000_000L);
    private final BigInteger gasPrice = BigInteger.valueOf(4300000);

    private Web3j web3;
    private Credentials credentials;
    private SE426_Project_Car_sol_ArduinoCar arduinoCar;

    // Bluetooth Related
    private final static int CONNECTING_STATUS = 1; // used in bluetooth handler to identify message status
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update

    private String deviceName = null;
    private String deviceAddress;

    public static Handler handler;
    public static BluetoothSocket mmSocket;

    public static ConnectedThread connectedThread;
    public static CreateConnectThread createConnectThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        initComponents();
        connectToEthNetwork();
        createContract();
        //updateLastCommand();
        //sendCommand();

        // If a bluetooth device has been selected from SelectDeviceActivity
        deviceName = getIntent().getStringExtra("deviceName");
        if (deviceName != null) {
            // Get the device address to make BT Connection
            deviceAddress = getIntent().getStringExtra("deviceAddress");
            // Show progree and connection status
            /*
            When "deviceName" is found the code will call a new thread
            to create a bluetooth connection to the selected device
             */
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            createConnectThread = new CreateConnectThread(bluetoothAdapter, deviceAddress);
            createConnectThread.start();
            Log.d(TAG, "Bluetooth Device: " + deviceName + " | Address: " + deviceAddress);
            carBluetoothStatusTextView.setText("Connected!");
        }

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case CONNECTING_STATUS:
                        switch (msg.arg1) {
                            case 1:
                                carBluetoothStatusTextView.setText("Connected!");
                                break;
                            case -1:
                                carBluetoothStatusTextView.setText("Failed to connect!");
                                break;
                        }
                        break;

                    case MESSAGE_READ:
                        String arduinoMsg = msg.obj.toString(); // Read message from Arduino
//                        switch (arduinoMsg.toLowerCase()){
//                            case "led is turned on":
//                                imageView.setBackgroundColor(getResources().getColor(R.color.colorOn));
//                                textViewInfo.setText("Arduino Message : " + arduinoMsg);
//                                break;
//                            case "led is turned off":
//                                imageView.setBackgroundColor(getResources().getColor(R.color.colorOff));
//                                textViewInfo.setText("Arduino Message : " + arduinoMsg);
//                                break;
//                        }
                        break;
                }
            }
        };
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded() {
        // When app on background
        //Stop();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onAppDestroyed() {
        //Stop();
    }

    private void initComponents() {
        forwardButton = findViewById(R.id.button_forward);
        backwardButton = findViewById(R.id.button_backward);
        stopButton = findViewById(R.id.button_stop);
        leftButton = findViewById(R.id.button_left);
        rightButton = findViewById(R.id.button_right);
        button_track = findViewById(R.id.button_track);

        connectButton = findViewById(R.id.button);

        detectionRangeSeekBar = findViewById(R.id.detectionRangeSeekBar);
        detectionRangeTextView = findViewById(R.id.detectionRangeTextView);
        carBluetoothStatusTextView = findViewById(R.id.carStatusTextview);

        smartContractBlockTextView = findViewById(R.id.smartBlock);
        smartContractCommandTextView = findViewById(R.id.smartCmmnd);
        smartContractDateTextView = findViewById(R.id.smartDate);
        smartContractGasTextView = findViewById(R.id.smartGas);
        smartContractAddressTextView = findViewById(R.id.smartContractAddress);

        forwardButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        backwardButton.setOnClickListener(this);
        connectButton.setOnClickListener(this);

        Log.d(TAG, "initComponents");

    }

    private void createContract() {
        arduinoCar = SE426_Project_Car_sol_ArduinoCar.
                load(contractAddress, web3, credentials, gasLimit, gasPrice);
        smartContractAddressTextView.setText(contractAddress);
    }

    public void ShowToast(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_LONG).show());
    }

    public void connectToEthNetwork() {
        web3 = Web3j.build(new HttpService(INFURA_ENDOPOINT_URL));
//        WebSocketService socketService = new WebSocketService(INFURA_ENDPOINT_WSS, true);
//        web3 = Web3j.build(socketService);
        credentials = Credentials.
                create("a9e4e1e425ec2e85baefa0ee8a67372abd9ac41983a9e4c2d9b62c4c029a67fc");
        Web3ClientVersion clientVersion = null;
        ShowToast(" Now Connecting to Ethereum network");
        Log.d(TAG, "Now Connecting to Ethereum network!");
        try {
            //if the client version has an error the user will
            //not gain access if successful the user will get connnected
            clientVersion = web3.web3ClientVersion().sendAsync().get();
            if (!clientVersion.hasError()) {
                ShowToast("Connected!");
                Log.d(TAG, "Connected!");
            } else {
                ShowToast(clientVersion.getError().getMessage());
                Log.e(TAG, "Error: " + clientVersion.getError().getMessage());
            }
        } catch (Exception e) {
            ShowToast(e.getMessage());
            Log.e(TAG, "Exception: " + e.getMessage());
        }

        if (clientVersion != null) {
            String web3ClientVersionString = clientVersion.getWeb3ClientVersion();
            if (web3ClientVersionString == null) {
                Log.d(TAG, "NULL");
            } else {
                Log.d(TAG, web3ClientVersionString);
            }
        } else {
            Log.d(TAG, "NULL");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_forward:
                cmdText = "D";
                connectedThread.write(cmdText);
                new SmartContractTransaction().execute("Drive");
                ShowToast("Drive");
                break;
            case R.id.button_stop:
                cmdText = "S";
                connectedThread.write(cmdText);
                new SmartContractTransaction().execute("Stop");
                ShowToast("Stop");
                break;
            case R.id.button_left:
                cmdText = "L";
                connectedThread.write(cmdText);
                new SmartContractTransaction().execute("Left");
                ShowToast("Left");
                break;
            case R.id.button_right:
                cmdText = "R";
                connectedThread.write(cmdText);
                new SmartContractTransaction().execute("Right");
                ShowToast("Right");
                break;
            case R.id.button_backward:
                cmdText = "B";
                connectedThread.write(cmdText);
                ShowToast("Back");
                break;
            case R.id.button_track:
                cmdText = "T";
                connectedThread.write(cmdText);
                ShowToast("Back");
                break;
            case R.id.button:
                Intent intent = new Intent(MainActivity.this, SelectBluetoothDeviceActivity.class);
                startActivity(intent);
                break;
            default:
                ShowToast("Not Implemented Button");
                break;
        }
    }

    private void ShowLog(Future<TransactionReceipt> transactionReceipt, String str) {
        String result;
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        try {
            result = "Successful transaction. Command: " + str + " | Gas Used: "
                    + transactionReceipt.get().getGasUsed()
                    + " Block Number: "
                    + transactionReceipt.get().getBlockNumber()
                    + " | HH:mm:ss -> " + currentTime;
            Log.d(TAG, result);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Future<TransactionReceipt> Drive(Future<TransactionReceipt> transactionReceipt) {
        transactionReceipt = arduinoCar.Drive().sendAsync();
        ShowLog(transactionReceipt, "Drive");
        lastCommand = "Drive";
        return transactionReceipt;
    }

    private Future<TransactionReceipt> Right(Future<TransactionReceipt> transactionReceipt) {
        transactionReceipt = arduinoCar.Right().sendAsync();
        ShowLog(transactionReceipt, "Right");
        lastCommand = "Right";
        return transactionReceipt;
    }

    private Future<TransactionReceipt> Left(Future<TransactionReceipt> transactionReceipt) {
        transactionReceipt = arduinoCar.Left().sendAsync();
        ShowLog(transactionReceipt, "Left");
        lastCommand = "Left";
        return transactionReceipt;
    }

    private Future<TransactionReceipt> Stop(Future<TransactionReceipt> transactionReceipt) {
        transactionReceipt = arduinoCar.Stop().sendAsync();
        ShowLog(transactionReceipt, "Stop");
        lastCommand = "Stop";
        return transactionReceipt;
    }

    private class SmartContractTransaction extends AsyncTask<String, Void, String> {

        Future<TransactionReceipt> transactionReceipt;
        String cmd;

        @Override
        protected String doInBackground(String... strings) {
            cmd = strings[0];
            if (cmd == "Drive")
                transactionReceipt = Drive(transactionReceipt);
            else if (cmd == "Left")
                transactionReceipt = Left(transactionReceipt);
            else if (cmd == "Right")
                transactionReceipt = Right(transactionReceipt);
            else if (cmd == "Stop")
                transactionReceipt = Stop(transactionReceipt);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                smartContractGasTextView.setText("Used Gas: " + transactionReceipt.get().getGasUsed().toString());
                smartContractBlockTextView.setText("Block Number: " + transactionReceipt.get().getBlockNumber().toString());
                smartContractCommandTextView.setText("Command: " + cmd);
                smartContractDateTextView.setText("Transaction Complete Date: " + currentTime);

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public static class CreateConnectThread extends Thread {

        private final String TAG = "SE426_Bluetooth";

        public CreateConnectThread(BluetoothAdapter bluetoothAdapter, String address) {
            /*
            Use a temporary object that is later assigned to mmSocket
            because mmSocket is final.
             */
            BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
            BluetoothSocket tmp = null;
            UUID uuid = bluetoothDevice.getUuids()[0].getUuid();

            try {
                /*
                Get a BluetoothSocket to connect with the given BluetoothDevice.
                Due to Android device varieties,the method below may not work fo different devices.
                You should try using other methods i.e. :
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                 */
                tmp = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);

            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            bluetoothAdapter.cancelDiscovery();
            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
                Log.e("Status", "Device connected");
                handler.obtainMessage(CONNECTING_STATUS, 1, -1).sendToTarget();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                    Log.e("Status", "Cannot connect to device");
                    handler.obtainMessage(CONNECTING_STATUS, -1, -1).sendToTarget();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            connectedThread = new ConnectedThread(mmSocket);
            connectedThread.run();
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }

    /* =============================== Thread for Data Transfer =========================================== */
    public static class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes = 0; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    /*
                    Read from the InputStream from Arduino until termination character is reached.
                    Then send the whole String message to GUI Handler.
                     */
                    buffer[bytes] = (byte) mmInStream.read();
                    String readMessage;
                    if (buffer[bytes] == '\n') {
                        readMessage = new String(buffer, 0, bytes);
                        Log.e("Arduino Message", readMessage);
                        handler.obtainMessage(MESSAGE_READ, readMessage).sendToTarget();
                        bytes = 0;
                    } else {
                        bytes++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            byte[] bytes = input.getBytes(); //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.e("Send Error", "Unable to send message", e);
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    /* ============================ Terminate Connection at BackPress ====================== */
    @Override
    public void onBackPressed() {
        // Terminate Bluetooth Connection and close app
        if (createConnectThread != null) {
            createConnectThread.cancel();
        }
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
