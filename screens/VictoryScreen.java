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

public class VictoryScreen implements Screen {
    private final EchoesMarsGame game;
    private final SpriteBatch batch;
    private final AssetManager assets;
    private final OrthographicCamera camera = new OrthographicCamera();
    private final BitmapFont font;
    private final float tempoSobrevivido;

    public VictoryScreen(EchoesMarsGame game, SpriteBatch batch, AssetManager assets, float tempoSobrevivido) {
        this.game = game;
        this.batch = batch;
        this.assets = assets;
        this.tempoSobrevivido = tempoSobrevivido;
        this.font = assets.font;
    }

    @Override public void show() { camera.setToOrtho(false,1280,720); }

    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0.03f,0.08f,0.06f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.setColor(0.4f,1f,0.65f,1f);
        font.getData().setScale(2.8f);
        font.draw(batch,"ECHOES // MISSAO CONCLUIDA",300,560);
        font.setColor(Color.WHITE);
        font.getData().setScale(1.25f);
        font.draw(batch,"Lua, Marte e Tita foram concluidos.",430,490);
        font.draw(batch,"Voce cumpriu a sequencia completa da missao.",395,455);
        font.setColor(1f,0.85f,0.25f,1f);
        font.getData().setScale(1.5f);
        font.draw(batch,"[ ENTER ] VOLTAR AO TITULO",425,320);
        batch.end();
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) game.setScreen(new MenuScreen(game,batch,assets));
    }

    @Override public void resize(int width,int height){camera.setToOrtho(false,1280,720);} @Override public void pause(){} @Override public void resume(){} @Override public void hide(){} @Override public void dispose(){}
}
