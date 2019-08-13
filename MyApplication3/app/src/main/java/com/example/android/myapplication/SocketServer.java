package com.example.android.myapplication;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {
	private static final int port = 1;
	private List<Socket> clients = new ArrayList<Socket>();
	private ServerSocket server = null;
	private ExecutorService threadPool = null;
	public static void main(String[] args)  {
		new SocketServer();
	}
	
	public SocketServer() {
    	try {
    		server = new ServerSocket(port);
    		threadPool = Executors.newCachedThreadPool();
    		while(true) {
    			Socket newClient = server.accept();
    			clients.add(newClient);
    			threadPool.execute(new clientTask(newClient));
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    class clientTask implements Runnable{
    	
    	private Socket client;
    	private BufferedReader in = null;
    	
    	private clientTask(Socket temp) {
    		client = temp;
    		try {
    			in = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
    			
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}

		@Override
		public void run() {
			while(true) {
				try {
					String msg = in.readLine();
					if(msg != null) {
						sendMessage(client.getInetAddress().toString().substring(1)+":"+msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		public void sendMessage(String s) throws IOException {
			for(int i = 0;i < clients.size();i++) {
				Socket client = clients.get(i);  
				try {
					PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter( client.getOutputStream(),"UTF-8")),true);
                	pw.write(s+"\n");
                	pw.flush();
                }catch (IOException e) {e.printStackTrace();}  
			}
		}
    }
}
