package net.yzwlab.javammd.format;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class PMD_INDEX_RECORD {
	public short id;

	public PMD_INDEX_RECORD() {
		this.id = 0;
	}
	
	public PMD_INDEX_RECORD read(IReadBuffer buffer) throws ReadException {
		return PMD_Reader.read(buffer, this);
	}
}
