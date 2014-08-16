package moe.shipduck.mmd;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;

import net.yzwlab.javammd.format.MMD_VECTOR2;
import net.yzwlab.javammd.format.MMD_VECTOR3;
import net.yzwlab.javammd.format.MMD_VERTEX_DESC;
import net.yzwlab.javammd.format.MMD_VERTEX_TEXUSE;
import net.yzwlab.javammd.format.PMD_MATERIAL_RECORD;
import net.yzwlab.javammd.format.PMD_VERTEX_RECORD;
import net.yzwlab.javammd.model.MMD_VERTEX_UNIT;

public class MikuMaterial {
	PMD_MATERIAL_RECORD m_material;
	MMD_VERTEX_UNIT[] m_pVertexes;
	Texture m_tex;
	Material gdxMaterial;
	
	Mesh mesh;
	
	public MikuMaterial(PMD_MATERIAL_RECORD pMaterial) {
		this.m_material = pMaterial;
		
		//Gdx material
		gdxMaterial = new Material();
		gdxMaterial.set(ColorAttribute.createAmbient(pMaterial.ambient.toColor()));
		gdxMaterial.set(ColorAttribute.createDiffuse(pMaterial.diffuse.toColor()));
		gdxMaterial.set(ColorAttribute.createSpecular(pMaterial.specular.toColor()));
		gdxMaterial.set(FloatAttribute.createShininess(pMaterial.shininess));
		String texFileName = pMaterial.getTextureFileName();
		if(texFileName.length() > 0) {
			Texture texture = new Texture(Gdx.files.internal(texFileName));
			gdxMaterial.set(TextureAttribute.createDiffuse(texture));
		}
	}

	public int init(MikuVertexList pVertexList, int offset) {
		assert pVertexList != null;
		Integer pNextOffset = 0;
		MMD_VERTEX_DESC pOriginalVert = null;
		PMD_VERTEX_RECORD vert = new PMD_VERTEX_RECORD();
		m_pVertexes = new MMD_VERTEX_UNIT[m_material.nEdges];
		for (int i = 0; i < m_material.nEdges; i++) {
			pOriginalVert = pVertexList.GetVertexDesc(i + offset);
			m_pVertexes[i] = new MMD_VERTEX_UNIT(pOriginalVert, new MMD_VERTEX_TEXUSE());
			m_pVertexes[i].pOriginalVert.copyCurrentTo(m_pVertexes[i].pCurrentVert);
			vert = pVertexList.GetVertex(i + offset);
		}
		pNextOffset = offset + m_material.nEdges;
		return pNextOffset;
	}
	
	public Mesh getMesh() {
		if(mesh == null) {
			MeshBuilder builder = new MeshBuilder();
			builder.begin(
				new VertexAttributes(
					VertexAttribute.Position(),
					VertexAttribute.Normal(),
					VertexAttribute.TexCoords(0)),
				GL20.GL_TRIANGLES
			);
			
			
			for(int i = 0 ; i < m_pVertexes.length ; ++i) {
				MMD_VERTEX_DESC vert = m_pVertexes[i].pOriginalVert;
				MMD_VECTOR3 point = vert.original.point;
				MMD_VECTOR3 normal = vert.original.normal;
				MMD_VECTOR2 uv = vert.original.uv;
				builder.vertex(
					point.x, point.y, point.z,
					normal.x, normal.y, normal.z,
					uv.x, uv.y
				);
			}
			
			mesh = builder.end();
		}
		return mesh;
	}
}
