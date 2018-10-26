import javafx.event.Event;
import javafx.event.EventType;

public class RockEvent extends Event{
    public static final EventType<RockEvent> ROCK_ALL = new EventType<>("ROCK_ALL");
    public static final EventType<RockEvent> TIMEOUT = new EventType<>(ROCK_ALL, "TIMEOUT");
    public static final EventType<RockEvent> CREATION = new EventType<>(ROCK_ALL, "CREATION");

    public RockEvent(EventType type) {
        super(type);
    }
}
