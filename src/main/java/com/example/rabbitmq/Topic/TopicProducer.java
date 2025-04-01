package com.example.rabbitmq.Topic;

import com.rabbitmq.client.*;

public class TopicProducer {
    private final static String EXCHANGE_NAME = "topic_logs";

    private static final String TASK_QUEUE_NAME = "topic_queue";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // Khai báo exchange kiểu TOPIC
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");

            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

            channel.queueBind(TASK_QUEUE_NAME, EXCHANGE_NAME, "kern.*");

            String routingKey = "kern.critical";
            String message = "Thông báo hệ thống (Topic Exchange 1)!";

            // Gửi message với routing key xác định
            channel.basicPublish(EXCHANGE_NAME, routingKey,
                    MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
        }
    }
}
