import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.hms.bo.BoFactory;
import lk.ijse.hms.bo.BoTypes;
import lk.ijse.hms.bo.custom.RoomBO;
import lk.ijse.hms.bo.custom.StudentBO;
import lk.ijse.hms.bo.custom.impl.RoomBOImpl;
import lk.ijse.hms.dao.DaoFactory;
import lk.ijse.hms.dao.DaoTypes;
import lk.ijse.hms.dao.SuperDAO;
import lk.ijse.hms.dao.custom.QueryDAO;
import lk.ijse.hms.dao.custom.UserDAO;
import lk.ijse.hms.dto.CustomDTO;
import lk.ijse.hms.dto.StudentDTO;
import lk.ijse.hms.dto.UserDTO;
import lk.ijse.hms.entity.Student;
import lk.ijse.hms.util.FactoryConfiguration;
import lk.ijse.hms.view.tm.UserTM;


import java.util.List;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/6/2023
 * Time :12:24 AM
 */

public class Tester {
    public static void main(String[] args) {
//        StudentBO studentBO = BoFactory.getInstance().getBO(BoTypes.STUDENT);
//        StudentDTO student = new StudentDTO();
//        student.setStudentId("S0001");
//        studentBO.deleteStudent(student);

        QueryDAO queryDAO = DaoFactory.getInstance().getDAO(DaoTypes.QUERY);
        queryDAO.findAllUnPaidReservationDetailsByStudentId("S0001", FactoryConfiguration.getInstance().getSession());
    }
}
