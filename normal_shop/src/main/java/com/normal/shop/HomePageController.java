package com.normal.shop;

import com.normal.base.query.QueryService;
import com.normal.base.query.QuerySql;
import com.normal.base.web.Result;
import com.normal.model.PageParam;
import com.normal.model.openapi.PageOpenApiGoodQueryParam;
import com.normal.openapi.IOpenApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author: fei.he
 */
@RestController("shop/home")
public class HomePageController {

    @Autowired
    private QueryService queryService;

    @Autowired
    @Qualifier("taobaoOpenApiService")
    private IOpenApiService openApiService;

    @GetMapping("listBanner")
    public Result listBanner() {
        QuerySql sql = QuerySql.newInstance()
                .fromTable("trd_shop_banner")
                .column("material_id")
                .column("banner_title");
        return Result.success(queryService.query(sql, List.class));
    }


    @GetMapping("customGoods")
    public Result customGoods(PageOpenApiGoodQueryParam param) {
        return Result.success(openApiService.pageCustomeQueryGoods(param));
    }


}
