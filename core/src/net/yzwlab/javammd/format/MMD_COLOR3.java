package net.yzwlab.javammd.format;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.badlogic.gdx.graphics.Color;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class MMD_COLOR3 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public byte r;

	public byte g;

	public byte b;

	public MMD_COLOR3() {
		this.r = 0;
		this.g = 0;
		this.b = 0;
	}

	public MMD_COLOR3 Read(IReadBuffer buffer) throws ReadException {
		assert buffer != null;
		this.r = (byte)(buffer.readFloat() * 255);
		this.g = (byte)(buffer.readFloat() * 255);
		this.b = (byte)(buffer.readFloat() * 255);
		return this;
	}
	
	public float toFloatBits() {
		byte[] data = new byte[4];
		data[0] = r;
		data[1] = g;
		data[2] = b;
		data[3] = (byte)255;
		float f = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getFloat();
		return f;
	}
	
	public Color toColor() {
		int rgba8888 = ((int)r << 24) | ((int)g << 16) | ((int)b << 8) | ((int)255);
		return new Color(rgba8888);
	}
}