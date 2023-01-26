package org.citycult.datastorage.dao.helper;

import org.citycult.datastorage.entity.Category;
import org.citycult.datastorage.dao.JpaEntityDaoFactory;
import org.citycult.datastorage.entity.JpaEvent;
import org.citycult.datastorage.entity.JpaVenue;
import org.citycult.datastorage.util.DateHelper;
import org.citycult.datastorage.util.SqlHelper;
import org.slf4j.Logger;

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
public class JPAEventCategoryDao<EVENT extends JpaEvent> {

    private final Logger log;

    private final JPADefaultDao<UUID, EVENT> defaultDAO;

    private final DateableDAO dateableDAO;

    private final EntityManagerFactory emf;

    private final Category cat;

    public JPAEventCategoryDao(Class<? extends EVENT> clazz, Logger log) {
        this.log = log;
        this.cat = JpaEventClassHelper.getCategory(clazz);
        emf = JpaEntityDaoFactory.getInstance().getEntityManagerFactory();
        defaultDAO = new DefaultDAO(clazz);
        dateableDAO = new DateableDAO(clazz);
    }

    public List<EVENT> getAll() {
        return defaultDAO.getAll();
    }

    public List<EVENT> find(EVENT obj) {
        return defaultDAO.find(obj);
    }

    public EVENT insert(EVENT obj) {
        return defaultDAO.insert(obj);
    }

    public EVENT update(EVENT obj) {
        return defaultDAO.update(obj);
    }

    public boolean delete(EVENT obj) {
        return defaultDAO.delete(obj);
    }

    public EVENT get(UUID uid) {
        return defaultDAO.get(uid);
    }

    public List<EVENT> getForVenue(JpaVenue venue) {
        final DateHelper.DateRange range = new DateHelper.DateRange(DateHelper.MIN_DATE, DateHelper.MAX_DATE);
        return getForVenue(venue, range);
    }

    public List<EVENT> getForVenue(JpaVenue venue, DateHelper.DateRange range) {
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
            query.append("category_id=" + cat.id);
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
        List<EVENT> entities = new LinkedList<EVENT>();
        for (Object o : uuids) {
            final String strUid = o.toString();
            final UUID uid = UUID.fromString(strUid);
            final EVENT e = get(uid);
            if (e != null)
                entities.add(e);
        }

        return entities;
    }

    public List<EVENT> getDate(DateHelper.DateRange range) {
        return dateableDAO.getDate(range);
    }

    public List<EVENT> getDate(Date start, Date end) {
        return dateableDAO.getDate(start, end);
    }

    private class DefaultDAO extends JPADefaultDao<UUID, EVENT> {

        public DefaultDAO(Class<? extends EVENT> clazz) {
            super(emf, clazz, log);
        }

        @Override
        public UUID getIdentifier(EVENT obj) {
            if (obj != null)
                return obj.getEventUid();
            else
                return null;
        }
    }

    private class DateableDAO extends JPAEventDateableDao<EVENT> {

        public DateableDAO(Class<? extends EVENT> clazz) {
            super(emf, clazz, log);
        }

    }
}
