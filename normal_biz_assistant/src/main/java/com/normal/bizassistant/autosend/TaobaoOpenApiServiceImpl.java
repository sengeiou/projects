package com.normal.bizassistant.autosend;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
        join(joiner, "【原价】" + item.getOrigPrice());
        join(joiner, "【折扣价】" + item.getZkFinalPrice());
        join(joiner, "【券有效截止日期】" + item.getCouponEndTime());
        join(joiner, "【优惠券信息】" + item.getCouponInfo());
        join(joiner, "【下单口令】" + queryPwd(item.getCouponShareUrl()));
        join(joiner, "复制这条信息到淘宝即可购买  : )");

        return joiner.toString();

    }

    private String queryPwd(String url) {
        TbkTpwdCreateRequest req = new TbkTpwdCreateRequest();
        req.setUrl(url);
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
        InputStream inputStream = null;
        ByteArrayOutputStream output = null;
        SendGood good = new SendGood();
        try {
            good.setText(genText(item));
            URL url = new URL(item.getPictUrl());
            inputStream = url.openStream();
            output = new ByteArrayOutputStream();
            int n;
            byte[] buffer = new byte[1024];
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
            good.setImages(Arrays.asList(output.toByteArray()));
        } catch (Exception e) {
            logger.error("e: {}", e);

        } finally {
            if (inputStream != null && output != null) {
                try {
                    inputStream.close();
                    output.close();
                } catch (IOException e) {

                }
            }
        }
        return good;
    }
}