package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.orion.echoes.mars.EchoesMarsGame;
import managers.AssetManager;

/**
 * Opening sequence required by Aula 12: logo + fade before gameplay/menu.
 */
public class IntroScreen implements Screen {

    private final EchoesMarsGame game;
    private final SpriteBatch batch;
    private final AssetManager assets;
    private final BitmapFont font;
    private final OrthographicCamera camera = new OrthographicCamera();

    private float time;

    private static final float INTRO_DURATION = 4.5f;
    private static final float FADE_DURATION = 1.0f;

    public IntroScreen(EchoesMarsGame game, SpriteBatch batch, AssetManager assets) {
        this.game = game;
        this.batch = batch;
        this.assets = assets;
        this.font = assets.font;
    }

    @Override
    public void show() {
        camera.setToOrtho(false, 1280, 720);
    }

    @Override
    public void render(float delta) {
        time += delta;

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
            || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)
            || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.getAudio().playClick();
            openMenu();
            return;
        }

        if (time >= INTRO_DURATION) {
            openMenu();
            return;
        }

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        float alpha = 1f;
        if (time < FADE_DURATION) {
            alpha = time / FADE_DURATION;
        } else if (time > INTRO_DURATION - FADE_DURATION) {
            alpha = (INTRO_DURATION - time) / FADE_DURATION;
        }
        alpha = Math.max(0f, Math.min(1f, alpha));

        batch.begin();

        font.getData().setScale(4.5f);
        font.setColor(0.72f, 0.88f, 1f, alpha);
        font.draw(batch, "ECHOES", 470f, 420f);

        font.getData().setScale(1.15f);
        font.setColor(1f, 1f, 1f, alpha);
        font.draw(batch, "UMA MISSAO ATRAVES DE TRES MUNDOS", 410f, 340f);

        font.getData().setScale(1f);
        font.setColor(0.55f, 0.65f, 0.78f, alpha);
        font.draw(batch, "LUA  //  MARTE  //  TITA", 500f, 285f);

        font.getData().setScale(0.9f);
        font.setColor(0.65f, 0.7f, 0.8f, alpha);
        font.draw(batch, "ENTER / ESPACO para pular", 505f, 100f);

        batch.end();
    }

    private void openMenu() {
        game.setScreen(new MenuScreen(game, batch, assets));
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, 1280, 720);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
