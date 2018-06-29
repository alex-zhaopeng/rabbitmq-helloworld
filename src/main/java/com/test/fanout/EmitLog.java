package com.test.fanout;

import com.rabbitmq.client.Channel;
import com.test.ChannelFactory;

import java.io.IOException;

public class EmitLog {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, InterruptedException {
        Channel channel = ChannelFactory.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        send(channel);
    }

    public static void send(Channel channel) throws IOException, InterruptedException {
        int i=0;
        while (true){
            String message = "hello" + i;
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
            i++;
            Thread.sleep(2000);
        }
    }
}
