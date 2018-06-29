package com.test.direct;

import com.rabbitmq.client.Channel;
import com.test.ChannelFactory;
import com.test.ConsumerListener;

import java.io.IOException;

public class ReceiveLogsDirect {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException {

        if (args.length < 1) {
            System.err.println("Usage: ReceiveLogsDirect [info] [warning] [error]");
            System.exit(1);
        }

        Channel channel = ChannelFactory.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        String queue = channel.queueDeclare().getQueue();
        for (String arg : args) {
            String severity = arg;
            channel.queueBind(queue, EXCHANGE_NAME, severity);
        }

        System.out.println("[*] Waiting for messages. To exit press CTRL+C");

        ConsumerListener.consume(channel, queue);

    }
}
