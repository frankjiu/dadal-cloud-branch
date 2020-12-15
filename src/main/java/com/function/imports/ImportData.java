package com.function.imports;

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
public class ImportData {

    @Autowired
    private ForkJoinPool pool;

    @Autowired
    private FileParser fileParser;

    /**
     * import file data
     *
     * @param fileContent(import_data)
     * @param fileName(data.txt)
     * @return
     */
    @PostMapping("/import")
    public HttpResult imports(@RequestParam("fileContent") String fileContent, @RequestParam("fileName") String fileName) {
        List<Demo> list = fileParser.parse(fileContent, fileName);
        try {
            long start = System.currentTimeMillis();
            ForkJoinCalculate task = new ForkJoinCalculate(0, list.size(), list);
            ForkJoinTask<Integer> result = pool.submit(task);
            long end = System.currentTimeMillis();
            return HttpResult.success("Data import successed " + list.size() + ", forked: " + result.get() + ", cost:" + (end - start) / 1000);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            return HttpResult.fail("import failed!");
        }
    }


}
