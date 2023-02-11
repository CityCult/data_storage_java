package org.citycult.datastorage.dao;

import org.citycult.datastorage.dao.helper.JpaEventCategoryDao;
import org.citycult.datastorage.entity.JpaEventCinema;
import org.citycult.datastorage.entity.JpaMovie;
import org.citycult.datastorage.entity.JpaVenue;
import org.citycult.datastorage.util.DateHelper;
import org.citycult.datastorage.util.SqlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * @author cpieloth
 */
public class JpaEventCinemaDao {

    private static final Logger log = LoggerFactory.getLogger(JpaEventCinemaDao.class);

    private static JpaEventCinemaDao instance = new JpaEventCinemaDao();

    public static JpaEventCinemaDao getInstance() {
        return instance;
    }

    private final EntityManagerFactory emf;

    private final JpaEventCategoryDao<JpaEventCinema> dao;

    private JpaEventCinemaDao() {
        dao = new JpaEventCategoryDao<>(JpaEventCinema.class, log);
        emf = JpaEntityDaoFactory.getInstance().getEntityManagerFactory();
    }

    public List<JpaEventCinema> getAll() {
        return dao.getAll();
    }

    public List<JpaEventCinema> find(JpaEventCinema obj) {
        return dao.find(obj);
    }

    public JpaEventCinema insert(JpaEventCinema obj) {
        return dao.insert(obj);
    }

    public JpaEventCinema update(JpaEventCinema obj) {
        return dao.update(obj);
    }

    public boolean delete(JpaEventCinema obj) {
        return dao.delete(obj);
    }

    public JpaEventCinema get(UUID uid) {
        return dao.get(uid);
    }

    public List<JpaEventCinema> getForVenue(JpaVenue venue) {
        return dao.getForVenue(venue);
    }

    public List<JpaEventCinema> getForVenue(JpaVenue venue, DateHelper.DateRange range) {
        return dao.getForVenue(venue, range);
    }

    public List<JpaEventCinema> getDate(DateHelper.DateRange range) {
        return dao.getDate(range);
    }

    public List<JpaEventCinema> getDate(Date start, Date end) {
        return dao.getDate(start, end);
    }

    public List<JpaEventCinema> getForMovie(JpaMovie movie) {
        final DateHelper.DateRange range = new DateHelper.DateRange(DateHelper.MIN_DATE, DateHelper.MAX_DATE);
        return getForMovie(movie, range);
    }

    public List<JpaEventCinema> getForMovie(JpaMovie movie, DateHelper.DateRange date) {
        if (movie == null || date == null) {
            log.error("Movie or DateRange is null!");
            return null;
        }

        List<JpaEventCinema> obj = null;

        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            // TODO Usage of JPA query
            StringBuilder subQuery = new StringBuilder();
            subQuery.append(SqlHelper.SELECT_).append("*");
            subQuery.append(SqlHelper._FROM_).append("event_cinema");
            // NOTE: Named parameters are not supported for native queries.
            subQuery.append(SqlHelper._WHERE_).append("movie_uid=?");

            StringBuilder query = new StringBuilder();
            query.append(SqlHelper.SELECT_).append("*");
            query.append(SqlHelper._FROM_).append("(").append(subQuery)
                    .append(") AS sb");
            query.append(SqlHelper._JOIN_).append("event");
            query.append(SqlHelper.getJoinUsing("event_uid"));
            query.append(SqlHelper._WHERE_).append(
                    SqlHelper.getDateRangeCondition(date, "startdate",
                            "enddate"));

            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();

            Query q = em.createNativeQuery(query.toString(), JpaEventCinema.class);
            q.setParameter(1, movie.getMovieUid().toString());

            obj = new LinkedList<JpaEventCinema>(q.getResultList());

            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive())
                tx.rollback();
            log.error("getForMovie()", e);
        } finally {
            if (em != null)
                em.close();
        }

        return obj;
    }

}
