import detection.Detection;
import org.junit.Assert;
import org.junit.Test;
import util.Preprocessor;
import util.ScriptObject;

import javax.imageio.ImageIO;
import javax.swing.table.DefaultTableCellRenderer;
import java.io.File;
import java.io.IOException;


public class TestScriptMarksDetectionAccuracy {

    String testLocation ="testResources/accuracy/";

    static int total = 0;
    static int numberOfIncorrectDetections = 0;

    @Test
    public void TestScriptMarksDetectionAccuracy() throws IOException {

        int[] ans = {12,11,20,10,1,12};
        long t = System.nanoTime();
        String path = testLocation + "l-0.jpg";

        Preprocessor preprocessor = new Preprocessor(path);
        ScriptObject script = preprocessor.process(t);

        int before = numberOfIncorrectDetections;
        total += script.getQrCodeProperties().getNumberOfQuestions()*2;
        int[] marks = Detection.detectTestMarks(script, true);

        for (int i = 0 ;i < marks.length ; i++){
            if(marks[i] != ans[i])
                numberOfIncorrectDetections++;
        }


        Assert.assertEquals(numberOfIncorrectDetections - before, 0);
    }

    @Test
    public void TestScriptMarksDetectionAccuracy1() throws IOException {

        int[] ans = {11,9,23,6};
        long t = System.nanoTime();
        String path = testLocation + "l-2.jpg";

        Preprocessor preprocessor = new Preprocessor(path);
        ScriptObject script = preprocessor.process(t);

        int before = numberOfIncorrectDetections;
        total += script.getQrCodeProperties().getNumberOfQuestions()*2;
        int[] marks = Detection.detectTestMarks(script, true);

        for (int i = 0 ;i < marks.length ; i++){
            if(marks[i] != ans[i])
                numberOfIncorrectDetections++;
        }


        Assert.assertEquals(numberOfIncorrectDetections - before, 0);
    }

    @Test
    public void TestScriptMarksDetectionAccuracy2() throws IOException {

        int[] ans = {88,66,44,22,0,0,11,22,33,44};
        long t = System.nanoTime();
        String path = testLocation + "l-3.jpg";

        Preprocessor preprocessor = new Preprocessor(path);
        ScriptObject script = preprocessor.process(t);

        int before = numberOfIncorrectDetections;
        total += script.getQrCodeProperties().getNumberOfQuestions()*2;
        int[] marks = Detection.detectTestMarks(script, true);

        for (int i = 0 ;i < marks.length ; i++){
            if(marks[i] != ans[i])
                numberOfIncorrectDetections++;
        }

        System.out.println("test script marks accuracy:\t\t3 scripts test \nACCURACY: \t\t\t\t\t\t" + (100 - (numberOfIncorrectDetections *100/total)) + "%\n");

        Assert.assertEquals(numberOfIncorrectDetections - before, 0);
    }
}
