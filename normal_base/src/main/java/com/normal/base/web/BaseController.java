package com.normal.base.web;

import com.normal.model.PageParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

public class BaseController {

    @ModelAttribute
    public PageParam pageParam(@RequestParam(required = false) String labels,
                               @RequestParam(required = false) String keyword) {
        PageParam pageParam = new PageParam();
        pageParam.put("labels", labels);
        pageParam.put("keyword", keyword);
        return pageParam;
    }


}
