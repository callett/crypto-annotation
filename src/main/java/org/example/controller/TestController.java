package org.example.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.annotation.Decrypt;
import org.example.annotation.Encrypt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/*
    注解解释：
    @Decrypt 请求参数解密
    @Encrypt 响应内容加密
    注解不写 algorithm 参数默认 AES 算法

    加解密算法可根据项目调整
    加解密算法不匹配会报错，比如入参是 AES 加密的话，解密也需要选 AES ，反之
    返回参数加密算法根据实际项目需求调整
 */
@RestController
@RequestMapping("/api")
public class TestController {

    /*
        测试 URL POST
        http://localhost:8080/api/body
        Headers设置 Content-Type: application/json
        Body -> text -> 填入加密参数
        y96g9/398f/McK6bQWUNyHXxw0Tp7GOfIM6j5MohWWU=
     */
    @PostMapping("/body")
    @Encrypt(algorithm = "AES")
    @Decrypt(algorithm = "AES")
    public String handleBody(@RequestBody String body) throws Exception {
        System.out.println("解密后json明文：" + body);

        // 可选转成 Map 进行后续逻辑操作
//        ObjectMapper objectMapper = new ObjectMapper();
//        Map<String, Object> map = objectMapper.readValue(body, new TypeReference<>() {
//        });
//        System.out.println("明文数据转map：" + map);

        return "Got body: " + body;
    }

    /*
        测试接口 URL GET
        Headers设置 Content-Type: application/json
        http://localhost:8080/api/param?data=3fvaLg5IDlveswuXzhVQcw==
     */
    @GetMapping("/param")
//    @Decrypt(algorithm = "RSA")
    @Decrypt(algorithm = "AES")
    @Encrypt(algorithm = "RSA")
//    @Encrypt(algorithm = "AES")
    public String handleParam(@RequestParam String data) {
        System.out.println("解密后明文：" + data);
        return "Got param: " + data;
    }


    /*
        测试接口 URL GET
        Headers设置 Content-Type: application/json
        http://localhost:8080/api/path/3fvaLg5IDlveswuXzhVQcw==
     */
    @GetMapping("/path/{data}")
    @Encrypt
    @Decrypt
    public String handlePath(@PathVariable String data) {
        System.out.println("解密后明文：" + data);
        return "Got path: " + data;
    }
}
