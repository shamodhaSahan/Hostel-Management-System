package lk.ijse.hms.bo.custom;

import lk.ijse.hms.bo.SuperBO;
import lk.ijse.hms.bo.exception.DuplicateException;
import lk.ijse.hms.bo.exception.InUseException;
import lk.ijse.hms.dto.CustomDTO;
import lk.ijse.hms.dto.RoomDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/6/2023
 * Time :1:59 PM
 */

public interface RoomBO extends SuperBO {
    List<RoomDTO> getAllRoom();

    List<RoomDTO> searchMembersByText(String text);

    List<CustomDTO> getAllUnPaidReservation();

    boolean payReservationByReservationId(String reservationId, CustomDTO.Status paid);

    String getNextReservationId();

    RoomDTO getRoomById(String roomTypeId);

    boolean addReservation(CustomDTO customDTO);

    List<CustomDTO> getAllReservation();

    List<CustomDTO> searchBookByText(String text);

    boolean saveRoom(RoomDTO roomDTO) throws DuplicateException;

    boolean updateBook(RoomDTO roomDTO);

    boolean deleteRoom(RoomDTO roomDTO) throws InUseException;

    long getAllReservationCountByDate(LocalDate date);
}
