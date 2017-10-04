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


public class Preprocessor {
    private ScriptObject[] scripts;
    private String ImgFileLocation;

    public Preprocessor(String imgFile){
        this.ImgFileLocation = imgFile;

        scripts = new ScriptObject[10];

    }



    public Pair[] projectPoints(Pair[] points, double angle, double x, double y){

        double a =  Math.toRadians(- angle);

        points = Arrays.stream(points).map( p -> {
                return new Pair(
                        (float) ((p.getX() - x) * Math.cos(a) + x - (p.getY() - y) * Math.sin(a)),
                        (float) ((p.getY() - y) * Math.cos(a) + y + (p.getX() - x) * Math.sin(a))
                );
            }
        ).toArray(Pair[]::new);
        return points;
    }


    public ScriptObject process(long t) throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Straighten straightener = new Straighten();
        BufferedImage bi = ImageIO.read(new File(ImgFileLocation));

        Result result = Detection.detectQRCode(bi);
        if (result == null){
            // TODO: handle: failure to detect QR code
            System.out.println("QR CODE NOT FOUND");
            return null;
        }

        Pair[] QRCodeCornerCoordinates = new Pair[3];

        for (int i = 0; i < 3; i++) {
            QRCodeCornerCoordinates[i] = new Pair(result.getResultPoints()[i].getX(), result.getResultPoints()[i].getY());
        }

        double angle = calculateRotationAngle(QRCodeCornerCoordinates);

        Mat image = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC1);
        image.put(0,0, ((DataBufferByte) bi.getRaster().getDataBuffer()).getData());

        QRCodeCornerCoordinates =  projectPoints(QRCodeCornerCoordinates, angle, image.cols()/2, image.rows()/2);
        QRCode qrcode = new QRCode(QRCodeCornerCoordinates, result);

        calculateScalingFactor(qrcode);

        image = straightener.straightenImage(image, angle, qrcode);

        Imgproc.threshold(image, image, 190, 255, Imgproc.THRESH_BINARY_INV);

        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(".jpg", image, mob);
        bi = ImageIO.read(new ByteArrayInputStream(mob.toArray()));

        ScriptObject script = new ScriptObject(bi);
        script.setQrCodeProperties(qrcode);
        return script;
   }

    public void calculateScalingFactor(QRCode qr) {
        ResultPoint[] points = qr.getResult().getResultPoints();

        float baseXDistance = (float) (2103 - 1430.5);
        float baseYDistance = (float) (2787 - 2114);

        float x = ResultPoint.distance(points[2], points[1]) / baseXDistance;
        float y = ResultPoint.distance(points[0], points[1]) / baseYDistance;

        qr.setScalingFactor(new Pair(x, y));
    }

    private double calculateRotationAngle(Pair[] points){

        /*
            Calculate the angle of rotation by taking looking at the triangle formed by the skew QR code.
            tan(angle) = opposite / adjacent
         */
        double deltaX = (points[2].getX() - points[1].getX());
        double deltaY = (points[2].getY() - points[1].getY());
        double angle = Math.atan2(deltaY, deltaX);
        angle = Math.toDegrees(angle);
        return angle;
    }

    public ScriptObject getScript(int i){
        return scripts[i];
    }

}
