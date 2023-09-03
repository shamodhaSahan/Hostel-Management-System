package lk.ijse.hms.bo.util;

import lk.ijse.hms.dto.CustomDTO;
import lk.ijse.hms.dto.RoomDTO;
import lk.ijse.hms.dto.StudentDTO;
import lk.ijse.hms.dto.UserDTO;
import lk.ijse.hms.entity.Room;
import lk.ijse.hms.entity.Student;
import lk.ijse.hms.entity.User;

import java.sql.Date;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/6/2023
 * Time :1:53 PM
 */

public class Convertor {
    public UserDTO fromUser(User user) {
        return new UserDTO(user.getUserId(), user.getName(), user.getContactNo(), user.getEmail(), user.getUserName(), user.getPassword());
    }

    public RoomDTO fromRoom(Room room) {
        return new RoomDTO(room.getRoomTypeId(), room.getType(), room.getKeyMoney(), room.getQty());
    }

    public StudentDTO fromStudent(Student student) {
        return new StudentDTO(student.getStudentId(), student.getName(), student.getAddress(), student.getContactNo(), student.getDob(), student.getGender());
    }

    public CustomDTO fromCustom(Object[] custom) {
        return new CustomDTO((String) custom[0], (String) custom[1], (String) custom[2], (String) custom[3], (String) custom[4], (Double) custom[5], custom[6] == "PAID"? CustomDTO.Status.PAID: CustomDTO.Status.UNPAID, (Date) custom[7]);
    }

    public Room toRoom(RoomDTO roomDTO) {
        return new Room(roomDTO.getRoomTypeId(), roomDTO.getType(), roomDTO.getKeyMoney(), roomDTO.getQty(), null);
    }

    public Student toStudent(StudentDTO studentDTO) {
        return new Student(studentDTO.getStudentId(), studentDTO.getName(), studentDTO.getAddress(), studentDTO.getContactNo(), studentDTO.getDob(), studentDTO.getGender(), null);
    }

    public User toUser(UserDTO userDTO) {
        return new User(userDTO.getUserId(), userDTO.getName(), userDTO.getTelNo(), userDTO.getEmail(), userDTO.getUserName(), userDTO.getPassword());
    }
}
