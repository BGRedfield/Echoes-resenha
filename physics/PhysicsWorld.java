package physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class PhysicsWorld {

    //Gravidades realistas
    public static final float GRAVITY_LUA = -1.62f;
    public static final float GRAVITY_MARTE = -3.71f;

    private World world;
    private float currentGravity = GRAVITY_MARTE;

    //ESCALA: 1 metro no box2d = 32 pixels
    public static final float PPM = 32f; //Pixel per meter

    public PhysicsWorld() {
        world = new World(new Vector2(0, currentGravity), true);
        world.setContactListener(new CollisionListener());
    }

    public void update(float delta) {
        world.step(delta,6,2);
    }

    public World getWorld() {
        return world;
    }

    public void setGravity(float gravity) {
        this.currentGravity = gravity;
        world.setGravity(new Vector2(0,gravity));
    }

    public float getCurrentGravity() {
        return  currentGravity;
    }

    public void setPhaseGravity(String phase) {
        if ("LUA".equalsIgnoreCase(phase)) {
            setGravity(GRAVITY_LUA);
        } else {
            setGravity(GRAVITY_MARTE);
        }
    }

    //==========CRIAÇÂO DE CORPOS

    public Body createDynamicBody(float x,float y, float width, float height, Object userData) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / PPM, y / PPM);
        bodyDef.fixedRotation = true;

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((width / 2f) / PPM, (height / 2f) / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.1f;

        body.createFixture(fixtureDef);
        body.setUserData(userData);

        shape.dispose();
        return body;
    }

    public Body createStaticBody(float x,float y, float width, float height, Object userData) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x / PPM, y / PPM);

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((width / 2f) / PPM, (height / 2f) / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.4f;

        body.createFixture(fixtureDef);
        body.setUserData(userData);

        shape.dispose();
        return body;
    }

    public Body createSensorBody(float x,float y, float width, float height, Object userData) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x / PPM, y / PPM);

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((width / 2f) / PPM, (height / 2f) / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef);
        body.setUserData(userData);

        shape.dispose();
        return body;
    }

    public void destroyBody(Body body) {
        if (body != null) {
            world.destroyBody(body);
        }
    }

    public void dispose(){
        if (world != null) {
            world.dispose();
        }
    }
}
