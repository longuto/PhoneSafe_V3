package com.longuto.phoneSafe.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {

	/**
	 * 将输入流转换为字节数组
	 * @param is 
	 * @return 字节数组
	 * @throws IOException 
	 */
	public static byte[] parseInputstram(InputStream is) throws IOException {
		ByteArrayOutputStream	baos = new ByteArrayOutputStream();	// 内存字节输出流
		byte[] arr = new byte[1024 * 4];	// 定义一个字节数组
		int len = 0;
		while((len = is.read(arr)) != -1) {
			baos.write(arr, 0, len);
		}
		if(baos != null) baos.close();	// 关流
		if(is != null) is.close();	// 关流
		return baos.toByteArray();
	}

}
