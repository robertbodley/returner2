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
import org.opencv.imgproc.Imgproc;

public class Straighten {

    //Rotation is done here
    private Mat rotateImage(Mat image, double angle) {
        /*
            Adapted code by Vecsei to work with rotation angle calculated using QR code coordinates.
         */

        //Calculate image center
        Point imgCenter = new Point(image.cols() / 2, image.rows() / 2);

        //Get the rotation matrix
        Mat rotMtx = Imgproc.getRotationMatrix2D(imgCenter, angle, 1.0);

        //Calculate the bounding box for the new image after the rotation (without this it would be cropped)
        Rect bbox = new RotatedRect(imgCenter, image.size(), angle).boundingRect();

        //Rotate the image
        Imgproc.warpAffine(image, image, rotMtx, bbox.size());
        return image;
    }

    //Sums the whole process and returns with the straight image
    public Mat straightenImage(Mat image, double angle) {
        return rotateImage(image, angle);
    }


}