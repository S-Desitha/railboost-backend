package org.ucsc.railboostbackend.services;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class HashCodeGenerator extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get form data
        String orderId = request.getParameter("order_id");
        double totalAmount = Double.parseDouble(request.getParameter("amount"));
        String merchantSecretId = "MTc4OTMzMjY2MTE1MTIxODQ1MTQyNjAxMjc4NzQzNjA2OTg1OTMw"; // Replace with your actual merchant secret ID
        String merchantId = "1226549"; // Replace with your actual merchant ID
        String amountCurrency = request.getParameter("currency");

        // Generate hash
        String hash = generatecode(orderId, totalAmount, merchantSecretId, merchantId, amountCurrency);

        // Set response content type
        response.setContentType("text/plain");

        // Write hash value to response
        response.getWriter().write(hash);
    }

    public static String generatecode(String orderId, double totalAmount, String merchantSecretId, String merchantId,
                                      String amountcurrency) {

        String merahantID = merchantId;
        String merchantSecret = merchantSecretId;
        String orderID = orderId;
        double amount = totalAmount;
        String currency = amountcurrency;
        DecimalFormat df = new DecimalFormat("0.00");
        String amountFormatted = df.format(amount);
        String hash = getMd5(merahantID + orderID + amountFormatted + currency + getMd5(merchantSecret));
        System.out.println("Generated Hash: " + hash);
        return hash;
    }

    private static String getMd5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext.toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
