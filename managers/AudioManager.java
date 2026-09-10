package managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

/**
 * Audio centralizado do ECHOES.
 * Cada Screen pode trocar a musica sem deixar a anterior tocando.
 * Os arquivos sao opcionais: se ainda nao existirem, o jogo continua sem crash.
 */
public class AudioManager {

    private Music currentMusic;

    private Sound click;
    private Sound collect;
    private Sound portal;
    private Sound alert;

    public void load() {
        click = loadSound("sounds/click.wav");
        collect = loadSound("sounds/coleta.wav");
        portal = loadSound("sounds/portal.wav");
        alert = loadSound("sounds/alerta.wav");
    }

    private Sound loadSound(String path) {
        FileHandle file = Gdx.files.internal(path);
        if (!file.exists()) return null;
        return Gdx.audio.newSound(file);
    }

    public void playClick() {
        if (click != null) click.play(0.65f);
    }

    public void playCollect() {
        if (collect != null) collect.play(0.7f);
    }

    public void playPortal() {
        if (portal != null) portal.play(0.8f);
    }

    public void playAlert() {
        if (alert != null) alert.play(0.7f);
    }

    public void playMusic(String path, float volume) {
        stopMusic();

        FileHandle file = Gdx.files.internal(path);
        if (!file.exists()) return;

        currentMusic = Gdx.audio.newMusic(file);
        currentMusic.setLooping(true);
        currentMusic.setVolume(volume);
        currentMusic.play();
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
        if (collect != null) collect.dispose();
        if (portal != null) portal.dispose();
        if (alert != null) alert.dispose();
    }
}
