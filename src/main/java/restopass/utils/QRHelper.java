package restopass.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class QRHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(QRHelper.class);

    public String createQRBase64(String content) {
        ByteArrayOutputStream createQrCodeImg = null;
        try {
            createQrCodeImg = this.getQRCodeImage(content, 10, 10, "JPEG");
        } catch (WriterException | IOException e) {
            LOGGER.error("Error generating QR code for: {}", content);
        }

        BASE64Encoder encoder = new BASE64Encoder();
        String base64Img = encoder.encode(createQrCodeImg.toByteArray());

        return String.format("data:image/jpeg;base64,%s", base64Img.replaceAll("\r|\n", ""));
    }

    private ByteArrayOutputStream getQRCodeImage(String text, int width, int height, String imgFormat) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream jpgOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, imgFormat, jpgOutputStream);
        return jpgOutputStream;
    }

}
