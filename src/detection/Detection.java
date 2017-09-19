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

    private static final float boxWidth = (float) 20.5;
    private static final float boxHeight = (float) 44;
    private static final float horizontalDistanceToNextBox = 36;
    private static final float verticalDistanceToNextBox = 19;

    private static final float marksVerticalDistanceToNextBox = (float) 21.05;
    private static final float marksHoritzontalDistanceToNextBox = (float) 37.5;
    /*
        QUIZ PAPER:
        Answer blocks start point: (856, 831) // 876
        Stu Num block start point: (288, 1600)
        QR point 1 coordinates   : (471, 747)

        TEST PAPER:
        Answer blocks start point: ()
        Stu Num blck start point : (192, 1141)
     */
    private static final float marksBoxesVerticalDistanceFromQR = 831 - 747;
    private static final float marksBoxesHorizontalDistanceFromQR = 856 - 471;

    private static final float studentNumberBoxesVerticalDistanceFromQR = 1141 - 747;
    private static final float studentNumberBoxesHorizontalDistanceFromQR = 471 - 192;


    public static void detectQuestionMarks(ScriptObject script, boolean quizPaper) {
        QRCode qr = script.getQrCodeProperties();
        Pair scalingFactor = qr.getScalingFactor();
        BufferedImage img = script.getImage();

        float scaledBoxVerticalDistanceRelativeToQRCoordinates = marksBoxesVerticalDistanceFromQR * scalingFactor.getY();
        float scaledBoxHorizontalDistanceRelativeToQRCoordinates = marksBoxesHorizontalDistanceFromQR * scalingFactor.getX();

        float scaledBoxWidth = boxWidth * scalingFactor.getX();
        float scaledBoxHeight = boxHeight * scalingFactor.getY();
        float scaledHorizontalDistanceToNextBox = marksHoritzontalDistanceToNextBox * scalingFactor.getX();
        float scaledVerticalDistanceToNextBox = marksVerticalDistanceToNextBox * scalingFactor.getY();

        float startY = qr.getQRCodeCornerCoordinates()[1].getY() + scaledBoxVerticalDistanceRelativeToQRCoordinates;
        float startX = qr.getQRCodeCornerCoordinates()[1].getX() + scaledBoxHorizontalDistanceRelativeToQRCoordinates;
        float currentX, currentY;

        float thresh = (scaledBoxHeight * scaledBoxWidth) / 3;
        int pixelCount;

        int[] marks = new int[30];
        int mark;

        for (int questions = 0; questions < 6; questions++) {
            currentX = startX;
            mark = 0;

            currentY = startY + questions * (scaledBoxWidth + scaledHorizontalDistanceToNextBox);

            for (int box = 0; box < 30; box++) {
                pixelCount = 0;

                for (int y = (int) currentY; y < currentY + scaledBoxHeight; y++) {

                    for (int x = (int) currentX; x < currentX + scaledBoxWidth; x++) {
                        if (pixelCount > thresh)
                            break;
                        if ((img.getRGB(x, y) & 0xFFFFFF) == 0xFFFFFF)
                            // if current pixel is white then increase amount of detected pixels.
                            // shaded in boxes would have white pixels due to the gray scaling
                            pixelCount++;

                        // TODO: only change pixel value and output a file if running in debug mode
                        img.setRGB(x, y, 0xd3d3d3);

                    }
                    if (pixelCount > thresh)
                        break;
                    mark = box;

                }
                if (pixelCount > thresh)
                    break;
                currentX += scaledBoxWidth + scaledHorizontalDistanceToNextBox;
            }
            marks[questions] += mark;
        }

        for (int m : marks) {
            System.out.println("mark    " + m);
        }

    }

    public static void detectStudentNumber(ScriptObject script) {
        QRCode qr = script.getQrCodeProperties();
        Pair scalingFactor = qr.getScalingFactor();
        BufferedImage img = script.getImage();

        float scaledBoxVerticalDistanceRelativeToQRCoordinates = studentNumberBoxesVerticalDistanceFromQR * scalingFactor.getY();
        float scaledBoxHorizontalDistanceRelativeToQRCoordinates = studentNumberBoxesHorizontalDistanceFromQR * scalingFactor.getX();

        float scaledBoxWidth = boxWidth * scalingFactor.getX();
        float scaledBoxHeight = boxHeight * scalingFactor.getY();
        float scaledHorizontalDistanceToNextBox = horizontalDistanceToNextBox * scalingFactor.getX();
        float scaledVerticalDistanceToNextBox = verticalDistanceToNextBox * scalingFactor.getY();

        System.out.println(scaledBoxHeight + " " + scaledBoxWidth + " " + scaledHorizontalDistanceToNextBox + " " + scaledVerticalDistanceToNextBox);

        float startY = qr.getQRCodeCornerCoordinates()[1].getY() + scaledBoxVerticalDistanceRelativeToQRCoordinates;
        float startX = qr.getQRCodeCornerCoordinates()[1].getX() - scaledBoxHorizontalDistanceRelativeToQRCoordinates;
        float currentX, currentY;
        System.out.println("qr point 1 x: " + qr.getQRCodeCornerCoordinates()[1].getX() + " y : " + qr.getQRCodeCornerCoordinates()[1].getY());
        System.out.println("START X " + startX + " " + startY);

        float thresh = (scaledBoxHeight * scaledBoxWidth) / 3;
        System.out.println("Scaled threshold value: " + thresh);
        int pixelCount, currentCharacter;
        String studentNumber = "";

        for (int columns = 0; columns < 9; columns++) {

            currentY = startY;
            currentCharacter = columns < 6 ? 64 : -1;
            currentX = startX + (columns * (scaledBoxWidth + scaledHorizontalDistanceToNextBox));

            for (int characters = 0; columns < 6 ? characters < 26 : characters < 10; characters++) {

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

                        } catch (Exception e) {
                            System.out.println("BOUNDS ERROR IN DETECTION. X, Y:" + x + ", " + y);
                            System.exit(0);
                        }
                    }
                    if (pixelCount > thresh)
                        break;
                }

//                if (characters == 9 && columns > 5)
//                    System.out.println("characters and cols");
                if (pixelCount > thresh)
                    break;
                currentY = startY + characters * (scaledBoxHeight + scaledVerticalDistanceToNextBox);
                currentCharacter++;

            }
            if (columns < 6)
                studentNumber += (char) currentCharacter;
            else
                studentNumber += currentCharacter;
        }
        System.out.println(studentNumber);
    }

    public static Result detectQRCode(BufferedImage bi) {
//        Map<DecodeHintType, Object> hints = new HashMap<>();
//        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
//        hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
        try {
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bi)));
            return new QRCodeReader().decode(binaryBitmap);
        } catch (Exception ex) {
            return null;
        }
    }


}