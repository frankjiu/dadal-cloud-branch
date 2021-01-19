package com.function.imports;

import com.core.exception.CommonException;
import com.core.result.HttpResult;
import com.modules.base.model.entity.Demo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-12-15
 */
@RestController
@Slf4j
public class ImportDataFromText {

    @Autowired
    private ForkJoinPool pool;

    /**
     * import file-text data
     *
     * @param fileContent(import_data)
     * @param fileName(data.txt)
     * @return
     */
    @PostMapping("/import")
    public HttpResult imports(@RequestParam("fileContent") String fileContent, @RequestParam("fileName") String fileName) {
        // 文件格式校验
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (!".xls".equalsIgnoreCase(suffix) && !".xlsx".equalsIgnoreCase(suffix)) {
            throw new CommonException("File type error!");
        }

        // 文件解析
        List<Demo> list = FileTxtParser.parse(fileContent, fileName);
        try {
            long start = System.currentTimeMillis();
            ForkJoinableTask task = new ForkJoinableTask(0, list.size(), list);
            ForkJoinTask<Integer> result = pool.submit(task);
            long end = System.currentTimeMillis();
            return HttpResult.success("Data import successed " + list.size() + ", forked: " + result.get() + ", cost:" + (end - start) / 1000);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            return HttpResult.fail("Import file was not successful!");
        }
    }


}
