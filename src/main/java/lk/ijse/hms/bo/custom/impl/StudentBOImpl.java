package lk.ijse.hms.bo.custom.impl;

import lk.ijse.hms.bo.custom.StudentBO;
import lk.ijse.hms.bo.exception.DuplicateException;
import lk.ijse.hms.bo.exception.InUseException;
import lk.ijse.hms.bo.util.Convertor;
import lk.ijse.hms.dao.DaoFactory;
import lk.ijse.hms.dao.DaoTypes;
import lk.ijse.hms.dao.custom.QueryDAO;
import lk.ijse.hms.dao.custom.StudentDAO;
import lk.ijse.hms.dto.StudentDTO;
import lk.ijse.hms.util.FactoryConfiguration;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/6/2023
 * Time :1:58 PM
 */

public class StudentBOImpl implements StudentBO {
    private final Convertor convertor = new Convertor();
    private final StudentDAO studentDAO = DaoFactory.getInstance().getDAO(DaoTypes.STUDENT);

    private final QueryDAO queryDAO = DaoFactory.getInstance().getDAO(DaoTypes.QUERY);
    @Override
    public List<StudentDTO> getAllStudent() {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return studentDAO.findAll(session).stream().map(student -> convertor.fromStudent(student)).collect(Collectors.toList());
        }finally {
            session.close();
        }
    }

    @Override
    public List<StudentDTO> searchBookByText(String text) {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return studentDAO.search(text, session).stream().map(student -> convertor.fromStudent(student)).collect(Collectors.toList());
        }finally {
            session.close();
        }
    }

    @Override
    public StudentDTO getStudentById(String studentId) {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return convertor.fromStudent(studentDAO.findByPk(studentId, session).get());
        }finally {
            session.close();
        }
    }

    @Override
    public boolean saveStudent(StudentDTO studentDTO) throws DuplicateException{
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            if (studentDAO.findByPk(studentDTO.getStudentId(), session).isPresent())
                throw new DuplicateException();
            studentDAO.save(convertor.toStudent(studentDTO), session);
            transaction.commit();
            return true;
        }catch (DuplicateException e){
            throw new DuplicateException();
        }catch (Exception e){
            transaction.rollback();
            return false;
        }finally {
            session.close();
        }
    }

    @Override
    public boolean updateStudent(StudentDTO studentDTO) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            studentDAO.update(convertor.toStudent(studentDTO), session);
            transaction.commit();
            return true;
        }catch (Exception e){
            transaction.rollback();
            return false;
        }finally {
            session.close();
        }
    }

    @Override
    public boolean deleteStudent(StudentDTO studentDTO) throws InUseException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            if (queryDAO.findAllUnPaidReservationDetailsByStudentId(studentDTO.getStudentId(), session).size() > 0)
                throw new InUseException();
            studentDAO.deleteByPk(studentDTO.getStudentId(), session);
            transaction.commit();
            return true;
        }catch (InUseException e){
            throw new InUseException();
        }catch (Exception e){
            transaction.rollback();
            return false;
        }finally {
            session.close();
        }
    }
}
