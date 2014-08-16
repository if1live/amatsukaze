package net.yzwlab.javammd.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.yzwlab.javammd.format.MMD_MATRIX;
import net.yzwlab.javammd.format.MMD_VECTOR3;
import net.yzwlab.javammd.format.MMD_VECTOR4;
import net.yzwlab.javammd.format.PMD_IK_RECORD;

public class MMDIK {
	protected PMD_IK_RECORD m_ik;

	protected boolean m_bEnabled;

	protected MMDBone m_pTarget;

	protected MMDBone m_pEffect;

	protected float m_fact;

	protected List<MMDBone> m_bones;

	public static int compare(MMDIK pLeft, MMDIK pRight) {
		assert pLeft != null;
		assert pRight != null;
		
		int sortVal1 = 0;
		int sortVal2 = 0;
		sortVal1 = pLeft.GetSortValue();
		sortVal2 = pRight.GetSortValue();
		if (sortVal1 < sortVal2) {
			return -1;
		}
		if (sortVal1 > sortVal2) {
			return 1;
		}
		return 0;
	}

	/**
	 * 構築します。
	 * 
	 * @param ik
	 *            IK。nullは不可。
	 */
	public MMDIK(PMD_IK_RECORD ik) {
		assert ik != null;
		if (ik.link.size() == 0) {
			throw new IllegalArgumentException();
		}
		this.m_bones = new ArrayList<MMDBone>();
		this.m_fact = 0.0f;
		this.m_pEffect = null;
		this.m_pTarget = null;
		this.m_bEnabled = false;
		this.m_ik = ik;
		m_pTarget = null;
		m_pEffect = null;
		m_fact = 0.0f;
		m_bEnabled = true;
	}

	public void dispose() {
		m_pTarget = null;
		m_pEffect = null;
	}

	public void init(List<MMDBone> bones) {
		assert bones != null;
		MMDBone pBone = null;
		m_pTarget = bones.get(m_ik.parent);
		m_pEffect = bones.get(m_ik.to);
		m_fact = (float) (m_ik.fact * Math.PI);
		for (int i = 0; i < m_ik.link.size(); i++) {
			pBone = bones.get(m_ik.link.get(i));
			m_bones.add(pBone);
			pBone.updateIKLimitAngle();
		}
	}

	public byte[] getTargetName() {
		assert m_pTarget != null : "E_UNEXPECTED";
		return m_pTarget.getName();
	}

	public void setEnabled(boolean enabled) {
		m_bEnabled = enabled;
		return;
	}

	public boolean isEnabled() {
		Boolean pEnabled = false;
		pEnabled = m_bEnabled;
		return pEnabled;
	}

	/**
	 * IKを更新します。
	 */
	public void update() {
		assert m_pTarget != null : "E_UNEXPECTED";
		assert m_pEffect != null : "E_UNEXPECTED";
		if (m_bEnabled == false) {
			return;
		}
		for (int i = m_bones.size() - 1; i >= 0; i--) {
			m_bones.get(i).updateMatrix();
		}
		m_pEffect.updateMatrix();

		// オブジェクトはあらかじめ準備しておく(パフォーマンス対策)
		MMD_VECTOR3 diff = new MMD_VECTOR3();
		MMD_MATRIX matBoneBuf = new MMD_MATRIX();
		MMD_MATRIX matInvBoneBuf = new MMD_MATRIX();
		MMD_VECTOR3 effectPositionBuf = new MMD_VECTOR3();
		MMD_VECTOR3 targetPositionBuf = new MMD_VECTOR3();

		MMD_VECTOR3 targetOriginalPosition = m_pTarget
				.getPositionFromLocal(new MMD_VECTOR3());
		MMD_VECTOR4 rotQuat = new MMD_VECTOR4();
		MMD_VECTOR3 rotAxis = new MMD_VECTOR3();
		MMD_VECTOR3 rotQuatBuf = new MMD_VECTOR3();
		MMD_VECTOR3 effectOriginalPosition = new MMD_VECTOR3();
		for (int it = 0; it < m_ik.count; it++) {
			int index = 0;
			for (Iterator<MMDBone> bit = m_bones.iterator(); bit.hasNext(); index++) {
				MMDBone pBone = bit.next();
				MMD_VECTOR3 effectPosition = m_pEffect
						.getPositionFromLocal(effectPositionBuf);
				effectOriginalPosition.copyFrom(effectPosition);
				MMD_MATRIX matBone = pBone.getLocal(matBoneBuf);
				MMD_MATRIX matInvBone = matBone.inverse(matInvBoneBuf);
				effectPosition = effectPosition.transform(matInvBone);
				targetPositionBuf.copyFrom(targetOriginalPosition);
				MMD_VECTOR3 targetPosition = targetPositionBuf
						.transform(matInvBone);
				diff.subtract(effectPosition, targetPosition);
				float dp = diff.dotProduct(diff);
				if (dp < 0.0000001f) {
					return;
				}
				// (1)基準関節→エフェクタ位置への方向ベクトル
				effectPosition.normalize();
				// (2)基準関節→目標位置への方向ベクトル
				targetPosition.normalize();

				// ベクトル(1)を(2)に一致させるための最短回転量計算
				dp = effectPosition.dotProduct(targetPosition);
				float rotAngle = (float) Math.acos(dp);
				if (0.00000001f < Math.abs(rotAngle)) {
					// 回転が必要
					if (rotAngle < -m_fact) {
						rotAngle = -m_fact;
					} else if (m_fact < rotAngle) {
						rotAngle = m_fact;
					}
					rotAxis.crossProduct(effectPosition, targetPosition);
					dp = rotAxis.dotProduct(rotAxis);
					if (dp < 0.0000001f) {
						continue;
					}
					rotAxis.normalize();
					rotQuat.createAxis(rotAxis, rotAngle);
					// String effectName = new String(m_pEffect.getName());
					// if (m_pEffect.getParentName() != null) {
					// effectName += "(<"
					// + new String(m_pEffect.getParentName()) + ")";
					// }
					// String targetName = new String(m_pTarget.getName());
					// if (m_pTarget.getParentName() != null) {
					// targetName += "(<"
					// + new String(m_pTarget.getParentName()) + ")";
					// }
					// System.err.println("MMDIK(" + effectName + " -> "
					// + targetName + " / #" + index + "): effector="
					// + effectOriginalPosition + ", target="
					// + targetOriginalPosition + ", rotQuat=" + rotQuat
					// + ", rotAngle=" + rotAngle);

					boolean limitAngle = pBone.isIKLimitAngle();
					if (limitAngle) {
						rotQuat = rotQuat.limitAngle(rotQuatBuf);
					}
					rotQuat.normalize();
					MMDBone.PositionAndQT pqt = pBone.getVectors();
					MMD_VECTOR4 destRotation = pqt.getQt();
					destRotation.multiply(destRotation, rotQuat);
					destRotation.normalize();
					pBone.setVectors(pqt.getPosition(), destRotation);
					for (int j = index; j >= 0; j--) {
						m_bones.get(j).updateMatrix();
					}
					m_pEffect.updateMatrix();
				}
			}
		}
		return;
	}

	public int GetSortValue() {
		Integer pNum = 0;
		if (m_ik.link.size() == 0) {
			throw new IllegalArgumentException("E_UNEXPECTED");
		}
		pNum = new Integer((Short) (m_ik.link.get(0)).shortValue());
		return pNum;
	}

}
