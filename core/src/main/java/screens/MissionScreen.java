package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.orion.echoes.mars.EchoesMarsGame;
import managers.AssetManager;

public class MissionScreen implements Screen {
    private final EchoesMarsGame game;
    private final SpriteBatch batch;
    private final AssetManager assets;
    private final BitmapFont font;
    private final OrthographicCamera camera = new OrthographicCamera();

    public MissionScreen(EchoesMarsGame game, SpriteBatch batch, AssetManager assets) {
        this.game = game;
        this.batch = batch;
        this.assets = assets;
        this.font = assets.font;
    }

    @Override public void show() { camera.setToOrtho(false, 1280, 720); }

    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0.02f, 0.025f, 0.05f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        font.setColor(0.72f, 0.88f, 1f, 1f);
        font.getData().setScale(2.6f);
        font.draw(batch, "MISSION // LUA", 455, 625);

        font.setColor(1f, 0.85f, 0.25f, 1f);
        font.getData().setScale(1.35f);
        font.draw(batch, "OBJETIVO", 560, 555);

        font.setColor(Color.WHITE);
        font.getData().setScale(1f);
        font.draw(batch, "Explore a Lua e sobreviva.", 470, 515);
        font.draw(batch, "Colete oxigenio, comida e rochas de gelo.", 420, 480);
        font.draw(batch, "Leve o gelo para a base e processe-o.", 425, 445);
        font.draw(batch, "Conclua a missao para liberar o portal para Marte.", 355, 410);

        font.setColor(0.45f, 1f, 0.7f, 1f);
        font.getData().setScale(1.2f);
        font.draw(batch, "REQUISITOS", 535, 350);

        font.setColor(Color.WHITE);
        font.getData().setScale(0.95f);
        font.draw(batch, "[1] Movimento WASD / SETAS", 455, 315);
        font.draw(batch, "[2] HUD fixa com O2 e recursos", 455, 285);
        font.draw(batch, "[3] Base e protecao", 455, 255);
        font.draw(batch, "[4] Coleta de O2 / comida / gelo", 455, 225);
        font.draw(batch, "[5] Processamento de gelo", 455, 195);

        font.setColor(1f, 0.85f, 0.25f, 1f);
        font.getData().setScale(1.5f);
        font.draw(batch, "[ ENTER ] COMEÇAR", 490, 120);

        font.setColor(0.55f, 0.62f, 0.72f, 1f);
        font.getData().setScale(0.95f);
        font.draw(batch, "[ ESC ] VOLTAR AO TITULO", 475, 70);
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new screens.lua.LunarScreen(game, batch, assets, false));
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game, batch, assets));
        }
    }

    @Override public void resize(int width, int height) { camera.setToOrtho(false, 1280, 720); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
