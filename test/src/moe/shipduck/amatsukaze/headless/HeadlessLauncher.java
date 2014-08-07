package moe.shipduck.amatsukaze.headless;

import moe.shipduck.amatsukaze.MyGdxGame;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;

public class HeadlessLauncher {
	public static void main(String[] arg) {
		HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
		new HeadlessApplication(new MyGdxGame(), config);
	}
}
