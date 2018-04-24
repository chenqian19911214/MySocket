package com.example.chenq.mysocket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private Socket socket;
    private EditText edit_query;
    private  SocketClass socketClass;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String srint = (String) msg.obj;

            Toast.makeText(MainActivity.this, srint, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();


     /*   new Thread(new Runnable() {
            @Override
            public void run() {
                servier();
            }
        }).start();*/

         socketClass = new   SocketClass();
    }

    public void sendMessd(View view) {
/*

        final String ser = submit();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final PrintStream ps = new PrintStream(socket.getOutputStream());
                    ps.println(ser);
                    //outputStream.write(ser.getBytes("utf-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
*/


        socketClass.sendMesser(submit());
    }


    private void servier() {
        try {
            socket = new Socket("192.168.1.126", 30000);

            boolean isconne = socket.isConnected();

            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String string = bufferedReader.readLine();
            Message message = new Message();
            message.obj = string;
            handler.sendMessage(message);
            Log.i("chenqian", "string:" + string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        edit_query = (EditText) findViewById(R.id.edit_querysss);
    }

    private String submit() {
        // validate
        String query = edit_query.getText().toString().trim();
        if (TextUtils.isEmpty(query)) {
            Toast.makeText(this, "query不能为空", Toast.LENGTH_SHORT).show();
            // return;
        }

        return query;
        // TODO validate success, do something


    }
}
