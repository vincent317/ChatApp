package com.example.android.myapplication;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private ListView messages;
    private String mes;
    private Button send;
    private EditText edit;
    private BaseAdapter adapter;
    private List<MyMessage> messList = new ArrayList<>();
    private String username;

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {

                messages.setAdapter(adapter);
                messages.setSelection(adapter.getCount() - 1);
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(edit.getWindowToken(), 0);
                edit.setText("");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);
        messages = findViewById(R.id.messages);
        send = findViewById(R.id.send);
        edit = findViewById(R.id.editText);
        getSupportActionBar().hide();
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return messList.size();
            }
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = ChatRoomActivity.this.getLayoutInflater();
                View view;

                if (convertView == null) {
                    view = inflater.inflate(R.layout.listview_item, null);
                } else {
                    view = convertView;
                }
                TextView time = (TextView) view.findViewById(R.id.time);
                time.setText(messList.get(position).getTime());
                TextView username = (TextView) view.findViewById(R.id.name);
                username.setText(messList.get(position).getUsername());
                TextView text = (TextView) view.findViewById(R.id.text);
                text.setText(messList.get(position).getMessage());
                return view;
            }
            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public Object getItem(int position) {//获取数据集中与指定索引对应的数据项
                return null;
            }
        };
        username = getIntent().getExtras().getString("username");

        messages.setAdapter(adapter);
        Thread t1 = new Thread() {
            public void run() {
                try {
                    client = new Socket(SocketServer.host, SocketServer.port);
                    in = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                            client.getOutputStream(), "UTF-8")), true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String msg = edit.getText().toString();
                new Thread() {
                    public void run() {
                        if (client.isConnected() && !client.isOutputShutdown()) {
                            try {
                                out.write("USRNAME:"+username+";MSG:"+msg+'\n');
                                out.flush();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
            }
        });

        new Thread() {
            public void run() {
                try {
                    while(true){
                        String processing= in.readLine();
                        if(processing != null) {
                            int firstend = processing.indexOf(";MSG:");
                            String from = processing.substring(processing.indexOf('/') + 1, firstend);
                            String message = processing.substring(firstend + 5);
                            messList.add(new MyMessage(from, message, new Date()));
                            handler.sendEmptyMessage(0x123);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}