package physics;

import com.badlogic.gdx.physics.box2d.*;
import events.EventBus;
import events.EventType;

public class CollisionListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Object dataA = fixtureA.getBody().getUserData();
        Object dataB = fixtureB.getBody().getUserData();

        //astronauta colidiu com obstaculo
        if (isAstronauta(dataA) && isObstacle(dataB) || isAstronauta(dataB) && isObstacle(dataA)) {
            EventBus.getInstance().publish(EventType.PLAYER_COLLIDED_OBSTACLE);
        }

        //Astronauta X parede
        if (isAstronauta(dataA) && isWall(dataB) || isAstronauta(dataB) && isWall(dataA)) {
            EventBus.getInstance().publish(EventType.PLAYER_COLLIDED_WALL);
        }

        //Astronauta X portal (sensor)
        if (isAstronauta(dataA) && isPortal(dataB) || isAstronauta(dataB) && isPortal(dataA)) {
            EventBus.getInstance().publish(EventType.PORTAL_ENTERED);
        }
    }

    @Override
    public void endContact(Contact contact) {
        //Pose ser usado no futuro para ex sair do portal
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        //Antes da resolução da colisão
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        //Depois da resolução da colisão
    }

    //============Helpers

    private boolean isAstronauta(Object data) {
        return data != null && data.toString().equalsIgnoreCase("ASTRONAUTA");
    }
    private boolean isObstacle(Object data) {
        return data != null && data.toString().equalsIgnoreCase("OBSTACLE");
    }
    private boolean isWall(Object data) {
        return data != null && data.toString().equalsIgnoreCase("WALL");
    }
    private boolean isPortal(Object data) {
        return data != null && data.toString().equalsIgnoreCase("PORTAL");
    }
}
