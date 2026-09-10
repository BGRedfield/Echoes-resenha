package input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import events.EventBus;
import events.EventType;

public class GameInputProcessor  implements InputProcessor {

    private final Vector2 direction = new Vector2();
    private boolean up, down, left, right;
    private boolean pausePressed = false;

    public Vector2 getDirection() {
        direction.set(0, 0);

        if (up) direction.y += 1;
        if (down) direction.y -= 1;
        if (left) direction.x -= 1;
        if (right) direction.x += 1;

        return direction.nor();
    }

    public boolean isMoving() {
        return up || down || left || right;
    }

    public boolean isPauseJustPressed() {
        boolean wasPressed = pausePressed;
        pausePressed = false;
        return wasPressed;
    }
    //=============TECLADO
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
            case Input.Keys.UP:
                up = true;
                break;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                down = true;
                break;
            case Input.Keys.A:
            case Input.Keys.LEFT:
                left = true;
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                right = true;
                break;
            case Input.Keys.ESCAPE:
                pausePressed = true;
                break;
            case Input.Keys.SPACE:
                EventBus.getInstance().publish(EventType.PLAYER_MOVED, "SPACE");
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
            case Input.Keys.UP:
                up = false;
                break;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                down = false;
                break;
            case Input.Keys.A:
            case Input.Keys.LEFT:
                left = false;
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                right = false;
                break;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    //====================MOUSE / TOUCH (NAUM VAI SER USADO AGR)

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
