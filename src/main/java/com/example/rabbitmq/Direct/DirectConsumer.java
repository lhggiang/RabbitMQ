package com.example.rabbitmq.Direct;

import com.rabbitmq.client.*;

public class DirectConsumer {
    private final static String QUEUE_NAME = "direct_queue";
    private final static String ROUTING_KEY = "demo";
    private final static String EXCHANGE_NAME = "direct_exchange_v2";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // Khai báo exchange và queue
            channel.exchangeDeclare("EXCHANGE_NAME", "direct", true, false, null);
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.queueBind(QUEUE_NAME, "EXCHANGE_NAME", ROUTING_KEY);

            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            // Tắt automatic acknowledgment
            boolean autoAck = false;

            // Định nghĩa một consumer để xử lý tin nhắn nhận
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            };
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}