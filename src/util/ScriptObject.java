package util;

import java.awt.image.BufferedImage;

public class ScriptObject {
    /*
        Script object stores information about a script front cover including the QR code and the actual image.
     */
    private QRCode qrCodeProperties;
    private BufferedImage image;

    public ScriptObject(BufferedImage img){
        this.image = img;
    }

    public BufferedImage getImage() {
        return image;
    }

    public QRCode getQrCodeProperties() {
        return qrCodeProperties;
    }

    public void setQrCodeProperties(QRCode qrCodeProperties) {
        this.qrCodeProperties = qrCodeProperties;
    }
}
