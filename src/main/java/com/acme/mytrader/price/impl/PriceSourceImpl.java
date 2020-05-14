package com.acme.mytrader.price.impl;

import com.acme.mytrader.price.PriceListener;
import com.acme.mytrader.price.PriceSource;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.NoArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
public class PriceSourceImpl extends PriceListenerImpl implements PriceSource {

    private String security;
    private double price;
    private int volume;
    private double stockPrice;

    private final static String CLOSE = "4. close";
    private final static String VOLUME = "5. volume";

    public PriceSourceImpl(String security, double price, int volume) {
        this.security = security;
        this.price = price;
        this.volume = volume;
    }

    public void getStockQuotes(){

        JsonObject jsonObject = null;
        try
        {
            JsonElement apiResponse = getHttpResponse();

            jsonObject = apiResponse.getAsJsonObject();

            PriceListener priceListener = getPriceListenerImpl(volume, price);

            List<Map.Entry<String, JsonElement>> buyingProductsList = jsonObject.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().getAsJsonObject().get(CLOSE).getAsDouble() < price)
                    .filter(entry -> entry.getValue().getAsJsonObject().get(VOLUME).getAsInt() >= volume)
                    .sorted(Comparator.comparing(entry -> entry.getValue().getAsJsonObject().get(CLOSE).getAsDouble()))
                    .collect(Collectors.toList());

            List<Map.Entry<String, JsonElement>> sellingProductsList = jsonObject.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().getAsJsonObject().get(CLOSE).getAsDouble() >= price)
                    .sorted(Comparator.comparing(entry -> entry.getValue().getAsJsonObject().get(CLOSE).getAsDouble()))
                    .collect(Collectors.toList());

            if(buyingProductsList.size() > 0) {
                stockPrice = buyingProductsList.get(0).getValue().getAsJsonObject().get(CLOSE).getAsDouble();
                addPriceListener(priceListener);
            } else if(sellingProductsList.size() > 0) {
                stockPrice = sellingProductsList.get(sellingProductsList.size() - 1).getValue().getAsJsonObject().get(CLOSE).getAsDouble();
                removePriceListener(priceListener);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public PriceListener getPriceListenerImpl(int volume, double price) {
        PriceListener priceListener = new PriceListenerImpl(volume, price);
        return priceListener;
    }

    public JsonElement getHttpResponse() {

        HttpClient httpClient = HttpClients.createDefault();
        JsonObject convertedObject = null;
        try {
            HttpGet getRequest = new HttpGet("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=5min&apikey=" + security);
            getRequest.addHeader("accept", "application/json");
            HttpResponse response = httpClient.execute(getRequest);

            //verify the valid error code first
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new RuntimeException("Failed with HTTP error code : " + statusCode);
            }

            HttpEntity httpEntity = response.getEntity();

            convertedObject = new Gson().fromJson(EntityUtils.toString(httpEntity), JsonObject.class);

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return convertedObject.get("Time Series (5min)");
    }

    public JsonElement getApiResponseObject(String apiResponse) {
        JsonObject convertedObject = new Gson().fromJson(apiResponse, JsonObject.class);
        return convertedObject.get("Time Series (5min)");
    }

    @Override
    public void addPriceListener(PriceListener listener) throws IOException {
        listener.priceUpdate(security, stockPrice);
    }

    @Override
    public void removePriceListener(PriceListener listener) throws IOException {
        listener.priceUpdate(security, stockPrice);
    }
}
