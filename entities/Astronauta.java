package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import managers.AssetManager;
import physics.PhysicsWorld;

public class Astronauta extends Entidade implements Interagível {

    private float oxigenio = 100f;
    private float energia = 100f;
    private float speed = 180f;
    private final Sprite sprite;
    private boolean protegido = false; //Protegido pelo abrigo
    private boolean viradoEsquerda = false;
    private float tempoVivo = 0f;

    private Body body;
    private PhysicsWorld physicsWorld;
    private final float width = 48f;
    private final float height = 64f;

    public Astronauta(float x, float y, AssetManager assets, PhysicsWorld physicsWorld) {
        super(x, y, 48f, 64f);
        this.physicsWorld = physicsWorld;

        //Textura
        Texture texture = new Texture(Gdx.files.internal("textures/astronauta.png"));
        sprite = new Sprite(texture);
        sprite.setSize(width, height);
        sprite.setPosition(x,y);

        //posicao e bounds
        position.set(x,y);
        bounds.set(x,y,width,height);
        ativo = true;

        body = physicsWorld.createDynamicBody(
            x + width / 2f,
            y + height / 2,
            width,
            height,
            "ASTRONAUTA"
        );
    }

    public void move(float dirX, float dirY, float delta){
        if (!ativo || body == null) return;

        if (dirX < 0) viradoEsquerda = true;
        else if (dirX > 0) viradoEsquerda = false;

        sprite.setFlip(viradoEsquerda, false);

        float velocityX = dirX * speed / PhysicsWorld.PPM;
        float velocityY = dirY * speed / PhysicsWorld.PPM;

        body.setLinearVelocity(velocityX, velocityY);

        Vector2 bodyPos = body.getPosition();
        position.set(
            bodyPos.x * PhysicsWorld.PPM - width / 2f,
            bodyPos.y * PhysicsWorld.PPM - height / 2f
        );

        sprite.setPosition(position.x, position.y);
        bounds.setPosition(position.x, position.y);

        if (dirX != 0 || dirY != 0) {
            energia -= 2.5f * delta;
            if (!protegido) {
                oxigenio -= 1.5f * delta;
            }
        }
    }

    @Override
    public void update(float delta) {
        if (!ativo) return;

        //Consumo de oxigenio
        if (!protegido) {
            oxigenio -= 4.5f * delta;
        }

        //Tempo vivo
        tempoVivo += delta;

        //limites
        if (oxigenio <= 0) {
            oxigenio = 0;
            ativo = false;
        }
        if (energia <= 0) {
            energia = 0;
            ativo = false;
        }
    }
    @Override
    public void render(SpriteBatch batch) {
        if (ativo && sprite != null) {
            sprite.draw(batch);
        }
    }

    @Override
    public void dispose() {}

    public Body getBody() {
        return  body;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getBounds(){
        return bounds;
    }
    public boolean isMoving() {
        return velocity.len() > 5f;
    }

    public void setProtegido(boolean protegido) {
        this.protegido = protegido;
    }





    @Override
    public void interagir(Entidade outra) {}

    @Override
    public boolean podeInteragir() {
        return ativo && oxigenio > 15;
    }
    public float getOxigenio() {return oxigenio;}
    public float getEnergia() { return energia; }
    public float getTempoVivo() { return tempoVivo; }

    public void oxigenioRecuperada(float quantidade) {
        oxigenio = Math.min(100f, oxigenio + quantidade);
    }

    public void energiaRecuperada(float quantidade) {
        oxigenio = Math.min(100f, energia + quantidade);
    }

    public boolean isMorto() {
        return !ativo;
    }


}
