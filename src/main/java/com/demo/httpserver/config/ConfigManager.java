package com.demo.httpserver.config;

public class ConfigManager {
	
	private volatile static ConfigManager instance;
	private static Configuration config;

	private ConfigManager() {}
	
	public static ConfigManager getInstance() {
		if (instance == null) {
			synchronized (ConfigManager.class) {
				if (instance == null) {
					instance = new ConfigManager();
				}
			}
		}
		return instance;
	}
	
	//set the Configuration
	public void setConfig(final int port,final String webRoot) {
		config = new Configuration(port, webRoot);
	}
	
	/*//load config file
	*//**
	 * 
	 * @param fileName fullpath
	 * @throws IOException 
	 *//*
	public void loadConfigFile(String fileName) throws IOException {
		FileReader fileReader =  null;
		
		try {
			fileReader = new FileReader(fileName);
		}catch(FileNotFoundException e) {
			throw new RuntimeException("File not Found on Path",e);
		}
		
		
		StringBuffer sb = new StringBuffer();
		int i ;
		while((i = fileReader.read()) != -1) {
			sb.append((char)i);
		}
		
		JsonNode conf = Json.parse(sb.toString());
		config = Json.fromJson(conf, Configuration.class);
	}*/

	/**
	 * gives the Current loaded COnfig
	 */
	public Configuration getConfig() {
		if(config == null) {
			throw new RuntimeException("No Configuration Found");
		}
		
		return config;
	}
}
