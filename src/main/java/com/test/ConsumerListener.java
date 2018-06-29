package com.test;

import com.rabbitmq.client.*;

import java.io.IOException;

public class ConsumerListener {

    public static void consume(final Channel channel, String Queue_NAME) throws IOException {
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "utf-8");
                System.out.println(" [x] Received "+envelope.getRoutingKey()+" '" + message + "'");
                try {
                    doWork(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    channel.basicAck(envelope.getDeliveryTag(), true);
                    System.out.println(" [x] Done");
                }
            }
        };
        channel.basicConsume(Queue_NAME, false, consumer);
    }

    private static void doWork(String task) throws InterruptedException {
        for (char ch : task.toCharArray()) {
            if (ch == '.') Thread.sleep(10000);
        }
    }
}
