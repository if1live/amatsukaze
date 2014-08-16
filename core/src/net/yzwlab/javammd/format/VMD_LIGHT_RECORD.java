package net.yzwlab.javammd.format;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class VMD_LIGHT_RECORD {
	public int frame_no;

	public float[] pos;

	public float[] direction;

	public VMD_LIGHT_RECORD() {
		this.direction = new float[3];
		this.pos = new float[3];
		this.frame_no = 0;
	}

	public VMD_LIGHT_RECORD read(IReadBuffer buffer) throws ReadException {
		return VMD_Reader.read(buffer, this);
	}
}
