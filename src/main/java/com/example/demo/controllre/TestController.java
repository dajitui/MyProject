package com.example.demo.controllre;

import com.alibaba.fastjson.JSON;
import com.example.demo.annotation.MyMethod;
import com.example.demo.dto.DajituiDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author M
 */
@RestController
public class TestController {

    @Autowired
    private StringRedisTemplate template;

    @GetMapping("search")
    public String getSomeThing() {
        DajituiDTO dto = new DajituiDTO("大鸡腿", null, 18);
        String key = getTheKey("search");
        dto.setKey(key);
        template.opsForValue().set(key, JSON.toJSONString(dto), 10, TimeUnit.MINUTES);
        return dto.toString();
    }

    @GetMapping("edit")
    //@MyMethod(toClass = DajituiDTO.class)
    public String editSomeThing(DajituiDTO dto) {
        //修改并显示有注解字段的改变情况
        return dto.toString();
    }

    @PostMapping("edit1")
    @MyMethod(toClass = DajituiDTO.class)
    public String editSomeThingPost(@RequestBody DajituiDTO dto) {
        //修改并显示有注解字段的改变情况
        return dto.toString();
    }

    private String getTheKey(String requestUrl) {
        return System.currentTimeMillis() + requestUrl + ThreadLocalRandom.current().nextInt();
    }


}
