package detection;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import util.Pair;
import util.QRCode;
import util.ScriptObject;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Detection {

    private static final float boxWidth = (float) 88;
    private static final float boxHeight = (float) 88;

    private static final float studentNumberHorizontalDistanceToNextBox = 81;
    private static final float studentNumberVerticalDistanceToNextBox = 36;

    private static final float quizAnswerHorizontalDistanceToNextBox = (float) 86.5;
    private static final float quizAnswerVerticalDistanceToNextBox = (float) 43;

    private static final float testAnswerHorizontalDistanceToNextBox = (float) 80.75;
    private static final float testAnswerVerticalDistanceToNextBox = (float) 42.85;

    /*
        QUIZ PAPER:
        Answer blocks start point: (1284, 1141) -> 2718, 2361
        Stu Num block start point: (288, 1600) -> 577, 3279
        QR point 1 coordinates   : (1430.5, 2114.0)

        TEST PAPER:
        Answer blocks start point 1: (1290, 1180) -> 2755, 2492
        Answer blocks start point 2: (1290, 2097) -> 2755, 4326
     */
    private static final float quizAnswersBoxesVerticalDistanceFromQR = 2361 - 2114;
    private static final float quizAnswersBoxesHorizontalDistanceFromQR = 2718 - (float)1430.5;

    private static final float testPoint1AnswersBoxesVerticalDistanceFromQR = 2492 - 2114;
    private static final float testPointAnswersBoxesHorizontalDistanceFromQR = 2755 - (float)1430.5;
    private static final float testPoint2AnswersBoxesVerticalDistanceFromQR = 4326 - 2114;

    private static final float studentNumberBoxesVerticalDistanceFromQR = 3279 - 2114;
    private static final float studentNumberBoxesHorizontalDistanceFromQR = (float)1430.5 - 577;

    public static void detectTestMarks(ScriptObject script){
        QRCode qr = script.getQrCodeProperties();
        Pair scalingFactor = qr.getScalingFactor();
        BufferedImage img = script.getImage();

        String[] qrdata = qr.getData().split("---");
        int numberOfQuestions = Integer.parseInt(qrdata[1]);

        float firstScaledBoxVerticalDistanceRelativeToQRCoordinates = testPoint1AnswersBoxesVerticalDistanceFromQR * scalingFactor.getY();
        float secondScaledBoxVerticalDistanceRelativeToQRCoordinates = testPoint2AnswersBoxesVerticalDistanceFromQR * scalingFactor.getY();
        float scaledBoxHorizontalDistanceRelativeToQRCoordinates = testPointAnswersBoxesHorizontalDistanceFromQR * scalingFactor.getX();

        float scaledBoxWidth = boxWidth * scalingFactor.getX();
        float scaledBoxHeight = boxHeight * scalingFactor.getY();
        float scaledHorizontalDistanceToNextBox = testAnswerHorizontalDistanceToNextBox * scalingFactor.getX();
        float scaledVerticalDistanceToNextBox = testAnswerVerticalDistanceToNextBox * scalingFactor.getY();

        float firstStartY = qr.getQRCodeCornerCoordinates()[1].getY() + firstScaledBoxVerticalDistanceRelativeToQRCoordinates;
        float secondStartY = qr.getQRCodeCornerCoordinates()[1].getY() + secondScaledBoxVerticalDistanceRelativeToQRCoordinates;
        float startX = qr.getQRCodeCornerCoordinates()[1].getX() + scaledBoxHorizontalDistanceRelativeToQRCoordinates;
        float currentX, currentY;

        float thresh = (scaledBoxHeight * scaledBoxWidth)/ 2;
        int pixelCount;

        int[] marks = new int[numberOfQuestions];
        numberOfQuestions = numberOfQuestions > 10 ? 10 : numberOfQuestions;
        int newMark = 0, oldMark = 0;

        for (int questions = 0; questions < numberOfQuestions * 2; questions++) {

            currentY = questions < 10 ? firstStartY : secondStartY;
            currentX =
                    questions < 10 ?
                    startX + questions * (scaledBoxWidth + scaledHorizontalDistanceToNextBox) :
                    startX + (questions - 10) * (scaledBoxWidth + scaledHorizontalDistanceToNextBox);

            for (int box = 0; box < 10; box++) {
                pixelCount = 0;

                for (int y = Math.round(currentY); y < currentY + scaledBoxHeight; y++) {

                    for (int x = Math.round(currentX); x < currentX + scaledBoxWidth; x++) {

                        if (pixelCount > thresh)
                            break;

                        try {
                            if ((img.getRGB(x, y) & 0xFFFFFF) == 0xFFFFFF)
                                // if current pixel is white then increment pixel count
                                // shaded in boxes would have white pixels due to the gray scaling
                                pixelCount++;

                            // TODO: only change pixel value and output a file if running in debug mode
                            img.setRGB(x, y, 0xd3d3d3);
                        }catch(Exception ex){
                            System.out.println("x, y" + x  +" , " + y);
                        }
                    }
                    if (pixelCount > thresh)
                        break;
                    if (questions % 2 == 0){
                        oldMark = box;
                    }else{
                        newMark = box;
                    }

                }
                if (pixelCount > thresh)
                    break;
                currentY += scaledBoxHeight + scaledVerticalDistanceToNextBox;
            }
            if (questions % 2 != 0){
                marks[questions / 2] = (oldMark * 10) + newMark;
            }
        }

        for (int m : marks) {
            System.out.print( m+ ", ");
        }
        System.out.println();
    }

    public static void detectQuizMarks(ScriptObject script) {

        QRCode qr = script.getQrCodeProperties();
        Pair scalingFactor = qr.getScalingFactor();
        BufferedImage img = script.getImage();

        String[] qrdata = qr.getData().split("---");
        int numberOfQuestions = Integer.parseInt(qrdata[1]);
        int numberOfAnswersPerQuestion = Integer.parseInt(qrdata[2]);

        float scaledBoxVerticalDistanceRelativeToQRCoordinates = quizAnswersBoxesVerticalDistanceFromQR * scalingFactor.getY();
        float scaledBoxHorizontalDistanceRelativeToQRCoordinates = quizAnswersBoxesHorizontalDistanceFromQR * scalingFactor.getX();

        float scaledBoxWidth = boxWidth * scalingFactor.getX();
        float scaledBoxHeight = boxHeight * scalingFactor.getY();
        float scaledHorizontalDistanceToNextBox = quizAnswerHorizontalDistanceToNextBox * scalingFactor.getX();
        float scaledVerticalDistanceToNextBox = quizAnswerVerticalDistanceToNextBox * scalingFactor.getY();

        float startY = qr.getQRCodeCornerCoordinates()[1].getY() + scaledBoxVerticalDistanceRelativeToQRCoordinates;
        float startX = qr.getQRCodeCornerCoordinates()[1].getX() + scaledBoxHorizontalDistanceRelativeToQRCoordinates;
        float currentX, currentY;

        float thresh = (scaledBoxHeight * scaledBoxWidth)/ 2;
        int pixelCount;

        int[] marks = new int[numberOfQuestions];
        int mark;

        for (int questions = 0; questions < numberOfQuestions; questions++) {
            currentX = startX;
            mark = 0;


            currentY = startY + questions * (scaledBoxHeight + scaledVerticalDistanceToNextBox);

            for (int box = 0; box < numberOfAnswersPerQuestion; box++) {
                pixelCount = 0;

                for (int y = Math.round(currentY); y < currentY + scaledBoxHeight; y++) {

                    for (int x = Math.round(currentX); x < currentX + scaledBoxWidth; x++) {
                        if (pixelCount > thresh)
                            break;
                        try {
                            if ((img.getRGB(x, y) & 0xFFFFFF) == 0xFFFFFF)
                                // if current pixel is white then increment pixel count.
                                // shaded in boxes would have white pixels due to the gray scaling
                                pixelCount++;

                            // TODO: only change pixel value and output a file if running in debug mode
                            img.setRGB(x, y, 0xd3d3d3);
                        }catch(Exception ex){
                                System.out.println("x, y" + x  +" , " + y);
                            }
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
            System.out.print( m+ ", ");
        }
        System.out.println();
    }

    public static String detectStudentNumber(ScriptObject script) {
        QRCode qr = script.getQrCodeProperties();
        Pair scalingFactor = qr.getScalingFactor();
        BufferedImage img = script.getImage();

        float scaledBoxVerticalDistanceRelativeToQRCoordinates = studentNumberBoxesVerticalDistanceFromQR * scalingFactor.getY();
        float scaledBoxHorizontalDistanceRelativeToQRCoordinates = studentNumberBoxesHorizontalDistanceFromQR * scalingFactor.getX();

        float scaledBoxWidth = boxWidth * scalingFactor.getX();
        float scaledBoxHeight = boxHeight * scalingFactor.getY();
        float scaledHorizontalDistanceToNextBox = studentNumberHorizontalDistanceToNextBox * scalingFactor.getX();
        float scaledVerticalDistanceToNextBox = studentNumberVerticalDistanceToNextBox * scalingFactor.getY();

        float startY = qr.getQRCodeCornerCoordinates()[1].getY() + scaledBoxVerticalDistanceRelativeToQRCoordinates;
        float startX = qr.getQRCodeCornerCoordinates()[1].getX() - scaledBoxHorizontalDistanceRelativeToQRCoordinates;
        float currentX, currentY;

        float thresh = (scaledBoxHeight * scaledBoxWidth)/ 2;
//        System.out.println("Scaled threshold value: " + thresh);
        int pixelCount, currentCharacter;
        String studentNumber = "";

        for (int columns = 0; columns < 9; columns++) {

            currentY = startY;
            currentCharacter = (int) '!';
            currentX = startX + (columns * (scaledBoxWidth + scaledHorizontalDistanceToNextBox));

            for (int characters = 0; columns < 6 ? characters < 26 : characters < 10; characters++) {

                pixelCount = 0;

                for (int y = Math.round(currentY); y < currentY + scaledBoxHeight; y++) {

                    for (int x = Math.round(currentX); x < currentX + scaledBoxWidth; x++) {

                        if (pixelCount > thresh) {
                            currentCharacter = characters;
                            break;
                        }
                        try {
                            if ((img.getRGB(x, y) & 0xFFFFFF) == 0xFFFFFF)
                                // if current pixel is white then increment pixel count.
                                // shaded in boxes would have white pixels due to the gray scaling
                                pixelCount++;

                            // TODO: only change pixel value and output a file if running in debug mode
                            img.setRGB(x, y, 0xd3d3d3);

                        } catch (Exception e) {
                            System.out.println("BOUNDS ERROR IN DETECTION. X, Y:" + x + ", " + y);
                            System.exit(0);
                        }
                    }
                    if (pixelCount > thresh){
                        currentCharacter = characters;
                        break;
                    }
                }
                if (pixelCount > thresh){
                    currentCharacter = characters;
                    break;
                }
                currentY += (scaledBoxHeight + scaledVerticalDistanceToNextBox);
            }
            if (columns < 6){
                if (currentCharacter == (int)'!')
                    studentNumber += '_';
                else
                    studentNumber += (char) (65 + currentCharacter);
            }
            else
                studentNumber += currentCharacter;
        }
        return studentNumber;
    }

    public static Result detectQRCode(BufferedImage bi) {
        Map<DecodeHintType, Object> hints = new HashMap<>();
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        try {
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bi)));
            return new QRCodeReader().decode(binaryBitmap, hints);
        } catch (Exception ex) {
            return null;
        }
    }
}