package org.citycult.datastorage.dao;

import org.citycult.datastorage.dao.helper.JpaDefaultDao;
import org.citycult.datastorage.entity.JpaMovie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.UUID;

public class JpaMovieDao {

    private static final Logger log = LoggerFactory.getLogger(JpaMovieDao.class);

    private static final JpaMovieDao instance = new JpaMovieDao();

    public static JpaMovieDao getInstance() {
        return instance;
    }

    private final EntityManagerFactory emf;

    private final DefaultDao defaultDao;

    private JpaMovieDao() {
        emf = JpaEntityDaoFactory.getInstance().getEntityManagerFactory();
        defaultDao = new DefaultDao();
    }

    public List<JpaMovie> getAll() {
        return defaultDao.getAll();
    }

    public List<JpaMovie> find(JpaMovie obj) {
        return defaultDao.find(obj);
    }

    public JpaMovie get(UUID uid) {
        return defaultDao.get(uid);
    }

    public JpaMovie insert(JpaMovie obj) {
        return defaultDao.insert(obj);
    }

    public JpaMovie update(JpaMovie obj) {
        return defaultDao.update(obj);
    }

    public boolean delete(JpaMovie obj) {
        return defaultDao.delete(obj);
    }

    private class DefaultDao extends JpaDefaultDao<UUID, JpaMovie> {

        public DefaultDao() {
            super(emf, JpaMovie.class, log);
        }

        @Override
        public UUID getIdentifier(JpaMovie obj) {
            if (obj != null)
                return obj.getMovieUid();
            else
                return null;
        }
    }
}
