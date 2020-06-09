package com.normal.portal;

import com.normal.core.mybatis.PageParam;
import com.normal.core.web.CommonErrorMsg;
import com.normal.core.web.Result;
import com.youbenzi.mdtool.tool.MDTool;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Component
public class PostService {
    public static final Logger logger = LoggerFactory.getLogger(PostService.class);

    @Autowired
    PostConfig config;

    @Autowired
    Configuration configuration;

    Result addPost(MultipartFile post) {
        try {
            String html = MDTool.markdown2Html(post.getResource().getFile());
            Template template = configuration.getTemplate("");
            Map<String, Object> dataModel = new HashMap<>(1);
            dataModel.put("post", html);

            String filePath = createEmptyFile(post.getName());

            template.process(dataModel, new FileWriter(filePath));
        } catch (IOException e) {
            logger.error("e:{}", e);
            return Result.fail(CommonErrorMsg.RUNTIME_ERROR);
        } catch (TemplateException e) {
            logger.error("freemarker  parse error.  e:{}");
            return Result.fail(CommonErrorMsg.RUNTIME_ERROR);
        }
        return null;
    }

    private String createEmptyFile(String fileName) {
        Files.createFile()
    }

    Result deletePost(String id) {
        return null;
    }

    Result listPost(PageParam param) {
        return null;
    }

}

