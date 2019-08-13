package com.example.android.myapplication;

import android.os.Bundle;
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

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_up);

		username = (EditText)findViewById(R.id.signup_username);
		password = (EditText)findViewById(R.id.signup_password);
		finish = (TextView)findViewById(R.id.signup_finish);

		finish.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				final String uname = username.getText().toString();
				final String upassword = password.getText().toString();

				if (uname.length() == 0 || upassword.length() == 0) {
					Toast.makeText(SignupActivity.this, "用户名或密码不得为空", Toast.LENGTH_LONG);
				} else {
					new Thread() {
						public void run() {
							Socket socket = null;
							BufferedReader in = null;
							PrintWriter out = null;
							try {
								socket = new Socket(SocketServer.host, SocketServer.port);
								in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
								out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
									socket.getOutputStream(), "UTF-8")), true);

								out.println("newusername:"+ uname+';'+"newnuserpassword"+upassword+'\n');
								out.flush();
								String serverReturn = in.readLine();
								socket.close();
								in.close();
								out.close();
								if(serverReturn.equals("success")){
									Toast.makeText(SignupActivity.this, "账号创建成功！", Toast.LENGTH_SHORT);
									finish();
								}else{
									Toast.makeText(SignupActivity.this, "用户名重复", Toast.LENGTH_LONG);
									socket.close();
								}
							} catch (Exception e) {
								Toast.makeText(SignupActivity.this, "网络异常，请稍后重试", Toast.LENGTH_LONG);
								e.printStackTrace();
								try {
									if (socket != null) {
										socket.close();
									}
									if(in != null){
										in.close();
									}
									if(out != null){
										out.close();
									}
								}catch(Exception ee) {
									ee.printStackTrace();
								}
							}
						}
					}.start();
				}
			}
		});
	}
}
