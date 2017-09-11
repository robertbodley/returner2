package util;

import com.google.zxing.Result;

public class QRCode {

    private Pair[] QRCodeCornerCoordinates;
    private Result QRCode;
    private Pair scalingFactor;

    public QRCode(Pair[] points, Result result){
        this.QRCodeCornerCoordinates = points;
        this.QRCode = result;
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

    public void setScalingFactor(Pair sf){
        this.scalingFactor = sf;
    }

    public Pair getScalingFactor(){
        return scalingFactor;
    }


}
