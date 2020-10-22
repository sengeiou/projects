package com.normal.openapi.impl.taobao;

import com.normal.model.autosend.SendGood;
import com.normal.model.openapi.TbOpenApiQueryParam;
import com.normal.openapi.impl.ClientWrapper;
import com.normal.openapi.impl.GoodsTextGeneratorFactory;
import com.normal.openapi.impl.IGoodsTextGenerator;
import com.normal.openapi.impl.ParamConverter;
import com.taobao.api.request.TbkDgOptimusMaterialRequest;
import com.taobao.api.response.TbkDgOptimusMaterialResponse;
import org.springframework.core.env.Environment;

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
public class TaoBaoSendGoodResultParamConverter implements ParamConverter<TbOpenApiQueryParam, TbkDgOptimusMaterialRequest, TbkDgOptimusMaterialResponse, List<SendGood>> {

    private Environment environment;

    ClientWrapper taoBaoClientWrapper;

    TaoBaoMaterialQueryParamConverter paramConverter;

    volatile Map<String, Object> myReqParam = new HashMap<>(1);

    public TaoBaoSendGoodResultParamConverter(Environment environment, ClientWrapper taoBaoClientWrapper, TaoBaoMaterialQueryParamConverter paramConverter) {
        this.environment = environment;
        this.taoBaoClientWrapper = taoBaoClientWrapper;
        this.paramConverter = paramConverter;
    }


    @Override
    public TbkDgOptimusMaterialRequest toOpenReq(TbOpenApiQueryParam myReqParam) {
        this.myReqParam = myReqParam;
        return paramConverter.toOpenReq(myReqParam);
    }

    @Override
    public List<SendGood> toMyRes(TbkDgOptimusMaterialResponse openBackParam, TbOpenApiQueryParam myReqParam) {
        List<TbkDgOptimusMaterialResponse.MapData> items = openBackParam.getResultList();
        return items.stream()
                .map((item) -> {
                    //save image
                    try {
                        URL url = new URL("http:" + item.getPictUrl());
                        Map<String, String> imageRst = TaobaoConvertFunctions.getGoodsPicPath(item.getPictUrl(), item.getCategoryId(), environment);
                        TaobaoConvertFunctions.downloadImage(url, imageRst.get("picPath"));

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


    private String genText(TbkDgOptimusMaterialResponse.MapData item) {
        String materialId = (String) this.myReqParam.get("materialId");
        IGoodsTextGenerator generator = GoodsTextGeneratorFactory.getTextGenerator(materialId, item, () -> taoBaoClientWrapper.queryTbPwd(item.getCouponShareUrl()));
        return generator.text();
    }


}
