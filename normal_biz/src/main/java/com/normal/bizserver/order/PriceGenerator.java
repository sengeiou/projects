package com.normal.bizserver.order;

import java.math.BigDecimal;

public interface PriceGenerator {

    Double offset = Double.valueOf("0.01");

    Double gen(Double originalPrice);

}
