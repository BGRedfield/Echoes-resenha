package com.orion.echoes.mars;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import managers.AssetManager;
import managers.AudioGameplayBridge;
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
    private AudioGameplayBridge audioBridge;

    @Override
    public void create() {
        batch = new SpriteBatch();
        assets = new AssetManager();
        assets.load();

        audio = new AudioManager();
        audio.load();
        audioBridge = new AudioGameplayBridge(audio);

        audio.playClick();
        setScreen(new IntroScreen(this, batch, assets));
    }

    @Override
    public void setScreen(Screen screen) {
        Screen previous = getScreen();

        if (audio != null) {
            audio.stopMusic();
            audio.stopPortal();
            audio.stopAlertLoop();

            if (previous != null && isPortalTransition(previous, screen)) {
                audio.playPortal();
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
        if (audio == null) {
            return;
        }

        if (screen instanceof MenuScreen || screen instanceof MissionScreen) {
            audio.playMusic("music/tema_menu.wav", 0.22f);
        } else if (screen instanceof LunarScreen) {
            audio.playMusic("music/tema_lua.wav", 0.22f);
        } else if (screen instanceof GameScreen) {
            audio.playMusic("music/tema_marte.wav", 0.22f);
        } else if (screen instanceof TitanScreen) {
            audio.playMusic("music/tema_tita.wav", 0.22f);
        }
    }

    @Override
    public void render() {
        handleGlobalSfx();

        if (audioBridge != null) {
            audioBridge.update(getScreen());
        }

        super.render();
    }

    private void handleGlobalSfx() {
        if (audio == null || getScreen() == null) {
            return;
        }

        // Feedback de UI: clique do mouse, Enter, C ou E.
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
            || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
            || Gdx.input.isKeyJustPressed(Input.Keys.C)
            || Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            audio.playClick();
        }
    }

    @Override
    public void dispose() {
        if (screen != null) {
            screen.dispose();
        }
        if (audio != null) {
            audio.dispose();
        }
        if (batch != null) {
            batch.dispose();
        }
        if (assets != null) {
            assets.dispose();
        }
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
