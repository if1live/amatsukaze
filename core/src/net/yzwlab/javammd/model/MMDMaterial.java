package net.yzwlab.javammd.model;

import java.util.List;

import net.yzwlab.javammd.GLTexture;
import net.yzwlab.javammd.IGL;
import net.yzwlab.javammd.IGLTextureProvider;
import net.yzwlab.javammd.ReadException;
import net.yzwlab.javammd.format.MMD_VERTEX_DESC;
import net.yzwlab.javammd.format.MMD_VERTEX_TEXUSE;
import net.yzwlab.javammd.format.PMD_MATERIAL_RECORD;
import net.yzwlab.javammd.format.PMD_VERTEX_RECORD;

/**
 * MMDのマテリアルを管理するクラスです。
 * MMD 자료를 관리하는 클래스입니다
 */
public class MMDMaterial {
	protected PMD_MATERIAL_RECORD m_material;

	protected GLTexture m_texture;

	// protected MMD_VERTEX_TEXUSE[] m_pCurrentVertexes;

	protected MMD_VERTEX_UNIT[] m_pVertexes;
	
	// TODO
	public MMD_VERTEX_UNIT[] getVertices() {
		return m_pVertexes;
	}
	
	// TODO
	public PMD_MATERIAL_RECORD getRawMaterial() {
		return m_material;
	}

	protected boolean m_bVisible;

	public MMDMaterial(PMD_MATERIAL_RECORD pMaterial) {
		assert pMaterial != null;
		
		this.m_bVisible = false;
		this.m_pVertexes = null;
		this.m_texture = null;
		this.m_material = new PMD_MATERIAL_RECORD();
		m_pVertexes = null;
		m_bVisible = true;
		m_material = pMaterial;
	}

	/**
	 * 初期化します。
	 * 
	 * @param pVertexList
	 *            頂点リスト。nullは不可。
	 * @param bones
	 *            ボーンリスト。nullは不可。
	 * @param offset
	 *            オフセット。
	 * @return 次のオフセット。
	 */
	public int init(MMDVertexList pVertexList, List<MMDBone> bones, int offset) {
		assert pVertexList != null;
		assert bones != null;
		Integer pNextOffset = 0;
		MMD_VERTEX_DESC pOriginalVert = null;
		PMD_VERTEX_RECORD vert = new PMD_VERTEX_RECORD();
		m_pVertexes = new MMD_VERTEX_UNIT[m_material.nEdges];
		for (int i = 0; i < m_material.nEdges; i++) {
			pOriginalVert = pVertexList.GetVertexDesc(i + offset);
			m_pVertexes[i] = new MMD_VERTEX_UNIT(pOriginalVert,
					new MMD_VERTEX_TEXUSE());
			m_pVertexes[i].pOriginalVert
					.copyCurrentTo(m_pVertexes[i].pCurrentVert);
			vert = pVertexList.GetVertex(i + offset);
			if (vert.b1 >= bones.size()) {
				throw new IllegalArgumentException("E_UNEXPECTED");
			}
			MMDBone targetBone = bones.get(vert.b1);
			if (targetBone == null) {
				throw new IllegalArgumentException("Bone not found: "
						+ vert.b1);
			}
			MMDBone[] tbones = new MMDBone[2];
			tbones[0] = targetBone;
			if (vert.b2 >= bones.size()) {
				throw new IllegalArgumentException("E_UNEXPECTED");
			}
			targetBone = bones.get(vert.b2);
			if (targetBone == null) {
				throw new IllegalArgumentException("Bone not found: "
						+ vert.b2);
			}
			tbones[1] = targetBone;
			m_pVertexes[i].pOriginalVert.setBones(tbones);
			m_pVertexes[i].pOriginalVert.setBweight((vert.bw / 100.0f));
		}
		pNextOffset = offset + m_material.nEdges;
		return pNextOffset;
	}

	public void SetVisible(boolean visible) {
		m_bVisible = visible;
		return;
	}

	public boolean IsVisible() {
		Boolean pVisible = false;
		pVisible = m_bVisible;
		return pVisible;
	}

	/**
	 * テクスチャの準備を行います。
	 * 텍스처 준비합니다
	 * 
	 * @param pTextureProvider
	 *            テクスチャを提供するインタフェース。nullは不可。
	 * @param handler
	 *            ハンドラ。nullは不可。
	 * @throws ReadException
	 *             読み込み失敗時のエラー。
	 */
	public void prepare(IGLTextureProvider pTextureProvider,
			final IGLTextureProvider.Handler handler) throws ReadException {
		assert pTextureProvider != null;
		assert handler != null;
		if (m_material.textureFileName[0] == '\0') {
			return;
		}
		pTextureProvider.load(m_material.textureFileName,
				new IGLTextureProvider.Handler() {

					@Override
					public void onSuccess(byte[] filename, GLTexture desc) {
						assert filename != null;
						assert desc != null;
						setTexture(desc);
						handler.onSuccess(filename, desc);
					}

					@Override
					public void onError(byte[] filename, Throwable error) {
						assert filename != null;
						assert error != null;
						handler.onError(filename, error);
					}

				});
	}

	/**
	 * 頂点バッファを更新します。
	 * 버텍스 버퍼를 업데이트합니다
	 */
	public void updateVertexBuffer() {
		if (m_bVisible == false) {
			return;
		}
		if (m_pVertexes == null) {
			throw new IllegalStateException();
		}
		for (MMD_VERTEX_UNIT vert : m_pVertexes) {
			vert.pOriginalVert.copyCurrentTo(vert.pCurrentVert);
		}
	}

	/**
	 * マテリアルの内容を描画します。
	 * 자료의 내용을 그립니다.
	 * 
	 * @param gl
	 *            描画対象プラットフォーム。nullは不可。
	 */
	public synchronized void draw(IGL gl) {
		assert gl != null;
		
		float dalpha = 0.0f;
		if (m_bVisible == false) {
			return;
		}
		gl.glPushMatrix();
		IGL.FrontFace frontFace = gl.glGetFrontFace();
		gl.glFrontFace(IGL.FrontFace.GL_CW);
		// dalpha = 1.0f;
		long bindGL_TEXTURE_2D = 0;
		boolean isGL_TEXTURE_2D = false;
		boolean isGL_BLEND = false;

		float[] matenv = new float[4];
		matenv[0] = m_material.diffuse.r;
		matenv[1] = m_material.diffuse.g;
		matenv[2] = m_material.diffuse.b;
		matenv[3] = m_material.diffuse.a * dalpha;
		gl.glMaterialfv(IGL.C.GL_FRONT_AND_BACK, IGL.C.GL_DIFFUSE, matenv);
		matenv[0] = m_material.ambient.r;
		matenv[1] = m_material.ambient.g;
		matenv[2] = m_material.ambient.b;
		matenv[3] = dalpha;
		gl.glMaterialfv(IGL.C.GL_FRONT_AND_BACK, IGL.C.GL_AMBIENT, matenv);
		matenv[0] = m_material.specular.r;
		matenv[1] = m_material.specular.g;
		matenv[2] = m_material.specular.b;
		matenv[3] = dalpha;
		gl.glMaterialfv(IGL.C.GL_FRONT_AND_BACK, IGL.C.GL_SPECULAR, matenv);
		matenv[0] = m_material.ambient.r;
		matenv[1] = m_material.ambient.g;
		matenv[2] = m_material.ambient.b;
		matenv[3] = dalpha;
		gl.glMaterialfv(IGL.C.GL_FRONT_AND_BACK, IGL.C.GL_EMISSION, matenv);
		gl.glMaterialf(IGL.C.GL_FRONT_AND_BACK, IGL.C.GL_SHININESS,
				m_material.shininess);
		gl.glEnableClientState(IGL.C.GL_VERTEX_ARRAY);
		gl.glEnableClientState(IGL.C.GL_NORMAL_ARRAY);
		if (m_texture != null) {
			gl.glEnableClientState(IGL.C.GL_TEXTURE_COORD_ARRAY);
		}
		isGL_TEXTURE_2D = gl.glIsEnabled(IGL.C.GL_TEXTURE_2D);
		isGL_BLEND = gl.glIsEnabled(IGL.C.GL_BLEND);
		bindGL_TEXTURE_2D = gl.glGetBindTexture(IGL.C.GL_TEXTURE_2D);
		gl.glEnable(IGL.C.GL_TEXTURE_2D);
		gl.glEnable(IGL.C.GL_BLEND);
		gl.glBlendFunc(IGL.C.GL_SRC_ALPHA, IGL.C.GL_ONE_MINUS_SRC_ALPHA);
		if (m_texture != null) {
			gl.glBindTexture(IGL.C.GL_TEXTURE_2D,
					m_texture.getTextureId(gl.getResourceContext()));
		}
		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glBegin(IGL.C.GL_TRIANGLES, m_pVertexes.length);
		for (MMD_VERTEX_UNIT unit : m_pVertexes) {
			unit.pCurrentVert.toGL(gl);
		}
		gl.glEnd();
		gl.glBindTexture(IGL.C.GL_TEXTURE_2D, bindGL_TEXTURE_2D);
		if (isGL_BLEND == false)
			gl.glDisable(IGL.C.GL_BLEND);
		if (isGL_TEXTURE_2D == false)
			gl.glDisable(IGL.C.GL_TEXTURE_2D);
		gl.glDisableClientState(IGL.C.GL_VERTEX_ARRAY);
		gl.glDisableClientState(IGL.C.GL_NORMAL_ARRAY);
		if (m_texture != null) {
			gl.glDisableClientState(IGL.C.GL_TEXTURE_COORD_ARRAY);
		}
		gl.glFrontFace(frontFace);
		gl.glPopMatrix();
		return;
	}

	public void UpdateVisibility() {
		MMDBone pBone = null;
		boolean vis = false;
		assert m_pVertexes != null : "E_UNEXPECTED";
		vis = true;
		for (int i = 0; i < m_material.nEdges; i++) {
			pBone = m_pVertexes[i].pOriginalVert.getBones()[0];
			if (pBone != null) {
				vis = pBone.isVisible();
				if (vis == false) {
					break;
				}
			}
			pBone = m_pVertexes[i].pOriginalVert.getBones()[1];
			if (pBone != null) {
				vis = pBone.isVisible();
				if (vis == false) {
					break;
				}
			}
		}
		m_bVisible = vis;
		return;
	}

	/**
	 * テクスチャを設定します。
	 * 텍스처를 설정합니다.
	 * 
	 * @param texture
	 *            テクスチャ。nullは不可。
	 */
	private synchronized void setTexture(GLTexture texture) {
		assert texture != null;
		m_texture = texture;
	}
}
