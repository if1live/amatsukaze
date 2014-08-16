package net.yzwlab.javammd.format;

public class ParserUtil {
	public static String byteToString(byte[] data) {
		if(data == null) {
			return "";
		}
		if(data[0] == 0) {
			return "";
		}
		
		//shift-jis 일 경우 대응 코드 추가하기
		
		int stringLen = 0;
		for(int j = 0 ; j < data.length ; ++j) {
			if(data[j] == 0) {
				break;
			}
			stringLen++;
		}
		String filename = new String(data, 0, stringLen);
		return filename;
	}
	
}
