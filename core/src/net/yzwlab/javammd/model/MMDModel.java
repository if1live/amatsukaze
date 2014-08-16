package net.yzwlab.javammd.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.yzwlab.javammd.IGL;
import net.yzwlab.javammd.IGLObject;
import net.yzwlab.javammd.IGLTextureProvider;
import net.yzwlab.javammd.IReadBuffer;
import net.yzwlab.javammd.ReadException;
import net.yzwlab.javammd.format.MMD_VERTEX_DESC;
import net.yzwlab.javammd.format.PMDFile;
import net.yzwlab.javammd.format.PMD_BONE_RECORD;
import net.yzwlab.javammd.format.PMD_IK_RECORD;
import net.yzwlab.javammd.format.PMD_MATERIAL_RECORD;
import net.yzwlab.javammd.format.PMD_MORP_RECORD;
import net.yzwlab.javammd.format.PMD_VERTEX_RECORD;
import net.yzwlab.javammd.format.VMDFile;
import net.yzwlab.javammd.format.VMD_MORP_RECORD;
import net.yzwlab.javammd.format.VMD_MOTION_RECORD;

/**
 * MMDのモデルを表現します。
 * MMD 모델을 표현합니다.
 */
public class MMDModel implements IGLObject {

	protected float m_scale;

	protected MMDVertexList m_pVertexList;

	protected List<MMDBone> m_bones;

	protected List<MMDMaterial> m_materials;

	protected List<MMDMorp> m_morps;

	protected List<MMDIK> m_iks;

	/**
	 * 構築します。
	 */
	public MMDModel() {
		this.m_iks = new ArrayList<MMDIK>();
		this.m_morps = new ArrayList<MMDMorp>();
		this.m_materials = new ArrayList<MMDMaterial>();
		this.m_bones = new ArrayList<MMDBone>();
		this.m_pVertexList = null;
		this.m_scale = 0.0f;
		m_pVertexList = null;
		m_scale = 1.0f;
	}

	/**
	 * PMDファイルを読み出します。
	 * 
	 * @param buffer
	 *            バッファ。nullは不可。
	 * @param pmdFile
	 *            PMDファイル。nullは不可。
	 * @throws ReadException
	 *             読み込み関係のエラー。
	 */
	public void setPMD(IReadBuffer buffer, PMDFile pmdFile)
			throws ReadException {
		assert buffer != null;
		assert pmdFile != null;
		
		PMD_MORP_RECORD baseMorp = new PMD_MORP_RECORD();
		PMD_BONE_RECORD bone = new PMD_BONE_RECORD();
		List<PMD_BONE_RECORD> bones = new ArrayList<PMD_BONE_RECORD>();
		PMD_IK_RECORD ik = new PMD_IK_RECORD();
		List<PMD_IK_RECORD> iks = new ArrayList<PMD_IK_RECORD>();
		List<Short> indices = new ArrayList<Short>();
		PMD_MATERIAL_RECORD material = new PMD_MATERIAL_RECORD();
		List<PMD_MATERIAL_RECORD> materials = new ArrayList<PMD_MATERIAL_RECORD>();
		PMD_MORP_RECORD morp = new PMD_MORP_RECORD();
		List<PMD_MORP_RECORD> morps = new ArrayList<PMD_MORP_RECORD>();
		int offset = 0;
		List<PMD_VERTEX_RECORD> vertexes = new ArrayList<PMD_VERTEX_RECORD>();
		if (m_pVertexList != null) {
			throw new IllegalArgumentException("E_UNEXPECTED");
		}
		vertexes = pmdFile.GetVertexChunk();
		indices = pmdFile.GetIndexChunk();
		m_pVertexList = new MMDVertexList(vertexes, indices);
		bones = pmdFile.GetBoneChunk();
		for (int i = 0; i < bones.size(); i++) {
			bone = bones.get(i);
			m_bones.add(new MMDBone(bone));
		}
		for (int i = 0; i < m_bones.size(); i++) {
			m_bones.get(i).init(m_bones);
		}
		morps = pmdFile.GetMorpChunk();
		for (int i = 0; i < morps.size(); i++) {
			morp = morps.get(i);
			if (i == 0) {
				m_morps.add(new MMDMorp(morp, null));
			} else {
				baseMorp = morps.get(0);
				m_morps.add(new MMDMorp(morp, baseMorp));
			}
		}
		iks = pmdFile.GetIKChunk();
		for (int i = 0; i < iks.size(); i++) {
			ik = iks.get(i);
			m_iks.add(new MMDIK(ik));
		}
		Collections.sort(m_iks, new Comparator<MMDIK>() {
			@Override
			public int compare(MMDIK o1, MMDIK o2) {
				return MMDIK.compare(o1, o2);
			}
		});
		for (int i = 0; i < m_iks.size(); i++) {
			m_iks.get(i).init(m_bones);
		}
		materials = pmdFile.GetMaterialChunk();
		for (int i = 0; i < materials.size(); i++) {
			material = materials.get(i);
			m_materials.add(new MMDMaterial(material));
		}
		offset = 0;
		for (int i = 0; i < m_materials.size(); i++) {
			offset = m_materials.get(i).init(m_pVertexList, m_bones, offset);
		}
		m_pVertexList.verify();
	}

	/**
	 * PMDファイルを読み出します。
	 * 
	 * @param buffer
	 *            バッファ。nullは不可。
	 * @throws ReadException
	 *             読み込み関係のエラー。
	 */
	public void openPMD(IReadBuffer buffer) throws ReadException {
		assert buffer != null;
		PMDFile pmdFile = new PMDFile();
		boolean br = pmdFile.open(buffer);
		if (br == false) {
			throw new IllegalArgumentException();
		}
		setPMD(buffer, pmdFile);
	}

	/**
	 * VMDファイルを読み出します。
	 * 
	 * @param buffer
	 *            バッファ。nullは不可。
	 * @return モーションが登録された時間区分。
	 * @throws ReadException
	 *             読み込み関係のエラー。
	 */
	public IMotionSegment setVMD(IReadBuffer buffer, VMDFile vmdFile)
			throws ReadException {
		assert buffer != null;
		assert vmdFile != null;
		boolean added = false;
		boolean br = false;
		VMD_MORP_RECORD morp = null;
		List<VMD_MORP_RECORD> morps = new ArrayList<VMD_MORP_RECORD>();
		VMD_MOTION_RECORD motion = null;
		List<VMD_MOTION_RECORD> motions = new ArrayList<VMD_MOTION_RECORD>();
		MMDBone pBone = null;
		MMDMorp pMorp = null;

		// モーションを追加するオフセット値
		// 모션을 추가하는 오프셋 값
		int offset = 0;
		Integer maxFrameNum = getMaxFrame();
		if (maxFrameNum != null) {
			offset = maxFrameNum.intValue() + 1;
		}

		motions = vmdFile.GetMotionChunk();
		for (int j = 0; j < motions.size(); j++) {
			motion = motions.get(j);
			added = false;
			for (int i = 0; i < m_bones.size(); i++) {
				pBone = m_bones.get(i);
				br = pBone.isTarget(motion);
				if (br) {
					pBone.addMotion(buffer, offset, motion);
					added = true;
					break;
				}
			}
			if (added == false) {
			}
		}
		for (int i = 0; i < m_bones.size(); i++) {
			pBone = m_bones.get(i);
			pBone.prepareMotion();
		}
		morps = vmdFile.GetMorpChunk();
		for (int j = 0; j < morps.size(); j++) {
			morp = morps.get(j);
			added = false;
			for (int i = 0; i < m_morps.size(); i++) {
				pMorp = m_morps.get(i);
				br = pMorp.IsTarget(morp);
				if (br) {
					pMorp.addMotion(offset, morp);
					added = true;
					break;
				}
			}
			if (added == false) {
			}
		}
		for (int i = 0; i < m_morps.size(); i++) {
			pMorp = m_morps.get(i);
			pMorp.PrepareMotion();
		}
		return new MotionSegment(offset);
	}

	/**
	 * VMDファイルを読み出します。
	 * 
	 * @param buffer
	 *            バッファ。nullは不可。
	 * @return モーションが登録された時間区分。
	 * @throws ReadException
	 *             読み込み関係のエラー。
	 */
	public IMotionSegment openVMD(IReadBuffer buffer) throws ReadException {
		assert buffer != null;
		VMDFile vmdFile = new VMDFile();
		boolean br = vmdFile.open(buffer);
		if (br == false) {
			throw new IllegalArgumentException("E_UNEXPECTED");
		}
		return setVMD(buffer, vmdFile);
	}

	@Override
	public void prepare(IGLTextureProvider pTextureProvider,
			IGLTextureProvider.Handler handler) throws ReadException {
		assert pTextureProvider != null;
		assert handler != null;
		for (int i = 0; i < m_materials.size(); i++) {
			m_materials.get(i).prepare(pTextureProvider, handler);
		}
	}

	/**
	 * 拡大率を設定します。
	 * 
	 * @param scale
	 *            拡大率。
	 */
	public void setScale(float scale) {
		m_scale = scale;
		return;
	}

	/**
	 * 拡大率を取得します。
	 * 
	 * @return 拡大率。
	 */
	public float getScale() {
		return m_scale;
	}

	/**
	 * モーション情報を消去します。
	 * 모션 정보를 삭제합니다.
	 */
	public void clearMotion() {
		for (int i = 0; i < m_bones.size(); i++) {
			m_bones.get(i).clearMotion();
		}
		for (int i = 0; i < m_morps.size(); i++) {
			m_morps.get(i).ClearMotion();
		}
		return;
	}

	public void update(float frameNo) {
		assert m_pVertexList != null : "E_POINTER";
		updateMotion(frameNo);
		for (int i = 0; i < m_bones.size(); i++) {
			m_bones.get(i).updateSkinning();
		}
		m_pVertexList.updateSkinning();
	}

	@Override
	public void draw(IGL gl) {
		assert gl != null;
		updateVertexBuffer();
		gl.glPushMatrix();
		gl.glScalef(m_scale, m_scale, m_scale * -1.0f);
		boolean normalizeEnabled = gl.glIsEnabled(IGL.C.GL_NORMALIZE);
		gl.glEnable(IGL.C.GL_NORMALIZE);
		for (int i = 0; i < m_materials.size(); i++) {
			m_materials.get(i).draw(gl);
		}
		if (normalizeEnabled == false) {
			gl.glDisable(IGL.C.GL_NORMALIZE);
		}
		gl.glPopMatrix();
	}

	/**
	 * Faceを設定します。
	 * 
	 * @param faceName
	 *            Face名。nullは不可。
	 */
	public void setFace(byte[] faceName) {
		assert faceName != null : "E_POINTER";
		byte[] elemName = null;
		byte[] name = null;
		MMDMorp pElem = null;
		MMDMorp pSelectedElem = null;
		MMD_VERTEX_DESC[] ppOriginalDescs = null;
		name = faceName;
		pSelectedElem = null;
		for (int i = 0; i < m_morps.size(); i++) {
			pElem = m_morps.get(i);
			elemName = pElem.GetName();
			if (Arrays.equals(elemName, name)) {
				pSelectedElem = pElem;
				break;
			}
		}
		if (pSelectedElem == null) {
			throw new IllegalArgumentException("E_UNEXPECTED");
		}
		ppOriginalDescs = m_pVertexList.GetVertexDescs();
		pSelectedElem.Set(ppOriginalDescs);
	}

	/**
	 * Face数を取得します。
	 * 
	 * @return Face数。
	 */
	public int getFaceCount() {
		return m_morps.size();
	}

	/**
	 * Face名を取得します。
	 * 
	 * @param index
	 *            インデックス。
	 * @return Face名。
	 */
	public byte[] getFaceName(int index) {
		if (index >= m_morps.size()) {
			throw new IllegalArgumentException("E_INVALIDARG");
		}
		return m_morps.get(index).GetName();
	}

	/**
	 * ボーン数を取得します。
	 * 
	 * @return ボーン。
	 */
	public int getBoneCount() {
		Integer pCount = 0;
		pCount = m_bones.size();
		return pCount;
	}

	/**
	 * ボーンを取得します。
	 * 
	 * @param index
	 *            インデックス。
	 * @return ボーン。
	 */
	public IMMDBone getBone(int index) {
		if (index >= m_bones.size()) {
			throw new IllegalArgumentException("E_INVALIDARG");
		}
		return new BoneAccessor(m_bones.get(index));
	}

	/**
	 * マテリアル数を取得します。
	 * 
	 * @return マテリアル数。
	 */
	public int getMaterialCount() {
		return m_materials.size();
	}

	/**
	 * マテリアルを取得します。
	 * 
	 * @param index
	 *            インデックス。
	 * @return マテリアル。
	 */
	public IMMDMaterial getMaterial(int index) {
		if (index >= m_materials.size()) {
			throw new IllegalArgumentException();
		}
		return new MaterialAccessor(m_materials.get(index));
	}

	public MMDMaterial getRawMaterial(int index) {
		if (index >= m_materials.size()) {
			throw new IllegalArgumentException();
		}
		return m_materials.get(index);
	}

	/**
	 * IK数を取得します。
	 * 
	 * @return IK数。
	 */
	public int getIKCount() {
		return m_iks.size();
	}

	/**
	 * IK名を取得します。
	 * 
	 * @param index
	 *            インデックス。
	 * @return IK名。
	 */
	public byte[] getIKTargetName(int index) {
		if (index >= m_iks.size()) {
			throw new IllegalArgumentException("E_INVALIDARG");
		}
		return m_iks.get(index).getTargetName();
	}

	/**
	 * IKが有効かどうかを判定します。
	 * IK가 유효한지 여부를 판정합니다.
	 * 
	 * @param index
	 *            インデックス。
	 * @return IKが有効であればtrue。
	 */
	public boolean isIKEnabled(int index) {
		if (index >= m_iks.size()) {
			throw new IllegalArgumentException("E_INVALIDARG");
		}
		return m_iks.get(index).isEnabled();
	}

	/**
	 * IKが有効かどうかを設定します。
	 * IK를 사용할 수 있는지 여부를 설정합니다.
	 * 
	 * @param index
	 *            インデックス。
	 * @param value
	 *            IKが有効であればtrue。
	 */
	public void setIKEnabled(int index, boolean value) {
		if (index >= m_iks.size()) {
			throw new IllegalArgumentException("E_INVALIDARG");
		}
		m_iks.get(index).setEnabled(value);
	}

	/**
	 * ボーンを表示するかどうかを設定します。
	 * 뼈를 표시할지 여부를 설정합니다.
	 * 
	 * @param index
	 *            インデックス。
	 * @param visible
	 *            ボーンを表示する場合はtrue。
	 */
	public void setBoneVisible(int index, boolean visible) {
		boolean curVisible = false;
		MMDBone pBone = null;
		MMDMaterial pElem = null;
		if (index >= m_bones.size()) {
			throw new IllegalArgumentException("E_INVALIDARG");
		}
		pBone = m_bones.get(index);
		curVisible = pBone.isVisible();
		if (curVisible == visible) {
			return;
		}
		pBone.setVisible(visible);
		for (int i = 0; i < m_materials.size(); i++) {
			pElem = m_materials.get(i);
			pElem.UpdateVisibility();
		}
	}

	/**
	 * ボーンを表示するかどうかを判定します。
	 * 뼈를 표시할지 여부를 판정합니다
	 * 
	 * @param index
	 *            インデックス。
	 * @return ボーンを表示する場合はtrue。
	 */
	public boolean isBoneVisible(int index) {
		if (index >= m_bones.size()) {
			throw new IllegalArgumentException("E_INVALIDARG");
		}
		return m_bones.get(index).isVisible();
	}

	/**
	 * 最大フレーム数を取得します。
	 * 최대 프레임 수를 가져옵니다.
	 * 
	 * @return 最大フレーム数。
	 */
	public Integer getMaxFrame() {
		int ret = 0;
		int validCount = 0;
		ret = 0;
		validCount = 0;
		for (int i = 0; i < m_morps.size(); i++) {
			Integer f = m_morps.get(i).GetMaxFrame();
			if (f == null) {
				continue;
			}
			validCount++;
			if (f > ret) {
				ret = f;
			}
		}
		for (int i = 0; i < m_bones.size(); i++) {
			Integer f = m_bones.get(i).getMaxFrame();
			if (f == null) {
				continue;
			}
			validCount++;
			if (f > ret) {
				ret = f;
			}
		}
		if (validCount == 0) {
			return null;
		}
		return ret;
	}

	/**
	 * 頂点情報をリセットします。
	 * 정점 정보를 재설정합니다.
	 */
	public void resetVertexes() {
		assert m_pVertexList != null : "E_POINTER";
		m_pVertexList.ResetVertexes();
	}

	/**
	 * スキニング情報を更新します。
	 * 스키닝 정보를 업데이트합니다.
	 */
	public void updateSkinning() {
		assert m_pVertexList != null : "E_POINTER";
		for (int i = 0; i < m_bones.size(); i++) {
			m_bones.get(i).updateSkinning();
		}
		m_pVertexList.updateSkinning();
	}

	/**
	 * 頂点バッファを更新します。
	 * 버텍스 버퍼를 업데이트합니다.
	 */
	public void updateVertexBuffer() {
		for (int i = 0; i < m_materials.size(); i++) {
			m_materials.get(i).updateVertexBuffer();
		}
	}

	/**
	 * モーションを更新します。
	 * 모션을 업데이트합니다.
	 * 
	 * @param elapsedFrame
	 *            経過フレーム数。
	 * @return 更新に成功した場合はtrue。
	 */
	public boolean updateMotion(float elapsedFrame) {
		if (m_pVertexList == null) {
			return false;
		}
		MMD_VERTEX_DESC[] ppOriginalDescs = m_pVertexList.GetVertexDescs();
		if (m_morps.size() > 0) {
			m_morps.get(0).Set(ppOriginalDescs);
		}
		for (int i = 0; i < m_morps.size(); i++) {
			m_morps.get(i).ApplyMotion(elapsedFrame, ppOriginalDescs);
		}
		for (int i = 0; i < m_bones.size(); i++) {
			m_bones.get(i).updateMotion(elapsedFrame);
		}
		for (int i = 0; i < m_bones.size(); i++) {
			m_bones.get(i).updateMatrix();
		}
		for (int i = 0; i < m_iks.size(); i++) {
			m_iks.get(i).update();
		}
		return true;
	}

	/**
	 * ボーンのアクセサを定義します。
	 */
	private class BoneAccessor implements IMMDBone {

		/**
		 * ボーンを保持します。
		 */
		private MMDBone bone;

		/**
		 * 構築します。
		 * 
		 * @param bone
		 *            ボーン。nullは不可。
		 */
		public BoneAccessor(MMDBone bone) {
			assert bone != null;
			this.bone = bone;
		}

		@Override
		public byte[] getName() {
			return bone.getName();
		}

	}

	/**
	 * マテリアルを実装します。
	 */
	private class MaterialAccessor implements IMMDMaterial {

		/**
		 * マテリアルを保持します。
		 */
		private MMDMaterial material;

		/**
		 * 構築します。
		 * 
		 * @param material
		 *            マテリアル。nullは不可。
		 */
		public MaterialAccessor(MMDMaterial material) {
			assert material != null;
			this.material = material;
		}

		@Override
		public int getVertexCount() {
			return material.m_pVertexes.length;
		}

	}

	/**
	 * モーションの区分情報を定義します。
	 */
	private class MotionSegment implements IMotionSegment {

		/**
		 * オフセットを保持します。
		 */
		private int offset;

		/**
		 * 終了点を保持します。
		 */
		private int end;

		/**
		 * 構築します。
		 * 
		 * @param offset
		 *            オフセット。
		 */
		public MotionSegment(int offset) {
			this.offset = offset;
			this.end = 0;

			Integer maxFrame = getMaxFrame();
			if (maxFrame != null) {
				this.end = maxFrame.intValue();
			}
		}

		@Override
		public int getStart() {
			return offset;
		}

		@Override
		public int getStop() {
			return end;
		}

		@Override
		public float getFrame(float frameRate, long currentTime) {
			float fcurrentTime = ((float) currentTime) / 1000.0f;

			int len = end - offset + 1;
			int currentFrame = ((int) (fcurrentTime * frameRate));
			int relativeFrame = currentFrame % len;
			return relativeFrame + offset;
		}

	}

}
