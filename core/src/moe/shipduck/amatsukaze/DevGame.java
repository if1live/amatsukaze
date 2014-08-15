package moe.shipduck.amatsukaze;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;

public class DevGame extends ApplicationAdapter {
	@Override
	public void create() {
		FileHandle pmdFileHandle = Gdx.files.internal("test.pmd");
		
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
	}
}
