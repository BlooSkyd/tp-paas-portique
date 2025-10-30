package fr.upec.episen.paas.core_operational_backend.service;

import java.time.LocalTime;
import java.time.ZoneId;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import paas.tp.common.backend.models.Student;

@Service
@RequiredArgsConstructor
public class StudentService {
    
    private final RedisTemplate<String, Object> redisTemplate;

    public Student getStudentIfAllowed(Long id) {
        LocalTime time = LocalTime.now(ZoneId.of("Europe/Paris"));
        if (time.isBefore(LocalTime.of(8,0)) || time.isAfter(LocalTime.of(21,0))) {
            //return null;
        }

        String key = "student:" + id;
        Object studentObj = redisTemplate.opsForValue().get(key);
        if (studentObj instanceof Student && ((Student)studentObj).isShouldOpen()) {
            return (Student) studentObj;
        }
        return null;
    }
}