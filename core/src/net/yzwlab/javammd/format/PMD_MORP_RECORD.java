package net.yzwlab.javammd.format;

import java.util.ArrayList;
import java.util.List;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class PMD_MORP_RECORD {
	public byte[] name;

	public int vnum;

	public byte grp;

	public List<PMD_MORP_VERTEX_RECORD> mv;

	public PMD_MORP_RECORD() {
		this.name = new byte[20];
		this.vnum = 0;
		this.grp = 0;
		this.mv = new ArrayList<PMD_MORP_VERTEX_RECORD>();
	}

	public PMD_MORP_RECORD(PMD_MORP_RECORD source) {
		this.name = new byte[20];
		this.vnum = 0;
		this.grp = 0;
		this.mv = new ArrayList<PMD_MORP_VERTEX_RECORD>();

		System.arraycopy(source.name, 0, this.name, 0, name.length);
		this.vnum = source.vnum;
		this.grp = source.grp;
		for (PMD_MORP_VERTEX_RECORD rec : source.mv) {
			this.mv.add(rec);
		}
	}

	public PMD_MORP_RECORD read(IReadBuffer buffer) throws ReadException {
		return PMD_Reader.read(buffer, this);
	}
}
