package util;

/**
 * Vecsei Gábor
 *
 * Blog:        https://gaborvecsei.wordpress.com/
 * Email:       vecseigabor.x@gmail.com
 * LinkedIn:    https://hu.linkedin.com/in/vecsei-gábor-004b8611a
 *
 * 2016.08.29.
 */

import com.google.zxing.Result;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Straighten {

    //Rotation is done here
    private Mat rotateImage(Mat image, double angle, Result qr) {
        //Calculate image center
        Point imgCenter = new Point(image.cols() / 2, image.rows() / 2);

        Point QRCenter = new Point(qr.getResultPoints()[0].getX(), qr.getResultPoints()[0].getY());
        //Get the rotation matrix
//        Mat rotMtx = Imgproc.getRotationMatrix2D(imgCenter, angle, 1.0);
        Mat rotMtx = Imgproc.getRotationMatrix2D(QRCenter, angle, 1.0);
        //Calculate the bounding box for the new image after the rotation (without this it would be cropped)
//        Rect bbox = new RotatedRect(imgCenter, image.size(), angle).boundingRect();
        Rect bbox = new RotatedRect(QRCenter, image.size(), angle).boundingRect();

        //Rotate the image
        Mat rotatedImage = image.clone();
        Imgproc.warpAffine(image, rotatedImage, rotMtx, bbox.size());

        return rotatedImage;
    }

    //Sums the whole process and returns with the straight image
    public Mat straightenImage(Mat image, double angle, Result r) {
        return rotateImage(image, angle,  r);
    }


}