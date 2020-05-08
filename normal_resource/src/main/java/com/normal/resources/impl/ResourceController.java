package com.normal.resources.impl;

import com.normal.core.mybatis.PageParam;
import com.normal.core.web.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public Result queryResByLabels(PageParam pageQueryParam) {
        return Result.success(resourceDao.queryByLabels(pageQueryParam));
    }

    @ResponseBody
    @GetMapping("queryResByKeyword")
    public Result queryResByKeyword(PageParam pageQueryParam) {
        return Result.success(resourceDao.queryByKeyword(pageQueryParam));
    }
}


