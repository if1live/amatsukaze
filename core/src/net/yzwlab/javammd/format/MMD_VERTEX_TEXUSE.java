package net.yzwlab.javammd.format;

import net.yzwlab.javammd.IGL;
import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class MMD_VERTEX_TEXUSE {
	public final MMD_VECTOR3 point;

	public final MMD_VECTOR3 normal;

	public final MMD_VECTOR2 uv;

	public MMD_VERTEX_TEXUSE() {
		this.point = new MMD_VECTOR3();
		this.normal = new MMD_VECTOR3();
		this.uv = new MMD_VECTOR2();
	}

	public void copyFrom(MMD_VERTEX_TEXUSE source) {
		this.point.copyFrom(source.point);
		this.normal.copyFrom(source.normal);
		this.uv.copyFrom(source.uv);
	}

	public void setPoint(MMD_VECTOR3 point) {
		this.point.copyFrom(point);
	}

	public void setNormal(MMD_VECTOR3 normal) {
		this.normal.copyFrom(normal);
	}

	public MMD_VERTEX_TEXUSE read(IReadBuffer buffer) throws ReadException {
		return BasicReader.read(buffer, this);
	}

	/**
	 * GLに対して頂点などを転送します。
	 * 
	 * @param gl
	 *            転送対象。nullは不可。
	 */
	public void toGL(IGL gl) {
		assert gl != null;
		gl.glTexCoord2f(uv.x, uv.y);
		gl.glVertex3f(point.x, point.y, point.z);
		gl.glNormal3f(normal.x, normal.y, normal.z);
	}
}
