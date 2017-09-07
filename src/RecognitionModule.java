import com.google.zxing.Result;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import detection.Detection;
import util.Pair;
import util.Preprocessor;
import util.ScriptObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.io.*;

public class RecognitionModule {
    public static void main(String[] args) throws IOException {
        if (args.length < 2){

            // to change before final
            System.out.println("No command line args entered");
        }

        //position of reference

        BufferedImage bi = ImageIO.read(new File("C:\\Users\\Aspire\\Desktop\\CSC3003S-Quiz-700-1.jpg"));
        Result result = Detection.readQRCode(bi);
//        float posX = result.getResultPoints()[1].getX();
//        float posY = result.getResultPoints()[1].getY();
        System.out.println(result.getText());
        System.out.println(result.getResultMetadata());
        Pair[] pairs = new Pair[3];
        for (int i = 0; i < 3; i++) {
            System.out.println(result.getResultPoints()[i].getX() + " : " + result.getResultPoints()[i].getY());
            Pair a = new Pair(result.getResultPoints()[i].getX(), result.getResultPoints()[i].getY());
            pairs[i] = a;
        }
        //Detection.scaling(pairs);
        long t = System.nanoTime();
        BufferedImage bi2 = ImageIO.read(new File("C:\\Users\\Aspire\\Desktop\\B&WTest3.png"));
        Detection.detectStudentNumber(Detection.scaling(pairs),bi2);//MUST FEED IN A GREYSCALED BUFFEREDIMAGE
        System.out.println((System.nanoTime() - t )/1000000000.0);

        String PDFFilePath = "";
        Preprocessor preprocessor = new Preprocessor();
        preprocessor.splitScripts(PDFFilePath);
        preprocessor.process();
    }
}
