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
        PddDdkCmsPromUrlGenerateRequest request = new PddDdkCmsPromUrlGenerateRequest();
        request.setChannelType(2);
        request.setGenerateMobile(false);
        request.setGenerateSchemaUrl(false);
        request.setGenerateShortUrl(true);
        request.setGenerateWeappWebview(false);
        request.setMultiGroup(false);
        List<String> pIdList = new ArrayList<String>();
        pIdList.add("11136444_175955262");
        request.setPIdList(pIdList);
        request.setGenerateWeApp(false);
        PddDdkCmsPromUrlGenerateResponse response = client.syncInvoke(request);
        System.out.println(JsonUtil.transferToJson(response));

       /* PddGoodsCatsGetRequest request = new PddGoodsCatsGetRequest();
        request.setParentCatId(0L);
        PddGoodsCatsGetResponse response = client.syncInvoke(request);
        System.out.println(JsonUtil.transferToJson(response));*/
    }
}
