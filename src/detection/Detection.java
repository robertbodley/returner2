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

    private static final float quizAnswerHorizontalDistanceToNextBox = (float) 86.75;
    private static final float quizAnswerVerticalDistanceToNextBox = (float) 42;

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


    /**
     * This method detects the marks shaded in boxes on a test script
     *
     * @param script a script object representing the test script
     * @param debugMode boolean to specify whether to set pixels to gray as detection for visual debugging
     * @return an array of marks
     */

    public static int[] detectTestMarks(ScriptObject script, boolean debugMode){
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

            // update X value before iterating over boxes vertically again
            currentX =
                    questions < 10 ?
                    startX + questions * (scaledBoxWidth + scaledHorizontalDistanceToNextBox) :
                    startX + (questions - 10) * (scaledBoxWidth + scaledHorizontalDistanceToNextBox);

            for (int box = 0; box < 10; box++) {
                pixelCount = 0;

                // loop over box vertically
                for (int y = Math.round(currentY); y < currentY + scaledBoxHeight; y++) {
                    // loop over box horizontally
                    for (int x = Math.round(currentX); x < currentX + scaledBoxWidth; x++) {

                        // if enough white pixels found
                        // set either old mark or new mark to the current box depending on whether the
                        // question is odd or even. This is used to create double digit marks.
                        if (pixelCount > thresh) {
                            if (questions % 2 == 0){
                                oldMark = box;
                            }else{
                                newMark = box;
                            }
                            break;
                        }

                        try {
                            if ((img.getRGB(x, y) & 0xFFFFFF) == 0xFFFFFF)
                                // if current pixel is white then increment pixel count
                                // shaded in boxes would have white pixels due to the gray scaling
                                pixelCount++;
                            if (debugMode)
                                img.setRGB(x, y, 0xd3d3d3);
                        }catch(Exception ex){
                            // Detection error (tried to access pixels out of image bounds)
                            return null;
                        }
                    }
                    if (pixelCount > thresh) {
                        if (questions % 2 == 0){
                            oldMark = box;
                        }else{
                            newMark = box;
                        }
                        break;
                    }


                }
                if (pixelCount > thresh) {
                    if (questions % 2 == 0){
                        oldMark = box;
                    }else{
                        newMark = box;
                    }
                    break;
                }
                currentY += scaledBoxHeight + scaledVerticalDistanceToNextBox;
            }
            if (questions % 2 != 0){
                marks[questions / 2] = (oldMark * 10) + newMark;
            }
        }

        return marks;
    }


    /**
     *
     * This method detects the marks shaded in on a quiz paper
     *
     * @param script a script object representing the test script
     * @param debugMode boolean to specify whether to set pixels to gray as detection for visual debugging
     * @return an array of characters representing the selection for quiz questions
     */

    public static char[] detectQuizMarks(ScriptObject script, boolean debugMode) {
        QRCode qr = script.getQrCodeProperties();
        Pair scalingFactor = qr.getScalingFactor();
        BufferedImage img = script.getImage();

        String[] qrdata = qr.getData().split("---");
        int numberOfQuestions = Integer.parseInt(qrdata[1]);
        int numberOfAnswersPerQuestion = Integer.parseInt(qrdata[2]);

        // Calculate new scaled distances for box detection
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

        char[] marks = new char[numberOfQuestions];
        int mark;

        // loop over questions in quiz
        for (int questions = 0; questions < numberOfQuestions; questions++) {
            currentX = startX;
            mark = (int)'_';


            currentY = startY + questions * (scaledBoxHeight + scaledVerticalDistanceToNextBox);

            // loop over boxes horizontally
            for (int box = 0; box < numberOfAnswersPerQuestion; box++) {
                pixelCount = 0;

                // loop over box vertically
                for (int y = Math.round(currentY); y < currentY + scaledBoxHeight; y++) {
                    // loop over box horizontally
                    for (int x = Math.round(currentX); x < currentX + scaledBoxWidth; x++) {

                        // if there is enough white pixels found
                        // break ouf of the loop and set the mark to the current box.
                        if (pixelCount > thresh) {
                            mark = box;
                            break;
                        }
                        try {
                            if ((img.getRGB(x, y) & 0xFFFFFF) == 0xFFFFFF)
                                // if current pixel is white then increment pixel count.
                                // shaded in boxes would have white pixels due to the gray scaling
                                pixelCount++;

                            if (debugMode)
                                img.setRGB(x, y, 0xd3d3d3);
                        }catch(Exception ex) {
                            // Detection error (tried to access pixels out of image bounds)
                            return null;
                        }
                    }
                    if (pixelCount > thresh){
                        mark = box;
                        break;
                    }

                }
                if (pixelCount > thresh){
                    mark = box;
                    break;
                }
                currentX += scaledBoxWidth + scaledHorizontalDistanceToNextBox;
            }

            // if still an underscore i.e didn't detect then set the answer to an underscore.
            if(mark == (int)'_')
                marks[questions] = (char) mark;
            else
                marks[questions] = (char) (65 + mark);
        }

        return marks;
    }

    /**
     *
     * detects the student number by counting the number of white pixels in boxes.
     *
     * @param script a script object representing the test script
     * @param debugMode boolean to specify whether to set pixels to gray as detection for visual debugging
     * @return a string of the detected student number
     */

    public static String detectStudentNumber(ScriptObject script, boolean debugMode) {
        QRCode qr = script.getQrCodeProperties();
        Pair scalingFactor = qr.getScalingFactor();
        BufferedImage img = script.getImage();

        // Calculate new scaled distances for box detection
        float scaledBoxVerticalDistanceRelativeToQRCoordinates = studentNumberBoxesVerticalDistanceFromQR * scalingFactor.getY();
        float scaledBoxHorizontalDistanceRelativeToQRCoordinates = studentNumberBoxesHorizontalDistanceFromQR * scalingFactor.getX();

        float scaledBoxWidth = boxWidth * scalingFactor.getX();
        float scaledBoxHeight = boxHeight * scalingFactor.getY();
        float scaledHorizontalDistanceToNextBox = studentNumberHorizontalDistanceToNextBox * scalingFactor.getX();
        float scaledVerticalDistanceToNextBox = studentNumberVerticalDistanceToNextBox * scalingFactor.getY();

        float startY = qr.getQRCodeCornerCoordinates()[1].getY() + scaledBoxVerticalDistanceRelativeToQRCoordinates;
        float startX = qr.getQRCodeCornerCoordinates()[1].getX() - scaledBoxHorizontalDistanceRelativeToQRCoordinates;
        float currentX, currentY;

        // Calculate new pixel threshold based on scaled box width and height
        float thresh =   (scaledBoxHeight * scaledBoxWidth)/ 2;
        int pixelCount, currentCharacter;
        String studentNumber = "";

        // loop over columns (each student number letter)
        for (int columns = 0; columns < 9; columns++) {

            currentY = startY;
            currentCharacter = (int) '_';
            currentX = startX + (columns * (scaledBoxWidth + scaledHorizontalDistanceToNextBox));

            // loop over 26 letters or 9 numbers boxes depending on column number
            for (int characters = 0; columns < 6 ? characters < 26 : characters < 10; characters++) {

                pixelCount = 0;

                // loop over box height
                for (int y = Math.round(currentY); y < currentY + scaledBoxHeight; y++) {

                    // loop over box width
                    for (int x = Math.round(currentX); x < currentX + scaledBoxWidth; x++) {

                        // check if we have reached the pixel threshold
                        // i.e enough pixels found to consider box shaded in
                        if (pixelCount > thresh) {
                            currentCharacter = characters;
                            break;
                        }
                        try {
                            if ((img.getRGB(x, y) & 0xFFFFFF) == 0xFFFFFF)
                                // if current pixel is white then increment pixel count.
                                // shaded in boxes would have white pixels due to the gray scaling
                                pixelCount++;


                            if (debugMode)
                                img.setRGB(x, y, 0xd3d3d3);
                        } catch (Exception e) {
                            // Detection error (tried to access pixels out of image bounds)
                            return null;
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

                // move vertically to next box
                currentY += (scaledBoxHeight + scaledVerticalDistanceToNextBox);
            }
            if (columns < 6){
                if (currentCharacter == (int)'_')
                    studentNumber += '_';
                else
                    studentNumber += (char) (65 + currentCharacter);
            }
            else
                studentNumber += currentCharacter;
        }
        return studentNumber;
    }

    /**
     *
     * Detects a QR code in a given buffered image returning the result, uses the ZXing library to perform detection.
     *
     * @param bi buffered image used to try and detect a QR code
     * @return result object representing QR code information and properties
     */
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