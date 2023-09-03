package lk.ijse.hms.dao.custom.impl;

import lk.ijse.hms.dao.custom.ReservationDAO;
import lk.ijse.hms.dto.CustomDTO;
import lk.ijse.hms.entity.Reservation;
import lk.ijse.hms.entity.Room;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/6/2023
 * Time :9:23 AM
 */

public class ReservationDAOImpl implements ReservationDAO {

    @Override
    public void save(Reservation reservation, Session session) {
        session.save(reservation);
    }

    @Override
    public void update(Reservation reservation, Session session) {
        session.update(reservation);

    }

    @Override
    public void deleteByPk(String pk, Session session) {
        session.delete(session.load(Reservation.class, pk));
    }

    @Override
    public List<Reservation> findAll(Session session) {
        return session.createQuery("FROM Reservation ").list();
    }

    @Override
    public Optional<Reservation> findByPk(String pk, Session session) {
        try {
            return Optional.of(session.get(Reservation.class, pk));
        }catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> getLastPk(Session session) {
        List<String> list = session.createQuery("select resId from Reservation ORDER BY resId DESC").list();
        return list.size() > 0? Optional.of(list.get(0)) : Optional.empty();
    }

    @Override
    public boolean updateStatusByPk(String reservationId, CustomDTO.Status status, Session session) {
        Query query = session.createQuery("UPDATE Reservation SET status = :status WHERE resId = :reservationId");
        query.setParameter("status", (status == CustomDTO.Status.PAID?"PAID":"UNPAID"));
        query.setParameter("reservationId", reservationId);
        return 0 < query.executeUpdate();
    }

    @Override
    public long findAllByDate(Date valueOf, Session session) {
        return session.createQuery("FROM Reservation WHERE date = :date").setParameter("date", valueOf).list().size();
    }
}
