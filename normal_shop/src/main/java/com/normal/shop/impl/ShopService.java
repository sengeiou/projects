package com.normal.shop.impl;

import com.normal.base.cache.Cache;
import com.normal.base.query.QueryService;
import com.normal.dao.base.QuerySql;
import com.normal.model.BizCodes;
import com.normal.model.BizDictEnums;
import com.normal.model.openapi.DefaultPageOpenApiQueryParam;
import com.normal.model.openapi.PddOpenApiQueryParam;
import com.normal.model.openapi.TbOpenApiQueryParam;
import com.normal.model.shop.GoodCat;
import com.normal.model.shop.ListGood;
import com.normal.openapi.impl.OpenApiManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author: fei.he
 */
@Component
public class ShopService {

    @Autowired
    QueryService queryService;

    @Autowired
    Cache cache;

    @Autowired
    OpenApiManager openApiManager;

    /**
     * 查询所有的类目
     *
     * @return
     */
    public List<GoodCat> queryCats() {
        Optional optional = cache.get(BizCodes.CATS_CACHE_KEY);
        if (optional.isPresent()) {
            return (List<GoodCat>) optional.get();
        }
        QuerySql sql = QuerySql.newInstance()
                .fromTable
                        ("trd_shop_config_cats")
                .column("id")
                .column("refer_id")
                .column("name")
                .column("platform")
                .withOrderBy("order by  platform desc");
        List<GoodCat> cats = queryService.queryList(sql, GoodCat.class);
        cache.put(BizCodes.CATS_CACHE_KEY, cats);
        return cats;
    }

    public List<ListGood> pageQueryByCat(DefaultPageOpenApiQueryParam param) {
        Objects.requireNonNull(param.getCatId());
        Objects.requireNonNull(param.getPlatform(), "平台参数不可为空");
        if (BizDictEnums.PLATFORM_TB.key().equals(param.getPlatform())) {
            TbOpenApiQueryParam tbParam = new TbOpenApiQueryParam(param);
            tbParam.setTbMaterialId(tbParam.getCatId());
            return openApiManager.tbQueryByMaterialId(tbParam).getResults();
        }
        if (BizDictEnums.PLATFORM_PDD.key().equals(param.getPlatform())) {
            return openApiManager.pddPageQueryGoods(new PddOpenApiQueryParam(param)).getResults();
        }
        throw new IllegalArgumentException("平台参数未知");
    }


    public List<Map> queryBanners() {
        QuerySql sql = QuerySql.newInstance()
                .fromTable("trd_shop_config_banner")
                .column("title")
                .column("background")
                .column("refer");
        List<Map> activies = queryService.queryList(sql, Map.class);
        return activies;
    }

    public List<Map> queryConfigUnits() {
        QuerySql sql = QuerySql.newInstance()
                .fromTable("trd_shop_config_unit")
                .column("icon")
                .column("title")
                .column("refer")
                .withOrderBy("order by sort_num");
        return queryService.queryList(sql, Map.class);
    }

    public List<Map> queryConfigHot3() {
        QuerySql sql = QuerySql.newInstance()
                .fromTable("trd_shop_config_hot3")
                .column("title")
                .column("image_url")
                .column("refer");
        return queryService.queryList(sql, Map.class);
    }


    public List<ListGood> pageQueryRecommendGoods(TbOpenApiQueryParam param) {
        param.setTbMaterialId(Long.valueOf(BizDictEnums.QUERY_CNXH.key()));
        return openApiManager.tbQueryByMaterialId(param).getResults();
    }


}
