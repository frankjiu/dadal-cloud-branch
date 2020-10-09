/**
 * All rights Reserved, Designed By www.xcompany.com  
 * @Package: com.modules.sys.controller   
 * @author: Frankjiu
 * @date: 2020年8月29日
 * @version: V1.0
 */

package com.modules.sys.controller;

import com.exception.CommonException;
import com.modules.sys.model.dto.DemoDto;
import com.modules.sys.model.entity.Demo;
import com.modules.sys.model.vo.DemoVo;
import com.modules.sys.service.DemoService;
import com.result.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: Demo Controller
 * @author: Frankjiu
 * @date: 2020年8月29日
 */
@RestController
public class DemoController {

    @Autowired
    private DemoService demoService;

    @PostMapping("findDemoList")
    @SuppressWarnings("rawtypes")
    public HttpResult findDemoList(@RequestBody DemoDto demoDto) throws CommonException {
        List<Demo> demoList;
        try {
            demoList = demoService.findDemoList(demoDto);
        } catch (Exception e) {
            throw new CommonException("数据库查询出错!", e);
        }
        List<DemoVo> demoVoList = demoList.stream()
                .map(e -> DemoVo.builder().id(e.getId()).cardName(e.getCardName()).cardNumber(e.getCardNumber()).build())
                .collect(Collectors.toList());
        return HttpResult.success(demoVoList);

    }

}
