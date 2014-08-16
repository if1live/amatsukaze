package net.yzwlab.javammd.format;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class PMD_BONE_RECORD {
	public byte[] name;

	public short parent;

	public short to;

	public byte kind;

	public short knum;

	public float[] pos;

	public PMD_BONE_RECORD() {
		this.name = new byte[20];
		this.parent = 0;
		this.to = 0;
		this.kind = 0;
		this.knum = 0;
		this.pos = new float[3];
	}

	public PMD_BONE_RECORD read(IReadBuffer buffer) throws ReadException {
		return PMD_Reader.read(buffer, this);
	}
}
