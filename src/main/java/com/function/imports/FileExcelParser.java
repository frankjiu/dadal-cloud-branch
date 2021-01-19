/**
 * All rights Reserved, Designed By www.xcompany.com
 *
 * @Package: com.controller
 * @author: Frankjiu
 * @date: 2020年6月1日
 * @version: V1.0
 */

package com.function.imports;

import com.core.exception.CommonException;
import com.core.utils.DateUtils;
import com.core.utils.ExcelUtil;
import com.modules.base.model.vo.DemoParseVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 文件解析器
 * @author: Frankjiu
 * @date: 2020年6月1日
 */
@Slf4j
public class FileExcelParser {

    private static final Integer startRows = 1; // 数据读取起始行

    private static final Integer maxRow = 65536; // 数据文件最大行数

    private static final Integer maxColumn = 4; // 数据文件最大列数

    private static final String sign = System.getProperty("file.separator"); //文件目录分隔符

    private static final String REGEX = "^$|^[A-Z]$"; //文件内容正则表达式

    private static final String ROOT = System.getProperty("user.dir"); //项目根目录

    public static List<DemoParseVo> parse(MultipartFile file) throws Exception {
        // 文件类型校验
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (!".xls".equalsIgnoreCase(suffix) && !".xlsx".equalsIgnoreCase(suffix)) {
            throw new CommonException("File type error!");
        }
        //解析xlsx格式文件
        InputStream ins = file.getInputStream();
        int flag = 1;
        Workbook workbook;
        if (".xlsx".equalsIgnoreCase(suffix)) {
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(ins);
            SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(xssfWorkbook, 100);
            workbook = sxssfWorkbook.getXSSFWorkbook();
        } else {
             workbook = new HSSFWorkbook(ins);
        }
        // 获取第一个sheet表单
        Sheet sheet = workbook.getSheetAt(0);
        return parseData(sheet, flag);
    }

    private static List<DemoParseVo> parseData(Sheet sheet, int flag){
        List<DemoParseVo> listData = new ArrayList<>();
        // 获取第一行,最后一行
        int firstRow = sheet.getFirstRowNum();
        int lastRowNum = sheet.getLastRowNum();
        if (startRows != null) {
            firstRow = startRows;
        }
        if (lastRowNum >= maxRow) {
            throw new CommonException("The max rownum is 65536!");
        }
        // 循环行数, 依次获取列
        for (int i = firstRow; i < lastRowNum + 1; i++) {
            // 获取第i行
            Row row = sheet.getRow(i);
            // 获取最后一列
            short lastCellNum = row.getLastCellNum();
            if (lastCellNum > maxColumn) {
                throw new CommonException("The max column is 4!");
            }
            if (!isEmptyRow(row)) {
                DemoParseVo demo = new DemoParseVo();
                // 获取第0...3共4列
                demo.setCardName(ExcelUtil.getCellVal(row.getCell(0)));
                demo.setCardNumber(ExcelUtil.getCellVal(row.getCell(1)));
                try {
                    Date dateCellValue = row.getCell(3).getDateCellValue();
                    if (dateCellValue == null) {
                        throw new CommonException("The " + (i+1) + "th row, 4th column value is invalid! data type: Date");
                    }
                    demo.setCreateTime(DateUtils.convertLongDateToString(Long.valueOf(row.getCell(3).getDateCellValue().getTime()), "yyyy-MM-dd"));
                } catch (Exception e) {
                    log.info(e.getMessage(), e);
                    throw new CommonException("The format of this column should be 'DATE' like '2021/12/22' !");
                }
            }
        }
        return listData;
    }

    /**
     * 一个不为空就不是空行
     * @param row
     * @return
     */
    private static boolean isEmptyRow(Row row) {
        if (row == null) {
            return true;
        }
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

}
