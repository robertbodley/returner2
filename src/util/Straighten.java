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

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Straighten {

    //Rotation is done here
    private Mat rotateImage(Mat image, double angle) {
        //Calculate image center
        Point imgCenter = new Point(image.cols() / 2, image.rows() / 2);
        //Get the rotation matrix
        Mat rotMtx = Imgproc.getRotationMatrix2D(imgCenter, angle, 1.0);
        //Calculate the bounding box for the new image after the rotation (without this it would be cropped)
        Rect bbox = new RotatedRect(imgCenter, image.size(), angle).boundingRect();

        //Rotate the image
        Mat rotatedImage = image.clone();
        Imgproc.warpAffine(image, rotatedImage, rotMtx, bbox.size());

        return rotatedImage;
    }

    //Sums the whole process and returns with the straight image
    public Mat straightenImage(Mat image, double angle) {
        return rotateImage(image, angle);
    }


}