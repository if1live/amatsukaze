package moe.shipduck.amatsukaze.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tests.g3d.*;
import com.badlogic.gdx.tests.g3d.shaders.*;
import com.badlogic.gdx.tests.gles2.*;

import moe.shipduck.amatsukaze.*;


public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		/*
		int width = 1080;
		int height = 1920;
		float scale = 0.5f;
		config.width = (int)(width * scale);
		config.height = (int)(height * scale);
		config.title = "Amatsukaze";
		*/
		
		//new LwjglApplication(new MyGdxGame(), config);
		//new LwjglApplication(new Basic3D(), config);
		//new LwjglApplication(new MikuMikuGame(), config);
		new LwjglApplication(new DevGame(), config);
		//new LwjglApplication(new LightsTest(), config);
	}
}