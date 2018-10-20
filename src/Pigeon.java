import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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


    public Pigeon(double px, double py) {
        id = incrementId++;
        x = px;
        y = py;
        shouldRun = true;
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
//            System.out.println("Pigeon " + id + " alive at " + x + " ; " + y);
            // Check for new food at interval ...
            // Then move towards it

//            updateViewPosition();
            try {
                Thread.sleep(500);
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
}
