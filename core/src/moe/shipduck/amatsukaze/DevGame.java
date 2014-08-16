package moe.shipduck.amatsukaze;

import java.util.Iterator;


import moe.shipduck.mmd.MikuMaterial;
import moe.shipduck.mmd.MikuModel;
import net.yzwlab.javammd.ReadException;
import net.yzwlab.javammd.format.PMDFile;
import net.yzwlab.javammd.io.GdxByteBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;


public class DevGame extends BaseGame {
	Mesh mesh;
	MikuModel model;
	
	//axis
	ModelInstance axesInstance;
	public ModelBatch modelBatch;
	public boolean showAxes = true;
	public final Color bgColor = new Color(0, 0, 0, 1);
	
	//light
	Environment environment;
	
	public Model cubeModel;
    public ModelInstance cubeInstance;
    
	//camera
	public PerspectiveCamera cam;
	public CameraInputController inputController;
	
	
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
		
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		shader = new ShaderProgram(vertexShader, fragmentShader);
		
		modelBatch = new ModelBatch();
		
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(10f, 10f, 10f);
		cam.lookAt(0, 0, 0);
		cam.near = 0.1f;
		cam.far = 1000f;
		cam.update();
		
		axesInstance = (new AxisModelGenerator()).createAxes();
		
		Gdx.input.setInputProcessor(inputController = new CameraInputController(cam));
		
		ModelBuilder modelBuilder = new ModelBuilder();
        cubeModel = modelBuilder.createBox(5f, 5f, 5f, 
            new Material(ColorAttribute.createDiffuse(Color.GREEN)),
            Usage.Position | Usage.Normal);
        cubeInstance = new ModelInstance(cubeModel);
		
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
		update();
		draw();
		super.render();
	}
	
	public void update() {
		inputController.update();
	}
	
	public void draw() {		
		Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		// libgdx
		Gdx.gl.glFrontFace(GL20.GL_CCW);
		modelBatch.begin(cam);
		if (showAxes) {
			modelBatch.render(axesInstance, environment);
		}
		modelBatch.render(cubeInstance, environment);
		modelBatch.end();
		
		// custom
		Matrix4 matrix = new Matrix4();
		float scale = 0.05f;
		matrix.scale(scale, scale, scale);
		
		Gdx.gl.glFrontFace(GL20.GL_CW);		
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
	
	@Override
	public void dispose() {
		cubeModel.dispose();
		modelBatch.dispose();
		super.dispose();
	}
}
