import detection.Detection;
import org.junit.Assert;
import org.junit.Test;
import util.Preprocessor;
import util.QRCode;
import util.ScriptObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TestAccuracy {
    String[] answers = {"BDLROB001", "PJND_D019", "JJZAYJ039", "WXYZAZ888", "WXYZAZ888"};


    @Test
    public void TestAccuracy() throws IOException {
        int testn = 0;
        String prefix ="../newer/scans/";


        long t = System.nanoTime();
        String path = prefix + "l-" + testn + ".jpg";
        System.out.println(path);

        Preprocessor preprocessor = new Preprocessor(path);
        preprocessor.process(t);
        ScriptObject script = preprocessor.getScript(0);

        QRCode qr = script.getQrCodeProperties();
        System.out.println("ASSERTING");
        String sn = Detection.detectStudentNumber(script);
        System.out.println(sn);
        System.out.println((System.nanoTime() - t)/1000000000.0);

        Assert.assertTrue(sn.equals(answers[testn]));
    }


    @Test
    public void TestAccuracy1() throws IOException {
        int testn = 1;
        String prefix ="../newer/scans/";


        long t = System.nanoTime();
        String path = prefix + "l-" + testn + ".jpg";
        System.out.println(path);

        Preprocessor preprocessor = new Preprocessor(path);
        preprocessor.process(t);
        ScriptObject script = preprocessor.getScript(0);

        QRCode qr = script.getQrCodeProperties();
        System.out.println("ASSERTING");
        String sn = Detection.detectStudentNumber(script);
        System.out.println(sn);
        System.out.println((System.nanoTime() - t)/1000000000.0);

        Assert.assertTrue(sn.equals(answers[testn]));
    }


    @Test
    public void TestAccuracy2() throws IOException {
        int testn = 2;
        String prefix ="../newer/scans/";


        long t = System.nanoTime();
        String path = prefix + "l-" + testn  +".jpg";
        System.out.println(path);

        Preprocessor preprocessor = new Preprocessor(path);
        preprocessor.process(t);
        ScriptObject script = preprocessor.getScript(0);

        QRCode qr = script.getQrCodeProperties();
        System.out.println("ASSERTING");
        String sn = Detection.detectStudentNumber(script);
        BufferedImage bi = script.getImage();
        ImageIO.write( bi ,"jpg", new File("outest.jpg"));
        System.out.println(sn);
        System.out.println((System.nanoTime() - t)/1000000000.0);

        Assert.assertTrue(sn.equals(answers[testn]));
    }






}
