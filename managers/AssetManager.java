package managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Disposable;

public class AssetManager implements Disposable {

    // =========================================================
    // TEXTURAS
    // =========================================================

    public Texture astronautaTexture;
    public Texture oxigenioTexture;
    public Texture comidaTexture;
    public Texture abrigoTexture;
    public Texture backgroundTexture;

    // =========================================================
    // SPRITES COM ESCALA DE JOGO
    // =========================================================

    public Sprite astronautaSprite;
    public Sprite oxigenioSprite;
    public Sprite comidaSprite;
    public Sprite abrigoSprite;

    // =========================================================
    // FONTE
    // =========================================================

    public BitmapFont font;

    public void load() {

        // Carrega as texturas usadas pelo projeto.
        astronautaTexture = new Texture(
            Gdx.files.internal("textures/astronauta.png")
        );

        oxigenioTexture = new Texture(
            Gdx.files.internal("textures/oxigenio.png")
        );

        comidaTexture = new Texture(
            Gdx.files.internal("textures/comida.png")
        );

        abrigoTexture = new Texture(
            Gdx.files.internal("textures/abrigo.png")
        );

        backgroundTexture = new Texture(
            Gdx.files.internal("textures/marte_background.png")
        );

        // Filtro linear evita serrilhado quando os assets
        // são desenhados na escala do jogo.
        astronautaTexture.setFilter(
            Texture.TextureFilter.Linear,
            Texture.TextureFilter.Linear
        );

        oxigenioTexture.setFilter(
            Texture.TextureFilter.Linear,
            Texture.TextureFilter.Linear
        );

        comidaTexture.setFilter(
            Texture.TextureFilter.Linear,
            Texture.TextureFilter.Linear
        );

        abrigoTexture.setFilter(
            Texture.TextureFilter.Linear,
            Texture.TextureFilter.Linear
        );

        backgroundTexture.setFilter(
            Texture.TextureFilter.Linear,
            Texture.TextureFilter.Linear
        );

        // Escala visual consistente com o astronauta das fases.
        astronautaSprite = new Sprite(astronautaTexture);
        astronautaSprite.setSize(54f, 54f);

        // Itens menores que o jogador, mas ainda legíveis.
        oxigenioSprite = new Sprite(oxigenioTexture);
        oxigenioSprite.setSize(48f, 48f);

        comidaSprite = new Sprite(comidaTexture);
        comidaSprite.setSize(48f, 48f);

        // Abrigo em escala próxima ao personagem,
        // evitando ocupar uma área exagerada da fase.
        abrigoSprite = new Sprite(abrigoTexture);
        abrigoSprite.setSize(220f, 130f);

        // HUD legível.
        font = new BitmapFont();
        font.setColor(1f, 1f, 1f, 1f);
        font.getData().setScale(1.6f);
    }

    @Override
    public void dispose() {
        if (astronautaTexture != null) astronautaTexture.dispose();
        if (oxigenioTexture != null) oxigenioTexture.dispose();
        if (comidaTexture != null) comidaTexture.dispose();
        if (abrigoTexture != null) abrigoTexture.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (font != null) font.dispose();
    }
}
