package screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.orion.echoes.mars.EchoesMarsGame;
import managers.AssetManager;

import com.badlogic.gdx.Input;

public class GameOverScreen implements Screen {

    private final EchoesMarsGame game;
    private final SpriteBatch batch;
    private final AssetManager assets;
    private OrthographicCamera camera;
    private BitmapFont font;

    private Rectangle botaoReiniciar;
    private Rectangle botaoMenu;

    private String motivoMorte;

    public GameOverScreen(EchoesMarsGame game, SpriteBatch batch, AssetManager assets, String motivoMorte) {
        this.game = game;
        this.batch = batch;
        this.assets = assets;
        this.motivoMorte = motivoMorte;
    }

    @Override
    public void show(){
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        font = assets.font;

        botaoReiniciar = new Rectangle(420,320,440,70);
        botaoMenu = new Rectangle(420,220,440,70);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f,0.05f,0.05f,1f); // Fundo escuro vermelho
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        //Titulo
        font.getData().setScale(3.0f);
        font.setColor(1f,0.2f,0.2f,1f);
        font.draw(batch, "MISSAO FALHOU", 420,580);

        //Motivo
        font.getData().setScale(1.6f);
        font.setColor(1f,0.7f,0.5f,1f);
        font.draw(batch, motivoMorte, 380,500);

        //Botoes
        font.getData().setScale(1.8f);
        font.setColor(0.3f,0.9f,0.3f,1f);
        font.draw(batch, "[ REINICIAAR MISSAO ]", 450,360);

        font.setColor(0.9f,0.7f,0.3f,1f);
        font.draw(batch, "[ VOLTAR MENU ]", 470,260);

        //rpdapé
        font.getData().setScale(1.0f);
        font.setColor(0.6f,0.5f,0.5f,1f);
        font.draw(batch, "Base Orion - Echoes em Marte | Professor Ricardo Marcel", 38,80);

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            game.setScreen(new MissionScreen(game, batch, assets));
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game, batch, assets));
            return;
        }

        //Clique
        if (Gdx.input.justTouched()) {
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if (botaoReiniciar.contains(touch.x, touch.y)) {
                game.setScreen(new GameScreen(game,batch,assets));
            } else if (botaoMenu.contains(touch.x, touch.y)) {
                game.setScreen(new MenuScreen(game,batch,assets));
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, 1280,720);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
