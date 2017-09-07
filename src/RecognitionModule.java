import com.google.zxing.Result;
import detection.Detection;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import util.Pair;
import util.Preprocessor;
import util.Straighten;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class RecognitionModule {
    public static void main(String[] args) throws IOException {
        if (args.length < 2){

            // to change before final
            System.out.println("No command line args entered");
        }

        String imgFile = "../scans/out.png";
        long t = System.nanoTime();
        BufferedImage bi = ImageIO.read(new File(imgFile));
        Result result = Detection.readQRCode(bi);
        System.out.println(result.getText());
        Pair[] coords = new Pair[3];

        for (int i = 0; i < 3; i++) {
            coords[i] = new Pair(result.getResultPoints()[i].getX(), result.getResultPoints()[i].getY());
            System.out.println(i + coords[i].toString());
        }
//         bi = ImageIO.read(new File( "../imgs/180.jpg"));
//         result = Detection.readQRCode(bi);
//        System.out.println("\n-----------------\n");
//        for (int i = 0; i < 3; i++) {
//            coords[i] = new Pair(result.getResultPoints()[i].getX(), result.getResultPoints()[i].getY());
//            System.out.println(coords[i]);
//        }

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        double angle = 0.0;

        if (coords[2].getX() - coords[1].getX() != 0) {
            angle = Math.atan(
                    Math.abs(
                            (coords[0].getY() - coords[1].getY()) / (coords[0].getX() - coords[1].getX())
                    )
            );
        }
        angle = Math.toDegrees(angle);

        if (coords[1].getY() > coords[2].getY()){
            angle += 180;
        }

        System.out.println(angle);


        Straighten ti = new Straighten();
        Mat image = Imgcodecs.imread(imgFile);

        Mat test = ti.straightenImage(image, angle);
        Imgcodecs.imwrite("straightImage.jpg", test);
        System.out.println((System.nanoTime() - t )/1000000000.0);

/*
        File file = new File("../imgs/rot.png");
//        File file = new File("p.RAW");

        ByteArrayOutputStream baos=new ByteArrayOutputStream(1000);
        BufferedImage img= ImageIO.read(file);
        img.getSource();
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
