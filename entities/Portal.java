package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import physics.PhysicsWorld;

public class Portal {

    private final Vector2 position;
    private final Sprite sprite;
    private final Rectangle bounds;
    private Body body;
    private final String targetPhase;
    private final float width;
    private final float height;

    public Portal(float x, float y, Texture texture, String targetPhase, PhysicsWorld physicsWorld) {
        this.position = new Vector2(x, y);
        this.sprite = new Sprite(texture);
        this.targetPhase = targetPhase;

        this.width = 96f;
        this.height = 96f;

        sprite.setSize(width, height);
        sprite.setPosition(x,y);

        bounds = new Rectangle(x,y,width,height);

        body = physicsWorld.createStaticBody(
            x + width / 2f,
            y + height / 2f,
            width,
            height,
            "PORTAL"
        );
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public String getTargetPhase() {
        return targetPhase;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Body getBody() {
        return body;
    }

    public void dispose(){

    }














}
