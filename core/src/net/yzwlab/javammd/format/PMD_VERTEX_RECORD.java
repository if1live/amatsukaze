package net.yzwlab.javammd.format;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class PMD_VERTEX_RECORD {
	public float x;

	public float y;

	public float z;

	public float nx;

	public float ny;

	public float nz;

	public float tx;

	public float ty;

	public short b1;

	public short b2;

	public byte bw;

	public byte unknown;

	public PMD_VERTEX_RECORD() {
		this.x = 0.0f;
		this.y = 0.0f;
		this.z = 0.0f;
		this.nx = 0.0f;
		this.ny = 0.0f;
		this.nz = 0.0f;
		this.tx = 0.0f;
		this.ty = 0.0f;
		this.b1 = 0;
		this.b2 = 0;
		this.bw = 0;
		this.unknown = 0;
	}

	public PMD_VERTEX_RECORD read(IReadBuffer buffer) throws ReadException {
		return PMD_Reader.read(buffer, this);
}
}
