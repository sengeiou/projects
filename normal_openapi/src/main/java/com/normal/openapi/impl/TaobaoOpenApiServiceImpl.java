package com.normal.openapi.impl;

import com.normal.base.utils.Files;
import com.normal.model.autosend.SendGood;
import com.normal.openapi.IOpenApiService;
import com.taobao.api.request.TbkDgOptimusMaterialRequest;
import com.taobao.api.request.TbkTpwdCreateRequest;
import com.taobao.api.response.TbkDgOptimusMaterialResponse;
import com.taobao.api.response.TbkTpwdCreateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Qualifier("taobaoOpenApiService")
public class TaobaoOpenApiServiceImpl implements IOpenApiService {
    public static final Logger logger = LoggerFactory.getLogger(TaobaoOpenApiServiceImpl.class);

    @Autowired
    TaobaoClientWrapper clientWrapper;

    @Autowired
    Environment environment;

    @Override
    public List<SendGood> querySendGoods(Map<String, Object> params) {

        TbkDgOptimusMaterialRequest req = new TbkDgOptimusMaterialRequest();
        req.setAdzoneId(Long.valueOf(environment.getProperty("autosend.taobao.adzoneid")));
        req.setMaterialId(Long.valueOf(String.valueOf(params.get("materialId"))));
        req.setPageNo(Long.valueOf(String.valueOf(params.get("pageNo"))));
        req.setPageSize(Long.valueOf(String.valueOf(params.get("pageSize"))));
        TbkDgOptimusMaterialResponse rsp = clientWrapper.execute(req);
        List<TbkDgOptimusMaterialResponse.MapData> resultList = rsp.getResultList();

        List<SendGood> goods = convert(resultList, req);
        return goods;
    }


    private String genText(TbkDgOptimusMaterialResponse.MapData item, TbkDgOptimusMaterialRequest req) {
        Long materialId = req.getMaterialId();
        IGoodsTextGenerator generator = GoodsTextGeneratorFactory.getTextGenerator(materialId, item, () -> queryPwd(item.getCouponShareUrl()));
        return generator.text();
    }

    private String queryPwd(String url) {
        TbkTpwdCreateRequest req = new TbkTpwdCreateRequest();
        url = url.replaceAll("\\\\", "");
        url = "https:" + url;
        req.setUrl(url);
        req.setText("优惠券领取");
        req.setUserId(environment.getProperty("autosend.taobao.userid"));
        TbkTpwdCreateResponse rsp = clientWrapper.execute(req);
        return rsp.getData().getModel();
    }


    private List<SendGood> convert(List<TbkDgOptimusMaterialResponse.MapData> items, TbkDgOptimusMaterialRequest req) {
        return items.stream()
                .map((item) -> {
                    //save image
                    try {
                        URL url = new URL("http:" + item.getPictUrl());
                        Map<String, String> imageRst = getGoodsPicPath(item.getPictUrl(), item.getCategoryId());
                        String goodsPicPath = imageRst.get("picPath");
                        File file = new File(goodsPicPath);
                        if (!file.exists()) {
                            Files.download(url, goodsPicPath);
                        }
                        SendGood good = new SendGood();
                        good.setCategoryId(item.getCategoryId());
                        good.setText(genText(item, req));
                        good.setImagePaths(Arrays.asList(imageRst.get("picPath")));
                        return good;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    private Map<String, String> getGoodsPicPath(String pictUrl, Long categoryId) {
        Map<String, String> rst = new HashMap<>(2);
        for (int j = pictUrl.length() - 1; j > 0; j--) {
            if (pictUrl.charAt(j) == '.') {
                String picName = categoryId + pictUrl.substring(j);
                rst.put("picPath", environment.getProperty("autosend.images.path") + picName);
                rst.put("picName", picName);
                return rst;
            }
        }
        return null;
    }
}