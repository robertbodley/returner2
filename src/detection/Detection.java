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

    private static final float boxWidth = (float) 43;
    private static final float boxHeight = (float) 44;
    private static final float studentNumberHorizontalDistanceToNextBox = 41;
    private static final float studentNumberVerticalDistanceToNextBox = 19;

    private static final float quizAnswerVerticalDistanceToNextBox = (float) 22;
    private static final float quizAnswerHorizontalDistanceToNextBox = (float) 44;

    private static final float testAnswerVerticalDistanceToNextBox = (float) 22;
    private static final float testAnswerHorizontalDistanceToNextBox = (float) 42;
    /*
        QUIZ PAPER:
        Answer blocks start point: (1284, 1141)
        Stu Num block start point: (288, 1600)
        QR point 1 coordinates   : (715.0, 1018.0)

        TEST PAPER:
        Answer blocks start point 1: (1290, 1180)
        Answer blocks start point 2: (1290, 2097)
        Stu Num blck start point   : (192, 1141)
        QR point 1 coordinates     : (715.0, 991.0)

     */
    private static final float quizAnswersBoxesVerticalDistanceFromQR = 1141 - 1018;
    private static final float quizAnswersBoxesHorizontalDistanceFromQR = 1284 - 715;

    private static final float testPoint1AnswersBoxesVerticalDistanceFromQR = 1180 - 991;
    private static final float testPointAnswersBoxesHorizontalDistanceFromQR = 1290 - 715;
    private static final float testPoint2AnswersBoxesVerticalDistanceFromQR = 2097 - 991;

    private static final float studentNumberBoxesVerticalDistanceFromQR = 1600 - 1018;
    private static final float studentNumberBoxesHorizontalDistanceFromQR = 715 - 288;

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

        float thresh = (scaledBoxHeight * scaledBoxWidth) / 3;
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

                for (int y = (int) currentY; y < currentY + scaledBoxHeight; y++) {

                    for (int x = (int) currentX; x < currentX + scaledBoxWidth; x++) {

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
                System.out.println("oldmark, newmark" + oldMark + ", " + newMark);
                marks[questions / 2] = (oldMark * 10) + newMark;
                System.out.println("Question "+ questions + ": " + marks[questions/2]);
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

        float thresh = (scaledBoxHeight * scaledBoxWidth) / 3;
        int pixelCount;

        int[] marks = new int[numberOfQuestions];
        int mark;

        for (int questions = 0; questions < numberOfQuestions; questions++) {
            currentX = startX;
            mark = 0;


            currentY = startY + questions * (scaledBoxHeight + scaledVerticalDistanceToNextBox);

            for (int box = 0; box < numberOfAnswersPerQuestion; box++) {
                pixelCount = 0;

                for (int y = (int) currentY; y < currentY + scaledBoxHeight; y++) {

                    for (int x = (int) currentX; x < currentX + scaledBoxWidth; x++) {
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

    public static void detectStudentNumber(ScriptObject script) {
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

        float thresh = (scaledBoxHeight * scaledBoxWidth) / 3;
        System.out.println("Scaled threshold value: " + thresh);
        int pixelCount, currentCharacter;
        String studentNumber = "";

        for (int columns = 0; columns < 9; columns++) {

            currentY = startY;
            currentCharacter = (int) '!';
            currentX = startX + (columns * (scaledBoxWidth + scaledHorizontalDistanceToNextBox));

            for (int characters = 0; columns < 6 ? characters < 26 : characters < 10; characters++) {

                pixelCount = 0;

                for (int y = (int) currentY; y < currentY + scaledBoxHeight; y++) {

                    for (int x = (int) currentX; x < currentX + scaledBoxWidth; x++) {

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
                    studentNumber += '!';
                else
                    studentNumber += (char) (65 + currentCharacter);
            }
            else
                studentNumber += currentCharacter;
        }
        System.out.println(studentNumber);
    }

    public static Result detectQRCode(BufferedImage bi) {
        try {
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bi)));
            return new QRCodeReader().decode(binaryBitmap);
        } catch (Exception ex) {
            return null;
        }
    }
}