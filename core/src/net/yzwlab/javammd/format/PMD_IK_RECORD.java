package net.yzwlab.javammd.format;

import java.util.ArrayList;
import java.util.List;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class PMD_IK_RECORD {
	public short parent;

	public short to;

	public byte NumLink;

	public short count;

	public float fact;

	public List<Short> link;

	public PMD_IK_RECORD() {
		this.parent = 0;
		this.to = 0;
		this.NumLink = 0;
		this.count = 0;
		this.fact = 0.0f;
		this.link = new ArrayList<Short>();
	}

	public PMD_IK_RECORD read(IReadBuffer buffer) throws ReadException {
		return PMD_Reader.read(buffer, this);
	}
}
