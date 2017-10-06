import detection.Detection;
import org.junit.Assert;
import org.junit.Test;
import util.Preprocessor;
import util.ScriptObject;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class TestStudentNumberDetectionAccuracy {
    String[] answers = {"BDLROB001", "PJND_D019", "JJZAYJ039", "WXYZAZ888", "WXYZAZ888"
    ,"BDLRJM199", "BABABA001", "YZYZYZ089", "BDLROB001", "VNTAND006"};
    String testLocation ="testResources/accuracy/";

    static int total = 0;
    static int numberOfIncorrectDetections = 0;

    @Test
    public void TestStudentNumberDetectionAccuracy() throws IOException {
        int testn = 0;
        long t = System.nanoTime();
        String path = testLocation + "l-" + testn + ".jpg";

        Preprocessor preprocessor = new Preprocessor(path);
        ScriptObject script = preprocessor.process(t);


        String sn = Detection.detectStudentNumber(script, false);

        total += 9;
        for(int i = 0; i < sn.length(); i++){
            if (sn.charAt(i) != answers[testn].charAt(i))
                numberOfIncorrectDetections++;
        }
//        ImageIO.write(script.getImage(), "jpg", new File("output" + testn+".jpg"));
        Assert.assertTrue(sn.equals(answers[testn]));
    }


    @Test
    public void TestStudentNumberDetectionAccuracy1() throws IOException {
        int testn = 1;

        long t = System.nanoTime();
        String path = testLocation + "l-" + testn + ".jpg";

        Preprocessor preprocessor = new Preprocessor(path);
        ScriptObject script = preprocessor.process(t);

        String sn = Detection.detectStudentNumber(script, false);
        total += 9;
        for(int i = 0; i < sn.length(); i++){
            if (sn.charAt(i) != answers[testn].charAt(i))
                numberOfIncorrectDetections++;
        }
        Assert.assertTrue(sn.equals(answers[testn]));
    }


    @Test
    public void TestStudentNumberDetectionAccuracy2() throws IOException {
        int testn = 2;

        long t = System.nanoTime();
        String path = testLocation + "l-" + testn  +".jpg";

        Preprocessor preprocessor = new Preprocessor(path);
        ScriptObject script = preprocessor.process(t);
        String sn = Detection.detectStudentNumber(script, true);
        total += 9;
        for(int i = 0; i < sn.length(); i++){
            if (sn.charAt(i) != answers[testn].charAt(i))
                numberOfIncorrectDetections++;
        }
        Assert.assertTrue(sn.equals(answers[testn]));
    }

    @Test
    public void TestStudentNumberDetectionAccuracy3() throws IOException {
        int testn = 3;

        long t = System.nanoTime();
        String path = testLocation + "l-" + testn  +".jpg";

        Preprocessor preprocessor = new Preprocessor(path);
        ScriptObject script = preprocessor.process(t);

        String sn = Detection.detectStudentNumber(script, true);
        total += 9;
        for(int i = 0; i < sn.length(); i++){
            if (sn.charAt(i) != answers[testn].charAt(i))
                numberOfIncorrectDetections++;
        }
        Assert.assertTrue(sn.equals(answers[testn]));
    }

    @Test
    public void TestStudentNumberDetectionAccuracy4() throws IOException {
        int testn = 4;

        long t = System.nanoTime();
        String path = testLocation + "l-" + testn  +".jpg";

        Preprocessor preprocessor = new Preprocessor(path);
        ScriptObject script = preprocessor.process(t);

        String sn = Detection.detectStudentNumber(script, false);
        total += 9;
        for(int i = 0; i < sn.length(); i++){
            if (sn.charAt(i) != answers[testn].charAt(i))
                numberOfIncorrectDetections++;
        }
        Assert.assertTrue(sn.equals(answers[testn]));
    }

    @Test
    public void TestStudentNumberDetectionAccuracy5() throws IOException {
        int testn = 5;

        long t = System.nanoTime();
        String path = testLocation + "badquality-" + testn  +".jpg";

        Preprocessor preprocessor = new Preprocessor(path);
        ScriptObject script = preprocessor.process(t);

        String sn = Detection.detectStudentNumber(script, false);
        total += 9;
        for(int i = 0; i < sn.length(); i++){
            if (sn.charAt(i) != answers[testn].charAt(i))
                numberOfIncorrectDetections++;
        }
        Assert.assertTrue(sn.equals(answers[testn]));
    }
    @Test
    public void TestStudentNumberDetectionAccuracy6() throws IOException {
        int testn = 6;

        long t = System.nanoTime();
        String path = testLocation + "badquality-" + testn  +".jpg";

        Preprocessor preprocessor = new Preprocessor(path);
        ScriptObject script = preprocessor.process(t);

        String sn = Detection.detectStudentNumber(script, false);
        total += 9;
        for(int i = 0; i < sn.length(); i++){
            if (sn.charAt(i) != answers[testn].charAt(i))
                numberOfIncorrectDetections++;
        }
        Assert.assertTrue(sn.equals(answers[testn]));
    }

    @Test
    public void TestStudentNumberDetectionAccuracy7() throws IOException {
        int testn = 7;

        long t = System.nanoTime();
        String path = testLocation + "badquality-" + testn  +".jpg";

        Preprocessor preprocessor = new Preprocessor(path);
        ScriptObject script = preprocessor.process(t);

        String sn = Detection.detectStudentNumber(script, false);
        total += 9;
        for(int i = 0; i < sn.length(); i++){
            if (sn.charAt(i) != answers[testn].charAt(i))
                numberOfIncorrectDetections++;
        }
        Assert.assertTrue(sn.equals(answers[testn]));
    }

    @Test
    public void TestStudentNumberDetectionAccuracy8() throws IOException {
        int testn = 8;

        long t = System.nanoTime();
        String path = testLocation + "badquality-" + testn  +".jpg";

        Preprocessor preprocessor = new Preprocessor(path);
        ScriptObject script = preprocessor.process(t);

        String sn = Detection.detectStudentNumber(script, false);
        total += 9;
        for(int i = 0; i < sn.length(); i++){
            if (sn.charAt(i) != answers[testn].charAt(i))
                numberOfIncorrectDetections++;
        }

        Assert.assertTrue(sn.equals(answers[testn]));
    }


    @Test
    public void TestStudentNumberDetectionAccuracy9() throws IOException {
        int testn = 9;

        long t = System.nanoTime();
        String path = testLocation + "badquality-" + testn  +".jpg";

        Preprocessor preprocessor = new Preprocessor(path);
        ScriptObject script = preprocessor.process(t);

        String sn = Detection.detectStudentNumber(script, false);
        total += 9;
        for(int i = 0; i < sn.length(); i++){
            if (sn.charAt(i) != answers[testn].charAt(i))
                numberOfIncorrectDetections++;
        }
        System.out.println("Student number detection:\t\t10 scripts tested \nACCURACY: \t\t\t\t\t\t" + (100 - (numberOfIncorrectDetections *100/total)) + "%\n");

        Assert.assertTrue(sn.equals(answers[testn]));
    }







}
