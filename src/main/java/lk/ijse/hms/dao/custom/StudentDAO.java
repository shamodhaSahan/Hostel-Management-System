package lk.ijse.hms.dao.custom;

import lk.ijse.hms.dao.CrudDAO;
import lk.ijse.hms.entity.Student;
import org.hibernate.Session;

import java.util.List;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/6/2023
 * Time :9:24 AM
 */

public interface StudentDAO extends CrudDAO<Student, String> {
    List<Student> search(String text, Session session);
}
