package com.normal.bizassistant.autosend;

import java.util.List;
import java.util.Map;

interface IOpenApiService {

    List<SendGood> querySendGoods(Map<String, Object> params);
}