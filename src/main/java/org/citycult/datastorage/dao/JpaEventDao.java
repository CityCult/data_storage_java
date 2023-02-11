package org.citycult.datastorage.dao;

import org.citycult.datastorage.dao.helper.JpaDefaultDao;
import org.citycult.datastorage.dao.helper.JpaEventDateableDao;
import org.citycult.datastorage.entity.JpaEvent;
import org.citycult.datastorage.entity.JpaVenue;
import org.citycult.datastorage.util.DateHelper;
import org.citycult.datastorage.util.DateHelper.DateRange;
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

public class JpaEventDao {

    private static final Logger log = LoggerFactory.getLogger(JpaEventDao.class);

    private static final JpaEventDao instance = new JpaEventDao();
    private final EntityManagerFactory emf;
    private final DefaultDao defaultDao;
    private final DateableDao dateableDao;
    private final JpaVenueDao venueDao;

    private JpaEventDao() {
        emf = JpaEntityDaoFactory.getInstance().getEntityManagerFactory();
        defaultDao = new DefaultDao();
        dateableDao = new DateableDao();
        venueDao = JpaVenueDao.getInstance();
    }

    public static JpaEventDao getInstance() {
        return instance;
    }

    public List<JpaEvent> getAll() {
        return defaultDao.getAll();
    }

    public List<JpaEvent> find(JpaEvent obj) {
        return defaultDao.find(obj);
    }

    public JpaEvent get(UUID uid) {
        return defaultDao.get(uid);
    }

    public List<JpaEvent> getForVenue(JpaVenue venue) {
        DateRange range = new DateRange(DateHelper.MIN_DATE, DateHelper.MAX_DATE);
        return getForVenue(venue, range);
    }

    public List<JpaEvent> getForVenue(JpaVenue venue, DateRange range) {
        // TODO(cpieloth): Use JPA Query Language to remove double queries UUID->Event.
        if (venue == null || range == null) {
            log.error("Venue or DateRange is null!");
            return null;
        }

        List<Object> uuids = null;

        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            final StringBuilder query = new StringBuilder();
            query.append(SqlHelper.selectFromWhere("event_uid", "event", "venue_uid='" + venue.getVenueUid() + "'"));
            query.append(SqlHelper._AND_);
            query.append(SqlHelper.getDateRangeCondition(range, "startdate", "enddate"));

            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();

            Query q = em.createNativeQuery(query.toString());
            uuids = q.getResultList();

            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive())
                tx.rollback();
            log.error("getForVenue()", e);
        } finally {
            if (em != null)
                em.close();
        }
        if (uuids == null) {
            return null;
        }

        // Get JPA entities, to get correct types and all attributes for
        // sub-types.
        List<JpaEvent> entities = new LinkedList<JpaEvent>();
        for (Object o : uuids) {
            final String strUid = o.toString();
            final UUID uid = UUID.fromString(strUid);
            final JpaEvent e = get(uid);
            if (e != null)
                entities.add(e);
        }

        return entities;
    }

    public JpaEvent insert(JpaEvent obj) {
        return defaultDao.insert(obj);
    }

    public JpaEvent update(JpaEvent obj) {
        return defaultDao.update(obj);
    }

    public boolean delete(JpaEvent obj) {
        return defaultDao.delete(obj);
    }

    public List<JpaEvent> getDate(DateHelper.DateRange range) {
        return dateableDao.getDate(range);
    }

    public List<JpaEvent> getDate(Date start, Date end) {
        return dateableDao.getDate(start, end);
    }

    private class DefaultDao extends JpaDefaultDao<UUID, JpaEvent> {

        public DefaultDao() {
            super(emf, JpaEvent.class, log);
        }

        @Override
        public UUID getIdentifier(JpaEvent obj) {
            if (obj != null)
                return obj.getEventUid();
            else
                return null;
        }
    }

    private class DateableDao extends JpaEventDateableDao<JpaEvent> {

        public DateableDao() {
            super(emf, JpaEvent.class, log);
        }

    }
}
