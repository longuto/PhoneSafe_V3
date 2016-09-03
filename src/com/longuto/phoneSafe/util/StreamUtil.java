package com.longuto.phoneSafe.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {

	/**
	 * ��������ת��Ϊ�ֽ�����
	 * @param is 
	 * @return �ֽ�����
	 * @throws IOException 
	 */
	public static byte[] parseInputstram(InputStream is) throws IOException {
		ByteArrayOutputStream	baos = new ByteArrayOutputStream();	// �ڴ��ֽ������
		byte[] arr = new byte[1024 * 4];	// ����һ���ֽ�����
		int len = 0;
		while((len = is.read(arr)) != -1) {
			baos.write(arr, 0, len);
		}
		if(baos != null) baos.close();	// ����
		if(is != null) is.close();	// ����
		return baos.toByteArray();
	}

}
