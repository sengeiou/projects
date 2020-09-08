package com.normal.model.openapi;

import com.normal.model.PageParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author: fei.he
 */
public class PageOpenApiGoodQueryParam extends PageParam implements OpenApiParam {

    @Override
    public void check() {
        assertIsNotNull(Arrays.asList("pageNo", "pageSize"));
    }

    private void assertIsNotNull(List<String> keys) {
        for (String key : keys) {
            if (this.get(key) == null) {
                throw new RuntimeException("参数校验失败, key: " + key + " 为空");
            }
        }
    }

    @Override
    public Map<String, Object> getParam() {
        return this;
    }


}
