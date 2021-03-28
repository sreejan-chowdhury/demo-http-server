package com.demo.httpserver.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.httpserver.config.ConfigManager;
import com.demo.httpserver.config.Configuration;
import com.sun.net.httpserver.HttpServer;

public class HttpServerStarter {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(HttpServerStarter.class);
	
//	private int port;
//	private String folderPath;
	
	private ExecutorService executor;
    private HttpServer httpServer;
    
    private static final int THREADS = 10;
    
    private HttpServerStarter(/*int port,String folderPath*/) throws IOException {
    	
    	Configuration c= ConfigManager.getInstance().getConfig();
//    	this.port = port;
//    	this.folderPath = folderPath;
    	
    	InetSocketAddress addr = new InetSocketAddress(/*"localhost",*/c.getPort());
    	
    	httpServer = HttpServer.create(addr, 0);
        httpServer.createContext("/", new SimpleHttpHandler());
        boolean started = false;
        executor = Executors.newFixedThreadPool(THREADS);
        try {
            httpServer.setExecutor(executor);
            httpServer.start();
            started = true;
            LOGGER.info("***** Server started ******");
        } finally {
            if (!started) {
                executor.shutdownNow();
            }
        }
    }
	
	public static HttpServerStarter start(/*int port,String folderPath*/) throws IOException {
		
		return new HttpServerStarter(/*port,folderPath*/);
	}

}
