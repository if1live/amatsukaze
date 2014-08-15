package net.yzwlab.javammd.io;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class ByteBuffer extends StreamBuffer {

	public ByteBuffer(byte[] data) {
		super(new DataInputStream(
				new ByteArrayInputStream(data)), data.length);
	}

	@Override
	public IReadBuffer createFromByteArray(byte[] data) throws ReadException {
		assert data != null;
		return new ByteBuffer(data);
	}

}
