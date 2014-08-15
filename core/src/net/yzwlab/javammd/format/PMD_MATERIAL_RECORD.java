package net.yzwlab.javammd.format;

import java.io.Serializable;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class PMD_MATERIAL_RECORD implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MMD_COLOR4 diffuse;

	public float shininess;

	public MMD_COLOR3 specular;

	public MMD_COLOR3 ambient;

	protected short p12;

	protected int nEdges;

	protected byte[] textureFileName;

	public PMD_MATERIAL_RECORD() {
		this.diffuse = new MMD_COLOR4();
		this.shininess = 0.0f;
		this.specular = new MMD_COLOR3();
		this.ambient = new MMD_COLOR3();
		this.p12 = 0;
		this.nEdges = 0;
		this.textureFileName = new byte[20];
	}

	public short getP12() {
		return p12;
	}

	public void setP12(short p12) {
		this.p12 = p12;
	}

	public int getNEdges() {
		return nEdges;
	}

	public void setNEdges(int nEdges) {
		this.nEdges = nEdges;
	}

	public byte[] getTextureFileName() {
		return textureFileName;
	}

	public void setTextureFileName(byte[] textureFileName) {
		this.textureFileName = textureFileName;
	}

	public PMD_MATERIAL_RECORD Read(IReadBuffer buffer) throws ReadException {
		assert buffer != null;
		this.diffuse = (new MMD_COLOR4()).Read(buffer);
		this.shininess = buffer.readFloat();
		this.specular = (new MMD_COLOR3()).Read(buffer);
		this.ambient = (new MMD_COLOR3()).Read(buffer);
		this.p12 = buffer.readShort();
		this.nEdges = buffer.readInteger();
		this.textureFileName = buffer.readByteArray(20);
		return this;
	}
}
