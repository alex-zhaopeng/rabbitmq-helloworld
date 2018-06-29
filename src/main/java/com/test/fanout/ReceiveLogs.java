package com.test.fanout;

import com.rabbitmq.client.Channel;
import com.test.ChannelFactory;
import com.test.ConsumerListener;

import java.io.IOException;

public class ReceiveLogs {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException {
        Channel channel = ChannelFactory.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        String queue = channel.queueDeclare().getQueue();

        channel.queueBind(queue, EXCHANGE_NAME, "");

        System.out.println("[*] Waiting for messages. To exit press CTRL+C");

        ConsumerListener.consume(channel,queue);

    }
}
