package com.wjh.utils;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Classname ExcelUtil
 * @Description
 * @Date 2020/6/15 下午2:56
 * @Created by wjh
 */
public class ExcelUtil {

    /**
     * excel数据检测
     *
     * @param file       文件
     * @param sheetIndex 第几个sheet
     * @param maxLimit   单次读取限制
     * @param cell       多少列
     * @param
     * @return
     */
    public static String readExcelCheck(MultipartFile file, Integer sheetIndex, Integer maxLimit, Integer cell) {
        if (file == null || file.isEmpty()) {
//            return ResultEnum.UPLOAD_FILE_NOT_EXISTS;
            return null;
        }
        if (sheetIndex == null) {
            sheetIndex = 0;
        }
        String orignalFilename = file.getOriginalFilename();
        String extString = orignalFilename.substring(orignalFilename.lastIndexOf("."));
        if (!extString.equals(".xls") && !extString.equals(".xlsx")) {
            return "格式";
        }
        Workbook wb = ExcelUtil.createWorkBook(file);
        if (wb != null) {
            Sheet sheet = wb.getSheetAt(sheetIndex);
            int maxRownum = sheet.getPhysicalNumberOfRows();
            if (maxRownum <= 1 || maxRownum > maxLimit) {
                return "**";
            }

            Row firstRow = sheet.getRow(0);
            int maxColnum = firstRow.getPhysicalNumberOfCells();
            if (maxColnum < cell) {
                return "**";
            }
        }
        return "**";
    }

    /**
     * 从excel中读内容
     *
     * @param file         文件
     * @param sheetIndex   第几个sheet
     * @param limit        单次读取限制
     * @param num          读取次数
     * @param firstRowName 表头存储
     * @return List<Map>
     */
    public static List<Map<String, String>> readExcel(MultipartFile file, Integer sheetIndex, Integer limit, Integer num, List<String> firstRowName) {
        List<Map<String, String>> dataList = new ArrayList<>();
        Workbook wb = ExcelUtil.createWorkBook(file);
        if (wb != null) {
            if (sheetIndex == null) {
                sheetIndex = 0;
            }
            if (limit == null) {
                limit = 200;
            }
            if (num == null) {
                num = 0;
            }
            Sheet sheet = wb.getSheetAt(sheetIndex);
            int maxRownum = sheet.getPhysicalNumberOfRows();
            Row firstRow = sheet.getRow(0);
            int maxColnum = firstRow.getPhysicalNumberOfCells();
            String columns[] = new String[maxColnum];
            for (int i = num * limit; i < maxRownum; i++) {
                Map<String, String> map = null;
                if (i > 0) {
                    map = new LinkedHashMap<>();
                    firstRow = sheet.getRow(i);
                }
                if (firstRow != null) {
                    String cellData = null;
                    for (int j = 0; j < maxColnum; j++) {
                        cellData = ExcelUtil.getCellValueToString(firstRow.getCell(j));
                        if (i == 0) {
                            columns[j] = cellData;
                            firstRowName.add(cellData);
                        } else {
                            map.put(columns[j], cellData);
                        }
                    }
                } else {
                    break;
                }
                if (i > 0) {
                    dataList.add(map);
                }
            }
        }
        return dataList;
    }

    public static String getCellValueToString(Cell cell) {
        String cellValue = null;
        if (cell != null) {
            cell.setCellType(CellType.STRING);
            cellValue = cell.getRichStringCellValue().getString();
        }
        return cellValue;
    }

    private static Workbook createWorkBook(MultipartFile file) {
        Workbook wb = null;
        File file1 = null;
        if (file.isEmpty()) {
            return null;
        }
        String orignalFilename = file.getOriginalFilename();
        String extString = orignalFilename.substring(orignalFilename.lastIndexOf("."));
        InputStream is = null;
        try {
            is = file.getInputStream();
            file1 = new File(file.getOriginalFilename());
            inputStreamToFile(is, file1);
            FileInputStream fis = new FileInputStream(file1);
            if (".xls".equals(extString)) {
                return wb = new HSSFWorkbook(fis);
            } else if (".xlsx".equals(extString)) {
                return wb = new XSSFWorkbook(fis);
            } else {
                return wb;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            File del = new File(file1.toURI());
            del.delete();
        }
        return wb;
    }

    public static void inputStreamToFile(InputStream ins, File file) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
        } finally {
            try {
                if (os != null)
                    os.close();
            } catch (IOException e) {
            }
        }
    }
}
