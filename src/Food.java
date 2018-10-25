import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Timer;
import java.util.TimerTask;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import javafx.scene.Node;

public class Food extends Node {
    private static int incrementId = 0;
    private static Image image;
    private static double imageShiftX;
    private static double imageShiftY;
    private static Image imageOutdated;
    private static double imageOutdatedShiftX;
    private static double imageOutdatedShiftY;
    private int id;
    private int x;
    private int y;
    private boolean fresh;
    private ImageView view;

    public Food(int px, int py) {
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
    public int getFoodId() {return id;}
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
    public synchronized void getEaten(int pigeonId) {
        fireEvent(new FoodEvent(FoodEvent.FOOD_EATEN, pigeonId));
    }

    @Override
    protected NGNode impl_createPeer() {
        return null;
    }

    @Override
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        return null;
    }

    @Override
    protected boolean impl_computeContains(double localX, double localY) {
        return false;
    }

    @Override
    public Object impl_processMXNode(MXNodeAlgorithm alg, MXNodeAlgorithmContext ctx) {
        return null;
    }
}