package lk.ijse.hms.dao.custom.impl;

import lk.ijse.hms.dao.custom.StudentDAO;
import lk.ijse.hms.entity.Room;
import lk.ijse.hms.entity.Student;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/6/2023
 * Time :9:23 AM
 */

public class StudentDAOImpl implements StudentDAO {

    @Override
    public void save(Student student, Session session) {
        session.save(student);
    }

    @Override
    public void update(Student student, Session session) {
        session.update(student);
    }

    @Override
    public void deleteByPk(String pk, Session session) {
        session.delete(session.load(Student.class, pk));
    }

    @Override
    public List<Student> findAll(Session session) {
        return session.createQuery("FROM Student ").list();
    }

    @Override
    public Optional<Student> findByPk(String pk, Session session) {
        try {
            return Optional.of(session.get(Student.class, pk));
        }catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> getLastPk(Session session) {
        List<String> list = session.createQuery("select studentId from Student ORDER BY studentId DESC").list();
        return list.size() > 0? Optional.of(list.get(0)) : Optional.empty();
    }

    @Override
    public List<Student> search(String text, Session session) {
        text = "%"+text+"%";
        Query query = session.createQuery("FROM Student WHERE studentId LIKE :text OR name LIKE :text OR address LIKE :text OR contactNo LIKE :text OR gender like :text");
        query.setParameter("text", text);
        return query.list();
    }
}
