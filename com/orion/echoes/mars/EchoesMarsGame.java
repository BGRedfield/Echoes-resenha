package com.orion.echoes.mars;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import managers.AssetManager;
import managers.AudioManager;
import screens.MenuScreen;

public class EchoesMarsGame extends Game {

    private SpriteBatch batch;
    private AssetManager assets;
    private AudioManager audio;

    @Override
    public void create() {
        batch = new SpriteBatch();
        assets = new AssetManager();
        assets.load();

        audio = new AudioManager();
        audio.load();

        setScreen(new MenuScreen(this, batch, assets));
    }

    @Override
    public void dispose() {
        if (screen != null) screen.dispose();
        if (audio != null) audio.dispose();
        if (batch != null) batch.dispose();
        if (assets != null) assets.dispose();
        super.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public AssetManager getAssets() {
        return assets;
    }

    public AudioManager getAudio() {
        return audio;
    }
}
