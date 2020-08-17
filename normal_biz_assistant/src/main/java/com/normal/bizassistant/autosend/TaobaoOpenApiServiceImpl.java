package com.normal.bizassistant.autosend;

import com.normal.core.utils.Dates;
import com.normal.core.utils.Files;
import com.taobao.api.request.TbkDgOptimusMaterialRequest;
import com.taobao.api.request.TbkTpwdCreateRequest;
import com.taobao.api.response.TbkDgOptimusMaterialResponse;
import com.taobao.api.response.TbkTpwdCreateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaobaoOpenApiServiceImpl implements IOpenApiService {
    public static final Logger logger = LoggerFactory.getLogger(TaobaoOpenApiServiceImpl.class);

    @Autowired
    TaobaoClientWrapper clientWrapper;

    @Autowired
    Environment environment;

    @Override
    public Iterator<SendGood> querySendGoods(Map<String, Object> params) {

        TbkDgOptimusMaterialRequest req = new TbkDgOptimusMaterialRequest();
        req.setAdzoneId(Long.valueOf(environment.getProperty("autosend.taobao.adzoneid")));
        req.setMaterialId(Long.valueOf(String.valueOf(params.get("materialId"))));
        req.setPageNo(Long.valueOf(String.valueOf(params.get("pageNo"))));
        req.setPageSize(100L);
        TbkDgOptimusMaterialResponse rsp = clientWrapper.execute(req);
        List<TbkDgOptimusMaterialResponse.MapData> resultList = rsp.getResultList();

        List<SendGood> goods = resultList.stream()
                .map(this::map).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(goods)) {
            return null;
        }
        return goods.iterator();
    }


    private String genText(TbkDgOptimusMaterialResponse.MapData item) {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("【商品名称】" + item.getCategoryName());
        join(joiner, item.getItemDescription());
        join(joiner, "【原价】" + item.getCouponStartFee());
        join(joiner, "【优惠减免】" + item.getCouponAmount());
        join(joiner, "【优惠券信息】" + item.getCouponInfo());
        join(joiner, "【券有效截止日期】" + Dates.format(Long.valueOf(item.getCouponEndTime())));
        join(joiner, "【下单口令】" + queryPwd(item.getCouponShareUrl()));
        join(joiner, "复制这条信息到淘宝即可购买 :)");

        return joiner.toString();

    }

    private String queryPwd(String url) {
        TbkTpwdCreateRequest req = new TbkTpwdCreateRequest();
        url = url.replaceAll("\\\\", "");
        url = "https:" + url;
        req.setUrl(url);
        req.setText("领券");
        req.setUserId(environment.getProperty("autosend.taobao.userid"));
        TbkTpwdCreateResponse rsp = clientWrapper.execute(req);

        return rsp.getData().getModel();
    }


    private StringJoiner join(StringJoiner joiner, String subStr) {
        if (!StringUtils.isEmpty(subStr)) {
            joiner.add(subStr);
        }
        return joiner;
    }

    private SendGood map(TbkDgOptimusMaterialResponse.MapData item) {
        //save image
        try {
            URL url = new URL("http:" + item.getPictUrl());
            String goodsPicPath = getGoodsPicPath(item.getPictUrl(), item.getCategoryId());
            File file = new File(goodsPicPath);
            if (!file.exists()) {
                Files.download(url, goodsPicPath);
            }
            SendGood good = new SendGood();
            good.setText(genText(item));
            good.setImagePaths(Arrays.asList(goodsPicPath));
            return good;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getGoodsPicPath(String pictUrl, Long categoryId) {
        for (int j = pictUrl.length() - 1; j > 0; j--) {
            if (pictUrl.charAt(j) == '.') {
                return environment.getProperty("autosend.image.temp.path") + categoryId + pictUrl.substring(j);
            }
        }
        return null;
    }


}