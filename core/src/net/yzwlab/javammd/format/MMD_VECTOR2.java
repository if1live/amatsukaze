package net.yzwlab.javammd.format;

import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;

public class MMD_VECTOR2 {
	public float x;

	public float y;

	public MMD_VECTOR2() {
		this.x = 0.0f;
		this.y = 0.0f;
	}

	public MMD_VECTOR2(MMD_VECTOR2 source) {
		assert source != null;
		this.x = source.x;
		this.y = source.y;
	}

	/**
	 * ベクトル情報のコピーを行います。
	 * 
	 * @param source
	 *            ソース。nullは不可。
	 */
	public void copyFrom(MMD_VECTOR2 source) {
		assert source != null;
		this.x = source.x;
		this.y = source.y;
	}

	public MMD_VECTOR2 read(IReadBuffer buffer) throws ReadException {
		return BasicReader.read(buffer, this);
	}
}

