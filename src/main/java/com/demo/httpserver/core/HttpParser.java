package com.demo.httpserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpParser {

	private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);
	
	/*public void parseHttpRequest(InputStream input ) throws IOException {
		
		Map<String, String>  headers = parseHTTPHeaders(input);
		
		byte[] bytes = new byte[1024];
		OutputStream outStream =  null;
		
		try {
			File targetFile = new File(ConfigManager.getInstance().getConfig().getWebroot() + "/" 
		+ headers.get("FILENAME"));
			targetFile.getParentFile().mkdirs();

			outStream = new FileOutputStream(targetFile);
			
//			if(fileSize <= 1024) 
//	        {
	          bytes = new byte[input.available()];
	          input.read(bytes);
	          outStream.write(bytes);
//	        }
//			else {
//				  while (in.read(bytes) != -1) {
//	                i++;
//	                outStream.write(bytes);
//	                bytesRemaining =  ( fileSize - ((i-1) * 1024));
//	                if (bytesRemaining >= 1024) {
//	                  bytes = new byte[1024];
//	                }
//	                else {
//	                  bytes = new byte[in.available()];
//
//	                  in.read(bytes);
//	                  outStream.write(bytes);
//	                  break;
//	                }
//	              }
//	            }
//			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			outStream.close();
		}
		
		InputStreamReader isr =  new InputStreamReader(input);
        BufferedReader reader = new BufferedReader(isr);
        String line = reader.readLine();            
        while (line != null && !line.isEmpty()) {
            System.out.println(line);
            line = reader.readLine();
        }
		
//        LOGGER.debug("######StringTokenizer##############");
		
//		StringTokenizer parse = new StringTokenizer(
//				new BufferedReader(
//						new InputStreamReader(input)
//				).readLine());
//		
//		while(parse.hasMoreTokens()) {
//			System.out.println( parse.nextToken());
//		}
		
//		String method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
//		
//		System.out.println("METHOD "+method);
		
		
	}*/
	public static Map<String, String> parseHTTPHeaders(InputStream inputStream)
	        throws IOException {
		
	    int charRead;
	    StringBuffer sb = new StringBuffer();
	    while (true) {
	        sb.append((char) (charRead = inputStream.read()));
	        if ((char) charRead == '\r') {            // if we've got a '\r'
	            sb.append((char) inputStream.read()); // then write '\n'
	            charRead = inputStream.read();        // read the next char;
	            if (charRead == '\r') {                  // if it's another '\r'
	                sb.append((char) inputStream.read());// write the '\n'
	                break;
	            } else {
	                sb.append((char) charRead);
	            }
	        }
	    }

	    String[] headersArray = sb.toString().split("\r\n");
	    //process the first line
	    //contains the Method and Path
	    String [] firstLine = headersArray[0].split(" ");
	    Map<String, String> headers = new HashMap<String, String>();
	    headers.put("METHOD", firstLine[0].trim());
	    
	    String[] pathParams = firstLine[1].trim().split("/");
	    headers.put("FILENAME",pathParams.length > 0 ? pathParams[pathParams.length - 1] : "");
	    
	    for (int i = 1; i < headersArray.length - 1; i++) {
	        headers.put(headersArray[i].split(": ")[0],
	                headersArray[i].split(": ")[1]);
	    }

	    return headers;
	}
	
	
	
	//taken from a blog for reference 
	public Map<String,String> parse(final String s) {
		char chars[] = s.toCharArray();
		int pos = 0;
        Map<String,String> result = new HashMap<String,String>();
        while (pos < chars.length) {
            skipSpaces(pos, chars);
            if (pos >= chars.length) {
                break;
            }
            StringBuilder buf = new StringBuilder();
            int nameEnd = 0;
            char c = ' ';
            while (pos < chars.length) {
                c = chars[pos++];
                if (c == '=' || c == ';') {
                    break;
                }                
                if (c == '"') {
                    quotedString(buf, chars, pos);
                    nameEnd = buf.length();
                } else {
                    buf.append(c);
                    if (!Character.isWhitespace(c)) {
                        nameEnd = buf.length();
                    }
                }
            }
            
            String name = buf.substring(0, nameEnd).toLowerCase();
            skipSpaces(pos, chars);;
            if (c != '=') {
                result.put("", name);
            } else {
                buf.setLength(0);
                int valEnd = 0;
                while (pos < chars.length) {
                    c = chars[pos++];
                    if (c == ';') {
                        break;
                    }
                    if (c == '"') {
                        quotedString(buf, chars, pos);
                        valEnd = buf.length();
                    } else {
                        buf.append(c);
                        if (!Character.isWhitespace(c)) {
                            valEnd = buf.length();
                        }
                    }
                }
                String value = buf.substring(0, valEnd);
                result.put(name.toLowerCase(), value);
            }
        }
        return result;
    }

    private void skipSpaces(int pos,char chars[]) {
        while (pos < chars.length && Character.isWhitespace(chars[pos])) {
            ++pos;
        }
    }

    private void quotedString(StringBuilder buf,char chars[],int pos) {
        while (pos < chars.length) {
            char c = chars[pos++];
            if (c == '"') {
                break;
            } else if (c == '\\' && pos < chars.length) {
                c = chars[pos++];
            }
            buf.append(c);
        }
    }
}
