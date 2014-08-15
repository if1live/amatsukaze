package net.yzwlab.javammd.io;

import java.io.DataInputStream;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

import com.badlogic.gdx.files.FileHandle;

public class GdxFileBuffer extends StreamBuffer {
	static final int BUFFER_SIZE = 32 * 1024;
	
	public GdxFileBuffer(FileHandle handle) {
		super(new DataInputStream(handle.read(BUFFER_SIZE)), 
				handle.length());
	}
	
	@Override
	public IReadBuffer createFromByteArray(byte[] data) throws ReadException {
		if (data == null) {
			throw new IllegalArgumentException();
		}
		return new ByteBuffer(data);
	}
}
