package org.cmcc.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author zhaolei
 * @date 2021-04-09 11:41
 */
@RestController
@RequestMapping(value = "testzl")
public class HystrixController {
    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "fallbackHystrix")
    @GetMapping(value = "getInfo")
    public String testHystrix(){
        System.out.println("================testHystrix============");
        String url = "http://localhost:8081/edu-teacher/user/login11";
        String result = restTemplate.getForObject(url, String.class);
        return result;
    }


    public String fallbackHystrix(){
        return "执行了hystrix方法===";
    }
}
