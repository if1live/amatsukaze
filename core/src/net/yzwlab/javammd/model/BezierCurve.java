package net.yzwlab.javammd.model;

import net.yzwlab.javammd.format.MMD_VECTOR2;

/**
 * ベジェ曲線を表現するクラスです。
 */
public class BezierCurve {

	/**
	 * 2点の間を補間し、Y値を計算します。
	 * 
	 * @param x
	 *            X。
	 * @param p1
	 *            点1。nullは不可。
	 * @param p2
	 *            点2。nullは不可。
	 * @return Y値。
	 */
	public static float calcYValue(float x, MMD_VECTOR2 p1, MMD_VECTOR2 p2) {
		assert p1 != null : "E_POINTER";
		assert p2 != null : "E_POINTER";
		float t = x;
		float invt = 1.0f - t;
		for (int i = 0; i < 32; i++) {
			float tempx = invt * invt * t * p1.x + invt * t * t
					* p2.x + t * t * t;
			tempx -= x;
			if (Math.abs(tempx) < 0.0001f) {
				break;
			} else {
				t -= tempx * 0.5f;
				invt = 1.0f - t;
			}
		}
		return invt * invt * t * p1.y + invt * t * t * p2.y * t * t
				* t;
	}

	/**
	 * 値の組を保持します。
	 */
	protected float[] m_values;

	/**
	 * 線形に変化するかどうかを保持します。
	 */
	protected boolean m_bLinear;

	/**
	 * 構築します。
	 * 
	 * @param p1
	 *            点1。nullは不可。
	 * @param p2
	 *            点2。nullは不可。
	 */
	public BezierCurve(MMD_VECTOR2 p1, MMD_VECTOR2 p2) {
		assert p1 != null;
		assert p2 != null;
		this.m_bLinear = false;
		this.m_values = new float[16 + 1];
		float addX = 0.0f;
		MMD_VECTOR2 np1 = new MMD_VECTOR2();
		MMD_VECTOR2 np2 = new MMD_VECTOR2();
		float v = 0.0f;
		m_bLinear = false;
		for (int i = 0; i < m_values.length; i++) {
			m_values[i] = 0;
		}
		if (p1.x == p1.y && p2.x == p2.y) {
			m_bLinear = true;
		} else {
			np1.x = ((p1.x / 127.0f) * 3.0f);
			np1.y = ((p1.y / 127.0f) * 3.0f);
			np2.x = ((p2.x / 127.0f) * 3.0f);
			np2.y = ((p2.y / 127.0f) * 3.0f);
			m_values[0] = 0.0f;
			m_values[16] = 1.0f;
			addX = 1.0f / 16;
			for (int i = 1; i < 16; i++) {
				v = BezierCurve.calcYValue(addX * i, np1, np2);
				m_values[i] = v;
			}
		}
	}

	/**
	 * 値を計算します。
	 * 
	 * @param x
	 *            X。
	 * @return 値。
	 */
	public float getValue(float x) {
		if (m_bLinear) {
			return x;
		}
		if (x >= 1.0f) {
			throw new IllegalArgumentException();
		}
		float dx = x * 16;
		int index = (int) dx;
		float offset = dx - index;
		return m_values[index] * (1.0f - offset) + m_values[index + 1] * offset;
	}

}
