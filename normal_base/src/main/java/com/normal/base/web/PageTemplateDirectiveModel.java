package com.normal.base.web;

import com.normal.base.mybatis.Page;
import freemarker.core.Environment;
import freemarker.ext.beans.StringModel;
import freemarker.template.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author fei.he
 */
public class PageTemplateDirectiveModel implements TemplateDirectiveModel {


    public static final Logger logger = LoggerFactory.getLogger(PageTemplateDirectiveModel.class);

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        Object pageModel = params.get("data");
        Object url = params.get("url");
        boolean paramIsRight = pageModel != null
                && pageModel instanceof StringModel
                && ((StringModel) pageModel).getWrappedObject() instanceof Page
                && url != null;

        if (!paramIsRight) {
            throw new TemplateModelException("page directive param error, must be PageType");
        }

        Page page = (Page) ((StringModel) pageModel).getWrappedObject();
        if (page.getTotalRecord() == 0) {
            return;
        }
        StringJoiner joiner = new StringJoiner("\n", "<div class=\"page\">", "</div>");
        StringBuffer buf = new StringBuffer();
        String beforeUrl = url + "?pageNo=" + (page.getPageNo() - 1);
        String afterUrl = url + "?pageNo=" + (page.getPageNo() + 1);
        if (page.getPageNo() == 0) {
            buf.append("<a class=\"before\" href=\"#\"  style=\"color: #cccccc;float: left\">前一页</a>");
        } else {
            buf.append("<a class=\"before\" href=\"" + beforeUrl + "\"  style=\"float: left\">前一页</a>");
        }

        if (page.getPageNo() == (page.getTotalPage() - 1)) {
            buf.append("<a class=\"after\" href=\"#\"  style=\"color: #cccccc;float: right\">后一页</a>");
        } else {
            buf.append("<a class=\"after\" href=\"" + afterUrl + "\"  style=\"float: right\">后一页</a>");
        }
        joiner.add(buf.toString());

        Writer out = env.getOut();
        out.write(joiner.toString());
        out.close();
    }
}
