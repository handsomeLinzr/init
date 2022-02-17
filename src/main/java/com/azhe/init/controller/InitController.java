package com.azhe.init.controller;

import com.azhe.pc_menu.init.service.IService;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Description:
 *
 * @author Linzr
 * @version V2.0.0
 * @date 2022/1/8 10:17 上午
 * @since V2.0.0
 */
@RestController
@RequestMapping("/init")
public class InitController {

    private final Map<String, IService> serviceMap;

    public InitController(Map<String, IService> serviceMap) {
        this.serviceMap = serviceMap;
    }

    @GetMapping("/role/{op}/{type}")
    public Integer initRole(@PathVariable String op, @PathVariable String type, @RequestParam(required = false) String phone) {
        if (!serviceMap.containsKey(op)) {
            return 0;
        }
        if (!"all".equals(type) && StringUtils.isBlank(phone)) {
            throw new RuntimeException("phone 不能为空");
        }
        return serviceMap.get(op).operate(type, phone);
    }
    
}
