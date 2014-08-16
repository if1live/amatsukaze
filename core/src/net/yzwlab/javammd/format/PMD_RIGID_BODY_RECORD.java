package net.yzwlab.javammd.format;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class PMD_RIGID_BODY_RECORD {
	public byte[] szName;

	public short unBoneIndex;

	public byte cbColGroupIndex;

	public short unColGroupMask;

	public byte cbShapeType;

	public float fWidth;

	public float fHeight;

	public float fDepth;

	public float[] pos;

	public float[] rotation;

	public float fMass;

	public float fLinearDamping;

	public float fAngularDamping;

	public float fRestitution;

	public float fFriction;

	public byte cbRigidBodyType;

	public PMD_RIGID_BODY_RECORD() {
		this.szName = new byte[20];
		this.unBoneIndex = 0;
		this.cbColGroupIndex = 0;
		this.unColGroupMask = 0;
		this.cbShapeType = 0;
		this.fWidth = 0.0f;
		this.fHeight = 0.0f;
		this.fDepth = 0.0f;
		this.pos = new float[3];
		this.rotation = new float[3];
		this.fMass = 0.0f;
		this.fLinearDamping = 0.0f;
		this.fAngularDamping = 0.0f;
		this.fRestitution = 0.0f;
		this.fFriction = 0.0f;
		this.cbRigidBodyType = 0;
	}

	public PMD_RIGID_BODY_RECORD read(IReadBuffer buffer) throws ReadException {
		return PMD_Reader.read(buffer, this);
	}
}
