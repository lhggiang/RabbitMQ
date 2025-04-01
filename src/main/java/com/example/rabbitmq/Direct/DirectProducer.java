package com.example.rabbitmq.Direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class DirectProducer {
    private final static String ROUTING_KEY = "demo";
    private final static String QUEUE_NAME = "direct_queue";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // Khai báo exchange và queue là durable
            channel.exchangeDeclare("direct_exchange_v2", "direct", true, false, null);
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            // Bind queue với exchange
            channel.queueBind(QUEUE_NAME, "direct_exchange_v2", ROUTING_KEY);

            String message = "Hello RabbitMQ2 from Java!";

            channel.basicPublish("direct_exchange_v2", ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}