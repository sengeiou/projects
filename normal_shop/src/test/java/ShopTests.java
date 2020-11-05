import com.pdd.pop.sdk.common.util.JsonUtil;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.pop.request.*;
import com.pdd.pop.sdk.http.api.pop.response.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: fei.he
 */
public class ShopTests {
    PopClient client;

    @Before
    public void init() {
        //pid =  11136444_175955262
        client = new PopHttpClient("66acc0f56ee545c48ff7748c10d4f871", "68cd9546b762345ad9dd5725a5f00edcf9912076");
    }

    @Test
    public void test() throws Exception {
        PddDdkGoodsPromotionUrlGenerateRequest request = new PddDdkGoodsPromotionUrlGenerateRequest();
//        request.setCustomParameters("str");
        request.setGenerateMallCollectCoupon(false);
        request.setGenerateQqApp(false);
        request.setGenerateSchemaUrl(true);
        request.setGenerateShortUrl(true);
        request.setGenerateWeappWebview(false);
        request.setGenerateWeiboappWebview(false);
        request.setGenerateWeApp(false);
        List<Long> goodsIdList = new ArrayList<Long>();
        goodsIdList.add(186672819012L);
        request.setGoodsIdList(goodsIdList);
        request.setMultiGroup(false);
        request.setPId("11136444_175955262");
        //request.setSearchId("str");
        //request.setZsDuoId(0L);
/*
        List<String> targetIdList = new ArrayList<String>();
        targetIdList.add("str");
        request.setTargetIdList(targetIdList);*/
        request.setGenerateAuthorityUrl(false);
        PddDdkGoodsPromotionUrlGenerateResponse response = client.syncInvoke(request);
        System.out.println(JsonUtil.transferToJson(response));
    }
}
