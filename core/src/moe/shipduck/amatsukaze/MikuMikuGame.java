package moe.shipduck.amatsukaze;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.yzwlab.javammd.IGLObject;
import net.yzwlab.javammd.ReadException;
import net.yzwlab.javammd.format.MMD_VECTOR3;
import net.yzwlab.javammd.format.MMD_VERTEX_TEXUSE;
import net.yzwlab.javammd.format.PMD_MATERIAL_RECORD;
import net.yzwlab.javammd.io.GdxFileBuffer;
import net.yzwlab.javammd.model.MMDMaterial;
import net.yzwlab.javammd.model.MMDModel;
import net.yzwlab.javammd.model.MMDMaterial.MMD_VERTEX_UNIT;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;

public class MikuMikuGame extends ApplicationAdapter {
	MMDModel model;

	public PerspectiveCamera cam;
	public ModelBatch modelBatch;
	public Model simpleModel;
	public ModelInstance instance;
	public Environment environment;
	public CameraInputController camController;

	private Long baseTime;
	
	Mesh mesh;
	Texture texture;
	
	String vertexShader = "attribute vec4 a_position;    \n" + 
            "attribute vec2 a_texCoord0;\n" +
            "attribute vec4 a_color;\n" +
            "uniform mat4 u_worldView;\n" + 
            "varying vec2 v_texCoords;" +
            "varying vec4 v_color;" +
            "void main()                  \n" + 
            "{                            \n" +
            "   v_color = a_color; \n" +	
            "   v_texCoords = a_texCoord0; \n" + 
            "   gl_Position =  u_worldView * a_position;  \n"      + 
            "}                            \n" ;
	String fragmentShader = "#ifdef GL_ES\n" +
              "precision mediump float;\n" + 
              "#endif\n" +  
              "varying vec2 v_texCoords;\n" +
              "varying vec4 v_color;\n" +
              "uniform sampler2D u_texture;\n" + 
              "void main()                                  \n" + 
              "{                                            \n" + 
              "  gl_FragColor = v_color;\n" +
              "}";
	ShaderProgram shader;
	
	public Mesh createMikuMesh(MMDModel model) {
		List<Float> vertices = new ArrayList<Float>();
		for(int i = 0 ; i < model.getMaterialCount() ; ++i) {
			MMDMaterial material = model.getRawMaterial(i);
			
			PMD_MATERIAL_RECORD pmdMtl = material.getRawMaterial();
			
			
			
			
			for (MMD_VERTEX_UNIT unit : material.getVertices()) {
				MMD_VERTEX_TEXUSE v = unit.pCurrentVert;
				
				vertices.add(v.point.x);
				vertices.add(v.point.y);
				vertices.add(v.point.z);
				
				vertices.add(v.normal.x);
				vertices.add(v.normal.y);
				vertices.add(v.normal.z);
				
				vertices.add(v.uv.x);
				vertices.add(v.uv.y);
				
				float color = pmdMtl.diffuse.toFloatBits(); 
				vertices.add(color);
			}
		}
		
		/*
		List<Float> vertices = new ArrayList<Float>();
		
		vertices.add(-1f);
		vertices.add(-1f);
		vertices.add(0f);
		vertices.add(0f);
		vertices.add(0f);

		vertices.add(1f);
		vertices.add(-1f);
		vertices.add(0f);
		vertices.add(1f);
		vertices.add(0f);

		vertices.add(1f);
		vertices.add(1f);
		vertices.add(0f);
		vertices.add(1f);
		vertices.add(1f);

		vertices.add(-1f);
		vertices.add(1f);
		vertices.add(0f);
		vertices.add(0f);
		vertices.add(1f);
		*/
		
		//short[] indices = new short[] {0, 1, 2, 2, 3, 0}; 
		Mesh mesh = new Mesh(true, vertices.size() / 9, 0,  // static mesh with 4 vertices and no indices
			VertexAttribute.Position(), 
			VertexAttribute.Normal(),
			VertexAttribute.TexCoords(0),
			VertexAttribute.Color()
		);
		//mesh.setIndices(indices);
		
		float[] tmpVert = new float[vertices.size()];
		int i = 0;

		for (Float f : vertices) {
			tmpVert[i++] = (f != null ? f : Float.NaN); // Or whatever default you want.
		}
		mesh.setVertices(tmpVert);
		
		return mesh;
	}


	public Model createSampleModel() {
		ModelBuilder modelBuilder = new ModelBuilder();

		simpleModel = modelBuilder.createBox(5f, 5f, 5f, 
				new Material(ColorAttribute.createDiffuse(Color.GREEN)),
				Usage.Position | Usage.Normal);

		return simpleModel;

	}

	@Override
	public void create() {
		modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(10f, 10f, 10f);
		cam.lookAt(0, 0, 0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);

		simpleModel = createSampleModel();
		instance = new ModelInstance(simpleModel);
		
		
		shader = new ShaderProgram(vertexShader, fragmentShader);
		texture = new Texture("eye2.bmp");
		
		
		//load mmmd data
		model = loadModel("test.pmd", "test.vmd");		
		mesh = createMikuMesh(model);
	}

	public MMDModel loadModel(String pmdFile, String vmdFile) {
		MMDModel model = new MMDModel();
		FileHandle pmdFileHandle = Gdx.files.internal(pmdFile);
		FileHandle vmdFileHandle = Gdx.files.internal(vmdFile);
		
		try {
			model.openPMD(new GdxFileBuffer(pmdFileHandle));
			model.openVMD(new GdxFileBuffer(vmdFileHandle));
		} catch (ReadException e) {
			e.printStackTrace();
			System.exit(0);
		}

		// show model data
		boolean showFaceData = false;
		boolean showBoneData = false;
		boolean showIKData = false;

		if(showFaceData) {
			for (int j = 0; j < model.getFaceCount() ; j++) {
				System.out.println("Face #" + String.valueOf(j + 1) + ": "
						+ model.getFaceName(j));
			}
		}

		if(showBoneData) {
			for (int j = 0; j < model.getBoneCount(); j++) {
				System.out.println("Bone #" + String.valueOf(j + 1) + ": "
						+ new String(model.getBone(j).getName()));
			}
		}

		if(showIKData) {
			for (int j = 0; j < model.getIKCount(); j++) {
				String name = new String(model.getIKTargetName(j));
				System.out.println("IK #" + String.valueOf(j + 1) + ": " + name);
				if (name.indexOf("�r") > 0) {
					model.setIKEnabled(j, false);
				}
			}
		}

		return model;
	}

	@Override
	public void render() {
		draw();
		update();
	}

	public void update() {
		if (baseTime == null) {
			baseTime = System.currentTimeMillis();
		}

		//TODO slow operator
		//updateModel(this.model);
	}

	public void updateModel(MMDModel model) {
		long beginTime = System.currentTimeMillis();
		long updateEndTime = 0L;

		double curTime = (beginTime - baseTime) / 1000.0;
		float frame = (float) (curTime * 30.0);
		Integer frameCount = model.getMaxFrame();
		if (frameCount != null) {
			if (frameCount > 0) {
				while (frame > frameCount) {
					frame -= frameCount;
				}
			}
		}
		float nextFrame = frame;
		model.update(nextFrame);
		model.updateSkinning();
		model.updateVertexBuffer();

		updateEndTime = System.currentTimeMillis();

		long updateDur = updateEndTime - beginTime;
		//TODO 시간 업데이트 필요해지면 복구
		//System.out.println("Frame: update=" + updateDur + "msec");
		
		mesh = createMikuMesh(model);
		
	}

	public void draw() {
		camController.update();

		Gdx.gl.glClearColor(0, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		modelBatch.begin(cam);
		modelBatch.render(instance, environment);
		modelBatch.end();
		
		Matrix4 matrix = new Matrix4();
		float scale = 0.05f;
		matrix.scale(scale, scale, scale);
		
		Gdx.gl.glFrontFace(GL20.GL_CW);
		Gdx.gl.glEnable(GL20.GL_CULL_FACE);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		
		texture.bind();
		shader.begin();
		shader.setUniformMatrix("u_worldView", matrix);
		//shader.setUniformi("u_texture", 0);
		mesh.render(shader, GL20.GL_TRIANGLES);
		shader.end();
	}

	@Override
	public void dispose() {
		simpleModel.dispose();
		modelBatch.dispose();
	}
}
