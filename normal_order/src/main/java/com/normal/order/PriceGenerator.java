package com.normal.order;

public interface PriceGenerator {

    Double offset = Double.valueOf("0.01");

    Double gen(Double originalPrice);

}
