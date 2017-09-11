import detection.Detection;
import util.Pair;
import util.Preprocessor;
import util.QRCode;
import util.ScriptObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class RecognitionModule {
    public static void main(String[] args) throws IOException {
        boolean debugMode = false;
        if (args.length > 1){
            
            if (args[1].equals("debug"))
                debugMode = true;
        }

        String imgFile = "../scans/base.jpg";
        long t = System.nanoTime();

        Preprocessor preprocessor = new Preprocessor(imgFile);
        preprocessor.process(t);
        ScriptObject script = preprocessor.getScript(0);
        QRCode qr = script.getQrCodeProperties();

        System.out.println("Time after rotation and QR detection: " + (System.nanoTime() - t )/1000000000.0);

        BufferedImage bi = script.getImage();
        Detection.detectStudentNumber(bi, qr);
        ImageIO.write(bi, "jpg" ,new File("t.jpg"));

        System.out.println("Time after student number detection: " + (System.nanoTime() - t )/1000000000.0);
    }
}
