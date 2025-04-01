package com.example.rabbitmq.Topic;

import com.rabbitmq.client.*;

public class TopicConsumer {
    private final static String EXCHANGE_NAME = "topic_logs";
    private static final String TASK_QUEUE_NAME = "topic_queue";

    private static final String ROUTING_KEY = "kern.critical";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // Khai báo exchange và queue
        channel.exchangeDeclare(EXCHANGE_NAME, "topic", false, false, null);
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

        channel.queueBind(TASK_QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String routingKey = delivery.getEnvelope().getRoutingKey();
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Nhận được '" + routingKey + "':'" + message + "'");
        };
        channel.basicConsume(TASK_QUEUE_NAME, true, deliverCallback, consumerTag -> {});
    }
}
