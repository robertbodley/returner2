package util;

public class ScriptObject {

    // not sure if would be int array or char array ?
    private int[][] rawPixels;
    private int[] marksPerQuestion;
    private QRCode qrCodeProperties;
    private String studentNumber;

    public ScriptObject(int[][] rawPixels){
        this.rawPixels = rawPixels;
    }

    public String toString() {
        /*
        output script contents correctly for web app to process
         */
        return "";
    }

    public int[][] getRawPixels() {
        return rawPixels;
    }

    public int[] getMarksPerQuestion() {
        return marksPerQuestion;
    }

    public void setMarksPerQuestion(int[] marksPerQuestion) {
        this.marksPerQuestion = marksPerQuestion;
    }

    public QRCode getQrCodeProperties() {
        return qrCodeProperties;
    }

    public void setQrCodeProperties(QRCode qrCodeProperties) {
        this.qrCodeProperties = qrCodeProperties;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
}
