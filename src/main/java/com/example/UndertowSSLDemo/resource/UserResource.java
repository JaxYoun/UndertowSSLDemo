package com.example.UndertowSSLDemo.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/2/24 11:47
 */
@RestController
@RequestMapping("/user")
public class UserResource {

    /**
     * 获取用户
     * @param id
     * @return
     */
    @GetMapping("/getUser")
    public String getUser(String id) {
        System.out.println(id);
        return "user0";
    }

}
