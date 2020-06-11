package com.normal.portal.impl;

import com.normal.core.mybatis.PageParam;
import com.normal.core.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("post")
public class PostController {

    @Autowired
    private PostService postService;

    @RequestMapping("add")
    @ResponseBody
    public Result addPost(MultipartFile post, HttpServletRequest request) {
        return postService.addPost(post, request);
    }

    @RequestMapping("delete")
    @ResponseBody
    public Result deletePost(Integer id, HttpServletRequest request) {
        return postService.deletePost(id, request);
    }


    @RequestMapping("list")
    public ModelAndView listPost(PageParam param) {
        Result result = postService.listPost(param);
        return new ModelAndView("postList", "result", result);
    }

    @RequestMapping("mng")
    public ModelAndView toMng() {
        return new ModelAndView("postMng");
    }

}
