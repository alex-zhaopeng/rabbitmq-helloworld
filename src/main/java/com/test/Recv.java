package com.test;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Recv {

    private final static String Queue_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("10.211.55.11");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("Passw0rd");
        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(Queue_NAME,false,false,false,null);
        channel.queueDeclare("hello1",false,false,false,null);
        channel.queueDeclare("hello2",false,false,false,null);

        System.out.println("[*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body,"utf-8");
                System.out.println("[x] Received '" + message + "'");
            }
        };
        channel.basicConsume(Queue_NAME,consumer);
    }
}
