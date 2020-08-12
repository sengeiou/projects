package com.normal.bizassistant.autosend;

import com.taobao.api.TaobaoResponse;

import java.util.List;

public class TaobaoSendGoodResponse extends TaobaoResponse implements SendGoodResponse {


    @Override
    public String text() {
        return null;
    }

    @Override
    public List<Byte[]> images() {
        return null;
    }
}
