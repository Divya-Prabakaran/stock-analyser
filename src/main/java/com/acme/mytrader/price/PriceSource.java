package com.acme.mytrader.price;

import java.io.IOException;

public interface PriceSource {

    void addPriceListener(PriceListener listener) throws IOException;

    void removePriceListener(PriceListener listener) throws IOException;

}
