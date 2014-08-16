package net.yzwlab.javammd.format;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class VMD_MOTION_RECORD {
	public byte[] name;

	public int frame_no;

	public float[] pos;

	public float[] qt;

	public byte[] pad;

	public VMD_MOTION_RECORD() {
		this.pad = new byte[0x40];
		this.qt = new float[4];
		this.pos = new float[3];
		this.frame_no = 0;
		this.name = new byte[15];
		System.arraycopy(VMDFile.c_hokan_data, 0, pad, 0, pad.length);
	}

	public VMD_MOTION_RECORD read(IReadBuffer buffer) throws ReadException {
		return VMD_Reader.read(buffer, this);
	}
}
