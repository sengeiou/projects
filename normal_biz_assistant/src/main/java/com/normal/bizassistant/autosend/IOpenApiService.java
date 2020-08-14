package com.normal.bizassistant.autosend;

import java.util.Iterator;
import java.util.Map;

interface IOpenApiService {

    Iterator<SendGood> querySendGoods(Map<String, Object> params);
}