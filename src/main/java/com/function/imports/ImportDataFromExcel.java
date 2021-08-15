package com.function.imports;

import com.core.anotation.Logged;
import com.core.result.HttpResult;
import com.core.utils.EasyPoiUtils;
import com.modules.base.demo.model.dto.DemoPostDto;
import com.modules.base.demo.model.entity.Demo;
import com.modules.base.demo.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * @remark: import file-excel data
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
    public HttpResult importFromExcel(/*@RequestParam("file") MultipartFile file*/) throws Exception {
        // 读取Excel文件
        // 获取输入流方案1
        /*String fileName = file.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (!".xls".equalsIgnoreCase(fileType) && !".xlsx".equalsIgnoreCase(fileType)) {
            throw new CommonException("File type error!");
        }
        InputStream ins = file.getInputStream();*/

        // 获取输入流方案2(用于postman测试)
        String fileContent = "import_data";
        String fileName = "excel_test.xlsx";
        String sign = System.getProperty("file.separator");
        String root = System.getProperty("user.dir");
        String content = "dadal-cloud-branch";
        FileInputStream ins = new FileInputStream(new File(root + sign + content + sign + fileContent + sign + fileName));

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
            ForkJoinTask<Integer> task = new ForkJoinableTask(0, listData.size(), listData);
            ForkJoinTask<Integer> result = pool.submit(task);
            ins.close();
            return HttpResult.success("File data import successed!");
        }
        return HttpResult.fail("File data import failed!");
    }


    // GET方式测试下载
    @Logged(remark = "exportExcelTest")
    @GetMapping("/exportExcelTest")
    public void exportExcelTest(HttpServletResponse response) throws IOException {
        List<Demo> exportExcelList = new ArrayList<>();
        byte[] dataArray = EasyPoiUtils.exportExcel(exportExcelList, "Demo_Excel", "Demo_Excel_SHEET",
                Demo.class);
        String codeFileName = "Demo_Excel";
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + codeFileName + ".xlsx");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(dataArray);
        outputStream.flush();
        outputStream.close();
    }

    // 将页面数据下载到excel
    @Logged(remark = "exportExcel")
    @PostMapping("/exportExcel")
    @RequiresPermissions("sys:sys:other")
    public void exportExcel(@RequestBody @Valid DemoPostDto vo, HttpServletResponse response) throws IOException {
        List<Demo> exportExcelList = this.demoService.findAll(500);
        byte[] dataArray = EasyPoiUtils.exportExcel(exportExcelList, "Demo_Excel", "Demo_Excel_SHEET",
                Demo.class);
        String codeFileName = "Demo_Excel";
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + codeFileName + ".xlsx");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(dataArray);
        outputStream.flush();
        outputStream.close();
    }

    // 将excel文件数据导入到库中
    @Logged(remark = "importExcel")
    @PostMapping(value = "/importExcel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HttpResult uploadExcel(@RequestParam(value = "file") MultipartFile file) {
        try {
            List<Demo> list = EasyPoiUtils.importExcel(file, 0, 1, 1, Demo.class);
            if (ObjectUtils.isEmpty(list)) {
                return HttpResult.fail("Imported file is null!");
            }
            demoService.batchInsert(list);
            return HttpResult.success();
        } catch (Exception e) {
            return HttpResult.fail("Import file was not successful!");
        }

    }

}
