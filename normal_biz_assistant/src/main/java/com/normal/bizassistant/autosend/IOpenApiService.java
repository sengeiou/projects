package com.normal.bizassistant.autosend;

import java.util.List;
import java.util.Map;

interface IOpenApiService {

    List<SendGoodResponse> querySendGoods(Map<String, String> param);
}