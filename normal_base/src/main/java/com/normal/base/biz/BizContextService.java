package com.normal.base.biz;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.normal.base.utils.Objs;
import com.normal.dao.context.BizContextMapper;
import com.normal.model.context.BizContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * @author: fei.he
 */
@Component
public class BizContextService {
    public static final Logger logger = LoggerFactory.getLogger(BizContextService.class);

    @Autowired
    BizContextMapper bizContextMapper;

    @Autowired
    ObjectMapper objectMapper;

    public void insert(BizContext context) {
        bizContextMapper.insert(context);
    }

    public void insertCtx(String typeKey, Object ctx) {
        BizContext bizContext = new BizContext();
        bizContext.setType(typeKey);
        bizContext.setContext(Objs.toJson(ctx));
        bizContextMapper.insert(bizContext);
    }

    public BizContext recoverContext(String type) {
        BizContext bizContext = bizContextMapper.queryByTypeKey(type);
        bizContextMapper.deleteByType(type);
        return bizContext;
    }

    public void deleteContext(String type){
        bizContextMapper.deleteByType(type);
    }

    public <T> Optional<T> loadCtxObj(String ctx, Class<T> clazz) {
        try {
            if (StringUtils.isEmpty(ctx)) {
                return Optional.of(null);
            }
            return Optional.of(objectMapper.readValue(ctx, clazz));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateCtxObjByType(String typeKey, String ctxJson) {
        bizContextMapper.updateCtxObjByType(typeKey, ctxJson);
    }

    public <T> T getByTypeKey(String typeKey, Class<T> clazz) {
        BizContext bizContext = bizContextMapper.queryByTypeKey(typeKey);
        if (bizContext == null) {
            return null;
        }
        return loadCtxObj(bizContext.getContext(), clazz).get();
    }


}
