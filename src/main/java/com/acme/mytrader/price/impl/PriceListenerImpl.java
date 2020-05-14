package com.acme.mytrader.price.impl;

import com.acme.mytrader.execution.impl.ExecutionServiceImpl;
import com.acme.mytrader.price.PriceListener;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class PriceListenerImpl extends ExecutionServiceImpl implements PriceListener {

    private int enteredVolume;
    private double enteredPrice;

    @Override
    public void priceUpdate(String security, double currentStockPrice) {
        if(currentStockPrice < enteredPrice) {
            buy(security, currentStockPrice, enteredVolume);
        }else{
            sell(security, currentStockPrice, enteredVolume);
        }
    }
}
