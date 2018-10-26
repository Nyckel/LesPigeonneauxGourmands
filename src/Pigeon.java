import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Pigeon extends Thread {

    private static int incrementId = 0;
    private static Image imageLeft;
    private static Image imageRight;
    private static double imageShiftX;
    private static double imageShiftY;
    private static final int speed = 5;
    private static int MAXPOSITIONX;
    private static int MAXPOSITIONY;


    private int id;
    private double x;
    private double y;
    private ImageView view;
    private volatile Boolean shouldRun;
    private ArrayList<Food> foods;
    private Food currentObjective;
    private boolean directionLeft;
    private Rock rock;

    public Pigeon(double px, double py) {
        id = incrementId++;
        rock = null;
        x = px;
        y = py;
        shouldRun = true;
        foods = new ArrayList<Food>();
        currentObjective = null;
        directionLeft = true;
        System.out.println("Creating pigeon at position " + px + ";" + py + "/" + x + " ; " + y);

        view = new ImageView();
        view.setImage(imageLeft);
        view.setX(x - imageShiftX);
        view.setY(y - imageShiftY);
        view.setFitHeight(imageLeft.getHeight());
        view.setPreserveRatio(true);
    }

    public static void setMaxPositions(int width, int height) {
        MAXPOSITIONX = width;
        MAXPOSITIONY = height;
    }

    public void run() {
        while(shouldRun) {
            // Check for danger

            if ((rock != null) && (Math.sqrt(Math.pow(x - rock.getX(), 2) + Math.pow(y - rock.getY(), 2)) <= rock.getRadius())) {
                runAwayFromRock();
                updateViewPosition();
            }
            else if (currentObjective != null)
            {
//                System.out.println("Pigeon " + id + " alive at " + x + " ; " + y);
//                System.out.println("Objective at " + currentObjective.getX() + " ; " + currentObjective.getY());
                moveTowardObjective();
                updateViewPosition();
            }

            try {
                Thread.sleep(50);
            } catch(InterruptedException e) {
                System.out.println("Was interrupted");
            }
        }

    }

    private void moveTowardObjective() {
        if (Math.sqrt(Math.pow(x - currentObjective.getX(), 2) + Math.pow(y - currentObjective.getY(), 2)) < speed)
        {
            x = currentObjective.getX();
            y = currentObjective.getY();
            if (currentObjective.isFresh())
                currentObjective.getEaten(id);
            else
                currentObjective.getOutdated();
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
                    if (directionLeft) {
                        directionLeft = false;
                        view.setImage(imageRight);
                    }
                    x += Math.cos(angle) * speed;
                    y += Math.sin(angle) * speed;
                }
                else
                {
                    if (!directionLeft) {
                        directionLeft = true;
                        view.setImage(imageLeft);
                    }
                    x -= Math.cos(angle) * speed;
                    y -= Math.sin(angle) * speed;
                }
            }
        }
    }

    private void runAwayFromRock() {
        if (rock.getX() == x)
        {
            if (rock.getY() > y)
                y = Math.min(Math.max(0, y - speed), MAXPOSITIONY);
            else
                y = Math.min(Math.max(0, y + speed), MAXPOSITIONY);
        }
        else {
            double angle = Math.atan((rock.getY()-y)/(rock.getX()-x));

            if (rock.getX() > x)
            {
                if (! directionLeft) {
                    directionLeft = true;
                    view.setImage(imageLeft);
                }
                x = Math.min(Math.max(0, x - Math.cos(angle) * speed), MAXPOSITIONX);
                y = Math.min(Math.max(0, y - Math.sin(angle) * speed), MAXPOSITIONY);
            }
            else
            {
                if (directionLeft) {
                    directionLeft = false;
                    view.setImage(imageRight);
                }
                x = Math.min(Math.max(0, x + Math.cos(angle) * speed), MAXPOSITIONX);
                y = Math.min(Math.max(0, y + Math.sin(angle) * speed), MAXPOSITIONY);
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

    public static void setImages(Image imgLeft, Image imgRight) {
        imageLeft = imgLeft;
        imageRight = imgRight;
        imageShiftX = imgLeft.getWidth() / 2;
        imageShiftY = imgLeft.getHeight() / 2;
    }

    public synchronized void notifyFoodPop(Food f) {
//        System.out.println("Pigeon_"+ id + " food pop " + f.getFoodId());
        foods.add(f);
        searchClosestFood();
    }

    public synchronized void notifyFoodEaten(int pid) {
//        System.out.println("Pigeon_"+ id + " food eaten " + pid);
        removeFood(pid);
        searchClosestFood();
    }

    public synchronized void notifyFoodOutdated(int pid) {
//        System.out.println("Pigeon_"+ id + " food rotten " + pid);
        removeFood(pid);
        searchClosestFood();
    }

    private synchronized void removeFood(int id) {
        for (Food e: foods) {
            if (e.getFoodId() == id) {
                foods.remove(e);
                break;
            }
        }
    }

    private synchronized void searchClosestFood() {
        Food f = null;
        double distanceMin = Double.MAX_VALUE;
        double distance;

        for (Food e : foods) {
            distance = Math.sqrt(Math.pow(x - e.getX(), 2) + Math.pow(y - e.getY(), 2));
            if (distance < distanceMin) {
                f = e;
                distanceMin = distance;
            }
        }
        currentObjective = f;
//        System.out.println("Pigeon_"+ id + " search..." + currentObjective);
        if (currentObjective == null) {
            System.out.println(foods.size());
        }

    }

    public synchronized void notifyNewRock(Rock ro) {
        rock = ro;
    }

    public synchronized void notifyClearRock() {
        rock = null;
    }
}
