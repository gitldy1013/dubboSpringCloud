package org.cmcc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import org.cmcc.service.DubboService;

/**
 * @author cmcc
 * @date 2020/6/26
 */
@RestController
public class WebController {
    @Autowired
    private DubboService dubboService;

    @GetMapping("/test/{p}")
    public String test(@PathVariable("p") String param) {
        return dubboService.test(param);
    }
}
