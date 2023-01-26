package org.citycult.datastorage.dao.helper;

import org.slf4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.LinkedList;
import java.util.List;

public abstract class JPADefaultDao<I, T> {

    private final Logger log;

    private final Class<? extends T> clazz;

    private final EntityManagerFactory emf;

    public JPADefaultDao(EntityManagerFactory emf, Class<? extends T> clazz, Logger log) {
        this.emf = emf;
        this.clazz = clazz;
        this.log = log;
    }

    public abstract I getIdentifier(T obj);

    public List<T> getAll() {
        List<T> obj = null;

        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            TypedQuery<? extends T> q = em.createQuery(
                    "SELECT e FROM " + clazz.getSimpleName() + " e", clazz);
            obj = new LinkedList<T>(q.getResultList());
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive())
                tx.rollback();
            log.error("getAll()", e);
            obj = null;
        } finally {
            if (em != null) {
                em.close();
            }
        }

        return obj;
    }

    public List<T> find(T obj) {
        final List<T> results = new LinkedList<T>();
        T e = get(getIdentifier(obj));
        if (e != null) {
            results.add(e);
        }
        return results;
    }

    public T get(I uid) {
        T obj = null;

        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            if (uid instanceof Integer || uid instanceof Long) {
                obj = em.find(clazz, uid);
            }
            else {
                obj = em.find(clazz, String.valueOf(uid));
            }
            if (obj != null)
                em.refresh(obj); // FIXED: Wrong caching between DB and JPA.
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive())
                tx.rollback();
            log.error("get()", e);
            obj = null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return obj;
    }

    public T insert(T obj) {
        if (obj == null)
            return null;

        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            obj = em.merge(obj);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive())
                tx.rollback();
            log.error("insert()", e);
            obj = null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return obj;
    }

    public T update(T obj) {
        if (obj == null)
            return null;

        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            obj = em.merge(obj);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive())
                tx.rollback();
            log.error("update()", e);
            obj = null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return obj;
    }

    public boolean delete(T obj) {
        if (obj == null)
            return true;

        boolean rc = true;
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            obj = em.merge(obj);
            em.remove(obj);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive())
                tx.rollback();
            log.error("delete()", e);
            rc = false;
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return rc;
    }
}
