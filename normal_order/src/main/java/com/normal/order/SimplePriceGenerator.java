package com.normal.order;

import com.normal.communicate.server.BizProperties;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * @author fei.he
 */
public class SimplePriceGenerator implements PriceGenerator {

    private Map<PriceEnum, PriceWrapper> usedPrice = new ConcurrentHashMap<>(8);

    @Autowired
    private BizProperties bizProperties;

    enum PriceEnum {
        TWO_NINE(1.9d),
        NINE_NINE(9.9d),
        TEN_NINE_NINE(19.9d),
        NINE_NINE_NINE(99.9d),
        ;
        private Double value;

        PriceEnum(Double value) {
            this.value = value;
        }
    }

    /**
     * 添加时间戳
     */
    static class PriceWrapper {
        private double price;
        private long currMillis;

        public PriceWrapper(double price) {
            this.price = price;
            this.currMillis = System.currentTimeMillis();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            PriceWrapper that = (PriceWrapper) o;
            return Double.compare(that.price, price) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(price);
        }
    }


    @Override
    public Double gen(Double originalPrice) {
        Optional<PriceEnum> matchedPrice = Stream.of(PriceEnum.values()).reduce((priceType, priceType2) -> {
            if (Math.abs(originalPrice - priceType.value) < Math.abs(originalPrice - priceType2.value)) {
                return priceType;
            } else {
                return priceType2;
            }
        });
        PriceEnum matchedPriceEnum = matchedPrice.get();
        PriceWrapper existedPrice = usedPrice.get(matchedPriceEnum);
        //规定范围以内价格重复,根据偏移量调整价格.
        if (existedPrice != null &&
                System.currentTimeMillis() - existedPrice.currMillis <= TimeUnit.MINUTES.toMillis(bizProperties.getPriceValidMin())) {
            double price = existedPrice.price - bizProperties.getPriceChgOffset();
            usedPrice.put(matchedPriceEnum, new PriceWrapper(price));
            return price;
        }

        usedPrice.put(matchedPriceEnum, new PriceWrapper(matchedPriceEnum.value));
        return matchedPriceEnum.value;
    }

}
