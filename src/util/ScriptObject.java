package util;

import java.awt.image.BufferedImage;

public class ScriptObject {

    private int[] marksPerQuestion;
    private QRCode qrCodeProperties;
    private String studentNumber;
    private BufferedImage image;

    public ScriptObject(BufferedImage img){
        this.image = img;
    }

    public String toString() {
        /*
        output script contents correctly for web app to process
         */
        return "";
    }

    public BufferedImage getImage() {
        return image;
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
