package net.yzwlab.javammd.io;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

import com.badlogic.gdx.files.FileHandle;

public class GdxByteBuffer extends StreamBuffer {
	public GdxByteBuffer(FileHandle handle) {
		super(new DataInputStream(
				new ByteArrayInputStream(handle.readBytes())), handle.length());
	}
	
	@Override
	public IReadBuffer createFromByteArray(byte[] data) throws ReadException {
		assert data != null;
		return new ByteBuffer(data);
	}
}
