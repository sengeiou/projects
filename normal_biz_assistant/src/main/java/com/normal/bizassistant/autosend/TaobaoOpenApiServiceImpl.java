package com.normal.bizassistant.autosend;

import com.taobao.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TaobaoOpenApiServiceImpl implements IOpenApiService {

    @Autowired
    TaobaoClient taobaoClient;

    @Override
    public List<SendGoodResponse> querySendGoods(Map<String, String> param) {

    }
}
