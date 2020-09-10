package com.normal.shop;

import com.normal.base.query.QueryService;
import com.normal.base.query.QuerySql;
import com.normal.base.web.Result;
import com.normal.model.BizDictEnums;
import com.normal.model.openapi.DefaultPageOpenApiQueryParam;
import com.normal.openapi.IOpenApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: fei.he
 */
@RestController("shop/home")
public class ShopController {

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

    /**
     * 实时热销
     *
     * @return
     */
    @GetMapping("ssrx")
    public Result ssrx() {
        DefaultPageOpenApiQueryParam param = DefaultPageOpenApiQueryParam
                .newInstance()
                .withQueryType(BizDictEnums.QUERY_SSRX);
        return Result.success(openApiService.pageQueryGoods(param).getResults());
    }

    /**
     * 个性化推荐
     *
     * @return
     */
    @GetMapping("gxhtj")
    public Result gxhtj(DefaultPageOpenApiQueryParam param) {
        param.withQueryType(BizDictEnums.QUERY_GXHTJ);
        return Result.success(openApiService.pageQueryGoods(param));
    }

    /**
     * 相关推荐
     * @return
     */
    @GetMapping("xgtj")
    public Result xgtj() {
        DefaultPageOpenApiQueryParam param = DefaultPageOpenApiQueryParam
                .newInstance()
                .withQueryType(BizDictEnums.QUERY_XGTJ);
        return Result.success(openApiService.pageQueryGoods(param).getResults());
    }


    /**
     * 商品详情
     * @return
     */
    @GetMapping("spxq")
    public Result spxq(String itemId) {
        return Result.success(openApiService.queryItemGood(itemId));
    }

}
