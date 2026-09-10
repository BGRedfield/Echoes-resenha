package managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Audio centralizado do ECHOES.
 * Arquivos ausentes ou invalidos nao derrubam o jogo.
 */
public class AudioManager {

    private Music currentMusic;

    private Sound click;
    private Sound step;
    private Sound collect;
    private Sound portal;
    private Sound alert;

    public void load() {
        click = loadSound("sounds/click.wav");
        step = loadSound("sounds/step.wav");
        collect = loadSound("sounds/collect.wav");
        portal = loadSound("sounds/portal.wav");
        alert = loadSound("sounds/alerta.wav");
    }

    private Sound loadSound(String path) {
        FileHandle file = Gdx.files.internal(path);
        if (!file.exists()) {
            return null;
        }

        try {
            return Gdx.audio.newSound(file);
        } catch (GdxRuntimeException e) {
            Gdx.app.log("AudioManager", "Audio invalido ignorado: " + path);
            return null;
        }
    }

    public void playClick() {
        if (click != null) click.play(0.65f);
    }

    public void playStep() {
        if (step != null) step.play(0.40f);
    }

    public void playCollect() {
        if (collect != null) collect.play(0.70f);
    }

    public void playPortal() {
        if (portal != null) portal.play(0.80f);
    }

    public void playAlert() {
        if (alert != null) alert.play(0.70f);
    }

    public void playMusic(String path, float volume) {
        stopMusic();

        FileHandle file = Gdx.files.internal(path);
        if (!file.exists()) {
            Gdx.app.log("AudioManager", "Musica ausente: " + path);
            return;
        }

        try {
            currentMusic = Gdx.audio.newMusic(file);
            currentMusic.setLooping(true);
            currentMusic.setVolume(volume);
            currentMusic.play();
        } catch (GdxRuntimeException e) {
            Gdx.app.log("AudioManager", "Musica invalida ignorada: " + path);
            currentMusic = null;
        }
    }

    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
            currentMusic = null;
        }
    }

    public void dispose() {
        stopMusic();

        if (click != null) click.dispose();
        if (step != null) step.dispose();
        if (collect != null) collect.dispose();
        if (portal != null) portal.dispose();
        if (alert != null) alert.dispose();
    }
}
