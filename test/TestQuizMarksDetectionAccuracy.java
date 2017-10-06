import detection.Detection;
import org.junit.Assert;
import org.junit.Test;
import util.Preprocessor;
import util.ScriptObject;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


public class TestQuizMarksDetectionAccuracy {
    String testLocation ="testResources/accuracy/";

    static int total = 0;
    static int numberOfIncorrectDetections = 0;

    @Test
    public void TestQuizMarksDetectionAccuracy() throws IOException {
        String path = testLocation +  "badquality-5.jpg";

        char[] ans = {'J','I','H','G','F','G','H','I','J','A','B','C','D','E','F','G','H','I','J','E','C','G','C','E','G','I','E','I','J','G'};


        long t = System.nanoTime();
        Preprocessor pre = new Preprocessor(path);
        ScriptObject script = pre.process(t);

        String sn = Detection.detectStudentNumber(script, true);
        int before = numberOfIncorrectDetections;


        total += script.getQrCodeProperties().getNumberOfQuestions();

        char[] chars = Detection.detectQuizMarks(script, true);
        for (int i = 0; i <ans.length; i++){
            if (chars[i] != ans[i])
                numberOfIncorrectDetections++;
        }

        Assert.assertEquals(numberOfIncorrectDetections - before, 0);
    }

    @Test
    public void TestQuizMarksDetectionAccuracy1() throws IOException {
        String path = testLocation +  "badquality-6.jpg";

        char[] ans = {'A','B','A','B','A','B','A','B','A','B','A','B','A','B','A','B','A','B','A','B','A','B','A','B','A','B','A','B','A','B'};


        long t = System.nanoTime();
        Preprocessor pre = new Preprocessor(path);
        ScriptObject script = pre.process(t);

        String sn = Detection.detectStudentNumber(script, true);
        int before = numberOfIncorrectDetections;

        total += script.getQrCodeProperties().getNumberOfQuestions();

        char[] chars = Detection.detectQuizMarks(script, true);
        for (int i = 0; i <ans.length; i++){
            if (chars[i] != ans[i])
                numberOfIncorrectDetections++;
        }

        Assert.assertEquals(numberOfIncorrectDetections - before, 0);
    }

    @Test
    public void TestQuizMarksDetectionAccuracy2() throws IOException {
        String path = testLocation +  "badquality-7.jpg";

        char[] ans = {'I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J'};


        long t = System.nanoTime();
        Preprocessor pre = new Preprocessor(path);
        ScriptObject script = pre.process(t);

        String sn = Detection.detectStudentNumber(script, true);
        int before = numberOfIncorrectDetections;

        total += script.getQrCodeProperties().getNumberOfQuestions();

        char[] chars = Detection.detectQuizMarks(script, true);
        for (int i = 0; i <ans.length; i++){
            if (chars[i] != ans[i])
                numberOfIncorrectDetections++;
        }

        Assert.assertEquals(numberOfIncorrectDetections - before, 0);
    }

    @Test
    public void TestQuizMarksDetectionAccuracy3() throws IOException {
        String path = testLocation +  "l-4.jpg";

        char[] ans = {'I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J'};


        long t = System.nanoTime();
        Preprocessor pre = new Preprocessor(path);
        ScriptObject script = pre.process(t);

        String sn = Detection.detectStudentNumber(script, true);
        int before = numberOfIncorrectDetections;

        total += script.getQrCodeProperties().getNumberOfQuestions();

        char[] chars = Detection.detectQuizMarks(script, true);
        for (int i = 0; i <ans.length; i++){
            if (chars[i] != ans[i])
                numberOfIncorrectDetections++;
        }
    System.out.println("quiz marking accuracy:\t\t\t3 quiz scripts tested \nACCURACY: \t\t\t\t\t\t" + (100 - (numberOfIncorrectDetections *100/total)) + "%\n");

        Assert.assertEquals(numberOfIncorrectDetections - before, 0);
    }




}
