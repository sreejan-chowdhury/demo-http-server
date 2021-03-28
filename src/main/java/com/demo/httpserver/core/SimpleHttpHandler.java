package com.demo.httpserver.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.httpserver.config.ConfigManager;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class SimpleHttpHandler implements HttpHandler {

	private final static Logger LOGGER = LoggerFactory.getLogger(SimpleHttpHandler.class);

	public void handle(HttpExchange exchange) throws IOException {
		// TODO Auto-generated method stub

		LOGGER.debug("*** Inside handler *****");
		// handle the request
		handleFileUploadRequest(exchange);

		OutputStream out = null;
		StringBuilder htmlBuilder = new StringBuilder();
		try {
			htmlBuilder.append("OK");

			exchange.sendResponseHeaders(200, htmlBuilder.toString().length());
			out = exchange.getResponseBody();
			out.write(htmlBuilder.toString().getBytes());
			out.flush();
			out.close();
		} catch (Exception ex) {
			LOGGER.error("Error : ", ex);
			if (out != null)
				out.close();
		}

	}

	private void handleFileUploadRequest(HttpExchange exchange) throws IOException {

		LOGGER.debug("httpExchange.getRequestURI() :" + exchange.getRequestURI().getPath());
		String[] pathParams = exchange.getRequestURI().toString().split("/");
		String fileName = pathParams[pathParams.length - 1];
		LOGGER.info("fileName :" + fileName);
		String method = exchange.getRequestMethod();
		LOGGER.info("Method : " + method);
		LOGGER.debug("Headers : " + exchange.getRequestHeaders());
		Headers reqHeaders = exchange.getRequestHeaders();

		String contentType = reqHeaders.getFirst("Content-Type");

		LOGGER.debug("contentType : " + contentType);

		if (!contentType.startsWith("multipart/")) {
			throw new IOException("Multipart content required");
		}

		// parse it to get all params.
		// can be used to get the boundary, which in turn will be used to get the actual
		// body
		// need to put it in parts to get the actual content
		HttpParser parser = new HttpParser();
		Map<String, String> params = parser.parse(contentType);
		String encoding = params.get("charset");
		if (encoding == null) {
			encoding = "ISO-8859-1";
		}
		InputStream in = exchange.getRequestBody();
		try {


			uploadFile(in, fileName, reqHeaders);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			parser = null;
			params = null;
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void uploadFile(InputStream in, String fileName, Headers headers) throws IOException {

		String contentLength = headers.get("Content-length").toString();
		LOGGER.debug("contentLength : " + contentLength);
		long fileSize = (Long.parseLong(contentLength.substring(1, contentLength.length() - 1)));
		//number of loops to get the next set of bytes
		int i = 1;
		long bytesRemaining = (fileSize) - (i * 1024);
		byte[] bytes = new byte[1024];
		OutputStream outStream =  null;
		
		try {
			File targetFile = new File(ConfigManager.getInstance().getConfig().getWebroot() + "/" + fileName);
			targetFile.getParentFile().mkdirs();

			outStream = new FileOutputStream(targetFile);
			
			if(fileSize <= 1024) 
	        {
	          bytes = new byte[in.available()];
	          in.read(bytes);
	          outStream.write(bytes);
	        }
			else {
				  while (in.read(bytes) != -1) {
	                i++;
	                outStream.write(bytes);
	                bytesRemaining =  ( fileSize - ((i-1) * 1024));
	                if (bytesRemaining >= 1024) {
	                  bytes = new byte[1024];
	                }
	                else {
	                  bytes = new byte[in.available()];

	                  in.read(bytes);
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
	}
}
