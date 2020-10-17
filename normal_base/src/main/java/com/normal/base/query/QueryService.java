package com.normal.base.query;

import com.normal.base.utils.Objs;
import com.normal.dao.base.QueryMapper;
import com.normal.dao.base.QuerySql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: fei.he
 */
public class QueryService {

    @Autowired
    private QueryMapper queryMapper;

    @Autowired
    SqlSession sqlSession;

    public <T> List<T> queryList(QuerySql querySql, Class<T> itemClazz){
        List<Map<String, Object>> rst = queryMapper.query(querySql.toString());

        if(rst == null  &&  rst.isEmpty()){
            return null;
        }

        if (Map.class.isAssignableFrom(itemClazz)) {
            return (List<T>) rst;
        }

        return rst.stream()
                .map((item) -> Objs.toObj(item, itemClazz))
                .collect(Collectors.toList());
    }


    public <T> T querySingle(QuerySql querySql, Class<T> clazz) {
        List<Map<String, Object>> rst = queryMapper.query(querySql.toString());

        if (rst.isEmpty()) {
            return null;
        }

        Map<String, Object> item = rst.get(0);
        if (isSimpleClazz(clazz)) {
            String itemValue = (String) item.get(querySql.getSimpleColumnAlias());
            if (String.class.equals(clazz)) {
                return clazz.cast(itemValue);
            }
            if (Integer.class.equals(clazz) || int.class.equals(clazz)) {
                return (T) Integer.valueOf(itemValue);
            }
            if (Long.class.equals(clazz) || long.class.equals(clazz)) {
                return (T) Long.valueOf(itemValue);
            }
            if (Boolean.class.equals(clazz) || boolean.class.equals(clazz)) {
                return (T) Boolean.valueOf(itemValue);
            }
            if (Float.class.equals(clazz) || float.class.equals(clazz)) {
                return (T) Float.valueOf(itemValue);
            }
            if (Double.class.equals(clazz) || double.class.equals(clazz)) {
                return (T) Double.valueOf(itemValue);
            }

            if (Map.class.isAssignableFrom(clazz)) {
                return clazz.cast(item);
            }
        }
        throw new UnsupportedOperationException("查询服务不支持该类型返回值: clazz: " + clazz.getName());
    }

    private <T> boolean isSimpleClazz(Class<T> clazz) {
        return clazz.isPrimitive() || clazz.equals(String.class);
    }
}
