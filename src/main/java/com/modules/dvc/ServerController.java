package com.modules.dvc;

import net.minidev.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 服务端接口发布
 * @Author: QiuQiang
 * @Date: 2020-11-07
 */
@RestController
@RequestMapping("/youku1327/")
public class ServerController {

    // get
    @GetMapping("user")
    public String getUser() {
        return "youku1327";
    }

    // get带参
    @GetMapping("user/{name}")
    public String getUserName(@PathVariable String name) {
        return name;
    }

    // post
    @PostMapping("provider")
    public ResponseEntity<String> addData(@RequestBody JSONObject jsonObject) {
        String user = (String) jsonObject.get("user");
        return ResponseEntity.ok(user);
    }

    // put
    @PutMapping("provider/{id}")
    public ResponseEntity<JSONObject> updateData(@PathVariable Long id, @RequestBody JSONObject jsonObject) {
        Object object = jsonObject.get("user");
        jsonObject.put("id", id);
        // {"id":1327,"user":"youku1327"}
        System.out.println(jsonObject);
        return ResponseEntity.ok(jsonObject);
    }

    // delete
    @DeleteMapping("provider/{id}")
    public ResponseEntity<String> delData(@PathVariable Long id) {
        String result = "delete" + id + "success";
        // delete1327success
        System.out.println(result);
        return ResponseEntity.ok(result);
    }

    // exchange参数说明: url, 请求方式, 请求实体, 返回值类型, uri地址变量.
    @GetMapping("user/exchange/{name}")
    public String getUserNameUserExchange(@PathVariable String name) {
        return name;
    }

}
