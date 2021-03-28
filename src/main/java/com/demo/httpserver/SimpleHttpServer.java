package com.demo.httpserver;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.httpserver.config.ConfigManager;
import com.demo.httpserver.config.Configuration;
import com.demo.httpserver.core.HttpServerListener;

public class SimpleHttpServer {

	private final static Logger LOGGER = LoggerFactory.getLogger(SimpleHttpServer.class);
//	private static Scanner in;
	
	public static void main(String[] args) throws IOException {
//		System.out.println("a0" + args[0] != null ? args[0] : "NA");
//		System.out.println("a1" + args[1] != null ? args[1] : "NA" );
//		in = new Scanner(System.in);
//		System.out.println("Enter port");
		int port = args[0] != null ? Integer.parseInt(args[0]) : 8080;//in.nextInt(); 
//		System.out.println("Enter folderPath");
		String webRoot =  args[1] != null ? args[1] : "/tmp" ;//in.next(); 
		
		
		ConfigManager.getInstance().setConfig(port, webRoot);
		Configuration c= ConfigManager.getInstance().getConfig();
		
//		HttpServerStarter.start();
		
		LOGGER.info("HTTP Server strted with config ");
		LOGGER.info("port :"+c.getPort());
		LOGGER.info("folder-path :"+c.getWebroot());
		
		HttpServerListener httpServerListener = new HttpServerListener(port, webRoot);
		httpServerListener.start();
	}

}
