package com.normal.base.web;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.stream.Stream;

public class PageTemplateModel implements TemplateDirectiveModel {
    public static final Logger logger = LoggerFactory.getLogger(PageTemplateModel.class);

    @Override
    public void execute(Environment env, Map map, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        Stream.of(loopVars).forEach((item) -> logger.info(item.getClass().getName()));
        Writer out = env.getOut();

        body.render(null);
    }


}
