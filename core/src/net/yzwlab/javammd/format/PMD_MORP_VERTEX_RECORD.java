package net.yzwlab.javammd.format;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class PMD_MORP_VERTEX_RECORD {
	public int no;

	public float[] vec;

	public PMD_MORP_VERTEX_RECORD() {
		this.no = 0;
		this.vec = new float[3];
	}

	public PMD_MORP_VERTEX_RECORD read(IReadBuffer buffer) throws ReadException {
		return PMD_Reader.read(buffer, this);
	}
}
