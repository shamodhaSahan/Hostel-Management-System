package lk.ijse.hms.view.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/6/2023
 * Time :10:35 PM
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReservationDetailTM {
    String reservationId;
    String studentId;
    String name;
    String RoomTypeId;
    String type;
    Double keyMoney;
    String status;
    Date date;
}
