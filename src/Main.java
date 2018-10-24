import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;

public class Main extends Application {

    private final static int WINDOW_WIDTH = 500;
    private final static int WINDOW_HEIGHT = 500;
    private ArrayList<Pigeon> pigeons;
    private ArrayList<Food> foods;

    public Main() {
        pigeons = new ArrayList<Pigeon>();
        foods = new ArrayList<Food>();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Les Pigeonnaux Gourmands");

        Pane layout = new Pane();
        layout.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        String imagePath = "file:resources/pigeon.gif";
        Image image = new Image(imagePath);
        Pigeon.setImage(image);

        String imagePathFood = "file:resources/food.gif";
        Image imageFood = new Image(imagePathFood);
        String imageOutdatedPathFood = "file:resources/foodOutdated.gif";
        Image imageOutdatedFood = new Image(imageOutdatedPathFood);
        Food.setImages(imageFood, imageOutdatedFood);

        Scene scene = new Scene(layout, Color.CADETBLUE);
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    Pigeon pig = new Pigeon(mouseEvent.getX(), mouseEvent.getY());
                    layout.getChildren().add(pig.getView());
                    pig.start();
                    pigeons.add(pig);
                } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    Food f = new Food(mouseEvent.getX(), mouseEvent.getY());
                    layout.getChildren().add(f.getView());
                    foods.add(f);
                    // Alerts pigeons
                }
            }
        });

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Stage is closing");
                for (Pigeon pig : pigeons) {
                    pig.stopRunning();
                }
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
