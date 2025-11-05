package fr.upec.episen.paas.core_operational_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.upec.episen.paas.core_operational_backend.models.Student;
import fr.upec.episen.paas.core_operational_backend.service.StudentService;
import lombok.RequiredArgsConstructor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/core_operational_backend/students")
@RequiredArgsConstructor
public class StudentController {

    @Autowired
    private final StudentService studentService;

    private static final Logger logger = LogManager.getLogger(StudentController.class);

    @GetMapping("/attempts/{id}")
    public Student openDoorForStudent(@PathVariable Long id) {
        Student student = studentService.getStudentIfAllowed(id);
        if (student != null) {
            logger.info("Student with ID " + id + " is allowed to enter.");
            return studentService.getStudentIfAllowed(id);
        } else {
            logger.info("Student with ID " + id + " is not allowed to enter.");
            return null;
        }
    }

}
