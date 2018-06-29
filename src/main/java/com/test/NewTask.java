package com.test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class NewTask {

    private final static String Queue_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Channel channel = getChannel();

        channel.queueDeclare(Queue_NAME, false, false, false, null);

        send(channel);
//        String message = getMessage(args);


    }

    public static void send(Channel channel) throws IOException, InterruptedException {
        int i=0;
        while (true){
            String message = "hello" + i + ".";
            channel.basicPublish("", "hello", null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
            i++;
            Thread.sleep(3000);
        }
    }

    public static Channel getChannel() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.121.157");
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("cango1qaz");
//        connectionFactory.setUsername("root");
//        connectionFactory.setPassword("cango1qaz");

        Connection connection = connectionFactory.newConnection();
        connection.addShutdownListener(new SelfShutdownListener());
        Channel channel = connection.createChannel();
        channel.queueDeclare(Queue_NAME, false, false, false, null);

        return channel;
    }

    private static String getMessage(String[] strings) {
        if (strings.length < 1)
            return "Hello World!";
        return joinStrings(strings, " ");
    }

    private static String joinStrings(String[] strings, String delimiter) {

        int length = strings.length;
        if (length == 0) return "";
        StringBuilder words = new StringBuilder(strings[0]);
        for (int i = 1; i < length; i++) {
            words.append(delimiter).append(strings[i]);
        }
        return words.toString();
    }
}
