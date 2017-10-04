package util;

import com.google.zxing.Result;

public class QRCode {

    private Pair[] QRCodeCornerCoordinates;
    private Result QRCode;
    private Pair scalingFactor;
    private boolean quizPaper;
    private int numberOfPages;

    public QRCode(Pair[] points, Result result){
        this.QRCodeCornerCoordinates = points;
        this.QRCode = result;
        quizPaper = result.getText().contains("quiz");
        numberOfPages = Integer.parseInt(result.getText().split("---")[4]);
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
