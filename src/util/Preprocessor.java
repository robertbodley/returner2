package util;

import com.google.zxing.Result;
import detection.Detection;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class Preprocessor {
    private ScriptObject[] scripts;
    private int numberOfPages;
    private int[] maxMarksPerQuestion;
    private String ImgFileLocation;



    public Preprocessor(String imgFile){
        this.ImgFileLocation = imgFile;

        // TODO: detect QR here to get number of pages per script etc.
        scripts = new ScriptObject[10];

    }

    public void process() throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Straighten straightener = new Straighten();
        BufferedImage bi = ImageIO.read(new File(ImgFileLocation));

        Result result = Detection.detectQRCode(bi);

        if (result == null){
            // TODO: handle: failure to detect QR code
        }

        System.out.println(result.getText());
        Pair[] QRCodeCornerCoordinates = new Pair[3];

        for (int i = 0; i < 3; i++) {
            QRCodeCornerCoordinates[i] = new Pair(result.getResultPoints()[i].getX(), result.getResultPoints()[i].getY());
            System.out.println(i + QRCodeCornerCoordinates[i].toString());
        }

        double angle = calculateRotationAngle(QRCodeCornerCoordinates);

        Mat image = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();

        image.put(0,0, data);

        image = straightener.straightenImage(image, angle);
        Imgproc.threshold(image, image, 127, 255, Imgproc.THRESH_BINARY_INV);
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);

//        Imgcodecs.imwrite("straightImage.jpg", image);

        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(".jpg", image, mob);
        bi = ImageIO.read(new ByteArrayInputStream(mob.toArray()));

        ScriptObject script = new ScriptObject(bi);
        script.setQrCodeProperties(new QRCode(QRCodeCornerCoordinates, result));
        scripts[0] = script;
   }

    private double calculateRotationAngle(Pair[] points){

        double angle = 0.0;

        double deltaX = Math.abs(points[0].getX() - points[1].getX());
        double deltaY = Math.abs(points[0].getY() - points[1].getY());

        angle = Math.atan2(deltaY, deltaX);
        angle = Math.toDegrees(angle);
        System.out.println("Rotation angle detected: " + Math.round(angle));

        if (points[1].getY() > points[0].getY() && points[1].getY() > points[2].getY())

            // upside down and rotated > 180. ie 183 degrees
            angle += 180;

        else if (points[1].getY() > points[2].getY())
            // upside dowm < 180. ie 177
            angle = 180 - angle;

        System.out.println("Rotation angle detected: " + Math.round(angle));
        return Math.round(angle);
    }

    public void splitScripts(String PDFFilePath){
        // TODO: detect and decode a QR code to determine number of pages to split by

        /*
        split scripts and store only front page/cover of each script
         */

    }

    public ScriptObject[] getScripts(){
        // intentionally return actual reference to object
        return scripts;
    }

    public ScriptObject getScript(int i){
        return scripts[i];
    }

    public void setNumberOfPages(int numberOfPages){
        this.numberOfPages = numberOfPages;
    }

    public void setMaxMarksPerQuestion(int[] maxMarksPerQuestion){
        this.maxMarksPerQuestion = maxMarksPerQuestion;
    }


}
