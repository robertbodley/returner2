package util;

import com.google.zxing.Result;

public class QRCode {

    private double scalingFactor;
    private Pair[] QRCodeCornerCoordinates;
    private Result QRCode;

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


}
