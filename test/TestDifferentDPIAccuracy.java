import detection.Detection;
import org.junit.Assert;
import org.junit.Test;
import util.Preprocessor;
import util.ScriptObject;

import java.io.IOException;

/**
 * Created by Oliver on 2017/10/06.
 */
public class TestDifferentDPIAccuracy {
    String testLocation ="testResources/dpi/";
    String[] answers = {"DBROLI001", "BDLROB001"};

    static int total = 0;
    static int numberOfIncorrectDetections = 0;

    @Test
    public void TestDifferentDPIAccuracy() throws IOException {
        String path = testLocation +  "100dpi-0.jpg";

        int[] ans = {99,88,77,66,55,0,12,34,56,78};
        long t = System.nanoTime();
        Preprocessor pre = new Preprocessor(path);
        ScriptObject script = pre.process(t);

        String sn = Detection.detectStudentNumber(script, true);
        int before = numberOfIncorrectDetections;
        total += 9;
        for(int i = 0; i < sn.length(); i++){
            if (sn.charAt(i) != answers[0].charAt(i))
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
    public void TestDifferentDPIAccuracy1() throws IOException {
        String path = testLocation +  "100dpi-1.jpg";

        int[] ans = {98,76,54,32,10,9,9,9,0,90};
        long t = System.nanoTime();
        Preprocessor pre = new Preprocessor(path);
        ScriptObject script = pre.process(t);

        String sn = Detection.detectStudentNumber(script, true);
        int before = numberOfIncorrectDetections;
        total += 9;
        for(int i = 0; i < sn.length(); i++){
            if (sn.charAt(i) != answers[1].charAt(i))
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
    public void TestDifferentDPIAccuracy2() throws IOException {
        String path = testLocation +  "200dpi-0.jpg";

        int[] ans = {99,88,77,66,55,0,12,34,56,78};
        long t = System.nanoTime();
        Preprocessor pre = new Preprocessor(path);
        ScriptObject script = pre.process(t);

        String sn = Detection.detectStudentNumber(script, true);
        int before = numberOfIncorrectDetections;
        total += 9;
        for(int i = 0; i < sn.length(); i++){
            if (sn.charAt(i) != answers[0].charAt(i))
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
    public void TestDifferentDPIAccuracy4() throws IOException {
        String path = testLocation +  "200dpi-1.jpg";

        int[] ans = {98,76,54,32,10,9,9,9,0,90};
        long t = System.nanoTime();
        Preprocessor pre = new Preprocessor(path);
        ScriptObject script = pre.process(t);

        String sn = Detection.detectStudentNumber(script, true);
        int before = numberOfIncorrectDetections;
        total += 9;
        for(int i = 0; i < sn.length(); i++){
            if (sn.charAt(i) != answers[1].charAt(i))
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
    public void TestDifferentDPIAccuracy3() throws IOException {
        String path = testLocation +  "300dpi-0.jpg";

        int[] ans = {99,88,77,66,55,0,12,34,56,78};
        long t = System.nanoTime();
        Preprocessor pre = new Preprocessor(path);
        ScriptObject script = pre.process(t);

        String sn = Detection.detectStudentNumber(script, true);
        int before = numberOfIncorrectDetections;
        total += 9;
        for(int i = 0; i < sn.length(); i++){
            if (sn.charAt(i) != answers[0].charAt(i))
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
    public void TestDifferentDPIAccuracy5() throws IOException {
        String path = testLocation +  "300dpi-1.jpg";

        int[] ans = {98,76,54,32,10,9,9,9,0,90};
        long t = System.nanoTime();
        Preprocessor pre = new Preprocessor(path);
        ScriptObject script = pre.process(t);

        String sn = Detection.detectStudentNumber(script, true);
        int before = numberOfIncorrectDetections;
        total += 9;
        for(int i = 0; i < sn.length(); i++){
            if (sn.charAt(i) != answers[1].charAt(i))
                numberOfIncorrectDetections++;
        }

        total += script.getQrCodeProperties().getNumberOfQuestions()*2;

        int[] marks = Detection.detectTestMarks(script, true);
        for (int i = 0; i <marks.length; i++){
            if (marks[i] != ans[i])
                numberOfIncorrectDetections++;
        }

        System.out.println("DPI robustness test:\t\t\t2 scripts tested at 100, 200 and 300 DPI each (6 tests) \nACCURACY: \t\t\t\t\t\t" + (100 - (numberOfIncorrectDetections *100/total)) + "%\n");

        Assert.assertEquals(numberOfIncorrectDetections - before, 0);
    }



}
