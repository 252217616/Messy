package com.example.demo01.word;

import com.example.demo01.bean.ExcelBean;
import com.example.demo01.bean.WeChatMsg;
import com.example.demo01.excel.ExeclTest;
import com.google.common.collect.Lists;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.List;

public class WordMain {
    public static void main(String[] args) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(new File("D:\\table.docx"));
        XWPFDocument doc = new XWPFDocument();
        List<WeChatMsg> read = ExeclTest.read(WeChatMsg.class, "C:\\Users\\Administrator\\Desktop\\load\\start.xlsx", 2);
        String oldTime = null;
        boolean flag = false;
        XWPFTable table = null;
        int i = 0;
        for (WeChatMsg weChatMsg : read) {
            String[] time = new SimpleDateFormat("yyyy年MM月dd日_HH").format(weChatMsg.getTime()).split("_");
            if (oldTime != null && !time[0].equals(oldTime)) {

                flag = true;
            }
            //换时间了，达到边际
            if (oldTime == null || (Integer.parseInt(time[1]) > 5 && flag)) {
                //段落
                XWPFParagraph firstParagraph = doc.createParagraph();
                firstParagraph.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun run = firstParagraph.createRun();
                run.setText(time[0]);
                run.setFontFamily("微软雅黑");
                run.setFontSize(12);
                run.setBold(true);
                //创建表格
                table = doc.createTable(1, 2);
                table.getCTTbl().getTblPr().addNewTblBorders();
                table.getCTTbl().getTblPr().unsetTblBorders();
//                table.getCTTbl().getTblPr().addNewTblW().setW(new BigInteger("9756"));
                table.getCTTbl().getTblPr().addNewJc().setVal(STJc.CENTER);
                CTTblWidth ctTblWidth = table.getCTTbl().getTblPr().addNewTblW();
                ctTblWidth.setType(STTblWidth.DXA);
                ctTblWidth.setW(BigInteger.valueOf(9756));
                flag = false;
                i = 0;
                oldTime = time[0];

            }
            XWPFTableRow row;
            if (i == 0) {
                row = table.getRow(0);
                i++;
            } else {
                row = table.createRow();
            }
            CTTblWidth ctTblWidth;
            if (weChatMsg.getType().contains("J")) {
                XWPFTableCell cell = row.getCell(0);

                cell.setText(weChatMsg.getMsg());
                cell.getCTTc().addNewTcPr().addNewVAlign().setVal(STVerticalJc.CENTER);
                cell.getCTTc().getPList().get(0).addNewPPr().addNewJc().setVal(STJc.LEFT);
                ctTblWidth = cell.getCTTc().addNewTcPr().addNewTcW();
                ctTblWidth.setType(STTblWidth.DXA);
                ctTblWidth.setW(new BigInteger("4878"));
                cell = row.getCell(1);
                cell.setText("");
                cell.getCTTc().addNewTcPr().addNewVAlign().setVal(STVerticalJc.CENTER);
                cell.getCTTc().getPList().get(0).addNewPPr().addNewJc().setVal(STJc.LEFT);
                ctTblWidth = cell.getCTTc().addNewTcPr().addNewTcW();
                ctTblWidth.setType(STTblWidth.DXA);
                ctTblWidth.setW(new BigInteger("4878"));
            } else {
                XWPFTableCell cell = row.getCell(0);
                cell.setText("");
                cell.getCTTc().addNewTcPr().addNewVAlign().setVal(STVerticalJc.CENTER);
                cell.getCTTc().getPList().get(0).addNewPPr().addNewJc().setVal(STJc.RIGHT);
                ctTblWidth = cell.getCTTc().addNewTcPr().addNewTcW();
                ctTblWidth.setType(STTblWidth.DXA);
                ctTblWidth.setW(new BigInteger("4878"));
                cell = row.getCell(1);
                cell.setText(weChatMsg.getMsg());
                cell.getCTTc().addNewTcPr().addNewVAlign().setVal(STVerticalJc.CENTER);
                cell.getCTTc().getPList().get(0).addNewPPr().addNewJc().setVal(STJc.RIGHT);
                ctTblWidth = cell.getCTTc().addNewTcPr().addNewTcW();
                ctTblWidth.setType(STTblWidth.DXA);
                ctTblWidth.setW(new BigInteger("4878"));
            }
        }


        doc.write(outputStream);
        outputStream.close();
    }

    private static List<WeChatMsg> readExcel() {
        List<WeChatMsg> read = ExeclTest.read(WeChatMsg.class, "C:\\Users\\Administrator\\Desktop\\load\\start.xlsx", 2);

        return read;
    }


}
