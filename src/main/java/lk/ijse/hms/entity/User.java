package lk.ijse.hms.entity;

import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/5/2023
 * Time :11:51 PM
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
public class User implements SuperEntity{
    @Id
    String userId;
    String name;
    String contactNo;
    String email;
    String userName;
    String password;
}
