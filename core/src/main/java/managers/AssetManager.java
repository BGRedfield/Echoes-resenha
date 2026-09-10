package managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Disposable;

public class AssetManager implements Disposable {

    //textures
    public Texture astronautaTexture;
    public Texture oxigenioTexture;
    public Texture comidaTexture;
    public Texture abrigoTexture;
    public Texture backgroundTexture;

    //Sprite prontos
    public Sprite astronautaSprite;
    public Sprite oxigenioSprite;
    public Sprite comidaSprite;
    public Sprite abrigoSprite;

    //Font
    public BitmapFont font;

    public void load() {
        //carrega todas as texturas
        astronautaTexture = new Texture(Gdx.files.internal("textures/astronauta.png"));
        oxigenioTexture = new Texture(Gdx.files.internal("textures/oxigenio.png"));
        comidaTexture = new Texture(Gdx.files.internal("textures/comida.png"));
        abrigoTexture = new Texture(Gdx.files.internal("textures/abrigo.png"));
        backgroundTexture = new Texture(Gdx.files.internal("textures/marte_background.png"));

        //Configura filtros (melhor qualidade)
        astronautaTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        oxigenioTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        comidaTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        abrigoTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        //Cria Sprites
        astronautaSprite = new Sprite(astronautaTexture);
        astronautaSprite.setSize(32,48);

        oxigenioSprite = new Sprite(oxigenioTexture);
        oxigenioSprite.setSize(32,48);

        comidaSprite = new Sprite(comidaTexture);
        comidaSprite.setSize(40,40);

        abrigoSprite = new Sprite(abrigoTexture);
        abrigoSprite.setSize(320,220); // Tamanho grande e proporcional

        //Fonte
        font = new BitmapFont();
        font.setColor(1,1,1,1);
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
