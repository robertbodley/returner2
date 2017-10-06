import detection.Detection;
import util.Preprocessor;
import util.QRCode;
import util.ScriptObject;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class RecognitionModule {

    /**
     * Main method of the program that has the main control loop
     * Loops through all PDFs in the batch passed in.
     *
     * @param args command line arguments passed in
     * @throws IOException if the file can't be found.
     */
    public static void main(String[] args) throws IOException {
        boolean debugMode = false;
        String PDFPath = "";

        if (args.length < 1){
            System.out.println("Incorrect usage. Please specify path to PDF as a command line argument");
            System.exit(0);
        }


        if (args.length > 1){
            if (args[1].equals("debug"))
                debugMode = true;
        }
        PDFPath = args[0];

        long t = System.nanoTime();


        boolean morePages = true;
        int index = 0;
        int numberOfPages = 0;
        ProcessBuilder pro = null;
        Preprocessor preprocessor = null;
        String s = null;

        while(morePages) {

            // convert command used to convert a specific PDF page to JPEG, resizing and then applying a binary threshold to enhance
            // the amount of black on the page
            Process te = Runtime.getRuntime().exec("magick -density 300 "+ PDFPath + "[" + index + "]" + " -resize 30% -threshold 90% temp/" + index+".jpg");
            BufferedReader reader = new BufferedReader(new InputStreamReader(te.getInputStream()));

            while ((s = reader.readLine()) != null) {
                // if there are any errors
                // i.e no more pages in PDF.
                morePages = false;
            }

            reader.close();
            try {
//                wait for convert process to complete.
                te.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // start script pre processing
            preprocessor = new Preprocessor("temp/"+index+".jpg");
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

            System.out.println("Time: " + (System.nanoTime() - t )/1000000000.0);

            // Detect student number and marks
            System.out.println("Student number: " + Detection.detectStudentNumber(script, debugMode));
            if (qr.isQuizPaper()) {
                for (char m : Detection.detectQuizMarks(script, debugMode))
                    System.out.print(m +", ");
                System.out.println();

            }else {
                for(int m: Detection.detectTestMarks(script, debugMode))
                    System.out.print(m +", ");
                System.out.println();
            }

            ImageIO.write(script.getImage(), "jpg", new File(index+".jpg"));

            //update the number of pages and index used to determine the next front page to try and convert
            numberOfPages = qr.getNumberOfPages();
            index += numberOfPages;
        }

        // delete temporary JPEG images in temp folder
        File dir = new File("temp/");
        if (!debugMode) {
            for (File file : dir.listFiles())
                if (!file.isDirectory())
                    file.delete();
        }

        System.out.println("TOTAL TIME: " + (System.nanoTime() - t )/1000000000.0);
    }
}
