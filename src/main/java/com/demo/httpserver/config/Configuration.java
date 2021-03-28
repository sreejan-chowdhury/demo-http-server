package com.demo.httpserver.config;

/**
 * TO store config
 * @author Sreejan
 *
 */
public final class Configuration {

	final int port;
	final String webRoot;
	
	
	
	public Configuration(int port, String webRoot) {
		this.port = port;
		this.webRoot = webRoot;
	}
	
	
	public int getPort() {
		return port;
	}
	public String getWebroot() {
		return webRoot;
	}
	
	
}
