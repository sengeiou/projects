package com.normal.portal.impl;

import com.normal.base.mybatis.Page;
import com.normal.model.PageParam;
import com.normal.base.mybatis.Pages;
import com.normal.base.web.CommonErrorMsg;
import com.normal.base.web.Result;
import com.normal.dao.post.PostMapper;
import com.normal.model.post.Post;
import com.normal.portal.PostProperties;
import com.youbenzi.mdtool.tool.MDTool;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Component
public class PostService {
    public static final Logger logger = LoggerFactory.getLogger(PostService.class);

    @Autowired
    PostProperties config;

    @Autowired
    Configuration configuration;

    @Autowired
    PostMapper postMapper;


    public Result addPost(MultipartFile post, HttpServletRequest request) {

        if (!checkToken(request)) {
            return Result.fail(CommonErrorMsg.AUTH_ERROR);
        }
        String html, fileName = post.getOriginalFilename().substring(0, post.getOriginalFilename().lastIndexOf("."));
        Post existedPost = postMapper.selectByFileName(fileName);
        if (existedPost != null) {
            postMapper.deleteByPrimaryKey(existedPost.getId());
        }
        FileWriter out = null;
        try {
            String preview = PostHelper.getPostPreview(post);
            BufferedReader reader = new BufferedReader(new InputStreamReader(post.getInputStream()));
            StringBuffer fileContext = new StringBuffer();
            for (; ; ) {
                String line = reader.readLine();
                if (line != null) {
                    fileContext.append(line + "\n");
                    continue;
                }
                break;
            }
            html = MDTool.markdown2Html(fileContext.toString());
            Template template = configuration.getTemplate("postDetail.ftlh");
            Map<String, Object> dataModel = new HashMap<>(1);
            dataModel.put("post", html);
            dataModel.put("title", fileName);
            File outputFile = createEmptyFile(fileName);
            out = new FileWriter(outputFile);
            template.process(dataModel, out);

            Post record = new Post();
            record.setPostTitle(fileName);
            record.setPostPreview(preview);
            postMapper.insertSelective(record);
        } catch (IOException e) {
            logger.error("e:{}", e.getMessage());
            return Result.fail(CommonErrorMsg.RUNTIME_ERROR);
        } catch (TemplateException e) {
            logger.error("freemarker  parse error.  e:{}");
            return Result.fail(CommonErrorMsg.RUNTIME_ERROR);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                //
                logger.error("file out put stream  close exception");
            }
        }
        return Result.success();
    }

    private File createEmptyFile(String fileName) throws IOException {
        Path staticFilePath = getStaticFilePath(fileName);
        if (Files.exists(staticFilePath)) {
            return staticFilePath.toFile();
        }
        Files.createFile(staticFilePath);
        return staticFilePath.toFile();
    }

    private Path getStaticFilePath(String fileName) {
        String staticFilePath = new StringBuffer(config.getUploadDir())
                .append(File.separator)
                .append(fileName)
                .append(".html")
                .toString();
        logger.info("upload dir: {}", staticFilePath);
        return new File(staticFilePath).toPath();
    }


    public Result deletePost(Integer id, HttpServletRequest request) {
        if (!checkToken(request)) {
            return Result.fail(CommonErrorMsg.AUTH_ERROR);
        }
        Post post = postMapper.selectByPrimaryKey(id);
        if (post == null) {
            return Result.fail(CommonErrorMsg.ILLEGE_ARG);
        }
        try {
            Files.deleteIfExists(getStaticFilePath(post.getPostTitle()));
        } catch (IOException e) {
            logger.error("delete static file error, e:{}", e);
            return Result.fail(CommonErrorMsg.RUNTIME_ERROR);
        }
        postMapper.deleteByPrimaryKey(id);
        return Result.success();
    }

    public Result listPost(PageParam param) {
        Page<?> rst = Pages.query(() -> postMapper.selectByPage(param));
        return Result.success(rst);
    }


    public boolean checkToken(HttpServletRequest request) {
        String token = request.getParameter("token");
        return token != null && PostProperties.token.equals(token);
    }

}

