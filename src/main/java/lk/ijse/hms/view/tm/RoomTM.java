package lk.ijse.hms.view.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/6/2023
 * Time :5:32 PM
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomTM {
    String roomTypeId;
    String type;
    Double keyMoney;
    Integer qty;
}
