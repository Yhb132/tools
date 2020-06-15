package com.wjh.utils;

import java.io.File;

/**
 * @Classname FileUtil
 * @Description
 * @Date 2020/5/14 下午4:07
 * @Created by
 */
public class FileUtil {
    //创建一级目录
    public static boolean createDir(String dirName) {
        File dir = new File(dirName);
        if (!dir.exists()) {
            return dir.mkdir();
        }
        return false;
    }

    //创建多级目录
    public static boolean createDirs(String dirName) {
        File dir = new File(dirName);
        if (!dir.exists()) {
            return dir.mkdirs();
        }
        return false;
    }

    //重命名目录
    public static boolean renameDir(String oldDir, String newDir) {
        File old = new File(oldDir);
        if (old.isDirectory()) {
            return old.renameTo(new File(newDir));
        }
        return false;
    }

    //删除空目录
    public static boolean deleteEmptyDir(String dirName) {
        File dir = new File(dirName);
        if (dir.isDirectory()) {
            return dir.delete();
        }
        return false;
    }

    //递归删除目录
    public static void deleteDirs(String dirName) {
        File dir = new File(dirName);
        File[] dirs = dir.listFiles();
        for (int i = 0; dirs != null && i < dirs.length; i++) {
            File f = dirs[i];
            //如果是文件直接删除
            if (f.isFile()) {
                f.delete();
            }
            //如果是目录继续遍历删除
            if (f.isDirectory()) {
                deleteDirs(f.getAbsolutePath());
            }
        }
        //删除本身
        dir.delete();
    }

    public static void delFile(String filePathAndName) {
        try {
            String filePath = filePathAndName;
            filePath = filePath.toString();
            java.io.File myDelFile = new java.io.File(filePath);
            myDelFile.delete();
        } catch (Exception e) {
            System.out.println("删除文件操作出错");
            e.printStackTrace();
        }
    }
}
