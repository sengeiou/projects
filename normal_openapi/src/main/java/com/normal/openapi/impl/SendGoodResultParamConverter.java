package com.normal.openapi.impl;

import com.normal.base.utils.Files;
import com.normal.model.autosend.SendGood;
import com.taobao.api.request.TbkDgOptimusMaterialRequest;
import com.taobao.api.request.TbkTpwdCreateRequest;
import com.taobao.api.response.TbkDgOptimusMaterialResponse;
import com.taobao.api.response.TbkTpwdCreateResponse;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: fei.he
 */
public class SendGoodResultParamConverter implements ParamConvertor<Map<String, Object>, TbkDgOptimusMaterialRequest, TbkDgOptimusMaterialResponse, List<SendGood>> {

    private Environment environment;

    TaobaoClientWrapper clientWrapper;

    MaterialQueryParamConverter paramConvertor;

    volatile Map<String, Object> myReqParam = new HashMap<>(1);

    public SendGoodResultParamConverter(Environment environment, TaobaoClientWrapper clientWrapper, MaterialQueryParamConverter paramConvertor) {
        this.environment = environment;
        this.clientWrapper = clientWrapper;
        this.paramConvertor = paramConvertor;
    }


    @Override
    public TbkDgOptimusMaterialRequest toOpenReq(Map<String, Object> myReqParam) {
        this.myReqParam = myReqParam;
        return paramConvertor.toOpenReq(myReqParam);
    }

    @Override
    public List<SendGood> toMyRes(TbkDgOptimusMaterialResponse openBackParam) {
        List<TbkDgOptimusMaterialResponse.MapData> items = openBackParam.getResultList();
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
                        good.setText(genText(item));
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
                rst.put("picPath", environment.getProperty("openapi.images.path") + picName);
                rst.put("picName", picName);
                return rst;
            }
        }
        return null;
    }


    private String genText(TbkDgOptimusMaterialResponse.MapData item) {
        String materialId = (String) this.myReqParam.get("materialId");
        IGoodsTextGenerator generator = GoodsTextGeneratorFactory.getTextGenerator(materialId, item, () -> queryPwd(item.getCouponShareUrl()));
        return generator.text();
    }

    private String queryPwd(String url) {
        TbkTpwdCreateRequest req = new TbkTpwdCreateRequest();
        url = url.replaceAll("\\\\", "");
        url = "https:" + url;
        req.setUrl(url);
        req.setText("优惠券领取");
        req.setUserId(environment.getProperty("openapi.taobao.userid"));
        TbkTpwdCreateResponse rsp = clientWrapper.execute(req);
        return rsp.getData().getModel();
    }
}
