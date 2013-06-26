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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.UUID;

public class MainActivity extends Activity {

    private BluetoothAdapter mAdapter;
    private BluetoothDevice btDevice;
    private BluetoothSocket btSocket;
    private OutputStream btOutStream;
    private Button forwardButton;

    private int motorSpeed = 150;
    private Button backButton;
    private Button leftButton;
    private Button rightButton;
    private Button connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        btDevice = mAdapter.getRemoteDevice("00:11:11:31:71:67");

        connect();

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


            btSocket = btDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"));
            btSocket.connect();
            btOutStream = btSocket.getOutputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToBt(int lMotor, int rMotor)
    {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putShort((short)lMotor);
        bb.putShort((short)rMotor);

        try {
            btOutStream.write(bb.array());
            btOutStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
