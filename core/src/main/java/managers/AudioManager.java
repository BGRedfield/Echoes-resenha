package managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

/**
 * Centraliza os efeitos e a trilha do ECHOES.
 */
public class AudioManager {

    private Music currentMusic;
    private Sound click;
    private Sound collect;
    private Sound portal;
    private Sound alert;

    private long portalLoopId = -1L;
    private long alertLoopId = -1L;

    public void load() {
        click = loadSound("sounds/clique.wav", "sounds/click.wav");
        collect = loadSound("sounds/collect.wav");
        portal = loadSound("sounds/portal.wav");
        alert = loadSound("sounds/alerta.wav");
    }

    private Sound loadSound(String primary, String fallback) {
        Sound result = loadSound(primary);
        return result != null ? result : loadSound(fallback);
    }

    private Sound loadSound(String path) {
        FileHandle file = Gdx.files.internal(path);
        if (!file.exists()) {
            Gdx.app.log("AudioManager", "SFX ausente: " + path);
            return null;
        }
        try {
            Sound sound = Gdx.audio.newSound(file);
            Gdx.app.log("AudioManager", "SFX carregado: " + path);
            return sound;
        } catch (Throwable e) {
            Gdx.app.error("AudioManager", "Falha ao carregar SFX: " + path, e);
            return null;
        }
    }

    public void playClick() {
        if (click != null) click.play(1.0f);
    }

    public void playStep() {
        if (click != null) click.play(0.20f);
    }

    public void playCollect() {
        if (collect != null) collect.play(0.80f);
    }

    public void playPortal() {
        if (portal != null) portal.play(0.85f);
    }

    public void playPortalProximity(float volume) {
        if (portal == null) return;
        volume = Math.max(0f, Math.min(1f, volume));
        if (volume <= 0.001f) {
            stopPortal();
            return;
        }
        if (portalLoopId == -1L) {
            portalLoopId = portal.loop(volume);
        } else {
            portal.setVolume(portalLoopId, volume);
        }
    }

    public void stopPortal() {
        if (portal != null && portalLoopId != -1L) {
            portal.stop(portalLoopId);
        }
        portalLoopId = -1L;
    }

    public void playAlertLoop() {
        if (alert == null || alertLoopId != -1L) return;
        alertLoopId = alert.loop(0.75f);
    }

    public void stopAlertLoop() {
        if (alert != null && alertLoopId != -1L) {
            alert.stop(alertLoopId);
        }
        alertLoopId = -1L;
    }

    public void playMusic(String path, float volume) {
        stopMusic();
        FileHandle file = Gdx.files.internal(path);
        if (!file.exists()) {
            Gdx.app.log("AudioManager", "Trilha ausente: " + path);
            return;
        }
        try {
            currentMusic = Gdx.audio.newMusic(file);
            currentMusic.setLooping(true);
            currentMusic.setVolume(volume);
            currentMusic.play();
        } catch (Throwable e) {
            Gdx.app.error("AudioManager", "Falha ao carregar trilha: " + path, e);
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
        stopPortal();
        stopAlertLoop();
        if (click != null) click.dispose();
        if (collect != null) collect.dispose();
        if (portal != null) portal.dispose();
        if (alert != null) alert.dispose();
    }
}
