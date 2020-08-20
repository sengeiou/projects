package com.normal.base.biz;

import com.normal.dao.context.BizContextMapper;
import com.normal.model.context.BizContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: fei.he
 */
@Component
public class BizContextService {

    @Autowired
    BizContextMapper bizContextMapper;

    public void insert(BizContext context) {
        bizContextMapper.insert(context);
    }

    public BizContext recoverContext(String type) {
        BizContext bizContext = bizContextMapper.queryByType(type);
        bizContextMapper.deleteByType(type);
        return bizContext;
    }

}
