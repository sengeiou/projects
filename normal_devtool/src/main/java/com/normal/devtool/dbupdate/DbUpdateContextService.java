package com.normal.devtool.dbupdate;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author: fei.he
 */
public interface DbUpdateContextService {

    /**
     * xml解析
     * @param xmlFile
     * @return
     */
    ParseNode parse(InputStream xmlFile);


    /**
     * 根据元数据生成增量升级脚本 (脚本可重复执行)
     * @param root
     * @param referDb
     * @return
     */
    OutputStream generateUpdateSql(ParseNode root, DataBase referDb);




}
