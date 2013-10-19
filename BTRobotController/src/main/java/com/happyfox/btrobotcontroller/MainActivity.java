package com.happyfox.btrobotcontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;
import java.io.BufferedReader;

public class MainActivity extends Activity {

    private BluetoothAdapter mAdapter;
    private BluetoothDevice btDevice;
    private BluetoothSocket btSocket;
    private OutputStream btOutStream;
    private InputStream btInStream;
    private Button forwardButton;

    private int motorSpeed = 255;
    private Button backButton;
    private Button leftButton;
    private Button rightButton;
    private Button connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        btDevice = mAdapter.getRemoteDevice("00:06:66:08:5F:FB");

        forwardButton = (Button) findViewById(R.id.forward);
        forwardButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    writeToBt(255, 255);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    writeToBt(0,0);
                }
                return false;
            }
        });

        backButton = (Button) findViewById(R.id.back);
        backButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    writeToBt(-motorSpeed, -motorSpeed);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    writeToBt(0,0);
                }
                return false;
            }
        });

        leftButton = (Button) findViewById(R.id.left);
        leftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    writeToBt(-motorSpeed, motorSpeed);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    writeToBt(0,0);
                }
                return false;
            }
        });

        rightButton = (Button) findViewById(R.id.right);
        rightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    writeToBt(motorSpeed, -motorSpeed);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    writeToBt(0,0);
                }
                return false;
            }
        });

        connectButton = (Button) findViewById(R.id.reconnect);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect();
            }
        } );

        forwardButton.setEnabled(false);
        backButton.setEnabled(false);
        leftButton.setEnabled(false);
        rightButton.setEnabled(false);

        connect();

    }


    private void connect()
    {
        try {

            try {
                if(btSocket != null){
                    btSocket.close();
                }
            } catch (IOException e){
                e.printStackTrace();
            }

            UUID robotUuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

            btSocket = btDevice.createRfcommSocketToServiceRecord(robotUuid);
            btSocket.connect();
            btOutStream = btSocket.getOutputStream();
            btInStream = btSocket.getInputStream();

            forwardButton.setEnabled(true);
            backButton.setEnabled(true);
            leftButton.setEnabled(true);
            rightButton.setEnabled(true);

        } catch (IOException e) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this);

            // set title
            alertDialogBuilder.setTitle("Connect Error");

            // set dialog message
            alertDialogBuilder
                    .setMessage(e.getLocalizedMessage())
                    .setCancelable(false)
                    .setNegativeButton("Dismiss",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    }

    private void writeToBt(int lMotor, int rMotor)
    {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putShort((short) rMotor);
        bb.putShort((short) lMotor);

        try {
            byte[] byteArray = bb.array();
            btOutStream.write(byteArray);
            btOutStream.flush();
        } catch (IOException e) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

            // set title
            alertDialogBuilder.setTitle("Write Error");

            // set dialog message
            alertDialogBuilder
                    .setMessage(e.getLocalizedMessage())
                    .setCancelable(false)
                    .setNegativeButton("Dismiss",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    });

            // Disable the buttons until reconnect
            forwardButton.setEnabled(false);
            backButton.setEnabled(false);
            leftButton.setEnabled(false);
            rightButton.setEnabled(false);

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    }

  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
