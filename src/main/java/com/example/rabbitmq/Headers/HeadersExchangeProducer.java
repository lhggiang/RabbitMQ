package com.example.rabbitmq.Headers;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import java.util.HashMap;
import java.util.Map;

public class HeadersExchangeProducer {
    private final static String EXCHANGE_NAME = "headers_logs";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // Khai báo exchange kiểu HEADERS
            channel.exchangeDeclare(EXCHANGE_NAME, "headers");

            // Tạo header map { "format": "pdf", "type": "report" }
            Map<String, Object> headers = new HashMap<>();
            headers.put("format", "pdf");
            headers.put("type", "report");

            String message = "Thông báo ibáo cáo định dạng PDF (Headers Exchange)!";

            // Tạo thuộc tính message vớ header
            AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                    .headers(headers)
                    .build();

            // Gửi message
            channel.basicPublish(EXCHANGE_NAME, "", props, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent message with headers " + headers);
        }
    }
}
