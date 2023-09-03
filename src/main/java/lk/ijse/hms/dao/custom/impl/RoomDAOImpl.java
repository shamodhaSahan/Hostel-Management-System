package lk.ijse.hms.dao.custom.impl;

import lk.ijse.hms.dao.custom.RoomDAO;
import lk.ijse.hms.entity.Room;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/6/2023
 * Time :9:23 AM
 */

public class RoomDAOImpl implements RoomDAO {

    @Override
    public void save(Room room, Session session) {
        session.save(room);
    }

    @Override
    public void update(Room room, Session session) {
        session.update(room);
    }

    @Override
    public void deleteByPk(String pk, Session session) {
        session.delete(session.load(Room.class, pk));
    }

    @Override
    public List<Room> findAll(Session session) {
        return session.createQuery("FROM Room ").list();
    }

    @Override
    public Optional<Room> findByPk(String pk, Session session) {
        try {
            return Optional.of(session.get(Room.class, pk));
        }catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> getLastPk(Session session) {
        List<String> list = session.createQuery("select roomTypeId from Room ORDER BY roomTypeId DESC").list();
        return list.size() > 0? Optional.of(list.get(0)) : Optional.empty();
    }

    @Override
    public List<Room> search(String text, Session session) {
        text = "%"+text+"%";
        Query query = session.createQuery("FROM Room WHERE roomTypeId LIKE :text OR type LIKE :text ");
        query.setParameter("text", text);
        return query.list();
    }
}
