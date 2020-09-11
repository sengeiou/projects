package com.normal.shop.impl;

import com.normal.base.query.QueryService;
import com.normal.dao.base.QuerySql;
import com.normal.base.web.Result;
import com.normal.model.BizDictEnums;
import com.normal.model.openapi.DefaultPageOpenApiQueryParam;
import com.normal.model.shop.GoodCat;
import com.normal.openapi.IOpenApiService;
import com.normal.openapi.impl.OpenApiQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: fei.he
 */
@RestController
@RequestMapping("shop")
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
                .column("banner_title")
                .column("image");
        return Result.success(queryService.query(sql, List.class));
    }

    /**
     * 实时热销
     *
     * @return
     */
    @GetMapping("ssrx")
    public Result ssrx(@OpenApiQueryParam DefaultPageOpenApiQueryParam param) {
        param.withQueryType(BizDictEnums.QUERY_SSRX);
        return Result.success(openApiService.pageQueryGoods(param).getResults());
    }

    /**
     * 关键字搜素
     * @return
     */
    @GetMapping("gjz")
    public Result gjz(@OpenApiQueryParam DefaultPageOpenApiQueryParam param) {
        param.withQueryType(BizDictEnums.QUERY_GJZ);
        return Result.success(openApiService.pageQueryGoods(param));
    }

    /**
     * 猜你喜欢
     *
     * @return
     */
    @GetMapping("cnxh")
    public Result cnxh(@OpenApiQueryParam DefaultPageOpenApiQueryParam param) {
        param.withQueryType(BizDictEnums.QUERY_CNXH);
        return Result.success(openApiService.pageQueryGoods(param).getResults());
    }


    /**
     * 商品详情
     *
     * @return
     */
    @Deprecated
    @GetMapping("spxq")
    public Result spxq(String itemId) {
        return Result.success(openApiService.queryItemGood(itemId));
    }


    /**
     * 类目树
     *
     * @return
     */
    @GetMapping("lms")
    public Result lms() {
        List<GoodCat> cats = new ArrayList<>(5);
        for (BizDictEnums parent : Arrays.asList(
                BizDictEnums.RM_ZH,
                BizDictEnums.GYQ_ZH,
                BizDictEnums.DEQ_ZH,
                BizDictEnums.PPQ_ZH,
                BizDictEnums.HQZB_ZH
        )) {
            GoodCat root = new GoodCat(parent);
            for (BizDictEnums child : BizDictEnums.getValuesByType(parent.getType())) {
                if (child.equals(parent)) {
                    continue;
                }
                root.addChild(child);
            }
            cats.add(root);
        }
        return Result.success(cats);
    }

}
