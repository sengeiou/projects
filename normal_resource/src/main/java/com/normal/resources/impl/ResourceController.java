package com.normal.resources.impl;

import com.normal.core.mybatis.PageParam;
import com.normal.core.mybatis.Pages;
import com.normal.core.web.BaseController;
import com.normal.core.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("resource")
public class ResourceController extends BaseController {

    @Autowired
    private ResourceDao resourceDao;

    @RequestMapping("resources")
    public ModelAndView resources(@ModelAttribute("pageParam") PageParam pageParam) {
        Map<String, Object> model = new HashMap<>(2);
        model.put("page", Pages.query(() -> resourceDao.queryResources(pageParam)));
        model.put("labels", ResourceLabelEnum.getDisplayLabels());
        return new ModelAndView("index", model);
    }

}


