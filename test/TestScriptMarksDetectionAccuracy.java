import detection.Detection;
import org.junit.Assert;
import org.junit.Test;
import util.Preprocessor;
import util.ScriptObject;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


public class TestScriptMarksDetectionAccuracy {

    String[] answers = {"BDLROB001", "PJND_D019", "JJZAYJ039", "WXYZAZ888", "WXYZAZ888"
            ,"BDLRJM199", "BABABA001", "YZYZYZ089", "BDLROB001", "VNTAND006"};
    String testLocation ="testResources/accuracy/";

    int total = 0;
    int numberOfIncorrect = 0;

    @Test
    public void TestScriptMarksDetectionAccuracy() throws IOException {

        int testn = 9;

        long t = System.nanoTime();
        String path = testLocation + "badquality-" + testn  +".jpg";

        Preprocessor preprocessor = new Preprocessor(path);
        ScriptObject script = preprocessor.process(t);

        String sn = Detection.detectStudentNumber(script, false);
        total += 9;
        for(int i = 0; i < sn.length(); i++){
            if (sn.charAt(i) != answers[testn].charAt(i))
                numberOfIncorrect++;
        }

        System.out.println("Student number detection:\t10 scripts test \nACCURACY: \t\t\t\t\t" + (100 - (numberOfIncorrect*100/total)) + "%\n");
        ImageIO.write(script.getImage(), "jpg", new File("output" + testn+".jpg"));

        Assert.assertTrue(sn.equals(answers[testn]));

    }
}
