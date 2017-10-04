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
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.util.Arrays;


public class Preprocessor {
    private String ImgFileLocation;

    public Preprocessor(String imgFile){
        this.ImgFileLocation = imgFile;

    }


    /**
     *
     * Calculates the point projection of the new QR code corner points based on the angle of rotation. Rotated around the center of the page by the angle
     * passed in.
     *
     * @param points an array of the three QR code corner points
     * @param angle a double representing the angle of rotation
     * @param x the x value of the "origin" or center of the current page
     * @param y the y value of the "origin" or center of the current page
     * @return an array of three new updated QR code corner point locations
     */

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

        image = straightener.straightenImage(image, angle);

        Imgproc.threshold(image, image, 190, 255, Imgproc.THRESH_BINARY_INV);

        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(".jpg", image, mob);
        bi = ImageIO.read(new ByteArrayInputStream(mob.toArray()));

        ScriptObject script = new ScriptObject(bi);
        script.setQrCodeProperties(qrcode);
        return script;
   }

    /**
     *
     * Calculates scaling factor by comparing the distance between QR code corner points (vertically and horizontally) to the distance between
     * points on the default template, allowing us to determine where boxes are "relative" from the default template's location.
     *
     * @param qr QRCode object storing all QR code data including coordinates needed for scaling factor calculation
     */

    public void calculateScalingFactor(QRCode qr) {
        ResultPoint[] points = qr.getResult().getResultPoints();


        // Given a QR code with corners A, B, C:

        //  B   C
        //
        //  A
        // This calculates the base distance between B and C (the X distance) on the default sheet
        // and the base distance between A and B (the Y distance) on the default sheet
        float baseXDistance = (float) (2103 - 1430.5);
        float baseYDistance = (float) (2787 - 2114);

        // The base distances are compared to the distances of the current QR code to determine how much bigger/smaller the
        // current script is relative to the default base script.
        float x = ResultPoint.distance(points[2], points[1]) / baseXDistance;
        float y = ResultPoint.distance(points[0], points[1]) / baseYDistance;
        qr.setScalingFactor(new Pair(x, y));
    }

    /**
     *
     * This method calculates the angle of rotation by evaluating the tangent of the opposite and adjacent side of the triangle formed by the skew QR code corner points
     *
     * @param points array of three QR code corner points
     * @return the angle of rotation
     */

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

}
