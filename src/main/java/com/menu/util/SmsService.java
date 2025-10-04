//package com.menu.util;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import com.twilio.Twilio;
//import com.twilio.rest.api.v2010.account.Message;
//import com.twilio.type.PhoneNumber;
//import com.menu.order.OrderEntity;
//
//@Service
//public class SmsService {
//    @Value("${twilio.account.sid}")
//    private String accountSid;
//
//    @Value("${twilio.auth.token}")
//    private String authToken;
//
//    @Value("${twilio.phone.number}")
//    private String twilioPhoneNumber;
//
//    public SmsService(@Value("${twilio.account.sid}") String accountSid,
//                     @Value("${twilio.auth.token}") String authToken) {
//        Twilio.init(accountSid, authToken);
//    }
//
//    public void sendOrderConfirmation(OrderEntity order) {
//
//        StringBuilder messageBody = new StringBuilder();
//        messageBody.append("Your order has been placed successfully!\n");
//        messageBody.append("Order ID: ").append(order.getId()).append("\n");
//        messageBody.append("Restaurant: ").append(order.getRestaurant().getRestaurantName()).append("\n");
//        messageBody.append("\nOrdered Items:\n");
//
//        for (OrderEntity.OrderItem item : order.getItems()) {
//            messageBody.append("- ").append(item.getName())
//                      .append(" (Qty: ").append(item.getQuantity()).append(")\n");
//        }
//
//        messageBody.append("\nTotal Amount: ₹").append(order.getTotalPrice());
//        messageBody.append("\nTable Number: ").append(order.getTableNumber());
//      System.out.println( order.getCustomerPhoneNumber());
//        try {
//            System.out.println("Attempting to send SMS...");
//            System.out.println("To: +91" + order.getCustomerPhoneNumber());
//            System.out.println("From: " + twilioPhoneNumber);
//            System.out.println("Message: " + messageBody.toString());
//            
//            Message message = Message.creator(
//                new PhoneNumber("+91" + order.getCustomerPhoneNumber().trim()), // Add country code and trim any spaces
//                new PhoneNumber(twilioPhoneNumber),
//                messageBody.toString()
//            ).create();
//            
//            System.out.println("SMS sent successfully! SID: " + message.getSid());
//        } catch (Exception e) {
//            // Log the detailed error
//            System.err.println("Failed to send SMS: " + e.getMessage());
//            e.printStackTrace();
//            System.err.println("Account SID: " + accountSid);
//            System.err.println("Auth Token length: " + (authToken != null ? authToken.length() : 0));
//            System.err.println("Twilio Phone: " + twilioPhoneNumber);
//        }
//    }
//}