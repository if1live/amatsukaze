package moe.shipduck.amatsukaze.android;

import android.os.Bundle;


import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.tests.g3d.LightsTest;

import moe.shipduck.amatsukaze.MikuMikuGame;
import moe.shipduck.amatsukaze.MyGdxGame;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//initialize(new MyGdxGame(), config);
		//initialize(new MikuMikuGame(), config);
		initialize(new LightsTest(), config);
	}
}
