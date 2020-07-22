package com.normal.biz.order;


import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

public class TestPriceGenerator {
    public static final Logger logger = LoggerFactory.getLogger(TestPriceGenerator.class);

    @Test
    public void test() throws Exception {
        SimplePriceGenerator generator = new SimplePriceGenerator();
        BizProperties mockProperties = Mockito.mock(BizProperties.class);
        Mockito.when(mockProperties.getPriceValidMin()).thenReturn(1);
        Mockito.when(mockProperties.getPriceChgOffset()).thenReturn(0.01d);
        Field bizProperties = generator.getClass().getDeclaredField("bizProperties");
        bizProperties.setAccessible(true);
        bizProperties.set(generator, mockProperties);

        Double rst = generator.gen(2.3d);

        Assert.assertTrue(rst.equals(new Double(1.9d)));

        Double rst2 = generator.gen(2.3d);
        logger.info("rst2: {}", rst2);
        Assert.assertTrue(rst2.equals(new Double(1.89d)));

        Thread.sleep(TimeUnit.MINUTES.toMillis(2));
        Double rst3 = generator.gen(2.3d);
        Assert.assertTrue(rst3.equals(new Double(1.9d)));

    }
}
