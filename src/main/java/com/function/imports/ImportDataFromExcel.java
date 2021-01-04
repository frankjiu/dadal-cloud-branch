package com.function.imports;

import com.core.exception.CommonException;
import com.core.result.HttpResult;
import com.modules.base.model.entity.Demo;
import com.modules.base.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * @Description: import file-excel data
 * @Author: QiuQiang
 * @Date: 2020-12-31
 */
@RestController
@Slf4j
public class ImportDataFromExcel {

    @Autowired
    private DemoService demoService;

    @Autowired
    private ForkJoinPool pool;

    private static final Integer startRows = 1; // 数据读取起始行

    @PostMapping("/import2")
    public HttpResult importFromExcel(@RequestParam("file") MultipartFile file) throws Exception {
        // 读取Excel文件
        // 获取输入流方案1
        String fileName = file.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (!".xls".equalsIgnoreCase(fileType) && !".xlsx".equalsIgnoreCase(fileType)) {
            throw new CommonException("File type error!");
        }
        InputStream ins = file.getInputStream();

        // 获取输入流方案2(用于postman测试)
        /*String fileContent = "import_data";
        String fileName = "excel_test.xlsx";
        String sign = System.getProperty("file.separator");
        FileInputStream ins = new FileInputStream(new File(System.getProperty("user.dir") + sign + fileContent + sign + fileName));*/

        // 存储解析的数据集
        List<Demo> listData = new ArrayList<>();
        // 获取整个Excel
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(ins);
        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(xssfWorkbook, 100);
        XSSFWorkbook workbook = sxssfWorkbook.getXSSFWorkbook();
        // 获取第一个sheet表单
        XSSFSheet sheet = workbook.getSheetAt(0);
        // 获取第一行
        int firstrow = sheet.getFirstRowNum();
        if (startRows != null) {
            firstrow = startRows;
        }
        // 获取最后一行
        int lastrow = sheet.getLastRowNum();
        // 循环行数, 依次获取列
        for (int i = firstrow; i < lastrow + 1; i++) {
            // 获取第i行
            Row row = sheet.getRow(i);
            if (row != null) {
                // 获取i行第一列
                int firstcell = row.getFirstCellNum();
                // 获取i行最后一列
                int lastcell = row.getLastCellNum();
                Demo demo = new Demo();
                for (int j = firstcell; j < lastcell; j++) {
                    demo.setCardName(row.getCell(firstcell + 0).toString());
                    demo.setCardNumber(row.getCell(firstcell + 1).toString());
                }
                listData.add(demo);
            }
        }
        int i = 0;
        if (listData.size() > 0) {
            // i = demoService.batchInsert(listData);
            ForkJoinTask task = new ForkJoinableTask(0, listData.size(), listData);
            ForkJoinTask<Integer> result = pool.submit(task);
        }

        ins.close();
        if (i == listData.size()) {
            return HttpResult.success("File data import successed: " + i);
        }
        return HttpResult.fail("File data import failed!");
    }
}
