package com.wjh.utils;

import com.alibaba.fastjson.util.IOUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;

/**
 * 二维码工具类
 * Base64
 * @author
 * @create 2017-08-16
 **/
public class QRUtil {

    private static final Logger logger = LogManager.getLogger(QRUtil.class);

    /**
     * 生成二维码
     *
     * @param contents 内容
     * @param width    二维码宽度
     * @param height   二维码高度
     */
    public static String generateQR(String contents, int width, int height) throws Exception {
        Hashtable<Object, Object> hints = new Hashtable<Object, Object>();
        // 指定纠错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        // 指定编码格式
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        try {
            BitMatrix byteMatrix;
            byteMatrix = new MultiFormatWriter().encode(new String(contents.getBytes("UTF-8"), "iso-8859-1"), BarcodeFormat.QR_CODE, width, height);
            MatrixToImageWriter.writeToStream(byteMatrix, "png", bao);
            return Base64Code(bao.toByteArray());
        } catch (Exception e) {
            logger.error("获取二维码流异常", e);
            throw new Exception("生成二维码时异常");
        } finally {
            IOUtils.close(bao);
        }
    }

    /**
     * 生成base64的二维码
     *
     * @param b
     * @return
     */
    public static String Base64Code(byte[] b) {
        BASE64Encoder encoder = new BASE64Encoder();
        String codeBase64 = "";
        StringBuilder pictureBuffer = new StringBuilder();
        pictureBuffer.append(encoder.encode(b));
//        System.out.println(pictureBuffer.toString());
        codeBase64 = pictureBuffer.toString();
        return codeBase64;
    }

}
