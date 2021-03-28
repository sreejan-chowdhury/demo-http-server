package com.demo.httpserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServerListener extends Thread{
	
	private final static Logger LOGGER = LoggerFactory.getLogger(HttpServerListener.class);

	private int port;
	private String webRoot;
	private ServerSocket serverSocket;
	
	
	public HttpServerListener(int port, String webRoot) throws IOException {
		this.port = port;
		this.webRoot = webRoot;
		this.serverSocket = new ServerSocket(port);;
	}



	@Override
	public void run() {
		
		
		Socket socket = null;
		try {
			
			while(serverSocket.isBound() && !serverSocket.isClosed()) {
				socket = serverSocket.accept(); //halts here until connection is made
				//next line are not exceuted until connection is made
				LOGGER.info("Connection made : "+socket.getInetAddress());
				HttpConnectionWorker httpConnectionWorker =  new HttpConnectionWorker(socket);
				httpConnectionWorker.start();
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally {
			if(serverSocket != null)
				try {
					serverSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	}
}
