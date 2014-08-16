package moe.shipduck.mmd;

import java.util.List;

import net.yzwlab.javammd.format.MMD_VERTEX_DESC;
import net.yzwlab.javammd.format.MMD_VERTEX_TEXUSE;
import net.yzwlab.javammd.format.PMD_VERTEX_RECORD;

public class MikuVertexList {
	protected List<PMD_VERTEX_RECORD> m_vertexes;
	protected List<Short> m_indices;
	protected MMD_VERTEX_DESC[] m_pVertexes;
	
	public MikuVertexList(List<PMD_VERTEX_RECORD> pVertexes, List<Short> pIndices) {
		assert pVertexes != null;
		assert pIndices != null;
		
		m_vertexes = pVertexes;
		m_indices = pIndices;
		m_pVertexes = null;
		
		CreateVertexDesc();
	}
	
	public MMD_VERTEX_DESC[] GetVertexDescs() {
		return m_pVertexes;
	}

	public MMD_VERTEX_DESC GetVertexDesc(int index) {
		int vindex = 0;
		if (m_indices.size() <= index) {
			throw new IllegalArgumentException("E_INVALIDARG");
		}
		vindex = m_indices.get(index);
		return m_pVertexes[vindex];
	}

	public PMD_VERTEX_RECORD GetVertex(int index) {
		int vindex = 0;
		if (m_indices.size() <= index) {
			throw new IllegalArgumentException("E_INVALIDARG");
		}
		vindex = m_indices.get(index);
		return m_vertexes.get(vindex);
	}
	
	public void ResetVertexes() {
		MMD_VERTEX_DESC pDesc = null;
		MMD_VERTEX_TEXUSE buffer = new MMD_VERTEX_TEXUSE();
		for (int i = 0; i < m_vertexes.size(); i++) {
			pDesc = m_pVertexes[i];
			pDesc.setFaced(pDesc.getOriginal());
			pDesc.setCurrent(pDesc.getFaced(buffer));
		}
		return;
	}

	private void CreateVertexDesc() {
		m_pVertexes = new MMD_VERTEX_DESC[m_vertexes.size()];
		MMD_VERTEX_TEXUSE buffer = new MMD_VERTEX_TEXUSE();
		for (int i = 0; i < m_vertexes.size(); i++) {
			PMD_VERTEX_RECORD vert = m_vertexes.get(i);
			MMD_VERTEX_DESC pTargetVert = new MMD_VERTEX_DESC(vert);
			pTargetVert.setFaced(pTargetVert.getOriginal());
			pTargetVert.setCurrent(pTargetVert.getFaced(buffer));
			m_pVertexes[i] = pTargetVert;
		}
	}
}
