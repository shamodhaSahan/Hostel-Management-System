package lk.ijse.hms.dao;

import lk.ijse.hms.entity.SuperEntity;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/6/2023
 * Time :9:22 AM
 */

public interface CrudDAO<T extends SuperEntity,ID extends Serializable> extends SuperDAO{
    void save(T t, Session session);

    void update(T t, Session session);

    void deleteByPk(ID pk, Session session);

    List<T> findAll(Session session);

    Optional<T> findByPk(ID pk, Session session);

    Optional<String> getLastPk(Session session);

}
