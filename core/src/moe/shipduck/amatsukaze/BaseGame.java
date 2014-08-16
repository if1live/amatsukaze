package moe.shipduck.amatsukaze;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

public class BaseGame extends ApplicationAdapter {
	// dev tool
	public final static int PREF_HUDWIDTH = 640;
	public final static int PREF_HUDHEIGHT = 480;
	protected Stage hud;
	protected float hudWidth, hudHeight;
	protected Skin skin;
	protected Label fpsLabel;
	protected Label vertexCountLabel;
	protected Label textureBindsLabel;
	protected Label shaderSwitchesLabel;
	protected Label drawCallsLabel;
	protected Label glCallsLabel;
	protected final StringBuilder stringBuilder = new StringBuilder();
	
	@Override
	public void create() {
		GLProfiler.enable();
		createHUD();
	}

	protected void createHUD () {
		hud = new Stage(new ScalingViewport(Scaling.fit, PREF_HUDWIDTH, PREF_HUDHEIGHT));
		hudWidth = hud.getWidth();
		hudHeight = hud.getHeight();
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		
		fpsLabel = new Label("FPS: 999", skin);
		hud.addActor(fpsLabel);
		
		vertexCountLabel = new Label("Vertices: 999", skin);
		vertexCountLabel.setPosition(0, fpsLabel.getTop());
		hud.addActor(vertexCountLabel);

		textureBindsLabel = new Label("Texture bindings: 999", skin);
		textureBindsLabel.setPosition(0, vertexCountLabel.getTop());
		hud.addActor(textureBindsLabel);

		shaderSwitchesLabel = new Label("Shader switches: 999", skin);
		shaderSwitchesLabel.setPosition(0, textureBindsLabel.getTop());
		hud.addActor(shaderSwitchesLabel);

		drawCallsLabel = new Label("Draw calls: 999", skin);
		drawCallsLabel.setPosition(0, shaderSwitchesLabel.getTop());
		hud.addActor(drawCallsLabel);

		glCallsLabel = new Label("GL calls: 999", skin);
		glCallsLabel.setPosition(0, drawCallsLabel.getTop());
		hud.addActor(glCallsLabel);
	
	}
	
	protected void getStatus (final StringBuilder stringBuilder) {
		stringBuilder.setLength(0);
		stringBuilder.append("GL calls: ");
		stringBuilder.append(GLProfiler.calls);
		glCallsLabel.setText(stringBuilder);

		stringBuilder.setLength(0);
		stringBuilder.append("Draw calls: ");
		stringBuilder.append(GLProfiler.drawCalls);
		drawCallsLabel.setText(stringBuilder);

		stringBuilder.setLength(0);
		stringBuilder.append("Shader switches: ");
		stringBuilder.append(GLProfiler.shaderSwitches);
		shaderSwitchesLabel.setText(stringBuilder);

		stringBuilder.setLength(0);
		stringBuilder.append("Texture bindings: ");
		stringBuilder.append(GLProfiler.textureBindings);
		textureBindsLabel.setText(stringBuilder);

		stringBuilder.setLength(0);
		stringBuilder.append("Vertices: ");
		stringBuilder.append((int)GLProfiler.vertexCount.total);
		vertexCountLabel.setText(stringBuilder);

		GLProfiler.reset();

		stringBuilder.setLength(0);
		stringBuilder.append("FPS: ").append(Gdx.graphics.getFramesPerSecond());
	}
	
	@Override
	public void render() {
		stringBuilder.setLength(0);
		getStatus(stringBuilder);
		fpsLabel.setText(stringBuilder);
		hud.act(Gdx.graphics.getDeltaTime());
		hud.draw();
	}
	
	@Override
	public void resize (int width, int height) {
		super.resize(width, height);
		hud.getViewport().update(width, height, true);
		hudWidth = hud.getWidth();
		hudHeight = hud.getHeight();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		skin.dispose();
		skin = null;
		GLProfiler.disable();
	}
}
