package net.yzwlab.javammd.format;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class PMD_GRP_NAME_RECORD {
	public byte[] name;

	public PMD_GRP_NAME_RECORD() {
		this.name = new byte[50];
	}

	public PMD_GRP_NAME_RECORD read(IReadBuffer buffer) throws ReadException {
		return PMD_Reader.read(buffer, this);
	}
}
