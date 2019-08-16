package com.example.android.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;
import java.net.Socket;

public class SignupActivity extends AppCompatActivity {
	private EditText username, password;
	private TextView finish;
	private Socket socket;

	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0x111) {
				Intent loginPage = new Intent(SignupActivity.this, MainActivity.class);
				startActivity(loginPage);
				finish();
			}else if(msg.what == 0x222){
				Toast.makeText(SignupActivity.this,"用户名已被使用！",Toast.LENGTH_SHORT).show();
			}else if(msg.what == 0x333){
				Toast.makeText(SignupActivity.this,"用户名/密码不得为空！",Toast.LENGTH_SHORT).show();
			}
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_up);
		getSupportActionBar().hide();

		username = (EditText)findViewById(R.id.signup_username);
		password = (EditText)findViewById(R.id.signup_password);
		finish = (TextView)findViewById(R.id.signup_finish);

		finish.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				final String uname = username.getText().toString();
				final String upassword = password.getText().toString();

				if (uname.length() == 0 || upassword.length() == 0) {
					handler.sendEmptyMessage(0x333);
				} else {
					new Thread() {
						public void run() {
							BufferedReader in = null;
							PrintWriter out = null;
							try {
								socket = new Socket(SocketServer.host, SocketServer.port);
								in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
								out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
									socket.getOutputStream(), "UTF-8")), true);
								if(socket.isConnected() && !socket.isOutputShutdown()) {
									out.write("SIGNUP:" + uname + ";PASSWORD:" + upassword + '\n');
									out.flush();
								}
								if(socket.isConnected() && !socket.isInputShutdown()) {
									String serverReturn = in.readLine();
									out.write("DISCONNECT\n");
									out.flush();
									socket.close();
									if(serverReturn.equals("SUCCESSSIGNUP")){
										handler.sendEmptyMessage(0x111);
									}else if(serverReturn.equals("DUPLICATENAME")){
										handler.sendEmptyMessage(0x222);
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}.start();
				}
			}
		});
	}
}
