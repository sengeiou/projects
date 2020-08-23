package com.normal.autosend.impl;

import com.normal.dao.context.BizContextMapper;
import com.normal.model.autosend.SendGood;
import com.normal.model.context.BizContext;
import com.normal.model.context.BizContextTypes;
import com.normal.openapi.IOpenApiService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 指定物料id轮询策略
 */
public class BoundMaterialVoteSendGoodQueryStrategy implements SendGoodQueryStrategy {

    private List<String> materialIds;
    private String strategyId;

    @Autowired
    private IOpenApiService openApiService;

    @Autowired
    BizContextMapper bizContextMapper;


    public BoundMaterialVoteSendGoodQueryStrategy(List<String> materialIds, String strategyId) {
        this.materialIds = materialIds;
        this.strategyId = strategyId;
    }


    @Override
    public List<SendGood> querySendGoods() {
        for (int i = 0; i < materialIds.size(); i++) {

            String materialId = materialIds.get(i);
            BizContext ctx = bizContextMapper.queryByType(BizContextTypes.querySendGood + "_" + materialId);
            if (ctx == null) {
                ctx = new Context();
            }

            Map<String, Object> param =  ctx.getQueryParam();

            List<SendGood> sendGoods = openApiService.querySendGoods(param);

        }
    }

    @Data
    static class Context extends BizContext {
        private int materialIdx;
        private int pageNo;


    }
}
