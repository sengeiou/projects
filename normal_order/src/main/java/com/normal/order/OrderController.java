package com.normal.order;

import com.normal.model.order.Order;
import com.normal.base.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@Controller
@RequestMapping("order")
public class OrderController extends BaseController {

    @Autowired
    private IOrderService orderService;

    @RequestMapping("createOrder")
    @ResponseBody
    public ModelAndView createOrder(Order order) {
        orderService.createOrder(order);
        return new ModelAndView("qrcode", new HashMap<>());
    }


}


