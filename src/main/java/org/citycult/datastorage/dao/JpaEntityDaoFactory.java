package org.citycult.datastorage.dao;


import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaEntityDaoFactory {

    private static final JpaEntityDaoFactory instance = new JpaEntityDaoFactory();
    private final EntityManagerFactory emf;

    private JpaEntityDaoFactory() {
        emf = Persistence.createEntityManagerFactory(Constants.JPA_PRODUCTIVE_PERSISTANCEUNIT);
    }

    public static JpaEntityDaoFactory getInstance() {
        return instance;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    public JpaEventDao getEventDao() {
        return JpaEventDao.getInstance();
    }

    public JpaEventArtCultureDao getEventArtCultureDao() {
        return JpaEventArtCultureDao.getInstance();
    }

    public JpaEventCinemaDao getEventCinemaDao() {
        return JpaEventCinemaDao.getInstance();
    }

    public JpaEventLiveDao getEventLiveDao() {
        return JpaEventLiveDao.getInstance();
    }

    public JpaEventNightlifeDao getEventNightlifeDao() {
        return JpaEventNightlifeDao.getInstance();
    }

    public JpaVenueDao getVenueDao() {
        return JpaVenueDao.getInstance();
    }

    public JpaMovieDao getMovieDao() {
        return JpaMovieDao.getInstance();
    }
}
