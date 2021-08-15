/**
 * All rights Reserved, Designed By www.xcompany.com
 *
 * @Package: com.controller
 * @author: Frankjiu
 * @date: 2020年6月1日
 * @version: V1.0
 */

package com.function.imports;

import com.modules.base.demo.model.entity.Demo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 文件解析器
 * @author: Frankjiu
 * @date: 2020年6月1日
 */
@Slf4j
public class FileTxtParser {

    private static final Integer startRows = 1; // 数据读取起始行

    private static final String sign = System.getProperty("file.separator"); //文件目录分隔符

    private static final String REGEX = "^\\w+$"; //文件内容正则表达式

    private static final String ROOT = System.getProperty("user.dir"); //项目根目录

    public static List<Demo> parse(String fileContent, String fileName) {
        // 存储解析的数据集
        List<Demo> listData = new ArrayList<>();
        BufferedReader bufReader = null;
        try {
            //解析
            if (fileName.endsWith(".txt")) {
                // 读取TXT文件
                File file = new File(ROOT + sign + fileContent + sign + fileName);
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "GBK");
                bufReader = new BufferedReader(isr);
                String line;
                // 逐行扫描
                while ((line = bufReader.readLine()) != null) {
                    boolean matched = false;
                    String record = null;
                    if (line.trim().length() >= 1) {
                        record = line.trim();
                        matched = record.matches(REGEX);
                    }
                    // 如果数据行格式通过正则表达式则进行处理
                    if (matched) {
                        Demo demo = new Demo();
                        demo.setCardName(record);
                        listData.add(demo);
                    }
                }
            }

            if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
                // 读取Excel文件 获取输入流方案1
                /*String fileName = file.getOriginalFilename();
                String fileType = fileName.substring(fileName.lastIndexOf("."));
                if (!".xls".equalsIgnoreCase(fileType) && !".xlsx".equalsIgnoreCase(fileType)) {
                    throw new CommonException("File type error!");
                }
                InputStream ins = file.getInputStream();*/

                // 读取Excel文件 获取输入流方案2(用于postman测试)
                //String fileContent = "import_data";
                //String fileName = "excel_test.xlsx";
                //String sign = System.getProperty("file.separator");
                FileInputStream ins = new FileInputStream(new File(ROOT + sign + fileContent + sign + fileName));

                // 获取整个Excel
                XSSFWorkbook xssfWorkbook = new XSSFWorkbook(ins);
                SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(xssfWorkbook, 100);
                XSSFWorkbook workbook = sxssfWorkbook.getXSSFWorkbook();
                // 获取第一个sheet表单
                XSSFSheet sheet = workbook.getSheetAt(0);
                // 获取第一行
                int firstRow = sheet.getFirstRowNum();
                if (startRows != null) {
                    firstRow = startRows;
                }
                // 获取最后一行
                int lastRow = sheet.getLastRowNum();
                // 循环行数, 依次获取列
                for (int i = firstRow; i < lastRow + 1; i++) {
                    // 获取第i行
                    Row row = sheet.getRow(i);
                    if (row != null) {
                        // 获取i行第一列
                        int firstCell = row.getFirstCellNum();
                        // 获取i行最后一列
                        int lastCell = row.getLastCellNum();
                        Demo demo = new Demo();
                        for (int j = firstCell; j < lastCell; j++) {
                            demo.setCardName(row.getCell(firstCell + 0).toString());
                            demo.setCardNumber(row.getCell(firstCell + 1).toString());
                        }
                        listData.add(demo);
                    }
                }
            }
        } catch (Exception e) {
            log.info("解析异常!" + e);
        } finally {
            try {
                bufReader.close();
            } catch (IOException e) {
                log.info(e.getMessage(), e);
            }
        }
        return listData;
    }

}
