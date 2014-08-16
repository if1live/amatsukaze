package net.yzwlab.javammd.format;

import org.junit.Test;
import org.junit.Assert;


public class ParserUtilTest {
	@Test
	public void testByteToString_success() {
		byte[] data = new byte[] {
			101, 121, 101, 50, 46, 98, 109, 112, 0, 
			-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3
		};
		String expected = "eye2.bmp";
		String actual = ParserUtil.byteToString(data);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testByteToString_null() {
		String actual = ParserUtil.byteToString(null);
		Assert.assertEquals("", actual);
	}
	
	@Test
	public void testByteToString_emptyString() {
		byte[] data = new byte[] {
			0, 0, 0, 0,
		};
		String actual = ParserUtil.byteToString(data);
		Assert.assertEquals("", actual);
	}
}
