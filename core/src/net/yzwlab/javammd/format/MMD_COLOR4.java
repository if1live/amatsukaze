package net.yzwlab.javammd.format;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.badlogic.gdx.graphics.Color;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class MMD_COLOR4 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public byte r;

	public byte g;

	public byte b;

	public byte a;

	public MMD_COLOR4() {
		this.r = 0;
		this.g = 0;
		this.b = 0;
		this.a = 0;
	}

	public MMD_COLOR4 Read(IReadBuffer buffer) throws ReadException {
		assert buffer != null;
		this.r = (byte)(buffer.readFloat() * 255);
		this.g = (byte)(buffer.readFloat() * 255);
		this.b = (byte)(buffer.readFloat() * 255);
		this.a = (byte)(buffer.readFloat() * 255);
		return this;
	}
	
	public float toFloatBits() {
		//http://stackoverflow.com/questions/13469681/how-to-convert-4-bytes-array-to-float-in-java
		//함수이름은 gdx 참고
		byte[] data = new byte[4];
		data[0] = r;
		data[1] = g;
		data[2] = b;
		data[3] = a;
		float f = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getFloat();
		return f;
	}
	
	public Color toColor() {
		int bitR = (int)r << 24 & (int)255 << 24;
		int bitG = (int)g << 16 & (int)255 << 16;
		int bitB = (int)b << 8 & (int)255 << 8;
		int bitA = (int)a & (int)255;
		int rgba8888 = bitR | bitG | bitB | bitA;
		return new Color(rgba8888);
	}
}