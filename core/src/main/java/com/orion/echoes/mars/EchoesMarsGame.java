package com.orion.echoes.mars;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import managers.AssetManager;
import screens.MenuScreen;

public class EchoesMarsGame extends Game {

    private SpriteBatch batch;
    private AssetManager assets;

    @Override
    public void create() {
        batch = new SpriteBatch();
        assets = new AssetManager();
        assets.load(); //carrega todas as texturas

        //Começa no Menu
        setScreen(new MenuScreen(this, batch, assets));
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (assets != null) assets.dispose();
        super.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public AssetManager getAssets(){
        return assets;
    }
}
