import com.google.zxing.Result;
import detection.Detection;
import org.junit.Assert;
import org.junit.Test;
import util.Pair;
import util.Preprocessor;
import util.ScriptObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Oliver on 2017/10/05.
 */
public class TestRotationAngle {
    String testLocation ="testResources/rotation/";
    double error = 0.5;


    @Test
    public void Test3degreeRotation() throws IOException {
        String path = testLocation + "3deg.jpg";

        BufferedImage bi = ImageIO.read(new File(path));
        Preprocessor pre = new Preprocessor(path);
        Result r = Detection.detectQRCode(bi);

        Pair[] QRCodeCornerCoordinates = new Pair[3];
        for (int i = 0; i < 3; i++) {
            QRCodeCornerCoordinates[i] = new Pair(r.getResultPoints()[i].getX(), r.getResultPoints()[i].getY());
        }

        double angle = pre.calculateRotationAngle(QRCodeCornerCoordinates);

        Assert.assertEquals(  3.0, angle, error);
    }

    @Test
    public void Testneg3degreeRotation() throws IOException {
        String path = testLocation + "-3deg.jpg";

        BufferedImage bi = ImageIO.read(new File(path));
        Preprocessor pre = new Preprocessor(path);
        Result r = Detection.detectQRCode(bi);

        Pair[] QRCodeCornerCoordinates = new Pair[3];
        for (int i = 0; i < 3; i++) {
            QRCodeCornerCoordinates[i] = new Pair(r.getResultPoints()[i].getX(), r.getResultPoints()[i].getY());
        }

        double angle = pre.calculateRotationAngle(QRCodeCornerCoordinates);
        Assert.assertEquals( -3.0, angle, error);
    }
    @Test
    public void Test45degreeRotation() throws IOException {
        String path = testLocation + "45deg.jpg";

        BufferedImage bi = ImageIO.read(new File(path));
        Preprocessor pre = new Preprocessor(path);
        Result r = Detection.detectQRCode(bi);

        Pair[] QRCodeCornerCoordinates = new Pair[3];
        for (int i = 0; i < 3; i++) {
            QRCodeCornerCoordinates[i] = new Pair(r.getResultPoints()[i].getX(), r.getResultPoints()[i].getY());
        }

        double angle = pre.calculateRotationAngle(QRCodeCornerCoordinates);
        Assert.assertEquals(45.0, angle, error);
    }

    @Test
    public void Test177degreeRotation() throws IOException {
        String path = testLocation + "177deg.jpg";

        BufferedImage bi = ImageIO.read(new File(path));
        Preprocessor pre = new Preprocessor(path);
        Result r = Detection.detectQRCode(bi);

        Pair[] QRCodeCornerCoordinates = new Pair[3];
        for (int i = 0; i < 3; i++) {
            QRCodeCornerCoordinates[i] = new Pair(r.getResultPoints()[i].getX(), r.getResultPoints()[i].getY());
        }

        double angle = pre.calculateRotationAngle(QRCodeCornerCoordinates);
        Assert.assertEquals(177.0, angle, error);
    }

    @Test
    public void Test180degreeRotation() throws IOException {
        String path = testLocation + "180deg.jpg";

        BufferedImage bi = ImageIO.read(new File(path));
        Preprocessor pre = new Preprocessor(path);
        Result r = Detection.detectQRCode(bi);

        Pair[] QRCodeCornerCoordinates = new Pair[3];
        for (int i = 0; i < 3; i++) {
            QRCodeCornerCoordinates[i] = new Pair(r.getResultPoints()[i].getX(), r.getResultPoints()[i].getY());
        }

        double angle = pre.calculateRotationAngle(QRCodeCornerCoordinates);
        Assert.assertEquals(180, angle, error);
    }

    @Test
    public void Test183degreeRotation() throws IOException {
        String path = testLocation + "183deg.jpg";

        BufferedImage bi = ImageIO.read(new File(path));
        Preprocessor pre = new Preprocessor(path);
        Result r = Detection.detectQRCode(bi);

        Pair[] QRCodeCornerCoordinates = new Pair[3];
        for (int i = 0; i < 3; i++) {
            QRCodeCornerCoordinates[i] = new Pair(r.getResultPoints()[i].getX(), r.getResultPoints()[i].getY());
        }

        double angle = pre.calculateRotationAngle(QRCodeCornerCoordinates);
        Assert.assertEquals(-177, angle, error);
    }

}
