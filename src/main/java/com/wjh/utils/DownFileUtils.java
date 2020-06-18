package com.wjh.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;

/**
 * @Classname DownFileUtils
 * @Description 文件下载工具
 * @Date 2020/6/18 下午3:40
 * @Created by wjh
 */
@Slf4j
public class DownFileUtils {

    /**
     * 链接流输出
     *
     * @param response
     * @param fileUrl  链接
     * @throws IOException
     */
    public void downUrl(HttpServletResponse response, String fileUrl) throws IOException {
        BufferedInputStream bis = null;
        try {
            URL url = new URL(fileUrl);
            bis = new BufferedInputStream(url.openStream());
            down(response, bis, "Pic.jpg");
        } finally {
            if (bis != null) {
                bis.close();
            }
        }
    }

    /**
     * 项目内文件流输出
     *
     * @param response
     * @param fileName    (路径+)文件名称(基于项目路径)
     * @param newFileName 下载文件名
     * @throws IOException
     */
    public void downFile(HttpServletResponse response, String fileName, String newFileName) throws IOException {
        InputStream is = new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(fileName));
        File file = new File(newFileName);
        FileUtils.copyInputStreamToFile(is, file);
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file); BufferedInputStream bis = new BufferedInputStream(fis)) {
                down(response, bis, file.getName());
            }
        }
    }

    public void down(HttpServletResponse response, BufferedInputStream bis, String fileName) {
        try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
            byte[] buffer = new byte[1024];
            int i = bis.read(buffer);
            while (i != -1) {
                bos.write(buffer, 0, i);
                i = bis.read(buffer);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
