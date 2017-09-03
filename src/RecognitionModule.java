import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import detection.Detection;
import util.Preprocessor;
import util.ScriptObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class RecognitionModule {
    public static void main(String[] args) throws IOException {
        if (args.length < 2){

            // to change before final
            System.out.println("No command line args entered");
        }

        File file = new File("p.png");
//        File file = new File("p.RAW");

        ByteArrayOutputStream baos=new ByteArrayOutputStream(1000);
        BufferedImage img= ImageIO.read(file);
        ImageIO.write(img, "png", baos);
        baos.flush();

        String base64String=Base64.encode(baos.toByteArray());
        baos.close();

        byte[] bytearray = Base64.decode(base64String);
        for(byte b : bytearray){
            System.out.println(b);
        }

       /* FileInputStream fis = new FileInputStream(file);
        //create FileInputStream which obtains input bytes from a file in a file system
        //FileInputStream is meant for reading streams of raw bytes such as image data. For reading streams of characters, consider using FileReader.

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                //Writes to this byte array output stream
                bos.write(buf, 0, readNum);
                System.out.println("read " + readNum + " bytes,");
//                if buf
                for (byte b: buf
                     ) {
                    System.out.print((int) b+" ");
                }
                System.out.println();
            }
        } catch (IOException ex) {
            System.out.println("FAILED TO OPEN");
        }

        byte[] bytes = bos.toByteArray();
//        for (byte b : bytes)
//            System.out.println(b);
*/

        String PDFFilePath = "";
        Preprocessor preprocessor = new Preprocessor();

        preprocessor.splitScripts(PDFFilePath);
        preprocessor.process();
/*
        for (ScriptObject script : preprocessor.getScripts()){
            Detection.detectStudentNumber(script);
            Detection.detectQuestionMarks(script);
        }*/
    }
}
