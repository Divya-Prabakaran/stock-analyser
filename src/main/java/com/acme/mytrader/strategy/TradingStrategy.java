package com.acme.mytrader.strategy;

import com.acme.mytrader.price.impl.PriceSourceImpl;

import java.util.Scanner;

/**
 * <pre>
 * User Story: As a trader I want to be able to monitor stock prices such
 * that when they breach a trigger level orders can be executed automatically
 * </pre>
 */
public class TradingStrategy extends PriceSourceImpl {

    public TradingStrategy(String security, double price, int volume) {
        super(security, price, volume);
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the security:");
        String security = input.nextLine();
        System.out.println("Enter the price:");
        double price =  input.nextDouble();
        System.out.println("Enter the volume:");
        int volume =  input.nextInt();

        PriceSourceImpl priceSource = new TradingStrategy(security, price, volume);
        priceSource.getStockQuotes();
    }
}
