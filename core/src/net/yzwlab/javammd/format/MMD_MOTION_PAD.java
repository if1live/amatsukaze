package net.yzwlab.javammd.format;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class MMD_MOTION_PAD {
	public byte[] cInterpolationX;

	public byte[] cInterpolationY;

	public byte[] cInterpolationZ;

	public byte[] cInterpolationRot;

	public MMD_MOTION_PAD() {
		this.cInterpolationX = new byte[16];
		this.cInterpolationY = new byte[16];
		this.cInterpolationZ = new byte[16];
		this.cInterpolationRot = new byte[16];
	}

	public MMD_MOTION_PAD read(IReadBuffer buffer) throws ReadException {
		return BasicReader.read(buffer, this);
	}
}
