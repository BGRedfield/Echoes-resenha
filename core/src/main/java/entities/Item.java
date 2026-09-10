package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import managers.AssetManager;

public class Item extends Entidade {
    private final Sprite sprite;
    private final String tipo;
    private boolean coletado = false;

    public Item(float x, float y, String tipo, AssetManager assets) {
        super(x, y, tipo.equals("abrigo") ? 320 : 32, tipo.equals("abrigo") ? 200 : 32);
        this.tipo = tipo;

        String fileName;
        switch (tipo) {
            case "oxigenio":
                fileName = "oxigenio.png";
                break;
            case "comida":
                fileName = "comida.png";
                break;
            case "abrigo":
                fileName = "abrigo.png";
                break;
            default:
                fileName = "oxigenio.png";
                break;
        }

        Texture texture = new Texture(Gdx.files.internal("textures/" + fileName));
        sprite = new Sprite(texture);

        if (tipo.equals("abrigo")) {
            sprite.setSize(320, 420); // Ajuste de tamanho do abrigo
        } else {
            sprite.setSize(32, 32);
        }
    }

    @Override
    public void update(float delta) {}

    @Override
    public void render(SpriteBatch batch) {
        if (!coletado) {
            sprite.setPosition(position.x, position.y);
            sprite.draw(batch);
        }
    }

    @Override
    public void dispose() {
        // Textura gerenciada
    }

    public String getTipo() { return tipo; }
    public boolean isColetado() { return coletado; }
    public void coletar() { this.coletado = true; }
}
