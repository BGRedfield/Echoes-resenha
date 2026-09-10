package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.orion.echoes.mars.EchoesMarsGame;
import managers.AssetManager;
import managers.SaveManager;

public class MenuScreen implements Screen {
    private final EchoesMarsGame game;
    private final SpriteBatch batch;
    private final AssetManager assets;
    private final BitmapFont font;
    private final OrthographicCamera camera = new OrthographicCamera();
    private final SaveManager saveManager = new SaveManager();

    public MenuScreen(EchoesMarsGame game, SpriteBatch batch, AssetManager assets) {
        this.game = game;
        this.batch = batch;
        this.assets = assets;
        this.font = assets.font;
    }

    @Override public void show() {
        camera.setToOrtho(false, 1280, 720);
    }

    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0.015f, 0.02f, 0.045f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        font.setColor(0.72f, 0.88f, 1f, 1f);
        font.getData().setScale(4f);
        font.draw(batch, "ECHOES", 500, 500);

        font.setColor(Color.WHITE);
        font.getData().setScale(1.25f);
        font.draw(batch, "LUA // MARTE // TITA", 500, 445);

        font.setColor(1f, 0.85f, 0.25f, 1f);
        font.getData().setScale(1.65f);
        font.draw(batch, "[ ENTER ] NOVA MISSAO", 445, 320);

        if (saveManager.hasSave()) {
            font.setColor(0.4f, 1f, 0.65f, 1f);
            font.getData().setScale(1.35f);
            font.draw(batch, "[ C ] CONTINUAR", 485, 240);
            font.getData().setScale(0.95f);
            font.setColor(0.7f, 0.78f, 0.88f, 1f);
            font.draw(batch, "SAVE: " + saveManager.getPhase(), 555, 205);
        }

        font.setColor(0.55f, 0.62f, 0.72f, 1f);
        font.getData().setScale(1f);
        font.draw(batch, "[ ESC ] SAIR", 560, 100);
        font.getData().setScale(1f);
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new MissionScreen(game, batch, assets));
        } else if (saveManager.hasSave() && Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            openSavedPhase();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    private void openSavedPhase() {
        String phase = saveManager.getPhase();
        if ("MARTE".equals(phase)) {
            game.setScreen(new GameScreen(game, batch, assets, true));
        } else if ("TITA".equals(phase)) {
            game.setScreen(new TitanScreen(game, batch, assets, true));
        } else {
            game.setScreen(new screens.lua.LunarScreen(game, batch, assets, true));
        }
    }

    @Override public void resize(int width, int height) { camera.setToOrtho(false, 1280, 720); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
