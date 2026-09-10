package events;

public class GameEvent {

    private final EventType type;
    private final Object data;

    public GameEvent(EventType type) {
        this.type = type;
        this.data = null;
    }

    public GameEvent(EventType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public EventType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    @SuppressWarnings("unchecked")
    public <T> T getData(Class<T> clazz) {
        if (data != null && clazz.isInstance(data)) {
            return (T) data;
        }
        return null;
    }
}
