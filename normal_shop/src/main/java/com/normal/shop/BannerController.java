package com.normal.shop;

import com.normal.base.web.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: fei.he
 */
@RestController("shop/banner")
public class BannerController {


    @GetMapping("listBanner")
    public Result listBanner() {
        return null;
    }


}
