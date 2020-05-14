package com.acme.mytrader.price.impl;

import com.acme.mytrader.price.PriceListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PriceSourceImplTest extends TestCase {

    @InjectMocks
    PriceSourceImpl priceSource;

    @Mock
    PriceListenerImpl priceListener;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_call_priceListener_whenCallingGetStockQuotes() throws IOException {

        PriceListener listener = Mockito.mock(PriceListener.class);

        PriceSourceImpl priceSourceImpl = new PriceSourceImpl("9PMWBAXL4KD2QQ3S", 160, 500) {
            @Override
            public JsonElement getHttpResponse() {
                return getJsonEntrySet();
            }

            @Override
            public PriceListener getPriceListenerImpl(int volume, double price) {
                return listener;
            }
        };

        priceSourceImpl.getStockQuotes();

        verify(listener, atLeastOnce()).priceUpdate(Mockito.anyString(), Mockito.anyDouble());
    }
    
    public JsonElement getJsonEntrySet() {

        String apiResponse = "{\n" +
                "   \"Meta Data\":{\n" +
                "      \"1. Information\":\"Intraday (5min) open, high, low, close prices and volume\",\n" +
                "      \"2. Symbol\":\"IBM\",\n" +
                "      \"3. Last Refreshed\":\"2020-05-14 16:00:00\",\n" +
                "      \"4. Interval\":\"5min\",\n" +
                "      \"5. Output Size\":\"Compact\",\n" +
                "      \"6. Time Zone\":\"US/Eastern\"\n" +
                "   },\n" +
                "   \"Time Series (5min)\":{\n" +
                "      \"2020-05-14 16:00:00\":{\n" +
                "         \"1. open\":\"116.6900\",\n" +
                "         \"2. high\":\"117.0900\",\n" +
                "         \"3. low\":\"116.6000\",\n" +
                "         \"4. close\":\"200.9600\",\n" +
                "         \"5. volume\":\"159883\"\n" +
                "      },\n" +
                "      \"2020-05-14 15:55:00\":{\n" +
                "         \"1. open\":\"116.5400\",\n" +
                "         \"2. high\":\"116.7900\",\n" +
                "         \"3. low\":\"116.5000\",\n" +
                "         \"4. close\":\"150.6800\",\n" +
                "         \"5. volume\":\"80698\"\n" +
                "      }\n" +
                "   }\n" +
                "}\n";

        JsonElement convertedObject = new Gson().fromJson(apiResponse, JsonObject.class).get("Time Series (5min)");

        return convertedObject;
    }

}