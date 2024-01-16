package org.ucsc.railboostbackend.utilities;

import net.glxn.qrgen.javase.QRCode;
import net.glxn.qrgen.core.image.ImageType;

import java.io.ByteArrayOutputStream;

public class  QRCodeGenerator {
    public static  byte[] generateQRCode(String data){
        ByteArrayOutputStream stream = QRCode.from(data).to(ImageType.PNG).stream();
        return stream.toByteArray();
    }
}
