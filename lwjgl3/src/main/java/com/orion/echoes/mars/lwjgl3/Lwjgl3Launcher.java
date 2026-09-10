package com.orion.echoes.mars.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.orion.echoes.mars.EchoesMarsGame;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return;
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new EchoesMarsGame(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("ECHOES - Lua // Marte // Tita");
        configuration.useVsync(true);
        configuration.setForegroundFPS(60);

        // The Screens use a 1280x720 FitViewport; keeping the desktop window
        // at the same logical resolution makes the HUD and assets easier to read.
        configuration.setWindowedMode(1280, 720);
        configuration.setResizable(true);

        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");

        return configuration;
    }
}