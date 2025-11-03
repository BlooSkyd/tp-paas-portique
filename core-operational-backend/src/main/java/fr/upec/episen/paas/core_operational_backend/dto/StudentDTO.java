package fr.upec.episen.paas.core_operational_backend.dto;

import java.sql.Timestamp;

import fr.upec.episen.paas.core_operational_backend.models.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Student student;
    private Timestamp timestamp;
    private String className;
    private boolean isAllowed;
    private String status;
    private String service = "core-operational-backend";

    public StudentDTO(Student student, Timestamp timestamp, String className, boolean isAllowed, String status) {
        this.student = student;
        this.timestamp = timestamp;
        this.className = className;
        this.isAllowed = isAllowed;
        this.status = status;
    }
}
