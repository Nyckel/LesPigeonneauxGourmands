import javafx.event.Event;
import javafx.event.EventType;

public class FoodEvent extends Event {

    public static final EventType<FoodEvent> FOOD_ALL = new EventType<>("FOOD_ALL");
    public static final EventType<FoodEvent> FOOD_EATEN = new EventType<>(FOOD_ALL, "FOOD_EATEN");
    public static final EventType<FoodEvent> FOOD_OUTDATED = new EventType<>(FOOD_ALL, "FOOD_OUTDATED");


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
