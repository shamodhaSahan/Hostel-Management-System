package lk.ijse.hms.dao.custom;

import lk.ijse.hms.dao.CrudDAO;
import lk.ijse.hms.entity.User;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/6/2023
 * Time :9:24 AM
 */

public interface UserDAO extends CrudDAO<User, String> {
    Optional<User> findByUserName(String userName, Session session);

    List<User> search(String text, Session session);
}
