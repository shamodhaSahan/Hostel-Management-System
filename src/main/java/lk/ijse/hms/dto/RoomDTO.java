package lk.ijse.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/6/2023
 * Time :9:22 AM
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    private String roomTypeId;
    private String type;
    private double keyMoney;
    private int qty;
}
