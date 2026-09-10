package managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class ParticleManager {

    private final ObjectMap<String, ParticleEffectPool> pools;
    private final Array<ParticleEffectPool.PooledEffect> activeEffects;

    public ParticleManager() {
        pools = new ObjectMap<>();
        activeEffects = new Array<>();
    }




    public void loadEffect(String name, String effectPath, String imagesDir) {
        ParticleEffect effect = new ParticleEffect();
        effect.load(Gdx.files.internal(effectPath), Gdx.files.internal(imagesDir));


        ParticleEffectPool pool = new ParticleEffectPool(effect, 8, 30);
        pools.put(name, pool);

        Gdx.app.log("ParticleManager", "Efeito carregado: " + name);
    }




    public void play(String name, float x, float y) {
        ParticleEffectPool pool = pools.get(name);
        if (pool == null) {
            Gdx.app.error("ParticleManager", "Efeito não encontrado: " + name);
            return;
        }

        ParticleEffectPool.PooledEffect effect = pool.obtain();
        effect.setPosition(x, y);
        effect.start();
        activeEffects.add(effect);
    }




    public void play(String name, float x, float y, float scale) {
        ParticleEffectPool pool = pools.get(name);
        if (pool == null) return;

        ParticleEffectPool.PooledEffect effect = pool.obtain();
        effect.setPosition(x, y);
        effect.scaleEffect(scale);
        effect.start();
        activeEffects.add(effect);
    }




    public void update(float delta) {
        for (int i = activeEffects.size - 1; i >= 0; i--) {
            ParticleEffectPool.PooledEffect effect = activeEffects.get(i);
            effect.update(delta);

            if (effect.isComplete()) {
                effect.free();
                activeEffects.removeIndex(i);
            }
        }
    }




    public void render(SpriteBatch batch) {
        for (ParticleEffectPool.PooledEffect effect : activeEffects) {
            effect.draw(batch);
        }
    }




    public void dispose() {
        for (ParticleEffectPool.PooledEffect effect : activeEffects) {
            effect.free();
        }
        activeEffects.clear();

        for (ParticleEffectPool pool : pools.values()) {
            pools.clear();
        }
    }
}
