package managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Disposable;

public class AssetManager implements Disposable {

    // =========================================================
    // TEXTURAS PRINCIPAIS
    // =========================================================

    public Texture astronautaTexture;
    public Texture oxigenioTexture;
    public Texture comidaTexture;
    public Texture abrigoTexture;
    public Texture backgroundTexture;

    // =========================================================
    // SPRITES COM ESCALA CONSISTENTE
    // =========================================================

    public Sprite astronautaSprite;
    public Sprite oxigenioSprite;
    public Sprite comidaSprite;
    public Sprite abrigoSprite;

    // =========================================================
    // FONTE DO HUD
    // =========================================================

    public BitmapFont font;

    public void load() {
        astronautaTexture = new Texture(Gdx.files.internal("textures/astronauta.png"));
        oxigenioTexture = new Texture(Gdx.files.internal("textures/oxigenio.png"));
        comidaTexture = new Texture(Gdx.files.internal("textures/comida.png"));
        abrigoTexture = new Texture(Gdx.files.internal("textures/abrigo.png"));
        backgroundTexture = new Texture(Gdx.files.internal("textures/marte_background.png"));

        // Linear filtering keeps the pixel assets readable when scaled to the game world.
        astronautaTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        oxigenioTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        comidaTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        abrigoTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        // World scale: the astronaut is the reference object for the other assets.
        astronautaSprite = new Sprite(astronautaTexture);
        astronautaSprite.setSize(54f, 54f);

        oxigenioSprite = new Sprite(oxigenioTexture);
        oxigenioSprite.setSize(48f, 48f);

        comidaSprite = new Sprite(comidaTexture);
        comidaSprite.setSize(48f, 48f);

        abrigoSprite = new Sprite(abrigoTexture);
        abrigoSprite.setSize(220f, 130f);

        // Larger default font keeps the HUD legible at the 1280x720 demo resolution.
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
