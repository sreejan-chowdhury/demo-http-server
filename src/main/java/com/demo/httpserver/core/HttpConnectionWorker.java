package com.demo.httpserver.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

import com.demo.httpserver.config.ConfigManager;

public class HttpConnectionWorker extends Thread {

	private Socket socket;

	public HttpConnectionWorker(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			

			inputStream = socket.getInputStream();

			outputStream = socket.getOutputStream();

//			HttpParser httpParser = new HttpParser();
//			httpParser.parseHttpRequest(inputStream);
			Map<String, String>  headers = HttpParser.parseHTTPHeaders(inputStream);
			
			long fileSize = (Long.parseLong(headers.get("Content-Length")));
			//number of loops to get the next set of bytes
			int i = 1;
			long bytesRemaining = (fileSize) - (i * 1024);
			byte[] bytes = new byte[1024];
			OutputStream outStream =  null;
			
			try {
				File targetFile = new File(ConfigManager.getInstance().getConfig().getWebroot() + "/" 
						+ headers.get("FILENAME"));
				targetFile.getParentFile().mkdirs();

				outStream = new FileOutputStream(targetFile);
				
				if(fileSize <= 1024) 
		        {
		          bytes = new byte[inputStream.available()];
		          inputStream.read(bytes);
		          outStream.write(bytes);
		        }
				else {
					  while (inputStream.read(bytes) != -1) {
		                i++;
		                outStream.write(bytes);
		                bytesRemaining =  ( fileSize - ((i-1) * 1024));
		                if (bytesRemaining >= 1024) {
		                  bytes = new byte[1024];
		                }
		                else {
		                  bytes = new byte[inputStream.available()];
	
		                  inputStream.read(bytes);
		                  outStream.write(bytes);
		                  break;
		                }
		              }
		            }
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				outStream.close();
			}
			
//			int _byte;
//			while((_byte = inputStream.read()) >= 0) {
//				System.out.print((char) _byte);
//			}
			
			// 1. not bothered of what comes from browse
			// so we are only writting
			String html = "OK";

			final String CRLF = "\n\r"; // 13 10

			String response = "HTTP/1.1 200 OK" + CRLF// Status : HTTP_VERSION STATUS_CODE MSG
					+ "Content-Length: " + 0/*html.getBytes().length*/ + CRLF // Header
					+ CRLF // HEADER ENDS
					+ html + CRLF + CRLF;
			outputStream.write(response.getBytes());


			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if(inputStream != null)
					inputStream.close();
				if(outputStream != null)
					outputStream.close();
				if(socket != null)
					socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
}
