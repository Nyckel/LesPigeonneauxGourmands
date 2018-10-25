import javafx.event.Event;
import javafx.event.EventType;

public class FoodEvent extends Event {

    public static final EventType<FoodEvent> ALL = new EventType<>("ALL");
    public static final EventType<FoodEvent> FOOD_EATEN = new EventType<>(ALL, "FOOD_EATEN");
    public static final EventType<FoodEvent> FOOD_OUTDATED = new EventType<>(ALL, "FOOD_OUTDATED");
    /*
    TODO: Maybe we can add an event FOOD_NOT_FRESH that would be fired when a pigeon discovers that food is not fresh
    the other pigeons become aware of that
    */

    private int pigeonThatAte;

    public FoodEvent(EventType type, int pigeonId) {
        super(type);
        pigeonThatAte = pigeonId;
    }

    public FoodEvent(EventType type) {
        super(type);
    }

    public int getPigeonThatAte() {
        return pigeonThatAte;
    }
}
