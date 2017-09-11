package detection;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import util.Pair;

import util.QRCode;
import util.ScriptObject;

import java.awt.image.BufferedImage;

public class Detection {

    public static void detectStudentNumber( BufferedImage img, QRCode qr){
        Pair scalingFactor = qr.getScalingFactor();

        float boxWidth = 25;
        float boxHeight = 24;
        float horizontalDistanceToNextBox = 30;
        float verticalDistanceToNextBox = 17;
        float scaledBoxVerticalDistanceRelativeToQRCoordinates = (float) (1003 - 682.5) * scalingFactor.getY();
        float scaledBoxHorizontalDistanceRelativeToQRCoordinates = (float) (609 - 195) * scalingFactor.getX();

        float scaledBoxWidth = boxWidth * scalingFactor.getX();
        float scaledBoxHeight = boxHeight * scalingFactor.getY();
        float scaledHorizontalDistanceToNextBox = horizontalDistanceToNextBox * scalingFactor.getX();
        float scaledVerticalDistanceToNextBox = verticalDistanceToNextBox * scalingFactor.getY();

        float startY = qr.getQRCodeCornerCoordinates()[1].getY() + scaledBoxVerticalDistanceRelativeToQRCoordinates;
        float startX = qr.getQRCodeCornerCoordinates()[1].getX() - scaledBoxHorizontalDistanceRelativeToQRCoordinates;
        float currentX , currentY;

//        System.out.println("scaled height " + scaledBoxHeight + "\nscaled width " + scaledBoxWidth);
        float thresh = (scaledBoxHeight * scaledBoxWidth) / 2;
        System.out.println("Scaled threshold value: " + thresh);
        int pixelCount, currentCharacter;
        String studentNumber = "";

        for (int columns = 0; columns < 9; columns++){

            currentY = startY;
            currentCharacter = columns < 6 ? 65 : 0;
            currentX = startX + columns * (scaledBoxWidth + scaledHorizontalDistanceToNextBox);

            for(int characters = 0; columns < 6 ? characters < 25: characters < 9; characters ++) {

                pixelCount = 0;

                for (int y = (int) currentY; y < currentY + scaledBoxHeight; y++) {

                    for (int x = (int) currentX; x < currentX + scaledBoxWidth; x++) {

                        if (pixelCount > thresh)
                            break;
                        try {
                            if ((img.getRGB(x, y) & 0xFFFFFF) == 0xFFFFFF)
                                // if current pixel is white then increase amount of detected pixels.
                                // shaded in boxes would have white pixels due to the gray scaling
                                pixelCount++;

                            // TODO: only change pixel value and output a file if running in debug mode
                            img.setRGB(x, y, 0xd3d3d3);

                        }catch(Exception e){
                            System.out.println("BOUNDS ERROR IN DETECTION. X, Y:" + x + ", " + y);
                            System.exit(0);
                        }
                    }
                    if (pixelCount > thresh)
                        break;
                }
                if (pixelCount > thresh)
                    break;
                currentY += scaledBoxHeight + scaledVerticalDistanceToNextBox;
                currentCharacter++;

            }
            if (columns < 6)
                studentNumber += (char) currentCharacter;
            else
                studentNumber += currentCharacter;
        }
        System.out.println(studentNumber);
    }

    public static Result detectQRCode(BufferedImage bi, long t) {
        try {
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bi)));
            return new QRCodeReader().decode(binaryBitmap);
        } catch (Exception ex) {
            return null;
        }
    }

    public static boolean detectQuestionMarks(ScriptObject script) {
        // TODO
        return false;
    }


}