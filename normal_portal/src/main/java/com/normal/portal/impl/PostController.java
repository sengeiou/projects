package com.normal.portal.impl;

import com.normal.base.mybatis.PageParam;
import com.normal.base.web.BaseController;
import com.normal.base.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("post")
public class PostController extends BaseController {

    @Autowired
    private PostService postService;


    @RequestMapping("add")
     public ModelAndView addPost(MultipartFile post, HttpServletRequest request) {
        Result result = postService.addPost(post, request);
        return new ModelAndView("postMng", "success", String.valueOf(result.isSuccess()));
    }

    @RequestMapping("delete")
    @ResponseBody
    public ModelAndView deletePost(Integer id, HttpServletRequest request) {
        Result result = postService.deletePost(id, request);
        return new ModelAndView("postMng", "success", String.valueOf(result.isSuccess()));
    }


    @RequestMapping("list")
    public ModelAndView listPost(@ModelAttribute("pageParam") PageParam param) {
        Result result = postService.listPost(param);
        return new ModelAndView("postList", "data", result.getData());
    }

    @RequestMapping("mng")
    public ModelAndView toMng() {
        return new ModelAndView("postMng");
    }

    @RequestMapping("aboutMe")
    public ModelAndView aboutMe() {
        return new ModelAndView("aboutMe");
    }

    @RequestMapping("static/{detail}.do")
    public ModelAndView aboutMe(@PathVariable String detail) {
        return new ModelAndView(detail);
    }



}
