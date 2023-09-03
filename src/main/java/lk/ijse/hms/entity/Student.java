package lk.ijse.hms.entity;

import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/6/2023
 * Time :12:06 AM
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
public class Student implements SuperEntity{
    @Id
    @Column(name = "student_id")
    private String studentId;
    private String name;
    private String address;
    @Column(name = "contact_no")
    private String contactNo;
    private LocalDate dob;
    private String gender;
    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();
}
