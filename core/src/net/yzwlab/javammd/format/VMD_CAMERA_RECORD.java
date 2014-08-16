package net.yzwlab.javammd.format;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class VMD_CAMERA_RECORD {
	public int frame_no;

	public float[] pos;

	public float[] angle;

	public byte[] pad;

	public VMD_CAMERA_RECORD() {
		this.pad = new byte[29];
		this.angle = new float[3];
		this.pos = new float[4];
		this.frame_no = 0;
		System.arraycopy(VMDFile.c_hokan_data2, 0, pad, 0, pad.length);
	}

	public VMD_CAMERA_RECORD read(IReadBuffer buffer) throws ReadException {
		return VMD_Reader.read(buffer, this);
	}
}
