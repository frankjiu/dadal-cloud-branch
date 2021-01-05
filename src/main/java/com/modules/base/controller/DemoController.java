/**
 * All rights Reserved, Designed By www.xcompany.com
 *
 * @Package: com.modules.base.controller
 * @author: Frankjiu
 * @date: 2020年8月29日
 * @version: V1.0
 */

package com.modules.base.controller;

import com.core.anotation.SysLogged;
import com.core.exception.CommonException;
import com.core.result.HttpResult;
import com.core.result.PageModel;
import com.google.common.collect.Lists;
import com.modules.base.config.AppConfig;
import com.modules.base.model.dto.DemoGetDto;
import com.modules.base.model.dto.DemoPostDto;
import com.modules.base.model.entity.Demo;
import com.modules.base.model.vo.DemoUploadVo;
import com.modules.base.model.vo.DemoVo;
import com.modules.base.service.DemoService;
import com.modules.sys.admin.controller.AbstractController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Description: Demo Controller
 * @author: Frankjiu
 * @date: 2020年8月29日
 */
@RestController
@Slf4j
@Validated
public class DemoController extends AbstractController {

    @Autowired
    private DemoService demoService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Value("${limited.count}")
    private Integer limitedCount;

    @Value("${config.upload.filepath}")
    private String uploadFilePath;

    @Autowired
    private AppConfig appConfig;

    @GetMapping("demo/config")
    public HttpResult findConfig() {
        return HttpResult.success(appConfig);
    }

    @GetMapping("demo/{id}")
    public HttpResult findById(@PathVariable Integer id) {
        Demo demo = demoService.findById(id);
        if (demo == null) {
            return HttpResult.fail("record not found!");
        }
        return HttpResult.success(demo);
    }

    /**
     * 查询所有数据, 缓存到redis 30s
     * @return
     */
    @GetMapping("demo")
    public HttpResult findAll() {
        String key = "demo_find_all";
        // 查询Redis
        List<Demo> demoList = (List<Demo>)redisTemplate.opsForValue().get(key);
        // 查询DB
        if (CollectionUtils.isEmpty(demoList)) {
            log.info(">>> Query from db.., the total data limited at: " + limitedCount);
            demoList = demoService.findAll(limitedCount);
            if (demoList.size() > limitedCount) {
                throw new CommonException("Data size overload!");
            }
            redisTemplate.opsForValue().set(key, demoList, 30, TimeUnit.SECONDS); //30s容忍
        }
        List<DemoVo> demoVoList = demoList.stream()
                .map(e -> DemoVo.builder().id(e.getId()).cardName(e.getCardName()).cardNumber(e.getCardNumber()).createTime(e.getCreateTime()).build())
                .collect(Collectors.toList());

        return HttpResult.success(demoVoList);
    }

    @SysLogged(description = "Page query")
    // @RequiresPermissions("demo:demo:page")
    @PostMapping("demo/page")
    public HttpResult findPage(@RequestBody @Valid DemoGetDto demoGetDto) {
        List<Demo> demoList = demoService.findPage(demoGetDto);
        int total = demoService.count(demoGetDto);
        List<DemoVo> demoVoList = demoList.stream()
                .map(e -> DemoVo.builder().id(e.getId()).cardName(e.getCardName()).cardNumber(e.getCardNumber()).createTime(e.getCreateTime()).build())
                .collect(Collectors.toList());
        PageModel<DemoVo> pageModel = new PageModel<>();
        pageModel.setData(demoVoList);
        pageModel.setTotalCount(total);
        return HttpResult.success(pageModel);
    }

    /**
     * Post or Put
     * @param demoPostDto
     * @return
     */
    @RequestMapping(value = "demo", method = {RequestMethod.POST, RequestMethod.PUT})
    public HttpResult save(@RequestBody @Valid DemoPostDto demoPostDto) {
        TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());
        // data check: has been check with annotation; We can also check in another method with regex and commonException.
        Pattern pattern = Pattern.compile("^[A-Za-z]*$|^[\\u4e00-\\u9fa5]*$");
        boolean match = pattern.matcher(demoPostDto.getCardName()).matches();
        if (!match) {
            throw new CommonException("cardName: required, and only letters or Chinese characters are allowed!");
        }
        // data convert
        Demo demo = new Demo();
        BeanUtils.copyProperties(demoPostDto, demo);
        int num = 0;
        try {
            if (demo.getId() == null) {
                num = demoService.insert(demo);
            } else {
                num = demoService.update(demo);
            }
            transactionManager.commit(transaction);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            transactionManager.rollback(transaction);
        }

        if (num <= 0) {
            return HttpResult.fail("save failed!");
        }
        return HttpResult.success(demo.getId());
    }

    @DeleteMapping("demo/{id}")
    public HttpResult delete(@PathVariable Integer id) {
        TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());
        int num = 0;
        try {
            transactionManager.commit(transaction);
            num = demoService.delete(id);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            transactionManager.commit(transaction);
        }
        if (num <= 0) {
            return HttpResult.fail("delete failed");
        }
        return HttpResult.success();
    }

    /**
     * 缓存
     */
    @GetMapping("demo/redis/{key}")
    public HttpResult findRedis(@PathVariable String key) {
        Demo data = new Demo(999, "China_Bank_Test", "66668888", new Date());
        redisTemplate.opsForValue().set(key, data, 10, TimeUnit.MINUTES); // 10min effect time
        data = (Demo)redisTemplate.opsForValue().get(key);
        return HttpResult.success(data);
    }

    /**
     * 串行测试
     * @return
     */
    @GetMapping("testSerial")
    public HttpResult testThreadNo() {
        long start = System.currentTimeMillis();
        DemoGetDto demoGetDto1 = new DemoGetDto("ABC");
        List<Demo> list1 = demoService.findPage(demoGetDto1);
        DemoGetDto demoGetDto2 = new DemoGetDto("DEF");
        List<Demo> list2 = demoService.findPage(demoGetDto2);
        long end = System.currentTimeMillis();
        list1.addAll(list2);
        return HttpResult.success(list1, "cost:" + (end-start)/1000);
    }

    /**
     * 并行测试
     * @return
     */
    @GetMapping("testParallel")
    public HttpResult testThread() {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        List<DemoGetDto> conditionList = Lists.newArrayList(new DemoGetDto("ABC"), new DemoGetDto("DEF"));
        List<Demo> completeList = new ArrayList<>();

        long start = System.currentTimeMillis();
        CompletableFuture[] futureResult = conditionList.stream().map(object -> CompletableFuture.supplyAsync(() -> demoService.findPage(object), pool)
                .thenApply(k -> k)
                .whenComplete((t, e) -> {
                    System.out.println(">>>>>>task completed! result=" + t + "; exception=" + e + "; " + LocalDateTime.now());
                    completeList.addAll(t);
                })).toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futureResult).join();
        long end = System.currentTimeMillis();
        return HttpResult.success(completeList, "cost:" + (end-start)/1000);
    }

    /**
     * 文件上传测试
     */
    @PostMapping("demo/upload")
    public String uploadFile(@RequestBody DemoUploadVo uploadVo) throws IOException {
        File srcFile = new File(uploadVo.getSrcDir());

        File destDir = new File(uploadVo.getDestDir());
        if(!destDir.exists()){
            destDir.mkdir();
        }
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String oldName = srcFile.getName();
        String destFileName = UUID.randomUUID().toString().replace("-", "") + date + "_" + oldName;

        FileUtils.copyFile(srcFile, new File(destDir, destFileName));
        return "Upload successful!";
    }

    /**
     * 文件上传
     * @param uploadFile
     * @param req
     * @return
     */
    @PostMapping("demo/uploaded")
    public String upload(MultipartFile uploadFile, HttpServletRequest req) {
        if (StringUtils.isEmpty(uploadFile)) {
            return "请先选择文件!";
        }
        // rename the uploaded file
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String oldName = uploadFile.getOriginalFilename();
        String date = sdf.format(new Date());
        String newName = date + UUID.randomUUID().toString().replace("-", "") + oldName.substring(oldName.lastIndexOf('.'));
        try {
            // file save
            File file = new File(new File(uploadFilePath).getAbsolutePath() + File.separator + newName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }
            uploadFile.transferTo(file);
            return "上传成功: " + req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath() + File.separator
                    + newName;
        } catch (IllegalStateException | IOException e) {
            log.info("上传异常:{}", e.getMessage(), e);
        }
        return "上传失败!";
    }

    /**
     * 文件下载1
     * @param request
     * @param response
     * @throws IOException
     */
    @PostMapping("demo/download1")
    public void download(String path, HttpServletRequest request, HttpServletResponse response) {
        InputStream is = null;
        ServletOutputStream os = null;
        try {
            // path源文件路径
            File file = new File(path);
            // 接收参数
            // String fileName = request.getParameter("filename");
            String fileName = file.getName();
            // 处理请求文件名中文乱码(服务器默认码表, 前台页面码表)
            fileName = new String(fileName.getBytes("iso8859-1"), "utf-8");
            // 处理下载文件名中文乱码(客户端码表, 响应头码表)
            String downLoadFileName = new String(fileName.getBytes("gbk"), "iso8859-1");

            // 设置下载方式
            response.setHeader("Content-Disposition", "attachment;filename=" + downLoadFileName);
            // 文件下载
            //is = this.getServletContext().getResourceAsStream("/download/" + fileName); //文件存放在本项目download目录中
            //is = this.getClass().getClassLoader().getResourceAsStream("/download/" + fileName); //文件存放在本项目download目录中
            is = new BufferedInputStream(new FileInputStream(path)); // 从路径中读取被下载文件
            os = response.getOutputStream();
            int len;
            byte[] arrLen = new byte[1024];
            while ((len = is.read(arrLen)) != -1) {
                os.write(arrLen, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            log.info("下载出现异常!", e);
        } finally {
            // 关闭输入流
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    log.info("关闭输入流异常!", e);
                }
            }
            // 关闭输出流
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                    log.info("关闭输出流异常!", e);
                }
            }

        }
    }

    /**
     * 文件下载2
     * @param path
     * @param response
     * @return
     */
    @PostMapping("demo/download2")
    public HttpServletResponse download(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径
            File file = new File(path);
            // 取得文件名
            String filename = file.getName();
            // 取得文件的后缀名
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
            // 以流的形式下载文件
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return response;
    }

    /**
     * 下载本地文件
     */
    @PostMapping("demo/downloadlocal")
    public void downloadLocal(HttpServletResponse response) throws FileNotFoundException {
        // 下载本地文件
        String fileName = "my.doc"; // 文件的默认保存名
        // 读到流中
        InputStream inStream = new FileInputStream("D:/demo/stored/target.txt");// 文件的存放路径
        // 设置输出的格式
        response.reset();
        response.setContentType("bin");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        // 循环取出流中的数据
        byte[] b = new byte[100];
        int len;
        try {
            while ((len = inStream.read(b)) > 0)
                response.getOutputStream().write(b, 0, len);
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载网络文件
     */
    @PostMapping("demo/downloadnet")
    public void downloadNet(HttpServletResponse response) throws MalformedURLException {
        int whole = 0;
        URL url = new URL("http://p4.itc.cn/q_70/images03/20210105/836f79cab1364615b1e5bad0ef82f1bb.jpeg");
        try {
            URLConnection conn = url.openConnection();
            InputStream ins = conn.getInputStream();
            FileOutputStream fos = new FileOutputStream("D:/demo/download/a.jpeg");

            byte[] buffer = new byte[1024];
            int read;
            while ((read = ins.read(buffer)) != -1) {
                whole += read;
                fos.write(buffer, 0, read);
            }
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
