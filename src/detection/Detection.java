package detection;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import util.Pair;

import util.QRCode;
import util.ScriptObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;

public class Detection {

    public static void detectStudentNumber(Pair scalingFactor, BufferedImage img, QRCode qr){

        float startY =  (float) (qr.getQRCodeCornerCoordinates()[1].getY() + ((1003 - 682.5) * scalingFactor.getY()));
//        float startY = (999 - qr.getQRCodeCornerCoordinates()[1].getY())
        float currentY;
//        float startX = 196 * scalingFactor.getX();
        float startX = (qr.getQRCodeCornerCoordinates()[1].getX() -( (609 - 195) * scalingFactor.getX()));
        float currentX = startX;
        float thresh = 300 *  ((scalingFactor.getX() + scalingFactor.getY()) / 2);
        System.out.println("THRESHOLD" + thresh);
        int pixelCount;

        int c = 65;
        String s = "";
        System.out.println("scaling"+ scalingFactor.getX() + " " + scalingFactor.getY());

        for (int b = 0; b < 6; b++){

            currentY = startY;
            c = 65;

            currentX = startX + (b * (25 + 30) * scalingFactor.getX());

            for(int l = 0; l < 25; l ++) {
                pixelCount = 0;

                for (int y = (int) currentY; y < currentY + (24 * scalingFactor.getY()); y++) {

                    for (int x = (int) currentX; x < currentX + (25 * scalingFactor.getX()); x++) {
                        if (pixelCount > thresh)
                            break;

                        if ((img.getRGB(x, y) & 0xFFFFFF) == 0xFFFFFF) { // Check if current pixel is white
                            pixelCount++;//increase amount of black
                        }
                        img.setRGB(x,y, 0xd3d3d3);
//                        img.setRGB(x,y, new Color(250 ,250, 250).getRGB());

                    }
                    if (pixelCount > thresh)
                        break;
                }
                if (pixelCount > thresh)
                    break;
                currentY += (24 + 17 ) * scalingFactor.getY();

                c++;
            }
            s += (char) c;
            System.out.println( s);
        }
    }

    public static Result detectQRCode(BufferedImage bi) {
        BinaryBitmap binaryBitmap;
        Result result;
        try {
            binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bi)));
            result = new QRCodeReader().decode(binaryBitmap);

            return result;
        } catch (Exception ex) {
//            System.out.println(ex);
            ex.printStackTrace();
            return null;
        }
    }


    public static boolean detectQuestionMarks(ScriptObject script) {
        // TODO
        return false;
    }

    public static Pair calculateScalingFactor(Result result) {

        ResultPoint[] points = result.getResultPoints();
        ResultPoint dist = new ResultPoint(0,0);

//right left bottom
        float originX = (float) (749.00 - 609.00);
        float originY = (float) (830.5 - 682.5);

//        System.out.println("Scale calculation:\n");
//        bi.setRGB(p[0].getX(), p[0].getY());
        float x = dist.distance(points[0], points[1]) / originX;
        float y = dist.distance(points[2], points[1]) / originY;
        System.out.println("SF: " + x + "   " + y);
        return new Pair(x, y);
    }
}