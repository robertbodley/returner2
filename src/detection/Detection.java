package detection;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import util.Pair;

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

    public static boolean detectStudentNumber(float[] scalers, BufferedImage bi) {
        // TODO
        int pixelThreshold = 600;//How many white pixels to count block as filled in
        Pair originalCord = new Pair((194*scalers[0]),(995*scalers[1]));//The Top left coroner of the first block
        float initialY = originalCord.getY()-((float)5.5*scalers[1]); // What part of each column to look at
        float initialX = originalCord.getX()-((float)5.5*scalers[0]); // What row to start looking at
        float startY = initialY; //Where to start looking in each column
        float yEnd = 0; //Last pixel in Y plane to look at for current block
        int pixelCount = 0;
        int letter = 65; // Capital A ASCII code
        String stdNo = "";
        for(int i=0; i<6;i++) {//Loop to get each letter of student number
            float xEnd = initialX + 40*scalers[0]; //Last pixel in X plane to look at for current block
            letter = 65;
            int num = 0;
            for (int l = 0; l < 25; l++) {//Loop to move down column to check letters
                yEnd = initialY + 40*scalers[1];
                pixelCount = 0; // amount of filled in pixels in block
                for (int y = (int)initialY; y < yEnd; y++) {//Move down current block
                    for (int x = (int)initialX; x < xEnd; x++) {//Move across current block
                        if ((bi.getRGB(x, y) & 0xFFFFFF) == 0xFFFFFF ) { // Check if current pixel is white
                            pixelCount++;//increase amount of black
                        }
                        if(pixelCount>pixelThreshold){//If block is filled in adequately exit
                            break;
                        }
                    }
                    if(pixelCount>pixelThreshold){ //If block is filled in adequately exit
                        break;
                    }
                }
                initialY = yEnd; //Move to next block if no block filled in
                if(pixelCount>pixelThreshold){ //If block is filled in adequately exit
                    break;
                }
                letter++;
                num++;
            }
            stdNo = stdNo + "" + Character.toString((char) letter);

            initialX= initialX+54*scalers[0];//Move to next column
            initialY = startY;//Start at top of the column
            System.out.println(stdNo);
        }
        return false;
    }

    public static Result readQRCode(BufferedImage bi) {
        BinaryBitmap binaryBitmap;
        Result result;
        try {
            binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bi)));
            result = new QRCodeReader().decode(binaryBitmap);
            return result;
        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
            return null;
        }
    }

    public static boolean detectQRCode(ScriptObject script) {


        // TODO
        return false;
    }

    public static boolean detectQuestionMarks(ScriptObject script) {
        // TODO
        return false;
    }

    public static float[] scaling(Pair p[]) {
//right left bottom
        float[] scalers = new float[2];
        float originX = (float) (749.00 - 609.00);
        float originY = (float) (830.5 - 682.5);
        scalers[0] = (p[0].getX() - p[1].getX()) / originX;
        scalers[1] = (p[2].getY() - p[1].getY()) / originY;
        return scalers;
    }
}