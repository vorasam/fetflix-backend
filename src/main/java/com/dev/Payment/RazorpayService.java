package com.dev.Payment;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    public JSONObject createOrder(int amount) throws Exception {

        RazorpayClient client = new RazorpayClient(keyId, keySecret);

        JSONObject options = new JSONObject();
        options.put("amount", amount * 100); // paise
        options.put("currency", "INR");
        options.put("receipt", "fetflix_plan");

        Order order = client.orders.create(options);
        return order.toJson();
    }

    public boolean verify(String orderId, String paymentId, String signature) {

        try {
            String payload = orderId + "|" + paymentId;
            String generated = Utils.getHash(payload, keySecret);
            return generated.equals(signature);
        } catch (Exception e) {
            return false;
        }
    }
}

