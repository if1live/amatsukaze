package net.yzwlab.javammd.format;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class PMD_MATERIAL_RECORD {
	public MMD_COLOR4 diffuse;

	public float shininess;

	public MMD_COLOR3 specular;

	public MMD_COLOR3 ambient;

	public short p12;

	public int nEdges;

	public byte[] textureFileName;

	public PMD_MATERIAL_RECORD() {
		this.diffuse = new MMD_COLOR4();
		this.shininess = 0.0f;
		this.specular = new MMD_COLOR3();
		this.ambient = new MMD_COLOR3();
		this.p12 = 0;
		this.nEdges = 0;
		this.textureFileName = new byte[20];
	}

	public PMD_MATERIAL_RECORD read(IReadBuffer buffer) throws ReadException {
		return PMD_Reader.read(buffer, this);
	}
}
