package com.normal.openapi;

import com.normal.model.autosend.SendGood;

import java.util.List;
import java.util.Map;

public interface IOpenApiService {

    List<SendGood> querySendGoods(Map<String, Object> params);

}