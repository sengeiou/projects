package com.normal.resources.impl;

import com.normal.core.mybatis.Page;
import com.normal.core.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class ResourceController {
    @Autowired
    private ResourceDao resourceDao;

    @RequestMapping("testRedirect")
    public String redirect() {
        return "redirect:https://www.baidu.com/";
    }


    @ResponseBody
    @GetMapping("queryResByLabels")
    public Result queryResByLabels(Page<Resource> pageQueryParam) {
        pageQueryParam.setResults(resourceDao.queryByLabels(pageQueryParam));
        return Result.success(pageQueryParam);
    }

    @ResponseBody
    @GetMapping("queryResByKeyword")
    public Result queryResByKeyword(Page<Resource> pageQueryParam) {
        pageQueryParam.setResults(resourceDao.queryByKeyword(pageQueryParam));
        return Result.success(pageQueryParam);
    }
}


