package org.citycult.datastorage.dao;

import org.citycult.datastorage.entity.Category;
import org.citycult.datastorage.dao.helper.JpaDefaultDao;
import org.citycult.datastorage.entity.JpaMovie;
import org.citycult.datastorage.entity.JpaVenue;
import org.citycult.datastorage.util.SqlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class JpaVenueDao {

    private static final Logger log = LoggerFactory.getLogger(JpaVenueDao.class);

    private static final JpaVenueDao instance = new JpaVenueDao();
    private final EntityManagerFactory emf;
    private final DefaultDao defaultDao;

    private JpaVenueDao() {
        emf = JpaEntityDaoFactory.getInstance().getEntityManagerFactory();
        defaultDao = new DefaultDao();
    }

    public static JpaVenueDao getInstance() {
        return instance;
    }

    public List<JpaVenue> getAll() {
        return defaultDao.getAll();
    }

    public List<JpaVenue> find(JpaVenue obj) {
        return defaultDao.find(obj);
    }

    public JpaVenue get(UUID uid) {
        return defaultDao.get(uid);
    }

    public List<JpaVenue> getForCategory(Category cat) {
        List<JpaVenue> obj = null;

        String viewTable;
        switch (cat) {
            case ARTCULTURE:
                viewTable = "venue_artculture";
                break;
            case NIGHTLIFE:
                viewTable = "venue_nightlife";
                break;
            case CINEMA:
                viewTable = "venue_cinema";
                break;
            case SPORT:
                log.warn("Requesting sport venues is not supported! Using venue instead.");
                viewTable = "venue";
                break;
            case LIVE:
                viewTable = "venue_live";
                break;
            case MISC:
                viewTable = "venue";
                break;
            default:
                viewTable = "venue";
                break;
        }

        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();

            StringBuilder query = new StringBuilder();
            query.append(SqlHelper.SELECT_).append("*");
            query.append(SqlHelper._FROM_).append(viewTable);

            Query q = em.createNativeQuery(query.toString(), JpaVenue.class);
            obj = new LinkedList<JpaVenue>(q.getResultList());

            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive())
                tx.rollback();
            log.error("getForCategory()", e);
        } finally {
            if (em != null)
                em.close();
        }

        return obj;
    }

    public List<JpaVenue> getForMovie(JpaMovie movie) {
        if (movie == null)
            return null;

        List<String> venueUids = null;

        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            // TODO Usage of JPA query
            StringBuilder subQuery = new StringBuilder();
            subQuery.append(SqlHelper.SELECT_).append("event_uid");
            subQuery.append(SqlHelper._FROM_).append("event_cinema");
            // NOTE: Named parameters are not supported for native queries.
            subQuery.append(SqlHelper._WHERE_).append("movie_uid=?");

            StringBuilder query = new StringBuilder();
            query.append(SqlHelper.SELECT_DISTINCT_).append("venue_uid");
            query.append(SqlHelper._FROM_).append("(").append(subQuery)
                    .append(") AS sb");
            query.append(SqlHelper._JOIN_).append("event");
            query.append(SqlHelper.getJoinUsing("event_uid"));

            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();

            Query q = em.createNativeQuery(query.toString());
            q.setParameter(1, movie.getMovieUid().toString());

            venueUids = q.getResultList();

            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive())
                tx.rollback();
            log.error("getForMovie()", e);
        } finally {
            if (em != null)
                em.close();
        }


        if (venueUids == null) {
            log.error("Empty venue list!");
            return null;
        }

        List<JpaVenue> venues = new LinkedList<JpaVenue>();
        JpaVenue venue;
        UUID uid;
        for (String sUid : venueUids) {
            try {
                uid = UUID.fromString(sUid);
            } catch (Exception e) {
                continue;
            }
            venue = this.get(uid);
            if (venue != null)
                venues.add(venue);
        }
        return venues;
    }

    public JpaVenue insert(JpaVenue obj) {
        return defaultDao.insert(obj);
    }

    public JpaVenue update(JpaVenue obj) {
        return defaultDao.update(obj);
    }

    public boolean delete(JpaVenue obj) {
        return defaultDao.delete(obj);
    }

    private class DefaultDao extends JpaDefaultDao<UUID, JpaVenue> {

        public DefaultDao() {
            super(emf, JpaVenue.class, log);
        }

        @Override
        public UUID getIdentifier(JpaVenue obj) {
            if (obj != null)
                return obj.getVenueUid();
            else
                return null;
        }
    }
}
