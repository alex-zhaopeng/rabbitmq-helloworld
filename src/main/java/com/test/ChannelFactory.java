package com.test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

public class ChannelFactory {

    public static Channel createChannel() throws IOException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.121.157");
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("cango1qaz");
//        connectionFactory.setHost("10.211.55.11");
//        connectionFactory.setUsername("admin");
//        connectionFactory.setPassword("Passw0rd");
        connectionFactory.setRequestedHeartbeat(0);
        Connection connection = connectionFactory.newConnection();
//        connection.addShutdownListener(new ConsumerSelfShutdownListener(Queue_NAME));
        final com.rabbitmq.client.Channel channel = connection.createChannel();

        return channel;
    }

    public static Channel declareQueue(Channel channel, String Queue_NAME) throws IOException {
        channel.queueDeclare(Queue_NAME, false, false, false, null);
        channel.basicQos(1);
        //this tells RabbitMQ not to give more than one message to a worker at a time. Or, in other words,
        // don't dispatch a new message to a worker until it has processed and acknowledged the previous one.
        return channel;
    }
}
