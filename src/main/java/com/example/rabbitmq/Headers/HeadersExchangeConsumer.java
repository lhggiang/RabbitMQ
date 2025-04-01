package com.example.rabbitmq.Headers;

import com.rabbitmq.client.*;
import java.util.HashMap;
import java.util.Map;

public class HeadersExchangeConsumer {
    private final static String EXCHANGE_NAME = "headers_logs";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // Khai báo exchange kiểu HEADERS
        channel.exchangeDeclare(EXCHANGE_NAME, "headers");

        // Tạo queue tạm thời
        String queueName = channel.queueDeclare().getQueue();

        // Tạo map binding cho header, ví dụ: yêu cầu các header phải khớp chính xác ("x-match" là "all")
        Map<String, Object> bindingHeaders = new HashMap<>();
        bindingHeaders.put("format", "pdf");
        bindingHeaders.put("type", "report");
        bindingHeaders.put("x-match", "all"); // yêu cầu tất cả các header phải match

        // Bind queue với exchange sử dụng headers
        channel.queueBind(queueName, EXCHANGE_NAME, "", bindingHeaders);

        System.out.println(" [*] Đang chờ message với binding headers " + bindingHeaders);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Nhận được: '" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}
