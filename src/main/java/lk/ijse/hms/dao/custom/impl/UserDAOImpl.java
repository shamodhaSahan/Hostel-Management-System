package lk.ijse.hms.dao.custom.impl;

import lk.ijse.hms.dao.custom.UserDAO;
import lk.ijse.hms.entity.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/6/2023
 * Time :9:24 AM
 */

public class UserDAOImpl implements UserDAO {

    @Override
    public void save(User user, Session session) {
        session.save(user);
    }

    @Override
    public void update(User user, Session session) {
        session.update(user);
    }

    @Override
    public void deleteByPk(String pk, Session session) {
        session.delete(session.load(User.class, pk));
    }

    @Override
    public List<User> findAll(Session session) {
        return session.createQuery("FROM User").list();
    }

    @Override
    public Optional<User> findByPk(String pk, Session session) {
        try {
            return Optional.of(session.get(User.class, pk));
        }catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> getLastPk(Session session) {
        List<String> list = session.createQuery("select userId from User ORDER BY userId DESC").list();
        return list.size() > 0? Optional.of(list.get(0)) : Optional.empty();
    }

    @Override
    public Optional<User> findByUserName(String userName, Session session) {
        Query query = session.createQuery("FROM User WHERE userName= :u");
        query.setParameter("u", userName);
        List<User> list = query.list();
        return list.size() > 0? Optional.of(list.get(0)) : Optional.empty();
    }

    @Override
    public List<User> search(String text, Session session) {
        text = "%"+text+"%";
        Query query = session.createQuery("FROM User WHERE userId LIKE :text OR name LIKE :text OR contactNo LIKE :text OR email LIKE :text OR userName LIKE :text OR password LIKE :text");
        query.setParameter("text", text);
        return query.list();
    }
}
