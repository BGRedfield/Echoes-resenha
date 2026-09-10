package com.orion.echoes.mars;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import managers.AssetManager;
import managers.AudioManager;
import screens.GameScreen;
import screens.IntroScreen;
import screens.MenuScreen;
import screens.MissionScreen;
import screens.TitanScreen;
import screens.lua.LunarScreen;

public class EchoesMarsGame extends Game {

    private SpriteBatch batch;
    private AssetManager assets;
    private AudioManager audio;

    private float stepTimer = 0f;

    @Override
    public void create() {
        batch = new SpriteBatch();
        assets = new AssetManager();
        assets.load();

        audio = new AudioManager();
        audio.load();

        setScreen(new IntroScreen(this, batch, assets));
    }

    @Override
    public void setScreen(Screen screen) {
        Screen previous = getScreen();

        if (audio != null) {
            audio.stopMusic();

            if (previous != null && isPortalTransition(previous, screen)) {
                audio.playPortal();
            } else if (previous != null) {
                audio.playClick();
            }

            startMusicFor(screen);
        }

        super.setScreen(screen);
    }

    private boolean isPortalTransition(Screen previous, Screen next) {
        return (previous instanceof LunarScreen && next instanceof GameScreen)
            || (previous instanceof GameScreen && next instanceof TitanScreen);
    }

    private void startMusicFor(Screen screen) {
        if (screen instanceof MenuScreen || screen instanceof MissionScreen) {
            audio.playMusic("music/tema_menu.ogg", 0.35f);
        } else if (screen instanceof LunarScreen) {
            audio.playMusic("music/tema_lua.ogg", 0.35f);
        } else if (screen instanceof GameScreen) {
            audio.playMusic("music/tema_marte.ogg", 0.35f);
        } else if (screen instanceof TitanScreen) {
            audio.playMusic("music/tema_tita.ogg", 0.35f);
        }
    }

    @Override
    public void render() {
        handleGlobalSfx(Gdx.graphics.getDeltaTime());
        super.render();
    }

    private void handleGlobalSfx(float delta) {
        if (audio == null || getScreen() == null) {
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
            || Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            audio.playClick();
        }

        if (isGameplayScreen()) {
            boolean moving = Gdx.input.isKeyPressed(Input.Keys.W)
                || Gdx.input.isKeyPressed(Input.Keys.A)
                || Gdx.input.isKeyPressed(Input.Keys.S)
                || Gdx.input.isKeyPressed(Input.Keys.D)
                || Gdx.input.isKeyPressed(Input.Keys.UP)
                || Gdx.input.isKeyPressed(Input.Keys.DOWN)
                || Gdx.input.isKeyPressed(Input.Keys.LEFT)
                || Gdx.input.isKeyPressed(Input.Keys.RIGHT);

            if (moving) {
                stepTimer -= delta;
                if (stepTimer <= 0f) {
                    audio.playCollect();
                    stepTimer = 0.45f;
                }
            } else {
                stepTimer = 0f;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                audio.playAlert();
            }
        }
    }

    private boolean isGameplayScreen() {
        return getScreen() instanceof LunarScreen
            || getScreen() instanceof GameScreen
            || getScreen() instanceof TitanScreen;
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
