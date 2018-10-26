import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class Rock extends Node implements Runnable {

    private static Image image;
    private static double imageShiftX;
    private static double imageShiftY;
    private ImageView view;
    private double x, y;
    private int radius;

    public Rock(double px, double py) {
        x = px;
        y = py;
        radius = 20;
        System.out.println("Creating rock at position " + px + ";" + py + "/" + x + " ; " + y);

        view = new ImageView();
        view.setImage(image);
        view.setX(x - imageShiftX);
        view.setY(y - imageShiftY);
        view.setFitHeight(image.getHeight() / 2);
        view.setPreserveRatio(true);
    }

    public ImageView getView() {
        return view;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public static void setImage(Image img) {
        image = img;
        imageShiftX = img.getWidth() / 2;
        imageShiftY = img.getHeight() / 2;
    }

    public synchronized void run() {
        Thread r = new Thread(this);
        fireEvent(new RockEvent(RockEvent.CREATION));

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                fireEvent(new RockEvent(RockEvent.TIMEOUT));
                timer.cancel();
            }
        }, 3000);
    }
}