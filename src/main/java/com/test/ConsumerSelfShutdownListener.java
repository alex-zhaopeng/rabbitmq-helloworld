package com.test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerSelfShutdownListener implements ShutdownListener {

    public String Queue_name;

    public ConsumerSelfShutdownListener() {
    }

    public ConsumerSelfShutdownListener(String queue_name) {
        Queue_name = queue_name;
    }

    private static final Log log = LogFactory.getLog(SelfShutdownListener.class);

    public void shutdownCompleted(ShutdownSignalException cause) {
        if (cause.isInitiatedByApplication()) {
            if (log.isInfoEnabled()) {
                log.info(String.format("Shutdown by application completed for reference [%s] - reason [%s]"
                        , cause.getReference(), cause.getReason()));
            }

        } else if (cause.isHardError()) {
            log.error(String.format("Hard error shutdown completed for reference [%s] - reason [%s]"
                    , cause.getReference(), cause.getReason()));
            try {
                Channel channel = ChannelFactory.createChannel();
                ConsumerListener.consume(channel,Queue_name);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (log.isInfoEnabled()) {
            log.info("Shutdown completed");
        }
    }
}
