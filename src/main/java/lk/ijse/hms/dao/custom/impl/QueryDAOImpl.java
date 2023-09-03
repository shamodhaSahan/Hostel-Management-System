package lk.ijse.hms.dao.custom.impl;

import lk.ijse.hms.dao.custom.QueryDAO;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.Date;
import java.util.List;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/6/2023
 * Time :9:23 AM
 */

public class QueryDAOImpl implements QueryDAO {

    @Override
    public List<Object[]> findAllUnPaidReservationDetails(Session session) {
        Query query = session.createQuery("SELECT re.resId,s.studentId,s.name,ro.roomTypeId,ro.type,ro.keyMoney,re.status,re.date FROM Reservation re JOIN Student s ON re.student = s.studentId JOIN Room ro ON re.room =ro.roomTypeId WHERE re.status = :status");
        return query.setParameter("status", "UNPAID").list();
    }

    @Override
    public List<Object[]> findAllReservationDetails(Session session) {
        return session.createQuery("SELECT re.resId,s.studentId,s.name,ro.roomTypeId,ro.type,ro.keyMoney,re.status,re.date FROM Reservation re JOIN Student s ON re.student = s.studentId JOIN Room ro ON re.room =ro.roomTypeId").list();
    }

    @Override
    public List<Object[]> findAllReservationDetailsByText(String text, Session session) {
        text="%"+text+"%";
        Query query = session.createQuery("SELECT re.resId,s.studentId,s.name,ro.roomTypeId,ro.type,ro.keyMoney,re.status,re.date FROM Reservation re JOIN Student s ON re.student = s.studentId JOIN Room ro ON re.room =ro.roomTypeId WHERE re.resId LIKE :text OR s.studentId LIKE :text OR s.name LIKE :text OR ro.roomTypeId LIKE :text OR ro.type LIKE :text OR re.status LIKE :text");
        return query.setParameter("text", text).list();
    }

    @Override
    public List<Object[]> findUnPaidReservationByRoomTypeId(String roomTypeId, Session session) {
        Query query = session.createQuery("SELECT re.resId,s.studentId,s.name,ro.roomTypeId,ro.type,ro.keyMoney,re.status,re.date FROM Reservation re JOIN Student s ON re.student = s.studentId JOIN Room ro ON re.room =ro.roomTypeId WHERE ro.roomTypeId = :roomTypeId AND re.status = 'UNPAID'");
        return query.setParameter("roomTypeId", roomTypeId).list();
    }

    @Override
    public List<Object[]> findAllUnPaidReservationDetailsByStudentId(String studentId, Session session) {
        Query query = session.createQuery("SELECT re.resId,s.studentId,s.name,ro.roomTypeId,ro.type,ro.keyMoney,re.status,re.date FROM Reservation re JOIN Student s ON re.student = s.studentId JOIN Room ro ON re.room =ro.roomTypeId WHERE s.studentId = :studentId AND re.status = 'UNPAID'");
        return query.setParameter("studentId", studentId).list();
    }

    @Override
    public List<Object[]> findAllReservationDetailsByRoomTypeId(String roomTypeId, Session session) {
        Query query = session.createQuery("SELECT re.resId,s.studentId,s.name,ro.roomTypeId,ro.type,ro.keyMoney,re.status,re.date FROM Reservation re JOIN Student s ON re.student = s.studentId JOIN Room ro ON re.room =ro.roomTypeId WHERE ro.roomTypeId = :roomTypeId");
        return query.setParameter("roomTypeId", roomTypeId).list();
    }
}
