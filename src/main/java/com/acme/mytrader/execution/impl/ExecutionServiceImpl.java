package com.acme.mytrader.execution.impl;

import com.acme.mytrader.execution.ExecutionService;

import java.io.PrintStream;

public class ExecutionServiceImpl implements ExecutionService {

    PrintStream out = System.out;

    @Override
    public void buy(String security, double price, int volume) {
        System.out.println("Bought the stock of volume " + volume + " for price " + price);
    }

    @Override
    public void sell(String security, double price, int volume) {
        System.out.println("Sold the stock of volume " + volume + " for price " + price);
    }
}
