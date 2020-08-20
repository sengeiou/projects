package com.normal.resources.impl;

import com.normal.model.PageParam;
import com.normal.base.mybatis.Pages;
import com.normal.base.web.BaseController;
import com.normal.base.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fei.he
 */
@Controller
@RequestMapping("resource")
public class ResourceController extends BaseController {

    @Autowired
    private ResourceDao resourceDao;

    @Autowired
    private ResourceService resourceService;

    @RequestMapping("resources")
    public ModelAndView resources(@ModelAttribute("pageParam") PageParam pageParam) {
        Map<String, Object> model = new HashMap<>(2);
        model.put("page", Pages.query(() -> resourceDao.queryResources(pageParam)));
        model.put("labels", ResourceLabelEnum.getDisplayLabels());
        return new ModelAndView("index", model);
    }

    @RequestMapping("resourceDetail")
    public ModelAndView resources(Long id) {
        Map<String, Object> model = new HashMap<>(1);
        Resource resource = resourceDao.queryByPrimaryKey(id);
        model.put("resource", resource);
        return new ModelAndView("detail", model);
    }

    @RequestMapping("queryRealUrl")
    @ResponseBody
    public Result queryRealUrl(Long id) {
        return resourceService.queryRealUrl(id);
    }


}


