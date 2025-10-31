package paas.tp.common.backend.models;

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
                "id:" + id +
                ", firstname:" + firstname +
                ", lastname:" + lastname +
                '}';
    }
}
