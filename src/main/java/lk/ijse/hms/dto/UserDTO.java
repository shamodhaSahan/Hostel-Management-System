package lk.ijse.hms.dto;

import lk.ijse.hms.view.tm.UserTM;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/6/2023
 * Time :9:22 AM
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String userId;
    private String name;
    private String telNo;
    private String email;
    private String userName;
    private String password;
    public UserTM toUserDTO(){
        return new UserTM(this.userId, this.name, this.telNo, this.email, this.userName, this.password);
    }
}
