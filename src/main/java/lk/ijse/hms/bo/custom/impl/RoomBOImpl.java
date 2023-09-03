package lk.ijse.hms.bo.custom.impl;

import lk.ijse.hms.bo.custom.RoomBO;
import lk.ijse.hms.bo.exception.DuplicateException;
import lk.ijse.hms.bo.exception.InUseException;
import lk.ijse.hms.bo.util.Convertor;
import lk.ijse.hms.dao.DaoFactory;
import lk.ijse.hms.dao.DaoTypes;
import lk.ijse.hms.dao.custom.QueryDAO;
import lk.ijse.hms.dao.custom.ReservationDAO;
import lk.ijse.hms.dao.custom.RoomDAO;
import lk.ijse.hms.dao.custom.StudentDAO;
import lk.ijse.hms.dto.CustomDTO;
import lk.ijse.hms.dto.RoomDTO;
import lk.ijse.hms.entity.Reservation;
import lk.ijse.hms.entity.Room;
import lk.ijse.hms.entity.Student;
import lk.ijse.hms.util.FactoryConfiguration;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/6/2023
 * Time :1:58 PM
 */

public class RoomBOImpl implements RoomBO {
    private final Convertor convertor = new Convertor();
    private final RoomDAO roomDAO = DaoFactory.getInstance().getDAO(DaoTypes.ROOM);
    private final QueryDAO queryDAO = DaoFactory.getInstance().getDAO(DaoTypes.QUERY);
    private final ReservationDAO reservationDAO = DaoFactory.getInstance().getDAO(DaoTypes.RESERVATION);
    private final StudentDAO studentDAO = DaoFactory.getInstance().getDAO(DaoTypes.STUDENT);
    @Override
    public List<RoomDTO> getAllRoom() {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return roomDAO.findAll(session).stream().map(room -> convertor.fromRoom(room)).collect(Collectors.toList());
        }finally {
            session.close();
        }
    }

    @Override
    public List<RoomDTO> searchMembersByText(String text) {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return roomDAO.search(text, session).stream().map(room -> convertor.fromRoom(room)).collect(Collectors.toList());
        }finally {
            session.close();
        }
    }

    @Override
    public List<CustomDTO> getAllUnPaidReservation() {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return queryDAO.findAllUnPaidReservationDetails(session).stream().map(custom -> convertor.fromCustom(custom)).collect(Collectors.toList());
        }finally {
            session.close();
        }
    }

    @Override
    public boolean payReservationByReservationId(String reservationId, CustomDTO.Status status) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            if (reservationDAO.updateStatusByPk(reservationId, status, session)) {
                transaction.commit();
                return true;
            }
            throw new Exception();
        }catch (Exception e){
            transaction.rollback();
            return false;
        }finally {
            session.close();
        }
    }

    @Override
    public String getNextReservationId() {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            Optional<String> optional = reservationDAO.getLastPk(session);
            if (optional.isPresent()) {
                return String.format("RSV-%04d", Integer.parseInt(optional.get().substring(4))+1);
            }
            return "RSV-0001";
        }finally {
            session.close();
        }
    }

    @Override
    public RoomDTO getRoomById(String roomTypeId) {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return convertor.fromRoom(roomDAO.findByPk(roomTypeId, session).get());
        }finally {
            session.close();
        }
    }

    @Override
    public boolean addReservation(CustomDTO customDTO) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            Optional<Student> studentOptional = studentDAO.findByPk(customDTO.getStudentId(), session);
            Optional<Room> roomOptional = roomDAO.findByPk(customDTO.getRoomTypeId(), session);
            if (studentOptional.isPresent() && roomOptional.isPresent()) {
                Student student = studentOptional.get();
                Room room = roomOptional.get();
                room.setQty(room.getQty() - 1);

                Reservation reservation = new Reservation(customDTO.getReservationId(), customDTO.getDate(), student, room, customDTO.getStatus().toString());

                reservationDAO.save(reservation, session);
                roomDAO.update(room, session);
                transaction.commit();
                return true;
            }
            throw new Exception();
        }catch (Exception e){
            e.printStackTrace();
            transaction.rollback();
            return false;
        }finally {
            session.close();
        }
    }

    @Override
    public List<CustomDTO> getAllReservation() {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return queryDAO.findAllReservationDetails(session).stream().map(objects -> convertor.fromCustom(objects)).collect(Collectors.toList());
        }finally {
            session.close();
        }
    }

    @Override
    public List<CustomDTO> searchBookByText(String text) {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return queryDAO.findAllReservationDetailsByText(text, session).stream().map(objects -> convertor.fromCustom(objects)).collect(Collectors.toList());
        }finally {
            session.close();
        }
    }

    @Override
    public boolean saveRoom(RoomDTO roomDTO) throws DuplicateException{
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            if (roomDAO.findByPk(roomDTO.getRoomTypeId(), session).isPresent())
                throw new DuplicateException();
            roomDAO.save(convertor.toRoom(roomDTO), session);
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
    public boolean updateBook(RoomDTO roomDTO) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            roomDAO.update(convertor.toRoom(roomDTO), session);
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
    public boolean deleteRoom(RoomDTO roomDTO) throws InUseException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            if(queryDAO.findUnPaidReservationByRoomTypeId(roomDTO.getRoomTypeId(), session).size() > 0){
                throw new InUseException();
            }
            roomDAO.deleteByPk(roomDTO.getRoomTypeId(), session);
            transaction.commit();
            return true;
        }catch (InUseException e){
            throw new InUseException();
        }catch (Exception e){
            e.printStackTrace();
            transaction.rollback();
            return false;
        }finally {
            session.close();
        }
    }

    @Override
    public long getAllReservationCountByDate(LocalDate date) {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return reservationDAO.findAllByDate(Date.valueOf(date), session);
        }finally {
            session.close();
        }
    }
}
