package com.modules.base.respcode;

import com.core.anotation.Logged;
import com.core.constant.Constant;
import com.core.result.HttpResult;
import com.core.utils.EnumUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-06-03
 */
@RequestMapping("/code")
@RestController
@Slf4j
@Validated
@Api(tags = "00-系统响应码")
public class RespCodeController {

    // @AuthCheck
    @Logged(type = Constant.Log.TYPE_QUERY, module = Constant.Log.MODULE_SYS, remark = "系统响应码列表")
    @ApiOperation(value = "系统响应码列表")
    @PostMapping("/list")
    public HttpResult<JSONArray> list() {
        JSONArray js = EnumUtil.listEnum();
        return HttpResult.success(js);
    }

}
