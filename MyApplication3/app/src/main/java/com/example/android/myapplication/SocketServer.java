package com.example.android.myapplication;

public class SocketServer {
	public static final String host = "106.13.165.209";
	public static final int port = 13345;
}
/*
package com.example.android.myapplication;
	import java.io.*;
	import java.net.ServerSocket;
	import java.net.Socket;
	import java.net.SocketTimeoutException;
	import java.util.ArrayList;
	import java.util.Iterator;
	import java.util.List;

public class SocketServer {

	private static final int port = 13345;
	private static ServerSocket server = null;
	private static List<Socket> sockets =  new ArrayList<>();

	public static void main(String[] args)  {
		try {
			server = new ServerSocket(port);
			new Thread() {
				public void run() {
					checkNewMessages();
				}
			}.start();
			while(true) {
				Socket client = server.accept();
				synchronized(sockets){
					sockets.add(client);
				}
				Thread.currentThread().interrupt();
			}

		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void checkNewMessages()  {
		while(true) {
			synchronized(sockets) {
				Iterator<Socket> iterator = sockets.iterator();
				while(iterator.hasNext()) {
					Socket socket = iterator.next();
					String msg = null;
					try {
						socket.setSoTimeout(1);
						BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
						msg = in.readLine();
					}catch(SocketTimeoutException ste) {
						continue;
					}catch(IOException e) {
						e.printStackTrace();
					}
					if(msg != null) {
						if(msg.startsWith("DISCONNECT")) {
							try {
								iterator.remove();
								socket.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}else if(msg.startsWith("STILLHERE")) {
							System.out.println("stillhere");
							continue;
						}else if(msg.startsWith("LOGIN")||msg.startsWith("SIGNUP")){
							System.out.println("login");
							PrintWriter pw = null;
							try {
								pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8")),true);
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							MysqlManager.openConnection();
							int a = msg.indexOf(':');
							int b = msg.indexOf(";PASSWORD:");
							String username = msg.substring(a+1,b);
							String password = msg.substring(b+10);
							if(msg.startsWith("LOGIN")) {
								int res = MysqlManager.checkPassword(username, password);
								if(res == MysqlManager.MATCHED) {
									pw.write("SUCCESSLOGIN\n");
								}else if(res == MysqlManager.UNMATCHED) {
									pw.write("WRONGPASSWORD\n");
								}else {
									pw.write("NOSUCHUSER\n");
								}
							}else {
								boolean res = MysqlManager.insertUser(username, password);
								if(res) {
									pw.write("SUCCESSSIGNUP\n");
								}else {
									pw.write("DUPLICATENAME\n");
								}
							}
							pw.flush();
							MysqlManager.closeConnection();
						}else {
							Iterator<Socket> niterator = sockets.iterator();
							while(niterator.hasNext()) {
								Socket temp = niterator.next();
								try {
									PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(temp.getOutputStream(),"UTF-8")),true);
									pw.write(msg+'\n');
									pw.flush();
								}catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
			Thread.currentThread().interrupt();
		}

	}
}*/