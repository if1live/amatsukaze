package net.yzwlab.javammd.format;

import java.io.Serializable;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class MMD_COLOR4 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public float r;

	public float g;

	public float b;

	public float a;

	public MMD_COLOR4() {
		this.r = 0.0f;
		this.g = 0.0f;
		this.b = 0.0f;
		this.a = 0.0f;
	}

	public MMD_COLOR4 Read(IReadBuffer buffer) throws ReadException {
		if (buffer == null) {
			throw new IllegalArgumentException();
		}
		this.r = buffer.readFloat();
		this.g = buffer.readFloat();
		this.b = buffer.readFloat();
		this.a = buffer.readFloat();
		return this;
	}
}