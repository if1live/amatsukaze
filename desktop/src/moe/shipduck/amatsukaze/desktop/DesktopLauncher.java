package moe.shipduck.amatsukaze.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import moe.shipduck.amatsukaze.Basic3D;
import moe.shipduck.amatsukaze.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		int width = 1080;
		int height = 1920;
		float scale = 0.5f;
		
		config.width = (int)(width * scale);
		config.height = (int)(height * scale);
		config.title = "Amatsukaze";
		//new LwjglApplication(new MyGdxGame(), config);
		new LwjglApplication(new Basic3D(), config);
	}
}
