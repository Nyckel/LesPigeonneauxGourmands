import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Pigeon extends Thread {

    private static int incrementId = 0;
    private static Image image;
    private static double imageShiftX;
    private static double imageShiftY;
    private static final int speed = 5;

    private int id;
    private double x;
    private double y;
    private ImageView view;
    private volatile Boolean shouldRun;
    private ArrayList<Food> foods;
    private Food currentObjective;

    public Pigeon(double px, double py) {
        id = incrementId++;
        x = px;
        y = py;
        shouldRun = true;
        foods = new ArrayList<Food>();
        currentObjective = null;
        System.out.println("Creating pigeon at position " + px + ";" + py + "/" + x + " ; " + y);


        view = new ImageView();
        view.setImage(image);
        view.setX(x - imageShiftX);
        view.setY(y - imageShiftY);
        view.setFitHeight(image.getHeight());
        view.setPreserveRatio(true);
    }

    public void run() {
        while(shouldRun) {

            // Check for danger

            if (currentObjective != null)
            {
                System.out.println("Pigeon " + id + " alive at " + x + " ; " + y);
                System.out.println("Objective at " + currentObjective.getX() + " ; " + currentObjective.getY());
                // Move towards food
                if (Math.sqrt(Math.pow(x - currentObjective.getX(), 2) + Math.pow(y - currentObjective.getY(), 2)) < speed)
                {
                    x = currentObjective.getX();
                    y = currentObjective.getY();
                    currentObjective.getEaten(id);
                    searchClosestFood();
                }
                else
                {
                    if (currentObjective.getX() == x)
                    {
                        if (currentObjective.getY() > y)
                            y += speed;
                        else
                            y -= speed;
                    }
                    else {
                        double angle = Math.atan((currentObjective.getY()-y)/(currentObjective.getX()-x));

                        if (currentObjective.getX() > x)
                        {
                            x += Math.cos(angle) * speed;
                            y += Math.sin(angle) * speed;
                        }
                        else
                        {
                            x -= Math.cos(angle) * speed;
                            y -= Math.sin(angle) * speed;
                        }
                    }
                }
                updateViewPosition();
            }

            try {
                Thread.sleep(50);
            } catch(InterruptedException e) {
                System.out.println("Was interrupted");
            }
        }

    }

    private void updateViewPosition() {
        view.setX(x - imageShiftX);
        view.setY(y - imageShiftY);
    }

    public void stopRunning() {
        shouldRun = false;
    }

    public ImageView getView() {
        return view;
    }

    public static void setImage(Image img) {
        image = img;
        imageShiftX = img.getWidth() / 2;
        imageShiftY = img.getHeight() / 2;
    }

    public void notifyFoodPop(Food f) {
        foods.add(f);
        searchClosestFood();
    }

    public void notifyFoodEaten(int id) {
        removeFood(id);
        searchClosestFood();
    }

    public void notifyFoodOutdated(int id) {
        removeFood(id);
        searchClosestFood();
    }

    private void removeFood(int id) {
        for (Food e: foods)
        {
            if (e.getFoodId() == id)
            {
                foods.remove(e);
                break;
            }

        }
    }

    private void searchClosestFood() {
        Food f = null;
        double distanceMin = Double.MAX_VALUE;
        double distance;

        for (Food e: foods) {
            distance = Math.sqrt(Math.pow(x - e.getX(), 2) + Math.pow(y - e.getY(), 2));
            if (distance < distanceMin) {
                f = e;
                distanceMin = distance;
            }
        }
        currentObjective = f;
    }
}
