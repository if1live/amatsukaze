package net.yzwlab.javammd.format;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class PMD_CONSTRAINT_RECORD {
	public byte[] szName;

	public long ulRigidA;

	public long ulRigidB;

	public float[] pos;

	public float[] rotation;

	public float[] posLimitL;

	public float[] posLimitU;

	public float[] rotLimitL;

	public float[] rotLimitU;

	public float[] springPos;

	public float[] springRot;

	public PMD_CONSTRAINT_RECORD() {
		this.szName = new byte[20];
		this.ulRigidA = 0L;
		this.ulRigidB = 0L;
		this.pos = new float[3];
		this.rotation = new float[3];
		this.posLimitL = new float[3];
		this.posLimitU = new float[3];
		this.rotLimitL = new float[3];
		this.rotLimitU = new float[3];
		this.springPos = new float[3];
		this.springRot = new float[3];
	}

	public PMD_CONSTRAINT_RECORD read(IReadBuffer buffer) throws ReadException {
		return PMD_Reader.read(buffer, this);
	}
}
