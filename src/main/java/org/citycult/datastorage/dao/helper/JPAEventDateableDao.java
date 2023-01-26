package org.citycult.datastorage.dao.helper;

import org.citycult.datastorage.entity.JpaEvent;
import org.citycult.datastorage.util.DateHelper;
import org.citycult.datastorage.util.JpaQueryHelper;
import org.slf4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author cpieloth
 */
public abstract class JPAEventDateableDao<T extends JpaEvent> {

    private final Logger log;

    private final Class<? extends T> clazz;

    private final String jpaTable;

    private final EntityManagerFactory emf;

    public JPAEventDateableDao(EntityManagerFactory emf, Class<? extends T> clazz, Logger log) {
        this.emf = emf;
        this.clazz = clazz;
        this.log = log;
        this.jpaTable = clazz.getSimpleName();
    }

    public List<T> getDate(DateHelper.DateRange range) {
        if (range == null) {
            log.error("DateRange is null!");
            return null;
        }
        return getDate(range.getStart(), range.getEnd());
    }

    public List<T> getDate(Date start, Date end) {
        if (start == null || end == null) {
            log.error("Date is null (start and/or end)!");
            return null;
        }

        List<T> obj = null;

        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();

            final StringBuilder query = new StringBuilder();
            JpaQueryHelper.selectFromWhere(query, "e", jpaTable + " e", "");
            JpaQueryHelper.getDateRangeCondition(query, "e.startDate", "e.endDate");

            TypedQuery<? extends T> q = em.createQuery(query.toString(), clazz);
            q.setParameter(JpaQueryHelper.PARAM_DATA_START, start);
            q.setParameter(JpaQueryHelper.PARAM_DATA_END, end);

            obj = new LinkedList<T>(q.getResultList());
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive())
                tx.rollback();
            log.error("getDate()", e);
            obj = null;
        } finally {
            if (em != null)
                em.close();
        }

        return obj;
    }
}
