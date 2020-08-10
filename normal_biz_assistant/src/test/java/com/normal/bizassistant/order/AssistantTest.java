package com.normal.bizassistant.order;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TbkTpwdCreateRequest;
import com.taobao.api.response.TbkTpwdCreateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * @author: fei.he
 */
public class AssistantTest {
    public static final Logger logger = LoggerFactory.getLogger(AssistantTest.class);


    @Test
    public void testTaoBao() throws Exception{
        TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "30963536", "8da2478ae2d25c96ebde05cf3f522e7f");
        TbkTpwdCreateRequest req = new TbkTpwdCreateRequest();
        req.setUserId("123");
        req.setText("长度大于5个字符");
        req.setUrl("https://uland.taobao.com/");
        req.setLogo("https://uland.taobao.com/");
        req.setExt("{}");
        TbkTpwdCreateResponse rsp = client.execute(req);
        System.out.println(rsp.getBody());
    }

}
