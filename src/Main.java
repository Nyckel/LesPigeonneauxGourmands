import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.Random;

public class Main extends Application {

    private final static int WINDOW_WIDTH = 500;
    private final static int WINDOW_HEIGHT = 500;
    private final Object lockMonitor = new Object();
    private ArrayList<Pigeon> pigeons;
    private ArrayList<Food> foods;
    private ArrayList<Rock> rocks;

    public Main() {
        pigeons = new ArrayList<Pigeon>();
        foods = new ArrayList<Food>();
        rocks = new ArrayList<Rock>();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Les Pigeonnaux Gourmands");

        Pane layout = new Pane();
        layout.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        String imagePath = "file:resources/pigeon_right.gif";
        Image image = new Image(imagePath);
        Pigeon.setImage(image);

        String imagePathFood = "file:resources/food.gif";
        Image imageFood = new Image(imagePathFood);
        String imageOutdatedPathFood = "file:resources/foodOutdated.gif";
        Image imageOutdatedFood = new Image(imageOutdatedPathFood);
        Food.setImages(imageFood, imageOutdatedFood);

        String imagePathRock = "file:resources/rock.png";
        Image imageRock = new Image(imagePathRock);
        Rock.setImage(imageRock);

        Scene scene = new Scene(layout, Color.CADETBLUE);
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    addPigeon(layout, (int) mouseEvent.getX(), (int)mouseEvent.getY());
                } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    addFood(layout, (int) mouseEvent.getX(), (int) mouseEvent.getY());
                }
            }
        });

        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.SPACE) {
                    if (!(rocks.size() < 2))
                        System.out.println("Two rocks are already on the screen");
                    else
                        addRock(layout);
                }
            }
        });

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                synchronized (lockMonitor) {
                    System.out.println("Stage is closing");
                    for (Pigeon pig : pigeons) {
                        pig.stopRunning();
                    }
                }
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void addPigeon(Pane layout, int x, int y) {
        Pigeon pig = new Pigeon(x, y);

        synchronized (lockMonitor) {
            layout.getChildren().add(pig.getView());
            pig.start();
            pigeons.add(pig);
        }
    }

    private void addFood(Pane layout, int x, int y) {
        Food f = new Food(x, y);
        layout.getChildren().add(f.getView());
        foods.add(f);
        // Alerts pigeons

        f.addEventHandler(FoodEvent.FOOD_EATEN, new EventHandler<FoodEvent>() {
            @Override
            public void handle(FoodEvent fe) {
                // NotifyFoodEaten
            }
        });

        synchronized (lockMonitor) {
            for (Pigeon p : pigeons) {
//                p.notifyFoodPop(food);
            }
        }
    }

    private void addRock(Pane layout)
    {
        double x = randomNumberInRange(10, 490);
        double y = randomNumberInRange(10, 490);
        Rock rock = new Rock(x, y);

        // EventListener pour l'affichage et le stockage de la pierre
        rock.addEventHandler(RockEvent.CREATION, new EventHandler<RockEvent>() {
            @Override
            public void handle(RockEvent rc) {
                System.out.println("Affichage du rock");
                layout.getChildren().add(rock.getView());
                rocks.add(rock);
            }
        });

        rock.addEventHandler(RockEvent.TIMEOUT, new EventHandler<RockEvent>() {
            @Override
            public void handle(RockEvent rs) {
                System.out.println("Supression du rock");
                Platform.runLater(()-> {
                    layout.getChildren().remove(rock.getView());
                });
                rocks.remove(rock);
            }
        });

        rock.run(); // lancement du thread
    }

    private static int randomNumberInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
