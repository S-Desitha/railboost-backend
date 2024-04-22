package org.ucsc.railboostbackend.services;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;



public class HashCodeGenerator {

    public static String generatecode(String orderId,double totalAmount ,String merchantSecretId,String merchantId,String amountcurrency){

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