package util;

import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import detection.Detection;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.util.Arrays;

import static org.opencv.core.CvType.CV_8UC;

public class Preprocessor {
    private ScriptObject[] scripts;
    private String ImgFileLocation;

    public Preprocessor(String imgFile){
        this.ImgFileLocation = imgFile;

        // TODO: detect QR here to get number of pages per script etc.
        scripts = new ScriptObject[10];

    }



    public Pair[] projectPoints(Pair[] points, double angle, double x, double y){

        double a = - Math.toRadians(angle);

        points = Arrays.stream(points).map( p -> {
                return new Pair(
                        (float) ((p.getX() - x) * Math.cos(a) + x - (p.getY() - y) * Math.sin(a)),
                        (float) ((p.getY() - y) * Math.cos(a) + y + (p.getX() - x) * Math.sin(a))
                );
            }
        ).toArray(Pair[]::new);

        for (Pair p : points)
            System.out.println(p);

        return points;
    }


    public BufferedImage fasterImageRead(String filename) throws IOException {
        // TODO: investigate further
        DataInputStream datainputstream = new DataInputStream(getClass().getResourceAsStream("j180.jpg"));
        System.out.println(datainputstream);
        byte abyte0[] = new byte[datainputstream.available()];
        datainputstream.readFully(abyte0);
        datainputstream.close();
        return  (BufferedImage) (Toolkit.getDefaultToolkit().createImage(abyte0));
    }

    public void process(long t) throws IOException {
        // TODO: modularise, delegate into other methods
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Straighten straightener = new Straighten();
        BufferedImage bi = ImageIO.read(new File(ImgFileLocation));
        System.out.println(bi.getType());

        System.out.println("Time to read in image time " + (System.nanoTime() - t )/1000000000.0 + "\n");


        Result result = Detection.detectQRCode(bi);
        System.out.println("Time after QR detection " + (System.nanoTime() - t )/1000000000.0 + "\n");

        if (result == null){
            // TODO: handle: failure to detect QR code
            System.out.println("QR CODE NOT FOUND");
            System.exit(0);
        }

        System.out.println("QR data: " + result.getText() + "\n");
        Pair[] QRCodeCornerCoordinates = new Pair[3];

        for (int i = 0; i < 3; i++) {
            QRCodeCornerCoordinates[i] = new Pair(result.getResultPoints()[i].getX(), result.getResultPoints()[i].getY());
            System.out.println(QRCodeCornerCoordinates[i]);
        }

        double angle = calculateRotationAngle(QRCodeCornerCoordinates);

        System.out.println("converting to mat from BI so that we can straighten.." + (System.nanoTime() - t )/1000000000.0 + "\n");
        Mat image = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC1);
        image.put(0,0, ((DataBufferByte) bi.getRaster().getDataBuffer()).getData());
        System.out.println("after MAT conversion" + (System.nanoTime() - t )/1000000000.0+ "\n");

        QRCodeCornerCoordinates =  projectPoints(QRCodeCornerCoordinates, angle, image.cols()/2, image.rows()/2);

        QRCode qrcode = new QRCode(QRCodeCornerCoordinates, result);

        calculateScalingFactor(qrcode);

        image = straightener.straightenImage(image, angle, qrcode);

        Imgproc.threshold(image, image, 190, 255, Imgproc.THRESH_BINARY_INV);
//        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
//        Imgproc.cvtColor(image, image, Imgproc.COLOR_GRAY2BGR);


        System.out.println("after grayscale and straightening: converting from mat to BI" + (System.nanoTime() - t )/1000000000.0+ "\n");

        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(".jpg", image, mob);
        bi = ImageIO.read(new ByteArrayInputStream(mob.toArray()));

        System.out.println("after BI conversion" + (System.nanoTime() - t )/1000000000.0+ "\n");

        ScriptObject script = new ScriptObject(bi);
        script.setQrCodeProperties(qrcode);
        scripts[0] = script;
   }

    public void calculateScalingFactor(QRCode qr) {
        ResultPoint[] points = qr.getResult().getResultPoints();

        float baseXDistance = (float) (707 - 471);
        float baseYDistance = (float) (984 - 747);

        float x = ResultPoint.distance(points[2], points[1]) / baseXDistance;
        float y = ResultPoint.distance(points[0], points[1]) / baseYDistance;

        qr.setScalingFactor(new Pair(x, y));
        System.out.println(qr.getScalingFactor());
    }

    private double calculateRotationAngle(Pair[] points){

        /*
            Calculate the angle of rotation by taking looking at the triangle formed by the skew QR code.
            tan(angle) = opposite / adjacent
         */
        double deltaX = (points[2].getX() - points[1].getX());
        double deltaY = (points[2].getY() - points[1].getY());
        System.out.println(deltaX + " " + deltaY);
        double angle = Math.atan2(deltaY, deltaX);
        angle = Math.toDegrees(angle);
        System.out.println(angle);

        if (points[1].getY() > points[2].getY() && points[1].getY() > points[0].getY())
            /*
                If the page is upside down and needs a rotation of > 180.
                Add 180 because tan only gives you the acute rotation required.
             */
            angle += 180;

        else if (points[1].getY() > points[0].getY())
            /*
                If the page is upside down but needs a rotation of < 180.
                Subtract 180 from the angle because tan gives you the acute angle of rotation required.
             */
            angle = 180 - angle;

        System.out.println("Rotation angle: " + (angle));
        return angle;
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

}
