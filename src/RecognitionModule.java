import com.sun.prism.shader.Solid_TextureFirstPassLCD_AlphaTest_Loader;
import detection.Detection;
import util.Preprocessor;
import util.QRCode;
import util.ScriptObject;

import javax.imageio.IIOException;
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
        long t = System.nanoTime();

//        String imgFile = "../newer/scans/out30.jpg";
        String path = new File(".").getCanonicalPath();

        String PDFPath = "/newer/scans/plss.pdf";
        PDFPath = "C:/Users/Oliver/Desktop/project" + PDFPath;

        boolean morePages = true, firstIteration = true;
        int index = 0;
        int numberOfPages = 0;
        ProcessBuilder pro = null;
        Preprocessor preprocessor = null;
        String s = null;
        while(morePages) {
            pro = new ProcessBuilder(
                    "cmd.exe",
                    "/c",
                    "convert -density 300 "+ PDFPath + "[" + index + "]" + " -resize 30% -threshold 90% " + index+".jpg"
            );
            Process te = pro.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(te.getInputStream()));
            pro.redirectErrorStream();
            while ((s = reader.readLine()) != null) {
                System.out.println("ERROR" + s);
                morePages = false;
            }
            reader.close();

            try {
                te.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            preprocessor = new Preprocessor(index+".jpg");
            ScriptObject script = null;
            try {
                script = preprocessor.process(t);
            }catch(IIOException ex){
                morePages = false;
                break;
            }
            if (script == null){
                index += numberOfPages ;
                continue;
            }
            QRCode qr = script.getQrCodeProperties();

            System.out.println("Time after script  " +index+ ": " + (System.nanoTime() - t )/1000000000.0);

            System.out.println(Detection.detectStudentNumber(script));
            if (qr.isQuizPaper())
                Detection.detectQuizMarks(script);
            else
                Detection.detectTestMarks(script);
            numberOfPages = qr.getNumberOfPages();
            index += numberOfPages;

        }

        System.out.println("TOTAL TIME: " + (System.nanoTime() - t )/1000000000.0);
    }
}
