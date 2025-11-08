package paas.tp.telemetry.telemetry_to_messaging_backend;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaSender {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final Logger logger = LogManager.getLogger(KafkaSender.class);

    public void send(String message) {
        logger.info("Sending message: " + message);
        kafkaTemplate.send("attemps-logs", message);
        kafkaTemplate.send("logs", message);
    }
}
