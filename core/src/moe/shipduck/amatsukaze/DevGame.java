package moe.shipduck.amatsukaze;

import java.util.ArrayList;
import java.util.List;

import net.yzwlab.javammd.ReadException;
import net.yzwlab.javammd.format.PMDFile;
import net.yzwlab.javammd.format.PMD_MATERIAL_RECORD;
import net.yzwlab.javammd.format.PMD_VERTEX_RECORD;
import net.yzwlab.javammd.io.GdxByteBuffer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;

public class DevGame extends ApplicationAdapter {
	Mesh mesh;
	String vertexShader = "attribute vec4 a_position;    \n" + 
            "uniform mat4 u_worldView;\n" + 
            "void main()                  \n" + 
            "{                            \n" +	 
            "   gl_Position =  u_worldView * a_position;  \n"      + 
            "}                            \n" ;
	String fragmentShader = "#ifdef GL_ES\n" +
              "precision mediump float;\n" + 
              "#endif\n" +   
              "void main()                                  \n" + 
              "{                                            \n" + 
              "  gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);\n" +
              "}";
              
	ShaderProgram shader;
	
	@Override
	public void create() {
		FileHandle pmdFileHandle = Gdx.files.internal("test.pmd");
		PMDFile pmdFile = new PMDFile();
		try {
			boolean success = pmdFile.open(new GdxByteBuffer(pmdFileHandle));
			assert success == true;
		} catch (ReadException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		//mesh
		int stride = 3;
		float[] vertices = new float[pmdFile.GetVertexChunkSize() * stride];
		int idx = 0;
		for(int i = 0 ; i < pmdFile.GetVertexChunkSize() ; ++i) {
			PMD_VERTEX_RECORD vert = pmdFile.GetVertexChunk().get(i); 
			vertices[idx++] = vert.x;
			vertices[idx++] = vert.y;
			vertices[idx++] = vert.z;
		}
		
		short[] indices = new short[pmdFile.GetIndexChunkSize()];
		idx = 0;
		for(int i = 0 ; i < pmdFile.GetIndexChunkSize() ; ++i) {
			short val = pmdFile.GetIndexChunk().get(i);
			indices[idx++] = val;
		}
		
		mesh = new Mesh(true, vertices.length / stride, indices.length,
			VertexAttribute.Position()
		);
		mesh.setVertices(vertices);
		mesh.setIndices(indices);
		
		
		shader = new ShaderProgram(vertexShader, fragmentShader);
		
		//TODO
		for(int i = 0 ; i < pmdFile.GetMaterialChunkSize() ; ++i) {
			PMD_MATERIAL_RECORD mtlData = pmdFile.GetMaterialChunk().get(i);
			Material material = new Material();
			material.set(ColorAttribute.createAmbient(mtlData.ambient.toColor()));
			material.set(ColorAttribute.createDiffuse(mtlData.diffuse.toColor()));
			material.set(ColorAttribute.createSpecular(mtlData.specular.toColor()));
			material.set(FloatAttribute.createShininess(mtlData.shininess));
			String texFileName = mtlData.getTextureFileName();
			if(texFileName.length() > 0) {
				Texture texture = new Texture(Gdx.files.internal(texFileName));
				material.set(TextureAttribute.createDiffuse(texture));
			}
		}
	}
	
	@Override
	public void render() {
		draw();
		update();
	}
	public void update() {
		
	}
	
	public void draw() {
		Gdx.gl.glClearColor(0, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		Matrix4 matrix = new Matrix4();
		float scale = 0.05f;
		matrix.scale(scale, scale, scale);
		
		Gdx.gl.glFrontFace(GL20.GL_CW);
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		
		shader.begin();
		shader.setUniformMatrix("u_worldView", matrix);
		mesh.render(shader, GL20.GL_TRIANGLES);
		shader.end();
	}
}
