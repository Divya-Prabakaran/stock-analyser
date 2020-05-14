package com.acme.mytrader.price;

import java.io.IOException;

public interface PriceListener {
    void priceUpdate(String security, double price) throws IOException;
}
