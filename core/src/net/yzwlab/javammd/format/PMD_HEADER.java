package net.yzwlab.javammd.format;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class PMD_HEADER {
	public static final int HEADER_1_SIZE = 0x1b;
	public static final int HEADER_2_SIZE = 0x100; 

	public byte[] header1;

	public byte[] header2;

	public PMD_HEADER() {
		this.header1 = new byte[HEADER_1_SIZE];
		this.header2 = new byte[HEADER_2_SIZE];
	}

	public PMD_HEADER read(IReadBuffer buffer) throws ReadException {
		return PMD_Reader.read(buffer, this);
	}
}
