package com.normal.shop.impl;

import com.normal.base.web.Result;
import com.normal.model.openapi.DefaultPageOpenApiQueryParam;
import com.normal.model.openapi.TbOpenApiQueryParam;
import com.normal.openapi.impl.OpenApiQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: fei.he
 */
@RestController
@RequestMapping("shop")
public class ShopController {

    @Autowired
    ShopService shopService;

    @GetMapping("queryBanners")
    public Result queryBanners() {
        return Result.success(shopService.queryBanners());
    }

    @GetMapping("queryConfigUnits")
    public Result queryConfigUnits() {
        return Result.success(shopService.queryConfigUnits());
    }

    @GetMapping("pageQueryRecommendGoods")
    public Result pageQueryRecommendGoods(@OpenApiQueryParam TbOpenApiQueryParam param) {
        return Result.success(shopService.pageQueryRecommendGoods(param));
    }

    @GetMapping("queryCats")
    public Result queryCats() {
        return Result.success(shopService.queryCats());
    }

    @GetMapping("pageQueryByCat")
    public Result pageQueryByCat(@OpenApiQueryParam DefaultPageOpenApiQueryParam param) {
        return Result.success(shopService.pageQueryByCat(param));
    }


}
