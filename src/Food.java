import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Timer;
import java.util.TimerTask;

public class Food {


    private static int incrementId = 0;
    private static Image image;
    private static double imageShiftX;
    private static double imageShiftY;
    private static Image imageOutdated;
    private static double imageOutdatedShiftX;
    private static double imageOutdatedShiftY;

    private int id;
    private double x;
    private double y;
    private boolean fresh;
    private ImageView view;

    public Food(double px, double py) {
        id = incrementId++;
        x = px;
        y = py;
        fresh = true;

        System.out.println("Creating food at position " + px + ";" + py);

        view = new ImageView();
        view.setImage(image);
        view.setX(x - imageShiftX);
        view.setY(y - imageShiftY);
        view.setFitHeight(image.getHeight()/2);
        view.setPreserveRatio(true);

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setFresh(false);
                timer.cancel();
            }
        }, 2000);
    }

    public void setFresh(Boolean f) {
        fresh = f;
        if (fresh)
        {
            view.setImage(image);
            view.setX(x - imageShiftX);
            view.setY(y - imageShiftY);
            view.setFitHeight(image.getHeight()/2);
        }
        else {
            view.setImage(imageOutdated);
            view.setX(x - imageOutdatedShiftX);
            view.setY(y - imageOutdatedShiftY);
            view.setFitHeight(imageOutdated.getHeight()/2);
        }
    }

    public ImageView getView() {return view;}
    public int getId() {return id;}
    public double getX() {return x;}
    public double getY() {return y;}
    public boolean isFresh() {return fresh;}

    public static void setImages(Image img, Image imgO) {
        image = img;
        imageOutdated = imgO;

        imageShiftX = img.getWidth() / 2;
        imageShiftY = img.getHeight() / 2;
        imageOutdatedShiftX = imgO.getWidth() / 2;
        imageOutdatedShiftY = imgO.getHeight() / 2;
    }
}
