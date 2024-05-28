package com.bensonzhou.apcsa;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.bensonzhou.apcsa.Main;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setResizable(false);
		config.setForegroundFPS(60);
		config.setTitle("WinKill");
		WindowManager windowManager = new Lwjgl3WindowManager();
		new Lwjgl3Application(new Main(windowManager), config);
	}
}
