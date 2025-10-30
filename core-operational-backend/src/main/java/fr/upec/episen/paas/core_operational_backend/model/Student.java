package fr.upec.episen.paas.core_operational_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private Long id;
    private String firstname;
    private String lastname;
    private boolean shouldOpen;

    public String toStringWithoutShouldOpen() {
        return "Student{" +
                "id:" + id + "\n" +
                "firstname:" + firstname + "\n" +
                "lastname:" + lastname + "\n" +
                '}';
    }
}
