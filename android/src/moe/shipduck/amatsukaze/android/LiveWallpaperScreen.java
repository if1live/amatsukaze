package moe.shipduck.amatsukaze.android;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import moe.shipduck.amatsukaze.MyGdxGame;

public class LiveWallpaperScreen implements Screen {
	MyGdxGame myGame;

	public LiveWallpaperScreen(final Game game) {
		this.myGame = new MyGdxGame();
		this.myGame.create();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		myGame.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		myGame.resize(width, height);

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}
}
