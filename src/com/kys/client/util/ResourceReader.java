package com.kys.client.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * properties 파일 읽기 전용
 *
 * @author kys0213
 * @since  2018. 11. 28.
 */
public class ResourceReader {
	
	private final String XML_EXTENSION = ".xml";

	/** Pseudo URL prefix for loading from the class path: "classpath:" */
	private final String CLASSPATH_URL_PREFIX = "classpath:";

	/** URL prefix for loading from the file system: "file:" */
	private final String FILE_URL_PREFIX = "file:";
	
	private final String path;
	
	public ResourceReader(String path) {
		super();
		this.path = path;
	}

	public Properties loadProp() throws IOException{

		if(path == null){
			throw new IllegalArgumentException("Properties 파일 경로 값이 null 입니다.");
		}

		Properties prop = new Properties();
		InputStream is = null;
		try{
			is = getResource(path);

			if(path.toLowerCase().endsWith(XML_EXTENSION)){
				prop.loadFromXML(is);
			}else{
				prop.load(is);
			}

			return prop;

		}finally{
			try { if(is != null) is.close(); } catch (IOException ignore) {}
		}
	}

	private InputStream getResource(String path) throws FileNotFoundException {
		if(path.startsWith(CLASSPATH_URL_PREFIX)){
			path = path.substring(CLASSPATH_URL_PREFIX.length());
			return this.getClass().getClassLoader().getResourceAsStream(path);
		}else if(path.startsWith(FILE_URL_PREFIX)){
			path = path.substring(FILE_URL_PREFIX.length());
			return new FileInputStream(new File(path));
		}else{
			return new FileInputStream(new File(path));
		}
	}
}
