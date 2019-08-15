package com.example.android.myapplication;

public class SocketServer {
	public static final String host = "192.168.3.151";
	public static final int port = 13345;
}

/*package com.example.android.myapplication;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

public class SocketServer {

	private static final String host = "106.13.165.209";
	private static final int port = 13345;
	private static ServerSocket server = null;
	private static Queue<String> messagePool = new ConcurrentLinkedQueue<>();
	private static List<Socket> sockets =  new ArrayList<>();

    public static void main(String[] args)  {
    	try {
    		server = new ServerSocket(port);
    		new Thread() {
    			public void run() {
    				try {
						checkNewMessages();
					} catch (Exception e) {
						e.printStackTrace();
					}
    			}
    		}.start();
    		new Thread() {
    			public void run() {
    				try {
						allSend();
					} catch (Exception e) {
						e.printStackTrace();
					}
    			}
    		}.start();
    		while(true) {
    			Socket client = server.accept();
    			sockets.add(client);
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    public synchronized static void checkNewMessages() throws Exception {
    	while(true) {
    		for(Socket socket: sockets) {
    			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
    			String msg = in.readLine();
    			if(msg != null) {
    				if(msg.startsWith("DISCONNECT")) {
    					sockets.remove(socket);
    					socket.close();
    				}else if(msg.startsWith("LOGIN")||msg.startsWith("SIGNUP")){
    	    			PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8")),true);
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
    	    					pw.write("DUPLICATEDNAME\n");
    	    				}
    	    			}
    	    			pw.flush();
    	    			MysqlManager.closeConnection();
    				}else {
    					messagePool.add(msg);
    				}
    			}
    		}
    	}
    }

    public synchronized static void allSend() {
    	while(true) {
    		if(!messagePool.isEmpty()) {
    			String processing = messagePool.poll();
    			for(Socket temp:sockets) {
    				try {
    					PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(temp.getOutputStream(),"UTF-8")),true);
                    	pw.write(processing+"\n");
                    	pw.flush();
                    }catch (IOException e) {
                    	e.printStackTrace();
                    }
    			}
    		}
    	}
    }
}*/

