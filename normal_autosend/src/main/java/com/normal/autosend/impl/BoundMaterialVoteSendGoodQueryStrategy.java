package com.normal.autosend.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.normal.base.biz.BizContextService;
import com.normal.base.utils.Objs;
import com.normal.dao.context.BizContextMapper;
import com.normal.model.autosend.SendGood;
import com.normal.model.context.BizContextTypes;
import com.normal.openapi.IAutoSendGoodsQueryService;
import com.normal.openapi.IOpenApiService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 指定物料id轮询策略
 */
public class BoundMaterialVoteSendGoodQueryStrategy implements SendGoodQueryStrategy {

    private Context ctx;

    private String strategyId;

    @Autowired
    private IOpenApiService openApiService;

    @Autowired
    private IAutoSendGoodsQueryService autoSendGoodsQueryService;

    @Autowired
    BizContextMapper bizContextMapper;

    @Autowired
    BizContextService bizContextService;

    @Autowired
    ObjectMapper objectMapper;


    public BoundMaterialVoteSendGoodQueryStrategy(List<String> materialIds, String strategyId) {
        this.strategyId = strategyId;
        this.ctx = new Context(materialIds);
    }

    @Override
    public List<SendGood> querySendGoods() {
        Context ctx = bizContextService.getByTypeKey(BizContextTypes.querySendGood, Context.class);
        if (ctx == null) {
            ctx = this.ctx;
            bizContextService.insertCtx(BizContextTypes.querySendGood, ctx);
        }
        if (ctx != null && ctx.isExpire()) {
            ctx = this.ctx;
            bizContextService.deleteContext(BizContextTypes.querySendGood);
            bizContextService.insertCtx(BizContextTypes.querySendGood, ctx);
        }
        Map<String, Object> param = ctx.getNextParam();
        bizContextService.updateCtxObjByType(BizContextTypes.querySendGood, Objs.toJson(ctx));
        return autoSendGoodsQueryService.querySendGoods(param);
    }

    @Override
    public String strategyId() {
        return this.strategyId;
    }

    @Data
    static class Context {
        private int materialIdx = -1;
        private int pageNo = 1;
        private List<String> materialIds;

        private long timestamp;

        public Context() {
        }

        public Context(List<String> materialIds) {
            this.materialIds = materialIds;
            this.timestamp = System.currentTimeMillis();
        }

        @JsonIgnore
        public Map<String, Object> getNextParam() {
            Map<String, Object> param = new HashMap<>(3);
            param.put("pageSize", 2);
            boolean end = materialIdx == materialIds.size() - 1;
            if (end) {
                materialIdx = 0;
                pageNo++;
            } else {
                materialIdx++;
            }
            param.put("materialId", materialIds.get(materialIdx));
            param.put("pageNo", pageNo);
            return param;
        }

        @JsonIgnore
        public boolean isExpire() {
            int dayOfMonth = LocalDateTime.ofInstant(Instant.ofEpochMilli(this.timestamp), ZoneOffset.ofHours(8)).getDayOfMonth();
            int nowDayOfMonth = LocalDateTime.now().getDayOfMonth();
            return nowDayOfMonth - dayOfMonth > 0;
        }
    }

}
