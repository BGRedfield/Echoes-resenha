package events;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class EventBus {

    public interface EventListener {
        void onEvent(GameEvent event);
    }

    private static EventBus Instance;

    private final ObjectMap<EventType, Array<EventListener>> listeners;

    private EventBus(){
        listeners = new ObjectMap<>();
    }

    public static EventBus getInstance() {
        if (Instance == null) {
            Instance = new EventBus();
        }
        return  Instance;
    }

    public void subscribe(EventType type, EventListener listener) {
        Array<EventListener> list = listeners.get(type);
        if (list == null) {
            list = new Array<>();
            listeners.put(type, list);
        }
        if (!list.contains(listener, true)) {
            list.add(listener);
        }
    }

    public void unsubscribe(EventType type, EventListener listener) {
        Array<EventListener> list = listeners.get(type);
        if (list != null) {
            list.removeValue(listener, true);
        }
    }
    public void publish(GameEvent event) {
        Array<EventListener> list = listeners.get(event.getType());
        if (list != null) {
            Array<EventListener> copy = new Array<>(list);
            for (EventListener listener : copy) {
                listener.onEvent(event);
            }
        }
    }

    public void publish(EventType type) {
        publish(new GameEvent(type));
    }

    public void publish(EventType type, Object data) {
        publish(new GameEvent(type, data));
    }

    public void clear() {
        listeners.clear();
    }
}
