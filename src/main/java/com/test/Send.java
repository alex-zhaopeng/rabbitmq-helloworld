package com.test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send {

    private final static String Queue_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("10.211.55.11");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("Passw0rd");
        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(Queue_NAME,false,false,false,null);
        String message = "hello world";

        channel.basicPublish("",Queue_NAME,null,message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");


    }

}
