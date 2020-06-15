package com.normal.resources.impl;

import com.normal.core.mybatis.PageParam;
import com.normal.core.mybatis.Pages;
import com.normal.core.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("resource")
public class ResourceController {

    @Autowired
    private ResourceDao resourceDao;

    @ModelAttribute
    public PageParam pageParam(@RequestParam(required = false) String labels,
                               @RequestParam(required = false) String keyword) {
        PageParam pageParam = new PageParam();
        pageParam.put("labels", labels);
        pageParam.put("keyword", keyword);
        return pageParam;
    }

    @RequestMapping("testRedirect")
    public String redirect() {
        return "redirect:https://www.baidu.com/";
    }


    @GetMapping("resources")
    public ModelAndView resources(@ModelAttribute("pageParam") PageParam pageParam) {
        return new ModelAndView("index", "page", Pages.query(() -> resourceDao.queryResources(pageParam)));
    }


    @ResponseBody
    @GetMapping("labels")
    public Result labels() {
        return Result.success(ResourceLabelEnum.getDisplayLabels());
    }
}


