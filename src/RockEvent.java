import javafx.event.Event;
import javafx.event.EventType;

public class RockEvent extends Event{
    public static final EventType<RockEvent> ALL = new EventType<>("ALL");
    public static final EventType<RockEvent> TIMEOUT = new EventType<>(ALL, "TIMEOUT");
    public static final EventType<RockEvent> CREATION = new EventType<>(ALL, "CREATION");

    public RockEvent(EventType type) {
        super(type);
    }
}
