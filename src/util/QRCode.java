package util;

import com.google.zxing.Result;

public class QRCode {
    /*
        This class stores information about the detected QR code on a script.
     */

    private Pair[] QRCodeCornerCoordinates;
    private Result QRCode;
    private Pair scalingFactor;
    private boolean quizPaper;
    private int numberOfPages;
    private int numberOfQuestions;
    private int numberOfAnswersPerQuestion;


    public QRCode(Pair[] points, Result result){
        /*
            Initializes a QR code object with default values.
         */
        this.QRCodeCornerCoordinates = points;
        this.QRCode = result;
        quizPaper = result.getText().contains("quiz");
        String[] data = result.getText().split("---");
        numberOfPages = Integer.parseInt(data[4]);
        numberOfQuestions = Integer.parseInt(data[1]);
        numberOfAnswersPerQuestion = Integer.parseInt(data[2]);

    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public int getNumberOfAnswersPerQuestion() {
        return numberOfAnswersPerQuestion;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public String getData(){
        return QRCode.getText();
    }

    public Pair[] getQRCodeCornerCoordinates(){
        return QRCodeCornerCoordinates;
    }

    public Result getResult(){
        return QRCode;
    }

    public boolean isQuizPaper(){
        return quizPaper;
    }

    public void setScalingFactor(Pair sf){
        this.scalingFactor = sf;
    }

    public Pair getScalingFactor(){
        return scalingFactor;
    }


}
