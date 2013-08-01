package com.ncgeek.games.shattered;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Shattered";
		cfg.useGL20 = true;
		cfg.width = 900;
		cfg.height = 600;
		
		com.ncgeek.games.shattered.utils.Log.setLogger(new Logger());
//		new LwjglApplication(new GamepadTest(), cfg);
		new LwjglApplication(new Shattered(), cfg);
	}
}
