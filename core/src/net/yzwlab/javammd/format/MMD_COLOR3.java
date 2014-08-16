package net.yzwlab.javammd.format;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.badlogic.gdx.graphics.Color;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class MMD_COLOR3 {
	public byte r;

	public byte g;

	public byte b;

	public MMD_COLOR3() {
		this((byte)0, (byte)0, (byte)0);
	}
	
	public MMD_COLOR3(byte r, byte g, byte b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public MMD_COLOR3 read(IReadBuffer buffer) throws ReadException {
		return BasicReader.read(buffer, this);
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
		int bitR = (int)r << 24 & (int)255 << 24;
		int bitG = (int)g << 16 & (int)255 << 16;
		int bitB = (int)b << 8 & (int)255 << 8;
		int bitA = (int)255;
		int rgba8888 = bitR | bitG | bitB | bitA;
		return new Color(rgba8888);
	}
}