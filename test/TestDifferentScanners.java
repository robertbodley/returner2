import detection.Detection;
import org.junit.Assert;
import org.junit.Test;
import util.Preprocessor;
import util.ScriptObject;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Created by Oliver on 2017/10/05.
 */
public class TestDifferentScanners {
    String testLocation ="testResources/scanners/";
    String[] answers = {"BDLROB001", "YZYZYZ089", "DBROLI001", "BDLROB001", "BDLRJM199", "WXYZAZ888"};

    static int total = 0;
    static int numberOfIncorrectDetections = 0;

    @Test
    public void TestScannable1() throws IOException {
        int testn = 0;
        String path = testLocation +  "scannableApp/k-1.jpg";

        char[] ans = {'J','I','H','I','J','I','H','G','F','E','F','G','F','E','D','C','B','D','F','H','J','E','C','E','H','E','B','J','C','A'};


        long t = System.nanoTime();
        Preprocessor pre = new Preprocessor(path);
        ScriptObject script = pre.process(t);

        String sn = Detection.detectStudentNumber(script, true);
        int before = numberOfIncorrectDetections;
        total += 9;
        for(int i = 0; i < sn.length(); i++){
            if (sn.charAt(i) != answers[testn].charAt(i))
                numberOfIncorrectDetections++;
        }

        total += script.getQrCodeProperties().getNumberOfQuestions();

        char[] chars = Detection.detectQuizMarks(script, true);
        for (int i = 0; i <ans.length; i++){
            if (chars[i] != ans[i])
                numberOfIncorrectDetections++;
        }

//        ImageIO.write(script.getImage(), "jpg", new File("test.jpg"));
        Assert.assertEquals(numberOfIncorrectDetections - before, 0);
    }

    @Test
    public void TestScannable2() throws IOException {
        int testn = 1;
        String path = testLocation +  "scannableApp/k-2.jpg";

        char[] ans = {'I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J'};


        long t = System.nanoTime();
        Preprocessor pre = new Preprocessor(path);
        ScriptObject script = pre.process(t);

        String sn = Detection.detectStudentNumber(script, true);
        int before = numberOfIncorrectDetections;
        total += 9;
        for(int i = 0; i < sn.length(); i++){
            if (sn.charAt(i) != answers[testn].charAt(i))
                numberOfIncorrectDetections++;
        }
        total += script.getQrCodeProperties().getNumberOfQuestions();

        char[] chars = Detection.detectQuizMarks(script, true);
        for (int i = 0; i <ans.length; i++){
            if (chars[i] != ans[i])
                numberOfIncorrectDetections++;
        }


        Assert.assertEquals(numberOfIncorrectDetections - before, 0);
    }

    @Test
    public void TestOliver1() throws IOException {
        String path = testLocation +  "oliver/200dpi-0.jpg";

        int[] ans = {99,88,77,66,55,0,12,34,56,78};
        long t = System.nanoTime();
        Preprocessor pre = new Preprocessor(path);
        ScriptObject script = pre.process(t);

        String sn = Detection.detectStudentNumber(script, true);
        int before = numberOfIncorrectDetections;
        total += 9;
        for(int i = 0; i < sn.length(); i++){
            if (sn.charAt(i) != answers[2].charAt(i))
                numberOfIncorrectDetections++;
        }

        total += script.getQrCodeProperties().getNumberOfQuestions()*2;

        int[] marks = Detection.detectTestMarks(script, true);
        for (int i = 0; i <marks.length; i++){
            if (marks[i] != ans[i])
                numberOfIncorrectDetections++;
        }


        Assert.assertEquals(numberOfIncorrectDetections - before, 0,5);
    }

    @Test
    public void TestOliver2() throws IOException {
        String path = testLocation +  "oliver/200dpi-1.jpg";

        int[] ans = {98,76,54,32,10,9,9,9,0,90};
        long t = System.nanoTime();
        Preprocessor pre = new Preprocessor(path);
        ScriptObject script = pre.process(t);

        String sn = Detection.detectStudentNumber(script, true);
        int before = numberOfIncorrectDetections;
        total += 9;
        for(int i = 0; i < sn.length(); i++){
            if (sn.charAt(i) != answers[3].charAt(i))
                numberOfIncorrectDetections++;
        }

        total += script.getQrCodeProperties().getNumberOfQuestions()*2;

        int[] marks = Detection.detectTestMarks(script, true);
        for (int i = 0; i <marks.length; i++){
            if (marks[i] != ans[i])
                numberOfIncorrectDetections++;
        }

        Assert.assertEquals(numberOfIncorrectDetections - before, 0);
    }


    @Test
    public void TestCSDeptscan1() throws IOException {
        String path = testLocation +  "CSdepartment/badquality-5.jpg";

        char[] ans = {'J','I','H','G','F','G','H','I','J','A','B','C','D','E','F','G','H','I','J','E','C','G','C','E','G','I','E','I','J','G'};


        long t = System.nanoTime();
        Preprocessor pre = new Preprocessor(path);
        ScriptObject script = pre.process(t);

        String sn = Detection.detectStudentNumber(script, true);
        int before = numberOfIncorrectDetections;
        total += 9;
        for(int i = 0; i < sn.length(); i++){
            if (sn.charAt(i) != answers[4].charAt(i))
                numberOfIncorrectDetections++;
        }

        total += script.getQrCodeProperties().getNumberOfQuestions();

        char[] chars = Detection.detectQuizMarks(script, true);
        for (int i = 0; i <ans.length; i++){
            if (chars[i] != ans[i])
                numberOfIncorrectDetections++;
        }

        Assert.assertEquals(numberOfIncorrectDetections - before, 0);
    }

    @Test
    public void TestCSDeptscan2() throws IOException {
        String path = testLocation +  "CSdepartment/l-4.jpg";

        char[] ans = {'I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J','I','J'};


        long t = System.nanoTime();
        Preprocessor pre = new Preprocessor(path);
        ScriptObject script = pre.process(t);

        String sn = Detection.detectStudentNumber(script, true);
        int before = numberOfIncorrectDetections;
        total += 9;
        for(int i = 0; i < sn.length(); i++){
            if (sn.charAt(i) != answers[5].charAt(i))
                numberOfIncorrectDetections++;
        }

        total += script.getQrCodeProperties().getNumberOfQuestions();

        char[] chars = Detection.detectQuizMarks(script, true);
        for (int i = 0; i <ans.length; i++){
            if (chars[i] != ans[i])
                numberOfIncorrectDetections++;
        }
        System.out.println("Testing different scanners:\t\ttesting 2 scannable App scans, 2 scans from Olivers scanner, 2 scans from CS dept (6 TOTAL) \nACCURACY: \t\t\t\t\t\t" + (100 - (numberOfIncorrectDetections *100/total)) + "%\n");


        Assert.assertEquals(numberOfIncorrectDetections - before, 0);
    }



}
