package net.yzwlab.javammd.format;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class PMD_GRP_RECORD {
	public short BoneNo;

	public byte grp;

	public PMD_GRP_RECORD() {
		this.BoneNo = 0;
		this.grp = 0;
	}

	public PMD_GRP_RECORD read(IReadBuffer buffer) throws ReadException {
		return PMD_Reader.read(buffer, this);
	}
}
