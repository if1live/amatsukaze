package net.yzwlab.javammd.format;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class VMD_MORP_RECORD {
	public byte[] name;

	public int frame_no;

	public float factor;

	public VMD_MORP_RECORD() {
		this.factor = 0.0f;
		this.frame_no = 0;
		this.name = new byte[15];
	}

	public VMD_MORP_RECORD read(IReadBuffer buffer) throws ReadException {
		return VMD_Reader.read(buffer, this);
	}
}
