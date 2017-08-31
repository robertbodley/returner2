import detection.Detection;
import util.Preprocessor;
import util.ScriptObject;

public class RecognitionModule {
    public static void main(String[] args) {
        if (args.length < 2){

            // to change before final
            System.out.println("No command line args entered");
        }

        String PDFFilePath = "";
        Preprocessor preprocessor = new Preprocessor();

        preprocessor.splitScripts(PDFFilePath);
        preprocessor.process();

        for (ScriptObject script : preprocessor.getScripts()){
            Detection.detectStudentNumber(script);
            Detection.detectQuestionMarks(script);
        }
    }
}
