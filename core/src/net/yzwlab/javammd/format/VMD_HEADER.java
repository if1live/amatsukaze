package net.yzwlab.javammd.format;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class VMD_HEADER {
	public byte[] hdr_string;

	public byte[] unknown;

	public VMD_HEADER() {
		this.unknown = new byte[0x04];
		this.hdr_string = new byte[0x1A];
		System.arraycopy(VMDFile.c_hdr_string, 0, hdr_string, 0,
				Math.min(hdr_string.length, VMDFile.c_hdr_string.length));
	}

	public VMD_HEADER read(IReadBuffer buffer) throws ReadException {
		return VMD_Reader.read(buffer, this);
	}

}
