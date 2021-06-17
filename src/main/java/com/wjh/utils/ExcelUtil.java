package com.wjh.utils;

import lombok.extern.slf4j.Slf4j;
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

import javax.servlet.http.HttpServletResponse;
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
@Slf4j
public class ExcelUtil {



    /**
     * 导出多个sheet的excel
     * @param name excel名
     * @param mapList 多个sheet（头headers、数据dataList、表名fileName、数据对应字段fields）
     * @param response
     * @param type type 默认传0，1还款计划模版，
     * @param <T>
     */
    public static <T> void exportMultisheetExcel(String name, List<Map> mapList, HttpServletResponse response, Integer type) {
        BufferedOutputStream bos = null;
        try {
            String fileName = name + ".xlsx";
            bos = getBufferedOutputStream(fileName, response);
            doExport(mapList, bos, type);
        } catch (Exception e) {
            log.error("导出excel异常,name:{},msg:{}", name, e.getMessage(), e);
//            throw new Exception("导出excel异常");
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                log.error("导出excel异常,name:{},msg:{}", name, e.getMessage(), e);
//            throw new Exception("导出excel异常");
            }
        }
    }

    private static BufferedOutputStream getBufferedOutputStream(String fileName, HttpServletResponse response) throws Exception {
        response.setContentType("application/x-msdownload");
//        response.setHeader("Content-Disposition", "attachment;filename="
//                + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
        response.setHeader("Content-Disposition", "attachment;filename="
                + new String(fileName.getBytes("utf-8"), "utf-8"));
        return new BufferedOutputStream(response.getOutputStream());
    }

    private static <T> void doExport(List<Map> mapList, OutputStream outputStream, Integer type) {
        int maxBuff = 100;
        // 创建excel工作文本，100表示默认允许保存在内存中的行数
        SXSSFWorkbook wb = new SXSSFWorkbook(maxBuff);
        try {
            for (int i = 0; i < mapList.size(); i++) {
                Map map = mapList.get(i);
                String[] headers = (String[]) map.get("headers");
                Collection<T> dataList = (Collection<T>) map.get("dataList");
                String fileName = (String) map.get("fileName");
                String[] exportFields = (String[]) map.get("fields");
                Integer length = (Integer) map.get("length");
                length = length == null ? 0 : length;
                if (type.equals(1)) {
                    createSheet1(wb, exportFields, headers, dataList, fileName, maxBuff, length);
                } else {
                    createSheet(wb, exportFields, headers, dataList, fileName, maxBuff);
                }
            }
            if (outputStream != null) {
                wb.write(outputStream);
            }
        } catch (Exception e) {
            log.error("导出excel异常,msg:{}",e.getMessage(), e);
//            throw new Exception("导出excel异常");
        }

    }

    private static <T> void createSheet1(SXSSFWorkbook wb, String[] exportFields, String[] headers, Collection<T> dataList, String fileName, int maxBuff, Integer length) throws NoSuchFieldException, IllegalAccessException, IOException {

        Sheet sh = wb.createSheet(fileName);

        CellStyle style = wb.createCellStyle();
        CellStyle style2 = wb.createCellStyle();
        //创建表头
        Font font = wb.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 11);//设置字体大小
        style.setFont(font);//选择需要用到的字体格式

        style.setFillForegroundColor(HSSFColor.LIME.index);// 设置背景色
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        style2.setFont(font);//选择需要用到的字体格式

        style2.setFillForegroundColor(HSSFColor.WHITE.index);// 设置背景色
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); //垂直居中
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平向下居中
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框

        Row headerRow = sh.createRow(0); //表头

        int headerSize = headers.length;
        for (int cellnum = 0; cellnum < headerSize; cellnum++) {
            Cell cell = headerRow.createCell(cellnum);
            cell.setCellStyle(style);
            sh.setColumnWidth(cellnum, 4000);
            cell.setCellValue(headers[cellnum]);
        }

        int rownum = 0;
        if (CollectionUtils.isEmpty(dataList)) {
        } else {
            Boolean firstFlag = Boolean.TRUE;
            Iterator<T> iterator = dataList.iterator();
            while (iterator.hasNext()) {
                T data = iterator.next();
                Row row = sh.createRow(rownum + 1);

                Field[] fields = getExportFields(data.getClass(), exportFields);
                for (int cellnum = 0; cellnum < headerSize; cellnum++) {
                    Cell cell = row.createCell(cellnum);
                    cell.setCellStyle(style2);
                    Field field = fields[cellnum];

                    setData1(field, data, field.getName(), cell, length, firstFlag);
                }
                firstFlag = false;
                rownum = sh.getLastRowNum();
                // 大数据量时将之前的数据保存到硬盘
                if (rownum % maxBuff == 0) {
                    ((SXSSFSheet) sh).flushRows(maxBuff); // 超过100行后将之前的数据刷新到硬盘

                }
            }
        }
    }

    private static <T> void createSheet(SXSSFWorkbook wb, String[] exportFields, String[] headers, Collection<T> dataList, String fileName, int maxBuff) throws NoSuchFieldException, IllegalAccessException, IOException {

        Sheet sh = wb.createSheet(fileName);

        CellStyle style = wb.createCellStyle();
        CellStyle style2 = wb.createCellStyle();
        //创建表头
        Font font = wb.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 11);//设置字体大小
        style.setFont(font);//选择需要用到的字体格式

        style.setFillForegroundColor(HSSFColor.YELLOW.index);// 设置背景色
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        style2.setFont(font);//选择需要用到的字体格式

        style2.setFillForegroundColor(HSSFColor.WHITE.index);// 设置背景色
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); //垂直居中
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平向下居中
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框

        Row headerRow = sh.createRow(0); //表头

        int headerSize = headers.length;
        for (int cellnum = 0; cellnum < headerSize; cellnum++) {
            Cell cell = headerRow.createCell(cellnum);
            cell.setCellStyle(style);
            sh.setColumnWidth(cellnum, 4000);
            cell.setCellValue(headers[cellnum]);
        }

        int rownum = 0;
        if (CollectionUtils.isEmpty(dataList)) {
        } else {
            Iterator<T> iterator = dataList.iterator();
            while (iterator.hasNext()) {
                T data = iterator.next();
                Row row = sh.createRow(rownum + 1);

                Field[] fields = getExportFields(data.getClass(), exportFields);
                for (int cellnum = 0; cellnum < headerSize; cellnum++) {
                    Cell cell = row.createCell(cellnum);
                    cell.setCellStyle(style2);
                    Field field = fields[cellnum];

                    setData(field, data, field.getName(), cell);
                }
                rownum = sh.getLastRowNum();
                // 大数据量时将之前的数据保存到硬盘
                if (rownum % maxBuff == 0) {
                    ((SXSSFSheet) sh).flushRows(maxBuff); // 超过100行后将之前的数据刷新到硬盘

                }
            }
        }
    }

    /**
     * 根据属性设置对应的属性值
     *
     * @param dataField 属性
     * @param object    数据对象
     * @param property  表头的属性映射
     * @param cell      单元格
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    private static <T> void setData(Field dataField, T object, String property, Cell cell)
            throws IllegalAccessException, NoSuchFieldException {
        dataField.setAccessible(true); //允许访问private属性
        Object val = dataField.get(object); //获取属性值
        Sheet sh = cell.getSheet(); //获取excel工作区
        CellStyle style = cell.getCellStyle(); //获取单元格样式
        int cellnum = cell.getColumnIndex();
        if (val != null) {
            if (dataField.getType().toString().endsWith("String")) {
                cell.setCellValue((String) val);
            } else if (dataField.getType().toString().endsWith("Integer") || dataField.getType().toString().endsWith("int")) {
                cell.setCellValue((Integer) val);
            } else if (dataField.getType().toString().endsWith("Long") || dataField.getType().toString().endsWith("long")) {
                cell.setCellValue(val.toString());
            } else if (dataField.getType().toString().endsWith("Double") || dataField.getType().toString().endsWith("double")) {
                cell.setCellValue((Double) val);
            } else if (dataField.getType().toString().endsWith("Date")) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                cell.setCellValue(format.format((Date) val));
            } else if (dataField.getType().toString().endsWith("List")) {
                List list1 = (List) val;
                int size = list1.size();
                for (int i = 0; i < size; i++) {
                    //加1是因为要去掉点号
                    int start = property.indexOf(dataField.getName()) + dataField.getName().length() + 1;
                    String tempProperty = property.substring(start, property.length());
                    Field field = getDataField(list1.get(i), tempProperty);
                    Cell tempCell = cell;
                    if (i > 0) {
                        int rowNum = cell.getRowIndex() + i;
                        Row row = sh.getRow(rowNum);
                        if (row == null) {//另起一行
                            row = sh.createRow(rowNum);
                            //合并之前的空白单元格（在这里需要在header中按照顺序把list类型的字段放到最后，方便显示和合并单元格）
                            for (int j = 0; j < cell.getColumnIndex(); j++) {
                                sh.addMergedRegion(new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex() + size - 1, j, j));
                                Cell c = row.createCell(j);
                                c.setCellStyle(style);
                            }
                        }
                        tempCell = row.createCell(cellnum);
                        tempCell.setCellStyle(style);
                    }
                    //递归传参到单元格并获取偏移量（这里获取到的偏移量都是第二层后list的偏移量）
                    setData(field, list1.get(i), tempProperty, tempCell);
                }
            } else {
                if (property.contains(".")) {
                    String p = property.substring(property.indexOf(".") + 1, property.length());
                    Field field = getDataField(val, p);
                    setData(field, val, p, cell);
                } else {
                    cell.setCellValue(val.toString());
                }
            }
        }
    }

    /**
     * 根据属性设置对应的属性值
     *
     * @param dataField 属性
     * @param object    数据对象
     * @param property  表头的属性映射
     * @param cell      单元格
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    private static <T> void setData1(Field dataField, T object, String property, Cell cell, Integer length, Boolean firstFlag)
            throws IllegalAccessException, NoSuchFieldException {
        dataField.setAccessible(true); //允许访问private属性
        Object val = dataField.get(object); //获取属性值
        Sheet sh = cell.getSheet(); //获取excel工作区
        CellStyle style = cell.getCellStyle(); //获取单元格样式
        int cellnum = cell.getColumnIndex();
        if (property.equals("orderId")) {
            if (firstFlag) {
                sh.addMergedRegion(new CellRangeAddress(cell.getRowIndex(), length, cell.getColumnIndex(), cell.getColumnIndex()));
            } else {
                return;
            }
        }
        if (val != null) {
            if (dataField.getType().toString().endsWith("String")) {
                cell.setCellValue((String) val);
            } else if (dataField.getType().toString().endsWith("Integer") || dataField.getType().toString().endsWith("int")) {
                cell.setCellValue((Integer) val);
            } else if (dataField.getType().toString().endsWith("Long") || dataField.getType().toString().endsWith("long")) {
                cell.setCellValue(val.toString());
            } else if (dataField.getType().toString().endsWith("Double") || dataField.getType().toString().endsWith("double")) {
                cell.setCellValue((Double) val);
            } else if (dataField.getType().toString().endsWith("Date")) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                cell.setCellValue(format.format((Date) val));
            } else if (dataField.getType().toString().endsWith("List")) {
                List list1 = (List) val;
                int size = list1.size();
                for (int i = 0; i < size; i++) {
                    //加1是因为要去掉点号
                    int start = property.indexOf(dataField.getName()) + dataField.getName().length() + 1;
                    String tempProperty = property.substring(start, property.length());
                    Field field = getDataField(list1.get(i), tempProperty);
                    Cell tempCell = cell;
                    if (i > 0) {
                        int rowNum = cell.getRowIndex() + i;
                        Row row = sh.getRow(rowNum);
                        if (row == null) {//另起一行
                            row = sh.createRow(rowNum);
                            //合并之前的空白单元格（在这里需要在header中按照顺序把list类型的字段放到最后，方便显示和合并单元格）
                            for (int j = 0; j < cell.getColumnIndex(); j++) {
                                sh.addMergedRegion(new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex() + size - 1, j, j));
                                Cell c = row.createCell(j);
                                c.setCellStyle(style);
                            }
                        }
                        tempCell = row.createCell(cellnum);
                        tempCell.setCellStyle(style);
                    }
                    //递归传参到单元格并获取偏移量（这里获取到的偏移量都是第二层后list的偏移量）
                    setData1(field, list1.get(i), tempProperty, tempCell, length, firstFlag);
                }
            } else {
                if (property.contains(".")) {
                    String p = property.substring(property.indexOf(".") + 1, property.length());
                    Field field = getDataField(val, p);
                    setData1(field, val, p, cell, length, firstFlag);
                } else {
                    cell.setCellValue(val.toString());
                }
            }
        }
    }

    /**
     * 获取单条数据的属性
     *
     * @param object
     * @param property
     * @param <T>
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static <T> Field getDataField(T object, String property) throws NoSuchFieldException, IllegalAccessException {
        Field dataField;
        if (property.contains(".")) {
            String p = property.substring(0, property.indexOf("."));
            dataField = object.getClass().getDeclaredField(p);
            return dataField;
        } else {
            dataField = object.getClass().getDeclaredField(property);
        }
        return dataField;
    }

    private static Field[] getExportFields(Class<?> targetClass, String[] exportFieldNames) {
        Field[] fields = null;
        if (exportFieldNames == null || exportFieldNames.length < 1) {
            fields = targetClass.getDeclaredFields();
        } else {
            fields = new Field[exportFieldNames.length];
            for (int i = 0; i < exportFieldNames.length; i++) {
                try {
                    fields[i] = targetClass.getDeclaredField(exportFieldNames[i]);
                } catch (Exception e) {
                    try {
                        fields[i] = targetClass.getSuperclass().getDeclaredField(exportFieldNames[i]);
                    } catch (Exception e1) {
                        throw new IllegalArgumentException("无法获取导出字段", e);
                    }

                }
            }
        }
        return fields;
    }

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
