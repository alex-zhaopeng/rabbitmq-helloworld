package com.test;

import com.rabbitmq.client.Channel;

import java.io.IOException;

public class Worker {

    private final static String Queue_NAME = "hello";

    public static void main(String[] args) throws IOException {

        final Channel channel = ChannelFactory.createChannel();
        channel.getConnection().addShutdownListener(new ConsumerSelfShutdownListener(Queue_NAME));
        ChannelFactory.declareQueue(channel, Queue_NAME);
        System.out.println("[*] Waiting for messages. To exit press CTRL+C");
        ConsumerListener.consume(channel, Queue_NAME);

    }


}
