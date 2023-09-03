package lk.ijse.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/6/2023
 * Time :9:22 AM
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomDTO {
    String reservationId;
    String studentId;
    String name;
    String RoomTypeId;
    String type;
    Double keyMoney;
    Status status;
    Date date;
    public enum Status{
        PAID,UNPAID
    }
}
