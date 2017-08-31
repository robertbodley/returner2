package util;

public class Preprocessor {
    private static int DPI;
    private ScriptObject[] scripts;
    private int numberOfPages;
    private int[] maxMarksPerQuestion;


    public Preprocessor(){
    }

    public void process(){
        // do actual preprocessing on array of script front covers
    }

    public void splitScripts(String PDFFilePath){
        // TODO: detect and decode a QR code to determine number of pages to split by

        /*
        split scripts and store only front page/cover of each script
         */

    }

    public ScriptObject[] getScripts(){
        // intentionally return actual reference to object
        return scripts;
    }

    public void setNumberOfPages(int numberOfPages){
        this.numberOfPages = numberOfPages;
    }

    public void setMaxMarksPerQuestion(int[] maxMarksPerQuestion){
        this.maxMarksPerQuestion = maxMarksPerQuestion;
    }


}
