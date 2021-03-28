package com.demo.httpserver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.demo.httpserver.core.HttpParser;

@TestInstance(Lifecycle.PER_CLASS)
public class HttpParserTest {

	private HttpParser httpParser;
	
	@BeforeAll
	public void beforeClass() {
		httpParser = new HttpParser();
	}
	
	private InputStream generateTestCase() {
		
		String testData = "GET / HTTP/1.1\r\n" + 
				"Host: localhost:8080\r\n" + 
				"Connection: keep-alive\r\n" + 
				"Cache-Control: max-age=0\r\n" + 
				"Upgrade-Insecure-Requests: 1\r\n" + 
				"User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36\r\n" + 
				"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9\r\n" + 
				"Sec-Fetch-Site: none\r\n" + 
				"Sec-Fetch-Mode: navigate\r\n" + 
				"Sec-Fetch-User: ?1\r\n" + 
				"Sec-Fetch-Dest: document\r\n" + 
				"Accept-Encoding: gzip, deflate, br\r\n" + 
				"Accept-Language: en-US,en;q=0.9\r\n"
				+ "\r\n";
		
		InputStream input = new ByteArrayInputStream(
				testData.getBytes(
						StandardCharsets.US_ASCII
						)
				);
		return input;
	}
	
//	@Test
	public void parseHttpRequestTest() throws IOException {
		System.out.println("Testing parseHttpRequestTest");
//		httpParser.parseHttpRequest(generateTestCase());
	}
}
