package com.normal.biz.order;

import com.normal.core.mybatis.PageParam;
import com.normal.core.mybatis.Pages;
import com.normal.core.web.BaseController;
import com.normal.core.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("order")
public class OrderController extends BaseController {

    @Autowired
    private IOrderService orderService;

    @RequestMapping("createOrder")
    @ResponseBody
    public Result createOrder(Order order) {
        return Result.success(orderService.createOrder(order));
    }

}


