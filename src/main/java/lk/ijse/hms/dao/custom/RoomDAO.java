package lk.ijse.hms.dao.custom;

import lk.ijse.hms.dao.CrudDAO;
import lk.ijse.hms.entity.Room;
import org.hibernate.Session;

import java.util.Arrays;
import java.util.List;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/6/2023
 * Time :9:24 AM
 */

public interface RoomDAO extends CrudDAO<Room, String> {
    List<Room> search(String text, Session session);
}
