package managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SaveManager {

    private static final String PREF_NAME = "echoes3mundos_save";
    private final Preferences prefs;

    public SaveManager() {
        prefs = Gdx.app.getPreferences(PREF_NAME);
    }

    public void save(
        String phase,
        float x,
        float y,
        float oxygen,
        float energy,
        int ice,
        int water,
        int fuel,
        boolean missionReady
    ) {
        save(
            phase,
            x,
            y,
            oxygen,
            energy,
            ice,
            water,
            fuel,
            missionReady,
            false
        );
    }

    public void save(
        String phase,
        float x,
        float y,
        float oxygen,
        float energy,
        int ice,
        int water,
        int fuel,
        boolean missionReady,
        boolean enemyDefeated
    ) {
        prefs.putBoolean("hasSave", true);

        prefs.putString("phase", phase);

        prefs.putFloat("x", x);
        prefs.putFloat("y", y);

        prefs.putFloat("oxygen", oxygen);
        prefs.putFloat("energy", energy);

        prefs.putInteger("ice", ice);
        prefs.putInteger("water", water);
        prefs.putInteger("fuel", fuel);

        prefs.putBoolean("missionReady", missionReady);
        prefs.putBoolean("enemyDefeated", enemyDefeated);

        prefs.flush();
    }

    public boolean hasSave() {
        return prefs.getBoolean("hasSave", false);
    }

    public String getPhase() {
        return prefs.getString("phase", "LUA");
    }

    public float getX() {
        return prefs.getFloat("x", 300f);
    }

    public float getY() {
        return prefs.getFloat("y", 300f);
    }

    public float getOxygen() {
        return prefs.getFloat("oxygen", 100f);
    }

    public float getEnergy() {
        return prefs.getFloat("energy", 100f);
    }

    public int getIce() {
        return prefs.getInteger("ice", 0);
    }

    public int getWater() {
        return prefs.getInteger("water", 0);
    }

    public int getFuel() {
        return prefs.getInteger("fuel", 0);
    }

    public boolean isMissionReady() {
        return prefs.getBoolean("missionReady", false);
    }

    public boolean isEnemyDefeated() {
        return prefs.getBoolean("enemyDefeated", false);
    }

    public void clear() {
        prefs.clear();
        prefs.flush();
    }
}
