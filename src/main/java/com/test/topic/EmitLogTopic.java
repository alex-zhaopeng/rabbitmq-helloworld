package com.test.topic;

import com.rabbitmq.client.Channel;
import com.test.ChannelFactory;

import java.io.IOException;

public class EmitLogTopic {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, InterruptedException {
        Channel channel = ChannelFactory.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        send(channel);
    }

    public static void send(Channel channel) throws IOException, InterruptedException {
        String severity = "";
        int i = 1;
        while (true) {
            if (i % 2 == 0) {
                severity = "kern";
            } else if (i % 3 == 0) {
                severity = ".critical";
            } else
                severity = "kern.critical";

            String message = "hello" + i;
            channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
            System.out.println(" [x] Sent " + severity + " '" + message + "'");
            i++;
            Thread.sleep(2000);
        }
    }
}
