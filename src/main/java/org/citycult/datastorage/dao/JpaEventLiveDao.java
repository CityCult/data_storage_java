package org.citycult.datastorage.dao;

import org.citycult.datastorage.dao.helper.JpaEventCategoryDao;
import org.citycult.datastorage.entity.JpaEventLive;
import org.citycult.datastorage.entity.JpaVenue;
import org.citycult.datastorage.util.DateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author cpieloth
 */
public class JpaEventLiveDao {

    private static final Logger log = LoggerFactory.getLogger(JpaEventLiveDao.class);

    private final JpaEventCategoryDao<JpaEventLive> dao;

    private static JpaEventLiveDao instance = new JpaEventLiveDao();

    public static JpaEventLiveDao getInstance() {
        return instance;
    }

    private JpaEventLiveDao() {
        dao = new JpaEventCategoryDao<JpaEventLive>(JpaEventLive.class, log);
    }

    public JpaEventLive get(UUID uid) {
        return dao.get(uid);
    }

    public List<JpaEventLive> getForVenue(JpaVenue venue) {
        return dao.getForVenue(venue);
    }

    public List<JpaEventLive> getForVenue(JpaVenue venue, DateHelper.DateRange range) {
        return dao.getForVenue(venue, range);
    }

    public List<JpaEventLive> getDate(DateHelper.DateRange range) {
        return dao.getDate(range);
    }

    public List<JpaEventLive> getDate(Date start, Date end) {
        return dao.getDate(start, end);
    }

    public List<JpaEventLive> getAll() {
        return dao.getAll();
    }

    public List<JpaEventLive> find(JpaEventLive obj) {
        return dao.find(obj);
    }

    public JpaEventLive insert(JpaEventLive obj) {
        return dao.insert(obj);
    }

    public JpaEventLive update(JpaEventLive obj) {
        return dao.update(obj);
    }

    public boolean delete(JpaEventLive obj) {
        return dao.delete(obj);
    }
}
