package com.test.direct;

import com.rabbitmq.client.Channel;
import com.test.ChannelFactory;

import java.io.IOException;

public class EmitLogDirect {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, InterruptedException {
        Channel channel = ChannelFactory.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        send(channel);
    }

    public static void send(Channel channel) throws IOException, InterruptedException {
        String severity = "";
        int i = 1;
        while (true) {
            if (i % 2 == 0) {
                severity = "error";
            } else if (i % 3 == 0) {
                severity = "info";
            } else
                severity = "warn";

            String message = "hello" + i;
            channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
            System.out.println(" [x] Sent " + severity + " '" + message + "'");
            i++;
            Thread.sleep(2000);
        }
    }
}
