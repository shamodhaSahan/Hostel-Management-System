package lk.ijse.hms.bo.custom.impl;

import lk.ijse.hms.bo.custom.LoginBO;
import lk.ijse.hms.bo.exception.DuplicateException;
import lk.ijse.hms.bo.exception.NotFoundException;
import lk.ijse.hms.bo.util.Convertor;
import lk.ijse.hms.dao.DaoFactory;
import lk.ijse.hms.dao.DaoTypes;
import lk.ijse.hms.dao.custom.UserDAO;
import lk.ijse.hms.dto.UserDTO;
import lk.ijse.hms.entity.User;
import lk.ijse.hms.util.FactoryConfiguration;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/6/2023
 * Time :1:58 PM
 */

public class LoginBOImpl implements LoginBO {
    private final UserDAO userDAO = DaoFactory.getInstance().getDAO(DaoTypes.USER);
    private final Convertor convertor = new Convertor();
    @Override
    public UserDTO verifyUser(UserDTO userDTO) throws NotFoundException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Optional<User> optional = userDAO.findByUserName(userDTO.getUserName(), session);
        session.close();
        if (optional.isPresent()){
            if (optional.get().getPassword().equals(userDTO.getPassword())){
                return convertor.fromUser(optional.get());
            }
        }
        throw new NotFoundException("user name or password is wrong ..!");
    }

    @Override
    public List<UserDTO> getAllUser() {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return userDAO.findAll(session).stream().map(user -> convertor.fromUser(user)).collect(Collectors.toList());
        }finally {
            session.close();
        }
    }

    @Override
    public List<UserDTO> searchUserByText(String text) {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return userDAO.search(text, session).stream().map(user -> convertor.fromUser(user)).collect(Collectors.toList());
        }finally {
            session.close();
        }
    }

    @Override
    public String getNextUserId() {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            Optional<String> optional = userDAO.getLastPk(session);
            if (optional.isPresent()) {
                return String.format("U-%04d", Integer.parseInt(optional.get().substring(2))+1);
            }
            return "U-0001";
        }finally {
            session.close();
        }
    }

    @Override
    public boolean saveUser(UserDTO userDTO) throws DuplicateException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            if (userDAO.findByUserName(userDTO.getUserName(), session).isPresent())
                throw new DuplicateException();
            userDAO.save(convertor.toUser(userDTO), session);
            transaction.commit();
            return true;
        }catch (DuplicateException e){
            throw new DuplicateException();
        }catch (Exception e){
            e.printStackTrace();
            transaction.rollback();
            return false;
        }finally {
            session.close();
        }
    }

    @Override
    public boolean updateUser(UserDTO userDTO) throws DuplicateException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            Optional<User> optionalUser = userDAO.findByUserName(userDTO.getUserName(), session);
            if (optionalUser.isPresent() && (!optionalUser.get().getUserId().equals(userDTO.getUserId())))
                throw new DuplicateException();
            userDAO.update(convertor.toUser(userDTO), session);
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
    public boolean deleteUser(UserDTO userDTO) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            userDAO.deleteByPk(userDTO.getUserId(), session);
            transaction.commit();
            return true;
        }catch (Exception e){
            transaction.rollback();
            return false;
        }finally {
            session.close();
        }
    }
}
