package detection;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import util.ScriptObject;

import java.awt.image.BufferedImage;

public class Detection {

    public static boolean detectStudentNumber(ScriptObject script){
        // TODO
        return false;
    }

    public static Result readQRCode(BufferedImage bi){
        BinaryBitmap binaryBitmap;
        Result result;
        try{
            binaryBitmap = new BinaryBitmap( new HybridBinarizer(new BufferedImageLuminanceSource( bi )));
            result = new QRCodeReader().decode(binaryBitmap);
            return result;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public static boolean detectQRCode(ScriptObject script){


        // TODO
        return false;
    }

    public static boolean detectQuestionMarks(ScriptObject script){
        // TODO
        return false;
    }
}
