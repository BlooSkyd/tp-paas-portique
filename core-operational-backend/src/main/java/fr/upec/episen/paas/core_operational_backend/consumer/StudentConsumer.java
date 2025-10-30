package fr.upec.episen.paas.core_operational_backend.consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.upec.episen.paas.core_operational_backend.model.Student;
import fr.upec.episen.paas.core_operational_backend.producer.StudentProducer;
import fr.upec.episen.paas.core_operational_backend.service.StudentService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentConsumer {
    private static final Logger logger = LogManager.getLogger(StudentConsumer.class);

    private static StudentService studentService;
    private static StudentProducer studentProducer;

    @KafkaListener(topics = "attemps-logs", groupId = "core-operational-backend")
    public void consumeStudentAttemptEvent(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        String id;
        try {
            id = objectMapper.readTree(message).get("id").asText();
        } catch (Exception e) {
            logger.error("Failed to parse message: " + message, e);
            return;
        }
        Student student = studentService.getStudentIfAllowed(Long.parseLong(id));
        if (student != null) {
            studentProducer.sendEntryAllowed(student);
            studentProducer.sendEntryLogs(student);
            logger.info("Student with ID " + id + " is allowed to enter.");
        } else {
            studentProducer.sendEntryLogs(student);
            logger.info("Student with ID " + id + " is not allowed to enter.");
        }
    }
}
