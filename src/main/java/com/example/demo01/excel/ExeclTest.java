package com.example.demo01.excel;

import com.example.demo01.bean.ExcelBean;
import com.example.demo01.excel.bean.Person;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.util.StringUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExeclTest<T> {

    private static final Integer FIRSTROW = 3;

    public static void main(String[] args) throws Exception {
        List<String> list = new ArrayList();
        list.add("黄莉娟");
        list.add("黄胜男");
        list.add("康佳洺");
        list.add("李佩佩");
        list.add("李天白");
        list.add("刘杨");
        list.add("孟小然");
        list.add("祁萃芳");
        list.add("宋欣怡");
        list.add("王卉");
        list.add("吴婷");
        list.add("杨殿辉");
        list.add("王梦茹");
        list.add("叶少微");
        list.add("余若雅");
        list.add("张涵");
        list.add("张露馨");
        list.add("张鑫");
        list.add("刘杨");
        list.add("华东 刘杨");
        list.add("赵晨宇");
        qianyan("C:\\Users\\Administrator\\Desktop\\钱\\2018年6月助理绩效汇总.xlsx",list);
//        List<gkw> read = read(gkw.class, "D:\\test\\excel\\ggw.xlsx", 2);
//        for (gkw e : read) {
//            if (e.getProduct().equals("易贷")) {
//                e.setProduct("03");
//            } else if (e.getProduct().equals("即时贷")) {
//                e.setProduct("21");
//            } else if (e.getProduct().equals("招手贷")) {
//                e.setProduct("02");
//            }
//            //INSERT INTO advert_position VALUES ('20180711000007', '易贷-H5-未登录-落地页', '易贷-H5-未登录-落地页', '03', '1', '1334', '750', '', '20180711000000','');
//            System.out.println("INSERT INTO ADVERT_POSITION VALUES ('" + e.getId() + "', '" + e.getName() + "', '" + e.getRemark() + "', '" + e.getProduct() + "', '" + e.getNum() + "', '" + e.getHeigh() + "', '" + e.getWidth() + "', '', '20180711000000','');");
//        }
//        File file = new File("D:\\test\\excel\\demo1.xls");
//        try (FileOutputStream fos = new FileOutputStream(file)) {
//            ExeclTest<ExcelBean> exl = new ExeclTest<>();
//            HSSFWorkbook wf = exl.write(read);
//            wf.write(fos);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public static List read(Class targetClass, String filePath) {
        return read(targetClass, filePath, FIRSTROW);
    }

    public static List read(Class targetClass, String filePath, Integer startRow) {

        List<Object> list = new ArrayList();
        File file = new File(filePath);
        InputStream inputStream = null;
        Workbook workbook = null;
        try {
            inputStream = new FileInputStream(file);
            workbook = WorkbookFactory.create(inputStream);
            inputStream.close();
            //工作表对象
            Sheet sheet = workbook.getSheetAt(0);
            //总行数
            int rowLength = sheet.getLastRowNum() + 1;
            //工作表的列
            Row row = sheet.getRow(0);
            //总列数
            int colLength = row.getLastCellNum();
            //防止无限边框-->全部设置边框但是数据只有几行
            int boundary = 0;
            System.out.println("行数：" + rowLength + ",列数：" + colLength);
            //获取属性列表
            Row classRow = sheet.getRow(1);
            Cell classCell = classRow.getCell(0);
            String classInformation = classCell.getStringCellValue();
            String[] fields = classInformation.split(" ");
            //从第i行开始读取
            for (int i = startRow; i < rowLength; i++) {
                row = sheet.getRow(i);
                //空行跳过
                if (row == null) {
                    boundary++;
                    continue;
                }
                Cell head = row.getCell(0);
                //第一列为空则整行跳过
                if (head.getCellType() == Cell.CELL_TYPE_BLANK) {
                    boundary++;
                    continue;
                }
                //50个空行之后的数据忽略。
                if (boundary >= 50) {
                    break;
                }
                Object target = targetClass.newInstance();
                for (int j = 0; j < colLength; j++) {
                    //开始赋值
                    Cell cell = row.getCell(j);
                    String methodName = "set" + fields[j].substring(0, 1).toUpperCase() + fields[j].substring(1);
                    Class<?> fType = targetClass.getDeclaredField(fields[j]).getType();
                    Method setMethod = targetClass.getMethod(methodName, fType);
                    //赋值类型
                    int type = Cell.CELL_TYPE_BLANK;
                    if (cell != null) {
                        type = cell.getCellType();
                    }
                    //若为空则新建一个空对象。
                    if (type == Cell.CELL_TYPE_BLANK) {
                        setMethod.invoke(target, fType.newInstance());
                    } else if (type == Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                        //赋值日期
                        setMethod.invoke(target, cell.getDateCellValue());
                    } else {
                        //赋值字符串
                        //若该列为时间，但是类型为文本时转时间处理。
                        if (fType.newInstance() instanceof Date) {
                            throw new RuntimeException("请将时间列的格式设置为日期");
                        }
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        setMethod.invoke(target, cell.getStringCellValue());
                    }
                }
                list.add(target);
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("读取行数错误，导致数据读取异常");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public HSSFWorkbook write(List<T> list) throws Exception {
        HSSFWorkbook work = new HSSFWorkbook();
        list.add(0, list.get(0));

        // 创建sheet
        HSSFSheet sheet = (HSSFSheet) work.createSheet();

        // 表头单元格样式
        CellStyle titleStyle = work.createCellStyle();
        HSSFFont titleFont = work.createFont();
        titleFont.setFontName("宋体");
        titleFont.setFontHeightInPoints((short) 10);
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        titleStyle.setFont(titleFont);
        setBorder(titleStyle);
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        // 表体单元格样式
        CellStyle normalStyle = work.createCellStyle();
        HSSFFont normalFont = work.createFont();
        normalFont.setFontName("宋体");
        normalFont.setFontHeightInPoints((short) 10);
        normalStyle.setFont(normalFont);
        setBorder(normalStyle);

        for (int i = 0; i < list.size(); i++) {
            T t = list.get(i);
            Class<?> tClass = t.getClass();
            Field[] fields = tClass.getDeclaredFields();
            HSSFRow row = sheet.createRow(i);
            for (int j = 0; j < fields.length; j++) {
                Field field = fields[j];
                if (field.isAnnotationPresent(ExcelHead.class)) {
                    //在第一行设置表头
                    if (i == 0) {
                        String headName = field.getAnnotation(ExcelHead.class).value();
                        newCell(row, j, headName, titleStyle);
                    } else {
                        String getMethod = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                        Method method = tClass.getMethod(getMethod);
                        Object value = method.invoke(t);
                        if (value instanceof String) {
                            newCell(row, j, checkNull((String) value), normalStyle);
                        } else if (value instanceof Date) {
                            newCell(row, j, checkNull(formatTime((Date) value)), normalStyle);
                        } else {
                            newCell(row, j, "", normalStyle);
                        }
                    }


                }
            }

        }
        return work;

    }

    /**
     * 设置边框
     *
     * @param style
     */
    private static void setBorder(CellStyle style) {
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());

        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());

        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());

        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
    }

    /***
     * 字符串为空处理
     * @param inputString
     * @return
     */
    private static String checkNull(String inputString) {
        if (StringUtils.isEmpty(inputString) || "null".equals(inputString)) {
            inputString = "";
        }
        return inputString;
    }

    /***
     * 格式化时间
     * @param date
     * @return
     */
    private static String formatTime(Date date) {
        if (date != null) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.format(date);
        }
        return "";
    }

    /**
     * 创建单元格
     *
     * @param row    行
     * @param column 列位置
     * @param value  值
     * @param style  样式
     */
    private static void newCell(HSSFRow row, int column, Object value, CellStyle style) {
        HSSFCell cell = row.createCell(column);
        cell.setCellValue(String.valueOf(value));
        cell.setCellStyle(style);
    }


    public static void qianyan(String filePath,List<String> list) throws Exception {
        List<Person> personList = new ArrayList<>();
        File file = new File(filePath);
        InputStream inputStream = null;
        Workbook workbook = null;
        inputStream = new FileInputStream(file);
        workbook = WorkbookFactory.create(inputStream);
        inputStream.close();
        String result ="";
        //工作表对象
        for(int i = 0;i<workbook.getNumberOfSheets(); i++){
            Person person = new Person();
            Sheet sheet = workbook.getSheetAt(i);
            //获取单元格名称
            String sheetName = sheet.getSheetName();
            sheetName = sheetName.replaceAll("[0-9]", "");
            sheetName = sheetName.replaceAll("[a-z]", "");
            sheetName = sheetName.replaceAll("[A-Z]", "");

            person.setName(sheetName);
            //获取数据
            Row row = sheet.getRow(12);
            Cell cell = row.getCell(7);
            if(cell!=null){
                cell.setCellType(Cell.CELL_TYPE_STRING);
                person.setResult(cell.getStringCellValue());
            }else {
                person.setResult("");
            }



        }



    }

}
