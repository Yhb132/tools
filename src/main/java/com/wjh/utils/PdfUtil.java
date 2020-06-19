package com.wjh.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Pdf util.
 *
 * @author
 * @since 2020 /4/23
 */
@Slf4j
public class PdfUtil {
    private static final String IMAGE_DIR_PATH = "/data/QQQ/temp/reviewdown/";

    /**
     * PDF转图片
     *
     * @param pdfUrl the pdf url
     * @return string string
     */
    public static String uploadToOss(String pdfUrl, String picName) {

        HttpURLConnection conn = null;
        ByteArrayOutputStream output = null;
        ByteArrayInputStream picStream = null;
        try {
            // 将pdf上传至oss
            URL url = new URL(pdfUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(20 * 1000);
            output = new ByteArrayOutputStream();
            IOUtils.copy(conn.getInputStream(), output);

            // 将pdf转图片上传至oss
            picStream = new ByteArrayInputStream(output.toByteArray());
            FileUtil.createDirs(IMAGE_DIR_PATH);
            String imageFilePath = IMAGE_DIR_PATH + picName;

            pdfToImage(picStream, imageFilePath);
            FileInputStream fis = new FileInputStream(imageFilePath);
//            UploadAliyunUtils.uploadPicture(fis, (long) fis.available(), UploadAliyunUtils.BUCKET_NAME, picName);
            FileUtil.delFile(imageFilePath);
        } catch (Exception e) {
            log.error("ID:{},文件上传OSS异常", e);
            return null;
        } finally {
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Exception e) {
                    log.error("关闭url链接异常:, e", e);
                }
            }

            if (output != null) {
                try {
                    output.close();
                } catch (Exception e) {
                    log.error("关闭流异常:, e", e);
                }
            }
            if (picStream != null) {
                try {
                    picStream.close();
                } catch (Exception e) {
                    log.error("关闭流异常:, e", e);
                }
            }
        }

        return "ImageConstant.OSS_PATH" + picName;
    }

    /**
     * pdf转成一张图片
     *
     * @param pdfStream the pdf stream
     * @param outPath   the outpath
     */
    public static void pdfToImage(InputStream pdfStream, String outPath) {
        try {
            PDDocument pdf = PDDocument.load(pdfStream);
            int actSize = pdf.getNumberOfPages();
            List<BufferedImage> piclist = new ArrayList<>();
            for (int i = 0; i < actSize; i++) {
                BufferedImage image = new PDFRenderer(pdf).renderImageWithDPI(i, 180, ImageType.RGB);
                piclist.add(image);
            }
            appendYPic(piclist, outPath);
        } catch (IOException e) {
            log.error("io exception", e);
        }
    }


    /**
     * 将宽度相同的图片，竖向追加在一起 ##注意：宽度必须相同
     *
     * @param piclist 文件流数组
     * @param outPath 输出路径
     */
    public static void appendYPic(List<BufferedImage> piclist, String outPath) {// 纵向处理图片
        if (piclist == null || piclist.size() <= 0) {
            log.info("图片数组为空!");
            return;
        }
        try {
            int height = 0, // 总高度
                    width = 0, // 总宽度
                    _height = 0, // 临时的高度 , 或保存偏移高度
                    __height = 0, // 临时的高度，主要保存每个高度
                    picNum = piclist.size();// 图片的数量
            int[] heightArray = new int[picNum]; // 保存每个文件的高度
            BufferedImage buffer = null; // 保存图片流
            List<int[]> imgRGB = new ArrayList<int[]>(); // 保存所有的图片的RGB
            int[] _imgRGB; // 保存一张图片中的RGB数据
            for (int i = 0; i < picNum; i++) {
                buffer = piclist.get(i);
                heightArray[i] = _height = buffer.getHeight();// 图片高度
                if (i == 0) {
                    width = buffer.getWidth();// 图片宽度
                }
                height += _height; // 获取总高度
                _imgRGB = new int[width * _height];// 从图片中读取RGB
                _imgRGB = buffer.getRGB(0, 0, width, _height, _imgRGB, 0, width);
                imgRGB.add(_imgRGB);
            }
            _height = 0; // 设置偏移高度为0
            // 生成新图片
            BufferedImage imageResult = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < picNum; i++) {
                __height = heightArray[i];
                if (i != 0) _height += __height; // 计算偏移高度
                imageResult.setRGB(0, _height, width, __height, imgRGB.get(i), 0, width); // 写入流中
            }
            File outFile = new File(outPath);
            ImageIO.write(imageResult, "jpg", outFile);// 写图片
        } catch (Exception e) {
            log.error("io exception", e);
        }
    }

    public static String uploadPicToOss(String picUrl, String picName) {

        HttpURLConnection conn = null;
        ByteArrayOutputStream output = null;
        ByteArrayInputStream picStream = null;
        try {
            // 将pic上传至oss
            URL url = new URL(picUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(20 * 1000);
            output = new ByteArrayOutputStream();
            IOUtils.copy(conn.getInputStream(), output);

            // 将图片上传至oss
            picStream = new ByteArrayInputStream(output.toByteArray());
            FileUtil.createDirs(IMAGE_DIR_PATH);
            String imageFilePath = IMAGE_DIR_PATH + picName;

            createPic(picStream, imageFilePath);
            FileInputStream fis = new FileInputStream(imageFilePath);
//            UploadAliyunUtils.uploadPicture(fis, (long) fis.available(), UploadAliyunUtils.BUCKET_NAME, picName);
            FileUtil.delFile(imageFilePath);
        } catch (Exception e) {
            log.error("ID:{},文件上传OSS异常", e);
            return null;
        } finally {
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Exception e) {
                    log.error("关闭url链接异常:, e", e);
                }
            }

            if (output != null) {
                try {
                    output.close();
                } catch (Exception e) {
                    log.error("关闭流异常:, e", e);
                }
            }
            if (picStream != null) {
                try {
                    picStream.close();
                } catch (Exception e) {
                    log.error("关闭流异常:, e", e);
                }
            }
        }

        return "ImageConstant.OSS_PATH" + picName;
    }

    public static void createPic(InputStream pdfStream, String outPath) {
        try {
            byte[] buffer = new byte[1024];
            int leng;
            //打开输入流
            FileImageOutputStream imageOutput = new FileImageOutputStream(new File(outPath));
            while ((leng = pdfStream.read(buffer)) != -1) {
                //将byte写入硬盘
                imageOutput.write(buffer, 0, leng);
            }
            imageOutput.close();
        } catch (IOException e) {
            log.error("io exception", e);
        }
    }
}
