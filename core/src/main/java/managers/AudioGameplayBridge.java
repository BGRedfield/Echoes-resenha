package managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Rectangle;

import java.lang.reflect.Field;

/**
 * Liga eventos de gameplay ao AudioManager sem duplicar código dentro de cada Screen.
 * Usa os campos de estado já existentes nas fases Lua, Marte e Titã.
 */
public class AudioGameplayBridge {

    private static final float PORTAL_RANGE = 650f;
    private static final float ALERT_ON = 25f;
    private static final float ALERT_OFF = 32f;

    private final AudioManager audio;

    private Screen lastScreen;
    private boolean lastOxygenCollected;
    private boolean lastFoodCollected;
    private boolean lastIceCollected;
    private boolean alertActive;

    public AudioGameplayBridge(AudioManager audio) {
        this.audio = audio;
    }

    public void update(Screen screen) {
        if (screen == null) {
            audio.stopPortal();
            stopAlert();
            lastScreen = null;
            return;
        }

        if (screen != lastScreen) {
            resetCollectionState(screen);
            lastScreen = screen;
        }

        detectCollection(screen);
        updatePortal(screen);
        updateAlert(screen);
    }

    private void resetCollectionState(Screen screen) {
        lastOxygenCollected = readBoolean(screen, "gotOxygen");
        lastFoodCollected = readBoolean(screen, "gotFood");
        lastIceCollected = readBoolean(screen, "gotIce");
    }

    private void detectCollection(Screen screen) {
        boolean oxygenCollected = readBoolean(screen, "gotOxygen");
        boolean foodCollected = readBoolean(screen, "gotFood");
        boolean iceCollected = readBoolean(screen, "gotIce");

        if (oxygenCollected && !lastOxygenCollected) {
            audio.playCollect();
        }
        if (foodCollected && !lastFoodCollected) {
            audio.playCollect();
        }
        if (iceCollected && !lastIceCollected) {
            audio.playCollect();
        }

        lastOxygenCollected = oxygenCollected;
        lastFoodCollected = foodCollected;
        lastIceCollected = iceCollected;
    }

    private void updatePortal(Screen screen) {
        Rectangle player = readRectangle(screen, "player");
        Rectangle portal = readFirstRectangle(screen, "portal", "portalTitan", "exitPortal");

        boolean portalActive = readBoolean(screen, "missionComplete")
            || readBoolean(screen, "missionUnlocked")
            || readBoolean(screen, "victory");

        if (player == null || portal == null || !portalActive) {
            audio.stopPortal();
            return;
        }

        float px = player.x + player.width * 0.5f;
        float py = player.y + player.height * 0.5f;
        float tx = portal.x + portal.width * 0.5f;
        float ty = portal.y + portal.height * 0.5f;

        float dx = px - tx;
        float dy = py - ty;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance >= PORTAL_RANGE) {
            audio.stopPortal();
            return;
        }

        float volume = 1f - distance / PORTAL_RANGE;
        volume = Math.max(0.05f, Math.min(1f, volume));
        audio.playPortalProximity(volume);

        boolean enter = Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ENTER);
        boolean e = Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.E);
        if ((enter || e) && player.overlaps(portal)) {
            audio.stopPortal();
        }
    }

    private void updateAlert(Screen screen) {
        float oxygen = readFloat(screen, "oxygen", 100f);
        float energy = readFloat(screen, "energy", 100f);
        boolean danger = oxygen <= ALERT_ON || energy <= ALERT_ON;

        if (!alertActive && danger) {
            audio.playAlertLoop();
            alertActive = true;
        } else if (alertActive && oxygen >= ALERT_OFF && energy >= ALERT_OFF) {
            stopAlert();
        }
    }

    private void stopAlert() {
        if (alertActive) {
            audio.stopAlertLoop();
            alertActive = false;
        } else {
            audio.stopAlertLoop();
        }
    }

    private boolean readBoolean(Object target, String fieldName) {
        Object value = readField(target, fieldName);
        return value instanceof Boolean && (Boolean) value;
    }

    private float readFloat(Object target, String fieldName, float fallback) {
        Object value = readField(target, fieldName);
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        return fallback;
    }

    private Rectangle readRectangle(Object target, String fieldName) {
        Object value = readField(target, fieldName);
        return value instanceof Rectangle ? (Rectangle) value : null;
    }

    private Rectangle readFirstRectangle(Object target, String... fieldNames) {
        for (String fieldName : fieldNames) {
            Rectangle rectangle = readRectangle(target, fieldName);
            if (rectangle != null) {
                return rectangle;
            }
        }
        return null;
    }

    private Object readField(Object target, String fieldName) {
        Class<?> type = target.getClass();
        while (type != null) {
            try {
                Field field = type.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(target);
            } catch (NoSuchFieldException e) {
                type = type.getSuperclass();
            } catch (IllegalAccessException | RuntimeException e) {
                return null;
            }
        }
        return null;
    }
}
