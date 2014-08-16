package moe.shipduck.amatsukaze;

import java.util.Iterator;

import moe.shipduck.mmd.MikuMaterial;
import moe.shipduck.mmd.MikuModel;
import net.yzwlab.javammd.ReadException;
import net.yzwlab.javammd.format.PMDFile;
import net.yzwlab.javammd.format.PMD_MATERIAL_RECORD;
import net.yzwlab.javammd.format.PMD_VERTEX_RECORD;
import net.yzwlab.javammd.io.GdxByteBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;


public class DevGame extends BaseGame {
	Mesh mesh;
	MikuModel model;
	
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
		super.create();
		
		shader = new ShaderProgram(vertexShader, fragmentShader);
		
		FileHandle pmdFileHandle = Gdx.files.internal("test.pmd");
		PMDFile pmdFile = new PMDFile();
		try {
			boolean success = pmdFile.open(new GdxByteBuffer(pmdFileHandle));
			assert success == true;
		} catch (ReadException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		model = new MikuModel();
		model.setPMD(pmdFile);
	}

	
	@Override
	public void render() {
		draw();
		update();
		
		super.render();
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
		
		Iterator<MikuMaterial> materialIter = model.getMaterialIterator();
		while(materialIter.hasNext()) {
			MikuMaterial material = materialIter.next();
			Mesh mesh = material.getMesh();
			mesh.render(shader, GL20.GL_TRIANGLES);
		}
		
		shader.end();
	}
}
